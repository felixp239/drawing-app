package com.company.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.company.Main;
import com.company.shapes.Line;
import com.company.shapes.Point;
import com.company.shapes.Rectangle;
import com.company.shapes.Oval;
import com.company.shapes.Shape;

public class Panel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ComponentListener {
    private java.awt.Point  start   = new java.awt.Point(0, 0);
    private java.awt.Point  current = new java.awt.Point(0, 0);
    private State.Mode      prev;
    private boolean         rotate  = false; //true when we're in the process of rotating a shape
    private boolean         dragged = false; //true when we're dragging a shape out
    private boolean         shift   = false; //When shift is pressed make squares and circles
    private boolean         alt     = false; //When alt is pressed make a shape with center at start

    public Panel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addComponentListener(this);
        setFocusable(true);
        requestFocus();
    }

    private void normalize(java.awt.Point start, java.awt.Point finish) {
        if (start.x > finish.x) {
            int t       = start.x;
            start.x     = finish.x;
            finish.x    = t;
        }
        if (start.y > finish.y) {
            int t       = start.y;
            start.y     = finish.y;
            finish.y    = t;
        }
    }

    private void normalize(java.awt.Point point) {  //Make it so vector from origin to point is a vector of length 10000
        double mod  = Math.sqrt(point.x * point.x + point.y * point.y);
        point.x     *= 10000;
        point.y     *= 10000;
        point.x     /= mod;
        point.y     /= mod;
    }

    private float getRotation(java.awt.Point origin, java.awt.Point start, java.awt.Point finish) {
        java.awt.Point[] points = new java.awt.Point[] { new java.awt.Point(start.x - origin.x, start.y - origin.y), new java.awt.Point(finish.x - origin.x, finish.y - origin.y) };
        normalize(points[0]);
        normalize(points[1]);
        return (float) Math.asin((points[0].y * points[1].x - points[0].x * points[1].y) / 100000000.0);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Main.frame.state.outlineColor);
        super.paint(g);
        for (com.company.shapes.Shape shape : Main.frame.getShapes()) {
            shape.draw(g, Main.getState().view);
        }
        if (dragged) {
            java.awt.Point[] points = new java.awt.Point[]{new java.awt.Point(start), new java.awt.Point(current)};
            if (shift && (Main.getState().mode == State.Mode.RECTANGLE || Main.getState().mode == State.Mode.OVAL)) {
                if (Math.abs(points[1].x - points[0].x) < Math.abs(points[1].y - points[0].y)) {
                    points[1].y = points[0].y + Integer.signum(points[1].y - points[0].y) * Math.abs(points[1].x - points[0].x);
                }
                else {
                    points[1].x = points[0].x + Integer.signum(points[1].x - points[0].x) * Math.abs(points[1].y - points[0].y);
                }
            }
            if (alt) {
                points[0].x = 2 * points[0].x - points[1].x;
                points[0].y = 2 * points[0].y - points[1].y;
            }
            switch (Main.getState().mode) {
                case LINE:
                    try {
                        Line temp = new Line(points, Main.getState());
                        temp.draw(g, Main.getState().view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case RECTANGLE:
                    normalize(points[0], points[1]);
                    try {
                        Rectangle temp = new Rectangle(points, Main.getState());
                        temp.draw(g, Main.getState().view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case OVAL:
                    normalize(points[0], points[1]);
                    try {
                        Oval temp = new Oval(points, Main.getState());
                        temp.draw(g, Main.getState().view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PEN:
                case POINT:
                    break;
                case MOVE:
                    Main.getState().view.move(new java.awt.Point(points[0].x - points[1].x, points[0].y - points[1].y));
                    break;
                case ROTATE:
                    if (!Main.frame.getShapes().isEmpty()) {
                        Shape shape = Main.frame.getShapes().get(Main.frame.getShapes().size() - 1);
                        try {
                            shape.rotate(getRotation(shape.getCenter(), start, current));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        start = current;
                    }
                    break;
                default:
                    System.out.println("Not supposed to happen");
                    break;
            }
        }
        // Whatever
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //shortcuts for mode switch
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                if (!dragged && Main.getState().mode != State.Mode.POINT) {
                    Main.getState().mode    = State.Mode.POINT;
                }
                break;
            case KeyEvent.VK_S:
                if (!dragged && Main.getState().mode != State.Mode.LINE) {
                    Main.getState().mode    = State.Mode.LINE;
                }
                break;
            case KeyEvent.VK_D:
                if (!dragged && Main.getState().mode != State.Mode.OVAL) {
                    Main.getState().mode    = State.Mode.OVAL;
                }
                break;
            case KeyEvent.VK_F:
                if (!dragged && Main.getState().mode != State.Mode.RECTANGLE) {
                    Main.getState().mode    = State.Mode.RECTANGLE;
                }
                break;
            case KeyEvent.VK_G:
                if (!dragged && Main.getState().mode != State.Mode.PEN) {
                    Main.getState().mode    = State.Mode.PEN;
                }
                break;
            case KeyEvent.VK_ADD:
                if (e.isControlDown()) {
                    Main.getState().view.zoom(1.05f / Main.getState().view.getScale());
                }
                break;
            case KeyEvent.VK_SUBTRACT:
                if (e.isControlDown()) {
                    Main.getState().view.zoom(0.95f / Main.getState().view.getScale());
                }
                break;
            case KeyEvent.VK_DELETE:
                if (!dragged && !Main.frame.getShapes().isEmpty()) {
                    Main.frame.getShapes().remove(Main.frame.getShapes().size() - 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //ctrl and shift can be used to modify the way we work with our shapes
        //also should probably implement some shortcuts for mode switch etc
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                shift = true;
                break;
            case KeyEvent.VK_ALT:
                alt   = true;
                break;
            case KeyEvent.VK_SPACE:
                if (!dragged && Main.getState().mode != State.Mode.MOVE) {
                    prev                    = Main.getState().mode;
                    Main.getState().mode    = State.Mode.MOVE;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                break;
            case KeyEvent.VK_R:
                if (!dragged && !Main.frame.getShapes().isEmpty() && Main.getState().mode != State.Mode.ROTATE) {
                    prev                    = Main.getState().mode;
                    Main.getState().mode    = State.Mode.ROTATE;
                }
                break;
            case KeyEvent.VK_A:
                if (!dragged && Main.getState().mode != State.Mode.POINT) {
                    Main.getState().mode    = State.Mode.POINT;
                }
                break;
            case KeyEvent.VK_S:
                if (!dragged && Main.getState().mode != State.Mode.LINE) {
                    Main.getState().mode    = State.Mode.LINE;
                }
                break;
            case KeyEvent.VK_D:
                if (!dragged && Main.getState().mode != State.Mode.OVAL) {
                    Main.getState().mode    = State.Mode.OVAL;
                }
                break;
            case KeyEvent.VK_F:
                if (!dragged && Main.getState().mode != State.Mode.RECTANGLE) {
                    Main.getState().mode    = State.Mode.RECTANGLE;
                }
                break;
            case KeyEvent.VK_G:
                if (!dragged && Main.getState().mode != State.Mode.PEN) {
                    Main.getState().mode    = State.Mode.PEN;
                }
                break;
            case KeyEvent.VK_ADD:
                if (e.isControlDown()) {
                    Main.getState().view.zoom(1.05f / Main.getState().view.getScale());
                }
                break;
            case KeyEvent.VK_SUBTRACT:
                if (e.isControlDown()) {
                    Main.getState().view.zoom(0.95f / Main.getState().view.getScale());
                }
                break;
            case KeyEvent.VK_DELETE:
                if (!dragged && !Main.frame.getShapes().isEmpty()) {
                    Main.frame.getShapes().remove(Main.frame.getShapes().size() - 1);
                }
                break;
            default:
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //ctrl and shift can be used to modify the way we work with our shapes
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                shift = false;
                break;
            case KeyEvent.VK_ALT:
                alt   = false;
                break;
            case KeyEvent.VK_SPACE:
                Main.getState().mode = prev;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
            case KeyEvent.VK_R:
                Main.getState().mode = prev;
                break;
            default:
                break;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //Draw point or do nothing if in other mode
        if (Main.getState().mode == State.Mode.POINT) {
            try {
                Main.frame.getShapes().add(new com.company.shapes.Point(Main.getState().view.viewToWorld(new java.awt.Point(mouseEvent.getX(), mouseEvent.getY())), Main.getState()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //Start working on drawing shape
        switch (Main.getState().mode) {
            case POINT:
                try {
                    Main.frame.getShapes().add(new Point(Main.getState().view.viewToWorld(new java.awt.Point(mouseEvent.getX(), mouseEvent.getY())), Main.getState()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LINE:
            case OVAL:
            case RECTANGLE:
            case MOVE:
            case PEN:
            case ROTATE:
                start = new java.awt.Point(mouseEvent.getX(), mouseEvent.getY());
                start = Main.getState().view.viewToWorld(start);
                break;
            default:
                System.out.println("nah");
                break;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //Draw current shape and add it to ArrayList
        current = new java.awt.Point(mouseEvent.getX(), mouseEvent.getY());
        current = Main.getState().view.viewToWorld(current);
        dragged = false;
        java.awt.Point[] points = new java.awt.Point[] { new java.awt.Point(start), new java.awt.Point(current) };
        if (shift && (Main.getState().mode == State.Mode.RECTANGLE || Main.getState().mode == State.Mode.OVAL)) {
            if (Math.abs(points[1].x - points[0].x) < Math.abs(points[1].y - points[0].y)) {
                points[1].y = points[0].y + Integer.signum(points[1].y - points[0].y) * Math.abs(points[1].x - points[0].x);
            }
            else {
                points[1].x = points[0].x + Integer.signum(points[1].x - points[0].x) * Math.abs(points[1].y - points[0].y);
            }
        }
        if (alt) {
            points[0].x = 2 * points[0].x - points[1].x;
            points[0].y = 2 * points[0].y - points[1].y;
        }
        switch (Main.getState().mode) {
            case RECTANGLE:
                normalize(points[0], points[1]);
                try {
                    Main.frame.getShapes().add(new Rectangle(points, Main.getState()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case OVAL:
                normalize(points[0], points[1]);
                try {
                    Main.frame.getShapes().add(new Oval(points, Main.getState()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LINE:
                try {
                    Main.frame.getShapes().add(new Line(points, Main.getState()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PEN:
                if (Main.getState().mode == State.Mode.PEN) {
                    try {
                        Main.frame.getShapes().add(new Line(new java.awt.Point[] { start, current }, Main.getState()));
                        start = current;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ROTATE:
                rotate = false;
                break;
            case MOVE:
            case POINT:
            default:
                //nothing
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        //Update so current shape is good
        current = new java.awt.Point(mouseEvent.getX(), mouseEvent.getY());
        current = Main.getState().view.viewToWorld(current);
        if (Main.getState().mode == State.Mode.PEN) {
            try {
                Main.frame.getShapes().add(new Line(new java.awt.Point[] { start, current }, Main.getState()));
                start = current;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dragged = true;
        repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (Main.frame != null) {
            Main.getState().view.setWidth(e.getComponent().getSize().width);
            Main.getState().view.setHeight(e.getComponent().getSize().height);
        }
    }

    //Functions that we must implement due to our use of interfaces but that have no use in our particular case

    @Override
    public void mouseMoved(MouseEvent e) {
        //None
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //None
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //None
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //None
    }

    @Override
    public void componentShown(ComponentEvent e) {
        //None
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //None
    }
}
