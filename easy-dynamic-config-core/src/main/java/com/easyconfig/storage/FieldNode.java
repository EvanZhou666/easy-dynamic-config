package com.easyconfig.storage;

public class FieldNode {

    Long fieldId;

    Long parentFieldId;

    String fieldName;

    String fieldType;

    Object fieldValue;

    public FieldNode(Long fieldId, Long parentFieldId, String fieldName, String fieldType, Object fieldValue) {
        this.fieldId = fieldId;
        this.parentFieldId = parentFieldId;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldValue = fieldValue;
    }
}
