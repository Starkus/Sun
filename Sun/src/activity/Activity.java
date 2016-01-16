package activity;

import java.util.Hashtable;

import view.View;

public abstract class Activity {
	
	private String name;
	private Hashtable<String, View> stuff = new Hashtable<String, View>();
	

	public Activity(String id) {
		name = id;
		setup();
		ActivityManager.registerActivity(this);
	}
	
	abstract void setup();
	abstract void loop();
	
	
	public void registerView(View view) {
		if (view.getSize().x * view.getSize().y == 0)
			new Exception("View size cannot be zero.").printStackTrace();
		
		if (view.getScale().x * view.getScale().y == 0)
			new Exception("View scale cannot be zero.").printStackTrace();
		
		stuff.put(view.id(), view);
	}
	public View getView(String id) {
		return stuff.get(id);
	}
	
	
	public void update() {
		loop();
		for (View view : stuff.values()) {
			view.update();
		}
	}
	public void render() {
		for (View view : stuff.values()) {
			view.render();
		}
	}
	
	public String id() {
		return name;
	}

}
