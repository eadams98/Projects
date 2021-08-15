import java.awt.Graphics2D;
import java.io.IOException;

/**
   A shape that can be moved around.
*/
public interface MoveableShape
{
   void draw (Graphics2D g2) throws IOException;

   void move ();					//   void translate (int dr, int dc);

   boolean contains (int r, int c);

}
