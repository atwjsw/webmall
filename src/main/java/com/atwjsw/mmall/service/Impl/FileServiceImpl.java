package com.atwjsw.mmall.service.Impl;

import com.atwjsw.mmall.service.IFileService;
import com.atwjsw.mmall.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by wenda on 6/4/2017.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {

        //获取原始文件名
        String fileName = file.getOriginalFilename();
        //获取原始文件扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //通过UUID生成上传文件名，原因在于避免不同用户上传的原始文件名相同造成的冲突
        String uploadFileName = UUID.randomUUID().toString()+ "." + fileExtensionName;
        logger.info("开始上传文件, 原始文件名: {}, 上传的路径：{}, 新文件名: {}", fileName, path, uploadFileName );
        //创建上传目录
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //上传文件
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功了

            //将targetFile上传到我们的FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完之后，删除upload下面的文件， 保持Tomcat服务器的存储空间
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常", e);
        }
        return targetFile.getName();
    }
}
