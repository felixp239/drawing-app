package com.company.shapes;

import java.awt.*;

import com.company.panel.View;

public abstract class Shape {
    protected java.awt.Point[]  definingPoints; //Defining points for a shape f. e.: starting and ending points for a line
    protected Color     outlineColor;           //Determines outline color
    protected int       outlineWidth;           //Determines outline width in pixels
    protected boolean   filled;                 //Determines whether shape is filled or not
    protected Color     fillColor;              //Color with which shape is filled if filled == true
    protected boolean   rotated;                //Determines whether shape is rotated or not
    protected float     angle;                  //Angle by which the shape is rotated clockwise

    public abstract void draw(Graphics g, View view);   //Function which draws the shape to g according to current view
    public abstract void rotate(float angle) throws Exception;
    public abstract void scale(float scaleFactor) throws Exception;
    public abstract void fill(Color color) throws Exception;

    public abstract java.awt.Point getCenter();

    public abstract String getDescription();

    public void setOutlineWidth(int outlineWidth) {
        this.outlineWidth = outlineWidth;
    }
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    protected java.awt.Point rotatePoint(java.awt.Point point, float angle) {
        java.awt.Point result = new java.awt.Point();
        result.x = (int) (point.x * Math.cos(angle) + point.y * Math.sin(angle));
        result.y = (int) (point.y * Math.cos(angle) - point.x * Math.sin(angle));
        return result;
    }
    protected java.awt.Point addPoints(java.awt.Point first, java.awt.Point second) {
        java.awt.Point result = new java.awt.Point();
        result.x = first.x + second.x;
        result.y = first.y + second.y;
        return result;
    }
    protected java.awt.Point subtractPoints(java.awt.Point first, java.awt.Point second) {
        java.awt.Point result = new java.awt.Point();
        result.x = first.x - second.x;
        result.y = first.y - second.y;
        return result;
    }

}