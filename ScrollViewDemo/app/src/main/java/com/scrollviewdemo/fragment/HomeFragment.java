package com.scrollviewdemo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.scrollviewdemo.R;
import com.scrollviewdemo.adapter.YouAdapter;
import com.scrollviewdemo.base.BaseFragment;
import com.scrollviewdemo.donw.PullRefreshRecyclerView;
import com.scrollviewdemo.donw.PullRefreshUtil;
import com.scrollviewdemo.donw.PullRefreshView;
import com.scrollviewdemo.hoder.MulitiTypeSupport;
import com.scrollviewdemo.model.GankGay;
import com.scrollviewdemo.net.DefaultObserver;
import com.scrollviewdemo.net.IdeaApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends BaseFragment implements MulitiTypeSupport<GankGay.ResultsBean> {
    private PullRefreshRecyclerView recyclerView;
    private int page = 1;
    private int pageSize = 20;
    private List<GankGay.ResultsBean> gankGays = new ArrayList<>();
    private YouAdapter youAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public int initView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onMyActivityCreated() {
//        getMyInconme(pageSize, page);
        getinit();

    }

    public void getinit() {
//        getMyInconme(pageSize, page);
        PullRefreshUtil.setRefresh(recyclerView, true, true);
        recyclerView.setOnPullDownRefreshListener(
                new PullRefreshView.OnPullDownRefreshListener() {
                    @Override
                    public void onRefresh() {
                        recyclerView.isMore(true);
                        page = 1;
                        getMyInconme(pageSize, page);
                        recyclerView.isMore(true);
                    }
                });
        recyclerView.setOnPullUpRefreshListener(
                new PullRefreshView.OnPullUpRefreshListener() {
                    @Override
                    public void onRefresh() {
                        page++;
                        getMyInconme(pageSize, page);
                    }
                });
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        youAdapter = new YouAdapter(getActivity(), gankGays, this);
        recyclerView.setAdapter(youAdapter);
    }

    public void getMyInconme(final int pageSize, final int pagenum) {
        IdeaApi.getApiService().getContent("福利", pageSize + "", pagenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<GankGay>(this, true) {
                    @Override
                    public void onSuccess(GankGay bean) {
                        if (pagenum == 1) {
                            gankGays.clear();
                        }
                        recyclerView.refreshFinish();
                        gankGays.addAll(bean.getResults());
                        youAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean addRxDestroy(Disposable disposable) {
        return false;
    }

    @Override
    public int getLayoutId(GankGay.ResultsBean item) {
        return R.layout.recycle_item;
    }
}
