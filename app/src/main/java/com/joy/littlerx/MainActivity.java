package com.joy.littlerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.joy.rxjava.functions.BiFunction;
import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observable.Observable;
import com.joy.rxjava.observable.ObservableEmitter;
import com.joy.rxjava.observable.ObservableOnSubscribe;
import com.joy.rxjava.observable.ObservableSource;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.schedulers.Schedulers;
import com.joy.rxjava.utils.RLog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	public static final String TAG = "MainActivity";
	private Button buttonBase;
	private Button buttonMap;
	private Button buttonFlapMap;
	private Button buttonZip;
	private Button buttonThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initClickListener();
	}

	private void initView() {
		buttonBase = findViewById(R.id.btn_base);
		buttonMap = findViewById(R.id.btn_map);
		buttonFlapMap = findViewById(R.id.btn_flap_map);
		buttonZip = findViewById(R.id.btn_zip);
		buttonThread = findViewById(R.id.btn_thread);
	}

	private void initClickListener() {
		buttonBase.setOnClickListener(this);
		buttonMap.setOnClickListener(this);
		buttonFlapMap.setOnClickListener(this);
		buttonZip.setOnClickListener(this);
		buttonThread.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_base:
				testBase();
				break;
			case R.id.btn_map:
				testMap();
				break;
			case R.id.btn_flap_map:
				test3FlapMapSimple();
				break;
			case R.id.btn_zip:
				testZip();
				break;
			case R.id.btn_thread:
				testThread();
				break;
			default:
				break;

		}
	}

	private void testBase() {
		Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("emitter发送第一个onNext，value = 1");
				emitter.onNext(1);
				RLog.printInfo("emitter发送第二个onNext，value = 2");
				emitter.onNext(2);
				RLog.printInfo("emitter发送onComplete");
				emitter.onComplete();
			}
		});
		Observer<Integer> observer = new Observer<Integer>() {

			@Override
			public void onSubscribe() {
				RLog.printInfo("Observer被订阅");
			}

			@Override
			public void onNext(Integer value) {
				RLog.printInfo("Observer接收到onNext，value = " + value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo("Observer接收到onError，errorMsg = " + e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo("Observer接收到onComplete");
			}
		};

		observable.subscribe(observer);
	}


	private void testMap() {

		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("emitter发送第一个onNext，value = 1");
				emitter.onNext(1);
				RLog.printInfo("emitter发送第二个onNext，value = 2");
				emitter.onNext(2);
				RLog.printInfo("emitter发送onComplete");
				emitter.onComplete();
			}
		}).map(new Function<Integer, String>() {
			@Override
			public String apply(Integer integer) throws Exception {
				return "A" + integer;
			}
		}).subscribe(new Observer<String>() {

			@Override
			public void onSubscribe() {
				RLog.printInfo("Observer被订阅");
			}

			@Override
			public void onNext(String value) {
				RLog.printInfo("Observer接收到onNext，被转换之后的value = " + value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo("Observer接收到onError，errorMsg = " + e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo("Observer接收到onComplete");
			}
		});
	}

	private void test3FlapMapSimple() {

		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("emitter发送第一个onNext，value = 1");
				emitter.onNext(1);
				RLog.printInfo("emitter发送onComplete");
				emitter.onComplete();
			}
		}).flatMapSimple(new Function<Integer, ObservableSource<? extends String>>() {
			@Override
			public ObservableSource<? extends String> apply(Integer integer) throws Exception {
				final List<String> list = new ArrayList<>();
				for (int i = 0; i < 2; i++) {
					list.add("I am value " + i);
				}
				return Observable.fromIterableSimple(list);
			}
		}).subscribe(new Observer<String>() {

			@Override
			public void onSubscribe() {
				RLog.printInfo("Observer被订阅");
			}

			@Override
			public void onNext(String value) {
				RLog.printInfo("Observer接收到onNext，被转换之后的value = " + value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo("Observer接收到onError，errorMsg = " + e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo("Observer接收到onComplete");
			}
		});

	}

	private void testZip() {
		Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("observable1 emitter发送第一个onNext，value = 1");
				emitter.onNext(1);
				RLog.printInfo("observable1 emitter发送第二个onNext，value = 2");
				emitter.onNext(2);
				RLog.printInfo("observable1 emitter发送onComplete");
				emitter.onComplete();
			}
		});

		Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				RLog.printInfo("observable2 emitter发送第一个onNext，value = A");
				emitter.onNext("A");
				RLog.printInfo("observable2 emitter发送第二个onNext，value = B");
				emitter.onNext("B");
				//RLog.printInfo("observable2 emitter发送onComplete");
				//emitter.onComplete();
			}
		});
		Observable<String> observableZip = Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
			@Override
			public String apply(Integer integer, String s) throws Exception {
				return integer + s;
			}
		});

		observableZip.subscribe(new Observer<String>() {

			@Override
			public void onSubscribe() {
				RLog.printInfo("Observer被订阅");
			}

			@Override
			public void onNext(String value) {
				RLog.printInfo("Observer接收到onNext，被Zip转换之后的value = " + value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo("Observer接收到onError，errorMsg = " + e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo("Observer接收到onComplete");
			}
		});


	}


	private void testThread() {

		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("emitter发送第一个onNext，value = 1");
				emitter.onNext(1);
				RLog.printInfo("emitter发送onComplete");
				emitter.onComplete();
			}
		}).subscribeOn(Schedulers.NEW_THREAD)
		  .subscribeOn(Schedulers.IO)
		  .subscribeOn(Schedulers.NEW_THREAD)
		  .subscribeOn(Schedulers.IO)
		  .observeOn(Schedulers.IO)
		  .map(new Function<Integer, String>() {
				@Override
				public String apply(Integer integer) throws Exception {
					RLog.printInfo("切换线程");
					return "切换线程" + integer;
				}
		}).observeOn(Schedulers.ANDROID_MAIN_THREAD)
		  .subscribe(new Observer<String>() {

			  @Override
			  public void onSubscribe() {
				  RLog.printInfo("Observer被订阅");
			  }

			  @Override
			  public void onNext(String value) {
				  RLog.printInfo("Observer接收到onNext，被转换之后的value = " + value);
			  }

			  @Override
			  public void onError(Throwable e) {
				  RLog.printInfo("Observer接收到onError，errorMsg = " + e.getMessage());
			  }

			  @Override
			  public void onComplete() {
				  RLog.printInfo("Observer接收到onComplete");
			  }
		});

	}

	private void testThread2(){
		Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("observable1 emitter发送第一个onNext，value = 1");
				emitter.onNext(1);
				RLog.printInfo("observable1 emitter发送第二个onNext，value = 2");
				emitter.onNext(2);
				RLog.printInfo("observable1 emitter发送onComplete");
				emitter.onComplete();
			}
		}).subscribeOn(Schedulers.NEW_THREAD);

		Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				RLog.printInfo("observable2 emitter发送第一个onNext，value = A");
				emitter.onNext("A");
				RLog.printInfo("observable2 emitter发送第二个onNext，value = B");
				emitter.onNext("B");
				//RLog.printInfo("observable2 emitter发送onComplete");
				//emitter.onComplete();
			}
		}).subscribeOn(Schedulers.IO);
		Observable<String> observableZip = Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
			@Override
			public String apply(Integer integer, String s) throws Exception {
				return integer + s;
			}
		}).observeOn(Schedulers.ANDROID_MAIN_THREAD);

		observableZip.subscribe(new Observer<String>() {

			@Override
			public void onSubscribe() {
				RLog.printInfo("Observer被订阅");
			}

			@Override
			public void onNext(String value) {
				RLog.printInfo("Observer接收到onNext，被Zip转换之后的value = " + value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo("Observer接收到onError，errorMsg = " + e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo("Observer接收到onComplete");
			}
		});
	}

}
