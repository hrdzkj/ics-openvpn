/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.liuyi;

import java.text.SimpleDateFormat;


import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;

public class PublicData extends Application {
	public static String IMEI; //
	public static SimpleDateFormat datetimesdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
	private static Context mContext;
	public static TextToSpeech mTts;
	public static int displayWidth; // ��Ļ���
	public static int displayHeight; // ��Ļ�߶�
	public static String ip= "10.203.7.1";
	public static int port=10868;
	public static int curTimeOutThread=0;
	public static boolean isreceive = false;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
	}

	/**
	 * ��ȡContext.
	 * 
	 * @return
	 */
	public static Context getContext() {
		return mContext;
	}

	public int getDisplayWidth() {
         return 0;
	}
	
	public int getDisplayHeight() {
        return 0;
	}
}
