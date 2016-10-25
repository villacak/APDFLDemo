package com.proquest.demo.enums;

/**
 * Created by kvillaca on 9/28/16.
 */
public enum OperationOrder {

    REMOVE_PROTECTION("removeprotection"),
    REMOVE_SPECIFIED_PAGES("removespecifiedpages"),
    EXTRACT_TEXT("extracttext"),
    ADD_COPYRIGHT_PAGE("addcopyrightpage"),
    ADD_COPYRIGHT_NOTICE("addcopyrightnotice"),
    LINEARIZE("linearize");

    private String operationOrder;

    private OperationOrder(String operationOrder) {
        this.operationOrder = operationOrder;
    }

    public String getOperationOrder() {
        return operationOrder;
    }
}
