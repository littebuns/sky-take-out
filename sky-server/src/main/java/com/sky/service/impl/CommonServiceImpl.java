package com.sky.service.impl;

import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String name = UUID.randomUUID() + extension;
        try {
            return aliOssUtil.upload(file.getBytes(), name);
        } catch (IOException e) {
            log.error("上传oss失败", e);
            throw new RuntimeException(e);
        }

    }
}
