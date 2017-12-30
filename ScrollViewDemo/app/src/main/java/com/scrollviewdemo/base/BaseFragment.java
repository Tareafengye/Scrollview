package com.scrollviewdemo.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.scrollviewdemo.R;
import com.scrollviewdemo.net.BaseImpl;
import com.scrollviewdemo.weight.CustomProgressDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 继承自该Fragment的类不需要findViewById，在onActivityCreated中调用super.onActivityCreated()
 *
 * @author Young
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,BaseImpl {
    int states;
    private CustomProgressDialog mProgressDialog;
    private CompositeDisposable disposables2Stop;// 管理Stop取消订阅者者
    private CompositeDisposable disposables2Destroy;// 管理Destroy取消订阅者者
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smartInject(getView());
        onMyActivityCreated();
        try {
            if (disposables2Destroy != null) {
                throw new IllegalStateException("onCreate called multiple times");
            }
            disposables2Destroy = new CompositeDisposable();
        }catch (Exception e){

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 显示ProgressDialog
     */
    public void showProgress(String msg) {
        mProgressDialog = new CustomProgressDialog.Builder(getActivity())
                .setMessage(msg)
                .setTheme(R.style.dialog)
                .build();
        mProgressDialog.show();
    }

    /**
     * 显示ProgressDialog
     */
    public void showProgress() {
        mProgressDialog = new CustomProgressDialog.Builder(getActivity())
                .setTheme(R.style.dialog)
                .build();
        mProgressDialog.show();
    }

    /**
     * 取消ProgressDialog
     */
    public void dismissProgress() {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public boolean addRxStop(Disposable disposable) {
        if (disposables2Stop == null) {
            throw new IllegalStateException(
                    "addUtilStop should be called between onStart and onStop");
        }
        disposables2Stop.add(disposable);
        return true;
    }

    public void remove(Disposable disposable) {
        if (disposables2Stop == null && disposables2Destroy == null) {
            throw new IllegalStateException("remove should not be called after onDestroy");
        }
        if (disposables2Stop != null) {
            disposables2Stop.remove(disposable);
        }
        if (disposables2Destroy != null) {
            disposables2Destroy.remove(disposable);
        }
    }

    public void onStart() {
        super.onStart();
        if (disposables2Stop != null) {
            throw new IllegalStateException("onStart called multiple times");
        }
        disposables2Stop = new CompositeDisposable();
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        if (disposables2Stop == null) {
            throw new IllegalStateException("onStop called multiple times or onStart not called");
        }
        disposables2Stop.dispose();
        disposables2Stop = null;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables2Destroy.dispose();
        disposables2Destroy = null;
    }

    protected void smartInject(View view) {
        try {
            Class<? extends Fragment> clz = getClass();
            while (clz != BaseFragment.class) {
                Field[] fs = clz.getDeclaredFields();
                Resources res = getResources();
                String packageName = getActivity().getPackageName();
                for (Field field : fs) {
                    if (!View.class.isAssignableFrom(field.getType())) {
                        continue;
                    }
                    int viewId = res.getIdentifier(field.getName(), "id",
                            packageName);
                    if (viewId == 0)
                        continue;
                    field.setAccessible(true);
                    try {
                        View v = view.findViewById(viewId);
                        field.set(this, v);
                        Class<?> c = field.getType();
                        Method m = c.getMethod("setOnClickListener",
                                View.OnClickListener.class);
                        m.invoke(v, this);
                    } catch (Throwable e) {
                    }
                    field.setAccessible(false);

                }

                clz = (Class<? extends Fragment>) clz.getSuperclass();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(initView(), null);
    }

    public abstract int initView();

    public abstract void onMyActivityCreated();


    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }


    public int ChangeToDP(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

}
