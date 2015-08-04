package tei.kav.smartorder.data;

import java.util.ArrayList;

import tei.kav.smartorder.MakeOrderActivity;
import tei.kav.smartorder.R;
import tei.kav.smartorder.TableActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomOrderAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<Order> data;
	private static LayoutInflater inflater = null;
	Order tempValue = null;
	int i = 0;
	boolean tableActivity;

	public CustomOrderAdapter(Activity a, boolean tableActivity,
			ArrayList<Order> o) {

		activity = a;
		data = o;
		this.tableActivity = tableActivity;

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
			
			vi = inflater.inflate(R.layout.order_item, null);

			holder = new ViewHolder();
			holder.orderDesc = (TextView) vi.findViewById(R.id.orderDescText);
			holder.image = (ImageView) vi.findViewById(R.id.removeImage);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (data.size() <= 0) {
			holder.orderDesc.setText("No Data");

		} else {
			tempValue = null;
			tempValue = (Order) data.get(position);

			holder.orderDesc.setText(tempValue.item_name);
			holder.image.setOnClickListener(new OnItemClickListener(position));
		}
		return vi;
	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {
			if (data.size() <= 0) {
				return;
			}
			
			if (tableActivity) {
				TableActivity ta = (TableActivity) activity;
				ta.removeOrderFromList(mPosition);
			} else {
				MakeOrderActivity moa = (MakeOrderActivity) activity;
				moa.removeOrderFromList(mPosition);
			}
		}
	}

	public static class ViewHolder {
		public TextView orderDesc;
		public ImageView image;
	}
}