package com.company.shapes;

import com.company.Main;
import com.company.panel.State;
import com.company.panel.View;

import java.awt.*;

public class Point extends Shape {
    protected java.awt.Point[]  definingPoints = new java.awt.Point[1]; //Contains 1 points
    protected Color     outlineColor;
    protected int       outlineWidth        = 0;        //Point does not have an outline width
    protected boolean   filled              = false;    //Point cannot be filled
    protected Color     fillColor           = null;     //Point cannot be filled
    protected boolean   rotated             = false;    //Any rotated point is the point itself
    protected float     angle               = 0.0f;     //that's why point rotation functionality is deprecated

    public Point(java.awt.Point first, Color outlineColor) {
        definingPoints[0]   = first;
        this.outlineColor   = outlineColor;
    }

    public Point(java.awt.Point point, State state) throws Exception {
        definingPoints[0]   = point;
        outlineColor        = state.outlineColor;
    }

    @Override
    public void draw(Graphics g, View view) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(outlineColor);
        java.awt.Point[] viewPoints = { view.worldToView(definingPoints[0]) };
        int radius = (int) (0.005 * Math.sqrt(Math.pow(Main.getState().view.getWidth(), 2) + Math.pow(Main.getState().view.getHeight(), 2)));
        radius = 3;
        g2d.fillOval(viewPoints[0].x - radius,viewPoints[0].y - radius,2 * radius,2 * radius);
    }

    @Override
    public void rotate(float angle) {
        //Does nothing
    }

    @Override
    public void scale(float scaleFactor) throws Exception {
        throw new Exception("Point cannot be scaled");
    }

    @Override
    public void fill(Color color) throws Exception {
        throw new Exception("Point cannot be filled");
    }

    public int getX() {
        return definingPoints[0].x;
    }

    public void setX(int x) {
        definingPoints[0].x = x;
    }

    public int getY() {
        return definingPoints[0].y;
    }

    public void setY(int y) {
        definingPoints[0].y = y;
    }

    @Override
    public java.awt.Point getCenter() {
        return new java.awt.Point(definingPoints[0]);
    }

    @Override
    public String getDescription() {
        String description = "2 ";  // 2 corresponds to point
        description += definingPoints[0].x + " " + definingPoints[0].y + " ";
        description += outlineColor.getRed() + " " + outlineColor.getGreen() + " " + outlineColor.getBlue();
        return description;
    }
}
