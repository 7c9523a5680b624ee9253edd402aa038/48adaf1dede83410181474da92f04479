package br.com.extremeantcheatclan.layout;

import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import br.com.extremeantcheatclan.util.Util;

public class LayoutGenericXK {

	public static final String[] PATH_ICO_XK = {"/imagens/extreme-ant-cheat-ico.png","1033"};
	public static final String[] PATH_ICO_XK_45_64 = {"/imagens/extreme-ant-cheat-ico-45-64.png","6827"};
	public static final String[] PATH_ICO_XK_INFO_100_67 = {"/imagens/warfaceInfo.png","15793"}; 
	public static final String[] PATH_ICO_XK_116_166 = {"/imagens/extreme-ant-cheat-116x166.png", "30592"};
	public static final String[] PATH_ICO_XK_22_31 = {"/imagens/extreme-ant-cheat-ico-22-31.png", "2123"};
	
	public static ImageIcon getImageIconXK(){
		try(InputStream ico = LayoutGenericXK.class.getResourceAsStream(PATH_ICO_XK[0])){
			return new ImageIcon(Util.inputStreamToByte(ico, Long.valueOf(PATH_ICO_XK[1])));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ImageIcon getImageIconXKInfo(String[] path){
		try(InputStream ico = LayoutGenericXK.class.getResourceAsStream(path[0])){
			return new ImageIcon(Util.inputStreamToByte(ico, Long.valueOf(path[1])));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Integer confirmDialogIcoInfo(String titulo, String descricao, String[] path){
		Icon icon = LayoutGenericXK.getImageIconXKInfo(path);
		return JOptionPane.showConfirmDialog(null, descricao, titulo, 0, JOptionPane.INFORMATION_MESSAGE, icon);
	}
	
	public static void mesageDialogIcoInfo(String titulo, String descricao, String[] path){
		Icon icon = LayoutGenericXK.getImageIconXKInfo(path);
		JOptionPane.showMessageDialog(null, descricao, titulo, JOptionPane.INFORMATION_MESSAGE, icon);
	}
	
}