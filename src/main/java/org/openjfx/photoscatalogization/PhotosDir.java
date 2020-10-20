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
public class PhotosDir {

    private Long id;
    private String photosDirPath;
    private String photosDirInsDirs;
    private Integer photosDirInsLevel;

    public PhotosDir(Long id, String photosDirPath,
            String photosDirInsDirs, Integer photosDirInsLevel) {
        this.id = id;
        this.photosDirPath = photosDirPath;
        this.photosDirInsDirs = photosDirInsDirs;
        this.photosDirInsLevel = photosDirInsLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotosDirPath() {
        return photosDirPath;
    }

    public void setPhotosDirPath(String photosDirPath) {
        this.photosDirPath = photosDirPath;
    }

    public String getPhotosDirInsDirs() {
        return photosDirInsDirs;
    }

    public void setPhotosDirInsDirs(String photosDirInsDirs) {
        this.photosDirInsDirs = photosDirInsDirs;
    }

    public Integer getPhotosDirInsLevel() {
        return photosDirInsLevel;
    }

    public void setPhotosDirInsLevel(Integer photosDirInsLevel) {
        this.photosDirInsLevel = photosDirInsLevel;
    }
}
