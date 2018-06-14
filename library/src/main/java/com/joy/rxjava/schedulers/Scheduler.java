package com.joy.rxjava.schedulers;

/**
 * Created by joybar on 2018/6/13.
 */

public abstract class Scheduler {

	public abstract void scheduleDirect(Runnable runnable);

	public static abstract class Worker extends NamedRunnable {

	}

	public static abstract class NamedRunnable implements Runnable {
		@Override
		public final void run() {
			execute();
		}
		protected abstract void execute();
	}

}
