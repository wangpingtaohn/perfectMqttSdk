package com.push.demo;

import com.perfect.mqtt.MqttConnection;
import com.perfect.mqtt.MqttConstant;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PushActivity extends Activity {
	private String mDeviceID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i("wpt", "PushActivity_onCreate");
		mDeviceID = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		((TextView) findViewById(R.id.target_text)).setText(mDeviceID);

		final Button startButton = ((Button) findViewById(R.id.start_button));
		final Button stopButton = ((Button) findViewById(R.id.stop_button));
		final Button cancelButton = ((Button) findViewById(R.id.cancel_button));
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String[] str = { "WANMEI/MESSAGE1", "WANMEI/MESSAGE2" };
				// String str = "WANMEI/MESSAGE2";
				MqttConnection.getInstance().init(PushActivity.this, str, "crzlZl", "crzlzl7986");

				startButton.setEnabled(false);
				stopButton.setEnabled(true);
			}
		});
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MqttConnection.getInstance().stopConnect(PushActivity.this);
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] str = { "WANMEI/MESSAGE1", "WANMEI/MESSAGE2" };
				// String str = "WANMEI/MESSAGE2";
				MqttConnection.getInstance()
						.unSubscribe(PushActivity.this, str);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("wpt", "PushActivity_onResume");
		SharedPreferences p = getSharedPreferences(MqttConstant.TAG,
				MODE_PRIVATE);
		boolean started = p.getBoolean(MqttConstant.PREF_STARTED, false);

		((Button) findViewById(R.id.start_button)).setEnabled(!started);
		((Button) findViewById(R.id.stop_button)).setEnabled(started);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MqttConnection.getInstance().unResgistBroadCast(PushActivity.this);
	}
}