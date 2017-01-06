package com.nordusk.UI.targetList;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataTarget;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 16/12/16.
 */

public class AdapterTarget extends RecyclerView.Adapter<AdapterTarget.HolderTarget>
        implements TargetListPresenter.OnAdapterNotifyListener {

    private TargetListPresenter.OnUserInteractionListener mPresenter;

    private ArrayList<DataTarget> mListTarget;
    private ArrayList<String> additnalData;
    private TextView total_target;
    private TextView target_achieved;
    public AdapterTarget(TargetListPresenter.OnUserInteractionListener mPresenter,TextView total_target,TextView target_achieved) {
        this.mPresenter = mPresenter;
        mListTarget = new ArrayList<>();
        additnalData = new ArrayList<>();
        this.total_target=total_target;
        this.target_achieved=target_achieved;
    }

    @Override
    public HolderTarget onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderTarget(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_target_item, parent,
                false));
    }

    @Override
    public void onBindViewHolder(HolderTarget holder, int position) {
        holder.tv_amout.setText("Target Amount "+holder.tv_amout.getContext().getResources().getString(R.string.rs) + " "
                + mListTarget.get(position).getAmount());
        holder.tv_target.setText(holder.tv_target.getContext().getResources().getString(R.string.target) + " "
                + mListTarget.get(position).getTargetAchived());
        holder.tv_date.setText("Date :"+ " "
                + mListTarget.get(position).getDate());

    }

    @Override
    public void updateTargetElements(ArrayList<DataTarget> mListTarget, ArrayList<String> additnaData) {
        this.mListTarget = mListTarget;
        this.additnalData = additnaData;
        this.notifyDataSetChanged();
        if(additnalData != null && additnalData.size() == 2) {
            total_target.setText("Total Target "+additnalData.get(1));
            target_achieved.setText("Total Target Achieved "+additnalData.get(0));
        }
        else {
            Log.e("Array","Array Null");
        }
    }

    @Override
    public int getItemCount() {
        return (mListTarget != null) ? mListTarget.size() : 0;
    }

    public static class HolderTarget extends RecyclerView.ViewHolder {
        TextView tv_target, tv_amout,tv_date, total_target,total_target_achieved;

        public HolderTarget(View itemView) {
            super(itemView);
            tv_amout = (TextView) itemView.findViewById(R.id.tv_amout);
            tv_target = (TextView) itemView.findViewById(R.id.tv_target);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            total_target = (TextView) itemView.findViewById(R.id.total_target);
            total_target_achieved = (TextView) itemView.findViewById(R.id.total_target_achieved);
        }
    }

}
