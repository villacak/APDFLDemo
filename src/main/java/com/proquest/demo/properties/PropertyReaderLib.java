package com.proquest.demo.properties;


import com.proquest.demo.enums.PropertyFileNames;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by kvillaca on 7/1/16.
 */
public class PropertyReaderLib {

    private static final String COMPONENT_NAME = PropertyReaderLib.class.getName();

    private PropertyFileNames propertyFileName;


    /**
     * Default constructor
     *
     */
    public PropertyReaderLib() {
        super();
    }


    /**
     * Constructor with property file name
     *
     * @param propertyFileName
     */
    public PropertyReaderLib(final PropertyFileNames propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    /**
     * Set the propertiy file name desired to retrieve the data
     *
     * @param propertyFileName
     */
    public void setPropertyFileName(final PropertyFileNames propertyFileName) {
        this.propertyFileName = propertyFileName;
    }


    /**
     * Reset the property file name
     *
     */
    public void resetPropertyFileName() {
        this.propertyFileName = null;
    }


    /**
     * Get properties value from an int value
     * @param codeInteger
     * @return
     */
    public String getPropertyValue(int codeInteger) {
        final String METHOD_NAME = "getPropertyValue - int parameter";
        String propValueToReturn = null;
        if (codeInteger > 0) {
            final String integerAsStr = Integer.toString(codeInteger);
            if (integerAsStr != null && integerAsStr.trim().length() > 0) {
                propValueToReturn = getPropertyValue(integerAsStr);
            }
        }
        return propValueToReturn;
    }


    /**
     * Get properties value from a String
     *
     * @param keyString
     * @return
     */
    public String getPropertyValue(final String keyString) {
        final String METHOD_NAME = "getPropertyValue - String parameter";
        String propValueToReturn = null;
        final ResourceBundle rb = ResourceBundle.getBundle(propertyFileName.getPropertyFileName(), Locale.getDefault());

        if (rb == null) {
            propValueToReturn = null;
        } else {
            propValueToReturn = rb.getString(keyString);
        }
        return propValueToReturn;
    }
}
