package com.company;

import com.company.panel.Frame;
import com.company.panel.State;

public class Main {
    public static com.company.panel.Frame frame;

        public static void main(String[] args) {
        Main main = new Main();
    }

    public Main() {
        frame = new Frame("Drawing app");
    }

    public static State getState() {
        return frame.state;
    }
}
