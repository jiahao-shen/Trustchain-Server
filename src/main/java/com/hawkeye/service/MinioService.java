package com.hawkeye.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


public interface MinioService {
    /**
     * @param file 文件
     * @return 相对路径
     */
    String upload(MultipartFile file);

    /**
     * @param file
     * @param path
     * @return
     */
    String upload(MultipartFile file, String path);

    /**
     * @param is: 输入流
     * @return
     */
    String upload(InputStream is);

    /**
     * @param is
     * @param path
     * @return
     */
    String upload(InputStream is, String path);

    /**
     * @param oldPath 文件旧路径(相对路径)
     * @param newPath 文件新路径(相对路径)
     * @return 是否成功
     */
    boolean copy(String oldPath, String newPath);

    /**
     * @param oldPath 文件旧路径(相对路径)
     * @param newPath 文件新路径(相对路径)
     * @return 是否成功
     */
    boolean move(String oldPath, String newPath);

    /**
     * @param path
     * @return
     */
    boolean remove(String path);

    /**
     * @param file 文件路径(相对路径)
     * @return Minio路径
     */
    String presignedUrl(String file);

    /**
     * @param path 路径
     * @return 是否为Url
     */
    boolean isUrl(String path);

    /**
     * @param file 文件路径(相对路径)
     * @return 文件对象
     */
    InputStream get(String file);

    /**
     * @param hash 哈希值
     * @return 文件是否存在
     */
    String exist(String hash);
}
