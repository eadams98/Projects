import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TShape implements MoveableShape {


    private int BLOCK = 24;
    private int x, y;
    private int dx, dy;
    private int LShape_x, LShape_y;
    private int width;


    public TShape (int brd_x, int brd_y, int width)
    {
        this.x = brd_x * width;
        this.y = brd_y * width;
        this.width = width;
    }

    public void draw(Graphics2D g2)
    {
        Rectangle2D.Double bl = new Rectangle2D.Double(x-24,y+1, BLOCK,BLOCK);
        Rectangle2D.Double bm = new Rectangle2D.Double(x+1,y+1, BLOCK,BLOCK);
        Rectangle2D.Double br = new Rectangle2D.Double(x+26,y+1, BLOCK,BLOCK);
        Rectangle2D.Double tm = new Rectangle2D.Double(x+1,y-24, BLOCK,BLOCK);

        g2.setColor(Color.red);
        g2.fill(bl);
        g2.fill(bm);
        g2.fill(br);
        g2.fill(tm);
        //g2.setStroke(new BasicStroke(1));
        //g2.drawLine(BORDER_SIZE, BORDER_SIZE+(line*BLOCK_SIZE), SCREEN_SIZE_W, BORDER_SIZE+(line*BLOCK_SIZE));
    }

    @Override
    public void move() {

    }

    @Override
    public void doAnimTracker(int one, int two, int three) {

    }

    @Override
    public void keyTracker(int req_dx, int req_dy) {

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
        return new int[0][];
    }

    @Override
    public int[][] getPreviousBlockPosition() {
        return new int[0][];
    }

    @Override
    public int[][] getLowestBlocks() {
        return new int[0][];
    }


}
