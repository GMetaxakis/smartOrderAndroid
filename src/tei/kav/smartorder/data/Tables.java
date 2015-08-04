package tei.kav.smartorder.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tables {
	public int id;
	public ArrayList<Order> orders,openedOrders;
	public double cost;
	
	public Tables(int id){
		this.id= id;
		orders = new ArrayList<Order>();
		cost = 0;
	}
	
	public Tables(JSONObject jsonObject) throws JSONException{
		
		this.id = jsonObject.getInt("Table_id");
		orders = new ArrayList<Order>();
		openedOrders = new ArrayList<Order>();
		JSONArray ordersArray = jsonObject.getJSONArray("Orders");
		int ordersArraySize= ordersArray.length();
		for(int i=0;i<ordersArraySize;i++)
		{
			Order o = new Order(ordersArray.getJSONObject(i));
			orders.add(o);
			if(o.opened)
				openedOrders.add(o);
		}
		getCost();
	}
	
	public void getCost(){
		double cost = 0;
		for (Order o : orders){
			if(o.opened)
				cost += o.cost;
		}
		this.cost = cost;
	}
	
	@Override
	public String toString(){
		return id+"";
	}
}
