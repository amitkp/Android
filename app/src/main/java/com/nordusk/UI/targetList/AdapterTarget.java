package com.nordusk.UI.targetList;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

    public AdapterTarget(TargetListPresenter.OnUserInteractionListener mPresenter) {
        this.mPresenter = mPresenter;
        mListTarget = new ArrayList<>();
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
    public void updateTargetElements(ArrayList<DataTarget> mListTarget) {
        this.mListTarget = mListTarget;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mListTarget != null) ? mListTarget.size() : 0;
    }

    public static class HolderTarget extends RecyclerView.ViewHolder {
        TextView tv_target, tv_amout,tv_date;

        public HolderTarget(View itemView) {
            super(itemView);
            tv_amout = (TextView) itemView.findViewById(R.id.tv_amout);
            tv_target = (TextView) itemView.findViewById(R.id.tv_target);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

}
