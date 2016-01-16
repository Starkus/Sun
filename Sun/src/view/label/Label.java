package view.label;


import org.mariani.vector.Vec2;
import org.mariani.vector.Vec3;

import static org.lwjgl.opengl.GL11.*;
import static util.EnumCorner.*;

import theme.Theme;
import util.Font;
import util.EnumCorner;
import view.View;


public class Label extends View {
	
	
	Font font = Font.active;
	EnumCorner alignment = BOTTOMLEFT;
	public String text = "";
	
	private String drawingString = "";
	
	public boolean hasBackground = false;
	public Vec3 back_color = Theme.current.media_color_2;
	
	private DrawingState drawing_state = DrawingState.NORMAL;
	

	public Label(String id, Vec2 pos, Vec2 size) {
		super(id, pos, size);
	}
	public Label(String id, float x, float y, float sx, float sy) {
		super(id, x, y, sx, sy);
	}
	public Label(String id, float x, float y, Vec2 size) {
		super(id, x, y, size);
	}
	public Label(String id, Vec2 pos, float sx, float sy) {
		super(id, pos, sx, sy);
	}
	

	@Override
	public void update() {
	}

	@Override
	public void render() {
		
		
		Vec2 pos = positionInScreen();
		Vec2 size = getSize();

		if (hasBackground) {
			int margin = 4;
			
			glColor3f(back_color.x, back_color.y, back_color.z);
			glBegin(GL_QUADS);
	
				glVertex2f(pos.x - margin,			pos.y - margin);
				glVertex2f(pos.x + size.x + margin,	pos.y - margin);
				glVertex2f(pos.x + size.x + margin,	pos.y + size.y + margin);
				glVertex2f(pos.x - margin,			pos.y + size.y + margin);
				
			glEnd();
		}
		
		
		color = new Vec3(1f);
		
		drawingString = text;
		

		if (font == null) {
			new NullPointerException("No font in label").printStackTrace();
			return;
		}
		
		font.texture.bind();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_ALPHA_TEST);
		glColor3f(1f, 1f, 1f);
		
		fitInLabel();
		
		Vec2 cursor = new Vec2(centerX(alignment, 0), centerY(alignment) - 3 * getScale().y);
		
		for (int i = 0; i < drawingString.length(); i++) {
			if (cursor.y < 0 && alignment.top())
				break;
			if (cursor.y > 0 && alignment.bottom())
				break;
			cursor = drawLetter(drawingString.charAt(i), cursor, i);
		}
		
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_ALPHA_TEST);
	}
	
	private Vec2 drawLetter(char c, Vec2 cursor, int i) {

		if (drawing_state == DrawingState.READINGRED) {
			color.x = (int) c / 128f;
			drawing_state = DrawingState.READINGGREEN;
			return cursor;
		}
		if (drawing_state == DrawingState.READINGGREEN) {
			color.y = (int) c / 128f;
			drawing_state = DrawingState.READINGBLUE;
			return cursor;
		}
		if (drawing_state == DrawingState.READINGBLUE) {
			color.z = (int) c / 128f;
			drawing_state = DrawingState.NORMAL;
			return cursor;
		}
		
		
		if (c == '\n') {
			int line = drawingString.substring(0, i+1).split("\n").length;
			float x = centerX(alignment, line);
			
			return new Vec2(x, cursor.y - font.getChar(' ')[5] * getScale().y);
		}
		
		if (c == 16) {
			drawing_state = DrawingState.READINGRED;
			return cursor;
	}
		if (c > 125)
			c = '?';
		if (c < 32){
			return cursor;}
		
		float[] chr = font.getChar(c);
		

		Vec2 pos = positionInScreen().add(cursor);
		
		glColor3f(color.x, color.y, color.z);
		
		glBegin(GL_QUADS);
			glTexCoord2f(chr[0], chr[3]);
			glVertex2f(pos.x,							pos.y);
			glTexCoord2f(chr[2], chr[3]);
			glVertex2f(pos.x + chr[4] * getScale().x,	pos.y);
			glTexCoord2f(chr[2], chr[1]);
			glVertex2f(pos.x + chr[4] * getScale().x,	pos.y + chr[5] * getScale().y);
			glTexCoord2f(chr[0], chr[1]);
			glVertex2f(pos.x,							pos.y + chr[5] * getScale().y);
		glEnd();
		
		return new Vec2(cursor.x + chr[4] * getScale().x, cursor.y);
	}
	
	
	
	private float getTotalHeight() {
		if (drawingString.isEmpty())
			return getLineHeight();
		return drawingString.split("\n").length * getLineHeight();
	}
	private float getLineHeight() {
		return font.getChar(' ')[5] * getScale().y;
	}
	private float getLineWidth(int l) {
		String line = drawingString.split("\n")[l];
		float w = 0;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == 16)
				i+= 3;
			else
				w += getScale().x * font.getChar(line.charAt(i))[4];
		}
		return w;
	}
	
	private float centerY(EnumCorner align) {
		float res = 0f;
		
		if (align == TOPLEFT || align == TOP || align == TOPRIGHT)
			res = -getLineHeight() + getSize().y;
		else if (align == LEFT || align == CENTER || align == RIGHT)
			res = -getLineHeight() + (getTotalHeight() + getSize().y) / 2f;
		else
			res = -getLineHeight() + getTotalHeight();
		
		return res;
	}
	private float centerX(EnumCorner align, int line) {
		float res = 0;
		
		if (align == TOP || align == CENTER || align == BOTTOM)
			res = (-getLineWidth(line) + getSize().x) / 2f;
		else if (align == TOPRIGHT || align == RIGHT || align == BOTTOMRIGHT)
			res = -getLineWidth(line) + getSize().x;
		
		return res;
	}
	
	
	private void fitInLabel() {
		float lineW = 0f;
		
		for (int i = 0; i < drawingString.length(); i++) {
			char c = drawingString.charAt(i);
			
			if (c == '\n')
				lineW = 0;
			if (c == 16)
				i += 3;
			if (c >= 32 && c < 126)
				lineW += font.getChar(c)[4] * getScale().x;
			
			if (lineW > getSize().x) {
				drawingString = drawingString.substring(0, i) + '\n' + drawingString.substring(i);
				lineW = 0;
			}
		}
	}
	
	
	public void setFont(Font f) {
		font = f;
	}
	public void setAlignment(EnumCorner s) {
		alignment = s;
	}
	
	

	public enum DrawingState {
		NORMAL,
		READINGRED,
		READINGGREEN,
		READINGBLUE
	}
}
