package com.sky.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {


    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public String upload(MultipartFile file){
        return null;
    }

}
