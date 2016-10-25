package com.proquest.demo.enums;

/**
 * Created by kvillaca on 7/1/16.
 */
public enum PropertyFileNames {

    MESSAGES("messages"), STORAGE("HMSStorage"), RETRIEVE("HMSRetrieve"), PDFLIBRARY("PDFLibrary"), DUMMY_JUST_FOR_TEST("dummy");

    private String propertyFileName;

    private PropertyFileNames(final String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }

}
