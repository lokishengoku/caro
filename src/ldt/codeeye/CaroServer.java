package ldt.codeeye;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class CaroServer {
  public static void main(String[] args) {
    new CaroServer();
  }

  boolean running = true;
  ArrayList<Point> checkedList = new ArrayList<>();
  Vector<ProcessCaro> players = new Vector<>();

  public CaroServer() {
    try {
      ServerSocket server = new ServerSocket(80);
      while (this.running) {
        Socket soc = server.accept();
        ProcessCaro processCaro = new ProcessCaro(soc);
        players.add(processCaro);
        if (players.size() < 2) {
          processCaro.start();
        } else {
          for (int i = 0; i <=1; i++) {
            if (soc.getInetAddress() == players.get(i).soc.getInetAddress() && !players.get(i).running) {
              players.remove(processCaro);
              players.remove(i);
              players.add(i, processCaro);
              processCaro.start();
              break;
            }
          }
        }

      }
    } catch (Exception e) {
      this.running = false;
      System.out.println(e.getMessage());
    }
  }

  private void positionValidate(ProcessCaro currentPlayer, int ix, int iy) {
    if (players.size() < 2) return;
    if (currentPlayer == this.players.get(0) && this.checkedList.size() % 2 != 0) return;
    if (currentPlayer == this.players.get(1) && this.checkedList.size() % 2 != 1) return;
    for (Point d : checkedList) {
      if (d.x == ix && d.y == iy) return;
    }
    this.checkedList.add(new Point(ix, iy));

    for (ProcessCaro player : this.players) {
      try {
        player.dos.writeUTF(ix + "");
        player.dos.writeUTF(iy + "");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        break;
      }
    }
  }

  class ProcessCaro extends Thread {
    boolean running = true;
    Socket soc;
    DataInputStream dis;
    DataOutputStream dos;

    public ProcessCaro(Socket soc) {
      try {
        this.soc = soc;
        this.dis = new DataInputStream(soc.getInputStream());
        this.dos = new DataOutputStream(soc.getOutputStream());

        for (Point point: checkedList) {
          this.dos.writeUTF(point.x + "");
          this.dos.writeUTF(point.y + "");
        }
      } catch (Exception e) {
        this.running = false;
        System.out.println(e.getMessage());
      }
    }

    @Override
    public void run() {
      try {
        while (this.running) {
          int ix = Integer.parseInt(this.dis.readUTF());
          int iy = Integer.parseInt(this.dis.readUTF());

          positionValidate(this, ix, iy);
        }
      } catch (Exception e) {
        this.running = false;
        System.out.println(e.getMessage());
      }
    }
  }
}
