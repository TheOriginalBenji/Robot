import javafx.scene.*;
import java.util.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class ResizePoly extends Circle {
    private final DoubleProperty x, y;

    ResizePoly(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 3);
      setFill(color);
      setStroke(color);
      setStrokeWidth(1);
      setStrokeType(StrokeType.OUTSIDE);

      this.x = x;
      this.y = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Point dragPoint = new Point();
      this.setOnMousePressed(e -> {
          dragPoint.x = getCenterX() - e.getX();
          dragPoint.y = getCenterY() - e.getY();
          getScene().setCursor(Cursor.MOVE);
      });
      
      setOnMouseDragged(e -> {
          double newX = e.getX() + dragPoint.x;
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }
          double newY = e.getY() + dragPoint.y;
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }
         //vtriangle.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(triangle.getPoints(),a))));
      });
    }

    // records relative x and y co-ordinates.
    private class Point { double x, y; }
  }