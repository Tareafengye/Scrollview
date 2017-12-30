package com.scrollviewdemo.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;


import com.scrollviewdemo.R;
import com.scrollviewdemo.app.App;
import com.scrollviewdemo.net.BaseImpl;
import com.scrollviewdemo.util.BarUtils;
import com.scrollviewdemo.weight.CustomProgressDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 继承自该类的Activity不需要使用findViewById(),只需要把控件声明称类内全局的，</br>并且和activity对应的布局中的id的名字相同，这样setContentView后就可以直接使用控件了，</br>
 * 同时不需要为控件设置clickListener， 直接在类上implements onClickListener,</br>
 * 并在activity中 重写onClick()方法，当点击控件时即可自动调用onClick()方法
 * <p>
 * </br>参考
 * 沉浸式状态栏 ，设置状态栏颜色使用setStatusBarColor，参数为颜色资源id，默认为橙色状态栏
 *
 * @author LIU
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener, BaseImpl {
    private LinearLayout container;
    /**
     * 是否开启  右划关闭activity 手势，默认开启
     */
    private boolean isGestureOpen = true;
    private int alpha;

    private CustomProgressDialog mProgressDialog;
    private CompositeDisposable disposables2Stop;// 管理Stop取消订阅者者
    private CompositeDisposable disposables2Destroy;// 管理Destroy取消订阅者者

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        container = new ViewContainer(getApplicationContext());
        container.setOrientation(LinearLayout.VERTICAL);
        StatusBarAlpha(this, alpha);
        StatusBarColor(this, R.color.colorAccent);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (disposables2Destroy != null) {
            throw new IllegalStateException("onCreate called multiple times");
        }
        disposables2Destroy = new CompositeDisposable();
        BarUtils.setStatusBarAlpha(this, alpha);

    }


    /**
     * 显示ProgressDialog
     */
    public void showProgress(String msg) {
        mProgressDialog = new CustomProgressDialog.Builder(this)
                .setMessage(msg)
                .setTheme(R.style.dialog)
                .build();
        mProgressDialog.show();
    }

    /**
     * 显示ProgressDialog
     */
    public void showProgress() {
        mProgressDialog = new CustomProgressDialog.Builder(this)
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

    public boolean addRxDestroy(Disposable disposable) {
//        if (disposables2Destroy == null) {
//            throw new IllegalStateException(
//                    "addUtilDestroy should be called between onCreate and onDestroy");
//        }
//        disposables2Destroy.add(disposable);
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
    }

    public void onStop() {
        super.onStop();
        if (disposables2Stop == null) {
            throw new IllegalStateException("onStop called multiple times or onStart not called");
        }
        disposables2Stop.dispose();
        disposables2Stop = null;
    }
    /**
     * 设置状态栏模糊度
     *
     * @param activity
     */
    public void StatusBarAlpha(Activity activity, int alpha) {
        BarUtils.setStatusBarAlpha(activity, alpha);
    }

    public void StatusBarColor(Activity activity, int alpha) {
        BarUtils.setStatusBarColor(activity, alpha);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {

        super.startActivityForResult(intent, requestCode);
    }


    @SuppressLint("NewApi")
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
//		overridePendingTransition(R.anim.anim_enter_open_activity, R.anim.anim_exit_open_activity);
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
//		overridePendingTransition(R.anim.anim_enter_finish_activity, R.anim.anim_exit_finish_activity);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            LayoutInflater.from(this).inflate(layoutResID, container, true);
            setContentView(container);

        } else {
            super.setContentView(layoutResID);
        }
        smartInject();

    }


    /**
     * 设置是否允许屏幕左侧 快速 右划关闭activity，默认允许
     *
     * @param b
     */
    protected void isGestureSensitive(boolean b) {
        isGestureOpen = b;
    }

    private void smartInject() {

        try {
            Class<? extends Activity> clz = getClass();

            while (clz != BaseActivity.class) {

                Field[] fs = clz.getDeclaredFields();
                Resources res = getResources();
                String packageName = getPackageName();
                for (Field field : fs) {
                    if (!View.class.isAssignableFrom(field.getType())) {
                        continue;
                    }
                    int viewId = res.getIdentifier(field.getName(), "id", packageName);
                    if (viewId == 0)
                        continue;
                    field.setAccessible(true);
                    try {
                        View v = findViewById(viewId);
                        field.set(this, v);
                        Class<?> c = field.getType();
                        Method m = c.getMethod("setOnClickListener", View.OnClickListener.class);
                        m.invoke(v, this);
                    } catch (Throwable e) {
                    }
                    field.setAccessible(false);

                }

                clz = (Class<? extends Activity>) clz.getSuperclass();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    /**
     * 屏幕左侧右划返回容器 ,
     *
     * @author Young
     */
    private class ViewContainer extends LinearLayout {

        private int leftMargin;
        private VelocityTracker tracker;
        private float startX;
        private float startY;

        public ViewContainer(Context context) {
            super(context);
//			leftMargin= DensityUtil.dip2px(35);

        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (isGestureOpen == false) {
                return super.dispatchTouchEvent(ev);
            }
            switch (ev.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    //当满足下面条件时 视为 右划关闭手势
                    //起始按压位置x坐标小与leftMargin&& 向右滑动                       &&           向右滑动距离    >   竖直方向距离
                    if (startX < leftMargin && ev.getRawX() > startX && ev.getRawX() - startX > Math.abs(ev.getRawY() - startY)) {
                        //速度大于2500时关闭activity
                        tracker.computeCurrentVelocity(1000);
                        if (tracker.getXVelocity() > 2500) {
                            finish();
                        }

                    }

                    tracker.recycle();
                    break;

                case MotionEvent.ACTION_DOWN:
                    startX = ev.getRawX();
                    startY = ev.getRawY();
                    tracker = VelocityTracker.obtain();
                    tracker.addMovement(ev);
                    break;
                case MotionEvent.ACTION_MOVE:
                    tracker.addMovement(ev);
                    break;
            }


            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (isGestureOpen == false) {
                return super.onTouchEvent(event);
            }
            return true;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables2Destroy == null) {
            throw new IllegalStateException(
                    "onDestroy called multiple times or onCreate not called");
        }
        disposables2Destroy.dispose();
        disposables2Destroy = null;
    }



    /**
     * 启动应用的设置
     */
    public void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }




}
