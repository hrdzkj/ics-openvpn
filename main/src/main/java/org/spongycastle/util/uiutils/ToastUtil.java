/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package org.spongycastle.util.uiutils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.spongycastle.util.uiutils.ScreenUtils;

import de.blinkt.openvpn.R;
import de.blinkt.openvpn.core.ICSOpenVPNApplication;


public class ToastUtil {

    private static Toast mToast;

        public static void showLong(String string) {
        if (string == null || string.trim().equals("")) return;
        View layout = LayoutInflater.from(ICSOpenVPNApplication.getInstance()).inflate(R.layout.layout_toast, null);
        layout.getBackground().setAlpha(200);

        /*设置布局*/
        TextView textView = (TextView) layout.findViewById(R.id.text_content);
        textView.setText(string);

        if (mToast == null) {
            mToast = new Toast(ICSOpenVPNApplication.getInstance());
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.BOTTOM, 0, ScreenUtils.dip2px(ICSOpenVPNApplication.getInstance(),60f));
        mToast.setView(layout);
        mToast.show();
    }

    public static void showShort(String string) {
        if (string == null || string.trim().equals("")) return;
        View layout = LayoutInflater.from(ICSOpenVPNApplication.getInstance()).inflate(R.layout.layout_toast, null);
        layout.getBackground().setAlpha(200);

        /*设置布局*/
        TextView textView = (TextView) layout.findViewById(R.id.text_content);
        textView.setText(string);

        if (mToast == null) {
            mToast = new Toast(ICSOpenVPNApplication.getInstance());
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, ScreenUtils.dip2px(ICSOpenVPNApplication.getInstance(),60f));
        mToast.setView(layout);
        mToast.show();
    }


}
