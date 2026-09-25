package com.example.web_banhang;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView tvLogo;
    private TextView tvName;
    private TextView tvSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_splash
        );

        tvLogo =
                findViewById(R.id.tvLogo);

        tvName =
                findViewById(R.id.tvName);

        tvSubtitle =
                findViewById(R.id.tvSubtitle);

        // Logo ban đầu
        tvLogo.setScaleX(0f);
        tvLogo.setScaleY(0f);
        tvLogo.setAlpha(0f);

        tvName.setAlpha(0f);
        tvName.setTranslationY(60f);

        tvSubtitle.setAlpha(0f);
        tvSubtitle.setTranslationY(40f);

        // Animation logo
        ObjectAnimator logoScaleX =
                ObjectAnimator.ofFloat(
                        tvLogo,
                        View.SCALE_X,
                        0f,
                        1.2f,
                        1f
                );

        ObjectAnimator logoScaleY =
                ObjectAnimator.ofFloat(
                        tvLogo,
                        View.SCALE_Y,
                        0f,
                        1.2f,
                        1f
                );

        ObjectAnimator logoAlpha =
                ObjectAnimator.ofFloat(
                        tvLogo,
                        View.ALPHA,
                        0f,
                        1f
                );

        AnimatorSet logoAnimation =
                new AnimatorSet();

        logoAnimation.playTogether(
                logoScaleX,
                logoScaleY,
                logoAlpha
        );

        logoAnimation.setDuration(1000);

        logoAnimation.setInterpolator(
                new DecelerateInterpolator()
        );

        // Animation tên
        ObjectAnimator nameAlpha =
                ObjectAnimator.ofFloat(
                        tvName,
                        View.ALPHA,
                        0f,
                        1f
                );

        ObjectAnimator nameMove =
                ObjectAnimator.ofFloat(
                        tvName,
                        View.TRANSLATION_Y,
                        60f,
                        0f
                );

        AnimatorSet nameAnimation =
                new AnimatorSet();

        nameAnimation.playTogether(
                nameAlpha,
                nameMove
        );

        nameAnimation.setDuration(700);

        // Animation subtitle
        ObjectAnimator subtitleAlpha =
                ObjectAnimator.ofFloat(
                        tvSubtitle,
                        View.ALPHA,
                        0f,
                        1f
                );

        ObjectAnimator subtitleMove =
                ObjectAnimator.ofFloat(
                        tvSubtitle,
                        View.TRANSLATION_Y,
                        40f,
                        0f
                );

        AnimatorSet subtitleAnimation =
                new AnimatorSet();

        subtitleAnimation.playTogether(
                subtitleAlpha,
                subtitleMove
        );

        subtitleAnimation.setDuration(600);

        // Chạy lần lượt
        logoAnimation.start();

        logoAnimation.addListener(
                new android.animation.AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(
                            android.animation.Animator animation
                    ) {

                        nameAnimation.start();
                    }
                }
        );

        nameAnimation.addListener(
                new android.animation.AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(
                            android.animation.Animator animation
                    ) {

                        subtitleAnimation.start();
                    }
                }
        );

        subtitleAnimation.addListener(
                new android.animation.AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(
                            android.animation.Animator animation
                    ) {

                        new android.os.Handler().postDelayed(
                                () -> {

                                    Intent intent =
                                            new Intent(
                                                    SplashActivity.this,
                                                    UserActivity.class
                                            );

                                    startActivity(intent);

                                    finish();

                                    overridePendingTransition(
                                            android.R.anim.fade_in,
                                            android.R.anim.fade_out
                                    );

                                },
                                500
                        );
                    }
                }
        );
    }
}