/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.photoscatalogization;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author vk-user
 */
public class DirectoryInfoWithSets {
    private String directoryPath;
    private Set<FileInfo> filesSet;
    
    public DirectoryInfoWithSets (String directoryPath, Set<FileInfo> filesList) {
        this.directoryPath = directoryPath;
        this.filesSet = filesSet;
    }
    
    public String getDirectoryPath() {
        return directoryPath;
    }
    
    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
    
    public Set<FileInfo> getFilesSet() {
        return filesSet;
    }
    
    public void setFilesSet(Set<FileInfo> filesSet) {
        this.filesSet = filesSet;
    }
}
