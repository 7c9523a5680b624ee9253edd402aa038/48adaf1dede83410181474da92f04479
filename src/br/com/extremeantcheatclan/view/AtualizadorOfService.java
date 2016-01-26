package br.com.extremeantcheatclan.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.dropbox.DropBoxApiDonwload;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;

public class AtualizadorOfService extends JFrame{

	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JLabel descricaoProgress;
	
	//Limit maximo de caracter pra ficar bonitinho no JTextArea
	public static final int LIMIT_CARACTER = 64;
	
	public static void main(String[] args) {
		new AtualizadorOfService(new XitersDAOImpl().getVersaoExtremeAntChet().getDescricaoVersao());
	}
	
	public AtualizadorOfService(String descricaoToAtualizacao) {
		Util.themaNimbus();
		this.setTitle("Atualizador extreme ant cheat");
		this.setSize(600, 410);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));
		
		this.getContentPane().setBackground(Color.WHITE);
		this.setVisible(true);
		
		JLabel tituloAtaulizador = new JLabel("ATUALIZADO EXTREME ANT CHEAT");
		tituloAtaulizador.setBounds(160, 20, 280, 50);
		tituloAtaulizador.setFont(new Font("", Font.BOLD,15));
		this.add(tituloAtaulizador);
		
		JLabel descricaoAtaulizador = new JLabel("Descricao da atualizacao");
		descricaoAtaulizador.setBounds(220, 50, 150, 50);
		descricaoAtaulizador.setFont(new Font("", Font.BOLD,12));
		this.add(descricaoAtaulizador);
		
		JTextArea descricao = new JTextArea(descricaoToAtualizacao);
		JScrollPane descricaoScrol = new JScrollPane(descricao); 
		descricaoScrol.setBounds(100, 90, 400, 130);
		this.getContentPane().add(descricaoScrol);
		
		descricaoProgress = new JLabel("Iniciando desistalacao...");
		descricaoProgress.setBounds(110, 230, 250, 20);
		this.add(descricaoProgress);
		
		this.startProgress();
		
		this.repaint();
		
		this.updateExtremeAntCheat();
	}
	
	private void updateExtremeAntCheat() {
		String nameServiceOld = "TaskStart";
		String nameServiceNew = "TaskExtreme";
		
		descricaoProgress.setText("Matando processo painel-servico-xk");
		progressBar.setValue(2);
		Util.timeOut(1000);
		
		//Desativa os servicos anteriores
		descricaoProgress.setText("Parando servico da versao antigo");
		Util.executeComandoCMD(String.format(LoginHelper.STOP_SERVICE, nameServiceOld));
		progressBar.setValue(3);
		Util.timeOut(1000);
		descricaoProgress.setText("Parando servico da nova versao");
		Util.executeComandoCMD(String.format(LoginHelper.STOP_SERVICE, nameServiceNew));
		progressBar.setValue(10);
		Util.timeOut(1000);
		
		//Exlui os servicos
		descricaoProgress.setText("Excluindo servico antigo");
		Util.executeComandoCMD(String.format(LoginHelper.COMANDO_DELETE_SERVICO, nameServiceOld));
		progressBar.setValue(15);
		Util.timeOut(1000);
		descricaoProgress.setText("Excluindo Servico");
		Util.executeComandoCMD(String.format(LoginHelper.COMANDO_DELETE_SERVICO, nameServiceNew));
		progressBar.setValue(20);
		Util.timeOut(1000);
		
		String logs = LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT+"\\logs";
		if(new File(logs).exists()){
			File[] diersLog = new File(logs).listFiles();
			for (int i = 0; i < diersLog.length; i++) {
				descricaoProgress.setText(String.format("Excluindo arquivo [%s]", diersLog[i].getName()));
				diersLog[i].delete();
				progressBar.setValue(progressBar.getValue()+5);
				Util.timeOut(1000);
			}
		}else{
			JOptionPane.showMessageDialog(null, "Arquivo de logs nao existe."
					+ "\n Entrar em contato com admin");
		}
		progressBar.setValue(25);
		Util.timeOut(1000);
		
		String taskJar = LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT+"\\"+LoginHelper.TASK_JAR_AND_SIZE[0];
		if(new File(taskJar).exists()){
			descricaoProgress.setText("Excluindo arquivo task.jar");
			new File(taskJar).delete();
			progressBar.setValue(progressBar.getValue()+5);
			Util.timeOut(1000);
		}else{
			JOptionPane.showMessageDialog(null, "Arquivo de task.jar nao existe."
					+ "\n Entrar em contato com admin");
		}
		progressBar.setValue(30);
		Util.timeOut(1000);
		
		String versionData = LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT+"\\version.data";
		if(new File(versionData).exists()){
			descricaoProgress.setText("Excluindo arquivo version.data");
			new File(versionData).delete();
			progressBar.setValue(progressBar.getValue()+5);
			Util.timeOut(1000);
		}else{
			JOptionPane.showMessageDialog(null, "Arquivo de version.data nao existe."
					+ "\n Entrar em contato com admin");
		}
		progressBar.setValue(40);
		Util.timeOut(1000);
		
		byte[] taskAtualizado = null;
		byte[] painelServicoXkAtualizado = null;
		try{
			//Pega o task.jar no dropbox
			descricaoProgress.setText("Fazendo donwload do extreme-task atualizado");
			taskAtualizado = DropBoxApiDonwload.getDonwloadExtremeAntCheat("task.jar");
			progressBar.setValue(75);
			Util.timeOut(1000);
			
			//Pega o painel-servico-xk.jar no dropbox
			descricaoProgress.setText("Fazendo donwload do extreme-painel atualizado");
			painelServicoXkAtualizado = DropBoxApiDonwload.getDonwloadExtremeAntCheat("painel-servico-xk.exe");
			progressBar.setValue(80);
			Util.timeOut(1000);
			
			if(taskAtualizado == null || painelServicoXkAtualizado == null){
				LayoutGenericXK.mesageDialogIcoInfo("Atualizacao Avancada", "Este tipo de atualizacao sera necessario "
						+ "\ndownload direto do site", LayoutGenericXK.PATH_ICO_XK_INFO_100_67);
				System.exit(0);
			}
		}catch(Exception e){
			LayoutGenericXK.mesageDialogIcoInfo("Atualizacao Avancada", "Este tipo de atualizacao sera necessario "
					+ "\ndownload direto do site", LayoutGenericXK.PATH_ICO_XK_INFO_100_67);
			System.exit(0);
		}
		
		//Escrevi os arquivos ataulizdos no pasta extreme-ant-cheat-service
		descricaoProgress.setText("Instalando extreme atualizado...");
		try(OutputStream escritorTaskByte = new FileOutputStream(String.format("%s//%s", 
				LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT,LoginHelper.TASK_JAR_AND_SIZE[0]));
			OutputStream escritorPainelByte = new FileOutputStream(String.format("%s//%s", 
					LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT,LoginHelper.ATUALIZADOR_OF_SERVICE_JAR_AND_SEZE[0]));	
			OutputStream escritorVersaoByte = new FileOutputStream(String.format("%s/%s", 
				LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT,"version.data"))){
			
			escritorTaskByte.write(taskAtualizado);
			escritorPainelByte.write(painelServicoXkAtualizado);
			
			String md5Versao = Util.parseHashMD5(
					new XitersDAOImpl().getVersaoExtremeAntChet().getNovaVersao());
			escritorVersaoByte.write(md5Versao.getBytes());
		}catch (Exception e) {
			System.out.println("Erro ao atualizar o task.jar ou a version.data. Expetion="+e.getMessage());
		}
		progressBar.setValue(90);
		Util.timeOut(1000);
		
		descricaoProgress.setText("Instalando servico extreme ant cheat");
		LoginHelper.installServicoExtremeAntCheat();
		progressBar.setValue(100);
		Util.timeOut(1000);
		
		JOptionPane.showMessageDialog(null, "Atualizacao realizada com sucesso...");
		System.exit(0);
	}

	private void startProgress(){
		progressBar = new JProgressBar();
		progressBar.setBounds(100,250,400,25);	
		progressBar.setMinimum(0); 
		progressBar.setMaximum(100); 
		progressBar.setStringPainted(true); 
		
		progressBar.setValue(0);
		this.add(progressBar);
	}
	
	private byte[] getImageIco(){
		try(InputStream in = Login.class.getResourceAsStream(LayoutGenericXK.PATH_ICO_XK[0])){
			return LoginHelper.inputStreamToByte(in, 1005l);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}