package com.xjdzy.controller;


import com.xjdzy.dto.Result;
import com.xjdzy.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    UploadFileService uploadFileService;

    @PostMapping("uploadFile")
    public Result UploadFileCon(@RequestPart("file") MultipartFile file){
        boolean res = uploadFileService.uploadFIle(file);
        log.info("Service处理结果："+res);
        if(res){
            return Result.success();
        }
        else{
            return Result.error("上传失败！");
        }
    }
}
