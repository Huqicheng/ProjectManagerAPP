package com.example.huqicheng.nao;

import android.graphics.Bitmap;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.utils.BitmapUtils;
import com.example.huqicheng.utils.HttpUtils;

import org.apache.http.HttpEntity;

import java.io.InputStream;

/**
 * Created by huqicheng on 2017/11/8.
 */

public class ImageNao {

    public Bitmap getImage(String path){
        HttpEntity entity = HttpUtils.execute(Config.SERVER_IP+"/"+path,null,HttpUtils.GET);

        if(entity == null) return null;

        InputStream is = HttpUtils.getStream(entity);

        Bitmap bm = BitmapUtils.fromStream(is,2);

        return bm;
    }
}
