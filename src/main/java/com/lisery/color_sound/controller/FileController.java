package com.lisery.color_sound.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/file")
@Api(tags = "File控制器")
public class FileController {
    //绑定文件上传路径到uploadPath
    @Value("${web.upload-path}")
    private String uploadPath;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");


    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("上传文件")
    public String upload(@ApiParam("文件") @RequestPart("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {
        // 在 uploadPath 文件夹中通过日期对上传的文件归类保存
        // 比如：/2019/06/06/cf13891e-4b95-4000-81eb-b6d70ae44930.png
//        String format = sdf.format(new Date());
//        File folder = new File(uploadPath + format);
        File folder = new File(uploadPath);

        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        // 对上传的文件重命名，避免文件重名
        String oldName = uploadFile.getOriginalFilename();
        String newName = oldName;
//        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            // 文件保存
            uploadFile.transferTo(new File(folder, newName));

            // 返回上传文件的访问路径
            return request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/" + newName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }


    @RequestMapping(value = "/download/{filename}", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("下载文件")
    public String downLoad(@ApiParam("文件名") @PathVariable("filename") String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(uploadPath + "/" + filename);
        //System.out.println(uploadPath + "/" + filename);
        //判断文件是否存在，其实不用判断，因为不存在也设置了对应的异常处理
        if (file.exists()) {  //文件存在
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(filename, "UTF-8"));  //设置下载文件名，解决文件名中文乱码

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //输入流
            BufferedInputStream bis = null;
            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int length;
                while ((length = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }
                fis.close();
                bis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "文件不存在！";
            } catch (IOException e) {
                e.printStackTrace();
                return "下载失败！";
            }

            return null;  //下载成功
        }

        return "文件不存在";
    }
}
