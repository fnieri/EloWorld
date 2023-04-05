// Onboarding code thanks to https://www.youtube.com/watch?v=nfsqxkrTQFY https://github.com/foxandroid/Onboarding

package com.example.eloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import src.Main;

public class OnboardingPage extends AppCompatActivity {

    int dotsSize = 4;
    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    Button backBtn, nextBtn, skipBtn;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding);
        System.out.println("SIAODJOSIAJDOISAJDO");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        backBtn = findViewById(R.id.back_btn);
        nextBtn = findViewById(R.id.next_btn);
        skipBtn = findViewById(R.id.skipButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getItem(0) > 0){

                    mSLideViewPager.setCurrentItem(getItem(-1),true);

                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getItem(0) < dotsSize - 1)
                    mSLideViewPager.setCurrentItem(getItem(1),true);
                else {

                    Intent i = new Intent(OnboardingPage.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnboardingPage.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        mSLideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        mSLideViewPager.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);

    }

    public void setUpindicator(int position){

        dots = new TextView[dotsSize];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dotsSize; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                backBtn.setVisibility(View.VISIBLE);

            }else {

                backBtn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }

}