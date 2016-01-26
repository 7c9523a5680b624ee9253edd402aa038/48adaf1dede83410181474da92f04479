package br.com.extremeancheatclan.socket;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class GenericSocket {

	protected static final String LOCAL_HOST_IP = "127.0.0.1";
	protected static final Integer LOCAL_HOST_PORT = 9876;
	public static final String COMMANDO_SOCKET_SCREENSHOT = "SCREENSHOT";
	public static final String COMMANDO_SOCKET_STOP_PAINEL_SERVICE = "stop-painel-service";
	public static final String COMMANDO_SOCKET_THEMA_BASIC = "thema_basic";
	
	protected ServerSocket server;
	protected Socket client;
	protected boolean isMessageHost = true;
	
	private static Object object;

	protected byte[] createEndereco(){
		try{
			byte[] endereco = InetAddress.getByName("localhost").getAddress();
			System.out.println(endereco[0] + "." + endereco[1] + "." + endereco[2] + "." + endereco[3]);
			
			return endereco;
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
		return null;
	}

	public static void send(Object object){
		GenericSocket.object = object;
	}
	
	public static void remove(){
		GenericSocket.object = null;
	}
	
	public static Object findObject(){
		return object;
	}
	
}