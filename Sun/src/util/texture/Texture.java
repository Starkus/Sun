package util.texture;

import static org.lwjgl.opengl.GL11.glGenTextures;

import org.lwjgl.opengl.GL11;

public abstract class Texture {
	
	int ID;
	int minFilter, magFilter;
	
	private int width, height;


	public Texture(int w, int h) {
		ID = glGenTextures();
		
		width = w;
		height = h;
		
		TextureBank.storeTexture(this);
	}
	
	
	public void free() {
		GL11.glDeleteTextures(ID);
	}
	
	
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public int getID() {
		return ID;
	}
}
