package com.robedev.museai.service;

import com.robedev.museai.data.api.OkHttpService;
import com.robedev.museai.interfaces.MuseumApiInterface;

public class MuseumApiImpl implements MuseumApiInterface {

    private OkHttpService okHttpClient;
    @Override
    public String getObjectById(String id) {
        return "objects/" + id;
    }
}
