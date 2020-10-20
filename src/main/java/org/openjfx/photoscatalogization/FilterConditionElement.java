/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.photoscatalogization;

/**
 *
 * @author vk-user
 */
public class FilterConditionElement {
    private String filterConditionStr;
    private Boolean noProperty;
    private Boolean isOperator;
    private Boolean isSqBracket;
//    private Boolean inBrackets;
    
    public FilterConditionElement(String filterConditionStr, Boolean noProperty, 
            Boolean isOperator, Boolean isSqBracket) {
        this.filterConditionStr = filterConditionStr;
        this.noProperty = noProperty;
        this.isOperator = isOperator;
        this.isSqBracket = isSqBracket;
//        this.inBrackets = inBrackets;
    }
    
    public String getFilterConditionStr() {
        return filterConditionStr;
    }
    
    public void setFilterConditionStr(String filterConditionStr) {
        this.filterConditionStr = filterConditionStr;
    }
    
    public Boolean getNoProperty() {
        return noProperty;
    }
    
    public void setNoProperty(Boolean noProperty) {
        this.noProperty = noProperty;
    }
    
    public Boolean getIsOperator() {
        return isOperator;
    }
    
    public void setIsOperator(Boolean isOperator) {
        this.isOperator = isOperator;
    }
    
    public Boolean getIsSqBracket() {
        return isSqBracket;
    }
    
    public void setIsSqBracket(Boolean isSqBracket) {
        this.isSqBracket = isSqBracket;
    }
    
//    public Boolean getInBrackets() {
//        return inBrackets;
//    }
//    
//    public void setInBrackets(Boolean inBrackets) {
//        this.inBrackets = inBrackets;
//    }
}
