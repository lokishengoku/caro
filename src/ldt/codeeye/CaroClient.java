package ldt.codeeye;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class CaroClient extends JFrame implements MouseListener, Runnable {
  public static void main(String[] args) {
    new CaroClient();
    new CaroClient();
  }

  int n = 15;
  int s = 30;
  int os = 50;
  ArrayList<Point> checkedList = new ArrayList<>();
  DataOutputStream dos;
  DataInputStream dis;

  public CaroClient() {
    this.setTitle("Co CARO");
    this.setSize(n * s + os * 2, n * s + os * 2);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.addMouseListener(this);

    try {
      Socket soc = new Socket("118.71.133.23", 80);
      dos = new DataOutputStream(soc.getOutputStream());
      dis = new DataInputStream(soc.getInputStream());
      new Thread(this).start();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    this.setVisible(true);
  }


  public void paint(Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setColor(Color.BLACK);
    for (int i = 0; i <= n; i++) {
      g.drawLine(os, os + i * s, os + n * s, os + i * s);
      g.drawLine(os + i * s, os, os + i * s, os + n * s);
    }

    g.setFont(new Font("arial", Font.BOLD, s));
    for (int i = 0; i < checkedList.size(); i++) {
      int ix = checkedList.get(i).x;
      int iy = checkedList.get(i).y;
      int x = os + ix * s + s / 7;
      int y = os + iy * s + s - s / 7;
      String str = "X";
      g.setColor(Color.RED);
      if (i % 2 == 1) {
        g.setColor(Color.BLUE);
        str = "O";
      }
      g.drawString(str, x, y);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    if (x < os || x >= os + n * s) return;
    if (y < os || y >= os + n * s) return;
    int ix = (x - os) / s;
    int iy = (y - os) / s;

    try {
      dos.writeUTF(ix + "");
      dos.writeUTF(iy + "");
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
       int ix = Integer.parseInt(dis.readUTF());
       int iy = Integer.parseInt(dis.readUTF());
       checkedList.add(new Point(ix, iy));
       repaint();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }
}
