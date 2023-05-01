package com.company.panel;



import com.company.Main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Slider implements ChangeListener {
    JFrame frame;
    JPanel panel;
    JLabel label;
    JSlider slider;

    public Slider(){
        frame   = new JFrame("Slider");
        panel   = new JPanel();
        label   = new JLabel();
        slider  = new JSlider(0,100, Main.frame.state.outlineWidth);
        slider.setPreferredSize(new Dimension(400,200));
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setPaintLabels(true);
        slider.setFont(new Font("MV Boli", Font.PLAIN,15));
        slider.setOrientation(SwingConstants.VERTICAL);
        slider.addChangeListener(this);
        label.setText("Width: " + slider.getValue());
        panel.add(slider);
        panel.add(label);
        frame.add(panel);
        frame.setSize(50,300);
        frame.setVisible(true);
        panel.setFocusable(true);
        panel.requestFocus();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        label.setText("Width: "+ slider.getValue());
        Main.frame.state.outlineWidth = slider.getValue();
    }
}
