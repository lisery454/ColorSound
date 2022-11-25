package com.lisery.color_sound.service.impl;

import com.lisery.color_sound.dao.ISoundDao;
import com.lisery.color_sound.entity.Sound;
import com.lisery.color_sound.service.ISoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoundService implements ISoundService {
    @Autowired
    private ISoundDao soundDao;

    @Override
    public Sound getSoundById(int id) {
        return soundDao.getSoundById(id);
    }

    @Override
    public void addSound(String name, int color, String url, String duration) {
        soundDao.addSound(name, color, url, duration);
    }

    @Override
    public List<Sound> getRandomSound(int count) {
        return soundDao.getRandomSound(count);
    }
}
