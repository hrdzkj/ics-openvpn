/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.liuyi;


public class SockData{
	  private byte[] data;
	  private  SockData next;
	  public int len;
	  
	  public SockData()
	  {
		next  =null;
		data  =null;
		len = -1;
	  };
	  
	  
	  public void SetData(final int Len,byte[] value)
	  {
		if ((Len>0)&&(value !=null)){
		 data = new byte[Len];
		 System.arraycopy(value,0,data,0,Len);
		 this.len = Len;
		};
	  };
	  
	  public byte[] GetData(){
		  return data;
	  }	
	  
	  
	  public void SetNext(SockData value)
	  {
		 next = value; 
	  }
	  
	  public SockData GetNext(){
		 return next;
	  }
}

