package com.trustchain.service;


import org.springframework.web.multipart.MultipartFile;


public interface MinioService {
    /**
     * @param file
     * @return
     */
    String upload(MultipartFile file);

    /**
     * @param oldPath
     * @param newPath
     * @return
     */
    boolean copy(String oldPath, String newPath);

    /**
     * @param oldPath
     * @param newPath
     * @return
     */
    boolean move(String oldPath, String newPath);

    /**
     * @param file
     * @return
     */
    String presignedUrl(String file);

}
