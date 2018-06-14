package com.joy.rxjava.schedulers;

/**
 * Created by joybar on 2018/6/13.
 */

public final class Schedulers {
	public static final Scheduler IO = IoScheduler.getInstance();
	public static final Scheduler NEW_THREAD = NewThreadScheduler.getInstance();
	public static final Scheduler ANDROID_MAIN_THREAD = AndroidSchedulers.getInstance();


}
