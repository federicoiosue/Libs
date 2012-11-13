package it.autostrade.feio.test;

import it.autostrade.feio.utils.obj.ColorFactory;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;


public class TestColorFactory {


	public static void main(String[] args) {

		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(ColorFactory.getInstance().getColor());
		colors.add(ColorFactory.getInstance().getColor("asd"));
		colors.add(ColorFactory.getInstance().getColor("asd"));

		Frame frame = new CirclesDraw(colors);
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		frame.setSize(300, 250);
		frame.setVisible(true);

	}
}



class CirclesDraw extends Frame {

	Shape circle = new Ellipse2D.Float(100.0f, 100.0f, 100.0f, 100.0f);
	ArrayList<Color> colors;

	public CirclesDraw(ArrayList<Color> colors) {
		this.colors = colors;
	}

	public void paint(Graphics g) {
		Graphics2D ga = (Graphics2D) g;;
		for (Color c: this.colors) {
			ga.draw(circle);
			ga.setPaint(c);
			ga.fill(circle);
		}
		
		
	}


}
