package com.proquest.demo.constants;

/**
 * Created by kvillaca on 7/1/16.
 */
public class Constants {


    public static final String COLON = ":";
    public static final String BLANK_SPACE = " ";
    public static final String EMPTY = "";
    public static final String SLASH_FORWARD = "/";
    public static final String PDF_SPLIT =".pdf";
    public static final String PERIOD = ".";
    public static final String UNDERSCORE = "_";

    public static final String DEFAULT_PATH = SLASH_FORWARD + "data" + SLASH_FORWARD + "temppdfs";
    public static final String ENVIRONMENT_VAR_FOR_TEMPPDFS = "TEMP_PDFS";

    // This will be used for generate the optimal number of threads depending upon the number of processors
    public static final int MAX_THREAD_COUNT = Runtime.getRuntime().availableProcessors();
}
