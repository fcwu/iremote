package com.doro.iremote;

import org.json.JSONException;
import org.json.JSONObject;

import com.doro.iremote.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

public class RemoteActivity extends Activity {
	private static final String TAG = "RemoteActivity";
	private SendCommandTask mSendCommandTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote);
	}
	
	private void sendCommand(String str) {
		mSendCommandTask = new SendCommandTask();
		mSendCommandTask.execute(str);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.digit_1: sendCommand("1"); break;
		case R.id.digit_2: sendCommand("2"); break;
		case R.id.digit_3: sendCommand("3"); break;
		case R.id.digit_4: sendCommand("4"); break;
		case R.id.digit_5: sendCommand("5"); break;
		case R.id.digit_6: sendCommand("6"); break;
		case R.id.digit_7: sendCommand("7"); break;
		case R.id.digit_8: sendCommand("8"); break;
		case R.id.digit_9: sendCommand("9"); break;
		case R.id.digit_0: sendCommand("0"); break;
		case R.id.power:   sendCommand("p"); break;
		case R.id.source:  sendCommand("e"); break;
		case R.id.enter:   sendCommand("d"); break;
		case R.id.channel_up:   sendCommand("w"); break;
		case R.id.channel_down: sendCommand("s"); break;
		case R.id.volume_up:    sendCommand("q"); break;
		case R.id.volume_down:  sendCommand("a"); break;
			
		}
	}
	
	private class SendCommandTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
//			showProgress(VIEW_LOADING);
		}
		@Override
		protected Void doInBackground(String... params) {
			HttpRequestBlocking conn = new HttpRequestBlocking(
					String.format("http://%s/keys/%s", 
							((AppIRemote) getApplication()).getServer(),
							params[0]));
			conn.doGet(null, null);
			if (!conn.success()) {
				Log.e(TAG, "Network error");
				return null;
			}
			try {
				JSONObject result = new JSONObject(conn.response());
				Log.d(TAG, result.getString("result"));
			} catch (JSONException e) {
				Log.e(TAG, "Parse json error: " + e.toString());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Void foo) {
		}

		@Override
		protected void onCancelled() {
		}
	}
}
