package com.proquest.demo.allinone;

/**
 *
 * Adding all close and delete to clear more memory on the APDFL side
 *
 * @author kvillaca
 */

import com.datalogics.PDFL.*;
import com.proquest.demo.enums.MessagesKeys;
import com.proquest.demo.enums.PdfLibraryKeys;
import com.proquest.demo.enums.PropertyFileNames;
import com.proquest.demo.pojos.GenericData;
import com.proquest.demo.properties.PropertyReaderLib;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 */
public class PDFLBase {

    private static final String COMPONENT_NAME = PDFLBase.class.getName();

    private final String ENVIRONMENT_VARIABLE_FOR_APDFL = "APDFL_PATH";
    private final String APDFL_PATH_RESOURCES = "Resources";

    private Library lib = null;


    /**
     * Initialize the library
     */
    public void initLibrary() {
        if (this.lib != null) {
            terminateLibrary();
        }


        this.lib = new Library();
    }




    public Library getLib() {
        return lib;
    }


    /**
     * Initialize in memory system
     */
    public void initInMemory(final int sizeInKb) {
        final int PERCENTAGE_DO_BE_ADDED = 30;
        // For the memory alloc after created, I am adding some % for more space, maybe more space will be needed later
        final int memoryUsageAfterExtraPercentage = ((sizeInKb / 100) * PERCENTAGE_DO_BE_ADDED) + sizeInKb;
        lib.setDefaultTempStore(TempStoreType.MEMORY);
        lib.setDefaultTempStoreMemLimit(memoryUsageAfterExtraPercentage);
    }


    /**
     * Initialize in memory system
     */
    public void initDisk() {
        lib.setDefaultTempStore(TempStoreType.DISK);
    }


    /**
     * Terminate the library, dealloc it.
     */
    public void terminateLibrary() {
        if (this.lib != null) {
            this.lib.terminate();
            this.lib.delete();
            this.lib = null;
        }
    }


    /**
     * Linearize a given document.
     *
     * @param doc
     * @param outputFile
     * @return
     */
    public Document linearize(final Document doc, final String outputFile) {
        doc.setRequiresFullSave(true);
        doc.save(EnumSet.of(SaveFlags.FULL, SaveFlags.LINEARIZED), outputFile);
        return doc;
    }

    /**
     * Save a document by a given set of saveFlags.
     *
     * @param doc
     * @param outputFile
     * @param saveFlags
     * @return
     */
    public Document save(final Document doc, final String outputFile, final EnumSet<SaveFlags> saveFlags) {
        doc.setRequiresFullSave(true);
        doc.save(saveFlags, outputFile);
        return doc;
    }

    /**
     * Add copyright notice at bottom of the page
     *
     * @param doc
     * @param copyrightText
     * @return
     */
    public Document addCopyrightNotice(final Document doc, final String copyrightText) {
        final String BLANK_SPACE = " ";
        final PropertyReaderLib propertyReaderLib = new PropertyReaderLib(PropertyFileNames.PDFLIBRARY);
        final Font font = new Font(propertyReaderLib.getPropertyValue(PdfLibraryKeys.COPYRIGHT_FONT.toString()));
        final double fontSize = Double.parseDouble(propertyReaderLib.getPropertyValue(PdfLibraryKeys.COPYRIGHT_FONT_MIN_SIZE.toString()));
        final double pageMargin = Double.parseDouble(propertyReaderLib.getPropertyValue(PdfLibraryKeys.COPYRIGHT_PAGE_MARGIN.toString()));
        final Page page = doc.getPage(doc.getNumPages() - 1);
        final List<String> rows = new ArrayList<String>();

        String textStr = null;

        if (copyrightText != null && !copyrightText.isEmpty()) {
            textStr = copyrightText;
        } else {
            final String defaultCopyrightText = propertyReaderLib.getPropertyValue(PdfLibraryKeys.DEFAULT_COPYRIGHT_TEXT.toString());
            textStr = defaultCopyrightText;
        }

        final String[] wordsArray = textStr.split(BLANK_SPACE);
        textStr = null;

        wrapTextCheck(rows, null, page, wordsArray, 0, font, fontSize, pageMargin);
        addTextToBottom(rows, page, font, fontSize, pageMargin);

        page.delete();
        font.delete();
        return doc;
    }

    /**
     * Add copyright page
     *
     * @param doc
     * @param copyrightText
     * @return
     */
    public Document addCopyrightPage(final Document doc, final String copyrightText) {
        final String BLANK_SPACE = " ";
        final PropertyReaderLib propertyReaderLib = new PropertyReaderLib(PropertyFileNames.PDFLIBRARY);
        final Font font = new Font(propertyReaderLib.getPropertyValue(PdfLibraryKeys.COPYRIGHT_FONT.toString()));
        final double fontSize = Double.parseDouble(propertyReaderLib.getPropertyValue(PdfLibraryKeys.COPYRIGHT_FONT_SIZE.toString()));
        final double pageMargin = Double.parseDouble(propertyReaderLib.getPropertyValue(PdfLibraryKeys.COPYRIGHT_PAGE_MARGIN.toString()));
        final Rect pageRect = doc.getPage(doc.getNumPages() - 1).getMediaBox();
        final Page copyrightPage = doc.createPage(doc.getNumPages() - 1, pageRect);
        final List<String> rows = new ArrayList<String>();
        final List<Double> rowXPos = new ArrayList<Double>();
        String textStr = null;

        if (copyrightText != null && !copyrightText.isEmpty()) {
            textStr = copyrightText;
        } else {
            final String defaultCopyrightText = propertyReaderLib.getPropertyValue(PdfLibraryKeys.DEFAULT_COPYRIGHT_TEXT.toString());
            textStr = defaultCopyrightText;
        }

        final String[] wordsArray = textStr.split(BLANK_SPACE);
        textStr = null;

        wrapTextCheck(rows, rowXPos, copyrightPage, wordsArray, 0, font, fontSize, pageMargin);
        addTextToPage(rows, rowXPos, copyrightPage, font, fontSize);

        copyrightPage.delete();
        pageRect.delete();
        font.delete();
        return doc;
    }

    /**
     * remove specified pages
     *
     * @param doc
     * @param pages
     * @param propertyReaderLib
     * @return
     */
    public Document removePages(final Document doc, final String pages, final PropertyReaderLib propertyReaderLib) throws Exception {
        final int numberOfPages = doc.getNumPages();
        final int[] pageArrayToBeRemoved = getPagesToBeRemoved(pages, numberOfPages);
        for (int pageToRemove : pageArrayToBeRemoved) {
            if (pageToRemove > numberOfPages) {
                throw new Exception(propertyReaderLib.getPropertyValue(MessagesKeys.PAGE_TO_REMOVE_IS_BIGGER_THEN_MAX_PAGES_NUMBER.toString()));
            } else {
                doc.deletePages(pageToRemove, pageToRemove);
                if (doc.getNumPages() == 0) {
                    throw new Exception(propertyReaderLib.getPropertyValue(MessagesKeys.PDF_CANNOT_HAVE_ITS_ONLY_PAGE_REMOVED.toString()));
                }
            }
        }
        return doc;
    }

    /**
     * Extract text from a given document.
     *
     * @param doc
     * @return
     */
    public byte[] extractText(final Document doc,
                              final GenericData tempGenericData,
                              final PropertyReaderLib propertyReaderLib) throws Exception {
        final String BLANK_SPACE = " ";
        final String UTF_8 = "UTF-8";
        final PropertyReaderLib libPropertyReaderLib = new PropertyReaderLib(PropertyFileNames.PDFLIBRARY);
        final String regex = libPropertyReaderLib.getPropertyValue(PdfLibraryKeys.REGEX_TO_REMOVE_FROM_EXTRACTEDTEXT.toString());

        final WordFinder wordFinder = getWordFinder(doc);

        final int nPages = doc.getNumPages();
        List<Word> pageWords = null;
        byte[] textAsBytes = null;
        final StringBuilder textToExtract = new StringBuilder();

        try {
            if (wordFinder != null) {
                for (int i = 0; i < nPages; i++) {
                    pageWords = wordFinder.getWordList(i);
                    if (pageWords != null && pageWords.size() > 0) {
                        for (Word tempWord : pageWords) {
                            textToExtract.append(tempWord.getText());
                            if (tempWord.getAttributes().contains(WordAttributeFlags.ADJACENT_TO_SPACE) ||
                                    tempWord.getIsLastWordInRegion()) {
                                textToExtract.append(BLANK_SPACE);
                            }
                        }
                        for (Word tempWord : pageWords) {
                            tempWord.delete(); // Do you have to do this???
                        }
                    }
                }
                pageWords.clear(); // or just this would be enough???

                final String textStr = new String(textToExtract.toString().getBytes(UTF_8)).
                        replaceAll(regex, BLANK_SPACE);
                textAsBytes = textStr.getBytes();
            }
            pageWords = null;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            wordFinder.delete();
        }
        return textAsBytes;
    }


    public WordFinder getWordFinder(final Document doc) {
        final WordFinderConfig wordConfig = new WordFinderConfig();
        wordConfig.setIgnoreCharGaps(false);
        wordConfig.setIgnoreLineGaps(false);
        wordConfig.setNoAnnots(false);
        wordConfig.setNoEncodingGuess(false);

        // Std Roman treatment for custom encoding; overrides the noEncodingGuess option
        wordConfig.setUnknownToStdEnc(false);
        wordConfig.setDisableTaggedPDF(false);  // legacy mode WordFinder creation
        wordConfig.setPreserveSpaces(false);
        wordConfig.setNoLigatureExp(false);
        wordConfig.setNoHyphenDetection(false);
        wordConfig.setTrustNBSpace(false);
        wordConfig.setNoExtCharOffset(false);       // text extraction efficiency
        wordConfig.setNoStyleInfo(false);           // text extraction efficiency

        wordConfig.setNoXYSort(true);
        final WordFinder tempWordFinder = new WordFinder(doc, WordFinderVersion.LATEST, wordConfig);
        return tempWordFinder;
    }


    /**
     * Extract Text using PDF BOX, instead APDFL
     *
     * @param fileNamePath
     * @return
     * @throws Exception
     */
    public byte[] extractTextPDFBox(final String fileNamePath) throws Exception {
        final String BLANK_SPACE = " ";
        final String UTF_8 = "UTF-8";
        final PropertyReaderLib libPropertyReaderLib = new PropertyReaderLib(PropertyFileNames.PDFLIBRARY);
        final String regex = libPropertyReaderLib.getPropertyValue(PdfLibraryKeys.REGEX_TO_REMOVE_FROM_EXTRACTEDTEXT.toString());

        byte[] bytesToReturn = null;
        try {
            final File file = new File(fileNamePath);
            final PDDocument pdfDoc =  PDDocument.load(file);
            final PDFTextStripper pdfStripper = new PDFTextStripper();
            final String textFromPDF = pdfStripper.getText(pdfDoc);
            pdfDoc.close();

            bytesToReturn = textFromPDF.getBytes(UTF_8);
            final String textStr = new String(bytesToReturn).replaceAll(regex, BLANK_SPACE);
            bytesToReturn = textStr.getBytes();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return bytesToReturn;
    }



    public void enableLicensedBehavior() {
        //this is enable license for the Edit bypass from Datalogics
        Library.enableLicensedBehavior("MK3gSXd1iubF1MsFs54oaNEOZ28rlqVJIF2jdunhkxE=");
    }

    /**
     * remove protection from a given document.
     *
     * @param doc
     * @return
     */
    public Document removeProtection(final Document doc) {
        doc.unsecure();
        final String fileName = doc.getFileName();
        if (doc.getIsLinearized()) {
            linearize(doc, fileName);
        } else {
            save(doc, fileName, EnumSet.of(SaveFlags.FULL));
        }
        doc.close();
        doc.delete();
        final Document newDoc = new Document(fileName);
        return newDoc;
    }

    public Document newDocument(final String fileName) {
        return new Document(fileName);
    }

    /**
     * Add text to the page
     *
     * @param rows
     * @param rowXPosList
     * @param page
     * @param font
     * @param fontSize
     */
    private void addTextToPage(final List<String> rows,
                               final List<Double> rowXPosList,
                               final Page page,
                               final Font font,
                               final double fontSize) {
        final Text text = new Text();
        final GraphicState gs = new GraphicState();
        final TextState ts = new TextState();
        ts.setTextRise(1.5); //setting the Text Rise

        double rowYPos = page.getMediaBox().getHeight() / 2 + ((ts.getTextRise() * fontSize) * rows.size()) / 4;

        int i = 0;
        for (String row : rows) {
            rowYPos = rowYPos - (ts.getTextRise() * fontSize);

            final Matrix m = new Matrix().translate(rowXPosList.get(i), rowYPos).scale(fontSize, fontSize);
            final TextRun textRun = new TextRun(row, font, gs, ts, m);

            text.addRun(textRun);
            textRun.delete();
            m.delete();

            page.getContent().addElement(text);
            page.updateContent();
            i++;
        }
        ts.delete();
        gs.delete();
        text.delete();
    }

    /**
     * Add text to the bottom
     *
     * @param rows
     * @param page
     * @param font
     * @param fontSize
     * @param pageMargin
     */
    private void addTextToBottom(final List<String> rows,
                                 final Page page,
                                 final Font font,
                                 final double fontSize,
                                 final double pageMargin) {
        final Text text = new Text();
        final GraphicState gs = new GraphicState();
        final TextState ts = new TextState();
        ts.setTextRise(0.5); //setting the Text Rise

        final Rect mediaRect = page.getMediaBox();
        final Rect cropRect = page.getCropBox();
        final double newBottom = -(ts.getTextRise() * fontSize * rows.size() * 2) - pageMargin;

        mediaRect.setBottom(newBottom);
        cropRect.setBottom(newBottom);
        page.setMediaBox(mediaRect);
        page.setCropBox(cropRect);

        double rowYPos = -4.0;

        for (String row : rows) {
            rowYPos = rowYPos - (ts.getTextRise() * fontSize * 2);

            final Matrix m = new Matrix().translate(pageMargin, rowYPos).scale(fontSize, fontSize);
            final TextRun textRun = new TextRun(row, font, gs, ts, m);

            text.addRun(textRun);

            page.getContent().addElement(text);
            page.updateContent();
        }

        cropRect.delete();
        mediaRect.delete();
        ts.delete();
        gs.delete();
        text.delete();
    }

    /**
     * Wrap the Text Check
     *
     * @param rows
     * @param rowXPos
     * @param page
     * @param wordsArray
     * @param textStrIndex
     * @param font
     * @param fontSize
     * @param pageMargin
     */
    private void wrapTextCheck(final List<String> rows,
                               final List<Double> rowXPos,
                               final Page page,
                               final String[] wordsArray,
                               int textStrIndex,
                               final Font font,
                               final double fontSize,
                               final double pageMargin) {
        final double pageWidth = page.getMediaBox().getWidth();
        final double x = pageMargin;
        final String BLANK_SPACE = " ";

        final GraphicState gs = new GraphicState();
        final TextState ts = new TextState();
        ts.setTextRise(1.0); //setting the Text Rise

        TextRun textRun = null;
        int wordCounter = textStrIndex + 1;
        double textWidth = 0.0;
        double rightPos = 0.0;
        String row = null;
        final StringBuilder strBuilder = new StringBuilder(wordsArray[textStrIndex]);

        do {
            if (textRun != null) {
                rightPos = x + (textRun.getAdvance() * fontSize);
                textRun.delete();
            }
            row = strBuilder.toString();
            if (wordCounter < wordsArray.length) {
                strBuilder.append(BLANK_SPACE).append(wordsArray[wordCounter]);
            }

            final Matrix m = new Matrix().translate(x, 0).scale(fontSize, fontSize);
            textRun = new TextRun(strBuilder.toString(), font, gs, ts, m);
            textWidth = x + (textRun.getAdvance() * fontSize);
            m.delete();

            wordCounter++;
        } while (textWidth < (pageWidth - pageMargin) && wordCounter < wordsArray.length);

        final boolean overFlow = textWidth > (pageWidth - pageMargin);

        if (wordCounter >= wordsArray.length && !overFlow) {
            row = strBuilder.toString();
            rightPos = x + (textRun.getAdvance() * fontSize);
        }

        if( textRun != null){
            textRun.delete();//PCG
        }

        rows.add(row);
        textRun = null;
        row = null;

        //recalculate text start x position to be in the center
        if (rowXPos != null) {
            final double newX = pageWidth / 2 - (rightPos - x) / 2;
            rowXPos.add(newX);
        }

        if (wordCounter < wordsArray.length || (wordCounter == wordsArray.length && overFlow)) {
            wrapTextCheck(rows, rowXPos, page, wordsArray, wordCounter - 1, font, fontSize, pageMargin);
        }

        ts.delete();
        gs.delete();
    }

    /**
     * Extract the pages to be removed as int array from a string with digits comma separate and range
     * e.g. : 1,2,5,10-15,21
     *
     * @param pagesToBeRemoved
     * @param numberOfDocPages
     * @return
     */
    private int[] getPagesToBeRemoved(String pagesToBeRemoved, final int numberOfDocPages) {
        final String LAST_UPPER_CASE = "LAST";
        final String COMMA = ",";
        final String DASH = "-";

        final int INITIAL_VALUE = 0;
        final int FINAL_VALUE = 1;
        final int MAX_ITEMS_FOR_EACH_PAGE_RANGE = 2;

        final Set<Integer> listWithAllPagesToBeRemoved = new HashSet<>();

        if (pagesToBeRemoved.contains(LAST_UPPER_CASE)) {
            final String lastPage = Integer.toString(numberOfDocPages);
            pagesToBeRemoved = pagesToBeRemoved.replace(LAST_UPPER_CASE, lastPage);
        }

        final String[] pageNumbers = pagesToBeRemoved.split(COMMA);

        if (pagesToBeRemoved.contains(DASH)) {
            for (String item : pageNumbers) {
                if (item.contains(DASH)) {
                    final String[] arrayFromDash = item.split(DASH);
                    if (arrayFromDash != null && arrayFromDash.length == MAX_ITEMS_FOR_EACH_PAGE_RANGE) {
                        final int initialValue = Integer.valueOf(arrayFromDash[INITIAL_VALUE]);
                        final int finalValue = Integer.valueOf(arrayFromDash[FINAL_VALUE]);
                        for (int i = initialValue; i <= finalValue; i++) {
                            listWithAllPagesToBeRemoved.add(i);
                        }
                    }
                }
            }
        }
        pagesToBeRemoved = null;

        for (String numberAsStr : pageNumbers) {
            if (!numberAsStr.contains(DASH)) {
                final int page = Integer.valueOf(numberAsStr);
                listWithAllPagesToBeRemoved.add(page);
            }
        }

        final int returnArraySize = listWithAllPagesToBeRemoved.size();
        final int returnArray[] = new int[returnArraySize];
        int counter = 0;

        //reverse removed pages list order .
        final List list = new ArrayList(listWithAllPagesToBeRemoved);
        Collections.sort(list, Collections.reverseOrder());
        final Set<Integer> resultSet = new LinkedHashSet(list);

        for (int pageInt : resultSet) {
            // Have to do this calc as the deletePages starts form page Zero that's equals the very first page.
            final int valueToImport = pageInt == 0 ? 0 : pageInt - 1;
            returnArray[counter] = valueToImport;
            counter++;
        }
        return returnArray;
    }
}
