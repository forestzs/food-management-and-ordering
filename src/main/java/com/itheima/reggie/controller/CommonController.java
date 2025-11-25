package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return R
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //MultipartFile临时文件请求完毕会自动删除，需要转存
        //UUID生成随机文件名
        String randomFileName = UUID.randomUUID().toString();
        //获取原始文件名
        String originalFileName = file.getOriginalFilename();
        //截取原文件格式（.XXX）
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        //生成转存文件名
        String newFileName = randomFileName + suffix;
        //创建目录对象
        File dir = new File(basePath);
        //判断目录是否存在,不存在则创建
        if(!dir.exists()) {
            dir.mkdirs();
        }

        try {
            //临时文件转存到指定位置,这里basePath ！= dir,basePath在dir的下一级
            file.transferTo(new File(basePath + newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newFileName);
    }

    /**
     * 文件下载
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        try {
            //通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            //通过输出流将文件写回前端展示
            ServletOutputStream servletOutputStream = response.getOutputStream();

            response.setContentType("imag/jpeg");
            int len;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1) { //读文件
                servletOutputStream.write(bytes, 0, len);  //写文件
                servletOutputStream.flush();
            }
            servletOutputStream.close();
            fileInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
