package br.com.extremeancheatclan.socket;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.extremeantcheatclan.business.TromServiceBO;

public class SocketTask extends GenericSocket implements SocketXK{

	public SocketTask(Object object) {
		GenericSocket.send(object);
	}
	
	public SocketTask() {
		/** Construtor vazio **/
	}
	
	public void openConnection(){
		try{
			server = new ServerSocket(LOCAL_HOST_PORT);
			server.setSoTimeout(70000);
			System.out.println("Inciando Servidor...");
			
			if (!server.isBound()){
				server.bind(new InetSocketAddress("192.168.0.1", 0));
			}
			
			while(TromServiceBO.SHUTDOWN) {
		        Socket serverOutClient = server.accept();
		        
		        if(isMessageHost){
		        	System.out.println("Cliente conectado SocketTaks para SocketPainel: " + 
		        			serverOutClient.getInetAddress().getHostAddress());
		        	isMessageHost = false;
		        } 
		        
	        	try{
	        		ObjectOutputStream saida = new ObjectOutputStream(serverOutClient.getOutputStream());
	        		saida.writeObject(GenericSocket.findObject());
		        	
//	        		if(GenericSocket.findObject() != null && 
//	        				TromServiceBO.macrosJaVerificados.containsKey(GenericSocket.findObject().toString())){
//	        		
//	        			ObjectInputStream objectInputStream = new ObjectInputStream(serverOutClient.getInputStream());
//			        	Object returnObject = objectInputStream.readObject();
//			        	if(returnObject != null && ! ((String) returnObject).isEmpty()){
//			        		String retornoToCliente = (String) returnObject;
//			        		System.out.println(GenericSocket.findObject().toString()+" - "+retornoToCliente);
//			        		TromServiceBO.macrosJaVerificados.put(GenericSocket.findObject().toString(), retornoToCliente);
//			        	}
//	        		
//	        		}
		        	
		        	GenericSocket.remove();
	        	}catch(Exception e){
		        	e.printStackTrace();
		        }
	        	serverOutClient.close();
		   }
			server.close();
		}catch(UnknownHostException e){
			System.out.println("Erro ao criar servidor ....");
		}catch(java.net.SocketTimeoutException e){
			System.out.println("Cliente demoro para se conectar... " +e.getMessage());
			
			//Reconectando o socket
			new SocketTask();
		}catch (java.net.BindException e) {
			System.out.println("Enderedo host ja esta sendo ultilizado... " + e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}