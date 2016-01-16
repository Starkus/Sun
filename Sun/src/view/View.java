package view;

import static util.EnumCorner.*;

import org.lwjgl.input.Mouse;

import org.mariani.vector.Vec2;
import org.mariani.vector.Vec3;

import theme.Theme;
import util.EnumCorner;

public abstract class View {

	private String name;
	private Vec2 position;
	private Vec2 size;
	private Vec2 scale;
	protected Vec3 color = Theme.current.media_color;
	protected EnumCorner center = BOTTOMLEFT;
	
	
	public View(String id, Vec2 pos, Vec2 size) {
		name = id;
		position = pos;
		this.size = size;
		
		scale = new Vec2(1);
	}
	public View(String id, float x, float y, float sx, float sy) {
		name = id;
		position = new Vec2(x, y);
		size = new Vec2(sx, sy);

		scale = new Vec2(1);
	}
	public View(String id, float x, float y, Vec2 size) {
		name = id;
		position = new Vec2(x, y);
		this.size = size;

		scale = new Vec2(1);
	}
	public View(String id, Vec2 pos, float sx, float sy) {
		name = id;
		position = pos;
		size = new Vec2(sx, sy);

		scale = new Vec2(1);
	}
	
	
	public abstract void update();
	public abstract void render();
	
	
	public Vec2 positionInScreen() {
		
		switch (center) {
		case TOPLEFT:
			return new Vec2(position.x,					position.y - size.y);
			
		case TOP:
			return new Vec2(position.x - size.x * 0.5f,	position.y - size.y);
			
		case TOPRIGHT:
			return new Vec2(position.x - size.x,		position.y - size.y);
			
		case LEFT:
			return new Vec2(position.x,					position.y - size.y * 0.5f);
			
		case CENTER:
			return new Vec2(position.x - size.x * 0.5f,	position.y - size.y * 0.5f);
			
		case RIGHT:
			return new Vec2(position.x - size.x,		position.y - size.y * 0.5f);
			
		case BOTTOMLEFT:
			return new Vec2(position.x,					position.y);
			
		case BOTTOM:
			return new Vec2(position.x - size.x * 0.5f,	position.y);
			
		case BOTTOMRIGHT:
			return new Vec2(position.x - size.x,		position.y);
			
		default:
			new RuntimeException("Invalid center").printStackTrace();
			return position;
		}
	}

	
	public boolean isMouseOver() {
		
		Vec2 pos = positionInScreen();
		Vec2 mouse = new Vec2(Mouse.getX(), Mouse.getY());
		
		if (pos.x < mouse.x && pos.x+size.x > mouse.x) 
			if (pos.y < mouse.y && pos.y+size.y > mouse.y)
				return true;
		
		return false;
	}
	
	public void setCenter(EnumCorner c) {
		center = c;
	}
	public String id() {
		return name;
	}
	public void setColor(Vec3 c) {
		color = c;
	}
	public Vec2 getPosition() {
		return position;
	}
	public void setPosition(Vec2 p) {
		position = p;
	}
	public Vec2 getSize() {
		if (size.x * size.y == 0)
			new Exception("WARNING: \""+id()+"\"'s size set to 0, clamped to 0.001").printStackTrace();
		return size;
	}
	public void setSize(Vec2 s) {
		size = s.clampFloor(0.001f);
	}
	public Vec2 getScale() {
		if (scale.x * scale.y == 0)
			new Exception("WARNING: \""+id()+"\"'s scale set to 0, clamped to 0.001").printStackTrace();
		return scale;
	}
	public void setScale(Vec2 s) {
		scale = s.clampFloor(0.001f);
	}
}
