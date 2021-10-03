package ldt.codeeye.caroAI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CaroAI extends JFrame implements MouseListener {

  public static void main(String[] args) {
    new CaroAI();
  }

  int n = 10;
  int size = 30;
  int margin = 70;
  Vector<Point> picked = new Vector<>();
  Map<String, Integer> mapPoint = new HashMap<>();
  boolean isEnd = false;

  public CaroAI() {
    this.setTitle("Caro AI");
    this.setSize(margin * 2 + n * size, margin * 2 + n * size);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.addMouseListener(this);

    this.setVisible(true);
  }

  public void paint(Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    g.setColor(Color.BLACK);
    for (int i = 0; i <= n; i++) {
      g.drawLine(margin, margin + i * size, margin + n * size, margin + i * size);
      g.drawLine(margin + i * size, margin, margin + i * size, margin + n * size);
    }

    g.setFont(new Font("Arial", Font.BOLD, size));
    for (int i = 0; i < picked.size(); i++) {
      int x = picked.get(i).x * size + margin + size / 7;
      int y = picked.get(i).y * size + margin + 6 * size / 7;

      String st = "O";
      g.setColor(Color.BLUE);
      if (i % 2 == 1) {
        g.setColor(Color.RED);
        st = "X";
      }
      g.drawString(st, x, y);
    }
    int v1 = getPlayerValue(1);
    int v2 = getPlayerValue(2);
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.ITALIC, 18));
    g.drawString("O Score:" + v1, margin, margin);
    g.drawString("X Score:" + v2, margin + 50, margin);

    if ((v1 > 100000) || (v2 > 100000)) g.drawString("Finish!", margin + 50, margin);
  }

  int minimax(Node node, int d, boolean MaxP) {
    if (endNode() || d == 0) {
      node.value = getPlayerValue(2) - getPlayerValue(1);
      return node.value;
    }
    Map<String, Integer> added = new HashMap<>();
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        if ((mapPoint.get(i + "," + j)) != null)
          for (int h = -1; h <= 1; h++)
            for (int k = -1; k <= 1; k++) {
              final String key = (i + h) + "," + (j + k);
              if (mapPoint.get(key) == null && added.get(key) == null) {
                Node c = new Node();
                c.p = new Point(i + h, j + k);
                c.parent = node;
                node.children.add(c);
                added.put(key, 1);
              }
            }

    int m;
    if (MaxP) {
      m = Integer.MIN_VALUE;

      for (Node child : node.children) {
        mapPoint.put(child.p.x + "," + child.p.y, 2);
        m = Math.max(m, minimax(child, d - 1, false));
        mapPoint.put(child.p.x + "," + child.p.y, null);
      }
    } else {
      m = Integer.MAX_VALUE;
      for (Node child : node.children) {
        mapPoint.put(child.p.x + "," + child.p.y, 1);
        m = Math.min(m, minimax(child, d - 1, true));
        mapPoint.put(child.p.x + "," + child.p.y, null);
      }
    }
    node.value = m;
    return node.value;
  }

  int v(int numContinualPoints, int numNonBlock) {
    if (numNonBlock == 2) {
      if (numContinualPoints == 4) return 100000;
      if (numContinualPoints == 3) return 1000;
      if (numContinualPoints == 2) return 30;
      if (numContinualPoints == 1) return 10;
      if (numContinualPoints == 0) return 3;
    }
    if (numNonBlock == 1) {
      if (numContinualPoints == 4) return 100000;
      if (numContinualPoints == 3) return 100;
      if (numContinualPoints == 2) return 10;
      if (numContinualPoints == 1) return 5;
      if (numContinualPoints == 0) return 1;
    }
    return 0;
  }

  boolean isInbound(int x, int y) {
    return (0 <= x) && (x < n) && (0 <= y) && (y < n);
  }

  boolean endNode() {
    if (getPlayerValue(1) >= 100000) return true;
    if (getPlayerValue(2) >= 100000) return true;
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        if ((mapPoint.get(i + "," + j)) == null) {
          return false;
        }
    return true;
  }

  Integer getPlayerValue(int player) {
    int[][] a = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        if (mapPoint.get(i + "," + j) != null) {
          a[j][i] = mapPoint.get(i + "," + j);
        }
    // check win
    int max = 0;
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++) {
        if (a[i][j] != player) continue;
        int ii = i;
        int jj = j;
        int c;
        //vertical
        if (isInbound(i + 1, j)) {
          while ((a[ii + 1][jj] == a[i][j])) {
            if ((ii - i) >= 4) return 100000;
            ii++;
            if (!(isInbound(ii + 1, jj))) break;
          }
          c = 0;
          if (isInbound(i - 1, j))
            if (a[i - 1][j] == 0) c++;
          if (isInbound(ii + 1, jj))
            if (a[ii + 1][jj] == 0) c++;
          max += v(ii - i, c);
        }
        // horizontal
        ii = i;
        jj = j;
        if (isInbound(i, j + 1)) {
          while (a[ii][jj + 1] == a[i][j]) {
            if ((jj - j) >= 4) {
              return 100000;
            }
            jj++;
            if (!(isInbound(ii, jj + 1))) break;
          }
          c = 0;
          if (isInbound(i, j - 1))
            if (a[i][j - 1] == 0) c++;
          if (isInbound(ii, jj + 1))
            if (a[ii][jj + 1] == 0) c++;
          max += v(jj - j, c);
        }
        // \
        ii = i;
        jj = j;
        if (isInbound(i + 1, j + 1)) {
          while (a[ii + 1][jj + 1] == a[i][j]) {
            if (ii - i >= 4)
              return 100000;
            ii++;
            jj++;
            if (!(isInbound(ii + 1, jj + 1))) break;
          }
          c = 0;
          if (isInbound(i - 1, j - 1))
            if (a[i - 1][j - 1] == 0) c++;
          if (isInbound(ii + 1, jj + 1))
            if (a[ii + 1][jj + 1] == 0) c++;
          max += v(ii - i, c);
        }
        // /
        ii = i;
        jj = j;
        if (isInbound(i - 1, j + 1)) {
          while ((a[ii - 1][jj + 1] == a[i][j])) {
            if ((jj - j) >= 4)
              return 100000;
            ii--;
            jj++;
            if (!(isInbound(ii - 1, jj + 1))) break;
          }
          c = 0;
          if (isInbound(i + 1, j - 1))
            if (a[i + 1][j - 1] == 0) c++;
          if (isInbound(ii - 1, jj + 1))
            if (a[ii - 1][jj + 1] == 0) c++;
          max += v(jj - j, c);
        }
      }
    return max;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (isEnd) return;
    int x = e.getX();
    int y = e.getY();

    if (x < margin || x >= margin + size * n) return;
    if (y < margin || y >= margin + size * n) return;

    int ix = (x - margin) / size;
    int iy = (y - margin) / size;
    for (Point point : picked) {
      if (ix == point.x && iy == point.y) return;
    }
    Point p = new Point(ix, iy);
    picked.add(p);
    mapPoint.put(p.x + "," + p.y, 1);
    this.repaint();
    Node node = new Node();
    minimax(node, 3, true);
    for (Node child : node.children) {
      if (child.value == node.value) {
        picked.add(child.p);
        mapPoint.put(child.p.x + "," + child.p.y, 2);
        break;
      }
    }
    this.repaint();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }
}



