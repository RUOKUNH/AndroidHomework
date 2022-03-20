package com.bytedance.jstu.homework;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

public class Animation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        startAnimation(findViewById(R.id.like_image));
        startAnimation(findViewById(R.id.coin_image));
        startAnimation(findViewById(R.id.collect_image));
    }

    private void startAnimation(ImageView v) {
        Animator jump = AnimatorInflater.loadAnimator(this, R.animator.jump);
        Animator back = AnimatorInflater.loadAnimator(this, R.animator.back);
        Animator like_st = AnimatorInflater.loadAnimator(this, R.animator.like_st);
        Animator coin_st = AnimatorInflater.loadAnimator(this, R.animator.coin_st);
        Animator collect_st = AnimatorInflater.loadAnimator(this, R.animator.collect_st);

        jump.setTarget(v);
        back.setTarget(v);
        like_st.setTarget(v);
        coin_st.setTarget(v);
        collect_st.setTarget(v);

        jump.addListener(new EasyListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                v.setColorFilter(Color.RED);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();

        switch(v.getTag().toString()) {
            case "like":{
                animatorSet.playSequentially(like_st, jump, back);
                animatorSet.start();
                break;
            }
            case "coin":{
                animatorSet.playSequentially(coin_st, jump, back);
                animatorSet.start();
                break;
            }
            case "collect":{
                animatorSet.playSequentially(collect_st, jump, back);
                animatorSet.start();
                break;
            }
        }

    }
    static abstract class EasyListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

}