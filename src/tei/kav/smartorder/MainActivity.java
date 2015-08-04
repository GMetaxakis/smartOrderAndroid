package tei.kav.smartorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@SuppressLint("HandlerLeak")
	public final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;
			if (message.equals("onPostExecute ok User")) {
				continueToOrders();
			} else if (message.startsWith("onPostExecute exception")) {
				showDialogError(message.substring(26));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	protected void showDialogError(String message) {
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage(message).setNeutralButton("ΟΚ", null)
				.setIcon(android.R.drawable.ic_dialog_alert).create().show();
	}

	protected void continueToOrders() {
		//Utils.userid = Utils.result;
		Intent myIntent = new Intent(MainActivity.this, OrdersActivity.class);
		MainActivity.this.startActivity(myIntent);
	}

	public void checkIpAndPort(View v) {
		String ip = ((EditText) findViewById(R.id.ipEditText)).getText()
				.toString();
		String port = ((EditText) findViewById(R.id.portEditText)).getText()
				.toString();
		Utils.username = ((EditText) findViewById(R.id.usernameEditText))
				.getText().toString();
		Utils.password = ((EditText) findViewById(R.id.passwordEditText))
				.getText().toString();

		boolean ipOk = Utils.checkIp(ip);
		boolean portOk = Utils.checkPort(port);

		if (ipOk && portOk) {
			MyHttpClient.setIp(ip);
			MyHttpClient.setPort(port);
			MyHttpClient.setHandler(mHandler);
			new MyHttpClient().execute("post","User");

		} else {
			if (!ipOk && !portOk)
				showDialogError("Μη αποδεκτή τιμή για ip και port");
			if (!ipOk)
				showDialogError("Μη αποδεκτή τιμή για ip");
			if (!portOk)
				showDialogError("Μη αποδεκτή τιμή για port");
		}
	}
}
