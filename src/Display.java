/* Benjamin Hamlin
 * 8 February, 2016
 * This program creates the display for the robot state space example program
 * 
 * 
 */

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

class Display extends ScrollPane {

    // Initialize the buttons for the display
    private Button btPlus, btMinus, btReset, btCalculate;

    // Initialize more stuffs
    protected Polygon robotStart, polyOne, polyTwo, polyThree, robotGoal;
    Polygon polyOneVirtual = new Polygon(), polyTwoVirtual = new Polygon(),
            polyThreeVirtual = new Polygon();
    static Polygon tempPolygonOne = new Polygon(),
            tempPolygonTwo = new Polygon(), tempPolygonThree = new Polygon();
    double scale, scale2, scaler;
    private static ArrayList<Polyline> visibles = new ArrayList<>();
    ArrayList<Node> closedList = new ArrayList<Node>(), openList = new ArrayList<Node>();
    Map<Node, Double> scoreBest = new HashMap<>(), scoreEstimate = new HashMap<>();
    Node current;
    Map<Node, Node> parent = new HashMap<>();
    double keepTrack = 0, keepTrack1 = 0;
    ArrayList<Line> holdNodes = new ArrayList<>();
    ArrayList<Node> noodles = new ArrayList<>();
    Node start = new Node(), goal = new Node();

//    private Label Start = new Label();
//    private Label Goal = new Label();
    // Constructor that will create the display window
    public Display(Button btPlus, Button btMinus, Button btReset, Button btCalculate) {

        // Assign buttons
        this.btPlus = btPlus;
        this.btMinus = btMinus;
        this.btReset = btReset;
        this.btCalculate = btCalculate;

        scaler = 40.0;
        scale = 30.0;

        // Assign robot and obstacles
        robotStart = drawRobot(scaler);
        polyOne = drawFirstPolygon();
        polyTwo = drawSecondPolygon();
        polyThree = drawThirdPolygon();
        robotGoal = drawGoal(scaler);

        // Set button widths
        btPlus.maxWidth(40.0);
        btMinus.maxWidth(40.0);
        btReset.maxWidth(40.0);
        btCalculate.maxWidth(40.0);

        // Create a pane, hbox, and vbox
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15));
        VBox vbox = new VBox();
        Pane pane = new Pane();

        // Hold the buttons in the HBox
        hbox.getChildren().addAll(btPlus, btMinus, btReset, btCalculate);

        // Hold the polygons in a pane
        pane.getChildren().addAll(robotStart, polyOne, polyTwo, polyThree,
                robotGoal);

        // Hold the virtual polygons in a pane
        pane.getChildren().addAll(polyOneVirtual, polyTwoVirtual,
                polyThreeVirtual);

        pane.getChildren().addAll(createControlPoints(polyOne.getPoints()));
        pane.getChildren().addAll(createControlPoints(polyTwo.getPoints()));
        pane.getChildren().addAll(createControlPoints(polyThree.getPoints()));

        // Draw the virtual obstacles
        polyOneVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale))));
        polyOneVirtual.setStroke(Color.BLACK);
        polyOneVirtual.setStrokeWidth(3);
        polyOneVirtual.setFill(Color.TRANSPARENT);

        polyTwoVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale))));
        polyTwoVirtual.setStroke(Color.BLACK);
        polyTwoVirtual.setStrokeWidth(3);
        polyTwoVirtual.setFill(Color.TRANSPARENT);

        polyThreeVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale))));
        polyThreeVirtual.setStroke(Color.BLACK);
        polyThreeVirtual.setStrokeWidth(3);
        polyThreeVirtual.setFill(Color.TRANSPARENT);

        tempPolygonOne.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale * 0.99))));
        tempPolygonTwo.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale * 0.99))));
        tempPolygonThree.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale * 0.99))));

        keepTrack1 = 19;

        // Hold the hbox and pane in a vbox and add it to the group
        vbox.getChildren().addAll(pane, hbox);
        this.setContent(vbox);
        this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        pane.setOnMouseDragged(e -> {
            polyOneVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale))));
            polyTwoVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale))));
            polyThreeVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale))));
            tempPolygonOne.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale * 0.99))));
            tempPolygonTwo.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale * 0.99))));
            tempPolygonThree.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale * 0.99))));

        });
        // lambda expressions for incrementing size of obstacles

        btPlus.setOnAction(e -> {
            scale += 3;
            robotStart.getPoints().setAll(drawRobot(scale).getPoints());
            polyOneVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale))));
            polyTwoVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale))));
            polyThreeVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale))));
            robotGoal.getPoints().setAll(drawGoal(scale).getPoints());
            tempPolygonOne.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale * 0.99))));
            tempPolygonTwo.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale * 0.99))));
            tempPolygonThree.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale * 0.99))));

        });

        // lambda expression for decrementing size of obstacles
        btMinus.setOnAction(e -> {
            if (scale > 3) {
                scale -= 3;
                robotStart.getPoints().setAll(drawRobot(scale).getPoints());
                polyOneVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale))));
                polyTwoVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale))));
                polyThreeVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale))));
                robotGoal.getPoints().setAll(drawGoal(scale).getPoints());
                tempPolygonOne.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale * 0.99))));
                tempPolygonTwo.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale * 0.99))));
                tempPolygonThree.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale * 0.99))));

            }
        });

        // lambda expressions for resetting display
        btReset.setOnAction(e -> {

            VBox vboxNew = new VBox();
            Pane paneNew = new Pane();
            // Assign robot and obstacles
            robotStart = drawRobot(40.0);
            polyOne = drawFirstPolygon();
            polyTwo = drawSecondPolygon();
            polyThree = drawThirdPolygon();
            robotGoal = drawGoal(40.0);
            pane.getChildren().clear();
            scale2 = 40.0;
            scale = 30.0;
            double s = 25.0;
            // Hold the buttons in the HBox

            // Hold the polygons in a pane
            paneNew.getChildren().addAll(robotStart, polyOne, polyTwo, polyThree,
                    robotGoal);

            // Hold the virtual polygons in a pane
            paneNew.getChildren().addAll(polyOneVirtual, polyTwoVirtual,
                    polyThreeVirtual);

            polyOne.getPoints().setAll(28 * s, 4 * s, 30 * s, 5 * s, 34 * s,
                    9 * s, 34 * s, 14 * s, 31 * s, 17 * s, 27 * s, 15 * s, 25 * s,
                    12 * s, 26 * s, 9 * s);
            polyTwo.getPoints().setAll(25 * s, 23 * s, 20 * s, 24 * s, 16 * s,
                    28 * s, 18 * s, 33 * s, 24 * s, 32 * s, 27 * s, 27 * s);
            polyThree.getPoints().setAll(36 * s, 31 * s, 41 * s, 29 * s, 40 * s,
                    22 * s, 38 * s, 19 * s, 31 * s, 24 * s, 32 * s, 27 * s);
            polyOneVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale))));
            polyTwoVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale))));
            polyThreeVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale))));

            polyOneVirtual.setStroke(Color.BLACK);
            polyOneVirtual.setStrokeWidth(3);
            polyOneVirtual.setFill(Color.TRANSPARENT);

            polyTwoVirtual.setStroke(Color.BLACK);
            polyTwoVirtual.setStrokeWidth(3);
            polyTwoVirtual.setFill(Color.TRANSPARENT);

            polyThreeVirtual.setStroke(Color.BLACK);
            polyThreeVirtual.setStrokeWidth(3);
            polyThreeVirtual.setFill(Color.TRANSPARENT);

            tempPolygonOne.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale * 0.99))));
            tempPolygonTwo.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale * 0.99))));
            tempPolygonThree.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale * 0.99))));

            paneNew.getChildren().addAll(createControlPoints(polyOne.getPoints()));
            paneNew.getChildren().addAll(createControlPoints(polyTwo.getPoints()));
            paneNew.getChildren().addAll(createControlPoints(polyThree.getPoints()));

            // Hold the hbox and pane in a vbox and add it to the group
            vboxNew.getChildren().addAll(paneNew, hbox);
            this.setContent(vboxNew);
        });

        // lambda expressions for calculating the shortest path
        btCalculate.setOnAction(e -> {
            visibles.clear();

            VBox vboxNew = new VBox();
            Pane paneNew = new Pane();
            // Assign robot and obstacles
            robotStart = drawRobot(40.0);
            polyOne = drawFirstPolygon();
            polyTwo = drawSecondPolygon();
            polyThree = drawThirdPolygon();
            robotGoal = drawGoal(40.0);
            pane.getChildren().clear();
            scale2 = 40.0;
            scale = 30.0;
            double s = 25.0;

            start.setXY(10.0, 10 + .866 * scaler);
            goal.setXY(810.0, 810 + .866 * scaler);

            noodles = aStar(start, goal);

            // Hold the polygons in a pane
            paneNew.getChildren().addAll(robotStart, polyOne, polyTwo, polyThree,
                    robotGoal);

            // Hold the virtual polygons in a pane
            paneNew.getChildren().addAll(polyOneVirtual, polyTwoVirtual,
                    polyThreeVirtual);
            
            for (int i = 0; i < noodles.size() / 2; i += 2) {
                holdNodes.add(new Line(noodles.get(i).getX(), noodles.get(i).getY(),
                        noodles.get(i + 1).getX(), noodles.get(i + 1).getY()));
                holdNodes.get(i).setFill(Color.RED);
                holdNodes.get(i).setStrokeWidth(5);
            }
            
            paneNew.getChildren().addAll(holdNodes);

            polyOne.getPoints().setAll(28 * s, 4 * s, 30 * s, 5 * s, 34 * s,
                    9 * s, 34 * s, 14 * s, 31 * s, 17 * s, 27 * s, 15 * s, 25 * s,
                    12 * s, 26 * s, 9 * s);
            polyTwo.getPoints().setAll(25 * s, 23 * s, 20 * s, 24 * s, 16 * s,
                    28 * s, 18 * s, 33 * s, 24 * s, 32 * s, 27 * s, 27 * s);
            polyThree.getPoints().setAll(36 * s, 31 * s, 41 * s, 29 * s, 40 * s,
                    22 * s, 38 * s, 19 * s, 31 * s, 24 * s, 32 * s, 27 * s);
            polyOneVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale))));
            polyTwoVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale))));
            polyThreeVirtual.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale))));

            polyOneVirtual.setStroke(Color.BLACK);
            polyOneVirtual.setStrokeWidth(3);
            polyOneVirtual.setFill(Color.TRANSPARENT);

            polyTwoVirtual.setStroke(Color.BLACK);
            polyTwoVirtual.setStrokeWidth(3);
            polyTwoVirtual.setFill(Color.TRANSPARENT);

            polyThreeVirtual.setStroke(Color.BLACK);
            polyThreeVirtual.setStrokeWidth(3);
            polyThreeVirtual.setFill(Color.TRANSPARENT);

            tempPolygonOne.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyOne.getPoints(), scale * 0.99))));
            tempPolygonTwo.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyTwo.getPoints(), scale * 0.99))));
            tempPolygonThree.getPoints().setAll(getConvexHull(sortPoints(virtualToReal(polyThree.getPoints(), scale * 0.99))));

            paneNew.getChildren().addAll(createControlPoints(polyOne.getPoints()));
            paneNew.getChildren().addAll(createControlPoints(polyTwo.getPoints()));
            paneNew.getChildren().addAll(createControlPoints(polyThree.getPoints()));

            

            // Hold the hbox and pane in a vbox and add it to the group
            vboxNew.getChildren().addAll(paneNew, hbox);
            this.setContent(vboxNew);
            //generateLists();
            //generateVisibles();
            //Shape temp1 = Shape.union(vpoly1,poly1);
            for (int i = 0; i < polyTwoVirtual.getPoints().size(); i += 2) {
                setVis(polyTwoVirtual.getPoints().get(i), polyTwoVirtual.getPoints().get(i + 1), polyOneVirtual);
            }
            for (int j = 0; j < polyThreeVirtual.getPoints().size(); j += 2) {
                setVis(polyThreeVirtual.getPoints().get(j), polyThreeVirtual.getPoints().get(j + 1), polyTwoVirtual);
            }
            for (int k = 0; k < polyThreeVirtual.getPoints().size(); k += 2) {
                setVis(polyThreeVirtual.getPoints().get(k), polyThreeVirtual.getPoints().get(k + 1), polyOneVirtual);
            }
            for (Polyline path : visibles) {
                paneNew.getChildren().add(path);
            }

        });
    }

    private static void setVis(double x, double y, Polygon poly) {
        for (int i = 0; i < poly.getPoints().size(); i += 2) {
            Polygon newPoly = new Polygon();
            newPoly.getPoints().setAll(
                    x, y,
                    poly.getPoints().get(i),
                    poly.getPoints().get(i + 1),
                    poly.getPoints().get((i + 2) % poly.getPoints().size()),
                    poly.getPoints().get((i + 3) % poly.getPoints().size())
            );
            Shape totalError = Shape.union(tempPolygonOne, tempPolygonTwo);
            totalError = Shape.union(tempPolygonThree, totalError);
            Shape inter = Shape.intersect(totalError, newPoly);
            if (inter.getLayoutBounds().getHeight() <= 0 || inter.getLayoutBounds().getWidth() <= 0) {
                //if(triangle.getBoundsInLocal().contains(vpoly1.getBoundsInLocal())){  
                Polyline one = new Polyline();
                Polyline two = new Polyline();
                one.getPoints().setAll(x, y, poly.getPoints().get(i), poly.getPoints().get(i + 1));
                two.getPoints().setAll(x, y, poly.getPoints().get((i + 2) % poly.getPoints().size()),
                        poly.getPoints().get((i + 3) % poly.getPoints().size()));
                System.out.println(visibles.size());

                visibles.add(one);
                visibles.add(two);
            } else {
                System.out.println("in else");
            }

        }

    }

    // Draw the robot at the starting location
    private Polygon drawRobot(double scaler) {
        this.robotStart = new Polygon();
        robotStart.getPoints().setAll((double) (10 + 0.5 * scaler), 10.0, 10.0,
                (double) (10 + .866 * scaler), (double) (10 + scaler),
                (double) (10 + .866 * scaler));

        robotStart.setStroke(Color.BLACK);
        robotStart.setFill(Color.CYAN);
        return robotStart;
    }

    // Draw the first default obstacle
    private static Polygon drawFirstPolygon() {
        Polygon polyOne = new Polygon();
        // use a scaler to increase readability
        double s = 25.0;
        polyOne.getPoints().setAll(28 * s, 4 * s, 30 * s, 5 * s, 34 * s, 9 * s,
                34 * s, 14 * s, 31 * s, 17 * s, 27 * s, 15 * s, 25 * s, 12 * s,
                26 * s, 9 * s);

        // Set obstacle's color
        polyOne.setStroke(Color.BLACK);
        polyOne.setFill(Color.BLUEVIOLET);

        return polyOne;
    }

    // draw the second default obstacle
    private static Polygon drawSecondPolygon() {
        Polygon polyTwo = new Polygon();
        // use a scaler to increase readability
        double s = 25.0;
        polyTwo.getPoints().setAll(25 * s, 23 * s, 20 * s, 24 * s, 16 * s,
                28 * s, 18 * s, 33 * s, 24 * s, 32 * s, 27 * s, 27 * s);

        // Set obstacle's color
        polyTwo.setStroke(Color.BLACK);
        polyTwo.setFill(Color.AQUAMARINE);

        return polyTwo;
    }

    // Draw the third default obstacle
    private static Polygon drawThirdPolygon() {
        Polygon polyThree = new Polygon();
        // use a scaler to increase readability
        double s = 25.0;
        polyThree.getPoints().setAll(36 * s, 31 * s, 41 * s, 29 * s, 40 * s,
                22 * s, 38 * s, 19 * s, 31 * s, 24 * s, 32 * s, 27 * s);

        // Set obstacle's color
        polyThree.setStroke(Color.BLACK);
        polyThree.setFill(Color.CHARTREUSE);

        return polyThree;
    }

    // Draw the robot's goal
    private Polygon drawGoal(double scaler) {
        this.robotGoal = new Polygon();

        robotGoal.getPoints().setAll((double) (1310 + 0.5 * scaler), 810.0,
                1310.0, (double) (810 + .866 * scaler),
                (double) (1310 + scaler), (double) (810 + .866 * scaler));

        robotGoal.setStroke(Color.BLACK);
        robotGoal.setFill(Color.RED);
        return robotGoal;
    }

    private ObservableList<Double> virtualToReal(ObservableList<Double> ptReal, double radius) {
        // initialize lists
        ObservableList<Double> ptVirtual = FXCollections.observableArrayList();

        // add all points in the real obstacle to the virtual obstacle
        for (int i = 0; i < ptReal.size(); i += 2) {
            ptVirtual.add(ptReal.get(i));
            ptVirtual.add(ptReal.get(i + 1));
        }

        // add all pts in away from the real obstacle that is the same distance 
        // as the base of the triangle
        for (int i = 0; i < ptReal.size(); i += 2) {
            ptVirtual.add(ptReal.get(i) - (radius / 2));
            ptVirtual.add(ptReal.get(i + 1) + .866 * radius);
            ptVirtual.add(ptReal.get(i) - radius);
            ptVirtual.add(ptReal.get(i + 1));
        }

        return ptVirtual;
    }

    private ObservableList<ResizePoly> createControlPoints(final ObservableList<Double> points) {
        // Initialize lists
        ObservableList<ResizePoly> anchors = FXCollections.observableArrayList();

        for (int i = 0; i < points.size(); i += 2) {
            final int idx = i;

            DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
            DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

            // listen to change x value
            xProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
                    points.set(idx, (double) x);
                }
            });
            // Listen to change y value
            yProperty.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
                    points.set(idx + 1, (double) y);
                }
            });

            anchors.add(new ResizePoly(Color.BLACK, xProperty, yProperty));
        }

        return anchors;
    }

    private ObservableList<Double> sortPoints(ObservableList<Double> vertList) {
        double centerX = 0, centerY = 0;
        for (int i = 0; i < vertList.size(); i += 2) {
            centerX += vertList.get(i);
            centerY += vertList.get(i + 1);
        }
        centerX /= (vertList.size() / 2);
        centerY /= (vertList.size() / 2);
        ArrayList<Double> angles = new ArrayList<>();
        for (int j = 0; j < vertList.size(); j += 2) {
            angles.add(Math.atan2(vertList.get(j + 1) - centerY, vertList.get(j) - centerX));
        }
        for (int j = angles.size() - 1; j >= 0; j--) {
            for (int k = 1; k < j; k++) {
                if (angles.get(k - 1) > angles.get(k)) {
                    double temp = angles.get(k - 1);
                    angles.set(k - 1, angles.get(k));
                    angles.set(k, temp);

                    double tempx = vertList.get(2 * (k - 1));
                    double tempy = vertList.get(2 * (k - 1) + 1);

                    vertList.set(2 * (k - 1), vertList.get(2 * k));
                    vertList.set(2 * (k - 1) + 1, vertList.get((2 * k) + 1));

                    vertList.set(2 * k, tempx);
                    vertList.set((2 * k) + 1, tempy);

                }
            }
        }
        return vertList;
    }

    private ObservableList<Double> getConvexHull(ObservableList<Double> vertList) {
        // initialize some arrays
        Point2D[] myPoints = new Point2D[vertList.size() / 2];
        ArrayList<Point2D> ptHull = new ArrayList<Point2D>();
        ObservableList<Double> convexHull = FXCollections.observableArrayList();

        // Take all the points from the bservablelist and put them into the 
        //array as Point2D
        for (int i = 0; i < myPoints.length; i++) {
            myPoints[i] = new Point2D(vertList.get(2 * i), vertList.get((2 * i) + 1));
        }

        // Find the right most lowest point from the array
        // and add it to a list of point2D
        Point2D ptStart = getRightMostLowestPoint(myPoints);
        ptHull.add(ptStart);
        Point2D pt1 = ptStart;

        // Find the points in the convex hull
        while (true) {
            Point2D pt2 = myPoints[0];

            // find the points in the ConvexHull
            for (int i = 1; i < myPoints.length; i++) {
                double status = whichSide(pt1.getX(), pt1.getY(), pt2.getX(),
                        pt2.getY(), myPoints[i].getX(), myPoints[i].getY());
                if (status > 0) {
                    pt2 = myPoints[i];
                } else if (status == 0) {
                    if (distance(myPoints[i].getX(), myPoints[i].getY(), pt1.getX(), pt1.getY())
                            > distance(pt2.getX(), pt2.getY(), pt1.getX(), pt1.getY())) {
                        pt2 = myPoints[i];
                    }
                }
            }

            // Check if the current point is the starting point
            // if it is, then stop
            if (pt2.getX() == ptStart.getX() && pt2.getY() == ptStart.getY()) {
                break;
            } else {
                ptHull.add(pt2);
                pt1 = pt2;
            }
        }

        // Add all the points in the convexhull into an observableList
        for (int j = 0; j < ptHull.size(); j++) {
            convexHull.add(ptHull.get(j).getX());
            convexHull.add(ptHull.get(j).getY());
        }

        return convexHull;
    }

    public double whichSide(double x0, double y0, double x1, double y1, double x2, double y2) {
        return (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
    }

    // Determines the distance between two points via the distance formula
    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    // Determines the right most lowest point in the array to start the convex hull
    static Point2D getRightMostLowestPoint(Point2D[] ptArr) {
        // initialize some variables 
        int rightMostIndex = 0;
        double rightMostX = ptArr[0].getX();
        double rightMostY = ptArr[0].getY();

        // iterate through the array and find the right most lowest point
        for (int i = 0; i < ptArr.length; i++) {
            // checks if this point is the right most
            if (rightMostY < ptArr[i].getY()) {
                rightMostY = ptArr[i].getY();
                rightMostX = ptArr[i].getX();
                rightMostIndex = i;
            } // checks if this is the lowest of the rightmost points
            else if (rightMostY == ptArr[i].getY() && rightMostX < ptArr[i].getX()) {
                rightMostX = ptArr[i].getX();
                rightMostIndex = i;
            }
        }

        return ptArr[rightMostIndex];
    }

    public ArrayList<Node> aStar(Node start, Node goal) {
        ArrayList<Node> arrList = new ArrayList<>();

        // clear the old sets
        openList.clear();
        closedList.clear();
        int count = 0;

        //add the start to tentative nodes
        openList.add(start);
        Node current = start;

        scoreBest.put(start, 0.0);
        scoreEstimate.put(goal, estimateCost(start, goal));

        while (true) {
            if (openList.isEmpty()) {
                break;
            }
            if (count != 0) {
                current = lowestScore(openList, scoreEstimate);
            } count++;
            if (current.equals(goal)) {
                return findPath(goal);
            }
            openList.remove(current);
            closedList.add(current);
            for (Node node : current.getNeighbors()) {
                if (closedList.contains(node)) {
                    continue;
                }
                keepTrack = scoreBest.get(current) + actualCost(current, node);
                if (!openList.contains(node)) {
                    openList.add(node);
                } else if (keepTrack >= scoreBest.get(node)) {
                    continue;
                }

                parent.replace(node, current);
                scoreBest.replace(node, keepTrack);
                scoreEstimate.replace(node, scoreBest.get(node) + actualCost(node, goal));
            }

        }

        return arrList;
    }

    public Node lowestScore(ArrayList<Node> openList, Map<Node, Double> scoreEstimate) {
        double lowest = Double.MAX_VALUE;
        Node lowestPoint = new Node();

        for (int i = 0; i < openList.size(); i++) {
            if (scoreEstimate.get(openList.get(i)) < lowest) {
                lowest = scoreEstimate.get(openList.get(i));
                lowestPoint.setXY(openList.get(i).getX(), openList.get(i).getY());
            }
        }
        return lowestPoint;
    }

    public Double actualCost(Node nodeOne, Node nodeTwo) {
        return Math.pow(.5, Math.pow(2, nodeOne.getX() - nodeTwo.getX())
                + Math.pow(2, nodeOne.getY() - nodeTwo.getY()));
    }

    public Double estimateCost(Node nodeOne, Node nodeTwo) {
        return Math.pow(2, nodeOne.getX() - nodeTwo.getX())
                + Math.pow(2, nodeOne.getY() - nodeTwo.getY());
    }

    public ArrayList<Node> findPath(Node current) {
        ArrayList<Node> path = new ArrayList<>();
        path.add(current);

        while (true) {
            if (current.getParent() == null) {
                break;
            }
            current = current.getParent();
            path.add(current);
        }

        return path;
    }

}