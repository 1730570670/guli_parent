package com.edu.ossservice.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @CreateTime: 2022-08-22
 */
public interface OssFileService {
    String fileUpload(MultipartFile file) throws IOException;

    String videoFileUpload(MultipartFile file);

    void deleteFile(String url);
}
