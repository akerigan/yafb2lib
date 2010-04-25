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
import com.jgoodies.animation.animations.BasicTextAnimation;
import com.jgoodies.animation.components.BasicTextLabel;
import com.jgoodies.animation.tutorial.TutorialUtils;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the features of the {@link BasicTextLabel}.
 * Consists of a preview panel that displays a BasicTextLabel,
 * and a tool panel for configuration the label's properties
 * and for running default animations on that label.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.10 $
 * 
 * @see com.jgoodies.animation.animations.BasicTextAnimation
 * @see com.jgoodies.animation.animations.BasicTextAnimations
 * @see com.jgoodies.animation.renderer.BasicTextRenderer
 */
public final class BasicTextExample extends Model {
    
    private static final String PROPERTYNAME_DURATION = "duration";
    
    private static final String INITIAL_TEXT = "BasicTextLabel";

    private BasicTextLabel textLabel;
    
    private JComponent textField;
    private JSlider alphaSlider;
    private JSlider scaleSlider;
    private JSlider spaceSlider;
    
    private int        duration;
    private JComponent durationField;
    private Action     fadeAction;
    private Action     scaleAction;
    private Action     spaceAction;
    

    // Self Starter ***********************************************************
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Animation Tutorial :: Basic Text");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new BasicTextExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    
    
    // Instance Creation ******************************************************
    
    public BasicTextExample() {
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
        // Setup a BasicTextLabel with dark gray, bold Tahoma 24 pt.
        textLabel  = new BasicTextLabel(INITIAL_TEXT);
        textLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        textLabel.setColor(Color.DARK_GRAY);
        
        // Create a bean adapter that vends property adapters.
        BeanAdapter beanAdapter = new BeanAdapter(textLabel, true);
        
        // Create a text field bound to the label's text.
        textField = BasicComponentFactory.createTextField(
                beanAdapter.getValueModel(BasicTextLabel.PROPERTYNAME_TEXT),
                false);

        // Create a slider for the color alpha value.
        alphaSlider = new JSlider();
        ValueModel colorAdapter = beanAdapter.getValueModel(BasicTextLabel.PROPERTYNAME_COLOR);
        alphaSlider.setModel(new BoundedRangeAdapter(
                new AlphaConverter(colorAdapter),
                0, 0, 255));
        
        // Create a slider for the text label's scale in interval [0.5, 3].
        scaleSlider = new JSlider();
        ValueModel scaleAdapter = beanAdapter.getValueModel(BasicTextLabel.PROPERTYNAME_SCALE);
        scaleSlider.setModel(new BoundedRangeAdapter(
                ConverterFactory.createFloatToIntegerConverter(scaleAdapter, 100),
                0, 50, 300));
        
        // Create a slider for the text glyph space in interval [-10, 20].
        spaceSlider = new JSlider();
        ValueModel spaceAdapter = beanAdapter.getValueModel(BasicTextLabel.PROPERTYNAME_SPACE);
        spaceSlider.setModel(new BoundedRangeAdapter(
                ConverterFactory.createFloatToIntegerConverter(spaceAdapter, 100),
                0, -1000, 2000));
        
        // Create a text field bound to the duration.
        durationField = BasicComponentFactory.createIntegerField(
                new PropertyAdapter(this, PROPERTYNAME_DURATION));
        
        // Create Actions for three default animations
        fadeAction  = new FadeAction();
        scaleAction = new ScaleAction();
        spaceAction = new SpaceAction();
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
        panel.add(textLabel, new CellConstraints());
        return panel;
    }
    
    
    public JComponent buildToolsPanel() {
        FormLayout layout = new FormLayout(
                "pref, 25dlu, pref",
                "fill:pref");
        //layout.setColumnGroups(new int[][]{{1, 3}});
        
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
        
        addSlider(builder, 5, "Alpha:", alphaSlider, "0",   "255");
        addSlider(builder, 7, "Scale:", scaleSlider, "0.5",   "3");
        addSlider(builder, 9, "Space:", spaceSlider, "-10",  "20");
        
        return builder.getPanel();
    }
    
    
    private JComponent buildAnimationsPanel() {
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, 40dlu, 0:grow",
                "p, 6dlu, p, 9dlu, p:grow");
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Animations", cc.xyw(1, 1, 4));
        builder.addLabel("Duration:",      cc.xy (1, 3));
        builder.add(durationField,         cc.xy (3, 3));
        builder.add(buildButtonBar(),      cc.xyw(1, 5, 4, "fill, bottom"));
        return builder.getPanel();
    }
    
    
    private JComponent buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(new JButton(fadeAction));
        builder.addRelatedGap();
        builder.addGridded(new JButton(scaleAction));
        builder.addRelatedGap();
        builder.addGridded(new JButton(spaceAction));
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
    
    
    // Animation Actions ************************************************************
    
    private void setActionsEnabled(boolean enabled) {
        fadeAction.setEnabled(enabled);
        scaleAction.setEnabled(enabled);
        spaceAction.setEnabled(enabled);
    }
    
    
    private abstract class AnimationAction extends AbstractAction {
        
        private AnimationAction(String text) {
            super(text);
        }
          
        public void actionPerformed(ActionEvent e) {
            Animation animation = createAnimation();
            int fps = 30;
            animation.addAnimationListener(new StartStopHandler());
            new Animator(animation, fps).start();
        }  
        
        abstract Animation createAnimation();
        
    }
    
    
    
    
    private final class FadeAction extends AnimationAction {
        
        private FadeAction() {
            super("Fade");
        }
          
        public Animation createAnimation() {
            return BasicTextAnimation.defaultFade(textLabel, 
                    getDuration(), textLabel.getText(), Color.DARK_GRAY);
        }  
        
    }
    
    
    private final class ScaleAction extends AnimationAction {
        private ScaleAction() {
            super("Scale");
        }
          
        public Animation createAnimation() {
            return BasicTextAnimation.defaultScale(textLabel, 
                    getDuration(), textLabel.getText(), Color.DARK_GRAY);
        }  
            
    }
    
    
    private final class SpaceAction extends AnimationAction {
        
        private SpaceAction() {
            super("Space");
        }
          
        public Animation createAnimation() {
            return BasicTextAnimation.defaultSpace(textLabel, 
                    getDuration(), textLabel.getText(), Color.DARK_GRAY);
        }  
    }
    

    /**
     * Disables the actions at animation start and enables them
     * when the animation stopped. Also restores the text label's text.
     */
    private final class StartStopHandler extends AnimationAdapter {
        
        private String text;
        
        public void animationStarted(AnimationEvent e) {
            setActionsEnabled(false);
            text = textLabel.getText();
        }
        
        public void animationStopped(AnimationEvent e) {
            setActionsEnabled(true);
            textLabel.setText(text);
        }
    }
}