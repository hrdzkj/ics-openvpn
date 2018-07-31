/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.liuyi;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class ClientSock {

	private static Socket instance;


	private ClientSock() {
		// TODO Auto-generated constructor stub
	}
	
	public static void CloseSocket(){
		if (instance!=null){
			try {
				instance.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		instance = null;
	};
	public static synchronized Socket getInstance(final boolean create ) {
		try {
			if (instance != null) {
				if (instance.isClosed()) {
					instance.close();
				}
			}
			;

			instance = new Socket(PublicData.ip, PublicData.port);
		}catch (Exception e){
			e.printStackTrace();
		}
		return instance;
		/*
		if (instance!=null){
        	if (instance.isClosed()) {
        	  instance = null;
        	}
         };

		
		if ((instance == null)&& create ) {
			try {
				instance = new Socket(PublicData.ip,PublicData.port);
			} catch (Exception e) {
				instance = null;
				e.printStackTrace();
				
				StringWriter sw = new StringWriter();   
				e.printStackTrace(new PrintWriter(sw, true));	            
				String str = sw.toString();	            
				System.out.println("==========");           
				System.out.println(str);

			}
		}
		return instance;
		*/
	}

	
}
