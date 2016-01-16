package view.label;


import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.mariani.vector.Vec2;
import org.mariani.vector.Vec3;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import event.EventListener;
import theme.Theme;


public class InputLabel extends Label {

	public InputLabel(String id, Vec2 pos, Vec2 size) {
		super(id, pos, size);
		hasBackground = true;
	}
	public InputLabel(String id, float x, float y, float sx, float sy) {
		super(id, x, y, sx, sy);
		hasBackground = true;
	}
	public InputLabel(String id, float x, float y, Vec2 size) {
		super(id, x, y, size);
		hasBackground = true;
	}
	public InputLabel(String id, Vec2 pos, float sx, float sy) {
		super(id, pos, sx, sy);
		hasBackground = true;
	}
	
	
	private boolean focus = false;
	
	protected Vec3 color = Theme.current.media_color_2;
	public Vec3 focus_color = Theme.current.media_color;
	protected Vec3 hover_color = Theme.current.hover_color;
	
	EventListener onValueReturnListener;
	
	char last_char;
	
	
	@Override
	public void update() {
		
		super.update();
		

		boolean lmb = Mouse.isButtonDown(0);
		
		if (isMouseOver() && lmb) {
			focus = true;
			while (Keyboard.next());
		}
		else if (lmb)
			focus = false;
		
		
		if (focus)
			readKeyboard();
	}
	
	private void readKeyboard() {
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				char c = Keyboard.getEventCharacter();
				
				if (c == 8) {
					try {
						text = text.substring(0, text.length() - 1);
					} catch (StringIndexOutOfBoundsException e){}
				} else if (c == 13) {
					focus = false;
					onValueReturn();
				} else {
					if ((c >= 32 && c <= 125) || c == '\r')
						text += String.valueOf(c);
				}
			}
		}
	}
	
	
	@Override
	public void render() {
		
		
		Vec2 pos = positionInScreen();
		Vec2 size = getSize();

		
		boolean textcursor = System.currentTimeMillis()%1000 < 500;
		if (textcursor && focus) text += "_";
		

		
		if (focus)
			back_color = focus_color;
		else
			back_color = color;
		
		super.render();
		
		
		if (isMouseOver()) {
			
			int margin = 4;
			
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			glBegin(GL_QUADS);
				
				glColor3f(hover_color.x, hover_color.y, hover_color.z);

				glVertex2f(pos.x - margin,			pos.y - margin);
				glVertex2f(pos.x + size.x + margin,	pos.y - margin);
				glVertex2f(pos.x + size.x + margin,	pos.y + size.y + margin);
				glVertex2f(pos.x - margin,			pos.y + size.y + margin);
				
			glEnd();
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
		
		
		if (textcursor && focus) text = text.substring(0, text.length()-1);
	}
	
	
	public void setOnValueReturnListener(EventListener e) {
		onValueReturnListener = e;
	}
	public EventListener getOnValueReturnListener() {
		return onValueReturnListener;
	}
	
	void onValueReturn() {
		if (onValueReturnListener != null)
			onValueReturnListener.onAction();
	}
	
}
