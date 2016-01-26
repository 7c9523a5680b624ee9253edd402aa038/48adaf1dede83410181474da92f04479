package br.com.extremeantcheatclan.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;

import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.macro.Razer;
import br.com.extremeantcheatclan.util.Util;

public class RazerView extends JFrame{

	public static void main(String[] args) {
		new RazerView();
	}
	
	private static final long serialVersionUID = 1L;
	
	public RazerView() {
		Util.themaNimbus();
		this.setTitle("Verificador Razer");
		this.setSize(700, 150);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));

		JLabel titulo = new JLabel("Foi indetifica RzSynapse em sua maquina");
		titulo.setBounds(175, 10, 400, 30);
		titulo.setForeground(new Color(0, 255, 0));
		titulo.setFont(new Font("", Font.BOLD, 14));
		this.getContentPane().add(titulo);
		
		JLabel descricao = new JLabel("Sera iniciado processo de verifica.");
		descricao.setBounds(210, 30, 400, 30);
		descricao.setForeground(new Color(0, 255, 0));
		descricao.setFont(new Font("", Font.BOLD, 12));
		this.getContentPane().add(descricao);
		
		JLabel aviso = new JLabel("Qual quer tipo de fraude ou ate mesmo desligamento forçado "
				+ "do\n computador o player sera banido imediatamente");
		aviso.setBounds(18, 60, 660, 50);
		aviso.setForeground(new Color(0, 255, 0));
		aviso.setFont(new Font("", Font.BOLD, 12));
		this.getContentPane().add(aviso);
		
		JLabel iconeEsquerdo = new JLabel(LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK_22_31));
		iconeEsquerdo.setBounds(130, 8, 50, 40);
		this.getContentPane().add(iconeEsquerdo);
		
		this.startCheckMacro();
		
		this.getContentPane().setBackground(Color.BLACK);
		this.setVisible(true);
	}
	
	private void startCheckMacro(){
		final JFrame viewAtual = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Util.timeOut(3000);
				
				new Razer(viewAtual, frameSuscess(), frameMacroDetect());
			}
		}).start();
	}
	
	public JFrame frameSuscess(){
		JFrame frameSuscess = new JFrame();
		Util.themaNimbus();
		frameSuscess.setTitle("Razer Checkado");
		frameSuscess.setSize(700, 130);
		frameSuscess.setLayout(null);
		frameSuscess.setLocationRelativeTo(null);
		frameSuscess.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameSuscess.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));

		JLabel titulo = new JLabel("Computador limpo");
		titulo.setBounds(289, 10, 400, 30);
		titulo.setForeground(new Color(0, 255, 0));
		titulo.setFont(new Font("", Font.BOLD, 14));
		frameSuscess.getContentPane().add(titulo);
		
		JLabel descricao = new JLabel("Obrigado pela atencao");
		descricao.setBounds(290, 30, 400, 30);
		descricao.setForeground(new Color(0, 255, 0));
		descricao.setFont(new Font("", Font.BOLD, 12));
		frameSuscess.getContentPane().add(descricao);
		
		frameSuscess.getContentPane().setBackground(Color.BLACK);
		return frameSuscess;
	}
	
	public JFrame frameMacroDetect(){
		JFrame frameMacroDetect = new JFrame();
		Util.themaNimbus();
		frameMacroDetect.setTitle("Razer Cheackdo");
		frameMacroDetect.setSize(700, 130);
		frameMacroDetect.setLayout(null);
		frameMacroDetect.setLocationRelativeTo(null);
		frameMacroDetect.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameMacroDetect.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));

		JLabel titulo = new JLabel("Macro Detectado");
		titulo.setBounds(289, 10, 400, 30);
		titulo.setForeground(new Color(0, 255, 0));
		titulo.setFont(new Font("", Font.BOLD, 14));
		frameMacroDetect.getContentPane().add(titulo);
		
		JLabel descricao = new JLabel("Usuario banido");
		descricao.setBounds(310, 25, 400, 30);
		descricao.setForeground(new Color(0, 255, 0));
		descricao.setFont(new Font("", Font.BOLD, 12));
		frameMacroDetect.getContentPane().add(descricao);
		
		JLabel iconeEsquerdo = new JLabel(LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK_22_31));
		iconeEsquerdo.setBounds(230, 8, 50, 40);
		frameMacroDetect.add(iconeEsquerdo);
		
		frameMacroDetect.getContentPane().setBackground(Color.BLACK);
		return frameMacroDetect;
	}
	
	private byte[] getImageIco(){
		try(InputStream in = Login.class.getResourceAsStream(LayoutGenericXK.PATH_ICO_XK[0])){
			return LoginHelper.inputStreamToByte(in, Long.valueOf(LayoutGenericXK.PATH_ICO_XK[1]));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}