package tei.kav.smartorder;

import tei.kav.smartorder.data.CustomTableAdapter;
import tei.kav.smartorder.data.Order;
import tei.kav.smartorder.data.Tables;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrdersActivity extends Activity {
	int onPostExecuteCounter = 0;
	ProgressDialog dialog;
	@SuppressLint("HandlerLeak")
	public final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;

			if (message.equals("onPostExecute ok Tables")) {
				
				//if (onPostExecuteCounter == 4) {
					dialog.dismiss();
					ShowTables();
				//} else {
				//	onPostExecuteCounter++;
					// showWaiting
				//}
			} else if (message.startsWith("onPostExecute exception")) {

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		AliveHolder.add();
		
		this.setTitle("Όνομα : " + Utils.username);

		MyHttpClient.setHandler(mHandler);

		new MyHttpClient().execute("get", "Categories");
		new MyHttpClient().execute("get", "Products");
		new MyHttpClient().execute("get", "Items");
		new MyHttpClient().execute("get", "Characteristics");
		new MyHttpClient().execute("get", "Tables");
		dialog = ProgressDialog.show(this, "Loading", "Please wait...", true);
	}
	
	@Override
	protected void onStop(){
		AliveHolder.remove();
		super.onStop();
	}

	ListView list;
	CustomTableAdapter adapter;
	TextView moneyToGetText, moneyOnHandText;
	double moneyToGet = 0, moneyOnHand = 0;

	protected void ShowTables() {
		list = (ListView) findViewById(R.id.listTables);
		moneyToGetText = (TextView) findViewById(R.id.moneyToGetText);
		moneyOnHandText = (TextView) findViewById(R.id.moneyOnHandText);

		adapter = new CustomTableAdapter(this, Utils.tables, getResources());
		list.setAdapter(adapter);

		setMoney();

	}

	private void setMoney() {
		moneyToGet = 0;
		moneyOnHand = 0;
		
		for (Tables t : Utils.tables) {
			for (Order o : t.orders) {
				if (o.opened)
					moneyToGet += o.cost;
				else
					moneyOnHand += o.cost;
			}
		}
		moneyToGetText.setText("Λεφτά να πάρεις : " + moneyToGet + "€");
		moneyOnHandText.setText("Λεφτά στο ταμείο : " + moneyOnHand + "€");
	}

	public void onItemClick(int mPosition) {
		Tables table = (Tables) Utils.tables.get(mPosition);

		Intent myIntent = new Intent(this, TableActivity.class);
		myIntent.putExtra("table_id", table.id);
		startActivity(myIntent);
	}

	@Override
	public void onBackPressed() {
		Log.i(Utils.LOGTAG, "on back pressed on orders activity");
		super.onBackPressed();
	}

	public void NewOrder(View v) {
		Intent myIntent = new Intent(this, MakeOrderActivity.class);
		startActivity(myIntent);
	}

	public void UpdateOrders() {
		ShowTables();
	}
}
