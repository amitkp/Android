package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataTargetManager;

import java.util.List;

public class AdapterManagerTarget extends BaseAdapter {

	private Context context;
	private List<DataTargetManager> list_BeneficiaryItems;
	private LayoutInflater mInflater;


	public AdapterManagerTarget(Context context, List<DataTargetManager> list_BeneficiaryItems) {
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

		DataTargetManager dataTargetManager=list_BeneficiaryItems.get(position);

		if(dataTargetManager.getMonth()!=null)
			holder.txt_month.setText("Month : "+list_BeneficiaryItems.get(position).getMonth());

		if(dataTargetManager.getAmount()!=null)
			holder.txt_amount.setText("Amount : "+list_BeneficiaryItems.get(position).getAmount());

		if(dataTargetManager.getTarget_achived()!=null)
			holder.txt_target.setText("Target Achieved : "+list_BeneficiaryItems.get(position).getTarget_achived());






		return convertView;
	}

	public class Holder {
		public TextView txt_month,txt_amount,txt_target;
	}

}
