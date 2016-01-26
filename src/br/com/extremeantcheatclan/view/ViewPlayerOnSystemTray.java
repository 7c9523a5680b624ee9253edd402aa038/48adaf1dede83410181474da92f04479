package br.com.extremeantcheatclan.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.PlayersOn;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;

public class ViewPlayerOnSystemTray extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static boolean WAITING = true;
	private List<PlayersOn> playersOns;
	private JTable table;
	
	public ViewPlayerOnSystemTray() {
		this.setTitle("Players Online");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		this.setSize(350,500);
		this.setLocation(600,300);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getImageIco()));  
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.GRAY);
		Util.themaNimbus();
		
		this.createPainelStatus();
		this.createPainelListaPalyersOn();
		
		this.setVisible(true);
		
		this.startThreadCheckPlayer();
		this.addWindowListener(new WindowAdapter() {  
		    public void windowClosing(WindowEvent evt) { 
		    	try {
		    		WAITING = false;
		    		System.out.println("Chamo esse caraio ");
		    	} catch (Exception e) {
					e.printStackTrace();
				}
		    }  
		}); 
	}
	
	private void createPainelStatus(){
		JPanel painelStatus = new JPanel();
		painelStatus.setLayout(null);
		painelStatus.setBounds(10, 10, 315, 130);
		painelStatus.setBorder(BorderFactory.createTitledBorder("Busca manual de players-on no ant cheat"));
		
		JLabel nick = new JLabel("Buscar por Nick - (Digite ate os 3 primerios digito)");
		nick.setBounds(20, 25, 280, 50);
		painelStatus.add(nick);
		
		final JTextField inputNick = new JTextField("");
		inputNick.setBounds(20, 58, 280, 30);
		painelStatus.add(inputNick);
		
		JButton buscar = new JButton("Buscar");
		buscar.setBounds(20, 90, 280, 30);
		buscar.setIcon(LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK));
		buscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean encontrado = false;
				for (PlayersOn playersOn : playersOns) {
					String nickPlayer = playersOn.getNick().length() >= 3 ? 
							playersOn.getNick().substring(0,3) : playersOn.getNick();
					String inputPlayer = inputNick.getText().length() >= 3 ? 
							inputNick.getText().substring(0,3) : inputNick.getText();
				
					if(nickPlayer.equals(inputPlayer)){
						encontrado = true;
						JOptionPane.showMessageDialog(null, "Player " + playersOn.getNick() + " encontrato..");
						break;
					}
				}
				if(encontrado){
					JOptionPane.showMessageDialog(null, "Player " + inputNick.getText() + " n�o encontrado");
				}
			}
		});
		painelStatus.add(buscar);
		
		this.add(painelStatus);
	}
	
	private void createPainelListaPalyersOn(){
		JPanel painelListaPlayersOn = new JPanel();
		painelListaPlayersOn.setLayout(new BorderLayout());
		painelListaPlayersOn.setBounds(10, 150, 315, 300);
		painelListaPlayersOn.setAutoscrolls(true);
		painelListaPlayersOn.setBorder(BorderFactory.createTitledBorder("Tabela de players-on no ant cheat"));
		
		Object[] colunas = new Object[] { "Nome", "Data", "Ativo" };
		Object[][] valores = null;
		try{
			valores = listaPlayers(new XitersDAOImpl().findAllPlayerOn());
		}catch(Exception e){
			System.err.println("Erro ao carregar players on \n"+e.getMessage());
			valores = new Object[0][3];
		}
		
		DefaultTableModel modelo = new DefaultTableModel(valores,colunas);
		table = new JTable(modelo);
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(80); 
		table.getColumnModel().getColumn(2).setPreferredWidth(10);
	    JScrollPane scrollPane = new JScrollPane(table);
	    painelListaPlayersOn.add(scrollPane, BorderLayout.CENTER);
		
		this.add(painelListaPlayersOn);
	}
	
	private Object[][] listaPlayers(List<PlayersOn> players){
		this.playersOns = players;
		Object[][] dados = new Object[players.size()][3];
		int posicao = 0;
		for(PlayersOn player : players){
			dados[posicao][0] = player.getNick();
			dados[posicao][1] = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(player.getDataInicio());
			dados[posicao][2] = "ATIVO";
			posicao++;
		}
		return dados;
	}

	private void startThreadCheckPlayer(){
		Thread checkPlayer = new Thread(new Runnable() {
			@Override
			public void run() {
				while(WAITING){
					Util.timeOut(4000);
					try{
						int playersOn = new XitersDAOImpl().getNumeroPlayerOn(); 
						DefaultTableModel model = (DefaultTableModel) table.getModel();  
						if(playersOn != model.getRowCount()){
							int numeroPlayersOn = model.getRowCount() - 1;
							for (int i = numeroPlayersOn; i >=  0; i--) {
								model.removeRow(i);
							}
							Object[] colunas = new Object[] { "Nome", "Data", "Ativo" };
							Object[][] valores = null;
							
							valores = listaPlayers(new XitersDAOImpl().findAllPlayerOn());
							model.setDataVector(valores, colunas);
						}	
					}catch(Exception e){
						System.err.println("Erro ao carregar players on \n"+e.getMessage());
					}
				}
			}
		});	
		checkPlayer.setName("Check Player");
		checkPlayer.start();
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