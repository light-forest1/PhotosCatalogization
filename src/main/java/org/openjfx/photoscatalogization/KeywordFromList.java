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
public class KeywordFromList {

    private Long id;
    private Integer keywordFromListCount;
    private String keywordFromList;

    public KeywordFromList(Long id, Integer keywordFromListCount,
            String keywordFromList) {
        this.id = id;
        this.keywordFromListCount = keywordFromListCount;
        this.keywordFromList = keywordFromList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKeywordFromListCount() {
        return keywordFromListCount;
    }

    public void setKeywordFromListCount(Integer keywordFromListCount) {
        this.keywordFromListCount = keywordFromListCount;
    }

    public String getKeywordFromList() {
        return keywordFromList;
    }

    public void setKeywordFromList(String keywordFromList) {
        this.keywordFromList = keywordFromList;
    }
}
