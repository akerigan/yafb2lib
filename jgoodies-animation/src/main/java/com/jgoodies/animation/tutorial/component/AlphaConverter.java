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

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;


/**
 * This implementation of <code>ValueModel</code> converts integers
 * between 0 and 255 to Color instances that use this integer as alpha value.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 * 
 * @see Color
 */

final class AlphaConverter extends AbstractConverter {


    /**
     * Constructs an <code>AlphaConverter</code> on the given subject.
     * 
     * @param subject     the underlying <code>ValueModel</code>
     */
    AlphaConverter(ValueModel subject) {
        super(subject);
    }

    /** 
     * Converts a Color to the color's alpha value.
     * 
     * @param subjectValue   the subject's Color
     * @return the color's alpha value
     * @throws ClassCastException if the subject value is not of type
     *     <code>Color</code>
     */
    public Object convertFromSubject(Object subjectValue) {
        Color color = (Color) subjectValue;
        return new Integer(color.getAlpha());
    }

    /** 
     * Converts an alpha value between 0 and 255 to a Color with that
     * integer as brightness.
     * 
     * @param newValue  the new alpha value
     * @throws ClassCastException if the new value is not of type
     *     <code>Integer</code>
     */
    public void setValue(Object newValue) {
        int alpha = ((Integer) newValue).intValue();
        Color oldColor = (Color) subject.getValue();
        subject.setValue(new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), alpha));
    }
}