package com.igeek.service.system;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Gyges on 2017/6/7.
 */
public interface IFileService {

    public String uploadFile(MultipartFile file,String path);
}
