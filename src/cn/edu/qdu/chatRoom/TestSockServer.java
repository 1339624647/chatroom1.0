package cn.edu.qdu.chatRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestSockServer {
	//���弯�ϴ洢ͨ��
	static List<Client> clientList = new ArrayList<Client>();

	public void init() {
		ServerSocket server = null;
		Socket socket = null;
		try {
			// �򿪶˿ڣ��ȴ����������ӡ�
			server = new ServerSocket(8888);
			System.out.println("�������ѿ�����");
			// �����ѭ�������������пͻ������ӣ�����˽��ܲ��Ҵ���һ���̣߳�������ͨ��socket������߳��У��������̡߳�
			while (true) {
				socket = server.accept();
				Client c = new TestSockServer().new Client(socket);
				clientList.add(c);// �������ӵĿͻ��˴���list�У��Ա��ں������������Ϣ��
				c.start();
			}

		} catch (IOException e) {
			System.out.println("server over!");
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestSockServer ts = new TestSockServer();
		ts.init();
	}

	// ��Ϊ��Ҫ���ö���߳�����ɿͻ��˵����ӣ���������ʹ���ڲ��ࡣ
	class Client extends Thread {
		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;

		// ʹ�ô������Ĺ��췽������ͨ�������߳��С�
		public Client(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			String str = null;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				// ѭ��������������ĳ���ͻ��˴�������Ϣ�����ҽ���Щ��Ϣ�������͸������ͻ��ˡ�
				while (true) {
					if ((str = dis.readUTF()) != null) {
						// dos.writeUTF(socket.getPort() + "say:" + str);
						// ѭ����������Ϣ���͸����пͻ��ˡ�
						for (int i = 0; i < clientList.size(); i++) {
							new DataOutputStream(clientList.get(i).socket.getOutputStream())
									.writeUTF(socket.getPort() + ":" + str);
						}
					}
				}
			} catch (IOException e) {
				System.out.println("�ͻ���" + socket.getPort() + "�˳�");
				// e.printStackTrace();
			} finally {
				// �رո�����
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}
}