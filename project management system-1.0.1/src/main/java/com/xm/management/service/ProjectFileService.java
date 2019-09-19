package com.xm.management.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;

public interface ProjectFileService {
    String batchImport(String fileName, MultipartFile mfile, String userName);

    public void batchExport(String[] titles, ServletOutputStream out) throws Exception;
}
