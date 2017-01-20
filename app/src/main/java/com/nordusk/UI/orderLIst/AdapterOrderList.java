package com.nordusk.UI.orderLIst;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.helper.ViewOrderitem;
import com.nordusk.pojo.DataOrder;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.ApproveOrderAsync;
import com.nordusk.webservices.HttpConnectionUrl;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class AdapterOrderList extends RecyclerView.Adapter<AdapterOrderList.HolderOrderList>
        implements OrderListPresenter.OnAdapterNotifyListener{

    private OrderListPresenter.OnUserInteractionListener mPresenter;
    private ArrayList<DataOrder> mListOrder;
    private Activity context;
    private Prefs mPrefs;

    public AdapterOrderList(OrderListPresenter.OnUserInteractionListener mPresenter, Activity context) {
        this.mPresenter = mPresenter;
        this.context=context;
        mListOrder = new ArrayList<>();
        mPrefs=new Prefs(context);
    }

    @Override
    public HolderOrderList onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderOrderList(new ViewOrderitem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(HolderOrderList holder, final int position) {
        holder.mView.setupView(mListOrder.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mPrefs.getString("designation", "").equalsIgnoreCase("6")) {
                    if (mListOrder.get(position).getItems() != null && mListOrder.get(position).getItems().size() > 0) {
                        if (mListOrder.get(position).getItems().get(0).getItemId() != null &&
                                mListOrder.get(position).getItems().get(0).getItemId().length() > 0)
                            showapproveDialog(mListOrder.get(position).getItems().get(0).getId());
                        else
                            Toast.makeText(context, "There is no item to approve in this order", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "There is no item to approve in this order", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    private void showapproveDialog(final String itemId) {

        final EditText et_quantity;
        final Button btn_approve;

        final Dialog mDialog_SelectSelectAccount = new Dialog(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.approve_order);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        et_quantity=(EditText)mDialog_SelectSelectAccount.findViewById(R.id.et_quantity);
        btn_approve=(Button)mDialog_SelectSelectAccount.findViewById(R.id.btn_approve);
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_quantity.getText().toString().trim()!=null && et_quantity.getText().toString().length()>0){


                    if(HttpConnectionUrl.isNetworkAvailable(context)){
                        ApproveOrderAsync approveOrderAsync=new ApproveOrderAsync(context,itemId,et_quantity.getText().toString().trim());
                        approveOrderAsync.setOnContentListParserListner(new ApproveOrderAsync.OnContentListSchedules() {
                            @Override
                            public void OnSuccess(String arrayList) {
                                Toast.makeText(context,arrayList,Toast.LENGTH_SHORT).show();
                                mDialog_SelectSelectAccount.dismiss();
                                context.finish();
                                Intent mIntent = new Intent(context, ActivityOrderList.class);
                                context.startActivity(mIntent);
                            }

                            @Override
                            public void OnError(String str_err) {
                                Toast.makeText(context,"Please check your network",Toast.LENGTH_SHORT).show();
                                mDialog_SelectSelectAccount.dismiss();
                            }

                            @Override
                            public void OnConnectTimeout() {

                            }
                        });
                        approveOrderAsync.execute();
                    }else{
                        Toast.makeText(context,"Please check your network",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(context,"Please provide order quantity to approve",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDialog_SelectSelectAccount.show();

    }

}
