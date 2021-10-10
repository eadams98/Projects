import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.sql.SQLOutput;

public class LShape implements MoveableShape {

    private int BLOCK = 24;
    private int x, y;
    private int dx, dy = 0;
    private int LShape_x, LShape_y;
    private int width;

    private int view_dx = 0; // NOT NEEDED

    private boolean init = true;
    private int pBlockPosition[][];
    private int blockPosition[][];
    private int lowestBlocks[][];
    private int state = 0; // 0 initial - 3 last. 4 rotates


    public LShape (int brd_x, int brd_y, int width)
    {
        this.x = brd_x * width;
        this.y = brd_y * width;
        this.width = width;
    }

    public void draw(Graphics2D g2)
    {
        // m never changes rotate around m
        if (state == 0) {
            Rectangle2D.Double t = new Rectangle2D.Double(x + 1, y + 1, BLOCK, BLOCK);
            Rectangle2D.Double m = new Rectangle2D.Double(x + 1, y + 26, BLOCK, BLOCK);
            Rectangle2D.Double b = new Rectangle2D.Double(x + 1, y + 51, BLOCK, BLOCK);
            Rectangle2D.Double s = new Rectangle2D.Double(x + 26, y + 51, BLOCK, BLOCK);

            g2.setColor(Color.CYAN);
            g2.fill(t);
            g2.fill(m);
            g2.fill(b);
            g2.fill(s);

            int tempx = x/width - 1;
            int tempy = y/width - 1;
            if (init == true) {
                pBlockPosition = new int[][]{
                        {tempx, tempy},
                        {tempx, tempy + 1},
                        {tempx, tempy + 2},
                        {tempx + 1, tempy + 2}
                };
                init = false;
            }
            blockPosition = new int[][] {
                    {tempx,tempy},
                    {tempx,tempy+1},
                    {tempx,tempy+2},
                    {tempx+1,tempy+2}
            };
            lowestBlocks = new int[][] {
                    {tempx,tempy+2},
                    {tempx+1,tempy+2}
            };

            for (int z = 0; z < blockPosition.length; z++) {
                    System.out.print(blockPosition[z][0]);
                    System.out.print(' ');
                    System.out.println(blockPosition[z][1]);
            }
            //g2.setStroke(new BasicStroke(1));
            //g2.drawLine(BORDER_SIZE, BORDER_SIZE+(line*BLOCK_SIZE), SCREEN_SIZE_W, BORDER_SIZE+(line*BLOCK_SIZE));
        }
        //System.out.println(dx == 1);
        //System.out.print("dx = ");
        //System.out.println(dx);
        else if (state == 1)
        {
            //System.out.println("turn babeee");
            Rectangle2D.Double t = new Rectangle2D.Double(x+26,y+26, BLOCK,BLOCK); //x+1,y+1
            Rectangle2D.Double m = new Rectangle2D.Double(x+1,y+26, BLOCK,BLOCK); // x,y+1
            Rectangle2D.Double b = new Rectangle2D.Double(x-24,y+26, BLOCK,BLOCK); // x-1,y+1
            Rectangle2D.Double s = new Rectangle2D.Double(x-24,y+51, BLOCK,BLOCK); // x-1,y+2

            g2.setColor(Color.CYAN);
            g2.fill(t);
            g2.fill(m);
            g2.fill(b);
            g2.fill(s);
            // dx = 0; dx resets somehow without this?

            int tempx = x/width - 1; // 4
            int tempy = y/width - 1; // 0

            blockPosition = new int[][] {
                    {tempx-1,tempy+1},
                    {tempx,tempy+1},
                    {tempx+1,tempy+1},
                    {tempx-1,tempy+2}
            };
            lowestBlocks = new int[][] {
                    {tempx+1,tempy+1}, // t
                    {tempx,tempy+1}, // m
                    {tempx-1,tempy+2} //s
            };

            for (int z = 0; z < blockPosition.length; z++) {
                System.out.print(blockPosition[z][0]);
                System.out.print(' ');
                System.out.println(blockPosition[z][1]);
            }
        }
        else if (state == 2)
        {
            //System.out.println("turn babeee");
            Rectangle2D.Double t = new Rectangle2D.Double(x + 1, y + 51, BLOCK, BLOCK); // x, y+2
            Rectangle2D.Double m = new Rectangle2D.Double(x+1,y+26, BLOCK,BLOCK);
            Rectangle2D.Double b = new Rectangle2D.Double(x + 1, y + 1, BLOCK, BLOCK);
            Rectangle2D.Double s = new Rectangle2D.Double(x-24,y+1, BLOCK,BLOCK); // x-1,y

            g2.setColor(Color.CYAN);

            g2.fill(t);
            g2.fill(m);
            g2.fill(b);
            g2.fill(s);

            int tempx = x/width - 1; // 4
            int tempy = y/width - 1; // 0

            blockPosition = new int[][] {
                    {tempx,tempy+2},
                    {tempx,tempy+1},
                    {tempx,tempy},
                    {tempx-1,tempy}
            };
            lowestBlocks = new int[][] {
                    {tempx,tempy+2}, // t
                    {tempx-1,tempy}, // s
            };

            for (int z = 0; z < blockPosition.length; z++) {
                System.out.print(blockPosition[z][0]);
                System.out.print(' ');
                System.out.println(blockPosition[z][1]);
            }
        }
        else
        {
            Rectangle2D.Double t = new Rectangle2D.Double(x-24,y+26, BLOCK,BLOCK); // x-1,y+1
            Rectangle2D.Double m = new Rectangle2D.Double(x+1,y+26, BLOCK,BLOCK); // x,y+1
            Rectangle2D.Double b = new Rectangle2D.Double(x+26,y+26, BLOCK,BLOCK); // x+1,y+1
            Rectangle2D.Double s = new Rectangle2D.Double(x+26,y+1, BLOCK,BLOCK);

            g2.setColor(Color.CYAN);
            g2.fill(t);
            g2.fill(m);
            g2.fill(b);
            g2.fill(s);

            int tempx = x/width - 1; // 4
            int tempy = y/width - 1; // 0

            blockPosition = new int[][] {
                    {tempx-1,tempy+1},
                    {tempx,tempy+1},
                    {tempx+1,tempy+1},
                    {tempx+1,tempy}
            };
            lowestBlocks = new int[][] {
                    {tempx-1,tempy+1}, // t
                    {tempx,tempy+1}, // m
                    {tempx+1,tempy+1} // m
            };

            for (int z = 0; z < blockPosition.length; z++) {
                System.out.print(blockPosition[z][0]);
                System.out.print(' ');
                System.out.println(blockPosition[z][1]);
            }
        }
    }

    @Override
    public void move() {
        if (y < 18*width) //bottom of board. NEED COLLISION DETECTION THIS IS GOOD START BUT BAD
            y += width;
        if (dx == 1) {
            System.out.println("rightttttt");
            view_dx = dx;
        }
        else if (dx == -1) {
            System.out.println("lefttttt");
        }
    }

    @Override
    public void doAnimTracker(int one, int two, int three) {

    }

    @Override
    public void keyTracker(int req_dx, int req_dy) {
        dx = req_dx; this.dy = req_dy;
        System.out.println(dx);
        if (state == 0 && dx == -1)
            state = 3;
        else
            state = (state + dx) % 4;
        System.out.print("state = ");
        System.out.println(state);
    }

    @Override
    public int getPacman_x() {
        return 0;
    }

    @Override
    public int getPacman_y() {
        return 0;
    }

    @Override
    public int[][] getCurrentBlockPosition() {
        pBlockPosition = blockPosition.clone();
        return blockPosition;
    }

    @Override
    public int[][] getPreviousBlockPosition() {
        return pBlockPosition;
    }

    @Override
    public int[][] getLowestBlocks() {
        return lowestBlocks;
    }


    // return piece position to
    //public int[][] getblockPosition(){
    //    return blockPosition;
    //}
}
