package br.com.extremeantcheatclan.leitormacro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.macro.Razer;
import br.com.extremeantcheatclan.util.Util;

public class LeitorImagemMacroRazer {

	public BufferedImage buffImage;
	private BufferedImage keyTokenBuffImage;
	public Object[] screenShotCompleto = new Object[3];
	public byte[] palvraMacroImg = null;
	public byte[] letraMacroImg = null;
	public byte[] configuracaoMacroImg = null;
	private boolean devOrProd = false;
	private static boolean razerSnapseEncontrado = false;
	private static boolean razerThreadStop = false;
	
	private static final int WIDTH_WINDONWS_RAZER = 800;
	private static final int HEIGTH_WINDONWS_RAZER = 600;
	public static String larguraAlturaToWindowRazer = "";
	
	public LeitorImagemMacroRazer(int altura, int largura) {
		razerThreadStop =  false;
		razerSnapseEncontrado = false;
		this.recorteMacro(altura, largura);
	}
	
	public LeitorImagemMacroRazer(String src) throws IOException {
		File fileImage = new File(src);
		if(fileImage.exists()){
			try {
				this.buffImage = ImageIO.read(fileImage);
				InputStream imgStream = LeitorImagemMacroRazer.class.getResourceAsStream("/imagens/key-token-img.png");
				this.keyTokenBuffImage = ImageIO.read(imgStream);
				
				final int heigthMax = this.buffImage.getHeight();
				this.speedPercorrendoWidthBufferImg(heigthMax);
				
			} catch (IOException e) {
				throw new IOException("Erro ao ler imagem  - "+e.getMessage());
			}
		}
	}

	public LeitorImagemMacroRazer(InputStream screenshotTelaAtual) {
		try{
			this.buffImage = ImageIO.read(screenshotTelaAtual);
			InputStream imgStream = LeitorImagemMacroRazer.class.getResourceAsStream("/imagens/key-token-img.png");
			this.keyTokenBuffImage = ImageIO.read(imgStream);
			
			int heigthMax = this.buffImage.getHeight();
			this.speedPercorrendoWidthBufferImg(heigthMax);			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void percorrendoWidthBufferImg(int altura,int heightMax, int heightMin) {
		int larguraFor = 30;
		
		for (int j = heightMin;j < heightMax; j++){
			for (int x = 0; x < this.buffImage.getWidth(); x++){
				if(x == larguraFor && x < 1320-10){
					larguraFor = larguraFor + 30;
					
					try{
						BufferedImage subimage = this.buffImage.getSubimage(x,altura, 45,50);
						if(devOrProd){
							ImageIO.write(subimage, "PNG", new File("testandoNovo.png"));
						}
						
						//this.bufferedImagesEqual(this.keyTokenBuffImage, subimage)
						if(this.isEqualsImg(this.keyTokenBuffImage, subimage)){
							razerSnapseEncontrado = true;
							this.recorteMacro(altura, x);
							return;
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					if(x >= 1320){
						System.out.println("fim da leitura");
						break;
					}
				}
			}
		}
	}
	
	private void recorteMacro(int posicaoAltura,int posicaoLargura){
		if(!razerThreadStop){
			razerThreadStop = true;
			
			larguraAlturaToWindowRazer = posicaoLargura +"&"+posicaoAltura;
			
			//Coletando provas 00
			this.buffImage = this.reprinteRazer(posicaoAltura, posicaoLargura);
			screenShotCompleto[0] = this.writeImage(this.buffImage, 000);
			Razer.screenShotsList.add((byte[])screenShotCompleto[0]);
			
			BufferedImage bufferImgRecortada = this.recortaUnPedacoDaImagemMacro();
			
			palvraMacroImg = this.recortaSomenteAPalavraMacro(bufferImgRecortada);
			letraMacroImg = this.recortaSomenteALetraM(bufferImgRecortada);
			
			if(razerSnapseEncontrado){
				this.robotCliclMacro(posicaoAltura, posicaoLargura);
			}
			
			//Esse segundo reprinte e da aba macros
			this.buffImage = this.reprinteRazer(posicaoAltura, posicaoLargura);
			
			//Coletando provas 01
			byte[] screenShot = this.writeImage(this.buffImage, 000);
			Razer.screenShotsList.add(screenShot);
			screenShotCompleto[1] = screenShot;
			
			//As provas sao recolhidas somente depois da configuracoes
			//Caso contrario o servico da thread seria atropelada pelo fluxo do Razer.java
			configuracaoMacroImg = this.recortaConfiguracaoMacro();

			//Coletando provas 02
			screenShot = this.writeImage(this.buffImage, 000);
			Razer.screenShotsList.add(screenShot);
			screenShotCompleto[2] = screenShot;
		}
	}
	
	private void robotCliclMacro(int posicaoAltura,int posicaoLargura) {
		BufferedImage macroClickImg = null;
		try(ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(letraMacroImg)){
			macroClickImg = ImageIO.read(arrayInputStream); 
			
			int x = posicaoLargura+macroClickImg.getWidth()+170;
			int y = posicaoAltura+macroClickImg.getHeight() + 5;
			
			Robot robot = new Robot();
			robot.mouseMove(x, y);
		
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.delay(1000);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.delay(500);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			
			System.out.println("Robo Executado - Clique na aba macro.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BufferedImage reprinteRazer(int posicaoAltura,int posicaoLargura){
		try {
			Util.timeOut(500);
			BufferedImage repaintScreenCaptureRazer = new Robot().createScreenCapture(
					new Rectangle(posicaoLargura, posicaoAltura, WIDTH_WINDONWS_RAZER, HEIGTH_WINDONWS_RAZER));
			
			return repaintScreenCaptureRazer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private BufferedImage recortaUnPedacoDaImagemMacro(){
		BufferedImage bufferImgRecortada = new BufferedImage(600, 400, this.buffImage.getType());
		Graphics2D graphucs2D = (Graphics2D) bufferImgRecortada.getGraphics().create();
		
		int valueDefaultDrawImage = 0;
		
		graphucs2D.drawImage(this.buffImage, 0, 0, bufferImgRecortada.getHeight(),
			bufferImgRecortada.getWidth(), 0, 0, valueDefaultDrawImage + 
		    bufferImgRecortada.getHeight(), valueDefaultDrawImage + bufferImgRecortada.getWidth(), null);
		graphucs2D.dispose();
		
		this.writeImage(bufferImgRecortada, 0);
		return bufferImgRecortada;
	}
	
	private byte[] recortaSomenteAPalavraMacro(BufferedImage bufferImgRecortada){
		// pegando somente a palavra
		BufferedImage bufferedImage = new BufferedImage(210, 60, bufferImgRecortada.getType());
		
		Graphics2D graphucs2D = (Graphics2D) bufferedImage.getGraphics().create();
		
		int valueDefaultDrawImage = 0;
		
		graphucs2D.drawImage(this.buffImage, 0, 0, bufferImgRecortada.getHeight(),
			bufferImgRecortada.getWidth(), 150, 40, valueDefaultDrawImage + 
		    bufferImgRecortada.getHeight(), valueDefaultDrawImage + bufferImgRecortada.getWidth(), null);
		graphucs2D.dispose();
		
		return this.writeImage(bufferedImage,1);
	}
	
	private byte[] recortaSomenteALetraM(BufferedImage bufferImgRecortada){
		// pegando somente a palavra
		BufferedImage bufferedImage = new BufferedImage(46, 60, bufferImgRecortada.getType());
		
		Graphics2D graphucs2D = (Graphics2D) bufferedImage.getGraphics().create();
		
		int valueDefaultDrawImage = 0;
		
		graphucs2D.drawImage(this.buffImage, 0, 0, bufferImgRecortada.getHeight(),
			bufferImgRecortada.getWidth(), 150, 40, valueDefaultDrawImage + 
		    bufferImgRecortada.getHeight(), valueDefaultDrawImage + bufferImgRecortada.getWidth(), null);
		graphucs2D.dispose();
		
		return this.writeImage(bufferedImage,2);
	}
	
	public byte[] recortaConfiguracaoMacro(){
		// pegando somente a palavra
		BufferedImage bufferedImageRedirecionamentoPart1 = new BufferedImage(1000, 800, this.buffImage.getType());
		int valueDefaultDrawImage = 0;
		
		Graphics2D graphucs2D = (Graphics2D) bufferedImageRedirecionamentoPart1.getGraphics().create();
		graphucs2D.drawImage(this.buffImage, 0, 0, bufferedImageRedirecionamentoPart1.getHeight(),
				bufferedImageRedirecionamentoPart1.getWidth(), 280, 90, valueDefaultDrawImage + 
				bufferedImageRedirecionamentoPart1.getHeight(), valueDefaultDrawImage + bufferedImageRedirecionamentoPart1.getWidth(), null);
		graphucs2D.dispose();
		
		//Redirecionando iamgem novamente
		BufferedImage bufferedImageRedirecionamentoPart2 = new BufferedImage(400, 400, bufferedImageRedirecionamentoPart1.getType());
		
		graphucs2D = (Graphics2D) bufferedImageRedirecionamentoPart2.getGraphics().create();
		graphucs2D.drawImage(bufferedImageRedirecionamentoPart1, 0, 0, bufferedImageRedirecionamentoPart2.getHeight(),
				bufferedImageRedirecionamentoPart2.getWidth(), 0, 120, valueDefaultDrawImage + 
				bufferedImageRedirecionamentoPart2.getHeight(), valueDefaultDrawImage + bufferedImageRedirecionamentoPart2.getWidth(), null);
		graphucs2D.dispose();
		
		return this.writeImage(bufferedImageRedirecionamentoPart2, 3);
	}
	
	public boolean virificaSeEstaNaAbaMacro() {
		try(InputStream in = LeitorImagemMacroRazer.class.getResourceAsStream("/imagens/rgb-aba-macro.bin");
				ObjectInputStream leitorObjeto = new ObjectInputStream(in)){
			InputStream byteToInputStream = Util.byteToInputStream(letraMacroImg);
			BufferedImage img = ImageIO.read(byteToInputStream); 
			
			@SuppressWarnings("unchecked")
			List<Color> rbgSAbaMacro = (List<Color>) leitorObjeto.readObject();
			boolean isAbaSelecionada = false;
			
			int count = 0;
			for (int y = 0; y < img.getHeight(); y++){
				for (int x = 0; x < img.getWidth(); x++){
					Color pixel = new Color(img.getRGB(x, y));
					if(!rbgSAbaMacro.contains(pixel)){
						count++;
					}else{
						if(pixel.getRed() == Color.WHITE.getRed()){
							isAbaSelecionada = true;
						}
					}
				}
			}
			
			return count <= 100 && isAbaSelecionada;
		}catch(Exception e){
			e.printStackTrace();  
		}
		
		return false;
	}
	
	public boolean hasWindowConfgRazerIsOpen(){
		try{
			InputStream byteToInputStream = Util.byteToInputStream(configuracaoMacroImg);
			BufferedImage img  = ImageIO.read(byteToInputStream); 
			
			List<Color> rbgSConfigurcaoRazer = new ArrayList<>();
			
			rbgSConfigurcaoRazer.add(new Color(32, 32, 32));
			rbgSConfigurcaoRazer.add(new Color(150,150,150));
			rbgSConfigurcaoRazer.add(new Color(44,44,44));
			rbgSConfigurcaoRazer.add(new Color(0,0,0));
			rbgSConfigurcaoRazer.add(new Color(12,12,12));
			rbgSConfigurcaoRazer.add(new Color(85,85,85));
			rbgSConfigurcaoRazer.add(new Color(255,255,255));
			rbgSConfigurcaoRazer.add(new Color(31,31,31));
			rbgSConfigurcaoRazer.add(new Color(30,30,30));
			rbgSConfigurcaoRazer.add(new Color(2,2,2));
			rbgSConfigurcaoRazer.add(new Color(10,10,10));

			rbgSConfigurcaoRazer.add(new Color(4,4,4));
			rbgSConfigurcaoRazer.add(new Color(7,7,7));
			rbgSConfigurcaoRazer.add(new Color(5,5,5));
			rbgSConfigurcaoRazer.add(new Color(8,8,8));
			rbgSConfigurcaoRazer.add(new Color(6,6,6));
			rbgSConfigurcaoRazer.add(new Color(3,3,3));
			rbgSConfigurcaoRazer.add(new Color(11,11,11));
			rbgSConfigurcaoRazer.add(new Color(1,1,1));
			rbgSConfigurcaoRazer.add(new Color(13,13,13));
			
			int count= 0;
			List<Color> colors = new ArrayList<>();
			for (int y = 0; y < img.getHeight(); y++){
				for (int x = 0; x < img.getWidth(); x++){
					Color pixel = new Color(img.getRGB(x, y));
					if(!rbgSConfigurcaoRazer.contains(pixel)){
						colors.add(pixel);
						count++;
					}
				}
			}
			boolean closeWindowRazer = count > 800;
			
			return closeWindowRazer ? false:true;
		}catch(Exception e){
			LayoutGenericXK.mesageDialogIcoInfo("Erro", "Ouve um erro de leitura.\n"
					+ "Entrente em contato com admin", LayoutGenericXK.PATH_ICO_XK_45_64);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean hasMacro(){
		BufferedImage img = null;
		try(ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(configuracaoMacroImg)){
			img = ImageIO.read(arrayInputStream); 
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		Color colorA = new Color(0, 255, 0);
		Color colorB = new Color(150,150,150);
		Color colorC = new Color(0,30,0);
		Color colorD = new Color(79,88,78);
		Color colorE = new Color(9,71,1);
		Color colorF = new Color(0,30,2);
		
		int a = 0,b = 0,c = 0,d = 0,e = 0,f = 0;
		for (int y = 0; y < img.getHeight(); y++){
			for (int x = 0; x < img.getWidth(); x++){
				Color pixel = new Color(img.getRGB(x, y));
				if (pixel.getRGB() == colorA.getRGB()){
					a = 1;
				}
				if(pixel.getRGB() == colorB.getRGB()){
					b = 1;
				}
				if(pixel.getRGB() == colorC.getRGB()){
					c = 1;
				}
				if(pixel.getRGB() == colorD.getRGB()){
					d = 1;
				}
				if(pixel.getRGB() == colorE.getRGB()){
					e = 1;
				}
				if(pixel.getRGB() == colorF.getRGB()){
					f = 1;
				}
			}
		}
		
		return (a + b + c + d + e + f >= 2) ? true : false;
	}
	
	public byte[] writeImage(BufferedImage bufferImg, int i){
		try {
			if(devOrProd){
				ImageIO.write(bufferImg, "PNG", new File("yourImageName"+i+".PNG"));
			}
			
			byte[] imageInByte = null;
			try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
				ImageIO.write(bufferImg, "PNG", baos);
				imageInByte = baos.toByteArray();
			}
			
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean isEqualsImg(BufferedImage bufferImg01, BufferedImage bufferImg02){
		try{
			List<Color> pixelsImg01 = new ArrayList<>();
			List<Color> pixelsImg02 = new ArrayList<>();
			
			if(bufferImg01.getWidth() == bufferImg02.getWidth() && bufferImg01.getHeight() == bufferImg02.getHeight()){
				for (int x = 0; x < bufferImg01.getWidth(); x++) {
					for (int y = 0; y < bufferImg01.getHeight(); y++) {
						Color color = new Color(bufferImg01.getRGB(x,y));
						pixelsImg01.add(color);
					}
				}
				for (int x = 0; x < bufferImg02.getWidth(); x++) {
					for (int y = 0; y < bufferImg02.getHeight(); y++) {
						Color color = new Color(bufferImg02.getRGB(x,y));
						pixelsImg02.add(color);
					}
				}
				
				for (int i = 0; i < pixelsImg02.size(); i++) {
					Color color = pixelsImg02.get(i);
					if(pixelsImg01.contains(color)){
						pixelsImg02.remove(i);
					}
				}
				
				int maxAcertoPermitido = 1500;
				Integer qtdadeAcerto = pixelsImg02.size();
				if(qtdadeAcerto <= maxAcertoPermitido){
					return true;
				}else{
					return false;
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void speedPercorrendoWidthBufferImg(final int heigthMax){
		for (int i = 0; i < 2; i++) {
			switch (i) {
			case 0:
				Thread leitor300 = new Thread(new Runnable() {
					@Override
					public void run() {
						//System.out.println("Carregando e checando imagem 0-300");
						int altura = 10;
						for (int j = 0;j < heigthMax; j++){
							if(altura >= heigthMax - 20){
								break;
							}
							
							percorrendoWidthBufferImg(altura, heigthMax, 0);
							altura = altura + 10;
							
							if(razerSnapseEncontrado){
								break;
							}
						}
						
						//Guarda o print para enviar como suspeito.
						screenShotCompleto[2] = writeImage(buffImage, 000);
					}
				});
				leitor300.setName("Carregando e checando imagem 0-300");
				leitor300.start();
				break;
			case 1:
				Thread leitor600 =	new Thread(new Runnable() {
					@Override
					public void run() {
						Util.timeOut(3500);
						System.out.println("Carregando e checando imagem 300 - " + heigthMax);
						int altura = 310;
						for (int j = 300;j < heigthMax; j++){
							if(altura >= heigthMax-20){
								break;
							}
							
							percorrendoWidthBufferImg(altura, heigthMax, 300);
							altura = altura + 10;
							
							if(razerSnapseEncontrado){
								break;
							}
						}
					}
				});
				leitor600.setName("Carregando e checando imagem 300-"+heigthMax);
				//leitor600.start();
				break;
			default:
				break;
			}
		}
		
		while(true){
			Util.timeOut(1000);
			if(configuracaoMacroImg != null || screenShotCompleto[2] != null){
				break;
			}
		}
		
	}
	
}