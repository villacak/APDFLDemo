package com.proquest.demo.pojos;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by kvillaca on 10/7/16.
 */
public class UniqueProccessData {

    private List<ActionCall> actionlist;
    private String inputFileS3;
    private String outputFileS3;
    private String inputFileLocal;
    private String outputFileLocal;
    private String mimeType;
    private String md5;
    private String fileSize;
    private String pages;// Comma separated pages and dash separated ranges
    private String copyrightText;
    private transient int order;
    private String pagesize;
    private String rotation;

    public List<ActionCall> getActionlist() {
        return actionlist;
    }

    public void setActionlist(List<ActionCall> actionlist) {
        this.actionlist = actionlist;
    }

    public String getInputFileS3() {
        return inputFileS3;
    }

    public void setInputFileS3(String inputFileS3) {
        this.inputFileS3 = inputFileS3;
    }

    public String getOutputFileS3() {
        return outputFileS3;
    }

    public void setOutputFileS3(String outputFileS3) {
        this.outputFileS3 = outputFileS3;
    }

    public String getInputFileLocal() {
        return inputFileLocal;
    }

    public void setInputFileLocal(String inputFileLocal) {
        this.inputFileLocal = inputFileLocal;
    }

    public String getOutputFileLocal() {
        return outputFileLocal;
    }

    public void setOutputFileLocal(String outputFileLocal) {
        this.outputFileLocal = outputFileLocal;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UniqueProccessData{");
        sb.append("actionlist=").append(actionlist);
        sb.append(", inputFileS3='").append(inputFileS3).append('\'');
        sb.append(", outputFileS3='").append(outputFileS3).append('\'');
        sb.append(", inputFileLocal='").append(inputFileLocal).append('\'');
        sb.append(", outputFileLocal='").append(outputFileLocal).append('\'');
        sb.append(", mimeType='").append(mimeType).append('\'');
        sb.append(", md5='").append(md5).append('\'');
        sb.append(", fileSize='").append(fileSize).append('\'');
        sb.append(", pages='").append(pages).append('\'');
        sb.append(", copyrightText='").append(copyrightText).append('\'');
        sb.append(", order=").append(order);
        sb.append(", pagesize='").append(pagesize).append('\'');
        sb.append(", rotation='").append(rotation).append('\'');
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
