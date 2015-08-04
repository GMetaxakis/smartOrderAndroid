package tei.kav.smartorder.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Characteristics {
	public int id;
	public String name;
	public double cost;
	public boolean selected = false;

	public Characteristics(int id, String name, double cost) {
		this.id = id;
		this.name = name;
		this.cost = cost;
	}
	
	public Characteristics(JSONObject jsonObject) throws JSONException {
		this.id = jsonObject.getInt("Id");
		this.name = jsonObject.getString("Name");
		this.cost = jsonObject.getDouble("Cost");
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void toggleSelected() {
		selected = !selected;
	}
}
