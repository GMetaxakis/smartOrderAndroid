package tei.kav.smartorder;

import java.util.ArrayList;

import tei.kav.smartorder.data.Categories;
import tei.kav.smartorder.data.Characteristics;
import tei.kav.smartorder.data.CustomOrderAdapter;
import tei.kav.smartorder.data.Items;
import tei.kav.smartorder.data.Order;
import tei.kav.smartorder.data.Products;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MakeOrderActivity extends Activity {
	private Spinner categorySpinner, productSpinner, itemSpinner, spinnerCount;
	private ListView newOrders;
	private TextView newOrdersCountText;
	private EditText textTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_order);
		AliveHolder.add();
		
		this.setTitle("Όνομα : " + Utils.username);
		categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
		productSpinner = (Spinner) findViewById(R.id.spinnerProduct);
		itemSpinner = (Spinner) findViewById(R.id.spinnerItem);
		spinnerCount = (Spinner) findViewById(R.id.spinnerCount);
		textTable = (EditText) findViewById(R.id.textTable);

		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				addProductsOnSpinner();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		productSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				addItemsOnSpinner();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		newOrders = (ListView) findViewById(R.id.listNewOrders);
		newOrdersCountText = (TextView) findViewById(R.id.newOrdersCountText);

		addCategoriesOnSpinner();

	}

	@Override
	protected void onStop(){
		AliveHolder.remove();
		super.onStop();
	}
	
	public void addCategoriesOnSpinner() {
		ArrayAdapter<Categories> dataAdapter = new ArrayAdapter<Categories>(
				this, android.R.layout.simple_spinner_item, Utils.categories);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(dataAdapter);
	}

	public void addProductsOnSpinner() {
		int category_id = ((Categories) categorySpinner.getSelectedItem()).id;

		ArrayAdapter<Products> dataAdapter = new ArrayAdapter<Products>(this,
				android.R.layout.simple_spinner_item,
				Utils.getSubProducts(category_id));
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productSpinner.setAdapter(dataAdapter);
	}

	public void addItemsOnSpinner() {
		int product_id = ((Products) productSpinner.getSelectedItem()).id;

		ArrayAdapter<Items> dataAdapter = new ArrayAdapter<Items>(this,
				android.R.layout.simple_spinner_item,
				Utils.getSubItems(product_id));
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		itemSpinner.setAdapter(dataAdapter);
	}

	ArrayAdapter<Characteristics> characteristicsAdapter;

	public void chooseCharacteristics(View v) {
		for (Characteristics ch : Utils.characteristics)
			ch.selected = false;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater.inflate(
				R.layout.characteristics_list, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle(R.string.characteristics);
		ListView lv = (ListView) convertView.findViewById(R.id.listView1);
		characteristicsAdapter = new ArrayAdapter<Characteristics>(this,
				android.R.layout.simple_list_item_multiple_choice,
				Utils.characteristics);
		lv.setAdapter(characteristicsAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Characteristics ch = characteristicsAdapter.getItem(position);
				ch.toggleSelected();
			}
		});
		alertDialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						for (Characteristics ch : Utils.characteristics)
							ch.selected = false;
					}
				});

		alertDialog.show();
	}

	CustomOrderAdapter ordersAdapter;
	ArrayList<Order> newOrdersArrayList = new ArrayList<Order>();

	private void updateOrdersList() {
		ordersAdapter = new CustomOrderAdapter(this, false, newOrdersArrayList);
		newOrders.setAdapter(ordersAdapter);
		newOrdersCountText
				.setText("Παραγγελίες : " + newOrdersArrayList.size());
	}

	public void Cancel(View v) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

	public void Insert(View v) {
		try {
			int count = Integer.parseInt(spinnerCount.getSelectedItem()
					.toString());

			Items selectedItem = (Items) itemSpinner.getSelectedItem();
			String name = selectedItem.name;
			int size = Utils.characteristics.size();
			ArrayList<Characteristics> characteristics = new ArrayList<Characteristics>();

			if (characteristicsAdapter != null) {
				for (int i = 0; i < size; i++) {
					if (characteristicsAdapter.getItem(i).selected) {
						characteristics.add(characteristicsAdapter.getItem(i));
					}
				}
			}
			// Order(int item_id, String name, double item_price, int table_id,
			// ArrayList<Characteristics> characteristics) {
			for (int i = 0; i < count; i++) {
				newOrdersArrayList.add(new Order(selectedItem.id, name,
						selectedItem.price, Integer.parseInt(textTable
								.getText().toString()), characteristics));
			}
			
			updateOrdersList();
		} catch (Exception ex) {

		}
	}

	public void removeOrderFromList(int position) {
		newOrdersArrayList.remove(position);
		updateOrdersList();
	}

	public void SentOrder(View v) {
		Utils.addOrders(newOrdersArrayList);
		newOrdersArrayList.clear();
		updateOrdersList();
	}

	public void deleted(View v) {
		Insert(v);

		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", "test");
		setResult(RESULT_OK, returnIntent);
		finish();
	}
}