package com.nordusk.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.nordusk.R;
import com.nordusk.animation.HTextView;
import com.nordusk.animation.HTextViewType;
import com.nordusk.utility.Prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Splash extends AppCompatActivity {

    private ImageView imgicon_one,imgicon_two,imgicon_three,imgicon_four;
    private HTextView hTextView;
    private Button btn_login;
    private boolean isGranted=false;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            executeUserPermissionTree();

        }

        initView();
        setListener();
        setAnimation();
    }



    private void setAnimation() {

        final Animation animation=AnimationUtils.loadAnimation(Splash.this, R.anim.zoomin);
        final Animation animation_two=AnimationUtils.loadAnimation(Splash.this, R.anim.zoomintwo);
        final Animation animation_three=AnimationUtils.loadAnimation(Splash.this, R.anim.zoominthree);
        final Animation animation_four=AnimationUtils.loadAnimation(Splash.this, R.anim.zoominfour);

        imgicon_one.setAnimation(animation);
        imgicon_two.setAnimation(animation_two);
        imgicon_three.setAnimation(animation_three);
        imgicon_four.setAnimation(animation_four);



        hTextView.setText("Hello Nordusk");
        hTextView.setAnimateType(HTextViewType.LINE);


    }

    private void initView() {

        imgicon_one=(ImageView)findViewById(R.id.splash_imgone);
        imgicon_two=(ImageView)findViewById(R.id.splash_imgtwo);
        imgicon_three=(ImageView)findViewById(R.id.splash_imgthree);
        imgicon_four=(ImageView)findViewById(R.id.splash_imgfour);
        hTextView=(HTextView)findViewById(R.id.splash_htxt);
        hTextView.setText("Hello Nordust");

        btn_login=(Button)findViewById(R.id.splash_btn_login);

        String userId="";
        userId=new Prefs(Splash.this).getString("userid","");
        if(userId!=null && userId.length()>0){
            startActivity(new Intent(Splash.this,Dashboard.class));

            finish();
        }
    }

    private void setListener() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(isGranted){
                        startActivity(new Intent(Splash.this,Login.class));
                        finish();
                    }
                }else{
                    startActivity(new Intent(Splash.this,Login.class));
                    finish();
                }

            }
        });
    }

    private void executeUserPermissionTree() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Access coarse location");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("Access network state");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Access phone state");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Access phone state");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
        isGranted=true;

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
            return true;
        }
        return false;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Splash.this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted
                    isGranted=true;
                } else {
                    // Permission Denied
                    finish();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
