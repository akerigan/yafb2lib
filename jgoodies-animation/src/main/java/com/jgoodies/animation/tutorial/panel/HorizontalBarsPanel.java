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

package com.jgoodies.animation.tutorial.panel;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.animation.renderer.BasicTextRenderer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * A Swing component that consists of a top, center, bottom separated
 * by horizontal bars. These bars can be moved up and down.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 * 
 * @see BasicTextRenderer
 */
public final class HorizontalBarsPanel extends JPanel {
    
    // Names of the bound bean properties *************************************
    
    public static final String PROPERTYNAME_COLOR    = "color";
    public static final String PROPERTYNAME_FRACTION = "fraction";

    
    private double fraction;
    
    // Instance Creation ******************************************************

    /**
     * Constructs a panel with horizontal bars and 
     * the given component in its center.
     * 
     * @param centerComponent   the component in the panel's center
     */
    public HorizontalBarsPanel(JComponent centerComponent) {
        this(null, centerComponent, null);
    }
    
    
    /**
     * Constructs a panel with horizontal bars and 
     * the given component in its center.
     * 
     * @param topComponent      the component in the panel's top
     * @param centerComponent   the component in the panel's center
     * @param bottomComponent   the component in the panel's bottom
     */
    public HorizontalBarsPanel(
            JComponent topComponent,
            JComponent centerComponent,
            JComponent bottomComponent) {
        super(new FormLayout(
                "fill:pref:grow",
                "fill:0:grow, 6dlu, fill:0:grow, 6dlu, fill:0:grow"));
        CellConstraints cc = new CellConstraints();
        if (topComponent != null){
            add(topComponent, cc.xy(1, 1));
        }
        add(createFiller(),     cc.xy(1, 2));
        add(centerComponent, cc.xy(1, 3));
        add(createFiller(),     cc.xy(1, 4));
        if (bottomComponent != null) {
            add(bottomComponent, cc.xy(1, 5));
        }
        setFraction(0.45);
    }
    
    
    // Public Accessors *******************************************************

    public double getFraction() {
        return fraction;
    }
    
    
    public void setFraction(double newFraction) {
        double oldFraction = getFraction();
        fraction = newFraction;
        getFormLayout().setRowSpec(1, createOuterRowSpec(newFraction));
        getFormLayout().setRowSpec(3, createCenterRowSpec(newFraction));
        getFormLayout().setRowSpec(5, createOuterRowSpec(newFraction));
        firePropertyChange(PROPERTYNAME_FRACTION, oldFraction, newFraction);
        revalidate();
        repaint();
    }
    
    
    // Helper Code ************************************************************
    
    private JComponent createFiller() {
        JPanel bar = new JPanel(null);
        bar.setBackground(Color.GRAY);
        return bar;
    }
    
    
    private FormLayout getFormLayout() {
        return (FormLayout) getLayout();
    }
    
    private RowSpec createOuterRowSpec(double aFraction) {
        return new RowSpec(RowSpec.FILL, Sizes.ZERO, aFraction);
    }

    private RowSpec createCenterRowSpec(double aFraction) {
        return new RowSpec(RowSpec.FILL, Sizes.ZERO, 1.0d - aFraction);
    }

    
   
}