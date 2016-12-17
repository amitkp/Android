package com.nordusk.UI.dialogTracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nordusk.R;

import java.lang.ref.WeakReference;

/**
 * Created by gouravkundu on 12/12/16.
 */

public class DialogAddTracker extends DialogFragment implements AddTrackerPresenter.OnNotifyUiListener, View.OnClickListener {

    public static DialogAddTracker newInstance() {

        Bundle args = new Bundle();

        DialogAddTracker fragment = new DialogAddTracker();
        fragment.setArguments(args);
        return fragment;
    }

    private WeakReference<Context> contextWeakReference;
    private AddTrackerPresenter.OnUserInteractionListener mPresenter;

    ProgressDialog mProgressDialog;

    private Button btn_add;
    private EditText et_tracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        initProgressDialog();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(contextWeakReference.get());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
    }

    private void initPresenter() {
        contextWeakReference = new WeakReference<Context>(getContext());
        mPresenter = new AddTrackerImpl(this, contextWeakReference);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_tracker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_tracker = (EditText)view.findViewById(R.id.et_tracker);
        btn_add = (Button)view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int) ((getActivity().getResources().getDisplayMetrics().widthPixels) * 0.80);
        int height = (int) ((getActivity().getResources().getDisplayMetrics().widthPixels) * 0.90);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onClick(View view) {
        mPresenter.onAddTracker();
    }

    @Override
    public String getLocation() {
        return et_tracker.getText().toString().trim();
    }

    @Override
    public void showLoader() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoader() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onErrorMessageReceive(@StringRes int resId) {
        onErrorMessageReceive(contextWeakReference.get().getResources().getString(resId));
    }

    @Override
    public void onErrorMessageReceive(String msg) {
        Toast.makeText(contextWeakReference.get(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrackerAddSuccessfull() {
        Toast.makeText(contextWeakReference.get(), contextWeakReference.get().getString(R.string.territory_success), Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    @Override
    public void onTrackerAddFailure() {

    }
}
