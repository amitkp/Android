package com.nordusk.UI.createTarget;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.utility.Util;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import static android.R.attr.startYear;

/**
 * Created by gouravkundu on 16/12/16.
 */

public class DialogTargetCreate extends DialogFragment implements TargetCreatePresenter.OnNotifyUiListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static DialogTargetCreate newInstance() {

        Bundle args = new Bundle();

        DialogTargetCreate fragment = new DialogTargetCreate();
        fragment.setArguments(args);
        return fragment;
    }

    private TargetCreatePresenter.OnUserInteractionListener mPresenter;

    private EditText et_date, et_amount;
    private Button btn_save;

    private ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TargetCreatePresenterImpl(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_target_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_date = (EditText) view.findViewById(R.id.et_date);
        et_amount = (EditText) view.findViewById(R.id.et_amount);
        btn_save = (Button) view.findViewById(R.id.btn_save);

        initLoader();
    }

    private void initLoader() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage(getResources().getString(R.string.please_wait));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et_date.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int) ((getActivity().getResources().getDisplayMetrics().widthPixels) * 0.80);
        int height = (int) ((getActivity().getResources().getDisplayMetrics().widthPixels) * 0.75);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                showDatePickerDialog();
                break;
            case R.id.btn_save:
                btn_save.setClickable(false);
                mPresenter.onAddTargetClick(new WeakReference<Context>(getContext()));
                break;
        }
    }

    private void showDatePickerDialog() {
        Calendar date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), R.style.MyDialogTheme, this,
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
        datePickerDialog.show();
    }

    @Override
    public String getTargetAmount() {
        return et_amount.getText().toString().trim();
    }

    @Override
    public String getTargetDate() {
        return et_date.getText().toString().trim();
    }

    @Override
    public void showLoader() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void hideLoader() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onTargetAddSuccessFull() {
        btn_save.setClickable(true);
        this.dismiss();
    }

    @Override
    public void onTargetAddFailure() {
        btn_save.setClickable(true);
    }

    @Override
    public void onErrorMsg(@StringRes int resId) {
        onErrorMsg(getContext().getResources().getString(resId));
    }

    @Override
    public void onErrorMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i("Response", "onDateSet: " + i + i1 + i2);
        et_date.setText(i+"-"+(i1+1)+"-"+i2);
    }
}
