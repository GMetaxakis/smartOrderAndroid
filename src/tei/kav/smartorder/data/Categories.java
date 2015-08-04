package tei.kav.smartorder.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Categories {
	public int id;
	public String name;

	public Categories(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Categories(JSONObject jsonObject) throws JSONException {
		this.id = jsonObject.getInt("Id");
		this.name = jsonObject.getString("Name");
	}

	@Override
	public String toString() {
		return name;
	}
}
