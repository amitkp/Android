package com.nordusk.UI.orderCreate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataProduct;
import com.nordusk.pojo.DataProducts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 20-12-2016.
 */
public class ProductsAdapter extends ArrayAdapter<DataProduct> {

    private List<DataProduct> mList;

    public ProductsAdapter(Context context, int resource, List<DataProduct> objects) {
        super(context, resource, objects);
        if (objects != null) {
            this.mList = objects;
        }else{
            this.mList = new ArrayList<DataProduct>();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_spinner, parent, false);
        TextView tv_productName = (TextView) mView.findViewById(R.id.tv_productName);
        tv_productName.setText(mList.get(position).getDescription());
        return mView;
    }
}
