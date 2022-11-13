package com.lisery.color_sound.service;

import com.lisery.color_sound.entity.Sound;

import java.util.List;

public interface ISoundService {
    Sound getSoundById(int id);

    void addSound(String name, int color, String url);

    List<Sound> getRandomSound(int count);
}
