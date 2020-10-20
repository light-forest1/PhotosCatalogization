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
public class FileKeyword {

    private Long id;
    private String fileKeyword;

    public FileKeyword(Long id, String fileKeyword) {
        this.id = id;
        this.fileKeyword = fileKeyword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileKeyword() {
        return fileKeyword;
    }

    public void setFileKeyword(String fileKeyword) {
        this.fileKeyword = fileKeyword;
    }
}
