import java.awt.*;

public interface MoveableShape {

    void draw (Graphics2D g2);

    void move (); // void translate (int dr, int dc);

    // boolean contains (int r, int c);

    void doAnimTracker(int one, int two, int three);

    void keyTracker(int req_dx, int req_dy);

    int getPacman_x(); // get rid

    int getPacman_y(); // get rid

    int[][] getCurrentBlockPosition();
    int[][] getPreviousBlockPosition();
    int[][] getLowestBlocks();

}
