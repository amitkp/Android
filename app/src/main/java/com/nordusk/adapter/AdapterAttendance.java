package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.pojo.DataTargetManager;
import com.nordusk.utility.GPSTracker;

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
			convertView = mInflater.inflate(R.layout.row_manager_attendance_list, parent, false);
			holder = new Holder();
			holder.txt_month = (TextView) convertView.findViewById(R.id.txt_month);
			holder.txt_amount = (TextView) convertView.findViewById(R.id.txt_amount);
			holder.txt_target = (TextView) convertView.findViewById(R.id.txt_target);
			holder.txt_login_address = (TextView) convertView.findViewById(R.id.txt_login_address);
			holder.txt_logout_address = (TextView) convertView.findViewById(R.id.txt_logout_address);



			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}


		DataObjectAttendance dp = list_BeneficiaryItems.get(position);

		if(dp.getLogin_latitude() != "" && dp.getLogin_longitutde()!= "" && dp.getLogout_latitude() != "" && dp.getLogout_longitude() != ""){
			GPSTracker gpsTracker = new GPSTracker(context);
			Double loginlat ,loginlon, logoutlat, logoutlon;

			loginlat = Double.parseDouble(dp.getLogin_latitude());
			loginlon = Double.parseDouble(dp.getLogin_longitutde());
			logoutlat = Double.parseDouble(dp.getLogout_latitude());
			logoutlon = Double.parseDouble(dp.getLogout_longitude());

			if(loginlat > 0.0 && loginlon > 0.0 && logoutlat > 0.0 && logoutlon > 0.0){
				/*Location loginloc = new Location("");
				Location logoutloc = new Location("");
				loginloc.setLatitude(loginlat);
				loginloc.setLongitude(loginlon);
				logoutloc.setLatitude(logoutlat);
				logoutloc.setLongitude(logoutlon);*/
				dp.setLogin_addresss(gpsTracker.addressSet(loginlat,loginlon));
				dp.setLogout_address(gpsTracker.addressSet(logoutlat,logoutlon));
				Log.e("GPS","Date-"+dp.getDate()+"Login-"+dp.getLogin_addresss()+"Logout"+dp.getLogout_address());
			}

		}

		if(dp.getDate()!=null)
			holder.txt_month.setText("Date : "+list_BeneficiaryItems.get(position).getDate());

		if(dp.getLogin_time()!=null)
			holder.txt_amount.setText("LogIn Time : "+list_BeneficiaryItems.get(position).getLogin_time());

		if(dp.getLogout_time()!=null)
			holder.txt_target.setText("LogOut Time : "+list_BeneficiaryItems.get(position).getLogout_time());

		if(dp.getLogin_addresss()!=null)
			holder.txt_login_address.setText("Login Address : "+list_BeneficiaryItems.get(position).getLogin_addresss());

		if(dp.getLogout_address()!=null)
			holder.txt_logout_address.setText("Logout Address : "+list_BeneficiaryItems.get(position).getLogout_address());


		return convertView;
	}

	public class Holder {
		public TextView txt_month,txt_amount,txt_target,txt_login_address,txt_logout_address;
	}

}
