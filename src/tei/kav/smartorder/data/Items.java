package tei.kav.smartorder.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Items {
	public int parent_id;
	public int id;
	public String name;
	public double price;
	
    public Items(int parent_id, int id, String name, double price)
    {
        this.parent_id = parent_id;
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
	public Items(JSONObject jsonObject) throws JSONException {
		this.parent_id = jsonObject.getInt("Parent_id");
		this.id = jsonObject.getInt("Id");
		this.name = jsonObject.getString("Name");
		this.price = jsonObject.getDouble("Price");
	}
	
	@Override
	public String toString(){
	  return name;
	}
}
