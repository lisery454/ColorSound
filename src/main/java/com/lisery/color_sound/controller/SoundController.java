package com.lisery.color_sound.controller;

import com.lisery.color_sound.entity.Sound;
import com.lisery.color_sound.service.ISoundService;
import com.lisery.color_sound.service.impl.SoundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sound")
@Api(tags = "Sound")
public class SoundController {

    @Value("${web.sound-path}")
    private String soundPath;

    private final ISoundService soundService;

    public SoundController(ISoundService soundService) {
        this.soundService = soundService;
    }

    @ApiOperation("根据id获取sound信息")
    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    public Sound getSoundById(@ApiParam("id") @RequestParam int id) {
        return soundService.getSoundById(id);
    }

    @ApiOperation("随机获取sound信息")
    @RequestMapping(value = "/getRandom", method = RequestMethod.POST)
    public List<Sound> getRandomSound(@ApiParam("sound个数") @RequestParam int count) {
        return soundService.getRandomSound(count);
    }

    @ApiOperation("上传sound")
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadSound(@ApiParam("声音") @RequestPart("sound") MultipartFile sound,
                              @ApiParam("颜色") @RequestParam int color,
                              HttpServletRequest request) {
        // 获取文件名
        String originalFilename = sound.getOriginalFilename();
        // 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        String soundName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        // 判断是不是可以接收的文件类型
        //public final static String avatarFileSuffix = ".mp3.jpeg.png";
        //if (!FileSuffix.avatarFileSuffix.contains(suffix)) {
        //    return ResultBean.error(ResultEnum.FILE_TYPE_ERROR);
        //}
        // 用uuid作为新的文件名
        String newName = UUID.randomUUID() + "." + suffix;

        File folder = new File(soundPath);

        if (!folder.isDirectory())
            folder.mkdirs();

        try {
            // 文件保存
            sound.transferTo(new File(folder, newName));

            //更改数据库
            soundService.addSound(soundName, color, newName);

            // 返回上传文件的访问路径
            return request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/" + newName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }
}
