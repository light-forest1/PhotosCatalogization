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
public class FoundFilesKeyword {

    private Long id;
    private String foundFilesKeyword;

    public FoundFilesKeyword(Long id, String foundFilesKeyword) {
        this.id = id;
        this.foundFilesKeyword = foundFilesKeyword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoundFilesKeyword() {
        return foundFilesKeyword;
    }

    public void setFoundFilesKeyword(String foundFilesKeyword) {
        this.foundFilesKeyword = foundFilesKeyword;
    }
}
