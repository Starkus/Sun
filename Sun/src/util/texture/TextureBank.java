package util.texture;

import java.util.ArrayList;

public class TextureBank {

	private static final ArrayList<Texture> bank = new ArrayList<Texture>();
	
	public static void storeTexture(Texture tex) {
		bank.add(tex);
	}
	
	public static void free() {
		for (Texture tex : bank) {
			tex.free();
		}
	}

}
