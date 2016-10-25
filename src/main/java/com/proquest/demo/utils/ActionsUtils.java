package com.proquest.demo.utils;


import com.proquest.demo.enums.MessagesKeys;
import com.proquest.demo.enums.PropertyFileNames;
import com.proquest.demo.pojos.*;
import com.proquest.demo.properties.PropertyReaderLib;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kvillaca on 7/26/16.
 */
public class ActionsUtils {
    private final PropertyReaderLib propertyReaderLib = new PropertyReaderLib(PropertyFileNames.MESSAGES);

    /**
     * Delete files created on the local machine
     *
     * @param data
     */
    public void deleteFilesGenerated(final GenericData data) {
        final String PATH = "Path: ";

        if (data != null && data.getInputFileS3() != null && data.getOutputFileS3() != null) {
            final FileUtils fileUtils = new FileUtils();
            final Path localPathRead = data.getInputFileLocal() != null ? Paths.get(data.getInputFileLocal()) : null;
            final Path localPathWrite = data.getOutputFileLocal() != null ? Paths.get(data.getOutputFileLocal()) : null;

            if (localPathRead != null)
                fileUtils.deleteFile(localPathRead);

            if (localPathWrite != null)
                fileUtils.deleteFile(localPathWrite);
        } else {
            System.out.println(propertyReaderLib.getPropertyValue(MessagesKeys.FAILURE.toString()));
        }
    }


    /**
     * Pause for some pre defined time
     *
     * @param milliseconds
     */
    public void timeOut(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create a new Generic Data object to be used by AWSS3IO to upload the text file.
     *
     * @param processData
     * @return
     */
    public GenericData createGeneralDataFromPDFProcessDataForTextFileUpload(final UniqueProccessData processData) {
        final String EXTRACT_TEXT = "extracttext";
        final FileUtils utils = new FileUtils();
        GenericData genericDataToReturn = null;

        for (ActionCall action : processData.getActionlist()) {
            for (PDFValueForActionCall valueCall : action.getValueForActionList()) {
                if (action.getPdfAction().equals(EXTRACT_TEXT)) {
                    genericDataToReturn = new GenericData();
                    genericDataToReturn.setOutputFileS3(valueCall.getValue());
                    genericDataToReturn.setInputFileS3(null);
                    genericDataToReturn.setMimeType("text/plain");
                    // When extractimg text, it's just one in the list, so we get the position 0, the first one.
                    genericDataToReturn.setOutputFileS3(action.getValueForActionList().get(0).getValue());
                    break;
                }
            }
        }
        return genericDataToReturn;
    }


    /**
     * Parse from JSON string with REST service payload to library object list
     *
     * @param jsonAsString
     * @return
     */
    public List<PDFProcessData> parseFromServiceRequestToLibraryObjects(final String jsonAsString) {
        JSONUtils jsonUtils = new JSONUtils();
        List<PDFRequestProcess> toConversion = jsonUtils.parseJSON(jsonAsString);

        final TransferObjects transferObjects = new TransferObjects();
        final List<PDFProcessData> tempProcessDataList = new ArrayList<>();

        for (PDFRequestProcess pdfRequest : toConversion) {
            final PDFProcessData tempData = transferObjects.fromPDFRequestProcessToPDFProccessData(pdfRequest);
            tempProcessDataList.add(tempData);
        }
        return tempProcessDataList;
    }
}
