/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.liuyi;

import java.io.InputStream;
import java.net.Socket;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class SocketService extends Service {
	private byte[] buffer = new byte[1024];
	private SockDataQueue dataQueue;
	private MyBinder mBinder = new MyBinder();

	public class MyBinder extends Binder {
		SocketService getService() {
			return SocketService.this;
		}
	}

	public void onCreate() {
		super.onCreate();
		dataQueue = TCPData.getInstance();
		new Thread(null, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					Socket socket = ClientSock.getInstance(false);
					try {

						if ((socket != null) && (!socket.isClosed())) {
							InputStream in = socket.getInputStream();
							int size = in.read(buffer, 0, 1024);
							Log.v("收到服务器数据","长度 size="+size);
							Thread.sleep(500);

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}, "SockReciver").start();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
