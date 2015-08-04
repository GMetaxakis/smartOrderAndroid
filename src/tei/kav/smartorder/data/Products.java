package tei.kav.smartorder.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Products {
	public int parent_id;
	public int id;
	public String name;

	public Products(int id, int parent_id, String name) {
		this.parent_id = parent_id;
		this.id = id;
		this.name = name;
	}
	public Products(JSONObject jsonObject) throws JSONException {
		this.parent_id = jsonObject.getInt("Parent_id");
		this.id = jsonObject.getInt("Id");
		this.name = jsonObject.getString("Name");
	}
	
	@Override
	public String toString(){
	  return name;
	}
}
