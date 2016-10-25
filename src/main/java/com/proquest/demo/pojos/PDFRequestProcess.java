package com.proquest.demo.pojos;

import com.google.gson.Gson;
import com.proquest.demo.enums.HTTPMethod;

import java.util.List;

/**
 * Created by kvillaca on 9/26/16.
 */
public class PDFRequestProcess {
    private String clientId;
    private HTTPMethod httpMethod;
    private String outputfile;
    private List<PDFAction> actionlist;
    private List<Object> objectslist; // Not been used at moment

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<PDFAction> getActionlist() {
        return actionlist;
    }

    public void setActionlist(List<PDFAction> actionlist) {
        this.actionlist = actionlist;
    }

    public List<Object> getObjectslist() {
        return objectslist;
    }

    public void setObjectslist(List<Object> objectslist) {
        this.objectslist = objectslist;
    }

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PDFRequestProcess{");
        sb.append("clientId='").append(clientId).append('\'');
        sb.append(", httpMethod=").append(httpMethod);
        sb.append(", actionlist=").append(actionlist);
        sb.append(", objectslist=").append(objectslist);
        sb.append(", outputfile='").append(outputfile).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Method toJsonString.
     * @return String
     */
    public String toJsonString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
