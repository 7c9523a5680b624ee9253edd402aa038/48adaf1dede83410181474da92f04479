package br.com.extremeantcheatclan.view;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import br.com.extremeancheatclan.socket.SocketPainel;
import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.Versao;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;

public class PainelServicoSystemTray{
	
	private static final String INFO = "	  Informacoes do servico Extreme ant cheat versao 'Service' 	\n"
			+ " O sistema extreme ant cheat versao service, servira para monitorar os jogadores do clan "
			+ "extremekillers \n		contra programas maliciosos.";
	private PopupMenu popup = new PopupMenu();
	private TrayIcon trayIcon = new TrayIcon(createImage(), "extreme ant cheat - ativo");
	private SystemTray tray = SystemTray.getSystemTray();
	public static boolean FINISH  = false;
	
	public static void main(String[] args) {
		new PainelServicoSystemTray();
		
		//Abre um servico de comunicao com task
		new SocketPainel().serviceOpenConnection();
	}
	
	public PainelServicoSystemTray() {
		Util.themaNimbus();
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		
		boolean isBanido = new XitersDAOImpl().isBanido(Util.getJogadorWarface().getNickJogo(), Util.verificaMacEnderec());
		if(isBanido){
			ImageIcon imageIconXKInfo = LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK_45_64);
			JOptionPane.showMessageDialog(null, "Seu Nick ou Endereco Mac foi banido em nosso sistema!", 
					"Banido", JOptionPane.INFORMATION_MESSAGE,imageIconXKInfo);
			System.exit(0);
		}
		
		this.checkThreadUtils();
		
		MenuItem aboutItem = new MenuItem("Sobre XK ant cheat");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame windowErro = new JFrame();
				windowErro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				windowErro.setLayout(null);
				windowErro.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));  
				windowErro.setSize(630, 200);
				windowErro.setLocationRelativeTo(null);
				// ------ fim das configuracoes de tela
 
				JLabel titulo = new JLabel("Informacoes");
				titulo.setBounds(250, 5, 500, 40);
				titulo.setFont(new Font("",Font.BOLD,15));
				windowErro.add(titulo);

				JTextArea texto = new JTextArea(INFO);
				texto.setBounds(10, 35, 590, 70);
				windowErro.add(texto);

				windowErro.setVisible(true);
			}
		});
		Menu displayMenu = new Menu("Vizualizar");
		MenuItem verPlayersOnItem = new MenuItem("players on");
		verPlayersOnItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame windowLolder = new JFrame();
				windowLolder.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				windowLolder.setLayout(null);
				windowLolder.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));  
				windowLolder.setSize(250, 100);
				windowLolder.setTitle("Carregando agurade....");
				windowLolder.setLocationRelativeTo(null);
 
				windowLolder.setVisible(true);
				JLabel carregando = new JLabel("Carregando agurade....");
				carregando.setBounds(30, 2, 590, 50);
				carregando.setFont(new Font("", Font.BOLD, 15));
				windowLolder.add(carregando);
				
				new ViewPlayerOnSystemTray();
				windowLolder.setVisible(false);
			}
		});
		MenuItem verServicoItem = new MenuItem("situacao sistema");
		verServicoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isAtivo = SocketPainel.isConect == false ? true:false;
				new ViewServicoSystemTray(isAtivo);
			}
		});
		MenuItem verErrosItem = new MenuItem("erros do sistema");
		verErrosItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame windowErro = new JFrame();
				windowErro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				windowErro.setLayout(null);
				windowErro.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));  
				windowErro.setSize(400, 350);
				windowErro.setLocationRelativeTo(null);
				// ------ fim das configuracoes de tela

				JLabel titulo = new JLabel("Expetion | Erros");
				titulo.setBounds(140, 5, 350, 30);
				windowErro.add(titulo);

				JTextArea texto = new JTextArea(getExpetion());
				texto.setBounds(10, 35, 350, 250);
				windowErro.add(texto);

				windowErro.setVisible(true);
			}
		});
		MenuItem sairItem = new MenuItem("Sair");

		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(verPlayersOnItem);
		displayMenu.add(verServicoItem);
		displayMenu.add(verErrosItem);
		popup.add(sairItem);

		trayIcon.setPopupMenu(popup);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
	}

	private void verificaAtualizacao() {
		String versaoAtual = Util.getVersaoAtualExtemeAntCheat(true);
		Versao versaoExtremeAntChet = new XitersDAOImpl().getVersaoExtremeAntChet();
		
		String versaoBD = versaoExtremeAntChet.getNovaVersao();
		if(versaoBD != null && !versaoAtual.equals(Util.parseHashMD5(versaoBD))){
			Util.openAplication(LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT+"\\"+LoginHelper.ATUALIZADOR_OF_SERVICE_JAR_AND_SEZE[0]);
			Util.timeOut(1000);
			System.exit(0);
		}
	}
	
	private String getExpetion() {
		String retorno = "";
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(
				new File(String.format("%s/logs/stderr.txt", LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT))))) {
			String linha = "";
			while ((linha = bufferedReader.readLine()) != null) {
				retorno += linha + "\n";
			}
			return retorno;
		} catch (IOException e1) {}

		return "Nem uma mensagem de erro ou exeption do servico";
	}

	private Image createImage() {
		try (InputStream imgStream = PainelServicoSystemTray.class
				.getResourceAsStream(LayoutGenericXK.PATH_ICO_XK[0])) {
			Image image = ImageIO.read(imgStream);
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private byte[] getImageIco(){
		try(InputStream in = Login.class.getResourceAsStream(LayoutGenericXK.PATH_ICO_XK[0])){
			return LoginHelper.inputStreamToByte(in, 1005l);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void checkThreadUtils(){
		Thread threadUtils = new Thread(new Runnable() {
			@Override
			public void run() {
				Util.timeOut(10000);
				while(true){
					//Verifica se o extreme ant cheat contem uma nov atualizacao
					verificaAtualizacao();
					Util.timeOut(10000);
				}
			}
		});
		threadUtils.setName("Check Update Extremes Thread");
		threadUtils.start();
	}

	public static void closePainelServico(){
		Thread closePainelServico = new Thread(new Runnable() {
			@Override
			public void run() {
				Util.timeOut(2000);
				System.exit(0);
			}
		});
		closePainelServico.setName("closePainelServico");
		closePainelServico.start();
	}
	
}