package com.joy.rxjava.schedulers;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by joybar on 2018/6/13.
 */

public class IoScheduler extends Scheduler {
	private static final String TAG = "NewThreadScheduler";
	private static final int PRIORITY = 5;

	public static IoScheduler getInstance(){
		return IoSchedulerHolder.INSTANCE;
	}

	private static class IoSchedulerHolder{
		private static IoScheduler INSTANCE = new IoScheduler();
	}

	@Override
	public void scheduleDirect(Runnable runnable) {
		executorService().submit(runnable);
	}

	private static ExecutorService executorService;

	private static synchronized ExecutorService executorService() {
		if (executorService == null) {
			executorService = Executors.newScheduledThreadPool(PRIORITY, new IOThreadFactory(PRIORITY));
		}
		return executorService;
	}

	private static class IOThreadFactory implements ThreadFactory{

		final int priority;

		public IOThreadFactory( int priority) {
			this.priority = priority;
		}

		@Override
		public Thread newThread(@NonNull Runnable runnable) {
			final AtomicInteger mCount = new AtomicInteger(1);
			Thread result = new Thread(runnable, "RxJava IO Thread #" + mCount.getAndIncrement());
			result.setPriority(priority);
			result.setDaemon(true);
			return result;
		}
	}
}
