package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataObjectOrderVsBill;
import com.nordusk.pojo.DataObjectOrderVsBillDetails;

import java.util.List;

public class AdapterOrderVsBill extends BaseAdapter {

    private Context context;
    private List<DataObjectOrderVsBill> list_BeneficiaryItems;
    private LayoutInflater mInflater;
    private LayoutInflater detailInflater;


    public AdapterOrderVsBill(Context context, List<DataObjectOrderVsBill> list_BeneficiaryItems) {
        this.context = context;
        this.list_BeneficiaryItems = list_BeneficiaryItems;
        this.mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.detailInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
            convertView = mInflater.inflate(R.layout.activity_list_order_vs_bill_details, parent, false);
            holder = new Holder();
            holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
            holder.order_date = (TextView) convertView.findViewById(R.id.order_date);
            holder.order_price = (TextView) convertView.findViewById(R.id.order_price);
            holder.lin_items = (LinearLayout) convertView.findViewById(R.id.lin_items);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        DataObjectOrderVsBill dataTargetManager = list_BeneficiaryItems.get(position);

        if (dataTargetManager.getName() != null)
            holder.order_name.setText("Order  Name: " + list_BeneficiaryItems.get(position).getName());

        if (dataTargetManager.getDate() != null)
            holder.order_date.setText("Date : " + list_BeneficiaryItems.get(position).getDate());

        if (dataTargetManager.getTotalPrice() != null)
            holder.order_price.setText("Total Price : " + list_BeneficiaryItems.get(position).getTotalPrice());

        if (dataTargetManager.getListDetails()!=null && dataTargetManager.getListDetails().size() > 0) {
            holder.lin_items.removeAllViews();
            for(int i=0;i<dataTargetManager.getListDetails().size();i++){
                holder.lin_items.addView(addItemView(dataTargetManager.getListDetails().get(i)));
            }

        }
        return convertView;
    }

    public class Holder {
        public TextView order_name, order_price, order_date;
        private LinearLayout lin_items;
    }

    private View addItemView(DataObjectOrderVsBillDetails dataObjectOrderVsBillDetails) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.activity_list_order_vs_bill_details_dynamic, null);
        TextView item_name = (TextView) view.findViewById(R.id.item_name);
        TextView item_price = (TextView) view.findViewById(R.id.item_price);
        TextView item_quantity = (TextView) view.findViewById(R.id.item_quantity);
        TextView appr_quantity = (TextView) view.findViewById(R.id.appr_quantity);

        item_name.setText("Item Name: "+dataObjectOrderVsBillDetails.getName());
        item_price.setText("Item Price: "+dataObjectOrderVsBillDetails.getPrice());
        item_quantity.setText("Item Quantity: "+dataObjectOrderVsBillDetails.getQuantity());
        appr_quantity.setText("Approved Quantity: "+dataObjectOrderVsBillDetails.getApprQuantity());
        return view;

    }

}
