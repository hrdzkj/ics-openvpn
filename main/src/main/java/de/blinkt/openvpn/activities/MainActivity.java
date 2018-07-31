/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4n.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.reactivestreams.Subscriber;
import org.spongycastle.util.net.HttpUtils;
import org.spongycastle.util.net.UrlConst;
import org.spongycastle.util.uiutils.ToastUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import de.blinkt.openvpn.R;
import de.blinkt.openvpn.fragments.AboutFragment;
import de.blinkt.openvpn.fragments.FaqFragment;
import de.blinkt.openvpn.fragments.GeneralSettings;
import de.blinkt.openvpn.fragments.GraphFragment;
import de.blinkt.openvpn.fragments.LogFragment;
import de.blinkt.openvpn.fragments.SendDumpFragment;
import de.blinkt.openvpn.fragments.VPNProfileList;
import de.blinkt.openvpn.liuyi.ClientSock;
import de.blinkt.openvpn.views.ScreenSlidePagerAdapter;
import de.blinkt.openvpn.views.SlidingTabLayout;
import de.blinkt.openvpn.views.TabBarView;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    // private SlidingTabLayout mSlidingTabLayout;
    private TabBarView mTabs;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        initFloatButton();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), this);

        /* Toolbar and slider should have the same elevation */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            disableToolbarElevation();
        }


        mPagerAdapter.addTab(R.string.vpn_list_title, VPNProfileList.class);
        mPagerAdapter.addTab(R.string.graph, GraphFragment.class);

        mPagerAdapter.addTab(R.string.generalsettings, GeneralSettings.class);
        mPagerAdapter.addTab(R.string.faq, FaqFragment.class);

        if (SendDumpFragment.getLastestDump(this) != null) {
            mPagerAdapter.addTab(R.string.crashdump, SendDumpFragment.class);
        }


        if (isDirectToTV())
            mPagerAdapter.addTab(R.string.openvpn_log, LogFragment.class);

        mPagerAdapter.addTab(R.string.about, AboutFragment.class);
        mPager.setAdapter(mPagerAdapter);

        mTabs = (TabBarView) findViewById(R.id.sliding_tabs);
        mTabs.setViewPager(mPager);
    }

    private static final String FEATURE_TELEVISION = "android.hardware.type.television";
    private static final String FEATURE_LEANBACK = "android.software.leanback";

    private boolean isDirectToTV() {
        return (getPackageManager().hasSystemFeature(FEATURE_TELEVISION)
                || getPackageManager().hasSystemFeature(FEATURE_LEANBACK));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void disableToolbarElevation() {
        ActionBar toolbar = getActionBar();
        toolbar.setElevation(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            String page = getIntent().getStringExtra("PAGE");
            if ("graph".equals(page)) {
                mPager.setCurrentItem(1);
            }
            setIntent(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_log) {
            Intent showLog = new Intent(this, LogWindow.class);
            startActivity(showLog);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(data);
    }

    private String mIp = "10.203.7.1";

    private void showInputIpDialog() {
        final EditText et = new EditText(this);
        et.setText(mIp);
        new AlertDialog.Builder(this).setTitle("搜索")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", (dialog, which) -> {
                    String input = et.getText().toString().trim();
                    if (TextUtils.isEmpty(input)) {
                        Toast.makeText(getApplicationContext(), "搜索内容不能为空！" + input, Toast.LENGTH_LONG).show();
                        return;
                    }
                    mIp = input;
                    callLocalServer(mIp);

                })
                .setNegativeButton("取消", null)
                .show();

    }


    private void callLocalServer(String ip) {
        String url = "http://" + ip + ":8080/OpensslService/hello";  //UrlConst.HOST + UrlConst.URL_LOGIN;
        Log.v("------>", "http is called:  " + url);
        Map<String, String> para = new HashMap<>();
        para.put("username", "ceshi1");
        para.put("password", "ceshi1");
        HttpUtils.post(this, url, para, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                ToastUtil.showShort(response.body());
                Log.v("------>", "http is result:  " + response.body());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtil.showShort("网络异常！");
                Log.v("------>", "http is error:  ");
                if (response != null && response.body() != null) {
                    Log.v("------>", "http is error:  " + response.body());
                }
            }
        });

    }


    private void initFloatButton() {
        findViewById(R.id.floatButton).setOnClickListener(view -> {
            // showInputIpDialog();
            sendDataObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String string) throws Exception {
                         Log.v("------->",string);
                         ToastUtil.showShort(string);
                        }
                    });
        });
    }


    private io.reactivex.Observable sendDataObservable() {

        io.reactivex.Observable observable = io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> userObservable) throws Exception {
                Socket socket = ClientSock.getInstance(true);
                if (socket != null) {
                    byte[] bytes = {23,43,56,78};
                    if ((!socket.isClosed()) && (bytes != null)) {
                        try {
                            OutputStream out = socket.getOutputStream();
                            out.write(bytes); // out.write(b) out.write(b,0,3)
                            out.flush();

                            userObservable.onNext("成功发送，size="+bytes.length);
                            userObservable.onComplete();

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            socket = null;
                            userObservable.onNext("发送异常");
                            userObservable.onComplete();
                        }

                    }else{
                        userObservable.onNext("socket 已经关闭");
                        userObservable.onComplete();
                    }
                }else{
                    userObservable.onNext("socket未初始化");
                    userObservable.onComplete();
                }
            }
        });

        return observable;


    }

}
