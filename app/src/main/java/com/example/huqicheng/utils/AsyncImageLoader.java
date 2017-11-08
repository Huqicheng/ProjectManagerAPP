package com.example.huqicheng.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;


import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.example.huqicheng.nao.ImageNao;

public class AsyncImageLoader {
	private ArrayList<ImageLoadTask> tasks;
	private boolean isLoop;
	private Thread workThread;
	private Handler handler;
	ImageNao imageNao;
	private HashMap<String, SoftReference<Bitmap>> caches;

	public AsyncImageLoader(final Callback callback) {
		tasks = new ArrayList<ImageLoadTask>();
		imageNao = new ImageNao();
		isLoop = true;
		caches = new HashMap<String, SoftReference<Bitmap>>();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				ImageLoadTask task = (ImageLoadTask) msg.obj;
				callback.imageLoaded(task);
			};

		};

		workThread = new Thread() {
			@Override
			public void run() {
				while (isLoop) {
					// task queue is not empty
					while (!tasks.isEmpty()) {
						// get the first task in the queue
						ImageLoadTask task = tasks.remove(0);

						// ִload image here
						task.bm = imageNao.getImage(task.path);

						if(task.bm == null) return;

						// handle result of the async task
						Message msg = Message.obtain();
						msg.obj = task;
						handler.sendMessage(msg);

						// put bitmap in the cache
						caches.put(task.path,
								new SoftReference<Bitmap>(task.bm));
						// persist the bitmap
						BitmapUtils.save(task.bm, task.path);
					}
					// wait for this task to be finished
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		workThread.start();
	}

	public Bitmap loadImage(String path) {
		Bitmap bm = null;
		// bitmap is in the cache?
		if (caches.containsKey(path)) {
			bm = caches.get(path).get();
			if (bm != null) {
				return bm;
			} else {
				caches.remove(path);
			}
		}
		//bitmap is persisted?
		bm = BitmapUtils.fromFile(path);
		if(bm!=null){
			return bm;
		}

		// bitmap not existed, then create a new task to load it from server
		ImageLoadTask task = new ImageLoadTask();
		task.path = path;

		// if task is existed, return
		if (!tasks.contains(task)) {
			tasks.add(task);

			synchronized (workThread) {
				try {
					workThread.notify();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public class ImageLoadTask {
		private String path;
		private Bitmap bm;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public Bitmap getBm() {
			return bm;
		}

		public void setBm(Bitmap bm) {
			this.bm = bm;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ImageLoadTask other = (ImageLoadTask) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			return true;
		}

		private AsyncImageLoader getOuterType() {
			return AsyncImageLoader.this;
		}

	}

	public interface Callback {
		void imageLoaded(ImageLoadTask task);
	}
}
