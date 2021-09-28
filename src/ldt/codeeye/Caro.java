package ldt.codeeye;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Caro extends JFrame implements ActionListener {
  private final int column = 30;
  private final int row = 20;
  private final JButton[][] buttons = new JButton[column + 2][row + 2];
  private final boolean[][] isChecked = new boolean[column + 2][row + 2];
  private final int[][] winPosition = new int[5][2];
  private boolean xTurn = true;

  private final Color bgColor = Color.WHITE;
  private final Color xColor = Color.RED;
  private final Color oColor = Color.BLUE;
  Container container;
  JPanel topPanel, bottomPanel;
  JLabel notificationLabel;
  JButton buttonNewGame, buttonExit;

  public Caro(String title) throws HeadlessException {
    super(title);
    System.out.println(">>> Starting...");
    container = this.getContentPane();
    this.setupBottomPanel();
    this.setupTopPanel();
    System.out.println(">>> Displaying GUI...");
    this.setVisible(true);
    this.setSize(1400, 900);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void setupTopPanel() {
    System.out.println(">>> Setting up top panel...");
    topPanel = new JPanel();
    notificationLabel = new JLabel("X First");
    buttonNewGame = new JButton("New Game");
    buttonExit = new JButton("Exit");
    buttonNewGame.addActionListener(this);
    buttonExit.addActionListener(this);
    topPanel.setLayout(new FlowLayout());
    topPanel.add(notificationLabel);
    topPanel.add(buttonNewGame);
    topPanel.add(buttonExit);
    container.add(topPanel, "North");
  }

  private void setupBottomPanel() {
    System.out.println(">>> Setting up bottom panel...");
    bottomPanel = new JPanel();
    bottomPanel.setLayout(new GridLayout(row, column));
    for (int i = 0; i <= column + 1; i++) {
      for (int j = 0; j <= row + 1; j++) {
        buttons[i][j] = new JButton(" ");
        buttons[i][j].setActionCommand(i + " " + j);
        buttons[i][j].setBackground(bgColor);
        buttons[i][j].addActionListener(this);
        isChecked[i][j] = false;
      }
    }
    for (int i = 1; i <= row; i++) {
      for (int j = 1; j <= column; j++) {
        bottomPanel.add(buttons[j][i]);
      }
    }
    container.add(bottomPanel);
  }

  private boolean checkWin(int currentColumn, int currentRow) {
    System.out.println(">>> Checking...");
    int scoreCount = 0, cRun = currentColumn, rRun = currentRow;
    String currentText = buttons[currentColumn][currentRow].getText();
    // SECTION: check row
    // check right side
    while (cRun < column && buttons[cRun][currentRow].getText().equals(currentText)){
      winPosition[scoreCount][0] = cRun;
      winPosition[scoreCount][1] = currentRow;
      scoreCount++;
      cRun++;
    }
    // check left side
    cRun = currentColumn - 1;
    while (cRun >= 0 && buttons[cRun][currentRow].getText().equals(currentText)) {
      winPosition[scoreCount][0] = cRun;
      winPosition[scoreCount][1] = currentRow;
      scoreCount++;
      cRun--;
    }
    if (scoreCount > 4) return true;

    // SECTION: check column
    scoreCount = 0;
    // check top side
    while (rRun < row && buttons[currentColumn][rRun].getText().equals(currentText)){
      winPosition[scoreCount][0] = currentColumn;
      winPosition[scoreCount][1] = rRun;
      scoreCount++;
      rRun++;
    }
    // check bottom side
    rRun = currentRow - 1;
    while (rRun >= 0 && buttons[currentColumn][rRun].getText().equals(currentText)) {
      winPosition[scoreCount][0] = currentColumn;
      winPosition[scoreCount][1] = rRun;
      scoreCount++;
      rRun--;
    }
    if (scoreCount > 4) return true;

    // SECTION: check diagonal line \
    cRun = currentColumn;
    rRun = currentRow;
    scoreCount = 0;
    while (cRun < column && rRun < row && buttons[cRun][rRun].getText().equals(currentText)) {
      winPosition[scoreCount][0] = cRun;
      winPosition[scoreCount][1] = rRun;
      scoreCount ++;
      cRun++;
      rRun++;
    }
    cRun = currentColumn - 1;
    rRun = currentRow - 1;
    while (cRun >= 0 && rRun >= 0 && buttons[cRun][rRun].getText().equals(currentText)) {
      winPosition[scoreCount][0] = cRun;
      winPosition[scoreCount][1] = rRun;
      scoreCount++;
      cRun--;
      rRun--;
    }
    if (scoreCount > 4) return true;
    // SECTION: check diagonal line /
    cRun = currentColumn;
    rRun = currentRow;
    scoreCount = 0;
    while (cRun < column && rRun >= 0 && buttons[cRun][rRun].getText().equals(currentText)) {
      winPosition[scoreCount][0] = cRun;
      winPosition[scoreCount][1] = rRun;
      scoreCount ++;
      cRun++;
      rRun--;
    }
    cRun = currentColumn - 1;
    rRun = currentRow + 1;
    while (cRun >= 0 && rRun < row && buttons[cRun][rRun].getText().equals(currentText)) {
      winPosition[scoreCount][0] = cRun;
      winPosition[scoreCount][1] = rRun;
      scoreCount++;
      cRun--;
      rRun++;
    }
    return scoreCount > 4;
  }

  private void addPoint(int clickedColumn, int clickedRow) {
    System.out.println(">>> Adding point...");
    if (xTurn) {
      buttons[clickedColumn][clickedRow].setText("X");
      buttons[clickedColumn][clickedRow].setForeground(xColor);
      notificationLabel.setText("O Turn");
    } else {
      buttons[clickedColumn][clickedRow].setText("O");
      buttons[clickedColumn][clickedRow].setForeground(oColor);
      notificationLabel.setText("X Turn");
    }
    buttons[clickedColumn][clickedRow].setBackground(Color.LIGHT_GRAY);
    isChecked[clickedColumn][clickedRow] = true;
    xTurn = !xTurn;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "New Game" -> {
        System.out.println(">>> New game clicked");
        new Caro("Cờ Caro");
        this.dispose();
      }
      case "Exit" -> {
        System.out.println(">>> Exit clicked");
        System.exit(0);
      }
      default -> {
        String cmd = e.getActionCommand();
        System.out.println(">>> Choosing position..." + cmd);
        int k = cmd.indexOf(32);
        int i = Integer.parseInt(cmd.substring(0, k));
        int j = Integer.parseInt(cmd.substring(k + 1));
        if (!isChecked[i][j]) {
          addPoint(i, j);
        }
        if (checkWin(i, j)) {
          notificationLabel.setBackground(Color.MAGENTA);
          notificationLabel.setText(buttons[i][j].getText() + " WIN");
          for (i = 1; i <= column; i++)
            for (j = 1; j <= row; j++)
              buttons[i][j].setEnabled(false);
          for (i = 0; i < 5; i++) {
            buttons[winPosition[i][0]][winPosition[i][1]].setEnabled(true);
            buttons[winPosition[i][0]][winPosition[i][1]].setBackground(xTurn ? oColor : xColor);
            buttons[winPosition[i][0]][winPosition[i][1]].setForeground(Color.WHITE);
          }
          buttonNewGame.setBackground(Color.GREEN);
        }
      }
    }
  }
}
