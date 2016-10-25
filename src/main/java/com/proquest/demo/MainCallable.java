package com.proquest.demo;

import com.proquest.demo.allinone.AllInOne;
import com.proquest.demo.pojos.PDFProcessData;
import com.proquest.demo.pojos.ReturnObject;
import com.proquest.demo.utils.ActionsUtils;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by kvillaca on 10/24/16.
 */
public class MainCallable implements Callable<ReturnObject> {

    final ActionsUtils actionsUtils = new ActionsUtils();

    private String jsonRequestPayload;

    public MainCallable(final String jsonRequestPayload) {
        this.jsonRequestPayload = jsonRequestPayload;
    }


    @Override
    public ReturnObject call() throws Exception {
        final List<PDFProcessData> dataList = actionsUtils.parseFromServiceRequestToLibraryObjects(jsonRequestPayload);
        final AllInOne allInOne = new AllInOne();
        final ReturnObject returnObject = allInOne.allInOne(dataList);
        return returnObject;
    }
}
