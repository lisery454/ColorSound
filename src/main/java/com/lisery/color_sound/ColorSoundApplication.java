package com.lisery.color_sound;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.lisery.color_sound.dao")
public class ColorSoundApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColorSoundApplication.class, args);
    }

}
