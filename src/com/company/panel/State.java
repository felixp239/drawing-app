package com.company.panel;

import java.awt.Color;

public class State {        //Simple class allowing us to save, change and transfer our current settings
    enum Mode {
        PEN,
        POINT,
        LINE,
        OVAL,
        RECTANGLE,
        MOVE,
        ROTATE
    }
    public Mode     mode;
    public Color    outlineColor;
    public int      outlineWidth;
    public boolean  fill;
    public Color    fillColor;
    public View     view;

    public State(int width, int height) {
        mode            = Mode.POINT;
        outlineColor    = Color.black;
        outlineWidth    = 1;
        fill            = false;
        fillColor       = Color.black;
        view            = new View(0, 0, 1.f, width, height);
    }
}
