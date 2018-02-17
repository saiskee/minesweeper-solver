package bot;
import java.util.Scanner;
import minesweeperbotgui.MinesweeperBotGui;

public class MinesweeperBotDriver {
    
    static int trials = 10;
    public MinesweeperBotDriver() {
        
    }
    public static void main(String[] args) {
       
        
      //  int[] loseToWinRatio = new int[trials];
       // for (int i = 0; i < trials; i++) {
            int win = 0;
            int lose = 0;
        //while(win == 0) {
            MinesweeperBot bot = new MinesweeperBot();
          bot.setDiagnostics(true);
        while (bot.getState() == GameState.running) {
            bot.playGame();
        
        
        } 
        if (bot.getState() == GameState.win) {
            win++;
        }
        else if (bot.getState() == GameState.lose){
            lose++;
        }
        while(!bot.printStream.isEmpty()) {
            System.out.println(bot.printStream.remove());
        }
        bot = null;
        
      // System.out.println("Losses: " + lose + " Wins: " + win);
       //     loseToWinRatio[i] = lose;
        //}
    
   // int sum = 0;
    
   //for (int a : loseToWinRatio) {
   //     sum+= a;
  // }
   System.out.println("Computation Done");
   
  // System.out.println("MinesweeperBot Win to Loss Ratio: " +(double)sum/trials);
   }
}
