package view.graph;

import org.mariani.vector.Vec2;
import org.mariani.vector.Vec2d;

import view.Quad;

import static org.lwjgl.opengl.GL11.*;

import java.util.Hashtable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import client.Client;
import util.Calculator;

public class Graph1D extends Quad {

	public Graph1D(String id, Vec2 pos, Vec2 size) {
		super(id, pos, size);
	}
	public Graph1D(String id, float x, float y, float sx, float sy) {
		super(id, x, y, sx, sy);
	}
	public Graph1D(String id, float x, float y, Vec2 size) {
		super(id, x, y, size);
	}
	public Graph1D(String id, Vec2 pos, float sx, float sy) {
		super(id, pos, sx, sy);
	}
	
	
	public String function;
	/* = "assign(y0, rand(floor(x)))"
			+ "assign(y1, rand(floor(x+1)))"
			+ "assign(y2, rand(floor(x+2)))"
			+ "assign(y3, rand(floor(x+3)))"
			+ "assign(mu, x%1)"
			+ "assign(mu2, mu*mu)"
			+ "assign(a0, y3-y2-y0+y1)"
			+ "assign(a1, y0-y1-a0)"
			+ "assign(a2, y2-y0)"
			+ "assign(a3, y1)"
			+ "a0*mu*mu2+a1*mu2+a2*mu+a3";*/
			//+ "y0*(1-mu)+y1*mu";
	//"(rand(floor(x)+1)-rand(floor(x))) + rand(floor(x))";
	public Vec2 shift = new Vec2(0);
	
	float resolution = 5;
	
	@Override
	public void update() {
		super.update();
		
		if (isMouseOver() && Mouse.isButtonDown(0)) {
			shift = shift.add(new Vec2(Mouse.getDX(), Mouse.getDY()));
		}
		
		if (isMouseOver() && Mouse.isButtonDown(1)) {
			setScale(getScale().add(new Vec2(Mouse.getDX(), Mouse.getDY())));
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			resolution += 0.1;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
			resolution = Math.max(1, resolution-0.1f);
		}
	}
	
	@Override
	public void render() {
		super.render();
		
		Vec2d lastv = null;

		Vec2 size = getSize();
		Vec2 scale = getScale();
		Vec2 pos = positionInScreen().add(size.divide(2));
		
		
		// Rulers
		
		drawRulers();
		
		
		// Function
		
		try {
			
			long current_time = System.currentTimeMillis() - Client.current.startTime();
		
			glColor3f(1, 0, 0);
			for (double x = -size.x/2; x < size.x/2; x+= (int) resolution) {
				
				Hashtable<String, Double> variables = new Hashtable<String, Double>();
				variables.put("x", (x-shift.x)/scale.x);
				variables.put("TIME", (double) current_time);
				double y = (Calculator.parse(function, variables) * scale.y) + shift.y;
				
				if (y > -size.y/2 && y < + size.y/2) {
					
					if (lastv == null) lastv = new Vec2d(x, y);
				
					glBegin(GL_LINES);
					glVertex2d(pos.x+lastv.x, pos.y+lastv.y);
					glVertex2d(pos.x+x, pos.y+y);
					glEnd();
					
					lastv = new Vec2d(x, y);
				}
			}
			
			Client.current.console = "";
			
		} catch (Exception e) {
			e.printStackTrace();
			Client.current.console += "\20\200\0\0" + e.toString() + "\n\20\120\30\30";
			for (int i = 0; i < e.getStackTrace().length; i++)
				Client.current.console += "       at: " + e.getStackTrace()[i] + "\n";
		}
	}
	
	
	private void drawRulers() {

		Vec2 size = getSize();
		Vec2 scale = getScale();
		Vec2 pos = positionInScreen().add(size.divide(2));

		
		glColor3f(1-color.x, 1-color.y, 1-color.z);
		
		Vec2 origin = pos.add(shift);
		
		glBegin(GL_LINES);
		glVertex2f(pos.x-size.x/2, origin.y);
		glVertex2f(pos.x+size.x/2, origin.y);
		glEnd();
		
		glBegin(GL_LINES);
		glVertex2f(origin.x, pos.y-size.y/2);
		glVertex2f(origin.x, pos.y+size.y/2);
		glEnd();
		
		int subdiv = 5;
		
		int i = 0;
		double x;
		float jump = scale.x / subdiv;
		
		if (scale.x > 4f) {
		
			do {
				
				x = origin.x;
				float back = (int)((size.x/2+shift.x) / jump) * jump;
				x -= back;
				int bigline_helper = (int) (((back/scale.x/2)*10)%subdiv);
				
				int width = 2;
				if ((i - bigline_helper) % subdiv == 0)
					width = 4;
				
				x += i * jump;
	
				if (width == 4 || scale.y > 16f) {	// Threshold to draw little lines
					glBegin(GL_LINES);
					glVertex2d(x, origin.y-width);
					glVertex2d(x, origin.y+width);
					glEnd();
				}
					
				i++;
			} while (x < pos.x + size.x/2 - jump);
		}
		
		i = 0;
		double y;
		jump = scale.y / subdiv;
		
		if (scale.y > 4f) {
		
			do {
				
				y = origin.y;
				float back = (int)((size.y/2+shift.y) / jump) * jump;
				y -= back;
				int bigline_helper = (int) (((back/scale.y/2)*10)%subdiv);
				
				int width = 2;
				if ((i - bigline_helper) % subdiv == 0)
					width = 4;
				
				y += i * jump;
				
				if (width == 4 || scale.y > 16f) {	// Threshold to draw little lines
					glBegin(GL_LINES);
					glVertex2d(origin.x-width, y);
					glVertex2d(origin.x+width, y);
					glEnd();
				}
				
				i++;
			} while (y < pos.y + size.y/2 - jump);
		}
	}

}
