package view.button;

import org.mariani.vector.Vec2;
import org.mariani.vector.Vec3;

import util.EnumState;
import view.Quad;

import static org.lwjgl.opengl.GL11.*;
import static util.EnumState.*;

import org.lwjgl.input.Mouse;

import event.EventListener;
import theme.Theme;

public class Button extends Quad {
	
	
	private EnumState state = NORMAL;
	private Vec3 final_color;
	
	protected Vec3 hover_color = Theme.current.hover_color;
	protected Vec3 pressed_color = Theme.current.pressed_color;
	
	EventListener onReleaseListener;
	

	public Button(String id, Vec2 pos, Vec2 size) {
		super(id, pos, size);
	}
	public Button(String id, float x, float y, float sx, float sy) {
		super(id, x, y, sx, sy);
	}
	public Button(String id, float x, float y, Vec2 size) {
		super(id, x, y, size);
	}
	public Button(String id, Vec2 pos, float sx, float sy) {
		super(id, pos, sx, sy);
	}
	
	
	public void update() {
		
		boolean lmb = Mouse.isButtonDown(0);
		
		if (state == HOVERING && !isMouseOver())
			state = NORMAL;
		
		else if (state == PRESSED && !lmb)
			if (isMouseOver())
				state = RELEASED;
			else
				state = NORMAL;
		
		else if (isMouseOver()) {
			if (lmb)
				state = PRESSED;
			else
				state = HOVERING;
		}
		
		if (state == PRESSED)
			final_color = pressed_color;
		else if (state == RELEASED) {
			final_color = color;
			onRelease();
		}
		else
			final_color = color;
	}
	
	public void render() {
		
		Vec2 pos = positionInScreen();
		
		Vec2 size = getSize();
		
		glBegin(GL_QUADS);
			
			glColor3f(final_color.x, final_color.y, final_color.z);

			glVertex2f(pos.x,			pos.y);
			glVertex2f(pos.x + size.x,	pos.y);
			glVertex2f(pos.x + size.x,	pos.y + size.y);
			glVertex2f(pos.x,			pos.y + size.y);
			
		glEnd();
		
		if (state == HOVERING) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			glBegin(GL_QUADS);
				
				glColor3f(hover_color.x, hover_color.y, hover_color.z);
	
				glVertex2f(pos.x,			pos.y);
				glVertex2f(pos.x + size.x,	pos.y);
				glVertex2f(pos.x + size.x,	pos.y + size.y);
				glVertex2f(pos.x,			pos.y + size.y);
				
			glEnd();
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
	}
	
	
	public void setOnReleaseListener(EventListener e) {
		onReleaseListener = e;
	}
	
	
	public void onRelease() {
		if (onReleaseListener != null)
			onReleaseListener.onAction();
	}

}
