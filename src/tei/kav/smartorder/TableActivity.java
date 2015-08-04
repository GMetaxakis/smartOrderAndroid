package tei.kav.smartorder;

import tei.kav.smartorder.data.CustomOrderAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class TableActivity extends Activity {

	int table_id;
	TextView tableNumber;
	private ListView newOrders;
	private TextView newOrdersCountText;
	CustomOrderAdapter ordersAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		AliveHolder.add();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			table_id = extras.getInt("table_id");
		}
		tableNumber = (TextView) findViewById(R.id.tableNumberText);
		tableNumber.setText("Τραπέζι : " + table_id);

		newOrders = (ListView) findViewById(R.id.listNewOrders);
		newOrdersCountText = (TextView) findViewById(R.id.newOrdersCountText);

		updateOrdersList();
	}

	@Override
	protected void onStop(){
		AliveHolder.remove();
		super.onStop();
	}

	private int getIndex() {
		int index = 0;
		for (int i = 0; i < Utils.tables.size(); i++) {
			if (Utils.tables.get(i).id == table_id)
				index = i;
		}
		return index;
	}

	private void updateOrdersList() {

		ordersAdapter = new CustomOrderAdapter(this, true,
				Utils.tables.get(getIndex()).openedOrders);
		newOrders.setAdapter(ordersAdapter);
		newOrdersCountText.setText("Παραγγελίες : "
				+ Utils.tables.get(getIndex()).openedOrders.size());
	}

	public void removeOrderFromList(int mPosition) {
		String order_id = ""
				+ Utils.tables.get(getIndex()).openedOrders.get(mPosition).order_id;

		Utils.paidOrder(order_id);
		Utils.tables.get(getIndex()).openedOrders.remove(mPosition);
		updateOrdersList();
	}
}
