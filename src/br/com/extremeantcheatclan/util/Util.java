package br.com.extremeantcheatclan.util;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import br.com.extremeantcheatclan.entity.JogadorWarface;
import br.com.extremeantcheatclan.view.LoginHelper;

public class Util {

	//RzSynapse.exe
	public static final Integer SEQUENCIAL_HASH = 0201354222;
	public static final String CMD_THEMA_BAISC = "rundll32.exe %SystemRoot%\\system32\\shell32.dll,"
			+ "Control_RunDLL %SystemRoot%\\system32\\desk.cpl desk,@Themes //Action:OpenTheme "
			+ "//file:C:\\Windows\\Resources\\Ease of Access Themes\\basic.theme";
	public static final String TASK_LIST = "tasklist";
	public static final String TASK_KILL = "taskkill /F /IM %s";
	public static final String TASK_KILL_BY_TITLE = "taskkill /FI \"Windowtitle eq %s";
	public static final String ENDERECO_MAC = "getmac";
	private static final String USER = "user.bin";
	//public static final String  TASKLIST_RUNNING = "TASKLIST /v /fi \"STATUS eq running\"";
	public static final String  TASKLIST_RUNNING = "TASKLIST /v /fi \"USERNAME ne NT AUTHORITY\\SYSTEM\" /fi \"STATUS eq running\"";
	
	public static void themaNimbus(){
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openAplication(String commmando){
		File file = new File(commmando);
		if(file.exists()){
			try{
				Desktop.getDesktop().open(file);
				Util.timeOut(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			JOptionPane.showMessageDialog(null, "erro=comando-nao-executado-"+commmando);
		}
	}
	
	public static boolean executeComandoCMD(String cmd){
		try{
			Process process = Runtime.getRuntime().exec(cmd);
			Util.timeOut(2000);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return (bufferedReader.readLine() != null);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean executeComandoCMD(String[] cmd){
		try{
			Process process = Runtime.getRuntime().exec(cmd);
			Util.timeOut(2000);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return (bufferedReader.readLine() != null);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static String executeComandoCMDReturn(String cmd){
		try{
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String linha,retorno = null;
			while((linha = bufferedReader.readLine()) != null){
				retorno += "\n"+linha;
			}
			retorno = retorno.contains("null") ? retorno.replace("null", "") : retorno;
			return retorno;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
			
	public static String getComandoFileCMD(String comando){
		try(BufferedReader buffer = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream(comando)))){
			String linha = null;	
			return (linha = buffer.readLine()) != null ? linha : null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] capturePrintTela(){
		try {
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));  
			return convertiBufferedImageToByteArray(image);
		} catch (AWTException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] convertiBufferedImageToByteArray(BufferedImage originalImage){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static ImageIcon redimensionaImg(byte[] bytes, int new_w, int new_h){  
	    try {  
	    	InputStream image = new ByteArrayInputStream(bytes);
	        BufferedImage imagem = ImageIO.read(image);  
	        BufferedImage new_img = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);  
	  
	        Graphics2D g = new_img.createGraphics();  
	        g.drawImage(imagem, 0, 0, new_w, new_h, null);  
	        g.dispose();  
	  
	        return new ImageIcon(new_img);  
	    } catch (IOException ex) {  
	        throw new RuntimeException(ex);  
	    }  
	}

	public static void main(String[] args) {
		System.out.println(Util.parseHashMD5("ExtemeAntCheatClan"));
	}
	public static String parseHashMD5(String value){
		try {
			MessageDigest securty;
			securty = MessageDigest.getInstance("MD5");
		    securty.update(value.getBytes(),0,value.length());
		    BigInteger hash = new BigInteger(1, securty.digest(value.getBytes()));  

		    return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void JogadorWarfaceCreateLocalData(JogadorWarface jogadorWarface){
		try(OutputStream escritorByte = new FileOutputStream(String.format("%s/%s", 
				LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT, Util.USER));
				ObjectOutputStream escritorObjeto = new ObjectOutputStream(escritorByte)){
			
			escritorObjeto.writeObject(jogadorWarface);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JogadorWarface getJogadorWarface(){
		JogadorWarface jogadorWarface = null;
		try(InputStream leitorByte = new FileInputStream(String.format("%s/%s", 
				LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT,Util.USER));
				ObjectInputStream leitorObjeto = new ObjectInputStream(leitorByte)){
			
				jogadorWarface = (JogadorWarface) leitorObjeto.readObject(); 
		}catch (Exception e) {
			System.out.println("jogadorWarface esta null "+ e.getMessage());
			return null;
		}
		return jogadorWarface != null ? jogadorWarface:null;
	}
	
	public static void saveSession(Integer id){
		File gravaIdTemp = new File(String.format("%s/FileSession.data", LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT));
		try{
			if(!gravaIdTemp.exists()){
				gravaIdTemp.createNewFile();
			}else{
				gravaIdTemp.delete();
				gravaIdTemp.createNewFile();
			}
			try(OutputStream escritor = new FileOutputStream(gravaIdTemp, true)){
				String hash = id.toString() +"%"+Util.SEQUENCIAL_HASH.toString();
				byte[] idByte = hash.getBytes();
				escritor.write(idByte);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Integer getIdByFileSession(){
		File idFileSession = new File(String.format("%s/FileSession.data", LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT));
		Integer id = null;
		try(BufferedReader leitor = new BufferedReader(new FileReader(idFileSession))){
			String linha = null;
			if((linha = leitor.readLine()) != null){
				String[] idSplit = linha.split("%");
				id = Integer.valueOf(idSplit[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return id;
	}
	
	public static void removeJogadorWarfaceLocal(){
		new File(String.format("%s/%s", LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT,Util.USER)).delete();
	}
	
	public static boolean isInt(String value){
		try{
			Integer.valueOf(value);
			return true;		
		}catch(Exception e){
			return false;
		}
	}
	
	public static void timeOut(long time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getVersaoAtualExtemeAntCheat(boolean isNotServicoExtreme){
		File versao = new File(String.format("%s//version.data", 
				LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT));
		if(versao.exists()){
			try(BufferedReader leitor = new BufferedReader(new FileReader(versao))){
				return leitor.readLine();
			}catch(Exception e){
				if(isNotServicoExtreme){
					JOptionPane.showMessageDialog(null, "Arquivo de versao nao encontrado\nReinstale aplicacao!");
				}
				e.printStackTrace();
			}
		}else{
			if(isNotServicoExtreme){
				JOptionPane.showMessageDialog(null, "Arquivo de versao nao encontrado\nReinstale aplicacao!\nOu entre em contato com 'Jaderson'.");
			}
		}
		return "";
	}
	
	public static InputStream byteToInputStream(byte[] value){
		try(ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(value)){
			return arrayInputStream;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] inputStreamToByte(InputStream inputStream, Long size) throws IOException {
		byte[] bytes = new byte[size.intValue()];  
		int offset = 0;  
		int numRead = 0;  
		while (offset < bytes.length && (numRead=inputStream.read(bytes, offset, bytes.length-offset)) >= 0) {  
		    offset += numRead;  
		}  
		          
		if (offset < bytes.length) {  
		    throw new IOException("Could not completely read file ");  
		}
		
		return bytes;
	}
	
	public static void saveObjectBin(Object object, String src){
		try(OutputStream outputStream = new FileOutputStream(src);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)){
			
			objectOutputStream.writeObject(object);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getObjectBin(String scr){
		try(InputStream in = new FileInputStream(scr);
				ObjectInputStream leitorObjeto = new ObjectInputStream(in)){
			
			return leitorObjeto.readObject();
		}catch (Exception e) {
			System.out.println("jogadorWarface esta null "+ e.getMessage());
			return null;
		}
	}
	
	public static String verificaMacEnderec() {
		try{
			Process process = Runtime.getRuntime().exec(Util.ENDERECO_MAC);	
			BufferedReader leitor = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String linha,retorno = null;
			while((linha = leitor.readLine()) != null){
				retorno += linha+"\n %";
			}
			
			return retorno.split("%")[3];
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
}