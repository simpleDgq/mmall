package com.mmall.service.impl;

import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     */
    public String upload(MultipartFile file, String path){
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1); // 获取文件扩展名
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName; // 使用UUID防止文件名重复
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){ // 创建upload目录
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path,uploadFileName);
        try {
            // 上传文件到web 服务器
            file.transferTo(targetFile); // 上传文件
            //文件已经上传到web服务器成功了
            // 上传文件到FTP服务器
            boolean uploaded =  FTPUtil.uploadFile(Arrays.asList(targetFile));
            if(!uploaded) { // 上传文件到FTP 服务器异常
                return null;
            }
            // 删除web服务器upload文件夹下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName(); // 返回上传成功之后的文件名
    }
}
