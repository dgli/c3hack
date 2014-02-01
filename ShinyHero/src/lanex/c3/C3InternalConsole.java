/**
 * Copyright (c) 2010 L.A.N.E.X Doujin team.
 * 
 * TH Internal Console
 * 
 * A utility providing command-line access to the game.
 * 
 * @author Daniel Gen Li
 */
package lanex.c3;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;


public class C3InternalConsole implements ComponentListener{
	/**
	 * Text field to accept commands
	 */
	private TextField cField;
	
	/**
	 * A queue to store past messages
	 */
	private ArrayBlockingQueue<String> queue;
	
	/**
	 * An instance of this class.  For static instancing.
	 */
	private static C3InternalConsole instance;
	
	/**
	 * The app to give commands to.
	 */
	private C3App app;
	
	/**
	 * The fone this console uses
	 */
	private Font font = new TrueTypeFont(
			new java.awt.Font("Courier New", java.awt.Font.PLAIN, 14), false);
	
	/**
	 * utility images
	 */
	private Image black, white;
	
	/**
	 * Not to be used.
	 * 
	 * @param container
	 * @param app
	 */
	private C3InternalConsole(GameContainer container, C3App app){
		this.app = app;
		cField = new TextField(container, font, -1, 460, container.getWidth(), container.getHeight(), this);
		
		queue = new ArrayBlockingQueue<String>(30);
		
		try{
			black = new Image("data/images/black.png");
			black.setAlpha(0.5f);
			white = new Image("data/images/white.png");
			white.setAlpha(0.5f);
		}catch(SlickException ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * returns the instance of this class.  If one already exists, 
	 * return that instance.  If not, return a new one.
	 * @param container
	 * @param app
	 * @return
	 */
	public static C3InternalConsole getInstance(GameContainer container, C3App app){
		if (instance == null){
			instance = new C3InternalConsole(container, app);
		}
		return instance;
	}
	
	/**
	 * Draws the console
	 * 
	 * @param container
	 * @param g
	 */
	public void render(GameContainer container, Graphics g){

		g.setDrawMode(Graphics.MODE_NORMAL);
		g.setAntiAlias(false);
		g.setFont(font);
		g.setColor(Color.white);
		cField.setTextColor(Color.white);
		cField.setBorderColor(Color.black);
		cField.render(container, g);

		g.drawImage(black, 0, 0, container.getWidth(), 460, 
				0, 0, 4, 4);
		g.drawImage(white, 0, 460, container.getWidth(), 462, 
				0, 0, 4, 4);
		g.drawImage(white, 0, 0, container.getWidth(), 20, 
				0, 0, 4, 4);
		
		Iterator<String> i = queue.iterator();
		int size = queue.size();
		for(int x = 0; x < size; x++){
			g.drawString(i.next(), 2,  x * 14 + 458 - size*14);
		}
		
		g.drawString("L.A.N.E.X  -  C3 HACKATHON  -  FPS: " + C3App.app.getFPS(), 5, 3);
	}

	/**
	 * Listens for actions from the text field
	 */
	@Override
	public void componentActivated(AbstractComponent source) {
		if (source == cField && !cField.getText().isEmpty()){
			cmdIn(cField.getText());
			cField.setText("");
		}
	}
	
	/**
	 * input commands here
	 * 
	 * @param s		The command
	 */
	public void cmdIn(String s){
		if (s.equals("cls")){
			queue.clear();
		}else if (s.equals("`")){ 
			app.keyPressed(-1, '`');
		}
		else{
			append("command \"" + s + "\" not found.  type \"help\" for a list of available commands");
		}
	}
	
	/**
	 * Adds a string or a series of strings to the messege queue.
	 * messages will be automaticly line-wrapped.
	 * 
	 * @param s		The messege
	 */
	public void append(String s){
		System.out.println(s);
		
		if (s.length() > 75){
			if (!queue.offer(s.substring(0, 75))){
				try{
					queue.take();
				}catch(InterruptedException ex){
					ex.printStackTrace();
				}
				queue.offer(s.substring(0, 75));
			}
			append(s.substring(75));
		}else if (!queue.offer(s)){
			try{
				queue.take();
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
			queue.offer(s);
		}
	}
	
	/**
	 * Appends a new line.
	 */
	public void newl(){
		append("");
	}

	public void setFocus(boolean focus){
		cField.setFocus(focus);
	}
}
