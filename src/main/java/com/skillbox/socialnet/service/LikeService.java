package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RS.DefaultRS;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    public DefaultRS like() {
        return new DefaultRS();
    }

    public DefaultRS dislike() {
        return new DefaultRS();
    }

}
