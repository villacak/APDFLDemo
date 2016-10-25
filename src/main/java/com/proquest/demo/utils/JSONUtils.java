package com.proquest.demo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proquest.demo.enums.HTTPMethod;
import com.proquest.demo.pojos.PDFAction;
import com.proquest.demo.pojos.PDFRequestProcess;
import com.proquest.demo.pojos.StandardRequest;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kvillaca on 10/21/16.
 */
public class JSONUtils {

    public List<PDFRequestProcess> parseJSON(final String jsonString) {
        final String OBJECTS_LIST = "objectslist";
        final String ACTIONS_LIST = "actionlist";
        final String OUTPUT_FILE = "outputfile";
        final String EMPTY_JSON = "{}";
        final Gson gson = new Gson();

        final List<PDFRequestProcess> pdfRequestList = new ArrayList<>();
        try {
            final JSONArray requestArray = new JSONArray(jsonString);
            final int jsonArrayMax = requestArray.length();

            for (int indexArray = 0; indexArray < jsonArrayMax; indexArray++) {
                final Object jsonObject = requestArray.get(indexArray);
                final String jsonObjectsString = jsonObject.toString();

                final PDFRequestProcess pdfRequest = new PDFRequestProcess();

                JSONArray jsonArray = null;
                List<Object> objectList = null;
                List<PDFAction> actionList = null;

                String outputfile = null;

                if (!jsonObjectsString.equals(EMPTY_JSON) &&
                        jsonObjectsString.contains(OBJECTS_LIST) &&
                        jsonObjectsString.contains(ACTIONS_LIST) &&
                        jsonObjectsString.contains(OUTPUT_FILE)) {
                    final JSONObject json = new JSONObject(jsonObjectsString);
                    jsonArray = json.getJSONArray(OBJECTS_LIST);
                    final Type listType = new TypeToken<List<StandardRequest>>() {
                    }.getType();
                    objectList = gson.fromJson(jsonArray.toString(), listType);

                    jsonArray = json.getJSONArray(ACTIONS_LIST);
                    final Type actionListType = new TypeToken<List<PDFAction>>() {
                    }.getType();
                    actionList = gson.fromJson(jsonArray.toString(), actionListType);

                    jsonArray = null;

                    outputfile = json.getString(OUTPUT_FILE);
                }

                pdfRequest.setClientId("dummy clientId");
                pdfRequest.setHttpMethod(HTTPMethod.POST);
                pdfRequest.setOutputfile(outputfile);
                pdfRequest.setObjectslist(objectList);
                pdfRequest.setActionlist(actionList);

                pdfRequestList.add(pdfRequest);
                outputfile = null;
                objectList = null;
                actionList = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfRequestList;
    }
}
