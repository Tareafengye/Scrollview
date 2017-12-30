package com.scrollviewdemo.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.scrollviewdemo.R;
import com.scrollviewdemo.base.BaseFragment;
import com.scrollviewdemo.impl.ActionBarClickListener;
import com.scrollviewdemo.weight.TranslucentActionBar;
import com.scrollviewdemo.weight.TranslucentScrollView;

import io.reactivex.disposables.Disposable;

public class AgentFragment extends BaseFragment implements ActionBarClickListener, TranslucentScrollView.TranslucentChangedListener {
    private TranslucentScrollView pullzoom_scrollview;
    private TranslucentActionBar actionbar;
    private View lay_header;
    private ImageView iv_actionbar_left;

    @Override
    public int initView() {
        return R.layout.fragment_agen;
    }

    @Override
    public void onMyActivityCreated() {
        init();
    }


    @Override
    public void onClick(View view) {


    }

    private void init() {
        //初始actionBar
        actionbar.setData("个人中心", R.mipmap.ic_left_light, null, 0, null, null);
        //开启渐变
        actionbar.setNeedTranslucent();
        //设置透明度变化监听
        pullzoom_scrollview.setTranslucentChangedListener(this);
        //关联需要渐变的视图
        pullzoom_scrollview.setTransView(actionbar, getResources().getColor(R.color.colorAccent));
        //关联伸缩的视图
        pullzoom_scrollview.setPullZoomView(lay_header);
    }

    @Override
    public void onLeftClick(View view) {
        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRightClick() {
        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTranslucentChanged(int transAlpha) {
        actionbar.tvTitle.setVisibility(transAlpha > 40 ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean addRxDestroy(Disposable disposable) {
        return false;
    }
}
