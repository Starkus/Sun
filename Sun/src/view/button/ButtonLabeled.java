package view.button;


import org.mariani.vector.Vec2;

import static util.EnumCorner.CENTER;

import util.EnumCorner;
import util.Font;
import view.label.Label;


public class ButtonLabeled extends Button {
	
	Label label;

	public ButtonLabeled(String id, Vec2 pos, Vec2 size) {
		super(id, pos, size);
		labelInit();
	}
	public ButtonLabeled(String id, float x, float y, float sx, float sy) {
		super(id, x, y, sx, sy);
		labelInit();
	}
	public ButtonLabeled(String id, float x, float y, Vec2 size) {
		super(id, x, y, size);
		labelInit();
	}
	public ButtonLabeled(String id, Vec2 pos, float sx, float sy) {
		super(id, pos, sx, sy);
		labelInit();
	}
	
	private void labelInit() {
		label = new Label(id()+"_label", getPosition(), getSize());
		label.text = "no_label";
		label.setCenter(center);
		label.setAlignment(CENTER);
	}
	
	@Override
	public void update() {
		super.update();
		label.update();
	}
	@Override
	public void render() {
		super.render();
		label.render();
	}
	
	
	@Override
	public void setCenter(EnumCorner c) {
		super.setCenter(c);
		label.setCenter(c);
	}
	public void setFont(Font f) {
		label.setFont(f);
	}
	public void setLabel(String s) {
		label.text = s;
	}
	public String getLabel() {
		return label.text;
	}
	
	@Override
	public void setPosition(Vec2 p) {
		super.setPosition(p);
		label.setPosition(getPosition());
	}
	@Override
	public void setSize(Vec2 s) {
		super.setSize(s);
		label.setSize(getSize());
	}
	@Override
	public void setScale(Vec2 s) {
		super.setScale(s);
		label.setScale(getScale());
	}

}
