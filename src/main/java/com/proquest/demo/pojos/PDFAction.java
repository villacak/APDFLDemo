package com.proquest.demo.pojos;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by kvillaca on 9/26/16.
 */
public class PDFAction {

    private String pdfAction;
    private List<PDFValueForAction> valueForActionList;

    public String getPdfAction() {
        return pdfAction;
    }

    public void setPdfAction(String pdfAction) {
        this.pdfAction = pdfAction;
    }

    public List<PDFValueForAction> getValueForActionList() {
        return valueForActionList;
    }

    public void setValueForActionList(List<PDFValueForAction> valueForActionList) {
        this.valueForActionList = valueForActionList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PDFAction{");
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
