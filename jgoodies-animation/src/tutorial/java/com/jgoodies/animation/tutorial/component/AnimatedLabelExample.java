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

import javax.swing.*;

import com.jgoodies.animation.components.AnimatedLabel;
import com.jgoodies.animation.tutorial.TutorialUtils;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the features of the {@link AnimatedLabel}.
 * Consists of a preview panel that displays an AnimatedLabel and a 
 * tools panel to edit its duration, animated state and to change the text.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.7 $
 */

public final class AnimatedLabelExample extends Model {
    
    private static final Color AMAZON_ORANGE = new Color(255, 142, 12);
    
    private static final String[] TEXTS = {
            "The AnimatedLabel", 
            "Uses an Animation",
            "That Blends Over",
            "Texts.",
            "YOU CAN CONFIGURE",
            "THE DURATION",
            "AND ANIMATED STATE"
    };

    /**
     * The current index in the above array of sample texts.
     */
    private int textIndex;
    
    /**
     * Refers to the animated label that is demonstrated in this example.
     */
    private AnimatedLabel animatedLabel;
    
    private JTextField durationField;
    private JComponent animatedBox;
    private Action     nextTextAction;
    

    // Self Starter ***********************************************************
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Animation Tutorial :: AnimatedLabel");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new AnimatedLabelExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    
    
    // Instance Creation ******************************************************
    
    public AnimatedLabelExample() {
        initComponents();
    }
    
    
    // Component Creation and Initialization **********************************

    /**
     * Creates the animated label, duration text field, a check box
     * for the animated state and an Action to set the new text.
     */
    private void initComponents() {
        textIndex = 0;
        // Setup an AnimatedLabel with an Amazon.com organe and a font
        // 16 pt larger than the dialog font.
        animatedLabel  = new AnimatedLabel(AMAZON_ORANGE, 16, TEXTS[textIndex]);
        
        // Create a bean adapter that vends property adapters.
        BeanAdapter beanAdapter = new BeanAdapter(animatedLabel, true);
        
        // Create a text field bound to the label's blend over duration.
        durationField = BasicComponentFactory.createIntegerField(
                beanAdapter.getValueModel(AnimatedLabel.PROPERTYNAME_DURATION));
        durationField.setColumns(5);
        animatedBox = BasicComponentFactory.createCheckBox(
                beanAdapter.getValueModel(AnimatedLabel.PROPERTYNAME_ANIMATED),
                "animated");
        
        // Create an Action to set the next text.
        nextTextAction  = new AnimateAction();
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
                "fill:p:grow, p, p");
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildPreviewPanel(), cc.xy(1, 1));
        builder.addSeparator("",         cc.xy(1, 2));
        builder.add(buildToolsPanel(),   cc.xy(1, 3));
        
        return builder.getPanel();
    }
    
    
    public JComponent buildPreviewPanel() {
        FormLayout layout = new FormLayout(
                "50dlu, left:200dlu:grow, 50dlu",
                "fill:100dlu:grow");
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        panel.add(animatedLabel, new CellConstraints(2, 1));
        return panel;
    }
    
    
    public JComponent buildToolsPanel() {
        FormLayout layout = new FormLayout(
                "p, 3dlu, p, 6dlu, p, 24dlu:grow, p",
                "p");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Duration:",  cc.xy(1, 1));
        builder.add(durationField,     cc.xy(3, 1));
        builder.add(animatedBox,       cc.xy(5, 1));
        builder.add(buildButtonBar(),  cc.xy(7, 1));
        return builder.getPanel();
    }
    
    
    private JComponent buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(new JButton(nextTextAction));
        return builder.getPanel();
    }
        
    
    // Animation Action *******************************************************
    
    private final class AnimateAction extends AbstractAction {
        
        private AnimateAction() {
            super("Next Text");
        }
          
        public void actionPerformed(ActionEvent e) {
            if (++textIndex == TEXTS.length)
                textIndex = 0;
            animatedLabel.setText(TEXTS[textIndex]);
        }  
        
    }
            
}