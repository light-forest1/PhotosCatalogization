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
public class PhotosDirFile {

    private Long id;
    private String photosDirFileName;

    public PhotosDirFile(Long id, String photosDirFileName) {
        this.id = id;
        this.photosDirFileName = photosDirFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotosDirFileName() {
        return photosDirFileName;
    }

    public void setPhotosDirFileName(String photosDirFileName) {
        this.photosDirFileName = photosDirFileName;
    }
}
