package client;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import util.texture.TextureBank;

public abstract class Client {
	
	public static Client current;
	
	
	boolean quit_request = false;
	private long start_time;
	
	public String console = "";
	
	int width, height;
	
	public Client(int width, int height) {

		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Sun");
			Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		current = this;
		start_time = System.currentTimeMillis();

		
		Keyboard.enableRepeatEvents(true);
		
		init();
		while(!Display.isCloseRequested() && !quit_request) {
			loop();
			Display.update();
		}
		TextureBank.free();
		Display.destroy();
	}
	
	public void requestQuit() {
		quit_request = true;
	}
	
	public long startTime() {
		return start_time;
	}
	
	abstract void init();
	abstract void loop();
	
	public int displayWidth() {
		return width;
	}
	public int displayHeight() {
		return height;
	}
}
