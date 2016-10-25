package com.proquest.demo.pojos;

import com.google.gson.Gson;

/**
 * Created by kvillaca on 7/12/16.
 */
public class GenericDataReturn {

    
    private String outputfile;
    private String mimeType;
    private String md5;
    private String size;
    private String actionResponse;

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(final String outputfile) {
        this.outputfile = outputfile;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(final String md5) {
        this.md5 = md5;
    }

    public String getSize() {
        return size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public String getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(String actionResponse) {
        this.actionResponse = actionResponse;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("outputfile:'").append(outputfile).append('\'');
        sb.append(", mimeType:").append(mimeType);
        sb.append(", md5:'").append(md5).append('\'');
        sb.append(", size:'").append(size).append('\'');
        sb.append(", actionResponse:'").append(actionResponse).append('\'');
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
