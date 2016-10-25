package com.proquest.demo.pojos;

import com.google.gson.Gson;

/**
 * Created by kvillaca on 9/26/16.
 */
public class PDFValueForActionCall {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PDFValueForActionCall{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }


     /**
     * Method toJsonString.
     * @return String
}        */
    public String toJsonString() {
        Gson gson = new Gson();
            String json = gson.toJson(this);
        return json;
    }
}