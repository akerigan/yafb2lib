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
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.jgoodies.animation.Animation;
import com.jgoodies.animation.AnimationAdapter;
import com.jgoodies.animation.AnimationEvent;
import com.jgoodies.animation.Animator;
import com.jgoodies.animation.animations.GlyphAnimation;
import com.jgoodies.animation.components.BasicTextLabel;
import com.jgoodies.animation.components.GlyphLabel;
import com.jgoodies.animation.tutorial.TutorialUtils;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the features of the {@link GlyphLabel}.
 * Consists of a preview panel that displays a GlyphLabel,
 * and a tool panel for configuration the label's properties
 * and for running animations on that label.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.10 $
 */

public final class GlyphLabelExample extends Model {
    
    private static final String PROPERTYNAME_DURATION = "duration";
    
    private static final String INITIAL_TEXT = "GlyphLabel";

    private GlyphLabel glyphLabel;
    
    private JComponent textField;
    private JSlider timeSlider;
    
    private int    duration;
    private Action animateAction;
    

    // Self Starter ***********************************************************
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Animation Tutorial :: Glyph Label");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new GlyphLabelExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    
    
    // Instance Creation ******************************************************
    
    public GlyphLabelExample() {
        duration = 4000;
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
    

    // Component Creation and Initialization **********************************

    /**
     * Creates the text label, sliders and animation Actions.
     * Binds sliders to bound properties of the text label. 
     */
    private void initComponents() {
        // Setup a GlyphLabel with dark gray, bold Tahoma 24 pt.
        glyphLabel = new GlyphLabel(INITIAL_TEXT, 4000, 150);
        glyphLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        //glyphLabel.setColor(Color.DARK_GRAY);
        
        // Create a bean adapter that vends property adapters.
        BeanAdapter beanAdapter = new BeanAdapter(glyphLabel, true);
        
        // Create a slider for the label's time in interval [0, 2000]
        timeSlider = new JSlider();
        ValueModel timeAdapter = beanAdapter.getValueModel(GlyphLabel.PROPERTYNAME_TIME);
        timeSlider.setModel(new BoundedRangeAdapter(
                ConverterFactory.createLongToIntegerConverter(timeAdapter),
                0, 0, 2000));
        
        textField = BasicComponentFactory.createTextField(
                beanAdapter.getValueModel(BasicTextLabel.PROPERTYNAME_TEXT),
                false);
        // Create a text field bound to the duration.
        //durationField = BasicComponentFactory.createIntegerField(
        //        new PropertyAdapter(this, PROPERTYNAME_DURATION));
        
        // Create an Action to animate the GlyphLabel
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
                "fill:100dlu:grow");
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        panel.add(glyphLabel, new CellConstraints());
        return panel;
    }
    
    
    public JComponent buildToolsPanel() {
        FormLayout layout = new FormLayout(
                "pref, 25dlu, pref",
                "fill:pref");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(buildPropertiesPanel(), cc.xy(1, 1));
        builder.add(buildAnimationsPanel(), cc.xy(3, 1));
        return builder.getPanel();
    }
    
        
    private JComponent buildPropertiesPanel() {
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, right:pref, 2dlu, 60dlu, 2dlu, right:pref",
                "p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, p");
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Properties", cc.xyw(1, 1, 7));
        builder.addLabel("Text:",          cc.xy (1, 3));
        builder.add(textField,             cc.xyw(3, 3, 5));
        
        addSlider(builder, 5, "Time:", timeSlider, "0",   "4000");
        return builder.getPanel();
    }

    
    private JComponent buildAnimationsPanel() {
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, 40dlu, 50dlu:grow",
                "p, 6dlu, p, 9dlu, p:grow");
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Animations", cc.xyw(1, 1, 4));
        //builder.addLabel("Duration:",      cc.xy (1, 3));
        //builder.add(durationField,         cc.xy (3, 3));
        builder.add(buildButtonBar(),      cc.xyw(1, 3, 4, "fill, bottom"));
        return builder.getPanel();
    }
    
    
    private JComponent buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(new JButton(animateAction));
        return builder.getPanel();
    }
        
    
    private void addSlider(PanelBuilder builder, int row, String title, JSlider slider,
            String leftText, String rightText) {
        CellConstraints cc = new CellConstraints();
        builder.addLabel(title,     cc.xy(1, row));
        builder.addLabel(leftText,  cc.xy(3, row));
        builder.add(slider,         cc.xy(5, row));
        builder.addLabel(rightText, cc.xy(7, row));
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
        
        private Animation createAnimation() {
            return new GlyphAnimation(
                    glyphLabel, 
                    getDuration(),
                    150, // Ignored
                    glyphLabel.getText());
        }  
        
    }
    
        
    /**
     * Disables the actions at animation start and enables them
     * when the animation stopped. Also restores the text label's text.
     */
    private class StartStopHandler extends AnimationAdapter {
        
        private String text;
        
        public void animationStarted(AnimationEvent e) {
            animateAction.setEnabled(false);
            text = glyphLabel.getText();
        }
        
        public void animationStopped(AnimationEvent e) {
            animateAction.setEnabled(true);
            glyphLabel.setText(text);
        }
    }
    
}