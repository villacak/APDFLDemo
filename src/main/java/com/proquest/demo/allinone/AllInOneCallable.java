package com.proquest.demo.allinone;

import com.datalogics.PDFL.Document;
import com.datalogics.PDFL.SaveFlags;
import com.proquest.demo.enums.HttpCode;
import com.proquest.demo.enums.MessagesKeys;
import com.proquest.demo.enums.OperationOrder;
import com.proquest.demo.enums.PropertyFileNames;
import com.proquest.demo.pojos.*;
import com.proquest.demo.properties.PropertyReaderLib;
import com.proquest.demo.utils.ActionsUtils;
import com.proquest.demo.utils.FileUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Created by kvillaca on 7/25/16.
 *
 * @author kvillaca, zliu
 */
public class AllInOneCallable implements Callable<List<GenericDataReturn>> {


    private static final String COMPONENTNAME = AllInOneCallable.class.getName();

    private UniqueProccessData data;
    private Document doc;
    private FileUtils fileUtils;
    private List<GenericDataReturn> dataReturnList = new ArrayList<>();
    private PDFLBase base;
    private boolean isTest;

    public AllInOneCallable(final UniqueProccessData data) {
        this.data = data;
    }


    /**
     * Process a single PDF, a single item from the list passed to the method above
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<GenericDataReturn> call() throws Exception {
        if (base == null) {
            base = new PDFLBase();
        }
        final PropertyReaderLib propertyReaderLib = new PropertyReaderLib(PropertyFileNames.MESSAGES);

        // Get local path for read and write files
        if (fileUtils == null) {
            fileUtils = new FileUtils();
        }


        try {
            // Init the JNI Lib
            base.initLibrary();

            boolean enableLicensedBehavior = false;
            final List<ActionCall> actions = data.getActionlist();
            for (ActionCall action : actions) {
                if (action.getPdfAction().equals(OperationOrder.REMOVE_PROTECTION.getOperationOrder())) {
                    enableLicensedBehavior = true;
                    break;
                }
            }
            if (enableLicensedBehavior) {
                base.enableLicensedBehavior();
            }

            if (doc == null) {
                doc = new Document(data.getInputFileS3());
            }

            // To save the file from local file system - Step 3
            doc = process(doc, data, base, propertyReaderLib);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (doc != null) {
                doc.close();
                doc = null;
            }
            // Dealloc the JNI Lib
            base.terminateLibrary();
        }

        if (data.getOutputFileLocal() != null && data.getOutputFileLocal().length() > 0) {

            // Upload the pdf file to S3 - Step 4
            final Path tempPath = Paths.get(data.getOutputFileLocal());
            final byte[] localPdfAsBytes = fileUtils.readFileFromTempLocation(tempPath);
            final String theDigest = DigestUtils.md5Hex(localPdfAsBytes);

            // Set new values from new file.
            data.setMd5(theDigest);
            data.setFileSize(Integer.toString(localPdfAsBytes.length));

            final Path outputPath = Paths.get(data.getOutputFileS3());
            final HttpCode codeReturned = fileUtils.writeFileToTempLocation(outputPath, localPdfAsBytes);

            final GenericDataReturn dataReturn = new GenericDataReturn();
            dataReturn.setMd5(theDigest);
            dataReturn.setMimeType("Dummy MimeType");
            dataReturn.setOutputfile(data.getOutputFileS3());
            dataReturn.setSize(data.getFileSize());
            dataReturn.setActionResponse("Dummy Action Response.");
            dataReturnList.add(dataReturn);

            fileUtils = null;
            data = null;

            return dataReturnList;
        } else {
            throw new Exception(propertyReaderLib.getPropertyValue(MessagesKeys.FAILURE.toString()));
        }
    }


    private Document process(Document doc
            , final UniqueProccessData data
            , final PDFLBase base
            , final PropertyReaderLib propertyReaderLib
    ) throws Exception {
        final String METHODNAME = "process";
        final String NONE = "NONE";
        final String TEXT = "text";
        final String PAGES = "pages";
        final String DEFAULT = "default";

        final List<ActionCall> actions = data.getActionlist();
        boolean pdfSaved = false;

        final OperationOrder[] operationOrder = OperationOrder.values();

        for (OperationOrder operation : operationOrder) {
            final String operationName = operation.getOperationOrder();

            for (ActionCall action : actions) {
                if (action.getPdfAction().equals(operationName)) {
                    if (operationName.equals(OperationOrder.REMOVE_PROTECTION.getOperationOrder())) {
                        final List<PDFValueForActionCall> valueList = action.getValueForActionList();
                        String removeProtectionFlag = null;
                        for (PDFValueForActionCall valueCall : valueList) {
                            if (valueCall.getKey().equals(DEFAULT)) {
                                removeProtectionFlag = valueCall.getValue();
                                break;
                            }
                        }
                        if (removeProtectionFlag.equalsIgnoreCase(Boolean.TRUE.toString())) {
                            doc = base.removeProtection(doc);
                        }
                        removeProtectionFlag = null; // Free some memory
                    } else if (operationName.equals(OperationOrder.REMOVE_SPECIFIED_PAGES.getOperationOrder())) {
                        final List<PDFValueForActionCall> valueList = action.getValueForActionList();
                        String pages = null;
                        for (PDFValueForActionCall valueCall : valueList) {
                            if (valueCall.getKey().equals(PAGES)) {
                                pages = valueCall.getValue();
                                break;
                            }
                        }
                        doc = base.removePages(doc, pages, propertyReaderLib);
                        pages = null; // Free some memory
                    } else if (operationName.equals(OperationOrder.ADD_COPYRIGHT_PAGE.getOperationOrder())) {
                        final List<PDFValueForActionCall> valueList = action.getValueForActionList();
                        String copyrightText = null;
                        for (PDFValueForActionCall valueCall : valueList) {
                            if (valueCall.getKey().equals(TEXT)) {
                                copyrightText = valueCall.getValue();
                                break;
                            }
                        }
                        doc = base.addCopyrightPage(doc, copyrightText);
                        copyrightText = null;
                    } else if (operationName.equals(OperationOrder.ADD_COPYRIGHT_NOTICE.getOperationOrder())) {
                        final List<PDFValueForActionCall> valueList = action.getValueForActionList();
                        String copyrightText = null;
                        for (PDFValueForActionCall valueCall : valueList) {
                            if (valueCall.getKey().equals(TEXT)) {
                                copyrightText = valueCall.getValue();
                                break;
                            }
                        }
                        doc = base.addCopyrightNotice(doc, copyrightText);
                        copyrightText = null;
                    } else if (operationName.equals(OperationOrder.EXTRACT_TEXT.getOperationOrder())) {
                        final String TEXT_PLAIN = "text/plain";
                        final ActionsUtils actionsUtils = new ActionsUtils();
                        final GenericData tempGenericData = actionsUtils.createGeneralDataFromPDFProcessDataForTextFileUpload(data);
                        final FileUtils txtfileUtils = new FileUtils();

                        // This line run the extract text from APDFL
                        // Read the next comment for run PDFBox.
                        final byte[] localTxtAsBytes = base.extractText(doc, tempGenericData, propertyReaderLib);

                        // This's necessary to run the PDFBOX 2.0.3
                        // Comment the line above and uncomment those two lines bellow to test it
//                        base.save(doc, tempGenericData.getOutputFileLocal(), EnumSet.of(SaveFlags.FULL));
//                        final byte[] localTxtAsBytes = base.extractTextPDFBox(tempGenericData.getOutputFileLocal());

                        GenericDataReturn textFileDataReturn = new GenericDataReturn();
                        if (localTxtAsBytes != null && localTxtAsBytes.length > 0) {
                            final String theDigest = DigestUtils.md5Hex(localTxtAsBytes);
                            textFileDataReturn.setMd5(theDigest);
                            textFileDataReturn.setSize(Integer.toString(localTxtAsBytes.length));

                            final Path pathTempLocationSaveTextFile = Paths.get(tempGenericData.getOutputFileS3());
                            final HttpCode code = fileUtils.writeFileToTempLocation(pathTempLocationSaveTextFile, localTxtAsBytes);
                            if (code == HttpCode.ERROR) {
                                textFileDataReturn.setOutputfile(NONE);
                            } else {
                                textFileDataReturn.setOutputfile(tempGenericData.getOutputFileS3());
                            }
                        } else {
                            textFileDataReturn.setOutputfile(NONE);
                        }
                        textFileDataReturn.setMimeType(TEXT_PLAIN);
                        textFileDataReturn.setActionResponse(operationName);
                        dataReturnList.add(textFileDataReturn);
                    } else if (operationName.equals(OperationOrder.LINEARIZE.getOperationOrder())) {
                        final List<PDFValueForActionCall> valueList = action.getValueForActionList();
                        String linerizeFlag = null;
                        for (PDFValueForActionCall valueCall : valueList) {
                            if (valueCall.getKey().equals(OperationOrder.LINEARIZE.getOperationOrder())) {
                                linerizeFlag = valueCall.getValue();
                                break;
                            }
                        }
                        if (linerizeFlag.equalsIgnoreCase(Boolean.TRUE.toString())) {
                            doc = base.linearize(doc, data.getOutputFileLocal());
                        } else {
                            doc = base.save(doc, data.getOutputFileLocal(), EnumSet.of(SaveFlags.FULL));
                        }
                        linerizeFlag = null;
                        pdfSaved = true;
                    }
                }
            }
        }

        if (!pdfSaved) {
            doc = base.save(doc, data.getOutputFileLocal(), EnumSet.of(SaveFlags.FULL));
        }
        return doc;
    }
}
