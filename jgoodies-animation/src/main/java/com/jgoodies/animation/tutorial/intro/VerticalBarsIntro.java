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

package com.jgoodies.animation.tutorial.intro;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import com.jgoodies.animation.*;
import com.jgoodies.animation.components.BasicTextLabel;
import com.jgoodies.animation.tutorial.TutorialUtils;
import com.jgoodies.animation.tutorial.panel.VerticalBarsPanel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An intro that combines a VerticalBarsPanel with a BasicTextLabel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.8 $
 */

public final class VerticalBarsIntro extends Model {
    
    private static final Color JAVAONE_BLUE = new Color(46, 49, 146);
//    private static final String ZOINK_FILENAME = 
//        "com/jgoodies/animation/tutorial/sound/mp3/zoink.mp3";
    
    private static final String PROPERTYNAME_DURATION = "duration";
    private static final String PROPERTYNAME_PAUSE    = "pause";
    
    
    private VerticalBarsPanel verticalBarsPanel;
    private BasicTextLabel    textLabel;
    
    private int duration;
    private long pause;
    
    private JComponent durationField;
    private JComponent pauseField;
    private Action animateAction;
    

    // Self Starter ***********************************************************
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Animation Tutorial :: Vertical Bars Intro");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new VerticalBarsIntro().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    
    
    // Instance Creation ******************************************************
    
    public VerticalBarsIntro() {
        duration = 350;
        pause = 2000;
        initComponents();
    }
    
    
    // Bound Properties *******************************************************
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int newDuration) {
        int oldDuration = getDuration();
        duration = newDuration;
        firePropertyChange(PROPERTYNAME_DURATION, oldDuration, newDuration);
    }
    

    public long getPause() {
        return pause;
    }
    
    public void setPause(long newPause) {
        long oldPause = getPause();
        pause = newPause;
        firePropertyChange(PROPERTYNAME_PAUSE, oldPause, newPause);
    }
    

    // Component Creation and Initialization **********************************

    /**
     * Creates the text label, sliders and animation Actions.
     * Binds sliders to bound properties of the text label. 
     */
    private void initComponents() {
        // Setup a BasicTextLabel with blue, bold Tahoma 20 pt.
        textLabel  = new BasicTextLabel(" ");
        textLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        textLabel.setColor(JAVAONE_BLUE);
        
        verticalBarsPanel = new VerticalBarsPanel(textLabel);
        verticalBarsPanel.setBackground(Color.WHITE);
        verticalBarsPanel.setFraction(0.2);
        
        durationField = BasicComponentFactory.createIntegerField(
                new PropertyAdapter(this, PROPERTYNAME_DURATION));
        pauseField = BasicComponentFactory.createIntegerField(
                new PropertyAdapter(this, PROPERTYNAME_PAUSE));
        // Create an Action to animate the bars
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
                "fill:275dlu:grow",
                "50dlu:grow, fill:50dlu, 50dlu:grow");
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        panel.add(verticalBarsPanel, new CellConstraints(1, 2));
        return panel;
    }
    
    
    public JComponent buildToolsPanel() {
        FormLayout layout = new FormLayout(
                "pref, 3dlu, 25dlu, 12dlu, pref, 3dlu, 25dlu, 12dlu:grow, 50dlu",
                "pref");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Duration",            cc.xy(1, 1));
        builder.add(durationField,              cc.xy(3, 1));
        builder.addLabel("Pause",               cc.xy(5, 1));
        builder.add(pauseField,                 cc.xy(7, 1));
        builder.add(new JButton(animateAction), cc.xy(9, 1));
        return builder.getPanel();
    }
    
        
    // Animation **************************************************************
    
    private Animation createAnimation() {
//        Animation soundAnimation = createSoundAnimation();

        String[] texts = new String[]{
                "With Swing", 
                "And Java2D", 
                "YOU can build",
                "Cool Java Apps", 
                "Today!"};
        textLabel.setText(texts[0]);
        List sequence = new LinkedList();
        sequence.add(Animations.pause(pause));
        for (int i = 1; i < texts.length; i++) {
            Animation moveBarsAnimation = 
                new MoveBarsAnimation(texts[i-1], texts[i], duration);
            sequence.add(moveBarsAnimation);
//            sequence.add(soundAnimation == null
//                    ? moveBarsAnimation
//                    : Animations.parallel(soundAnimation, moveBarsAnimation));
            sequence.add(Animations.pause(pause));
        }
        return Animations.sequential(sequence);
    }  
    
    
//    private Animation createSoundAnimation() {
//        try {
//            return new SoundAnimation(ZOINK_FILENAME, duration);
//        } catch (Exception e) {
//            System.out.println("Failed to create the sound animation.");
//            return null;
//        }
//    }
    
    
    private final class MoveBarsAnimation extends AbstractAnimation {
        
        private final AnimationFunction fractionFunction;
        private final AnimationFunction colorFunction;
        private final AnimationFunction stringFunction;
        
        private MoveBarsAnimation(String text1, String text2, long duration) {
            super(duration, true);
            fractionFunction = createFractionFunction(duration);
            colorFunction = AnimationFunctions.alphaColor(
                    createAlphaFunction(duration),
                    JAVAONE_BLUE);
            stringFunction = createStringFunction(text1, text2, duration);
        }
        
        private AnimationFunction createFractionFunction(long duration) {
            return AnimationFunctions.linear(
                    duration,
                    new Float[]{ new Float(0.4f), new Float(0.75d), new Float(0.4f)},
                    new float[]{ 0.0f, 0.5f, 1.0f});
        }
        
        private AnimationFunction createAlphaFunction(long duration) {
            return AnimationFunctions.linear(
                    duration,
                    new Integer[]{ new Integer(255), new Integer(0), new Integer(255)},
                    new float[]{ 0.0f, 0.5f, 1.0f});
        }
        
        private AnimationFunction createStringFunction(
                String text1, String text2, long duration) {
            return AnimationFunctions.discrete(
                    duration,
                    new String[]{text1, text2});
        }
        
        public void applyEffect(long time) {
            Float fraction = (Float) fractionFunction.valueAt(time);
            verticalBarsPanel.setFraction(fraction.doubleValue());
            textLabel.setColor((Color) colorFunction.valueAt(time));
            textLabel.setText((String) stringFunction.valueAt(time));
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