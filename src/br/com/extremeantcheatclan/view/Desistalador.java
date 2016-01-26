package br.com.extremeantcheatclan.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;

public class Desistalador extends JFrame{

	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JLabel descricaoProgress;
	
	public static void main(String[] args) {
		new Desistalador();
	}
	
	public Desistalador() {
		Util.themaNimbus();
		this.setTitle("Desistalador extreme ant cheat");
		this.setSize(600, 210);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));
		
		this.getContentPane().setBackground(Color.WHITE);
		this.setVisible(true);
		
		JLabel tituloAtaulizador = new JLabel("DESISTALANDO EXTREME ANT CHEAT");
		tituloAtaulizador.setBounds(140, 20, 297, 50);
		tituloAtaulizador.setFont(new Font("", Font.BOLD,15));
		this.add(tituloAtaulizador);
		
		this.createImagenInfoLadoEsquerdo();
		
		this.startProgress();

		this.repaint();

		this.desistalar();

	}
	
	private void createImagenInfoLadoEsquerdo(){
		JLabel img01 = new JLabel(LayoutGenericXK.getImageIconXK(), 0);
		img01.setBounds(28, 20, 200, 50);
		this.add(img01);
		
		JLabel img02 = new JLabel(LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK_INFO_100_67),0);
		img02.setBounds(5, 10, 100, 50);
		this.add(img02);
	}

	private void desistalar(){
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
		Util.timeOut(4000);
		
		//Exlui os servicos
		descricaoProgress.setText("Excluindo servico antigo");
		Util.executeComandoCMD(String.format(LoginHelper.COMANDO_DELETE_SERVICO, nameServiceOld));
		progressBar.setValue(15);
		Util.timeOut(1000);
		descricaoProgress.setText("Excluindo Servico");
		Util.executeComandoCMD(String.format(LoginHelper.COMANDO_DELETE_SERVICO, nameServiceNew));
		progressBar.setValue(20);
		Util.timeOut(1000);
		
		File dier = new File(LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT);
		File[] diers = dier.listFiles();
		for (int i = 0; i < diers.length; i++) {
			if(!diers[i].isDirectory()){
				descricaoProgress.setText(String.format("Excluindo arquivo [%s]", diers[i].getName()));
				diers[i].delete();
				progressBar.setValue(progressBar.getValue()+5);
				Util.timeOut(1000);
			}else{
				File[] subDiers = diers[i].listFiles();
				for (int j = 0; j < subDiers.length; j++) {
					descricaoProgress.setText(String.format("Excluindo arquivo [%s]", subDiers[j].getName()));
					subDiers[j].delete();
					progressBar.setValue(progressBar.getValue()+5);
					Util.timeOut(1000);
				}
				descricaoProgress.setText("Excluindo diretorio [LOGS]");
				diers[i].delete();
				progressBar.setValue(progressBar.getValue()+5);
				Util.timeOut(1000);
			}
		}
		descricaoProgress.setText("Excluindo diretorio [extreme ant cheat]");
		dier.delete();
		progressBar.setValue(progressBar.getValue()+5);
		Util.timeOut(1000);

		descricaoProgress.setText("Desistalando tarefa [extreme ant cheat]");
		progressBar.setValue(progressBar.getValue()+5);
		Util.executeComandoCMD(LoginHelper.DELETE_AGENDAMENTO_TAREFA);
		Util.timeOut(1000);
		
		descricaoProgress.setText("Finalizando desistalacao...");
		progressBar.setValue(progressBar.getValue()+5);
		Util.timeOut(1000);
		
		setVisible(false);
		new Login();
	}
	
	private void startProgress(){
		descricaoProgress = new JLabel("Iniciando desistalacao...");
		descricaoProgress.setBounds(110, 80, 250, 20);
		this.add(descricaoProgress);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(100, 100, 400, 25);	
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