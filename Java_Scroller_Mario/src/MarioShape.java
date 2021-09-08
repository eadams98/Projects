import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;


public class MarioShape implements MoveableShape
{
	private int BLOCK_SIZE;
	private int dx, dy;
	private static int player_speed = 2;
	private int JUMP_HEIGHT;

	private short screenData[][];
	private boolean PowerUp;
	private boolean Airborne = false;
	private boolean FacingRight = true;
	
	//private Image KirbyRight = new ImageIcon("gamepix/Kirby1.png").getImage();
	File inFile = new File("gamepix/Kirby1.png");
	BufferedImage KirbyRight;
	private Image KirbyRightTrans;

	{
		try {
			KirbyRight = ImageIO.read(inFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	BufferedImage KirbyLeft;

	BufferedImage background = new BufferedImage(KirbyRight.getWidth(null), KirbyRight.getHeight(null), BufferedImage.TYPE_INT_RGB);
	BufferedImage KirbyRightBuffered;
	//private Image[][] kirbyImages = new Image [4][4];
	//private ImageIcon[] KirbyRightWalkArray = new ImageIcon[4];
	private BufferedImage[][] KirbyRightWalkArray = new BufferedImage[3][4];
	private int KirbyWalkIdx = -1;

	
	private int view_dx = -1; private int view_dy = 0;
	private int req_dx = 0; private int req_dy = 0;
	private int player_x, player_y;
	private int player_dx = 0; private int player_dy = 0;

	public MarioShape (short screenData[][], int brd_x, int brd_y, int req_dx, int req_dy,
				int width, boolean PowerUp)
	{
		this.screenData = screenData;
		this.player_x = brd_x * width;
		this.player_y = brd_y * width;
		this.BLOCK_SIZE = width;
		this.PowerUp = PowerUp;
		this.req_dx = req_dx;
		this.req_dy = req_dy;
		this.JUMP_HEIGHT = BLOCK_SIZE * 3;

		KirbyLeft = AddTransparency.toBufferedImage( new ImageIcon("gamepix/Kirby2.png").getImage() );
		//KirbyLeft = TransformColorToTransparency( toBufferedImage(KirbyLeft) );


		//	KIRBY RIGHT WALKING SPRITES
		//
		Image KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby1-2.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255) );
		KirbyRightWalkArray[0][0] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby1-3.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255));
		KirbyRightWalkArray[0][1] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby1-2.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255) );
		KirbyRightWalkArray[0][2] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby1-4.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255) );
		KirbyRightWalkArray[0][3] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		//	KIRBY LEFT WALKING SPRITES
		//
		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby2-2.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255) );
		KirbyRightWalkArray[1][0] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby2-3.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255));
		KirbyRightWalkArray[1][1] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby2-2.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255) );
		KirbyRightWalkArray[1][2] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby2-4.png").getImage();
		KirbyRightWalkAnimation = AddTransparency.TransformColorToTransparency( AddTransparency.toBufferedImage(KirbyRightWalkAnimation), new Color(233,233,233), new Color(255,255,255) );
		KirbyRightWalkArray[1][3] = AddTransparency.ImageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

		KirbyRightBuffered = new BufferedImage(
				KirbyRight.getWidth(null),
				KirbyRight.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		//System.out.println("OTAY: " + KirbyRightBuffered.getRGB(0,0) );

	}

	public void draw(Graphics2D g2) 
	{
		//System.out.println(x +" "+ y);
		if (player_dx == 1) {
			KirbyWalkIdx += 1;
			KirbyRightBuffered = KirbyRightWalkArray[0][ (KirbyWalkIdx) % 4 ];
			FacingRight = true;
		}
		else if ( player_dx == -1 ) {
			KirbyWalkIdx += 1;
			KirbyRightBuffered = KirbyRightWalkArray[1][ (KirbyWalkIdx) % 4 ];
			FacingRight = false;
		}
		//g2.drawImage(KirbyRight, player_x, player_y, null); //supposed to be player_x, player_y
		//Graphics2D g2d = background.createGraphics();
		//g2.setColor(Color.BLACK);
		//g2.fillRect(player_x, player_y, background.getWidth(), background.getHeight());
		else {
			if (FacingRight) {
				KirbyRightTrans = AddTransparency.TransformColorToTransparency(KirbyRight, new Color(233,233,233), new Color(255,255,255)); //image
			} else {
				KirbyRightTrans = AddTransparency.TransformColorToTransparency(KirbyLeft, new Color(233,233,233), new Color(255,255,255)); //image
			}
			KirbyRightBuffered = AddTransparency.ImageToBufferedImage(KirbyRightTrans, KirbyRight.getWidth(), KirbyRight.getHeight()); //buffered image
			File outFile2 = new File("gamepix/KirbyTrans.png");
			try {
				ImageIO.write(KirbyRightBuffered, "PNG", outFile2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//KirbyRightBuffered.getGraphics();
			//g2.drawImage(KirbyRightTrans, player_x, player_y, null); //supposed to be player_x, player_y
		}
		g2.drawImage(KirbyRightBuffered, null, player_x, player_y);

		// ONLY USE .dispose() WHEN YOU EXPLICITYLY CALL GRAPHICS YOURSELE (AKA .createGraphic()
		//g2.dispose();

		if (player_dx == 0) { KirbyWalkIdx = -1; } // reset sprite counter
		System.out.println(player_x + ":x y: " + player_y);
		
	}

	public void move() 
	{
		int px, py;
		short ch;
		System.out.println(player_dx + " " + req_dx + " " + player_dx );

		if (req_dx == -player_dx && req_dy == -player_dy)
		{
			// Triggers when not moving ( POSSIBLY 0 == 0 )
			//System.out.println("first");
			player_dx = req_dx;
			player_dy = req_dy;
			view_dx = player_dx;
			view_dy = player_dy;
		}

		// use or here because even if we are inbetween x coordinates we want to check if we hit the ground
		if (player_x % BLOCK_SIZE == 0 || player_y % BLOCK_SIZE == 0) // USED TO BE &&
		{
			//System.out.println("second");
			px = player_x / BLOCK_SIZE;
			py = player_y / BLOCK_SIZE;
			//ch = screenData[py][px]; //THIS NEEDS TO BE FLIPPED
			ch = screenData[px][py];
			//System.out.println(screenData[px][py+1]); // BOARD VIEW
			if (getPlayer_y() == BLOCK_SIZE*11) { Airborne = false; }
			//System.out.println(py + " " + px);
			//more here stop for walls/pipes/enemy
			
			if (req_dx != 0 || req_dy != 0) //key press is moved
				//if conditional here to check if pipe or object is stopping motion
				System.out.println("second. second");
				player_dx = req_dx;
				player_dy = req_dy;
				view_dx = player_dx;
				view_dy = player_dy;

		}

			if (Airborne)
			{
				player_x = player_x + player_speed * player_dx;
				player_y = player_y + player_speed * 1;
			}

			// this works to prevent player from walking through pipes, but It could be improved a lot
			else
		{
			if ( 	(screenData[(player_x / BLOCK_SIZE) + 1][player_y/BLOCK_SIZE] == 7 && player_dx == 1)||
					(screenData[(player_x / BLOCK_SIZE) - 1][player_y/BLOCK_SIZE] == 7) && player_dx == -1) {
				player_x = player_x + player_speed * 0;
				player_y = player_y + player_speed * player_dy;
			} else {
				player_x = player_x + player_speed * player_dx;
				player_y = player_y + player_speed * player_dy;
			}
		}
		/*
			else
		{

			player_x = player_x + player_speed * player_dx;
			player_y = player_y + player_speed * player_dy;

		}*/
			
			if (player_dy == -1) {
				System.out.println("hello");
				if ( getPlayer_y() == 187) {
					//stop player from going below ground
					System.out.println("STOP");player_dy = 0; Airborne = false;} //slopppyyy FIX
				//Airborne = true;
			}
		//System.out.println(player_x + " " + player_y);


		
	}

	public boolean contains(int x, int y)
	{
		//System.out.println("KIRBY Y: " + player_y + " ENEMY Y: " + y);

		// WORKS IF ENEMY IS ON RIGHT OF PLAYER
		//if (player_y == y-BLOCK_SIZE && ( (player_x >= x-BLOCK_SIZE && player_x <= x) || (player_x >= x && player_x <= x+BLOCK_SIZE) ) ) {
		if (player_x/BLOCK_SIZE == x/BLOCK_SIZE && player_y/BLOCK_SIZE == (y/BLOCK_SIZE)-1) {
			System.out.println("Kirby Kill");
			return true;
		} else {
			return false;
		}

	}

	public void keyTracker(int req_dx, int req_dy) 
	{
		this.req_dx = req_dx; this.req_dy = req_dy;
		//System.out.println ("updated");
	}
	
	public void jump ()
	{ Airborne = true; }

	public int getPlayer_y() {
		//starts 187, one block up is 170, 153
		return player_y;
	}

	public int getPlayer_x() {
		//starts 187, one block up is 170, 153
		return player_x;
	}

	public void togglePower(String type) {
		PowerUp = true; // PUT IT ON A TIMER OR SOMETHING

		if (type == "SPEED") {player_speed *= 2;}
		else if (type == "SLOW") {player_speed /= 2;}
		else if (type == "JUMP") {JUMP_HEIGHT *= 2;} // jump height tied to was tied to level but abstracted/encapsulated here

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// return to normal stats
				player_speed = 2;
				JUMP_HEIGHT = BLOCK_SIZE*3;
			}
		};

		Timer timer = new Timer("Power");
		long delay = 10000L;
		timer.schedule(task,delay);
	}

	public int getJUMP_HEIGHT() {
		System.out.println("JUMP HEIGHT = " + JUMP_HEIGHT);
		return JUMP_HEIGHT;
	}
	/* UNECESSARY?
	public void reset() {
		// EVERYTHING RESETS THAT WILL MESS UP PLAYER
		Airborne = false;
	} */

////////////////////////


}
