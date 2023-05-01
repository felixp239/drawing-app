package com.company.shapes;

import com.company.panel.State;
import com.company.panel.View;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Rectangle extends Shape {
    protected java.awt.Point[]  definingPoints = new java.awt.Point[2]; //Contains 2 points: upper left point and lower right point of the rectangle
    protected Color outlineColor;
    protected int       outlineWidth;
    protected boolean   filled;
    protected Color     fillColor;
    protected boolean   rotated                 = false;    //Initially rectangle is not rotated
    protected float     angle                   = 0.0f;

    public Rectangle(java.awt.Point first, java.awt.Point second, Color outlineColor, int outlineWidth, boolean filled, Color fillColor, boolean rotated, float angle) {
        definingPoints[0]   = first;
        definingPoints[1]   = second;
        this.outlineColor   = outlineColor;
        this.outlineWidth   = outlineWidth;
        this.filled         = filled;
        this.fillColor      = fillColor;
        this.rotated        = rotated;
        this.angle          = angle;
    }

    public Rectangle(java.awt.Point[] points, State state) throws Exception {
        if (points.length != 2) {
            throw new Exception("Can't construct rectangle from number of points differing from 2");
        }
        definingPoints[0]   = points[0];
        definingPoints[1]   = points[1];
        outlineColor        = state.outlineColor;
        outlineWidth        = state.outlineWidth;
        filled              = state.fill;
        fillColor           = (state.fill) ? state.fillColor : null;
    }

    @Override
    public void draw(Graphics g, View view) {
        Graphics2D g2d = (Graphics2D) g;
        java.awt.Point[] viewPoints = { view.worldToView(definingPoints[0]), view.worldToView(definingPoints[1]) };
        if (rotated) {
            g2d.rotate(-angle);
            viewPoints[1]   = new java.awt.Point(viewPoints[1].x - viewPoints[0].x, viewPoints[1].y - viewPoints[0].y);
            viewPoints[0]   = new java.awt.Point(viewPoints[1].x + 2 * viewPoints[0].x, viewPoints[1].y + 2 * viewPoints[0].y);
            viewPoints[0]   = rotatePoint(viewPoints[0], -angle);
            viewPoints[1]   = addPoints(viewPoints[0], viewPoints[1]);
            viewPoints[0]   = new java.awt.Point(2 * viewPoints[0].x - viewPoints[1].x, 2 * viewPoints[0].y - viewPoints[1].y);
            viewPoints[0].x /= 2;
            viewPoints[0].y /= 2;
            viewPoints[1].x /= 2;
            viewPoints[1].y /= 2;
        }
        if (filled) {
            g2d.setColor(fillColor);
            g2d.fillRect(viewPoints[0].x, viewPoints[0].y,
                    viewPoints[1].x - viewPoints[0].x,
                    viewPoints[1].y - viewPoints[0].y);
        }
        g2d.setColor(outlineColor);
        g2d.setStroke(new BasicStroke(outlineWidth / view.getScale()));
        g2d.drawRect(viewPoints[0].x, viewPoints[0].y,
                viewPoints[1].x - viewPoints[0].x,
                viewPoints[1].y - viewPoints[0].y);
        if (rotated) {
            g2d.rotate(angle);
        }
    }
    @Override
    public void rotate(float angle) {   //Rotate line angle radians clockwise relative to the line center
        if (!rotated) {
            rotated     = true;
            this.angle  = 0;
        }
        this.angle  += angle;
    }
    @Override
    public void scale(float scaleFactor) {  //Scale line relative to the center
        java.awt.Point doubleCenter = addPoints(definingPoints[0], definingPoints[1]);  //Contains coordinates of center but doubled
        //(so we don't lose data do to integer division)
        definingPoints[0] = new java.awt.Point(
                (int) (scaleFactor * (2 * definingPoints[0].x - doubleCenter.x)),
                (int) (scaleFactor * (2 * definingPoints[0].y - doubleCenter.y))
        );
        definingPoints[0] = addPoints(doubleCenter, definingPoints[0]);
        definingPoints[0].x /= 2;
        definingPoints[0].y /= 2;

        definingPoints[1] = new java.awt.Point(
                (int) (scaleFactor * (2 * definingPoints[1].x - doubleCenter.x)),
                (int) (scaleFactor * (2 * definingPoints[1].y - doubleCenter.y))
        );
        definingPoints[1] = addPoints(doubleCenter, definingPoints[0]);
        definingPoints[1].x /= 2;
        definingPoints[1].y /= 2;
    }
    @Override
    public void fill(Color color) {
        filled      = true;
        fillColor   = color;
    }

    @Override
    public java.awt.Point getCenter() {
        return new java.awt.Point((definingPoints[0].x + definingPoints[1].x) / 2, (definingPoints[0].y + definingPoints[1].y) / 2);
    }

    @Override
    public String getDescription() {
        String description = "3 "; // 3 corresponds to rectangle
        description += definingPoints[0].x + " " + definingPoints[0].y + " " + definingPoints[1].x + " " + definingPoints[1].y + " ";
        description += outlineColor.getRed() + " " + outlineColor.getGreen() + " " + outlineColor.getBlue() + " ";
        description += outlineWidth + " ";
        description += ((filled) ? 1 : 0) + " ";
        description += ((filled) ? fillColor.getRed() + " " + fillColor.getGreen() + " " + fillColor.getBlue() : "0 0 0") + " ";
        description += ((rotated) ? 1 : 0) + " ";
        description += ((rotated) ? angle : 0.0f);
        return description;
    }
}
