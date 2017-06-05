package com.atwjsw.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wenda on 6/4/2017.
 */
public interface IFileService {

    public String upload(MultipartFile file, String path);
}
