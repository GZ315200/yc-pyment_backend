package com.igeek.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Gyges on 2017/6/7.
 * 用于上传文件
 */
public class FTPUtil {

    public static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    public static final String ftoIp = PropertiesUtil.getProperty("ftp.server.ip");
    public static final String ftpUser = PropertiesUtil.getProperty("ftp.user");
    public static final String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public  static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftoIp,21,ftpUser,ftpPass);
        logger.info("开始连接FTP服务器");
        boolean result = ftpUtil.uploadFile(fileList,"img");
        logger.info("开始连接FTP服务器，结束上传，上传结果:{}",result);
        return result;
    }

    /**
     *
     * @param fileList
     * @param remotePath 上传的ftp服务器的更多的文件夹
     * @return
     */
    public boolean uploadFile(List<File> fileList,String remotePath) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
//                需要切换文件夹 配置存储文件的信息
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem:fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }

            } catch (IOException e) {
                logger.error("上传文件异常",e);
                e.printStackTrace();
            }finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }

        //链接服务器
        return uploaded;
    }

    public boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
           ftpClient.connect(ip);
           isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接服务器失败",e);
        }
        return isSuccess;
    }


    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
