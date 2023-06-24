package com.xjdzy.utils;

import com.xjdzy.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageToBase64Utils {

    /**
     * 判断文件格式、大小是否符合要求，若符合要求则将其转换为Base64格式
     * @param imageFile 文件
     * @return 图片文件转换为base64格式的字符串编码
     */
    public static String ImageToBase64(MultipartFile imageFile){
        // 判断
        if (imageFile == null) {
            return "0";
        }
        String fileName = imageFile.getOriginalFilename();
        System.out.println("文件名+后缀名："+fileName);
        System.out.println("文件大小："+imageFile.getSize());
        if(fileName == null || (!fileName.endsWith(".jpg") && !fileName.endsWith(".png") && !fileName.endsWith(".jpeg"))){
            return "1";
        }
        if (imageFile.getSize() >= 16 * 1024 * 1024) {
            return "2";
        }
        // 转换
        InputStream inputStream = null;
        try {
            inputStream = imageFile.getInputStream();
            byte[] data = new byte[inputStream.available()];
            int read = inputStream.read(data);
            System.out.println("读取内容大小："+read);
            String fileContentType = imageFile.getContentType();
            String base64String = Base64.getEncoder().encodeToString(data);
            return "data:"+fileContentType+";base64,"+base64String;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
