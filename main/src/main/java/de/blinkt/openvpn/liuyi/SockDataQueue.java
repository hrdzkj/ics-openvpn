/*
 * Copyright (c) 2012-2018 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.liuyi;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SockDataQueue {
	private SockData head, tail;
	private Lock lock;
	private int count;

	public SockDataQueue() {
		lock = new ReentrantLock();
		head = new SockData();
		tail = head;
		count = 0;
	}

	public boolean enqueue(final int len, byte[] bytes) // ���
	{
		lock.lock();
		try {
			if (len > 0) {
				SockData node = new SockData();
				node.SetData(len, bytes);
				node.SetNext(null);

				tail.SetNext(node);
				tail = node;
				count++;

				return true;
			}
			;
			return false;
		}

		finally {
			lock.unlock();
		}
	};

	public int dequeue(byte[] value) {
		lock.lock();
		try {
			if (count > 0) {
				SockData node = head.GetNext();
			    
				byte[] bytes = node.GetData();
				head.SetNext(node.GetNext());
				//node = null;
				System.arraycopy(bytes, 0, value, 0, node.len);

				count--;

				// ���Ҫ�ÿ�һ�����У�βָ�뽫������Ч����˽�������ָ��ͷ�ڵ�
				if (count == 0) {
					tail = head;
				}
				return node.len;
			}
			else
			{
				return -1;
			}

		} finally {
			lock.unlock();
		}
	}
}
