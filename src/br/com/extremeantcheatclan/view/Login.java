package br.com.extremeantcheatclan.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.Border;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.JogadorWarface;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;
import br.com.extremeantcheatclan.util.UtilFile;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame aguardeWindows = new JFrame();
	private static final int CODIGO_SALA = 000000;
	private JProgressBar progressBar;
	
	public static void main(String[] args) {
		Util.themaNimbus();
		if(LoginHelper.isAdministrador()){
			if(!LoginHelper.isExistJogadorWarface()){
				new Login();
			}else{
				String msg = "Voce ja tem um usuario registrado nessa maquina.\nDeseja reinstalar?";
				String titulo = "Usuario registrado nessa maquina";
				if (LayoutGenericXK.confirmDialogIcoInfo(titulo, msg,LayoutGenericXK.PATH_ICO_XK_INFO_100_67) == JOptionPane.OK_OPTION){  
					new Desistalador();
				}else{
            		System.exit(0);
            	}
			}
		}else{
			JOptionPane.showMessageDialog(null, "Renicie o instalador como administrador");
			System.exit(0);
		}
	}
	
	public Login() {
		Util.themaNimbus();
		this.setTitle("Startando Ant Cheat XK-Extreme Killers");
		this.setLayout(null);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));  
		this.getContentPane().setBackground(Color.WHITE);
		
		this.box1();
		this.box2();
		
		this.setSize(600, 550);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		windwosAguarde();
	}
	
	private byte[] getImageIco(){
		try(InputStream in = Login.class.getResourceAsStream("/imagens/extreme-ant-cheat-ico.png")){
			return LoginHelper.inputStreamToByte(in, 1005l);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Imagen do age
	private void box1(){
		JPanel fieldsetImegenAge = new JPanel();
		fieldsetImegenAge.setBackground(Color.WHITE);
		fieldsetImegenAge.setLayout(null);
		Border border = BorderFactory.createTitledBorder("Warface - Start Ant Cheat...");
		fieldsetImegenAge.setBorder(border);
		fieldsetImegenAge.setBounds(10, 10, 560, 190);
		
		try(InputStream inputStream = getClass().getResourceAsStream("/imagens/warface-xk.png")){
			//ImageIcon imagem = new ImageIcon(UtilFile.inputStreamToByte(inputStream, 301657l));
			ImageIcon imagem = Util.redimensionaImg(UtilFile.inputStreamToByte(inputStream, 301657l), 540, 160);
			JLabel figura = new JLabel(imagem);
			figura.setBounds(1, 15, 560, 170);
			fieldsetImegenAge.add(figura);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		this.getContentPane().add(fieldsetImegenAge);
	}
	
	//Imagen do age
	private void box2(){
		final JPanel fieldsetImegenAge2 = new JPanel();
		fieldsetImegenAge2.setBackground(Color.WHITE);
		fieldsetImegenAge2.setLayout(null);
		fieldsetImegenAge2.setBounds(10, 240, 560, 190);
		Border border = BorderFactory.createTitledBorder("Dados para Inicilizacao...");
		fieldsetImegenAge2.setBorder(border);
		
		startProgress(fieldsetImegenAge2);
		
		JLabel email = new JLabel("Email");
		email.setBounds(100, 15, 100, 100);
		fieldsetImegenAge2.add(email);
		
		final JTextField inputEmail = new JTextField("");
		inputEmail.setBounds(135, 50, 196, 30);
		fieldsetImegenAge2.add(inputEmail);
		
		JLabel serial = new JLabel("Senha");
		serial.setBounds(100, 45, 100, 100);
		fieldsetImegenAge2.add(serial);
		
		final JPasswordField inputSerial = new JPasswordField("");
		inputSerial.setBounds(135, 85, 196, 30);
		fieldsetImegenAge2.add(inputSerial);
		
		final JLabel validadoComSucesso = new JLabel("");
		validadoComSucesso.setBounds(140, 25, 400, 32);
		validadoComSucesso.setFont(new Font("",Font.BOLD, 14));
		validadoComSucesso.setVisible(false);
		fieldsetImegenAge2.add(validadoComSucesso);
		
		final JButton validador = new JButton("Validar Senha");
		validador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(camposIsNotNull(inputEmail, inputSerial)){
					@SuppressWarnings("deprecation")
					String senha = inputSerial.getText();
					String email = inputEmail.getText();
					
					JogadorWarface jogadorWarface = new XitersDAOImpl().autenticar(email, senha);
					if(jogadorWarface != null && jogadorWarface.getId() > 0){
						if(!new File(LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT).exists()){
							if(new File(LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT).mkdir()){
								jogadorWarface.setCodigoSala(CODIGO_SALA);
								Util.JogadorWarfaceCreateLocalData(jogadorWarface);
								
								validadoComSucesso.setText(String.format("Validado com sucesso - Bem vindo %s", 
										Util.getJogadorWarface().getNome()));
								validadoComSucesso.setVisible(true);
							}else{
								JOptionPane.showConfirmDialog(null, "Sistem anao consegiu criar Diretorio");
								System.exit(0);
							}
						}else{
							jogadorWarface.setCodigoSala(CODIGO_SALA);
							Util.JogadorWarfaceCreateLocalData(jogadorWarface);
							
							validadoComSucesso.setText(String.format("Validado com sucesso - Bem vindo %s", 
									Util.getJogadorWarface().getNome()));
							validadoComSucesso.setVisible(true);
						}
					}else{
						inputSerial.setBackground(Color.ORANGE);
						inputSerial.setText("Email Ou Senha nao existe..");
						
						inputEmail.setBackground(Color.ORANGE);
						inputEmail.setText("Email Ou Senha nao existe..");
					}
				}
			}
		});
		validador.setBounds(330, 50, 130, 65);
		validador.setIcon(LayoutGenericXK.getImageIconXK());
		fieldsetImegenAge2.add(validador);
		
		final JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		cancelar.setBounds(290, 145, 130, 35);	
		cancelar.setIcon(LayoutGenericXK.getImageIconXK());
		fieldsetImegenAge2.add(cancelar);
		
		final JButton finalizar = new JButton("Finalizar");
		finalizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Util.getJogadorWarface() != null && Util.getJogadorWarface().getId() > 0){
					new Thread(new Runnable() {
						@Override
						public void run() {
							finalizar.setVisible(false);
							cancelar.setVisible(false);
							validador.setVisible(false);
							startProgress(fieldsetImegenAge2);
							progressBar.setVisible(true);
							
							try{
								String typeSo = System.getProperty("os.arch");
								if(typeSo.equals("amd64")){
									System.out.println("Sistema 64");
									LoginHelper.createPrunsrvSO64();
								}else{
									System.out.println("Sistema 32");
									LoginHelper.createPrunsrvSO32();
								}
								progressBar.setValue(30);
								Util.timeOut(1000);
								
								LoginHelper.createTaskStartJarAndUtis();
								progressBar.setValue(60);
								Util.timeOut(1000);
								
								LoginHelper.updatePathInInstallBat();
								progressBar.setValue(90);
								Util.timeOut(1000);
								
								LoginHelper.installServicoExtremeAntCheat();
								progressBar.setValue(100);
								Util.timeOut(1000);
								
								LayoutGenericXK.mesageDialogIcoInfo("Instalacao Finalizada", 
										"Sua instalacao foi concluida com exito", LayoutGenericXK.PATH_ICO_XK_INFO_100_67);
				            	System.exit(0);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}).start();
				}else{
					JOptionPane.showMessageDialog(null, "Valide seus dados primeiro...");
				}
			}
		});
		finalizar.setBounds(420, 145, 130, 35);	
		finalizar.setIcon(LayoutGenericXK.getImageIconXK());
		fieldsetImegenAge2.add(finalizar);
		
		this.getContentPane().add(fieldsetImegenAge2);
	}
	
	private boolean camposIsNotNull(JTextField inputEmail, JPasswordField inputSerial){
		@SuppressWarnings("deprecation")
		String senha = inputSerial.getText();
		String email = inputEmail.getText();
		boolean isNotEmpty = true;
		
		if(email == null || email.isEmpty()){
			inputEmail.setBackground(Color.red);
			inputEmail.setText("Campo Vazio");
			isNotEmpty = false;
		}
		
		if((senha == null || senha.isEmpty())){
			inputSerial.setBackground(Color.red);
			inputSerial.setText("Campo Vazio");
			isNotEmpty = false;
		}
		
		return isNotEmpty;
	}
	
	private void startProgress(JPanel fieldsetImegenAge2){
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 145, 540, 35);
		progressBar.setMinimum(0); 
		progressBar.setMaximum(100); 
		progressBar.setStringPainted(true); 
		
		progressBar.setValue(0);
		progressBar.setVisible(false);
		fieldsetImegenAge2.add(progressBar);
	}
	
	private void windwosAguarde(){
		aguardeWindows.setTitle("Aguarde .......");
		aguardeWindows.setLayout(null);
		aguardeWindows.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));
		aguardeWindows.setBackground(Color.WHITE);
		aguardeWindows.setSize(300, 150);
		aguardeWindows.setLocationRelativeTo(null);
		aguardeWindows.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel instal = new JLabel("Instalando Servico ......");
		instal.setBounds(50,10,300,100);
		instal.setFont(new Font("", Font.BOLD,15));
		aguardeWindows.add(instal);
	}
	
}
