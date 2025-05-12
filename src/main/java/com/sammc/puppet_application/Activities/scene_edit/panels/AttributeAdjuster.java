package com.sammc.puppet_application.activities.scene_edit.panels;


import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;

/**
 * Template class for a gui object that has a slider
 * and a spinner to adjust a value either intuitively 
 * or precisely. Should be instantiated with anonoyous
 * classes that reimplement the attribute specific methods.
 * @author sammc
 */
public class AttributeAdjuster extends JPanel {
    
    private JSpinner spinner;
    private JSlider slider;
    private double divisor;


    public AttributeAdjuster(String attribute_name, int max, int min, double divisor) {
        this.divisor = divisor;
        setVisible(true);
        setPreferredSize(new Dimension(200, 100));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        String title = attribute_name;
        if (divisor != 1) {
            title += " ( x/" + divisor + " )";
        }

        setBorder(BorderFactory.createTitledBorder(title));

        int range = max - min;
        
        // Slider
        slider = new JSlider();
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setValue(0);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setMajorTickSpacing(range / 2);
        slider.setMinorTickSpacing(range / 20);
        slider.setPaintTicks(true);

        // Spinner
        spinner = new JSpinner() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 20);
            }
        };
        spinner.setValue(0);

        // change listeners for the slider and the spinner
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            spinner.setValue(value);
            setValue();
        });
        spinner.addChangeListener(e -> {
            int value = (int) spinner.getValue();
            slider.setValue(value);
            setValue();
        });

        // Add components
        add(slider);
        add(spinner);

    }


 
    /*
     * Dummy methods to be overridden by anonymous classes
     */

    public void setValue() {
        // this method should be overridden by the anonymous class,
        // probably using getValue(), and then setting the value on
        // the entity equal to that.
    }

    // allow for double values
    public double getValue() {
        return (int) spinner.getValue() / divisor;
    }


    
}

