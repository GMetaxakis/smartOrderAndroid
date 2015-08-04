package tei.kav.smartorder.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import tei.kav.smartorder.Utils;

public class Order {
	// "Order_id":1,"Items_id":1,"Opened":true,"Cost":1.5,"Datetime":"2015-04-02 19:55:32"
	public int order_id;
	public int item_id;
	public String item_name;
	public boolean opened;
	public double cost;
	public String datetime;
	public int table_id;
	public ArrayList<Characteristics> characteristics;

	public Order(int item_id, String name, double item_price, int table_id,
			ArrayList<Characteristics> characteristics) {
		double extra = 0;
		String extraName = "";
		
		for(Characteristics c : characteristics){
			extra += c.cost;
			extraName += " "+c.name;
		}
		this.characteristics =  characteristics;
		
		this.item_id = item_id;
		this.opened = true;

		this.item_name = name + extraName;
		this.table_id = table_id;
		this.cost = item_price + extra;
	}

	public Order(int item_id, String item_name, double cost, int table_id) {
		this.item_id = item_id;
		this.opened = true;
		this.item_name = item_name;
		this.table_id = table_id;
		this.cost = cost;
	}

	public Order(int order_id, int item_id, boolean opened, double cost,
			String datetime) {
		this.order_id = order_id;
		this.item_id = item_id;

		setItemName();

		this.opened = opened;
		this.cost = cost;
		this.datetime = datetime;
	}

	public Order(JSONObject jsonObject) throws JSONException {
		this.order_id = jsonObject.getInt("Order_id");
		this.item_id = jsonObject.getInt("Items_id");

		setItemName();

		this.opened = jsonObject.getBoolean("Opened");
		this.cost = jsonObject.getDouble("Cost");
		this.datetime = jsonObject.getString("Datetime");
	}

	private void setItemName() {
		item_name = "";
		for (Items i : Utils.items)
			if (i.id == item_id)
				item_name = i.name;
	}

	@Override
	public String toString() {
		return item_name;
	}
}
