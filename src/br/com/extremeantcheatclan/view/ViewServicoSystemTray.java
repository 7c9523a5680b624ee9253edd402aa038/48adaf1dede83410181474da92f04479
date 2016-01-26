package br.com.extremeantcheatclan.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;

import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;

public class ViewServicoSystemTray extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public ViewServicoSystemTray(boolean isAtivoSystem) {
		Util.themaNimbus();
		this.setTitle("Servico Extrem ant cheat");
		this.setLayout(null);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));  
		this.setSize(300, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setBackground(Color.white);
		
		if(isAtivoSystem){
			JLabel titulo = new JLabel("Servico esta Ativo", LayoutGenericXK.getImageIconXK(), 0);
			titulo.setBounds(50, 50, 200, 100);
			titulo.setFont(new Font("Dialog", Font.BOLD, 20));
			this.add(titulo);
		}else{
			JLabel titulo = new JLabel("Servico Inativo!!", LayoutGenericXK.getImageIconXK(), 0);
			titulo.setBounds(50, 50, 200, 100);
			titulo.setFont(new Font("Dialog", Font.BOLD, 20));
			this.add(titulo);
		}
		
		this.setVisible(true);
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