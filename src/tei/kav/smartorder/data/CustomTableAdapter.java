package tei.kav.smartorder.data;

import java.util.ArrayList;

import tei.kav.smartorder.OrdersActivity;
import tei.kav.smartorder.R;
import tei.kav.smartorder.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomTableAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<Tables> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	Tables tempValues = null;
	int i = 0;

	public CustomTableAdapter(Activity a, ArrayList<Tables> d,
			Resources resLocal) {

		activity = a;
		data = d;
		res = resLocal;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.table_item, null);

			holder = new ViewHolder();
			holder.tableId = (TextView) vi.findViewById(R.id.tableIdText);
			holder.ordersCount = (TextView) vi
					.findViewById(R.id.ordersCountText);
			holder.cost = (TextView) vi.findViewById(R.id.costText);
			holder.image = (ImageView) vi.findViewById(R.id.cashImage);
			
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (data.size() <= 0) {
			holder.tableId.setText("No Data");

		} else {
			tempValues = null;
			tempValues = (Tables) data.get(position);

			holder.tableId.setText("Τραπέζι :" + tempValues.id);
			holder.ordersCount.setText("Παραγγελίες : "
					+ tempValues.openedOrders.size());
			holder.cost.setText("Κόστος : " + tempValues.cost + "€");
			holder.image.setOnClickListener(new OnImageClickListener(position));
			vi.setOnClickListener(new OnItemClickListener(position));
		}
		return vi;
	}

	private class OnImageClickListener implements OnClickListener {
		private int mPosition;

		OnImageClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {
			Utils.paidOrders(mPosition);
			OrdersActivity sct = (OrdersActivity) activity;
			sct.UpdateOrders();
		}
	}
	
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			OrdersActivity sct = (OrdersActivity) activity;
			sct.onItemClick(mPosition);
		}
	}

	public static class ViewHolder {

		public TextView tableId;
		public TextView ordersCount;
		public TextView cost;
		public ImageView image;
	}

}
