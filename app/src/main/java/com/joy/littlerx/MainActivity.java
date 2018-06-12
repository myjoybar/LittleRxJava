package com.joy.littlerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observable.Observable;
import com.joy.rxjava.observable.ObservableEmitter;
import com.joy.rxjava.observable.ObservableOnSubscribe;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.utils.RLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	public static final String TAG = "MainActivity";
	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initClickListener();
	}

	private void initView() {
		button1 = findViewById(R.id.btn_test1);
	}

	private void initClickListener() {
		button1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
			case R.id.btn_test1:
				test();
				break;

			default:
				break;

		}
	}

	private void test() {
		Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo("observable: onNext");
				emitter.onNext(1);
				RLog.printInfo("observable: onComplete");
				emitter.onComplete();
			}
		});
		Observer<Integer> observer = new Observer<Integer>() {


			@Override
			public void onSubscribe() {
				RLog.printInfo("Observer: onSubscribe");
			}

			@Override
			public void onNext(Integer value) {
				RLog.printInfo( "Observer: onNext,"+value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo( "Observer: onError,"+e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo( "Observer: onComplete");
			}
		};

		observable.subscribe(observer);
	}


	private void  test2(){
		Observer<String> observer = new Observer<String>() {

			@Override
			public void onSubscribe() {
				RLog.printInfo( "Observer: onSubscribe");
			}

			@Override
			public void onNext(String value) {
				RLog.printInfo( "Observer: onNext,"+value);
			}

			@Override
			public void onError(Throwable e) {
				RLog.printInfo( "Observer: onError,"+e.getMessage());
			}

			@Override
			public void onComplete() {
				RLog.printInfo( "Observer: onComplete");
			}
		};

		Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				RLog.printInfo( "Observable: onNext");
				emitter.onNext(1);
				RLog.printInfo( "Observable: onComplete");
				emitter.onComplete();
			}
		});
		Observable<String> observableMap = observable.map(new Function<Integer, String>() {
			@Override
			public String apply(Integer integer) throws Exception {
				return "AAAAAA";
			}
		});

		observableMap.subscribe(observer);


	}

}
