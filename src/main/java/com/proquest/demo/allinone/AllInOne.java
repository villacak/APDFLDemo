package com.proquest.demo.allinone;


import com.proquest.demo.constants.Constants;
import com.proquest.demo.enums.HttpCode;
import com.proquest.demo.enums.MessagesKeys;
import com.proquest.demo.enums.PropertyFileNames;
import com.proquest.demo.pojos.*;
import com.proquest.demo.properties.PropertyReaderLib;
import com.proquest.demo.utils.ActionsUtils;
import com.proquest.demo.utils.FileUtils;

import java.util.List;
import java.util.concurrent.*;

/**
 * Class that produces the PDF Linearized
 */
public class AllInOne {

    private static final String COMPONENTNAME = AllInOne.class.getName();

    private final PropertyReaderLib propertyReaderLib = new PropertyReaderLib(PropertyFileNames.MESSAGES);

    /**
     * Linearize PDFs from a list
     *
     * @param dataList
     * @return
     */
    public ReturnObject allInOne(final List<PDFProcessData> dataList) {
        final ActionsUtils actionsUtils = new ActionsUtils();
        final ReturnObject returnObject = new ReturnObject();

        if (dataList != null && dataList.size() > 0) {
            List<Object> objectList = new CopyOnWriteArrayList<>();
            final ExecutorService executorService = Executors.newFixedThreadPool(Constants.MAX_THREAD_COUNT);
            for (PDFProcessData pdfProccess : dataList) {

                for (GenericData data : pdfProccess.getObjectslist()) {
                    final UniqueProccessData tempProccessData = createUnique(data, pdfProccess.getActionlist());

                    try {
                        // Multi-thread run
                        final Callable<List<GenericDataReturn>> callable = new AllInOneCallable(tempProccessData);
                        final Future<List<GenericDataReturn>> dataReturnList = executorService.submit(callable);
                        final List<GenericDataReturn> pojos = dataReturnList.get();

                        //For debuging
//                    final AllInOneCallable callable = new AllInOneCallable(tempProccessData, s3ConnectionFactoryTemp);
//                    final List<GenericDataReturn> pojos = new ArrayList<>(callable.call());

                        for (GenericDataReturn pojo : pojos) {
                            objectList.add(pojo);
                        }
                    } catch (Exception e) {
                        // if some error occur then we return the object showing on what item was the error, among all success ones.
                        objectList.clear();
                        final StringBuilder sbMessage =
                                new StringBuilder(propertyReaderLib.getPropertyValue(MessagesKeys.FAILURE.toString()))
                                        .append(Constants.COLON)
                                        .append(e.getMessage())
                                        .append(" - Data:")
                                        .append(data.toJsonString());

                        returnObject.setMessage(sbMessage.toString());
                        returnObject.setObjects(objectList);
                        returnObject.setResponseCode(HttpCode.ERROR);
                        executorService.shutdownNow();
                        break;
                    }
                }
            }


            // Once the loop with all pdfs has ran we need to shutdown the executor.
            if (!executorService.isTerminated()) {
                executorService.shutdown();
            }

            // Remove files created
            for (PDFProcessData proccessData : dataList) {
                for (GenericData data: proccessData.getObjectslist()) {
                    actionsUtils.deleteFilesGenerated(data);
                }
            }


            if (returnObject.getResponseCode() == null && objectList.size() > 0) {
                returnObject.setResponseCode(HttpCode.SUCCESS);
                returnObject.setMessage(propertyReaderLib.getPropertyValue(MessagesKeys.ALL_DATA_DID_PROCESS_WITH_SUCCESS.toString()));
                returnObject.setObjects(objectList);
            }
        } else {
            final StringBuilder sbMessage =
                    new StringBuilder(propertyReaderLib.getPropertyValue(MessagesKeys.SUCCESS.toString()))
                            .append(Constants.COLON)
                            .append(propertyReaderLib.getPropertyValue(MessagesKeys.EMPTY_OBJECT_LIST.toString()));
            returnObject.setResponseCode(HttpCode.SUCCESS);
            returnObject.setMessage(sbMessage.toString());
        }

        return returnObject;
    }


    /**
     * Create a new object to be used by one thread on AllInOneCallable class
     *
     * @param gd
     * @param actions
     * @return
     */
    private UniqueProccessData createUnique(GenericData gd, final List<ActionCall> actions) {
        // Get local path for read and write files
        final FileUtils fileUtils = new FileUtils();
        gd = fileUtils.setLocalReadAndWritePath(gd);

        final UniqueProccessData pd = new UniqueProccessData();
        pd.setActionlist(actions);
        pd.setInputFileS3(gd.getInputFileS3());
        pd.setInputFileLocal(gd.getInputFileLocal());
        pd.setOutputFileS3(gd.getOutputFileS3());
        pd.setOutputFileLocal(gd.getOutputFileLocal());
        pd.setFileSize(gd.getFileSize());
        pd.setCopyrightText(gd.getCopyrightText());
        pd.setInputFileLocal(gd.getInputFileLocal());
        pd.setMimeType(gd.getMimeType());
        pd.setMd5(gd.getMd5());
        pd.setRotation(gd.getRotation());
        pd.setPages(gd.getPages());
        return pd;
    }
}