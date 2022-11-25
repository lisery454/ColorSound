package com.lisery.color_sound.dao;


import com.lisery.color_sound.entity.Sound;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISoundDao {
    Sound getSoundById(@Param("id") int id);

    void addSound(@Param("name") String name,
                  @Param("color") int color,
                  @Param("url") String url,
                  @Param("duration") String duration);

    List<Sound> getRandomSound(@Param("count") int count);
}
