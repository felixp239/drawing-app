package com.company.panel;

import com.company.Main;

import com.company.shapes.Point;

public class View {
    private java.awt.Point  start;      //Upper left corner of our view
    private float           scale;      //Scale of our view (equal to amount of scaled unit lengths that can be contained within regular unit length)
    private int             width;
    private int             height;

    public View(int x, int y, float scale, int width, int height) {
        start       = new java.awt.Point(x, y);
        this.scale  = scale;
        this.width  = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void move(java.awt.Point delta) {
        start.x += delta.x;
        start.y += delta.y;
    }

    public void zoom(float zoom) {
        java.awt.Point doubleCenter = new java.awt.Point(2 * start.x + (int) (scale * width), 2 * start.y + (int) (scale * height));
        scale = 1.f / zoom;
        doubleCenter.x -= (int) (scale * width);
        doubleCenter.x /= 2;
        doubleCenter.y -= (int) (scale * height);
        doubleCenter.y /= 2;
        start = doubleCenter;
        Main.frame.getPanel().repaint();
    }

    public java.awt.Point worldToView(java.awt.Point point) {
        java.awt.Point result = new java.awt.Point();
        result.x = (int) ((point.x - start.x) / scale);
        result.y = (int) ((point.y - start.y) / scale);
        return result;
    }

    public java.awt.Point viewToWorld(java.awt.Point point) {
        java.awt.Point result = new java.awt.Point();
        result.x = (int) (point.x * scale + start.x);
        result.y = (int) (point.y * scale + start.y);
        return result;
    }

}
