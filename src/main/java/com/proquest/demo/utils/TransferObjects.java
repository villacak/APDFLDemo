package com.proquest.demo.utils;

import com.proquest.demo.pojos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kvillaca on 7/20/16.
 */
public class TransferObjects {

    /**
     * Move data from GenericData object to StandardRequest
     *
     * @param data
     * @return
     */
    public StandardRequest fromGenericDataToStandardRequest(final GenericData data) {
        if (data == null)
            return null;

        final StandardRequest sr = new StandardRequest();
        sr.setSize(data.getFileSize());
        sr.setMimetype(data.getMimeType());
        sr.setMd5(data.getMd5());
        sr.setCopyrighttext(data.getCopyrightText());
        sr.setPages(data.getPages());
        sr.setInputfile(data.getInputFileS3());
        sr.setOutputfile(data.getOutputFileS3());
        sr.setPagesize(data.getPagesize());
        sr.setRotation(data.getRotation());
        return sr;
    }


    /**
     * Move data from StandardRequest object to GenericData
     *
     * @param data
     * @return
     */
    public GenericData fromStandardRequestToGenericData(final StandardRequest data) {
        if (data == null)
            return null;

        final GenericData gd = new GenericData();
        gd.setFileSize(data.getSize());
        gd.setMimeType(data.getMimetype());
        gd.setMd5(data.getMd5());
        gd.setCopyrightText(data.getCopyrighttext());
        gd.setPages(data.getPages());
        gd.setInputFileS3(data.getInputfile());
        final String outputS3 = (data.getOutputfile() != null)? data.getOutputfile() : data.getInputfile();
        gd.setOutputFileS3(outputS3);

        gd.setPagesize(data.getPagesize());
        gd.setRotation(data.getRotation());
        return gd;
    }


    /**
     * Copy values from PDFRequestProcess object to PDFProcessData
     *
     * As we have separated classes and methods for each action that has just downloading files to upload one
     * For the regular AllInOne Class, that will have those other tasks, that will usually download one and
     * upload one file, or two in case of extract text and maybe preview, there's no need to have the outputfile
     * outside the objectlist.
     * The code bellow will move the outter outputfile to the inner object list objects if it has only one object
     * within the object list. If there is more than one, than for the rules all outputfiles should be within those
     * objects from the objectlist.
     *
     * @param pdfRequest
     * @return
     */
    public PDFProcessData fromPDFRequestProcessToPDFProccessData(final PDFRequestProcess pdfRequest) {
        final String TO_REPLACE = "(_)";
        final int UNIQUE_ELEMENT = 1;
        final PDFProcessData data = new PDFProcessData();

        // Populate Object List
        if (pdfRequest.getObjectslist() != null && pdfRequest.getObjectslist().size() > 0) {
            final List<GenericData> genericDatas = new ArrayList<>();
            for (Object pdfData : pdfRequest.getObjectslist()) {
                final StandardRequest tempData = (StandardRequest) pdfData;
                if (pdfRequest.getObjectslist().size() == UNIQUE_ELEMENT) {
                    final String uuidStr = UUID.randomUUID().toString();
                    final String tempOutputfileName = pdfRequest.getOutputfile().replace(TO_REPLACE, uuidStr);
                    tempData.setOutputfile(tempOutputfileName);
                }
                final GenericData tempGenericData = fromStandardRequestToGenericData(tempData);
                if (tempGenericData != null) {
                    genericDatas.add(tempGenericData);
                }
            }
            data.setObjectslist(genericDatas);
        }

        // Populate the Action List
        if (pdfRequest.getActionlist() != null && pdfRequest.getActionlist().size() > 0) {
            final List<ActionCall> genericActions = new ArrayList<>();
            for (PDFAction actionData : pdfRequest.getActionlist()) {
                final ActionCall actionCall = new ActionCall();
                actionCall.setPdfAction(actionData.getPdfAction());
                final List<PDFValueForActionCall> actionsCalls = new ArrayList<>();
                if (actionData.getValueForActionList() != null && actionData.getValueForActionList().size() > 0) {

                    for (PDFValueForAction actionValues : actionData.getValueForActionList()) {
                        final PDFValueForActionCall callActionValue = new PDFValueForActionCall();
                        callActionValue.setKey(actionValues.getKey());
                        final String value = actionValues.getValue();
                        if (value.contains(TO_REPLACE)) {
                            final String uuidStr = UUID.randomUUID().toString();
                            final String tempOutputfileName = value.replace(TO_REPLACE, uuidStr);
                            callActionValue.setValue(tempOutputfileName);
                        } else {
                            callActionValue.setValue(value);
                        }
                        actionsCalls.add(callActionValue);
                    }
                }
                actionCall.setValueForActionList(actionsCalls);
                genericActions.add(actionCall);
            }
            data.setActionlist(genericActions);
        }
        return data;
    }
}
