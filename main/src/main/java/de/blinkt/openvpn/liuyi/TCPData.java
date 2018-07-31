/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.liuyi;

import java.net.Socket;

public class TCPData {

	private static SockDataQueue instance;

	private TCPData() {
		// TODO Auto-generated constructor stub
	}

	public static synchronized SockDataQueue getInstance() {
		if (instance == null) {
			instance = new SockDataQueue();
		}
		;
		return instance;
	};

}
