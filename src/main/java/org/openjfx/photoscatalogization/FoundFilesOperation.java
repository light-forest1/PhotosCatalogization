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
public class FoundFilesOperation {

    private Long id;
    private String foundFilesOperation;

    public FoundFilesOperation(Long id, String foundFilesOperation) {
        this.id = id;
        this.foundFilesOperation = foundFilesOperation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoundFilesOperation() {
        return foundFilesOperation;
    }

    public void setFoundFilesOperation(String foundFilesOperation) {
        this.foundFilesOperation = foundFilesOperation;
    }
}
