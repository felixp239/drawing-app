package com.company.panel;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import com.company.Main;
import com.company.shapes.Line;
import com.company.shapes.Oval;
import com.company.shapes.Shape;

public class Frame extends JFrame {
    private ArrayList<Shape>    shapes  = new ArrayList<>();
    private int                 width   = 800;
    private int                 height  = 600;
    private Panel               panel;
    public  State               state   = new State(width, height);
    private Slider              slider;

    public Frame(String title) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuPoint());
        menuBar.add(createModeSwitcher());
        menuBar.add(createOutlineColorSwitcher());
        menuBar.add(createOutlineWidthSwitcher());
        menuBar.add(createFillSwitch());
        menuBar.add(createFillColorSwitcher());
        menuBar.add(createZoomMenu());
        menuBar.add(createRemoveMenu());
        setJMenuBar(menuBar);
        panel = new Panel();
        add(panel);
        setVisible(true);
    }

    public Panel getPanel() {
        return panel;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    private JMenu createTest() {
        JMenu test = new JMenu("Test");
        JMenuItem testItem = new JMenuItem("Test item");
        test.add(testItem);
        testItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Shape shape : shapes) {
                    try {
                        shape.rotate(0.1f);
                        panel.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        return test;
    }
    private JMenu createMenuPoint () {
        JMenu menuPoint     = new JMenu("File");
        JMenuItem open      = new JMenuItem("Open");
        JMenuItem save      = new JMenuItem("Save");
        JMenuItem saveJpeg  = new JMenuItem("Save as Jpeg");
        JMenuItem exit      = new JMenuItem("Exit");
        menuPoint.add(open);
        menuPoint.add(save);
        menuPoint.add(saveJpeg);
        menuPoint.addSeparator();
        menuPoint.add(exit);
        JFileChooser fileChooser = new JFileChooser();  // Object that can allow us to choose file for reading
        open.addActionListener(new ActionListener() {
            // Action when we choose "Open"
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Choose file");          //Dialog window title
                if (fileChooser.showOpenDialog(null) ==
                        JFileChooser.APPROVE_OPTION){
                    try {
                        Scanner in = new Scanner(fileChooser.getSelectedFile());
                        shapes = new ArrayList<>();
                        int n = in.nextInt();
                        int     type;
                        Point[] points = new Point[2];
                        Color   outlineColor;
                        int     outlineWidth;
                        boolean filled;
                        Color   fillColor;
                        boolean rotated;
                        float   angle;
                        in.nextLine();
                        for (int i = 0; i < n; i++) {
                            type = in.nextInt();
                            switch (type) {
                                case 0:
                                    points[0]       = new Point(in.nextInt(), in.nextInt());
                                    points[1]       = new Point(in.nextInt(), in.nextInt());
                                    outlineColor    = new Color(in.nextInt(), in.nextInt(), in.nextInt());
                                    outlineWidth    = in.nextInt();
                                    rotated         = (in.nextInt() == 1);
                                    angle           = Float.parseFloat(in.nextLine());
                                    shapes.add(new com.company.shapes.Line(points[0], points[1], outlineColor, outlineWidth, rotated, angle));
                                    break;
                                case 1:
                                    points[0]       = new Point(in.nextInt(), in.nextInt());
                                    points[1]       = new Point(in.nextInt(), in.nextInt());
                                    outlineColor    = new Color(in.nextInt(), in.nextInt(), in.nextInt());
                                    outlineWidth    = in.nextInt();
                                    filled          = (in.nextInt() == 1);
                                    fillColor       = new Color(in.nextInt(), in.nextInt(), in.nextInt());
                                    rotated         = (in.nextInt() == 1);
                                    angle           = Float.parseFloat(in.nextLine());
                                    shapes.add(new com.company.shapes.Oval(points[0], points[1], outlineColor, outlineWidth, filled, fillColor, rotated, angle));
                                    break;
                                case 2:
                                    points[0]       = new Point(in.nextInt(), in.nextInt());
                                    outlineColor    = new Color(in.nextInt(), in.nextInt(), in.nextInt());
                                    shapes.add(new com.company.shapes.Point(points[0], outlineColor));
                                    break;
                                case 3:
                                    points[0]       = new Point(in.nextInt(), in.nextInt());
                                    points[1]       = new Point(in.nextInt(), in.nextInt());
                                    outlineColor    = new Color(in.nextInt(), in.nextInt(), in.nextInt());
                                    outlineWidth    = in.nextInt();
                                    filled          = (in.nextInt() == 1);
                                    fillColor       = new Color(in.nextInt(), in.nextInt(), in.nextInt());
                                    rotated         = (in.nextInt() == 1);
                                    angle           = Float.parseFloat(in.nextLine());
                                    shapes.add(new com.company.shapes.Rectangle(points[0], points[1], outlineColor, outlineWidth, filled, fillColor, rotated, angle));
                                    break;
                                default:
                                    break;
                            }
                        }
                        repaint();
                        in.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        save.addActionListener(new ActionListener() {
            // Action when we choose save
            @Override
            public void actionPerformed(ActionEvent e) {
                //Open file for saving
                fileChooser.setDialogTitle("Save");     //Dialog window title
                if (fileChooser.showSaveDialog(null) ==
                        JFileChooser.APPROVE_OPTION){   //if we chose file
                    try {
                        PrintStream out = new PrintStream(fileChooser.getSelectedFile());
                        out.println(shapes.size());
                        Shape shape;
                        for (int i = 0 ; i < shapes.size() ; i++) {
                            shape = shapes.get(i);
                            out.println(shape.getDescription());
                        }
                        out.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        saveJpeg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Save as Jpeg");     //Dialog window title
                if (fileChooser.showSaveDialog(null) ==
                        JFileChooser.APPROVE_OPTION){   //if we chose file
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = new Robot().createScreenCapture(panel.getBounds());
                        Graphics2D g2d = bufferedImage.createGraphics();
                        panel.paint(g2d);
                        ImageIO.write(bufferedImage, "jpeg", fileChooser.getSelectedFile());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        return menuPoint;
    }
    private JMenu createModeSwitcher () {
        JMenu modeSwitcher  = new JMenu("Mode");
        JMenuItem pen       = new JMenuItem("Pen");
        JMenuItem point     = new JMenuItem("Point");
        JMenuItem line      = new JMenuItem("Line");
        JMenuItem rectangle = new JMenuItem("Rectangle");
        JMenuItem oval      = new JMenuItem("Oval");
        modeSwitcher.add(pen);
        modeSwitcher.add(point);
        modeSwitcher.add(line);
        modeSwitcher.add(rectangle);
        modeSwitcher.add(oval);
        pen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.mode = State.Mode.PEN;
            }
        });
        point.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.mode = State.Mode.POINT;
            }
        });
        line.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.mode = State.Mode.LINE;
            }
        });
        rectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.mode = State.Mode.RECTANGLE;
            }
        });
        oval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.mode = State.Mode.OVAL;
            }
        });
        return modeSwitcher;
    }
    private JMenu createOutlineColorSwitcher () {
        JMenu outlineColorSwitcher  = new JMenu("Outline color");
        JMenuItem black             = new JMenuItem("Black");
        JMenuItem white             = new JMenuItem("White");
        JMenuItem red               = new JMenuItem("Red");
        JMenuItem green             = new JMenuItem("Green");
        JMenuItem blue              = new JMenuItem("Blue");
        JMenuItem yellow            = new JMenuItem("Yellow");
        JMenuItem custom            = new JMenuItem("Custom");
        outlineColorSwitcher.add(black);
        outlineColorSwitcher.add(white);
        outlineColorSwitcher.add(red);
        outlineColorSwitcher.add(green);
        outlineColorSwitcher.add(blue);
        outlineColorSwitcher.add(yellow);
        outlineColorSwitcher.addSeparator();
        outlineColorSwitcher.add(custom);
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.BLACK;
            }
        });
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.BLACK;
            }
        });
        white.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.WHITE;
            }
        });
        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.RED;
            }
        });
        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.GREEN;
            }
        });
        blue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.BLUE;
            }
        });
        yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.outlineColor = Color.YELLOW;
            }
        });
        custom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JColorChooser colorChooser  = new JColorChooser();
                state.outlineColor          = JColorChooser.showDialog(null,"Pick a color", state.outlineColor);
            }
        });
        return outlineColorSwitcher;
    }
    private JMenu createOutlineWidthSwitcher () {
        JMenu outlineWidthSwitcher = new JMenu("Outline width");
        outlineWidthSwitcher.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                slider = new Slider();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        return outlineWidthSwitcher;
    }
    private JMenu createFillSwitch () {
        JMenu fillSwitch    = new JMenu("Fill");
        JMenuItem on        = new JMenuItem("On");
        JMenuItem off       = new JMenuItem("Off");
        fillSwitch.add(on);
        fillSwitch.addSeparator();
        fillSwitch.add(off);
        on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fill = true;
            }
        });
        off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fill = false;
            }
        });
        return fillSwitch;
    }
    private JMenu createFillColorSwitcher () {
        JMenu fillColorSwitcher = new JMenu("Fill color");
        JMenuItem black         = new JMenuItem("Black");
        JMenuItem white         = new JMenuItem("White");
        JMenuItem red           = new JMenuItem("Red");
        JMenuItem green         = new JMenuItem("Green");
        JMenuItem blue          = new JMenuItem("Blue");
        JMenuItem yellow        = new JMenuItem("Yellow");
        JMenuItem custom        = new JMenuItem("Custom");
        fillColorSwitcher.add(black);
        fillColorSwitcher.add(white);
        fillColorSwitcher.add(red);
        fillColorSwitcher.add(green);
        fillColorSwitcher.add(blue);
        fillColorSwitcher.add(yellow);
        fillColorSwitcher.addSeparator();
        fillColorSwitcher.add(custom);
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.BLACK;
            }
        });
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.BLACK;
            }
        });
        white.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.WHITE;
            }
        });
        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.RED;
            }
        });
        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.GREEN;
            }
        });
        blue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.BLUE;
            }
        });
        yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                state.fillColor = Color.YELLOW;
            }
        });
        custom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JColorChooser colorChooser  = new JColorChooser();
                state.fillColor          = JColorChooser.showDialog(null,"Pick a color", state.fillColor);
            }
        });
        return fillColorSwitcher;
    }
    private JMenu createZoomMenu () {
        JMenu       zoomMenu            = new JMenu("Zoom");   //200 150 125 110 100 90 80 75 67 50
        JMenuItem   twoHundred          = new JMenuItem("200%");
        JMenuItem   hundredFifty        = new JMenuItem("150%");
        JMenuItem   hundredTwentyFive   = new JMenuItem("125%");
        JMenuItem   hundredTen          = new JMenuItem("110%");
        JMenuItem   hundred            = new JMenuItem("100%");
        JMenuItem   ninety              = new JMenuItem("90%");
        JMenuItem   eighty              = new JMenuItem("80%");
        JMenuItem   seventyFive         = new JMenuItem("75%");
        JMenuItem   sixtySeven          = new JMenuItem("67%");
        JMenuItem   fifty               = new JMenuItem("50%");
        zoomMenu.add(twoHundred);
        zoomMenu.add(hundredFifty);
        zoomMenu.add(hundredTwentyFive);
        zoomMenu.add(hundredTen);
        zoomMenu.add(hundred);
        zoomMenu.add(ninety);
        zoomMenu.add(eighty);
        zoomMenu.add(seventyFive);
        zoomMenu.add(sixtySeven);
        zoomMenu.add(fifty);
        twoHundred.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(2.f);
            }
        });
        hundredFifty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(1.5f);
            }
        });
        hundredTwentyFive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(1.25f);
            }
        });
        hundredTen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(1.1f);
            }
        });
        hundred.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(1.f);
            }
        });
        ninety.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(0.9f);
            }
        });
        eighty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(0.8f);
            }
        });
        seventyFive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(0.75f);
            }
        });
        sixtySeven.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(0.677f);
            }
        });
        fifty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.view.zoom(0.5f);
            }
        });
        return zoomMenu;
    }
    private JMenu createRemoveMenu() {
        JMenu removeMenu        = new JMenu("Remove");
        JMenuItem removeLast    = new JMenuItem("Remove last");
        JMenuItem removeAll     = new JMenuItem("Remove all");
        removeMenu.add(removeLast);
        removeMenu.addSeparator();
        removeMenu.add(removeAll);
        removeLast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!shapes.isEmpty()) {
                    shapes.remove(shapes.size() - 1);
                    panel.repaint();
                }
            }
        });
        removeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.clear();
                panel.repaint();
            }
        });
        return removeMenu;
    }
}
