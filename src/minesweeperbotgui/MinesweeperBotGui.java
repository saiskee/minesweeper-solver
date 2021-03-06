package minesweeperbotgui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.omg.CORBA.SystemException;
import bot.BotTile;
import bot.GameState;
import bot.MinesweeperBotDriver;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class MinesweeperBotGui {
    private JFrame frame;
    public static MinesweeperBotBackground botbg;
    private Timer tmr;
    private int counter = 0;
    private Queue<String[][]> streams = new LinkedList<>();
    JButton btnRunSimulation;
    JLabel[][] grid;
    JButton btnStepbystepBot;
    public boolean initialized = false;
    private JLabel lblBotWon;
    Dimension scrollPaneSize;
    private JScrollPane scrollPane;
    private JPanel scrollPanePanel;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                  //  MinesweeperBotController controller = new MinesweeperBotController();
                    //MinesweeperBotController.main(args);
                    MinesweeperBotGui window = new MinesweeperBotGui();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MinesweeperBotGui() {
        initialize();
    }

    public void createTimer(JScrollPane pane) {
        btnRunSimulation.setEnabled(false);
        tmr = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botbg.botStep();

                if (!botbg.streamIsEmpty()) {
                    
                    populateJLabels(pane,botbg.bot.width, botbg.bot.height, botbg.getNextStream());

                } else {
                    tmr.stop();
                    return;
                }



                if (botbg.getState() == GameState.lose) {
                	JOptionPane.showMessageDialog(frame, "Game Over, You Lost :(" , "The Bot Hit a Mine!", JOptionPane.INFORMATION_MESSAGE);
                    scrollPane.revalidate();
                    scrollPane.repaint();
                    pane.revalidate();
                    pane.repaint();

                    btnRunSimulation.setEnabled(true);
                    btnStepbystepBot.setEnabled(true);

                } else if (botbg.getState() == GameState.win) {
                	JOptionPane.showMessageDialog(frame, "Game Over, You Won!" , "The Bot Won solved this Board (" + botbg.bot.height + " x " + botbg.bot.width + " tiles) ", JOptionPane.INFORMATION_MESSAGE);
                    scrollPane.revalidate();
                    scrollPane.repaint();
                    pane.revalidate();
                    pane.repaint();

                    btnRunSimulation.setEnabled(true);
                    btnStepbystepBot.setEnabled(true);

                }
            }

        });

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        frame = new JFrame();
        frame.setSize(1033, 1035);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 1011, 979);
        scrollPaneSize = scrollPane.getSize();
        scrollPane.setLayout(null);
        frame.getContentPane().add(scrollPane);
        
        btnRunSimulation = new JButton("Run Simulation");

        btnRunSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnRunSimulation.setEnabled(false);
                btnStepbystepBot.setEnabled(false);
                botbg = new MinesweeperBotBackground();
                btnRunSimulation.setEnabled(true);
                initialized = false;
                createTimer(scrollPane);
                tmr.start();  
               
            }
        });
        btnRunSimulation.setBounds(15, 16, 156, 42);
        
        scrollPane.add(btnRunSimulation);
        btnStepbystepBot = new JButton("Step-By-Step Bot");
        btnStepbystepBot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnRunSimulation.setEnabled(false);
                botbg = new MinesweeperBotBackground();
                initialized = false;
            }
        });
        btnStepbystepBot.setBounds(217, 16, 156, 42);
        scrollPane.add(btnStepbystepBot);
        
        JButton btnStep = new JButton(">>");
        btnStep.setBounds(385, 23, 61, 29);
        scrollPane.add(btnStep);
        
        
        btnStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                botbg.botStep();

                

                if (!botbg.streamIsEmpty()) {
                    populateJLabels(scrollPane,botbg.bot.width, botbg.bot.height, botbg.getNextStream());

                } else {
                   // tmr.stop();
                    return;
                }



                if (botbg.getState() == GameState.lose) {
                	JOptionPane.showMessageDialog(frame, "The Bot Hit a Mine", "Game Over!" ,  JOptionPane.INFORMATION_MESSAGE);
                    scrollPane.revalidate();
                    scrollPane.repaint();

                    btnRunSimulation.setEnabled(true);
                    btnStepbystepBot.setEnabled(true);

                } else if (botbg.getState() == GameState.win) {
                	JOptionPane.showMessageDialog(frame, "The Bot Won solved this Board (" + botbg.bot.height + " x " + botbg.bot.width + " tiles) ", "Game Over, You Won!"  , JOptionPane.INFORMATION_MESSAGE);
                    scrollPane.revalidate();
                    scrollPane.repaint();

                    btnRunSimulation.setEnabled(true);
                    btnStepbystepBot.setEnabled(true);

                }
            }
            
        
        });



    }

    private void populateJLabels(JScrollPane pane,int width, int height, String[][] board) {
        int x = 0;
        int y = 100;
        int labelSize = 35;
        
        grid = new JLabel[height][width];

        if (initialized) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Component[] a = pane.getComponents();
                    for (Component b : a) {
                        if (b instanceof JLabel) {
                            pane.remove(b);
                        }
                    }
                }
            }
        } 
        else {
            initialized = true;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                String labelString = board[i][j];

                JLabel label = new JLabel();
                if (labelString.equals("💥")) {
                    if(botbg.bot.getBotTile(j,i).isFlag()) {
                       
                        label.setBackground(new Color(234, 112, 112));
                        label.setOpaque(true);
                        label.setForeground(Color.WHITE);
                        labelString = "⛿";
                        label.setOpaque(true);
                    }else {
                    label.setBackground(new Color(255, 210, 127));
                    label.setForeground(new Color(204, 108, 34));
                    label.setOpaque(true);
                    }
                }
                else if (labelString.contains("CURRENTMOVE")) {
                    label.setBackground(Color.PINK);
                    label.setOpaque(true);
                    label.setForeground(Color.ORANGE);
                    labelString = Character.toString(labelString.charAt(0));
                } 
                else if (labelString.contains("losingtilexx249fs")){
                    label.setBackground(Color.RED);
                    label.setOpaque(true);
                    label.setForeground(Color.ORANGE);
                    labelString = "💥";
                }
                else if (labelString.equals(" ")) { // not yet discovered
                
                    labelString = " ";
                    label.setBackground(new Color(107, 108, 114));
                    label.setOpaque(true);
                    
                } else if (labelString.equals("0")) {
                    label.setBackground(Color.LIGHT_GRAY);
                    label.setOpaque(true);
                    labelString = " ";

                } else if (labelString.equals("1")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(Color.BLUE);
                } else if (labelString.equals("2")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(new Color(87, 153, 44));
                } else if (labelString.equals("3")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(Color.RED);
                } else if (labelString.equals("4")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(new Color(37, 48, 132));
                } else if (labelString.equals("5")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(new Color(150, 30, 30));
                } else if (labelString.equals("6")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(Color.cyan);
                } else if (labelString.equals("7")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(Color.DARK_GRAY);
                } else if (labelString.equals("8")) {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                    label.setForeground(Color.GRAY);
                } else if (labelString.equals("⛿")) {
                    label.setBackground(new Color(234, 112, 112));
                    label.setOpaque(true);
                    label.setForeground(Color.WHITE);

                    label.setOpaque(true);
                }
                BotTile tile = botbg.bot.getBotTile(j,i);
                String toolTipText = generateToolTipTextForBotTile(tile);
                label.setToolTipText(toolTipText);
                label.setText(labelString);
                label.setBounds(x, y, labelSize, labelSize); // 40,40
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                grid[i][j] = label;
                pane.add(label);
                x += labelSize +(labelSize*0.375);
                
            }
            x = 0;
            y += labelSize + (labelSize*0.375);

        }
        pane.revalidate();
        pane.repaint();
    }

    

    private int calculateLabelHeight() {
        int height = (int) (scrollPaneSize.getHeight()-300);
        int verticalTiles = botbg.bot.height;
        height = (height - (verticalTiles * 10))/verticalTiles;
        return height;
        
        
    }
    private int calculateLabelWidth() {
        int width = (int) (scrollPaneSize.getWidth());
        int horizontalTiles = botbg.bot.width;
        width = (width - (horizontalTiles * 10))/horizontalTiles;
        return width;
        
    }

    private String generateToolTipTextForBotTile(BotTile tile) {
        String[] neighbors = botbg.bot.getBotTile(tile.x, tile.y).neighbors;
        String result = "<html>";
        result +="<b><u>Bot Analysis ("+tile.x+","+tile.y+"):</u></b> <br>";
        if (tile.isDiscovered()) {
            result += "<b>Tile value:</b>" + tile.getValue();
            result += "<br><b>Neighbors:</b>";
            result += "<br> " + neighbors[0] +" | " +neighbors[1] + " | " +neighbors[2] + "<br>";
            result += "----+------+-----";
            result += "<br> " + neighbors[3] +" | &nbsp;&nbsp;" +tile.getValue() + " &nbsp;&nbsp;| " +neighbors[4] + "<br>";
            result += "----+------+----";
            result += "<br> " + neighbors[5] +" | " +neighbors[6] + " | " +neighbors[7] + "<br>";
            
        }else if(tile.isFlag()){
           
                
                    result += "<b>Tile is ⛿Flag⛿</b>";
                    result += "<br><b>Neighbors:</b>";
                    result += "<br> " + neighbors[0] +" | " +neighbors[1] + " | " +neighbors[2] + "<br>";
                    result += "----+------+-----";
                    result += "<br> " + neighbors[3] +" |&nbsp;&nbsp;⛿&nbsp;  | " +neighbors[4] + "<br>";
                    result += "----+------+----";
                    result += "<br> " + neighbors[5] +" | " +neighbors[6] + " | " +neighbors[7] + "<br>";
                    ;
        }else {
            result += "Tile is <u>covered</u> (Chance: " +tile.getChance()+"%)";
            result += "<br><b>Neighbors:</b>";
            result += "<br> " + neighbors[0] +" | " +neighbors[1] + " | " +neighbors[2] + "<br>";
            result += "-----+-----+----";
            result += "<br> " + neighbors[3] +" | &emsp;&emsp;| " +neighbors[4] + "<br>";
            result += "----+------+----";
            result += "<br> " + neighbors[5] +" | " +neighbors[6] + " | " +neighbors[7] + "<br>";
            
        }
        result += "<b>Undiscovered Neighbors: </b>" + tile.amountOfUndiscoveredNeighbors +"<br>";
        result += "<b>Neighboring Flags: </b>" + tile.getSurroundingFlags();
        result += "</html>";
        return result;
    }
}
