package com.nordusk.UI.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nordusk.R;
import com.nordusk.pojo.DataDistributor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ViewMarkerWindow extends FrameLayout implements View.OnClickListener {

    private TextView tv_name, tv_address, tv_ph, tv_edit;
    private ImageView iv;
    private Context context;
    private RelativeLayout rl_call;

    private ImageLoaderConfiguration config;
    private ImageLoader imageLoader;

//    private SearchPointItemModel pointItemModel;

    private boolean isAvailable = false, isClosed = false;

    public ViewMarkerWindow(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ViewMarkerWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewMarkerWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_marker_window, this, false);
        addView(view);


        iv = (ImageView) view.findViewById(R.id.iv);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_ph = (TextView) view.findViewById(R.id.tv_ph);
        tv_edit = (TextView) view.findViewById(R.id.tv_edit);

        rl_call = (RelativeLayout) view.findViewById(R.id.rl_call);

        rl_call.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setUpView(DataDistributor model) {

        tv_name.setText(model.getName());
        tv_address.setText(model.getAddress());
        tv_ph.setText(model.getMobile());

        config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        if (model.getImage() != null)
            imageLoader.displayImage(model.getImage(), iv);

        Glide.with(getContext())
                .load(model.getImage())
                .centerCrop().crossFade()
                .bitmapTransform(new CircleTransform(getContext()))
                .into(iv);
        rl_call.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_call:
                Toast.makeText(getContext(), "Development is in progress",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

}