package activity;

import java.util.Hashtable;

public class ActivityManager {

	static final Hashtable<String, Activity> bank = new Hashtable<String, Activity>();
	static Activity current;
	
	public static void registerActivity(Activity activity) {
		bank.put(activity.id(), activity);
		if (current == null)
			current = activity;
	}
	
	public static void switchActivity(String id) {
		current = bank.get(id);
	}
	
	public static void update() {
		current.update();
	}
	public static void render() {
		current.render();
	}
}
