package com.nordusk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.webservices.ParentId;

import java.util.ArrayList;

/**
 * Created by DELL on 28-11-2016.
 */


public class CustomAutoCompleteAdapter extends ArrayAdapter<ParentId> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<ParentId> items;
    private ArrayList<ParentId> itemsAll;
    private ArrayList<ParentId> suggestions;
    private int viewResourceId;

    public CustomAutoCompleteAdapter(Context context, int viewResourceId, ArrayList<ParentId> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<ParentId>) items.clone();
        this.suggestions = new ArrayList<ParentId>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        ParentId parentId = items.get(position);
        if (parentId != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(parentId.getName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ParentId)(resultValue)).getName()+"-"+((ParentId)(resultValue)).getId();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (ParentId parentId : itemsAll) {
                    if(parentId.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(parentId);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ParentId> filteredList = (ArrayList<ParentId>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (ParentId c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}
