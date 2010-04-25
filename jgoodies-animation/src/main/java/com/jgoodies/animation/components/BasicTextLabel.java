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

package com.jgoodies.animation.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import com.jgoodies.animation.renderer.BasicTextRenderer;
import com.jgoodies.animation.renderer.HeightMode;

/**
 * A Swing text component that can change the text, x and y scaling, 
 * glyph space, x and y offset and alignment mode.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see BasicTextRenderer
 */
public final class BasicTextLabel extends JComponent {
    
    // Names of the bound bean properties *************************************
    
    public static final String PROPERTYNAME_COLOR       = "color";
    public static final String PROPERTYNAME_HEIGHT_MODE = "heightMode";
    public static final String PROPERTYNAME_SCALE       = "scale";
    public static final String PROPERTYNAME_SCALE_X     = "scaleX";
    public static final String PROPERTYNAME_SCALE_Y     = "scaleY";
    public static final String PROPERTYNAME_SPACE       = "space";
    public static final String PROPERTYNAME_TEXT        = "text";
    public static final String PROPERTYNAME_OFFSET_X    = "offsetX";
    public static final String PROPERTYNAME_OFFSET_Y    = "offsetY";

    
    /**
     * Refers to the object that renders the text onto a graphics2D.
     */
    private final BasicTextRenderer renderer;
    
    
    // Instance Creation ******************************************************

    /**
     * Constructs a animation text Swing label with an empty initial text.
     */
    public BasicTextLabel() {
        this("");
    }
    
    
    /**
     * Constructs a animation text Swing label for the given text.
     * 
     * @param text     the initial text to be displayed
     */
    public BasicTextLabel(String text) {
        renderer = new BasicTextRenderer(text);
    }
    
    
    // Public Accessors *******************************************************

    public Color getColor() {
        return renderer.getColor();
    }
    
    public HeightMode getHeightMode() {
        return renderer.getHeightMode();
    }
    
    public float getScale() {
        return Math.max(getScaleX(), getScaleX());
    }
    
    public float getScaleX() {
        return renderer.getScaleX();
    }
    
    public float getScaleY() {
        return renderer.getScaleY();
    }
    
    public float getSpace() {
        return renderer.getSpace();
    }
    
    public float getOffsetX() {
        return renderer.getOffsetX();
    }
    
    public float getOffsetY() {
        return renderer.getOffsetY();
    }
    
    public String getText() {
        return renderer.getText();
    }

    public void setColor(Color newColor) {
        Color oldColor = getColor();
        if (oldColor.equals(newColor))
            return;
        renderer.setColor(newColor);
        firePropertyChange(PROPERTYNAME_COLOR, oldColor, newColor);
        repaint();
    }

    public void setHeightMode(HeightMode heightMode) {
        HeightMode oldMode = getHeightMode();
        renderer.setHeightMode(heightMode);
        firePropertyChange(PROPERTYNAME_HEIGHT_MODE, oldMode, heightMode);
        repaint();
    }

    public void setScale(float newScale) {
        float oldScale = getScale();
        renderer.setScaleX(newScale);
        renderer.setScaleY(newScale);
        firePropertyChange(PROPERTYNAME_SCALE, oldScale, newScale);
        repaint();
    }

    public void setScaleX(float newScaleX) {
        float oldScaleX = getScaleX();
        if (oldScaleX == newScaleX)
            return;
        float oldScale = getScale();
        renderer.setScaleX(newScaleX);
        firePropertyChange(PROPERTYNAME_SCALE_X, oldScaleX, newScaleX);
        firePropertyChange(PROPERTYNAME_SCALE, oldScale, getScale());
        repaint();
    }

    public void setScaleY(float newScaleY) {
        float oldScaleY = getScaleY();
        if (oldScaleY == newScaleY)
            return;
        float oldScale = getScale();
        renderer.setScaleY(newScaleY);
        firePropertyChange(PROPERTYNAME_SCALE_Y, oldScaleY, newScaleY);
        firePropertyChange(PROPERTYNAME_SCALE, oldScale, getScale());
        repaint();
    }

    public void setSpace(float newSpace) {
        float oldSpace = getSpace();
        if (oldSpace == newSpace)
            return;
        renderer.setSpace(newSpace);
        firePropertyChange(PROPERTYNAME_SPACE, oldSpace, newSpace);
        repaint();
    }

    public void setOffsetX(float offsetX) {
        float oldOffsetX = getOffsetX();
        renderer.setOffsetX(offsetX);
        firePropertyChange(PROPERTYNAME_OFFSET_X, oldOffsetX, offsetX);
        repaint();
    }

    public void setOffsetY(float offsetY) {
        float oldOffsetY = getOffsetY();
        renderer.setOffsetY(offsetY);
        firePropertyChange(PROPERTYNAME_OFFSET_Y, oldOffsetY, offsetY);
        repaint();
    }

    public void setText(String newText) {
        String oldText = getText();
        if (oldText.equals(newText))
            return;
        renderer.setText(newText);
        firePropertyChange(PROPERTYNAME_TEXT, oldText, newText);
        repaint();
    }

    
    // Painting ***************************************************************
    
    /**
     * Paints the component. Enabled anti-aliasing and sets high quality hints,
     * then renderers the component via the underlying renderer.
     * 
     * @param g    the Graphics object to render on
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);

        renderer.setFont(getFont());
        renderer.render(g2, getWidth(), getHeight());
    }
}