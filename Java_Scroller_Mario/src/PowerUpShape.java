import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// FUTURE WORK:
//      - either make this class a interface (multiple powerups with different effects or pass pic and string in constructor
//

public class PowerUpShape implements MoveableShape{

    // Initizialize all variables
    //      Power up speed and location variables
    private int BLOCK_SIZE;
    private int dx, dy;
    private static int item_speed = 1;
    private short screenData[][];

    private int view_dx = -1;
    private int view_dy = 0;
    private int req_dx = 0;
    private int req_dy = 0;
    private int item_x, item_y;
    private int player_dx = 0;
    private int player_dy = 0;
    private String pathname;

    private final static int GRASS = 1;
    private final static int PIPE = 5;
    private final static int BLOCK = 3;
    private final static int MYS_BLOCK = 4;

    private BufferedImage powerUp;

    PowerUpShape (short screenData[][], int brd_x, int brd_y, int req_dx, int req_dy, int width, String powerUpPathname) {
        this.screenData = screenData;
        this.item_x = brd_x * width;
        this.item_y = brd_y * width;
        this.BLOCK_SIZE = width;
        //this.PowerUp = PowerUp;
        this.req_dx = req_dx;
        this.req_dy = req_dy;
        this.pathname = powerUpPathname;

        Image powerUpStaging = new ImageIcon(this.pathname).getImage();
        powerUpStaging = AddTransparency.TransformColorToTransparency(AddTransparency.toBufferedImage(powerUpStaging), new Color(247, 248, 255), new Color(255, 255, 255));
        powerUp =  AddTransparency.toBufferedImage(powerUpStaging);

        File outFile2 = new File("gamepix/powerup/mushroomTrans.png");
        try {
            ImageIO.write(powerUp, "PNG", outFile2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) throws IOException {
        g2.drawImage(powerUp, item_x, item_y, null);

        //AddTransparency.resize(this.pathname, "gamepix/powerup/transScaled.png", 22, 22);
        //Image temp = new ImageIcon("gamepix/powerup/transScaled.png").getImage();
        //g2.drawImage(temp, item_x, item_y, null);
    }

    @Override
    public void move() {
        int oneBlockDown = (item_y/BLOCK_SIZE) + 1;
        if(     item_y != 176 && // not on ground
                screenData[item_x/BLOCK_SIZE][oneBlockDown] == 2
        ) { player_dy = 2; }
        else { player_dy = 0; }

        //System.out.println("item y: " + screenData[item_x/BLOCK_SIZE][item_y/BLOCK_SIZE]);
        //move toward player
        item_x = item_x + item_speed * 1;
        item_y = item_y + item_speed * player_dy;

    }

    @Override
    public boolean contains(int r, int c) {
        return false;
    }
}
