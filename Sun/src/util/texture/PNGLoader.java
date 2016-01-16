package util.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class PNGLoader {

	public static Texture2D load(String filename) throws IOException {
		return load(filename, GL11.GL_TEXTURE_2D);
	}
	public static Texture2D load(String filename, int target) throws IOException {
		
		BufferedImage img = null;
		img = ImageIO.read(new File(filename));
		
		ByteBuffer buffer = getBuffer(img);
		
		Texture2D tex = new Texture2D(img.getWidth(), img.getHeight(), buffer);
		
		return tex;
	}
	
	public static ByteBuffer getBuffer(BufferedImage img) throws IOException {
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);

		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				
				int rgb = img.getRGB(x, y);

				buffer.put((byte) ((rgb >> 16) & 0xFF));
				buffer.put((byte) ((rgb >> 8) & 0xFF));
				buffer.put((byte) (rgb & 0xFF));
				buffer.put((byte) ((rgb >> 24) & 0xFF)); 
				
			}
		}
		buffer.flip();
		
		return buffer;
	}

}
