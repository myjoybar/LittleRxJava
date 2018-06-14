package com.joy.rxjava.schedulers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by joybar on 2018/6/13.
 */

public class AndroidSchedulers extends Scheduler {

	private static final String TAG = "NewThreadScheduler";
	private final Handler handler = new Handler(Looper.getMainLooper());

	public static AndroidSchedulers getInstance() {
		return AndroidSchedulersHolder.INSTANCE;
	}

	private static class AndroidSchedulersHolder {
		private static AndroidSchedulers INSTANCE = new AndroidSchedulers();
	}

	@Override
	public void scheduleDirect(Runnable runnable) {
		Message message = Message.obtain(handler, runnable);
		message.obj = this;
		handler.sendMessage(message);
	}

}
