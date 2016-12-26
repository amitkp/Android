package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.webservices.ParentId;

import java.util.List;

public class AdapterStock extends BaseAdapter {

	private Context context;
	private List<ParentId> list_BeneficiaryItems;
	private LayoutInflater mInflater;


	public AdapterStock(Context context, List<ParentId> list_BeneficiaryItems) {
		this.context = context;
		this.list_BeneficiaryItems = list_BeneficiaryItems;
		this.mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list_BeneficiaryItems.size();
	}

	@Override
	public Object getItem(int position) {
		return list_BeneficiaryItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_stock, parent, false);
			holder = new Holder();
			holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txt_amount = (TextView) convertView.findViewById(R.id.txt_amount);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}


		ParentId parentId=list_BeneficiaryItems.get(position);
		if(parentId.getName()!=null && parentId.getName().length()>0)
			holder.txt_name.setText("Product Name :"+parentId.getName());

		if(parentId.getId()!=null && parentId.getId().length()>0)
			holder.txt_amount.setText("Product Available Amount :"+parentId.getId());






		return convertView;
	}

	public class Holder {
		public TextView txt_name,txt_amount;
	}

}
