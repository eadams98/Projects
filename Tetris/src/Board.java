import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.ArrayList;

public class Board extends JPanel {
    //private final static int N_BLOCKS = 15;
    private final static int BLOCK_SIZE = 25;
    private final static int BORDER_SIZE = 25;
    private final static int SCREEN_SIZE_W = 11 * BLOCK_SIZE;
    private final static int SCREEN_SIZE_H = 20 * BLOCK_SIZE;

    private int score; // score
    private boolean inGame = true , inMotion = true;
    private MoveableShape current;
    private ArrayList<MoveableShape> shapes; // shapes on the board not current
    private Timer timer; private int counter = 0;

    // 0 for empty, 1 for taken. For collision and points
    private final short screenData [][] =
            {
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0}
            };

    public Board()
    {
        // listen to keyboard
        //System.out.print("ok");
        addKeyListener( new TetrisAdapter());
        setFocusable(true); // makes the buttons hearable

        setMinimumSize(new Dimension(SCREEN_SIZE_W + BORDER_SIZE, SCREEN_SIZE_H + BORDER_SIZE));
        setPreferredSize(new Dimension(SCREEN_SIZE_W + BORDER_SIZE, SCREEN_SIZE_H + BORDER_SIZE));

        initVariables();
        initBoard();
        initGame();
        LShape ll = new LShape(5,1, BLOCK_SIZE); // 0,0 doesn't work
        screenData[0][4] = 1;
        screenData[1][4] = 1;
        screenData[2][4] = 1;
        screenData[2][5] = 1;
        current = ll;
    }

    private void initBoard()
    {
        setBackground(Color.white);
    }

    private void initVariables()
    {
        timer = new Timer (40, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
                //System.out.println(timer);
            }
        });
        timer.start();
    }

    private void initGame()
    {
        score = 0;
        initLevel();

    }

    private void initLevel()
    {
        shapes = new ArrayList<MoveableShape>();
    }

    ////////////////////DRAW/////////////

    public void paintComponent (Graphics g) //overwrites the basic draw
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor (Color.white);
        g2d.fillRect (0, 0, getWidth(), getHeight()); // fills whole board/JPanel black

        if (inGame) {
            playGame(g2d);
            moveShape(g2d);
        }
        else
        {
            showIntroScreen (g2d);
        }
    }

    private void showIntroScreen (Graphics2D g2d)
    {
        g2d.setColor (new Color (0, 32, 48));
        g2d.fillRect (SCREEN_SIZE_W/4+BORDER_SIZE/2, SCREEN_SIZE_H/2-BORDER_SIZE/2, SCREEN_SIZE_W/2, 50);
        g2d.setColor (Color.white);
        g2d.drawRect (SCREEN_SIZE_W/4+BORDER_SIZE/2, SCREEN_SIZE_H/2-BORDER_SIZE/2, SCREEN_SIZE_W/2, 50);

        String s = "Press s to start.";
        Font small = new Font ("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics (small);

        g2d.setColor (Color.white);
        g2d.setFont (small);
        g2d.drawString (s, (SCREEN_SIZE_W + BORDER_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE_H / 2 + BORDER_SIZE);
    }

    private void playGame (Graphics2D g2d)
    {
        int line;
        counter = (counter+1) % 11; // control drop speed
        // horizontal
        for (line = 0; line < 11; line++ ) {
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(BORDER_SIZE+(line*BLOCK_SIZE), BORDER_SIZE, BORDER_SIZE+(line*BLOCK_SIZE), SCREEN_SIZE_H );
        }
        // vertical
        for (line = 0; line < 20; line++ ) {
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(BORDER_SIZE, BORDER_SIZE+(line*BLOCK_SIZE), SCREEN_SIZE_W, BORDER_SIZE+(line*BLOCK_SIZE));
        }

        // block
        for (int a = 0; a < shapes.size(); a++) {shapes.get(a).draw(g2d);}
        if (counter == 10) {
            int lowestBlocks[][] = current.getLowestBlocks();
            for (int block = 0; block < lowestBlocks.length; block++) {
                System.out.println("lowest");
                System.out.print(lowestBlocks[block][0]);
                System.out.print(' ');
                System.out.println(lowestBlocks[block][1]);

                //System.out.print("data here = ");
                //System.out.println(screenData[lowestBlocks[block][1]][lowestBlocks[block][0]]);
                if (lowestBlocks[block][1]+1 != 19)
                    System.out.println(screenData[lowestBlocks[block][1]+1][lowestBlocks[block][0]]);
               // int delay = 1000; // number of milliseconds to sleep
                //long start = System.currentTimeMillis();
                //while(start >= System.currentTimeMillis() - delay); // do nothing


                if (lowestBlocks[block][1] + 1 == 19 || screenData[lowestBlocks[block][1]+1][lowestBlocks[block][0]] == 1)
                {
                    System.out.println("set false");
                    inMotion = false;

                    System.out.print(lowestBlocks[block][0]);
                    System.out.print(' ');
                    System.out.println(lowestBlocks[block][1]+1);
                    System.out.println(screenData[lowestBlocks[block][1]+1][lowestBlocks[block][0]]);
                    int delay = 5000; // number of milliseconds to sleep
                    long start = System.currentTimeMillis();
                    while(start >= System.currentTimeMillis() - delay); // do nothing
                }
            }
            //if (inMotion == true) {
                //if(inMotion == true)
                current.move();
                int pblockPosition[][] = current.getPreviousBlockPosition();
                int blockPosition[][] = current.getCurrentBlockPosition();

                for (int block = 0; block < pblockPosition.length; block++) {
                    screenData[pblockPosition[block][1]][pblockPosition[block][0]] = 0;
                }

                for (int block = 0; block < blockPosition.length; block++) {
                    screenData[blockPosition[block][1]][blockPosition[block][0]] = 1;
                    System.out.print("testtttt");
                    System.out.println(blockPosition[block][1] + 1);

                    // IF bottom of screen or block below
                    /*if (screenData[blockPosition[block][1] + 1][blockPosition[block][0]] == 1 || blockPosition[block][1] + 1 == 18) {
                        inMotion = false;
                        shapes.add(current);
                        System.out.println("FUCKKKKKKKKKKKKKK");
                    }*/
                }
            //}
        }
        current.draw(g2d);
        print2D(screenData);

        if (inMotion == false)
        {
            shapes.add(current);
            LShape ll = new LShape(5,1, BLOCK_SIZE); // 0,0 doesn't work
            screenData[0][4] = 1;
            screenData[1][4] = 1;
            screenData[2][4] = 1;
            screenData[2][5] = 1;
            current = ll;
            inMotion = true;
            current.draw(g2d);
        }

        //System.out.println(counter);
        //TShape tt = new TShape(7, 1, BLOCK_SIZE);
        //tt.draw(g2d);
        //current = tt;
    }

    ////////////////////

    protected class TetrisAdapter extends KeyAdapter
    {
        public void keyPressed (KeyEvent e)
        {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT)
            {
                System.out.println("left");
                current.keyTracker(-1,0);

            }
            else if (key == KeyEvent.VK_RIGHT)
            {
                System.out.print("right");
                current.keyTracker(1,0);
            }
        }
    }

    //////////////

    private void moveShape (Graphics2D g2d)
    {
        //current.move();
        current.draw(g2d);
    }

    public static void print2D(short mat[][]) // helper func
    {
        // Loop through all rows
        for (short[] col : mat)

            // converting each row as string
            // and then printing in a separate line
            System.out.println(Arrays.toString(col));
        System.out.println("end");
    }

}
