/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.photoscatalogization;

import java.util.ArrayList;

/**
 *
 * @author vk-user
 */
public class DirectoryInfo {
    private String directoryPath;
    private ArrayList<FileInfo> filesList;
    
    public DirectoryInfo (String directoryPath, ArrayList<FileInfo> filesList) {
        this.directoryPath = directoryPath;
        this.filesList = filesList;
    }
    
    public String getDirectoryPath() {
        return directoryPath;
    }
    
    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
    
    public ArrayList<FileInfo> getFilesList() {
        return filesList;
    }
    
    public void setFilesList(ArrayList<FileInfo> filesList) {
        this.filesList = filesList;
    }
}
