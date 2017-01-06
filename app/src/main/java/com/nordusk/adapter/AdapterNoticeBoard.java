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
import com.nordusk.pojo.DataNotice;

import java.util.List;

public class AdapterNoticeBoard extends BaseAdapter {

	private Context context;
	private List<DataNotice> notice_list;
	private LayoutInflater mInflater;


	public AdapterNoticeBoard(Context context, List<DataNotice> notice_list) {
		this.context = context;
		this.notice_list = notice_list;
		this.mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return notice_list.size();
	}

	@Override
	public Object getItem(int position) {
		return notice_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_salesperson_notice_board_list, parent, false);
			holder = new Holder();
			holder.txt_date_time = (TextView) convertView.findViewById(R.id.txt_date_time);
			holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
			holder.txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		DataNotice dataNotice = notice_list.get(position);

		if(dataNotice.getDateTime()!=null)
			holder.txt_date_time.setText("Date : "+notice_list.get(position).getDateTime());

		if(dataNotice.getHeader()!=null)
			holder.txt_title.setText("Title : "+notice_list.get(position).getHeader());

		if(dataNotice.getHeader()!=null)
			holder.txt_desc.setText("Description : "+notice_list.get(position).getBody());

		return convertView;
	}

	public class Holder {
		public TextView txt_date_time,txt_title,txt_desc;
	}

}
