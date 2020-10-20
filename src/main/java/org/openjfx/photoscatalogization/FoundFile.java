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
public class FoundFile {

    private Long id;
    private String foundFileName;

    public FoundFile(Long id, String foundFileName) {
        this.id = id;
        this.foundFileName = foundFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoundFileName() {
        return foundFileName;
    }

    public void setFoundFileName(String foundFileName) {
        this.foundFileName = foundFileName;
    }
}
