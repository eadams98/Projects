import java.awt.*;
import java.awt.event.*; //why
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer; //why 


public class Level extends JPanel
{
	private int lives, score, numGoomba, currLevel;
	private boolean inGame = true;
	private boolean dying = false;
	private int playerLives = 3;
    
    private final static int N_BLOCKS_X = 40;
    private final static int N_BLOCKS_Y = 15;
    private final static int BLOCK_SIZE = 16;
    private final static int SCREEN_SIZE = 15 * BLOCK_SIZE; //15 by 15 view even though map is larger

    private final static int SCREEN_SIZE_X = N_BLOCKS_X * BLOCK_SIZE;
    private final static int SCREEN_SIZE_Y = N_BLOCKS_Y * BLOCK_SIZE;
    //private final static int SCREEN_SIZE =  * BLOCK_SIZE; //square viewing size

    private final static int BORDER_SIZE = 25;
    private Color Background = new Color (0, 219, 255);
    private Timer timer;
    private short[][] screenData;
    //private final int JUMP_HEIGHT = BLOCK_SIZE * 3; // jump height to eliminate magic numbers
	private final int FLOOR_LOCATION = BLOCK_SIZE * 11;
    
    private final static int GROUND = 0;
    private final static int GRASS = 1;
    private final static int PIPE_BODY = 5;
    private final static int PIPE_TOP = 7;
    private final static int BLOCK = 3;
    private final static int MYS_BLOCK = 4;
    // after mys_block has ben hit
	private final static int MYS_BLOCK_USED = 6;
    
    private MarioShape Player; private EnemyShape Enemy;
    private List<EnemyShape> EnemyList = new ArrayList<>(1);
    private List<Integer> toBeDel = new ArrayList<>();
    private List<MoveableShape> items = new ArrayList<>();
    private boolean PowerUp = false;
    private int req_dx, req_dy;
    
	private Image ground = new ImageIcon("gamepix/middleGround.png").getImage();
	private Image grass = new ImageIcon("gamepix/topGround.png").getImage();
	private Image block = new ImageIcon("gamepix/yellowEye.png").getImage();
	private Image mys_block_used = new ImageIcon("gamepix/greyEye.png").getImage();
	private Image mushroom = new ImageIcon("gamepix/mushroom.png").getImage();

	private Image pipe_body; // = new ImageIcon("gamepix/pipebody.png").getImage();

	private Image pipe_top; // = new ImageIcon("gamepix/pipehead.png").getImage();

	private Image[] mys_block = {
			new ImageIcon("gamePix/question1.png").getImage(),
			new ImageIcon("gamePix/question4.png").getImage(),
			new ImageIcon("gamePix/question3.png").getImage(),
			new ImageIcon("gamePix/question2.png").getImage()
	}; private boolean mys_blockReset = false; private int mys_blockAnim = 0;
    
  private final short levelData[][] = 
	  {
			  {2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 1, 0, 0}, 
			  {2, 2, 2, 2, 2, 2, 2, 2, 4, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, 1, 0, 0}, //5
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0}, // 10
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0}, //15
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0}, //20
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0}, //25
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
			  {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0} //30

	  };


	public Level()
	{
		
		setMinimumSize (new Dimension (SCREEN_SIZE + BORDER_SIZE, SCREEN_SIZE + BORDER_SIZE));
		setPreferredSize (new Dimension (SCREEN_SIZE , SCREEN_SIZE +  BORDER_SIZE));

		try {
			intVariables();
		} catch (Exception e){
			System.out.println("PROBLEM FORMATING PIPE IMAGE");
		}
		intBoard();
		intGame();

	}
	
	private void intBoard()
	{
		addKeyListener (new PlayerKeyAdapter());
		setFocusable (true);
		setBackground (Background);
		setDoubleBuffered (true);
	}
	
	private void intVariables() throws IOException
	{
		screenData = new short[levelData.length][levelData[0].length]; ///check again

		AddTransparency.resize("gamepix/pipebody.png", "gamepix/pipebodyFormated.png", 22, 22);
		pipe_body = new ImageIcon("gamepix/pipebodyFormated.png").getImage();
		pipe_body = AddTransparency.TransformColorToTransparency(AddTransparency.toBufferedImage(pipe_body), new Color(61, 70, 72), new Color(125, 145, 151));

		AddTransparency.resize("gamepix/pipehead.png", "gamepix/pipeheadFormated.png", 22, 22);
		pipe_top = new ImageIcon("gamepix/pipeheadFormated.png").getImage();;
		pipe_top = AddTransparency.TransformColorToTransparency(AddTransparency.toBufferedImage(pipe_top), new Color(250, 250, 250), new Color(255, 255, 255));;
		
		timer = new Timer (40, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
			
	}
	
	private void intGame()
	{
		lives = 3;
		score = 0;
		numGoomba = 8 ;
		currLevel = 1;
		
		initLevel();
	}
	
	private void initLevel()
	{
		for (int r = 0; r < levelData.length; r++)
			for (int c = 0; c < levelData[0].length; c++)
				{screenData[r][c] = levelData[r][c];
				//System.out.println(levelData[r][c]);
				}
		
		continueLevel();
	}
	
	private void death()
	{
		playerLives--;
		dying = false;
		EnemyList.clear(); // remove all enemy's from last play through
		items.clear();
		
		if (playerLives == 0)
			inGame = false;

		// RESET MAP
		initLevel();
	}
	
	private void continueLevel()
	{
		Player = new MarioShape(screenData, 10, 11, 0, 0, BLOCK_SIZE, PowerUp);
		Enemy = new EnemyShape(screenData, 0, 11, 0, 0, BLOCK_SIZE, PowerUp);
		EnemyList.add(Enemy);
	}
	
	////////////////////////////////////////
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Background);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		drawLevel (g2d);
		drawScore (g2d);
		
		if (inGame) {
			try {
				playGame (g2d);
			} catch (IOException e) {
				System.out.println("SOMETHING WRONG WITH ENEMY SCALING");
				e.printStackTrace();
			}
		}
		else
		{
			currLevel = 1;
			//showIntroScreen (g2d);
			Player.draw(g2d);
			for (EnemyShape villian : EnemyList) {
				try {
					villian.draw(g2d);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!items.isEmpty()) {
				for (MoveableShape item : items) {
					try {
						item.draw(g2d);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			showPauseScreen(g2d);
		}
	}

	private void showPauseScreen(Graphics2D g2d) {
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 24));
		g2d.drawString("PAUSED", (SCREEN_SIZE+BORDER_SIZE)/4, SCREEN_SIZE/2);

	}

	private void drawLevel (Graphics2D g2d)
	{
		int r, c;
		
		for (r = 0; r < screenData.length * BLOCK_SIZE; r += BLOCK_SIZE) //rn shows whole data
		{
			for (c = 0; c < 15 * screenData[0].length; c += BLOCK_SIZE) //rn shows whole data
			{
				int gr = r / BLOCK_SIZE;
				int gc = c / BLOCK_SIZE;
				
				g2d.setColor (Background);
				//System.out.println(screenData[gr][gc]);
				//System.out.println("gr: " + gr + " gc: " + gc);
				
				if (screenData[gr][gc] == GROUND ) //0
					g2d.drawImage(ground, r, c, null);//draw ground 

				if (screenData[gr][gc] == GRASS) //1
					g2d.drawImage(grass, r, c, null);
				
				if (screenData[gr][gc] == PIPE_BODY) //5
					g2d.drawImage(pipe_body, r,c, null);//draw pipe body
					//System.out.println("2");

				if (screenData[gr][gc] == PIPE_TOP) //7
					// DEAL WITH MAGIC NUMBER (PIPE HEAD AND BODY HAVE DIFF DIMESIONS SO REQUIRED AN OFFSET
					g2d.drawImage(pipe_top, r, c, null); //draw pipe top

				if (screenData[gr][gc] == BLOCK) //3
					g2d.drawImage(block, r, c, null);//draw block
					//System.out.println("3");
				if (screenData[gr][gc] == MYS_BLOCK)
					g2d.drawImage(mys_block[mys_blockAnim], r, c, null);//draw mys block
				if (screenData[gr][gc] == MYS_BLOCK_USED)
					g2d.drawImage(mys_block_used, r, c, null);//draw mys block after being hit
					
				
			}
		}
	}
	
	private void playGame (Graphics2D g2d) throws IOException {
		if (dying) {
			//Player.reset(); // unecessary
			death();
		}
		else
		{

			movePlayer (g2d);
			moveItems(g2d);

			// remove/despawn items that have passed expiration
			for (Integer num : toBeDel) {
				items.remove(items.get(num));
			} toBeDel.clear();

			// if no enemies left generate more ( EITHER MAKE THIS A FUNCTION OR REMOVE ENTIRELY )
			//if (EnemyList.isEmpty()) {
			//	EnemyList.add(new EnemyShape(screenData, 0, 11, 0, 0, BLOCK_SIZE, PowerUp));
			//}

			for (EnemyShape  villian: EnemyList) {
				if ( Player.contains(villian.getPlayer_x(), villian.getPlayer_y()) )
					//System.out.println("villian IDX: " + EnemyList.indexOf(villian));
					toBeDel.add(EnemyList.indexOf(villian));
			}

			// remove jumped on enemies
			for (Integer num : toBeDel) {
				EnemyList.remove(EnemyList.get(num));
				System.out.println(num);
			} toBeDel.clear();
			//CLEAN UP REMOVING ENEMEIES
			//FIX ONLY REMOVE IF PLAYER ON IT'S HEAD OR POSSIBLE PUNCHING
			//MAKE CONDITIONAL SIMPLIER
			if (!EnemyList.isEmpty()) {
				for (EnemyShape villian : EnemyList) {
					if (villian.contains(Player.getPlayer_x(), Player.getPlayer_y()))
						dying = true;
				}
			}
			// Don't know if it should go B4 or AFTER move
			moveEnemy(g2d);
		}
			//checkMaze(); have to add things for it to work

	}

	
	private void movePlayer (Graphics2D g2d)
	{
		//System.out.println("should print if stuck at  step");

		Player.move();
		//Player = new MarioShape (screenData, 0, 11, req_dx, req_dy, BLOCK_SIZE, PowerUp); no movement because req_dx, req_dy not passed
		Player.draw(g2d);
		//System.out.println(Player.getPlayer_y());

		// If player is jumping don't toggle Player's Airborne propert until it reaches the height of its jump

		if ( 	Player.getPlayer_y() == FLOOR_LOCATION - Player.getJUMP_HEIGHT() || // reached the apex of jump height
				screenData[Player.getPlayer_x() / BLOCK_SIZE][Player.getPlayer_y() / BLOCK_SIZE] == BLOCK || // block above head
				screenData[Player.getPlayer_x() / BLOCK_SIZE][Player.getPlayer_y() / BLOCK_SIZE] == MYS_BLOCK || // mys block above head
				screenData[Player.getPlayer_x() / BLOCK_SIZE][Player.getPlayer_y() / BLOCK_SIZE] == MYS_BLOCK_USED // used mys block above head
			) { Player.jump(); }

		// If player is not on the ground, then don't reset the player's momentum until it reaches the ground
		// BLOCK_SIZE = 17, then 187. BLOCK_SIZE = 16 then 176
		if ( Player.getPlayer_y() >= FLOOR_LOCATION) { req_dx=0; req_dy=0; } // get rid of conditional

		Player.keyTracker(req_dx, req_dy);

		// SPINNING mys_blocks (DEPENDENT ON PLAYER BEING ALIVE IF IT SPINS)
		mys_blockAnim += 1;
		if (mys_blockAnim > 3) { mys_blockAnim = 0;}
		int Px = Player.getPlayer_x()/BLOCK_SIZE; int Py = Player.getPlayer_y()/BLOCK_SIZE;
		if (screenData[Px][Py] == MYS_BLOCK) {
			screenData[Px][Py] = MYS_BLOCK_USED;
			// MUSHROOM
			//g2d.drawImage(mushroom, Px*BLOCK_SIZE, (Py-1)*BLOCK_SIZE, null);
			items.add(new PowerUpShape(screenData, Px, (Py-1), 1, 0, BLOCK_SIZE, "gamepix/powerup/mushroom.png"));
		}
			//g2d.drawImage(mys_block_used, Px*BLOCK_SIZE, Py*BLOCK_SIZE, null);//draw mys block after being hit

	}

	private void moveEnemy (Graphics2D g2d) throws IOException {
		/*
		EnemyList.get(0).getPlayer_x(Player.getPlayer_x());
		EnemyList.get(0).move();
		EnemyList.get(0).draw(g2d);
		*/
		for (EnemyShape villian : EnemyList) {
			villian.getPlayer_x(Player.getPlayer_x());
			villian.move();
			villian.draw(g2d);
		}
	}

	private void moveItems (Graphics2D g2d) throws IOException {
		if (!items.isEmpty()) {
			for (MoveableShape item : items) {
				item.move();
				item.draw(g2d);
				if ( item.contains(Player.getPlayer_x()/BLOCK_SIZE, Player.getPlayer_y()/BLOCK_SIZE) ) // CLEAN THIS UP!!!!!
				{
					toBeDel.add(items.indexOf(item));
					PowerUpShape temp = (PowerUpShape) item; // Hate having to cast might  need to rework a bit
					Player.togglePower(temp.getPowerType());
				}
				// SLOPPY. FIX (EITHER MAKE ITEMS OF TYPE POWERUPSHAPE OR SOMETHING ELSE)
				// !!!!!!!!!!!!!!!!!!!!!!!!!! FIX !!!!!!!!!!!!!!!!!!!!!!!!!!
				// when paused the items get time method doesn't account for the pause therefore it will time out
				// !!!!!!!!!!!!!!!!!!!!!!!!!! FIX !!!!!!!!!!!!!!!!!!!!!!!!!!
				PowerUpShape temp = (PowerUpShape) item;
				if (temp.getElapsedTime() > 10000)
					toBeDel.add(items.indexOf(item));
			}
		}
	}
	
	private void drawScore (Graphics2D g)
	{
		String s = "Score: " + 0;
		Font smallFont = new Font ("Helvetica",  Font.BOLD, 14);
		g.setFont( smallFont);
		g.setColor(Color.WHITE);
		g.drawString(s, SCREEN_SIZE / 2  + 60, SCREEN_SIZE + BLOCK_SIZE); // FIX THIS WITH MaTH So IT scales
	}


	
	/////////////////////////////////////////////////////
	
	protected class PlayerKeyAdapter extends KeyAdapter
	{
		public void keyPressed (KeyEvent e)
		{
			int key = e.getKeyCode();
			
			if (inGame)
			{
                if (key == KeyEvent.VK_LEFT)
                {
                    req_dx = -1; //tied to pacKeyAdapter and therfore can't be seperated into new pac class
                    //req_dy = 0;
                    Player.keyTracker(req_dx, req_dy);
                }
                else if (key == KeyEvent.VK_RIGHT)
                {
                    req_dx = 1;
                    //req_dy = 0;
                    Player.keyTracker(req_dx, req_dy);
                }
                else if (key == KeyEvent.VK_UP)
                {
                    req_dx = 0;
                    req_dy = -1;
                    Player.keyTracker(req_dx, req_dy);
                    //Player.jump(); ORDER OF OPERATION MAKES THIS INEFFECIENT
                }
                else if (key == KeyEvent.VK_SPACE)
				{
					// pause game
					// Potentially turn inGame = false
					// if inGame False it'll save position of all pieces, but won't display them when paused
					// if timer.stop(). it'll freeze pieces in Place and show them, but won't print pause
					System.out.println("PAUSED");

					if (timer.isRunning()) {  inGame =false; }
					else { timer.start(); }

				}
			}
			else
			{
				if (key == KeyEvent.VK_SPACE)
				{
					// pause game
					// Potentially turn inGame = false
					System.out.println("Play");
					inGame = true;
					//timer.start();
				}
			}
		}
	}

}
