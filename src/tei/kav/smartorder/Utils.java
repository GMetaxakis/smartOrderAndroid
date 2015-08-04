package tei.kav.smartorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;

import tei.kav.smartorder.data.*;

public class Utils {
	public static final String LOGTAG = "SMARTORDER";
	public static String username = "";
	public static String password = "";
	public static String result;
	public static String userid;

	public static ArrayList<Categories> categories = new ArrayList<Categories>();
	public static ArrayList<Products> products = new ArrayList<Products>();
	public static ArrayList<Items> items = new ArrayList<Items>();
	public static ArrayList<Characteristics> characteristics = new ArrayList<Characteristics>();
	public static ArrayList<Tables> tables = new ArrayList<Tables>();

	public static void loadCategories(String result) throws JSONException {
		categories = new ArrayList<Categories>();
		JSONArray jsonarray = new JSONArray(result);
		for (int i = 0; i < jsonarray.length(); i++) {
			categories.add(new Categories(jsonarray.getJSONObject(i)));
		}
	}

	public static void loadProducts(String result) throws JSONException {
		products = new ArrayList<Products>();
		JSONArray jsonarray = new JSONArray(result);
		for (int i = 0; i < jsonarray.length(); i++) {
			products.add(new Products(jsonarray.getJSONObject(i)));
		}
	}

	public static ArrayList<Products> getSubProducts(int category_id) {
		ArrayList<Products> productsToReturn = new ArrayList<Products>();
		try {
			for (Products pr : products) {
				if (pr.parent_id == category_id)
					productsToReturn.add(pr);
			}
		} catch (Exception ex) {

		}
		return productsToReturn;
	}

	public static void loadItems(String result) throws JSONException {
		items = new ArrayList<Items>();
		JSONArray jsonarray = new JSONArray(result);
		for (int i = 0; i < jsonarray.length(); i++) {
			items.add(new Items(jsonarray.getJSONObject(i)));
		}
	}

	public static ArrayList<Items> getSubItems(int product_id) {
		ArrayList<Items> itemsToReturn = new ArrayList<Items>();
		try {
			for (Items it : items) {
				if (it.parent_id == product_id)
					itemsToReturn.add(it);
			}
		} catch (Exception ex) {

		}
		return itemsToReturn;
	}

	public static void loadCharacteristics(String result) throws JSONException {
		characteristics = new ArrayList<Characteristics>();
		JSONArray jsonarray = new JSONArray(result);
		for (int i = 0; i < jsonarray.length(); i++) {
			characteristics
					.add(new Characteristics(jsonarray.getJSONObject(i)));
		}
	}

	public static void loadTables(String result) throws JSONException {
		tables = new ArrayList<Tables>();
		JSONArray jsonarray = new JSONArray(result);
		for (int i = 0; i < jsonarray.length(); i++) {
			tables.add(new Tables(jsonarray.getJSONObject(i)));
		}
	}

	public static boolean checkPort(String port) {
		try {
			Integer.parseInt(port);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean checkIp(String ip) {
		return InetAddressUtils.isIPv4Address(ip);
	}

	static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void paidOrders(int table_index) {
		for (Order o : tables.get(table_index).openedOrders) {
			paidOrder(o.order_id + "");
		}
		for (Order o : tables.get(table_index).orders) {
			o.opened = false;
		}
		tables.get(table_index).openedOrders = new ArrayList<Order>();
	}

	public static void paidOrder(String order_id) {
		new MyHttpClient().execute("post", "RemoveOrder", order_id);
	}

	public static void addOrders(ArrayList<Order> newOrders) {
		for (Order o : newOrders) {
			addOrder(o);
		}

	}

	public static void addOrder(Order o) {
		int characteristicsSize = 0;
		if(o.characteristics!=null)
			characteristicsSize = o.characteristics.size();
		
		String [] params = new String[6+characteristicsSize];
		params[0] = "post";
		params[1] = "Order";
		params[2] = ""+o.table_id;
		params[3] = ""+o.item_id;
		params[4] = ""+Utils.userid;
		params[5] = ""+characteristicsSize;
		for(int i=6;i<characteristicsSize+6;i++){
			params[i] = ""+o.characteristics.get(i-6).id;
		}
		new MyHttpClient().execute(params);
	}
}
