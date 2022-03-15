import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.imageio.ImageIO;

public class SnakeBot {
   
   Robot robot;
   Board board;
   int[] boardSize;
   int[] apple;
   Set<String> snake;
   Set<String> tempSnake;
   
   int[] head;
   int dir;
   boolean start;
   boolean ateApple;
   
   public SnakeBot() throws AWTException {
      robot = new Robot();
      board = new Board();
      boardSize = new int[2];
      boardSize[0] = 17;
      boardSize[1] = 15;
      apple = new int[2];
      snake = new HashSet<String>();
      tempSnake = new HashSet<String>();
      
      head = new int[2];
      head[0] = 3;
      head[1] = 7;
      dir = 0;
      start = true;
      ateApple = false;
   }
   
   public void play() throws Exception {
      robot.keyPress(KeyEvent.VK_RIGHT);
      int count = 0;
      int ss = 10;
      int shots = 0;
      long begin;
      long end;
      while (true) {
         begin = System.currentTimeMillis();
         board.refreshBoard();
         board.processScaledBoard();
         tempSnake = board.getSnake();
         if (start) {
            apple = board.getApple();
            snake = board.getSnake();
            start = false;
         }
         if (head[0] == apple[0] && head[1] == apple[1]) {
            ateApple = true;
            apple = board.getApple();
         }
         else
            ateApple = false;
         
         String nextHead = this.nextHead();
         if (tempSnake.contains(nextHead)) {
            snake = new HashSet<String>(tempSnake);
         }
         
         if (!snake.contains(head[0] + " " + head[1])) {
            System.out.println("bad");
            System.out.println(Arrays.toString(head));
            System.out.println(String.join(", ", snake));
            
            if (shots < ss) {
               ImageIO.write(board.getBoardImage(), "png", new File("screenshot" + shots + ".png"));
               ImageIO.write(board.getScaledBoardImage(), "png", new File("scaledscreenshot" + shots + ".png"));
               shots += 1;
            }
         }
         // ImageIO.write(board.getScaledBoardImage(), "png", new File("scaledscreenshot" + count + ".png"));
            
         // String nextHead = this.nextHead();
         // System.out.println(nextHead);
         if (snake.contains(nextHead)) {
            
            String[] arr = nextHead.split(" ");
            head[0] = Integer.parseInt(arr[0]);
            head[1] = Integer.parseInt(arr[1]);
            this.move(ateApple);
         }
         count += 1;
         end = System.currentTimeMillis();
         // System.out.println(end - begin);
      }
   }
   
   public String nextHead() {
      int[] newHead = new int[2];
      newHead[0] = head[0];
      newHead[1] = head[1];
      if (dir == 0 || dir == 3)
         newHead[dir % 2] += 1;
      else
         newHead[dir % 2] -= 1;
      return newHead[0] + " " + newHead[1];
   }
   
   public void move(boolean ateApple) throws Exception {
      double[] heuristics = new double[4];
      heuristics[0] = heuristic(head[0] + 1, head[1], dir, 0, apple);
      heuristics[1] = heuristic(head[0], head[1] - 1, dir, 1, apple);
      heuristics[2] = heuristic(head[0] - 1, head[1], dir, 2, apple);
      heuristics[3] = heuristic(head[0], head[1] + 1, dir, 3, apple);
      heuristics[(dir + 2) % 4] = -9999;
      // System.out.println(Arrays.toString(heuristics));
      int choice = argMax(heuristics);
      // System.out.println(choice);
      boolean turn = ((choice % 2) != (dir % 2));
      // robot.keyPress(dirMap.get(choice));
      // robot.keyPress(KeyEvent.VK_DOWN);
      if (!ateApple)
         Thread.sleep(20);
      if (choice == 0) {
         // System.out.println("right");
         robot.keyPress(KeyEvent.VK_RIGHT);
      }
      else if (choice == 1) {
         // System.out.println("up");
         robot.keyPress(KeyEvent.VK_UP);
      }
      else if (choice == 2) {
         // System.out.println("left");
         robot.keyPress(KeyEvent.VK_LEFT);
      }
      else if (choice == 3) {
         // System.out.println("down");
         robot.keyPress(KeyEvent.VK_DOWN);
      }
      dir = choice;
   }
   
   public double heuristic(int xHead, int yHead, int prevDir, int newDir, int[] apple) {
      int manhattenDist;
      if (apple[0] == -1 && apple[1] == -1)
         manhattenDist = 0;
      else
         manhattenDist = Math.abs(apple[0] - xHead) + Math.abs(apple[1] - yHead);
         
      double turn;
      if (prevDir % 2 == newDir % 2)
         turn = 0.5;
      else
         turn = 0;
         
      int cross;
      if (snake.contains(xHead + " " + yHead))
         cross = -999;
      else
         cross = 0;
         
      int edge;
      if (xHead < 0 || xHead >= boardSize[0] || yHead < 0 || yHead >= boardSize[1])
         edge = -999;
      else
         edge = 0;
         
      double snakeDist;
      int x;
      int y;
      String[] arr;
      int sum = 0;
      double count = 0;
      for (String s : snake) {
         arr = s.split(" ");
         x = Integer.parseInt(arr[0]);
         y = Integer.parseInt(arr[1]);
         sum += (xHead - x + yHead - y);
         count += 1;
      }
      snakeDist = sum / count;
      
      double score = -manhattenDist + turn + cross + edge + 0.01*snakeDist;
      return score;
   }
   
   public static int argMax(double[] arr) {
      int maxIdx = 0;
      for (int i = 0; i < arr.length; i++)
         if (arr[i] > arr[maxIdx])
            maxIdx = i;
      return maxIdx;
   }
   
}
      
      
      