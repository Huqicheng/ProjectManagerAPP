package com.example.huqicheng.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {
	public static Bitmap fromStream(InputStream is, int inSampleSize) {
		Bitmap bm = null;
		Options opts = new Options();
		opts.inSampleSize = inSampleSize;
		bm = BitmapFactory.decodeStream(is, null, opts);
		return bm;
	}

	public static Bitmap fromStream(InputStream is) {
		Bitmap bm = null;
		bm = BitmapFactory.decodeStream(is);
		return bm;
	}

	public static Bitmap fromFile(String path) {
		Bitmap bm = null;
		if (path != null) {
			bm = BitmapFactory.decodeFile("/mnt/sdcard/" + path);
		}
		return bm;
	}

	public static void save(Bitmap bm, String path) {
		try {
			if (bm != null && path != null) {
				File file = new File("/mnt/sdcard/" + path);
				File dir = file.getParentFile();

				if (!dir.exists()) {
					dir.mkdirs();
				}

				if (!file.exists()) {
					file.createNewFile();
				}

				FileOutputStream stream = new FileOutputStream(file);
				bm.compress(CompressFormat.JPEG, 100, stream);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
