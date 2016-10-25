package com.proquest.demo.pojos;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by kvillaca on 9/26/16.
 */
public class ActionCall {

    private String pdfAction;
    private List<PDFValueForActionCall> valueForActionList;

    public String getPdfAction() {
        return pdfAction;
    }

    public void setPdfAction(String pdfAction) {
        this.pdfAction = pdfAction;
    }

    public List<PDFValueForActionCall> getValueForActionList() {
        return valueForActionList;
    }

    public void setValueForActionList(List<PDFValueForActionCall> valueForActionList) {
        this.valueForActionList = valueForActionList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActionCall{");
        sb.append("pdfAction='").append(pdfAction).append('\'');
        sb.append(", valueForActionList=").append(valueForActionList);
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
