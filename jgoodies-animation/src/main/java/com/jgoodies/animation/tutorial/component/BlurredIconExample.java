/*
 * Copyright (c) 2001-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.animation.tutorial.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.*;

import com.jgoodies.animation.*;
import com.jgoodies.animation.tutorial.TutorialUtils;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the BlurredIcon.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class BlurredIconExample extends Model {
    
    private static final String ICON_FILENAME = 
        "com/jgoodies/animation/tutorial/component/JavaOne.jpg";
    
    private static final long DURATION = 2000;
    
    private BlurredIcon blurredIcon;
    private JLabel      iconLabel;
    private JSlider timeSlider;
    private Action animateAction;
    

    // Self Starter ***********************************************************
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Animation Tutorial :: BlurredIcon");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new BlurredIconExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    
    
    // Instance Creation ******************************************************
    
    public BlurredIconExample() {
        initComponents();
    }
    
    
    // Component Creation and Initialization **********************************

    /**
     * Creates the text label, sliders and animation Actions.
     * Binds sliders to bound properties of the text label. 
     */
    private void initComponents() {
        // Load the JavaOne logo.
        URL url = getClass().getClassLoader().getResource(ICON_FILENAME);
        ImageIcon javaOneLogo = new ImageIcon(url);
        // Setup a BlurredIcon with a JavaOne logo.
        blurredIcon = new BlurredIcon(javaOneLogo, DURATION, 100);
        
        // Have a label that uses the blurred Icon
        iconLabel = new JLabel(blurredIcon);
        
        // Create a slider for the label's time in interval [0, 5000]
        timeSlider = new JSlider();
        ValueModel timeAdapter = new PropertyAdapter(blurredIcon, "time", true);
        timeSlider.setModel(new BoundedRangeAdapter(
                ConverterFactory.createLongToIntegerConverter(timeAdapter),
                0, 0, (int) DURATION));
        timeAdapter.addValueChangeListener(new PropertyChangeListener() {
            
            public void propertyChange(PropertyChangeEvent evt) {
                iconLabel.repaint();
            }
        });
        
        // Create an Action to animate the blur effect
        animateAction  = new AnimateAction();
    }


    // Building *************************************************************

    /**
     * Builds and returns a panel with the preview in the top 
     * and the tool panel in the bottom.
     * 
     * @return the built panel
     */
    private JComponent buildPanel() {
        FormLayout layout = new FormLayout(
                "fill:pref:grow",
                "fill:pref:grow, p, p");
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildPreviewPanel(), cc.xy(1, 1));
        builder.addSeparator("",         cc.xy(1, 2));
        builder.add(buildToolsPanel(),   cc.xy(1, 3));
        return builder.getPanel();
    }
    
    
    public JComponent buildPreviewPanel() {
        FormLayout layout = new FormLayout(
                "fill:200dlu:grow",
                "fill:150dlu:grow");
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        panel.add(iconLabel, new CellConstraints());
        return panel;
    }
    
    
    public JComponent buildToolsPanel() {
        FormLayout layout = new FormLayout(
                "pref, 3dlu, 50dlu, 12dlu, 50dlu",
                "pref");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Time",                cc.xy(1, 1));
        builder.add(timeSlider,                 cc.xy(3, 1));
        builder.add(new JButton(animateAction), cc.xy(5, 1));
        return builder.getPanel();
    }
    
    
    // Animation **************************************************************
    
    private Animation createAnimation() {
        Animation blurAnimation = new BlurAnimation(DURATION / 2);
        return Animations.sequential(Animations.reverse(blurAnimation), 
                                     blurAnimation);
    }
    
    
    private final class BlurAnimation extends AbstractAnimation {
        
        private BlurAnimation(long duration) {
            super(duration, true);
        }
        
        public void applyEffect(long time) {
            blurredIcon.setTime(time*2);
            iconLabel.repaint();
        }
        
    }
    
    
    // Animation Action *******************************************************
    
    private final class AnimateAction extends AbstractAction {
        
        private AnimateAction() {
            super("Animate");
        }
          
        public void actionPerformed(ActionEvent e) {
            Animation animation = createAnimation();
            int fps = 30;
            animation.addAnimationListener(new StartStopHandler());
            new Animator(animation, fps).start();
        }  
        
    }
    
        
    /**
     * Disables the animate action at animation start and
     * enables it when the animation stopped.
     */ 
    private final class StartStopHandler extends AnimationAdapter {
        
        public void animationStarted(AnimationEvent e) {
            animateAction.setEnabled(false);
        }
        
        public void animationStopped(AnimationEvent e) {
            animateAction.setEnabled(true);
        }
    }
    
}