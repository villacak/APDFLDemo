package com.proquest.demo.pojos;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by kvillaca on 8/8/16.
 */
public class PDFProcessData {

    private List<ActionCall> actionlist;
    private List<GenericData> objectslist; // Not been used at moment
//    private String outputfile;
//    private String inputfile;
//    private String outputfilelocal;
//    private String inputfilelocal;


    public List<ActionCall> getActionlist() {
        return actionlist;
    }

    public void setActionlist(List<ActionCall> actionlist) {
        this.actionlist = actionlist;
    }

    public List<GenericData> getObjectslist() {
        return objectslist;
    }

    public void setObjectslist(List<GenericData> objectslist) {
        this.objectslist = objectslist;
    }

//    public String getOutputfile() {
//        return outputfile;
//    }
//
//    public void setOutputfile(String outputfile) {
//        this.outputfile = outputfile;
//    }
//
//    public String getInputfile() {
//        return inputfile;
//    }
//
//    public void setInputfile(String inputfile) {
//        this.inputfile = inputfile;
//    }
//
//
//    public String getOutputfilelocal() {
//        return outputfilelocal;
//    }
//
//    public void setOutputfilelocal(String outputfilelocal) {
//        this.outputfilelocal = outputfilelocal;
//    }
//
//    public String getInputfilelocal() {
//        return inputfilelocal;
//    }
//
//    public void setInputfilelocal(String inputfilelocal) {
//        this.inputfilelocal = inputfilelocal;
//    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PDFProcessData{");
        sb.append("actionlist=").append(actionlist);
        sb.append(", objectslist=").append(objectslist);
//        sb.append(", outputfile='").append(outputfile).append('\'');
//        sb.append(", inputfile='").append(inputfile).append('\'');
//        sb.append(", outputfilelocal='").append(outputfilelocal).append('\'');
//        sb.append(", inputfilelocal='").append(inputfilelocal).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Method toJsonString.
     *
     * @return String
     */
    public String toJsonString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
