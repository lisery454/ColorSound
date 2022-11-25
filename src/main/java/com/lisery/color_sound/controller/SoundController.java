package com.lisery.color_sound.controller;

import com.lisery.color_sound.entity.Sound;
import com.lisery.color_sound.service.ISoundService;
import com.lisery.color_sound.utils.ResponsePackage;
import com.lisery.color_sound.utils.ResponseStatusCode;
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
    public ResponsePackage<Sound> getSoundById(@ApiParam("id") @RequestParam int id) {
        Sound sound = soundService.getSoundById(id);
        if (sound == null) return new ResponsePackage<>(ResponseStatusCode.FAIL, "找不到该id对应的Sound", null);
        else return new ResponsePackage<>(ResponseStatusCode.SUCCESS, "获取成功", soundService.getSoundById(id));
    }

    @ApiOperation("随机获取sound信息")
    @RequestMapping(value = "/getRandom", method = RequestMethod.POST)
    public ResponsePackage<List<Sound>> getRandomSound() {
        List<Sound> randomSounds = soundService.getRandomSound(10);
        return new ResponsePackage<>(ResponseStatusCode.SUCCESS, "成功返回" + randomSounds.size() + "条数据", randomSounds);
    }

    @ApiOperation("上传sound")
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponsePackage<String> uploadSound(@ApiParam("声音") @RequestPart("sound") MultipartFile sound,
                                               @ApiParam("颜色") @RequestParam int color,
                                               @ApiParam("时长") @RequestParam String duration,
                                               @ApiParam("声音名字") @RequestParam String name,
                                               HttpServletRequest request) {

        if (sound == null) {
            return new ResponsePackage<>(ResponseStatusCode.FAIL, "声音文件不能为空", null);
        }

        // 获取文件名
        String originalFilename = sound.getOriginalFilename();

        if (originalFilename == null) {
            return new ResponsePackage<>(ResponseStatusCode.FAIL, "声音文件名不能为空", null);
        }

        // 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        // 判断是不是可以接收的文件类型
        if (!suffix.equalsIgnoreCase("mp3")) {
            return new ResponsePackage<>(ResponseStatusCode.FAIL, "文件格式不符，应该是MP3", null);
        }

        // 用uuid作为新的文件名
        String url = UUID.randomUUID() + "." + suffix;
        File folder = new File(soundPath);
        if (!folder.isDirectory()) folder.mkdirs();

        try {
            // 文件保存
            sound.transferTo(new File(folder, url));
            //更改数据库
            soundService.addSound(name, color, url, duration);
            // 返回上传文件的访问路径
            return new ResponsePackage<>(ResponseStatusCode.SUCCESS, "成功上传Sound", request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/" + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponsePackage<>(ResponseStatusCode.FAIL, "上传失败", null);
    }
}
