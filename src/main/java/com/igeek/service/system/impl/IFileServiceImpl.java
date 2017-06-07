package com.igeek.service.system.impl;

import com.google.common.collect.Lists;
import com.igeek.service.system.IFileService;
import com.igeek.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Gyges on 2017/6/7.
 */
@Service("iFileService")
public class IFileServiceImpl implements IFileService {

    public static final Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) {
        String filename = file.getOriginalFilename();//获取源文件名称
        String fileExtensionName = filename.substring(filename.lastIndexOf(".") + 1);//获取扩展文件名后缀
        String uploadFilename = UUID.randomUUID().toString()+"."+ fileExtensionName;//解决文件名存在重复问题
        logger.info("开始上传文件，文件名:{},上传路径:{},新文件名:{}", filename, path, uploadFilename);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);//文件可以写权限，可以修改
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFilename);
        try {
            file.transferTo(targetFile);
            //文件已经上传成功,传到upload这个文件夹下面,此文件在tomcat目录下
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }


//    public static void main(String[] args) {
//        String filename = "a.jpg";
//        String fileExtensionName = filename.substring(filename.lastIndexOf(".")+1);//获取扩展文件名后缀
//        System.out.println(fileExtensionName);
//    }
}
