package view;

import org.mariani.vector.Vec2;

import static org.lwjgl.opengl.GL11.*;

public class Quad extends View {
	
	
	public Quad(String id, Vec2 pos, Vec2 size) { super(id, pos, size); }
	public Quad(String id, float x, float y, float sx, float sy) { super(id, x, y, sx, sy); }
	public Quad(String id, float x, float y, Vec2 size) { super(id, x, y, size); }
	public Quad(String id, Vec2 pos, float sx, float sy) { super(id, pos, sx, sy); }

	
	public void update() {}
	public void render() {
		
		Vec2 pos = positionInScreen();
		
		glBegin(GL_QUADS);
			
			glColor3f(color.x, color.y, color.z);

			glVertex2f(pos.x,				pos.y);
			glVertex2f(pos.x + getSize().x,	pos.y);
			glVertex2f(pos.x + getSize().x,	pos.y + getSize().y);
			glVertex2f(pos.x,				pos.y + getSize().y);
			
		glEnd();
	}

}
