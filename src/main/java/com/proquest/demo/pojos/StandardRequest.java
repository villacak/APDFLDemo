package com.proquest.demo.pojos;

import com.google.gson.Gson;

/**
 * Created by kvillaca on 7/6/16.
 */
public class StandardRequest {


    private String inputfile;
    private String outputfile;
    private String mimetype;
    private String md5;
    private String size;
    private String pages; // Comma separated pages and dash separated ranges
    private String copyrighttext;
    private String pagesize;
    private String rotation;


    public String getInputfile() {
        return inputfile;
    }

    public void setInputfile(String inputfile) {
        this.inputfile = inputfile;
    }

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getCopyrighttext() {
        return copyrighttext;
    }

    public void setCopyrighttext(String copyrighttext) {
        this.copyrighttext = copyrighttext;
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
        final StringBuilder sb = new StringBuilder("StandardRequest{");
        sb.append("inputfile='").append(inputfile).append('\'');
        sb.append(", outputfile='").append(outputfile).append('\'');
        sb.append(", mimetype='").append(mimetype).append('\'');
        sb.append(", md5='").append(md5).append('\'');
        sb.append(", size='").append(size).append('\'');
        sb.append(", pages='").append(pages).append('\'');
        sb.append(", copyrighttext='").append(copyrighttext).append('\'');
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
