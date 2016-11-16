package com.nordusk.UI;

import android.content.Intent;
import android.graphics.Color;
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

public class Splash extends AppCompatActivity {

    private ImageView imgicon_one,imgicon_two,imgicon_three,imgicon_four;
    private HTextView hTextView;
    private Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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



        hTextView.setText("Hello Nordust");
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
    }

    private void setListener() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(Splash.this,Login.class));
            }
        });
    }
}
