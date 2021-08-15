import java.awt.EventQueue;

import javax.swing.JFrame;

public class Frame extends JFrame
{

	public Frame() 
	{
		initUI();

	}

	private void initUI() {
		
		add (new Level());
		
		setTitle ("Mario/Kirby");
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo (null);
		setVisible (true);
	}
	
	public static void main (String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Frame lvl = new Frame();
				lvl.setVisible(true);
			}
		});
	}
}
