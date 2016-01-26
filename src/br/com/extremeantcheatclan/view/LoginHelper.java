package br.com.extremeantcheatclan.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.JogadorWarface;
import br.com.extremeantcheatclan.util.Util;

public class LoginHelper {

	private final static Logger logger = Logger.getLogger(Login.class.getName());

	public static final String COMANDO_DELETE_SERVICO = "sc delete %s";
	public static final String START_SERVICE = "net start %s";
	public static final String STOP_SERVICE = "net stop %s";
	
	public static final String PATH_WINDOWS = "C:";
	public static final String PATH_SERVICE_EXTREME_ANT_CHEAT = String.format("%s\\extreme-ant-cheat-service", PATH_WINDOWS);
	private static final String PRUNSRV = "prunsrv.exe";
	private static final String INSTALL_BAT = "Install.bat";
	public static final String PRE_INSTALL_BAT = "pre-install.bat";
	private static final String SO_64 = "so64";
	private static final String SO_32 = "so32";
	private static final String CONF = "configuracao";
	private static final String PATH_PR_JVM = getPathPRJVM();
	public static final String AGENDAMENTO_TAREFA = "schtasks /create /tn \"extreme-ant-cheat-service\" "
			+ "/tr C:\\extreme-ant-cheat-service\\painel-servico-xk.exe /sc onlogon";
	public static final String DELETE_AGENDAMENTO_TAREFA = "schtasks /delete /tn \"extreme-ant-cheat-service\" /f";
	
	public static final String[] TASK_JAR_AND_SIZE = {"task.jar","130695"};
	private static final String[] DRIVER_POSTGRES_AND_SIZE = {"postgresql.jdbc4.jar","579981"};
	private static final String[] JL_MEDIA_PLAYER_AND_SIZE = {"jl1.0.1.jar","105363"};
	private static final String[] PAINEL_SERVICO_XK_JAR_AND_SIZE = {"painel-servico-xk.exe","6331446"};
	public static final String[] ATUALIZADOR_OF_SERVICE_JAR_AND_SEZE = {"atualizador-of-service.exe","2094195"};
	
	public static void createPrunsrvSO64() {
		try(InputStream stream = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", SO_64,PRUNSRV));
				OutputStream escritorByte = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, PRUNSRV))){
				byte[] prunsrvByte = inputStreamToByte(stream, 109696l);
				escritorByte.write(prunsrvByte);
		}catch (IOException e) {
			logger.log(Level.CONFIG, e.getMessage());
		}
	}
	
	public static void createPrunsrvSO32() {
		try(InputStream stream = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", SO_32,PRUNSRV));
				OutputStream escritorByte = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, PRUNSRV))){
				byte[] prunsrvByte = inputStreamToByte(stream, 86656l);
				escritorByte.write(prunsrvByte);
		}catch (IOException e) {
			logger.log(Level.CONFIG, e.getMessage());
		}
	}
	
	public static void createTaskStartJarAndUtis() {
		try(InputStream streamTask = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", CONF, TASK_JAR_AND_SIZE[0]));
				InputStream streamPostgres = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", CONF, DRIVER_POSTGRES_AND_SIZE[0]));
				InputStream streamJL = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", CONF, JL_MEDIA_PLAYER_AND_SIZE[0]));
				InputStream streamPainelJar = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", CONF, PAINEL_SERVICO_XK_JAR_AND_SIZE[0]));
				InputStream streamAtualizadorJar = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", CONF, ATUALIZADOR_OF_SERVICE_JAR_AND_SEZE[0]));
				OutputStream escritorTask = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, TASK_JAR_AND_SIZE[0]));
				OutputStream escritorPostgres = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, DRIVER_POSTGRES_AND_SIZE[0]));
				OutputStream escritorJl = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, JL_MEDIA_PLAYER_AND_SIZE[0]));
				OutputStream escritorPainelJar = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, PAINEL_SERVICO_XK_JAR_AND_SIZE[0]));
				OutputStream escritorAtualizadorJar = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, ATUALIZADOR_OF_SERVICE_JAR_AND_SEZE[0]));
				OutputStream escritorVersaoExtrme = new FileOutputStream(String.format("%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT,"version.data"))){
			
				byte[] taskByte = inputStreamToByte(streamTask, Long.valueOf(TASK_JAR_AND_SIZE[1]));
				byte[] postgresByte = inputStreamToByte(streamPostgres, Long.valueOf(DRIVER_POSTGRES_AND_SIZE[1]));
				byte[] jlByte = inputStreamToByte(streamJL, Long.valueOf(JL_MEDIA_PLAYER_AND_SIZE[1]));
				byte[] painelJarByte = inputStreamToByte(streamPainelJar, Long.valueOf(PAINEL_SERVICO_XK_JAR_AND_SIZE[1]));
				byte[] atualizadorJarByte = inputStreamToByte(streamAtualizadorJar, Long.valueOf(ATUALIZADOR_OF_SERVICE_JAR_AND_SEZE[1]));
				
				escritorJl.write(jlByte);
				escritorTask.write(taskByte);
				escritorPostgres.write(postgresByte);
				escritorPainelJar.write(painelJarByte);
				escritorAtualizadorJar.write(atualizadorJarByte);
				
				String md5Versao = Util.parseHashMD5(
						new XitersDAOImpl().getVersaoExtremeAntChet().getNovaVersao());
				escritorVersaoExtrme.write(md5Versao.getBytes());
		}catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public static void installServicoExtremeAntCheat() {
		try {
			String pathBat = String.format("%s\\%s", PATH_SERVICE_EXTREME_ANT_CHEAT, INSTALL_BAT);
			Runtime.getRuntime().exec(pathBat);
			
			Thread.sleep(3000);
			Runtime.getRuntime().exec(AGENDAMENTO_TAREFA);
			
			Thread.sleep(2000);
			String painelServicoXk = String.format("%s\\%s", PATH_SERVICE_EXTREME_ANT_CHEAT,PAINEL_SERVICO_XK_JAR_AND_SIZE[0]);
			
			Util.openAplication(painelServicoXk);
			Thread.sleep(2000);
		} catch (IOException e) {
			logger.log(Level.CONFIG, e.getMessage());
		}catch (InterruptedException e) {
			logger.log(Level.CONFIG, e.getMessage());
		}
	}
	
	public static void updatePathInInstallBat(){
		String installBat = "";
		File instalBatFile = new File(String.format("/%s/%s", PATH_SERVICE_EXTREME_ANT_CHEAT, INSTALL_BAT));
		instalBatFile.setExecutable(true);
		instalBatFile.setReadable(true);
		try(InputStream stream = LoginHelper.class.getResourceAsStream(String.format("/%s/%s", CONF, PRE_INSTALL_BAT));
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
				OutputStream escritorByte = new FileOutputStream(instalBatFile)){
			String readLine = "";
			String tokken = " set PR_JVM=";
			while((readLine = bufferedReader.readLine()) != null){
				if(readLine.contains(tokken)){
					String server = PATH_PR_JVM.concat("\\server\\jvm.dll");
					String client = PATH_PR_JVM.concat("\\client\\jvm.dll");
					
					if(new File(server).exists()){
						readLine += server;	
					}else if(new File(client).exists()){
						readLine += client;
					}else{
						JOptionPane.showMessageDialog(null, "Nao foi encontrato cominho do Java...");
						System.exit(0);
					}
					
					installBat += readLine.concat("\n");
				}else{
					installBat += readLine.concat("\n");
				}
			}
			
			escritorByte.write(installBat.getBytes());
		}catch (IOException e) {
			logger.log(Level.CONFIG, e.getMessage());
		}
	}
	
	public static String getPathPRJVM(){
		String pathJava = System.getProperty("java.library.path").split(";")[0];
		return pathJava;
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

	public static boolean isAdministrador(){
		File fileAdmin = new File("C:\\Windows\\System32\\admin-jaderosn-bdfsd");
		if(fileAdmin.mkdir()){
			fileAdmin.delete();
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isExistJogadorWarface(){
		try{
			JogadorWarface jogadorWarface = Util.getJogadorWarface();
			if(jogadorWarface != null && jogadorWarface.getId() > 0){
				return true;
			}
		}catch(Exception e){
			return false;
		}
		
		return false;
	}
	
}