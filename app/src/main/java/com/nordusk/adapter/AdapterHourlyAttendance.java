package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.utility.GPSTracker;

import java.util.ArrayList;
import java.util.List;

public class AdapterHourlyAttendance extends BaseAdapter {

	private Context context;
	private List<DataObjectAttendance> list_BeneficiaryItems;
	private LayoutInflater mInflater;


	public AdapterHourlyAttendance(Context context, List<DataObjectAttendance> list_BeneficiaryItems) {
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
			convertView = mInflater.inflate(R.layout.row_manager_hourly_attendance_list, parent, false);
			holder = new Holder();
//			holder.txt_id = (TextView) convertView.findViewById(R.id.txt_id);
			holder.txt_date_time = (TextView) convertView.findViewById(R.id.txt_date_time);
//			holder.txt_lat = (TextView) convertView.findViewById(R.id.txt_lat);
//			holder.txt_lon = (TextView) convertView.findViewById(R.id.txt_lon);
			holder.txt_login_address = (TextView) convertView.findViewById(R.id.txt_login_address);

			DataObjectAttendance dp = list_BeneficiaryItems.get(position);

			if(dp.getLogin_latitude() != "" && dp.getLogin_longitutde()!= "" ){
				GPSTracker gpsTracker = new GPSTracker(context);
				Double loginlat ,loginlon;

				loginlat = Double.parseDouble(dp.getLogin_latitude());
				loginlon = Double.parseDouble(dp.getLogin_longitutde());

				if(loginlat > 0.0 && loginlon > 0.0 ){
					dp.setLogin_addresss(gpsTracker.addressSet(loginlat,loginlon));
					Log.e("GPS","Date-"+dp.getDate()+"Login-"+dp.getLogin_addresss()+"Logout"+dp.getLogout_address());
				}

			}

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		DataObjectAttendance dataTargetManager=list_BeneficiaryItems.get(position);

//		if(dataTargetManager.getId()!=null)
//			holder.txt_id.setText("Id : "+list_BeneficiaryItems.get(position).getId());

		if(dataTargetManager.getDate()!=null)
			holder.txt_date_time.setText("Date Time : "+list_BeneficiaryItems.get(position).getDate());

//		if(dataTargetManager.getLogin_latitude()!=null)
//			holder.txt_lat.setText("Login Latitude : "+list_BeneficiaryItems.get(position).getLogin_latitude());
//
//		if(dataTargetManager.getLogin_longitutde()!=null)
//			holder.txt_lon.setText("Login Latitude : "+list_BeneficiaryItems.get(position).getLogin_longitutde());

		if(dataTargetManager.getLogin_addresss()!=null)
			holder.txt_login_address.setText("Login Address : "+list_BeneficiaryItems.get(position).getLogin_addresss());


		return convertView;
	}

	public class Holder {
		public TextView txt_date_time,txt_login_address;
	}

	public List<DataObjectAttendance> getUpdateList(){
		return list_BeneficiaryItems;
	}

}
