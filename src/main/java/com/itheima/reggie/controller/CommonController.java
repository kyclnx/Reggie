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

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是一个临时的文件，需要转存到指定的位置
        log.info(file.toString());
//        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid生成文件名，防止文件名重复造成覆盖       //动态将原始文件名后缀截取
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()){
            //目录不存在需要创建yige
            dir.mkdirs();
        }
        //将临时文件转存到指定的位置
        file.transferTo(new File(basePath+ fileName));
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) throws IOException {
        //输入流，通过输入流 读取文件内容，
        FileInputStream fileInputStream = new FileInputStream(new File(basePath+ name) );
        //输出流，通过输出流将文件写回浏览器。
        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg");

        byte[] bytes = new byte[1024];
        int length = 0;
        while((length = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,length);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }


}
