package com.example.zhong.paulapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhong.paulapp.adapter.FragAdapter;
import com.example.zhong.paulapp.fragment.Fragment2;
import com.example.zhong.paulapp.fragment.Fragment3;
import com.example.zhong.paulapp.fragment.Fragment4;
import com.example.zhong.paulapp.fragment.Fragment5;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private LinearLayout ll_news;
    private LinearLayout ll_secondhand;
    private LinearLayout ll_finding;
    private LinearLayout ll_dynamic;
    private LinearLayout ll_errand;

    private ImageView iv_news;
    private ImageView iv_secondhand;
    private ImageView iv_finding;
    private ImageView iv_dynamic;
    private ImageView iv_errand;

    private TextView tv_news;
    private TextView tv_secondhand;
    private TextView tv_finding;
    private TextView tv_dynamic;
    private TextView tv_errand;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        iv_errand.setImageResource(R.drawable.errand1);
        tv_errand.setTextColor(0xFF009788);
        viewPager.setCurrentItem(2);
    }

    private void initView(){
        this.ll_news = (LinearLayout) findViewById(R.id.ll_news);
        this.ll_secondhand = (LinearLayout) findViewById(R.id.ll_secondhand);
        this.ll_finding = (LinearLayout) findViewById(R.id.ll_finding);
        this.ll_dynamic = (LinearLayout) findViewById(R.id.ll_dynamic);
        this.ll_errand = (LinearLayout) findViewById(R.id.ll_errand);

        this.iv_news = (ImageView) findViewById(R.id.iv_news);
        this.iv_secondhand = (ImageView) findViewById(R.id.iv_secondhand);
        this.iv_finding = (ImageView) findViewById(R.id.iv_finding);
        this.iv_dynamic = (ImageView) findViewById(R.id.iv_dynamic);
        this.iv_errand = (ImageView) findViewById(R.id.iv_errand);

        this.tv_news = (TextView) findViewById(R.id.tv_news);
        this.tv_secondhand = (TextView) findViewById(R.id.tv_secondhand);
        this.tv_finding = (TextView) findViewById(R.id.tv_finding);
        this.tv_dynamic = (TextView) findViewById(R.id.tv_dynamic);
        this.tv_errand = (TextView) findViewById(R.id.tv_errand);
        this.viewPager = (ViewPager) findViewById(R.id.vp_content);

        viewPager.setOffscreenPageLimit(5);
        List<Fragment> fragments=new ArrayList<Fragment>();
//        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());
        fragments.add(new Fragment5());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    private void initEvent() {
        ll_news.setOnClickListener(this);
        ll_secondhand.setOnClickListener(this);
        ll_finding.setOnClickListener(this);
        ll_dynamic.setOnClickListener(this);
        ll_errand.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        restartBotton();
        switch (position){
            case 0:
                iv_news.setImageResource(R.drawable.news1);
                tv_news.setTextColor(0xff009788);
                break;
            case 1:
                iv_dynamic.setImageResource(R.drawable.dynamic1);
                tv_dynamic.setTextColor(0xff009788);
                break;
            case 2:
                iv_errand.setImageResource(R.drawable.errand1);
                tv_errand.setTextColor(0xff009788);
                break;
            case 3:
                iv_finding.setImageResource(R.drawable.finding1
                );
                tv_finding.setTextColor(0xff009788);
                break;
            case 4:
                iv_secondhand.setImageResource(R.drawable.secondhand1);
                tv_secondhand.setTextColor(0xff009788);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_news:
                iv_news.setImageResource(R.drawable.news1);
                tv_news.setTextColor(0xff009788);
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_dynamic:
                iv_dynamic.setImageResource(R.drawable.dynamic1);
                tv_dynamic.setTextColor(0xff009788);
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_errand:
                iv_errand.setImageResource(R.drawable.errand1);
                tv_errand.setTextColor(0xff009788);
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_finding:
                iv_finding.setImageResource(R.drawable.finding1);
                tv_finding.setTextColor(0xff009788);
                viewPager.setCurrentItem(3);
                break;
            case R.id.ll_secondhand:
                iv_secondhand.setImageResource(R.drawable.secondhand1);
                tv_secondhand.setTextColor(0xff009788);
                viewPager.setCurrentItem(4);
                break;
        }
    }

    public void restartBotton(){
        iv_news.setImageResource(R.drawable.news);
        iv_secondhand.setImageResource(R.drawable.secondhand);
        iv_finding.setImageResource(R.drawable.finding);
        iv_dynamic.setImageResource(R.drawable.dynamic);
        iv_errand.setImageResource(R.drawable.errand);

        tv_news.setTextColor(0xff666666);
        tv_secondhand.setTextColor(0xff666666);
        tv_finding.setTextColor(0xff666666);
        tv_dynamic.setTextColor(0xff666666);
        tv_errand.setTextColor(0xff666666);
    }

}
