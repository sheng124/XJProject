package com.xjdzy.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

public class FtpUtil {

    // 服务器IP
    private static final String  FTP_ADDRESS = "121.36.202.123";
    // 端口号
    private static final int FTP_PORT = 21;
    // 用户名
    private static final String FTP_USERNAME = "xjdzyUser";
    // 密码
    private static final String FTP_PASSWORD = "0627.1243";
    // 文件存储路径
    private static final String FTP_PATH = "/var/ftp/work01";
    // 服务器端设置的baseurl
    private static final String BASE_URL = "http://121.36.202.123:8080/assert/";

    public  static String uploadFile(String newFileName, InputStream inputStream) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try{
            ftpClient.connect(FTP_ADDRESS,FTP_PORT);
            ftpClient.login(FTP_USERNAME,FTP_PASSWORD);
            int reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)){
                ftpClient.disconnect();
                System.out.println("登录失败！");
                return null;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.makeDirectory(FTP_PATH);
            ftpClient.changeWorkingDirectory(FTP_PATH);
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(newFileName,inputStream);
            return BASE_URL+newFileName;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } finally {
            ftpClient.logout();
        }
    }
}
