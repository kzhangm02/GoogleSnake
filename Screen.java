import java.lang.*;
import java.awt.*;
import java.awt.image.*;

public class Screen {
   
   Robot robot;
   DisplayMode mode;
   Dimension screenDim;
   Rectangle screen;
   Dimension squareDim;
   Dimension boardDim;
   Rectangle board;
   
   Color corner = new Color(170, 215, 81);
   int[] boardSize = {17,15};
   
   public Screen() throws AWTException {
      robot = new Robot();
      mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
      screenDim = new Dimension(mode.getWidth(), mode.getHeight());
      screen = new Rectangle(screenDim);
      this.findBoard();
   }
   
   public void findBoard() {
      BufferedImage screenShot = captureScreen();
      int screenHeight = screenDim.height;
      int screenWidth = screenDim.width;
      int[][] boardCorners = new int[2][2];
      for (int i = 0; i < screenHeight; i ++) {
         for (int j = 0; j < screenWidth; j++) {
            if (isColor(j, i, corner, screenShot) && boardCorners[0][0] == 0 && boardCorners[0][1] == 0) {
               boardCorners[0][0] = j;
               boardCorners[0][1] = i;
            }
            if (isColor(screenWidth-j-1, screenHeight-i-1, corner, screenShot) && boardCorners[1][0] == 0 && boardCorners[1][1] == 0) {
               boardCorners[1][0] = screenWidth-j-1;
               boardCorners[1][1] = screenHeight-i-1;
            }
         }
      }
      
      int tempBoardWidth = boardCorners[1][0] - boardCorners[0][0];
      int tempBoardHeight = boardCorners[1][1] - boardCorners[0][1];
      int squareWidth = (int) Math.round( (double) tempBoardWidth / boardSize[0]);
      int squareHeight = (int) Math.round( (double) tempBoardHeight / boardSize[1]);
      int finalBoardWidth = squareHeight * boardSize[0];
      int finalBoardHeight = squareHeight * boardSize[1];
      
      squareDim = new Dimension(squareWidth, squareHeight);
      boardDim = new Dimension(finalBoardWidth, finalBoardHeight);
      board = new Rectangle(boardCorners[0][0] - 1, boardCorners[0][1], boardDim.width, boardDim.height);
   }
   
   public boolean isColor(int x, int y, Color c, BufferedImage image) {
      Color pixel = new Color(image.getRGB(x,y));
      return c.equals(pixel);
   }
   
   public BufferedImage captureScreen() {
      return robot.createScreenCapture(screen);
   }
   
   public BufferedImage captureBoard() {
      return robot.createScreenCapture(board);
   }
   
   public Dimension getSquareDim() {
      return squareDim;
   }
   
   public Dimension getBoardDim() {
      return boardDim;
   }

}