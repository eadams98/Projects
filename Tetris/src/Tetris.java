import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    public Tetris() {
        initUI();
    }

    private void initUI() {
        add (new Board());

        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main (String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tetris tt = new Tetris();
                tt.setVisible(true);
            }
        });
    }

}
