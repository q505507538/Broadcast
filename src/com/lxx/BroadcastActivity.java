package com.lxx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BroadcastActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	Context context;
	TextView myTextView;
	Button qibeiButton, pingtangButton, xiatuiButton, taituiButton,
			zuofansenButton, youfansenButton;
	MyReceiver receiver;
	IBinder serviceBinder;
	MyService mService;
	Intent intent;
	int value = 0;

	/************** service 命令 *********/
	static final int CMD_STOP_SERVICE = 0x01;// 停止服务
	static final int CMD_SEND_DATA = 0x02;// 发送数据
	static final int CMD_SYSTEM_EXIT = 0x03;// 退出程序
	static final int CMD_SHOW_TOAST = 0x04;// 界面上显示toast

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.main);
		setView(context);
		setListener();
		intent = new Intent(BroadcastActivity.this, MyService.class);
		startService(intent);
	}

	private void setView(Context context) {
		qibeiButton = (Button) findViewById(R.id.qibeiButton);
		pingtangButton = (Button) findViewById(R.id.pingtangButton);
		xiatuiButton = (Button) findViewById(R.id.xiatuiButton);
		taituiButton = (Button) findViewById(R.id.taituiButton);
		zuofansenButton = (Button) findViewById(R.id.zuofansenButton);
		youfansenButton = (Button) findViewById(R.id.youfansenButton);
	}

	private void setListener() {
		qibeiButton.setOnClickListener(this);
		pingtangButton.setOnClickListener(this);
		xiatuiButton.setOnClickListener(this);
		taituiButton.setOnClickListener(this);
		zuofansenButton.setOnClickListener(this);
		youfansenButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		byte command = 1;
		int value = 0x00;
		switch (v.getId()) {
		case R.id.qibeiButton:
			value = 0x31;
			break;
		case R.id.pingtangButton:
			value = 0x32;
			break;
		case R.id.xiatuiButton:
			value = 0x33;
			break;
		case R.id.taituiButton:
			value = 0x34;
			break;
		case R.id.zuofansenButton:
			value = 0x35;
			break;
		case R.id.youfansenButton:
			value = 0x36;
			break;
		}
		sendCmd(command, value);
	}

	public class SendButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			byte command = 1;
			int value = 0x31;
			sendCmd(command, value);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (receiver != null) {
			BroadcastActivity.this.unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.lxx");
		BroadcastActivity.this.registerReceiver(receiver, filter);
	}

	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.lxx")) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				if (cmd == CMD_SHOW_TOAST) {
					String str = bundle.getString("str");
					showToast(str);
				}

				else if (cmd == CMD_SYSTEM_EXIT) {
					System.exit(0);
				}

			}
		}
	}

	public void sendCmd(byte command, int value) {
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("cmd", CMD_SEND_DATA);
		intent.putExtra("command", command);
		intent.putExtra("value", value);
		sendBroadcast(intent);// 发送广播
	}

}