package com.xjdzy.service.Implement;

import com.xjdzy.service.UploadFileService;
import com.xjdzy.utils.FtpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UploadFileServiceImplements implements UploadFileService {
    @Override
    public boolean uploadFIle(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            if(fileName == null)
                return false;
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString().replace("-","")+suffix;
            return FtpUtil.uploadFile(newFileName,inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
