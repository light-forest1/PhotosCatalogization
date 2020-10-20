/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.photoscatalogization;

import com.drew.metadata.Metadata;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vk-user
 */
public class FileInfo {
    private String filePath;
    private Metadata fileMetadata;
    private Set<String> fileKeywordsSet;
    
    public FileInfo (String filePath, Metadata fileMetadata, 
            Set<String> fileKeywordsSet) {
        this.filePath = filePath;
        this.fileMetadata = fileMetadata;
        this.fileKeywordsSet = fileKeywordsSet;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Metadata getFileMetadata() {
        return fileMetadata;
    }
    
    public void setFileMetadata(Metadata fileMetadata) {
        this.fileMetadata = fileMetadata;
    }
    
    public Set<String> getFileKeywordsSet() {
        return fileKeywordsSet;
    }
    
    public void setFileKeywordsSet(Set<String> fileKeywordsSet) {
        this.fileKeywordsSet = fileKeywordsSet;
    }
}
