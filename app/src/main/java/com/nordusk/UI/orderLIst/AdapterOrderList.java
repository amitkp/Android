package com.nordusk.UI.orderLIst;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nordusk.UI.helper.ViewOrderitem;
import com.nordusk.pojo.DataOrder;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class AdapterOrderList extends RecyclerView.Adapter<AdapterOrderList.HolderOrderList>
        implements OrderListPresenter.OnAdapterNotifyListener{

    private OrderListPresenter.OnUserInteractionListener mPresenter;
    private ArrayList<DataOrder> mListOrder;

    public AdapterOrderList(OrderListPresenter.OnUserInteractionListener mPresenter) {
        this.mPresenter = mPresenter;
        mListOrder = new ArrayList<>();
    }

    @Override
    public HolderOrderList onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderOrderList(new ViewOrderitem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(HolderOrderList holder, int position) {
        holder.mView.setupView(mListOrder.get(position));
    }

    @Override
    public void updateListElement(ArrayList<DataOrder> mListOrder) {
        this.mListOrder = mListOrder;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mListOrder.size();
    }

    public static class HolderOrderList extends RecyclerView.ViewHolder {

        ViewOrderitem mView;
        public HolderOrderList(View itemView) {
            super(itemView);
            mView = (ViewOrderitem) itemView;
        }
    }

}
