import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.File;
import javax.imageio.ImageIO;

public class Board {
   
   Screen screen;
   Dimension squareDim;
   Dimension boardDim;
   BufferedImage boardImg;
   BufferedImage scaledBoardImg;
   int[] boardSize = {17, 15};
   int[][] boardArray = new int[boardSize[0]][boardSize[1]];
   int[] apple = new int[2];
   Set<String> snake = new HashSet<String>();
   
   public Board() throws AWTException {
      screen = new Screen();
      squareDim = screen.getSquareDim();
      boardDim = screen.getBoardDim();
      boardImg = screen.captureBoard();
      this.scaleBoardImg();
   }
   
   public void scaleBoardImg() {
      scaledBoardImg = toBufferedImage(boardImg.getScaledInstance(boardSize[0], boardSize[1], Image.SCALE_FAST));
   }
   
   public static BufferedImage toBufferedImage(Image img) {
      if (img instanceof BufferedImage)
         return (BufferedImage) img;
      BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
   
      Graphics2D bGr = bimage.createGraphics();
      bGr.drawImage(img, 0, 0, null);
      bGr.dispose();
   
      return bimage;
   }
   
   public void refreshBoard() throws Exception {
      boardImg = screen.captureBoard();
      // ImageIO.write(boardImg, "png", new File("screenshot.png"));
      this.scaleBoardImg();
      //ImageIO.write(scaledBoardImg, "png", new File("screenshot.png"));
   }
   
   public void refreshBoard(int i) throws Exception {
      boardImg = screen.captureBoard();
      ImageIO.write(boardImg, "png", new File("screenshot" + i + ".png"));
      // this.scaleBoardImg();
   }
   
   public int[] findApple() throws Exception {
      this.refreshBoard();
      int maxRed = -1;
      int red = -1;
      int rgb = -1;
      for (int i = 0; i < boardSize[0]; i++) {
         for (int j = 0; j < boardSize[1]; j++) {
            rgb = scaledBoardImg.getRGB(i,j);
            red = (rgb >> 16) & 0x000000FF;
            if (red > maxRed) {
               apple[0] = i;
               apple[1] = j;
               maxRed = red;
            }
         }
      }
      return apple;
   }
   
   public BufferedImage getBoardImage() {
      return boardImg;
   }
   
   public BufferedImage getScaledBoardImage() {
      return scaledBoardImg;
   }
   
   public int[] getApple() {
      return apple;
   }
   
   public boolean isColor(int x, int y, Color c, boolean scaled) {
      Color pixel;
      if (scaled)
         pixel = new Color(scaledBoardImg.getRGB(x,y));
      else
         pixel = new Color(boardImg.getRGB(x,y));
      return c.equals(pixel);
   }
   
   public void printColor(int x, int y) {
      Color pixel = new Color(boardImg.getRGB(x,y));
      System.out.println(pixel.getRed() + " " + pixel.getGreen() + " " + pixel.getBlue());
   }
   
   public Set<String> getSnake() {
      return snake;
   }
   
   Color c1 = new Color(162, 209, 73);
   Color c2 = new Color(170, 215, 81);
   Color c3 = new Color(169, 214, 81);
   Color c4 = new Color(161, 208, 73);
   
   public boolean isBackground(int i, int j, boolean scaled) {
      if (scaled) {
         int color = scaledBoardImg.getRGB(i, j);
            
         int blue = color & 0xff;
         int green = (color & 0xff00) >> 8;
         int red = (color & 0xff0000) >> 16;
         
         if (Math.abs(blue - 73) < 3 && Math.abs(green - 208) < 3 && Math.abs(red - 161) < 3)
            return true;
         if (Math.abs(blue - 81) < 3 && Math.abs(green - 214) < 3 && Math.abs(red - 170) < 3)
            return true;
         return false;
      }
      return false;
   }
         
   public void processScaledBoard() {
      snake.clear();
      boolean foundApple = false;
      for (int i = 0; i < boardSize[0]; i++) {
         for (int j = 0; j < boardSize[1]; j++) {
            if (isBackground(i, j, true)) {
               // System.out.println(i + " " + j);
               continue;
            }
            int color = scaledBoardImg.getRGB(i, j);
            
            int blue = color & 0xff;
            int green = (color & 0xff00) >> 8;
            int red = (color & 0xff0000) >> 16;
            
            if (red - blue > 150) {
               apple[0] = i; 
               apple[1] = j;
               foundApple = true;
            }
            
            // if (blue > 150 && green < 180)
            if (blue - green > 50 || (blue > 200))
               snake.add(i + " " + j);
         }
      }
      if (!foundApple) {
         apple[0] = -1;
         apple[1] = -1;
      }
      
      String appleString = apple[0] + " " + apple[1];
      if (snake.contains(appleString))
         snake.remove(appleString);
   }
   
}
   
   
      
      
