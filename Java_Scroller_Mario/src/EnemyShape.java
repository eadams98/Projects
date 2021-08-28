import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnemyShape implements MoveableShape {
    // Initizialize all variables
    //      enemy speed and location variables
    private int BLOCK_SIZE;
    private int dx, dy;
    private static int enemy_speed = 1;
    private short screenData[][];

    private int view_dx = -1;
    private int view_dy = 0;
    private int req_dx = 0;
    private int req_dy = 0;
    private int player_x, player_y;
    private int player_dx = 0;
    private int player_dy = 0;

    //      enemy images
    //Image KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby1-2.png").getImage();
    private BufferedImage[][] EnemyAnimationArray = new BufferedImage[2][3];


    //      enemy varables
    boolean facingPlayer = true;
    int enemyMoveIdx = -1;

    // Constructor
    public EnemyShape(short screenData[][], int brd_x, int brd_y, int req_dx, int req_dy, int width, boolean PowerUp) {
        this.screenData = screenData;
        this.player_x = brd_x * width;
        this.player_y = brd_y * width;
        this.BLOCK_SIZE = width;
        //this.PowerUp = PowerUp;
        this.req_dx = req_dx;
        this.req_dy = req_dy;

        //Image KirbyRightWalkAnimation = new ImageIcon("gamepix/Kirby1-2.png").getImage();
        //KirbyRightWalkAnimation = TransformColorToTransparency( toBufferedImage(KirbyRightWalkAnimation) );
        //KirbyRightWalkArray[0][0] = imageToBufferedImage(KirbyRightWalkAnimation, KirbyRightWalkAnimation.getWidth(null), KirbyRightWalkAnimation.getHeight(null));

        //stationary
        Image enemy = new ImageIcon("gamepix/enemy/enemyLeft.png").getImage();
        enemy = AddTransparency.TransformColorToTransparency(AddTransparency.toBufferedImage(enemy), new Color(61, 70, 72), new Color(125, 145, 151));
        EnemyAnimationArray[0][0] = AddTransparency.toBufferedImage(enemy);
    }


    @Override
    public void draw(Graphics2D g2) throws IOException {
        AddTransparency.resize("gamepix/enemy/trans.png", "gamepix/enemy/transScaled.png", 22, 22);
        Image temp = new ImageIcon("gamepix/enemy/transScaled.png").getImage();
        g2.drawImage(temp, player_x, player_y, null);
        //g2.drawImage(EnemyAnimationArray[0][0], null, 0, 0);

        File outFile2 = new File("gamepix/enemy/trans.png");
        try {
            ImageIO.write(EnemyAnimationArray[0][0], "PNG", outFile2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("ENEMY Y: " + EnemyAnimationArray[0][0].getWidth());
        System.out.println(player_x + ":x y: " + player_y);
    }

    public void getPlayer_x(int player_pos) { //player pos / which direction enemy shoud go
        if (player_pos > player_x) {
            player_dx = -1;
        } else if (player_pos < player_x) {
            player_dx = 1;
        } else {
            player_dx = 0;
        }
    }

    @Override
    public void move() {

        if (Math.random() > .8) {
            //move toward player
            player_x = player_x + enemy_speed * player_dx;
            player_y = player_y + enemy_speed * player_dy;
        } else {
            //move away from player
            player_x = player_x + enemy_speed * -player_dx;
            player_y = player_y + enemy_speed * player_dy;
        }

    }

    @Override
    public boolean contains(int x, int y) {
        //int lowX = x, highX = x + 22; // each sprite is 22px
        //int lowY = y, highY = y + 22;
        //System.out.println(" block: " +  y / BLOCK_SIZE + " CEIL: " + Math.ceil(y / BLOCK_SIZE));
        if ( (Math.ceil(player_y / BLOCK_SIZE) == Math.ceil((double)y / BLOCK_SIZE)) && (
                (player_x / BLOCK_SIZE == x / BLOCK_SIZE) || // RIGHT TOUCH
                (Math.ceil((double) player_x / BLOCK_SIZE) == Math.ceil((double) x / BLOCK_SIZE)))) // LEFT TOUCH
        {
            System.out.println("dead");
            return true;
        } else {
            return false;
        }
    }

    public int getPlayer_y() {
        //starts 187, one block up is 170, 153
        return player_y;
    }

    public int getPlayer_x() {
        //starts 187, one block up is 170, 153
        return player_x;
    }
}
