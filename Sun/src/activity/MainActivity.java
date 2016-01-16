package activity;


import static util.EnumCorner.*;

import org.lwjgl.opengl.Display;

import org.mariani.vector.Vec2;
import org.mariani.vector.Vec3;

import client.Client;
import event.EventListener;
import util.StringFormatter;
import view.View;
import view.button.ButtonLabeled;
import view.graph.Graph1D;
import view.label.InputLabel;
import view.label.Label;


public class MainActivity extends Activity {

	public MainActivity(String id) {
		super(id);
	}

	@Override
	void setup() {
		
		
		
		Label label = new Label("Function", 0, 0, 1, 1);
		label.setScale(new Vec2(1.5f));
		label.text = "Function";
		label.text = StringFormatter.format(label.text);
		label.setCenter(CENTER);
		label.setAlignment(TOP);
		registerView(label);
		
		
		Graph1D graph = new Graph1D("Graph", 0, 0, 1, 1);
		graph.setColor(new Vec3(0f));
		graph.setScale(new Vec2(50));
		graph.shift = new Vec2(0);
		graph.setCenter(TOPLEFT);
		registerView(graph);
		
		
		Label yeq = new Label("YEquals", 16, 32, 40, 56);
		yeq.text = "y = ";
		yeq.setScale(new Vec2(1.5f));
		yeq.setAlignment(LEFT);
		registerView(yeq);
		
		InputLabel input = new InputLabel("Input", 0, 0, 1, 1);
		input.setPosition(new Vec2(yeq.getPosition().x + yeq.getSize().x, 32));
		input.setScale(new Vec2(1.5f));
		//input.text = "(sin(((x%1)-0.5)PI)*0.5+0.5) * (rand(floor(x)+1)-rand(floor(x))) + rand(floor(x))";
		input.text = "(1-cos(x*PI))/2";
		input.setAlignment(TOPLEFT);
		input.setOnValueReturnListener(new EventListener() {
			public void onAction() {
				label.text = StringFormatter.format(input.text);
				graph.function = input.text;
			}
		});
		registerView(input);
		
		ButtonLabeled go = new ButtonLabeled("ButtonGo", 0, 0, 1, 1);
		go.setSize(new Vec2(32, yeq.getSize().y));
		go.setPosition(new Vec2(Display.getWidth()-32, input.getPosition().y-4));
		go.setLabel("Go");
		go.setScale(new Vec2(1.5f));
		go.setCenter(BOTTOMRIGHT);
		go.setOnReleaseListener(input.getOnValueReturnListener());
		registerView(go);
		
		
		Label console = new Label("Console", 0, 0, 1, 1) {
			@Override
			public void update() {
				super.update();
				
				String c = Client.current.console;
				text = c;
			}
		};
		console.setSize(new Vec2(Display.getWidth()-console.getPosition().x-36, Display.getHeight()-console.getPosition().y-180));
		console.text = "\20\200\0\0Console";
		console.setCenter(TOPLEFT);
		console.setAlignment(TOPLEFT);
		console.hasBackground = true;
		console.back_color = new Vec3(0);
		registerView(console);
		
		
		go.onRelease();
	}
	
	@Override
	void loop() {
		
		float dw = Client.current.displayWidth();
		float dh = Client.current.displayHeight();
		
		
		View func = getView("Function");
		func.setPosition(new Vec2(dw/2f, dh/2f));
		func.setSize(new Vec2(dw-64, 48));
		
		View graph = getView("Graph");
		graph.setPosition(new Vec2(32, dh-32));
		graph.setSize(new Vec2(dw-64, dh/2-64));
		
		View go = getView("ButtonGo");
		View input = getView("Input");
		
		go.setPosition(new Vec2(dw-32, input.getPosition().y-4));
		input.setSize(new Vec2(dw-84 - go.getSize().x-8, 48));
		
		View console = getView("Console");
		console.setPosition(new Vec2(32+4,
									graph.getPosition().y - graph.getSize().y - 64));
		console.setSize(new Vec2(dw-console.getPosition().x-36, dh-console.getPosition().y-180));
		
	}
	
	

}
