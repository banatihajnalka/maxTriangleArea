import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TriangleMaxArea {

  private static final int NUMBER_OF_POINTS = 20;

  private static void mainDraw(Graphics graphics) {
    graphics.setColor(Color.blue);
    List<Point> points = drawRandomPoints(graphics);
    List<Point> sortedPoints = sortPoints(points);
    drawMaxPerimeter(graphics, sortedPoints);
  }

  private static List<Point> drawRandomPoints(Graphics graphics) {
    List<Point> points = new ArrayList<>();
    Random r = new Random();
    for (int i = 0; i < NUMBER_OF_POINTS; i++) {
      int x = Math.abs((r.nextInt()) % WIDTH);
      int y = Math.abs((r.nextInt()) % HEIGHT);
      Point point = new Point(Math.abs(x), Math.abs(y));
      points.add(point);
      graphics.drawLine(x, y, x, y);
    }
    return points;
  }

  private static List<Point> sortPoints(List<Point> points) {
    points.sort(new Comparator<Point>() {
      public int compare(Point first, Point second) {
        return Integer.compare(first.getX(), second.getX());
      }
    });
    return points;
  }

  private static void drawMaxPerimeter(Graphics graphics, List<Point> points) {
    int maxArea = Integer.MIN_VALUE;
    List<Point> maxTriangleCoordinates = new ArrayList<>();
    for (int i = 0; i < points.size() - 2; i++) {
      for (int j = i + 1; j < points.size() - 1; j++) {
        if (points.get(i) == points.get(j)) {
          continue;
        }
        for (int k = j; k < points.size(); k++) {
          if (points.get(j) == points.get(k) || points.get(i) == points.get(k)) {
            continue;
          }
          Polygon triangle = new Polygon();
          boolean isEmpty = true;
          triangle.addPoint(points.get(i).getX(), points.get(i).getY());
          triangle.addPoint(points.get(j).getX(), points.get(j).getY());
          triangle.addPoint(points.get(k).getX(), points.get(k).getY());
          for (Point point : points) {
            if (point == points.get(k) || point == points.get(j) || point == points.get(i)) {
              continue;
            }
            if (triangle.contains(point.getX(), point.getY())) {
              isEmpty = false;
              break;
            }
          }
          if (isEmpty) {
            List<Point> triangleCoordinates = new ArrayList<>();
            triangleCoordinates.add(new Point(points.get(i).getX(), points.get(i).getY()));
            triangleCoordinates.add(new Point(points.get(j).getX(), points.get(j).getY()));
            triangleCoordinates.add(new Point(points.get(k).getX(), points.get(k).getY()));
            int currentArea = calculateArea(triangle);
            if ((maxArea < currentArea)) {
              maxArea = currentArea;
              maxTriangleCoordinates.clear();
              maxTriangleCoordinates.addAll(triangleCoordinates);
            }
          }
        }
      }
    }
    drawTriangle(graphics, maxTriangleCoordinates);
  }

  private static int calculateArea(Polygon triangle) {
    int[] x = triangle.xpoints;
    int[] y = triangle.ypoints;
    return Math.abs((x[0] * (y[1] - y[2]) + x[1] * (y[2] - y[0]) + x[2] * (y[1] - y[2])) / 2);
  }

  private static void drawTriangle(Graphics graphics, java.util.List<Point> points) {
    graphics.drawPolygon(new int[]{points.get(0).getX(), points.get(1).getX(), points.get(2).getX()},
        new int[]{points.get(0).getY(), points.get(1).getY(), points.get(2).getY()}, 3);
  }

  private static int WIDTH = 320;
  private static int HEIGHT = 320;

  public static void main(String[] args) {
    JFrame jFrame = new JFrame("Drawing");
    jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    ImagePanel panel = new ImagePanel();
    panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    jFrame.add(panel);
    jFrame.setLocationRelativeTo(null);
    jFrame.setVisible(true);
    jFrame.pack();
  }

  static class ImagePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      mainDraw(graphics);
    }
  }
}
