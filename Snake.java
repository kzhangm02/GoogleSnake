import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

public class Snake {

   public static void main(String[] args) throws Exception {
      Thread.sleep(3000);
      
      Board board = new Board();
      BufferedImage boardImage = board.getBoardImage();
      board.scaleBoardImg();
      // ImageIO.write(board.getScaledBoardImage(), "png", new File("screenshot.png"));
      
      SnakeBot sb = new SnakeBot();
      sb.play();
   }

}