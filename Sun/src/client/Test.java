package client;


import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import activity.ActivityManager;
import activity.MainActivity;
import theme.Theme;
import util.texture.PNGLoader;

import static org.lwjgl.opengl.GL11.*;

import org.mariani.vector.Vec3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;


public class Test extends Client{
	
	
	private final static int WIDTH = 500;
	private final static int HEIGHT = 650;

	public Test() {
		super(WIDTH, HEIGHT);
	}
	
	void init() {
		
		try {
			ByteBuffer[] iconlist = new ByteBuffer[2];
			BufferedImage icon16 = ImageIO.read(new File("icon/icon_16.png"));
			iconlist[0] = PNGLoader.getBuffer(icon16);
			BufferedImage icon32 = ImageIO.read(new File("icon/icon_32.png"));
			iconlist[1] = PNGLoader.getBuffer(icon32);
			Display.setIcon(iconlist);
		} catch (IOException e) {
			System.err.println("Couldn't load icon");
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluOrtho2D(0, WIDTH, 0, HEIGHT);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glAlphaFunc(GL_GREATER, 0.6F);

		
		Theme darkblue = new Theme();
		darkblue.media_color = new Vec3(0.2f, 0.4f, 0.7f);
		darkblue.media_color_2 = new Vec3(0.1f, 0.2f, 0.4f);
		darkblue.back_color = new Vec3(0f, 0.05f, 0.15f);
		darkblue.hover_color = new Vec3(0.4f, 0.7f, 1f);
		darkblue.pressed_color = new Vec3(0.1f, 0.2f, 0.4f);
		
		Theme white = new Theme();
		white.media_color = new Vec3(0.8f, 0.8f, 0.8f);
		white.media_color_2 = new Vec3(0.6f, 0.6f, 0.6f);
		white.back_color = new Vec3(1f, 1f, 1f);
		white.hover_color = new Vec3(0f, 0f, 0f);
		white.pressed_color = new Vec3(0.5f, 0.5f, 0.5f);
		
		Theme.current = white;
		
		
		Theme t = Theme.current;
		
		new MainActivity("MainActivity");

		glClearColor(t.back_color.x, t.back_color.y, t.back_color.z, 0f);
	}
	
	void loop() {
		
		width = Math.max(200, Display.getWidth());
		height = Math.max(250, Display.getHeight());
		
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluOrtho2D(0, width, 0, height);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
		
		ActivityManager.update();
		ActivityManager.render();
		
	}

}
