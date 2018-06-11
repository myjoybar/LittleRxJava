package com.joy.littlerx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.joy.rxjava.observable.Observable;
import com.joy.rxjava.observable.ObservableEmitter;
import com.joy.rxjava.observable.ObservableOnSubscribe;
import com.joy.rxjava.observer.Observer;

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
				Log.d(TAG, "onNext");
				emitter.onNext(1);
			}
		});

		Observer<Integer> observer = new Observer<Integer>() {


			@Override
			public void onSubscribe() {
				Log.d(TAG, "onSubscribe");
			}

			@Override
			public void onNext(Integer value) {
				Log.d(TAG, "onNext " + value);
			}

			@Override
			public void onError(Throwable e) {
				Log.d(TAG, "onError " + e.getMessage());
			}

			@Override
			public void onComplete() {
				Log.d(TAG, "onComplete");
			}
		};
		observable.subscribe(observer);
	}


}
