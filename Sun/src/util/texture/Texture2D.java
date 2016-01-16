package util.texture;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

public class Texture2D extends Texture {
	
	private ByteBuffer data;
	

	public Texture2D(int w, int h, ByteBuffer buffer) {
		super(w, h);
		
		minFilter = magFilter = GL_LINEAR;
		
		data = buffer;
	}
	
	
	public void load() {
		
		glBindTexture(GL_TEXTURE_2D, ID);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, getWidth(), getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
	}
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, ID);
	}
	
	
	public void setFilter(int min, int mag) {
		minFilter = min;
		magFilter = mag;
	}

}
