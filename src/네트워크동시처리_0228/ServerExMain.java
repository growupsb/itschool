package 네트워크동시처리_0228;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.DataType;

public class ServerExMain {
	private static ServerSocket serverSocket = null;
	private static ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	public static void main(String[] args) {
		System.out.println("서버를 종료하려면 q 또는 Q를 입력하고 Enter 키를 입력하세요.");
		
		startServer();
		
		while(true) {
			String key = DataType.inputString();
			if(key.toLowerCase().equals("q")) {
				break;
			}
		}
		
		stopServer(); //TCP 서버 종료
	}

	public static void startServer() {
		Thread thread = new Thread() {
			public void run() {
				try { 
					serverSocket = new ServerSocket(60001);
					System.out.println("[서버 시작]");
					
					while(true) {
						
					//연결 수락
					Socket socket = serverSocket.accept();
					
					executorService.execute(() -> {
						try {
							//연결된 클라이언트 정보 얻기
							InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
							System.out.println("[서버] " + isa.getHostName() + "의 연결 요청을 수락함");
							//데이터 받기
							DataInputStream dis = new DataInputStream(socket.getInputStream());
							String message = dis.readUTF();
							//데이터 보내기
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							dos.writeUTF(message);
							dos.flush(); // 버퍼 비우기
							System.out.println("[서버] 받은 데이터를 다시 보냄 : " + message);
							
							//연결 끊기
							socket.close();
							System.out.println("[서버]" + isa.getHostName() + "의 연결을 끊음");
						} catch(IOException e) {
						}												
					});
				}
				} catch(IOException e) {
						System.out.println("[서버]" + e.getMessage());
					}
				}
			};
		thread.start();
	}

	private static void stopServer() {
		try {
			serverSocket.close();
			System.out.println("[서버] 종료됨");
		} catch (IOException e) {}
	}
}
