package minesweeperbotgui;

import bot.GameState;
import bot.MinesweeperBot;

public class MinesweeperBotBackground {

      public MinesweeperBot bot;
      
      public MinesweeperBotBackground() {
          bot = new MinesweeperBot(45,25,75);
          bot.setGuiDiagnostics(true);
          //bot.setDiagnostics(true);
      }
      public MinesweeperBotBackground(int x, int y, int mines) {
          bot = new MinesweeperBot(x,y,mines);
          bot.setGuiDiagnostics(true);
          //bot.setDiagnostics(true);
      }
      
      public void botStep() {
          GameState state = bot.getState();
          if (state == GameState.running) {
              bot.playGame();
          }
      }
      public boolean streamIsEmpty() {
          return bot.printStream.isEmpty();
      }
      
      public String[][] getNextStream() {
          return bot.printStream.remove();
      }
      public void stream() {
          System.out.println(bot.printStream.remove());
      }
      public GameState getState() {
          return bot.getState();
      }
}
