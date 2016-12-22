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
import com.nordusk.pojo.DataTargetManager;

import java.util.List;

public class AdapterAttendance extends BaseAdapter {

	private Context context;
	private List<DataObjectAttendance> list_BeneficiaryItems;
	private LayoutInflater mInflater;


	public AdapterAttendance(Context context, List<DataObjectAttendance> list_BeneficiaryItems) {
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
			convertView = mInflater.inflate(R.layout.row_manager_target_list, parent, false);
			holder = new Holder();
			holder.txt_month = (TextView) convertView.findViewById(R.id.txt_month);
			holder.txt_amount = (TextView) convertView.findViewById(R.id.txt_amount);
			holder.txt_target = (TextView) convertView.findViewById(R.id.txt_target);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		DataObjectAttendance dataTargetManager=list_BeneficiaryItems.get(position);

		if(dataTargetManager.getDate()!=null)
			holder.txt_month.setText("Date : "+list_BeneficiaryItems.get(position).getDate());

		if(dataTargetManager.getLogin_time()!=null)
			holder.txt_amount.setText("LogIn Time : "+list_BeneficiaryItems.get(position).getLogin_time());

		if(dataTargetManager.getLogout_time()!=null)
			holder.txt_target.setText("LogOut Time : "+list_BeneficiaryItems.get(position).getLogout_time());






		return convertView;
	}

	public class Holder {
		public TextView txt_month,txt_amount,txt_target;
	}

}
