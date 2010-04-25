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

package com.jgoodies.animation.tests;

import junit.framework.TestCase;

import com.jgoodies.animation.AnimationFunction;
import com.jgoodies.animation.AnimationFunctions;


/**
 * A test case for class {@link AnimationFunctions}.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class AnimationFunctionsTest extends TestCase {
    
    private static Float FLOAT_VALUE1 = new Float(5.12f);
    private static Float FLOAT_VALUE2 = new Float(19.67f);
    private static Float FLOAT_VALUE3 = new Float(18.5f);
    private static Float FLOAT_VALUE4 = new Float(19.80f);
    
    private static float FROM_VALUE   = 1.0f;
    private static float TO_VALUE     = 3.0f;
    
    private static float[] KEY_TIMES  = {0.0f, 0.25f, 0.5f, 0.75f};
    
    private AnimationFunction constant1;
    private AnimationFunction constant2;
    private AnimationFunction discrete1;
    private AnimationFunction discrete2;
    private AnimationFunction linear;

    protected void setUp() throws Exception {
        super.setUp();
        constant1 = AnimationFunctions.constant(1000, FLOAT_VALUE1);
        constant2 = AnimationFunctions.constant(3000, FLOAT_VALUE2);
        discrete1 = AnimationFunctions.discrete(4000, new Object[]{
                        FLOAT_VALUE1, FLOAT_VALUE2, 
                        FLOAT_VALUE3, FLOAT_VALUE4}); 
        discrete2 = AnimationFunctions.discrete(4000, new Object[]{
                        FLOAT_VALUE1, FLOAT_VALUE2, 
                        FLOAT_VALUE3, FLOAT_VALUE4},
                        KEY_TIMES ); 
        linear = AnimationFunctions.fromTo(7000, FROM_VALUE, TO_VALUE);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        constant1 = null;
        constant2 = null;
        discrete1 = null;
        discrete2 = null;
        linear    = null;
    }
    
    /**
     * Checks if negative durations throw an IllegalArgumentException.
     */
    public void testIllegalDuration() {
        try {
            AnimationFunctions.constant(-100, new Integer(42));
            fail("Should prevent negative durations.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        }
    }

    /**
     * Checks if value request outside the duration throw an IllegalArgumentException.
     */
    public void testIllegalInterval() {
        try {
            constant1.valueAt(-100); 
            fail("#valueAt should forbid an invalid time (-100).");
        } catch (IllegalArgumentException e) {
            // The expected behavior.
        }
        try {
            constant1.valueAt(-1); 
            fail("#valueAt should forbid an invalid time (-1).");
        } catch (IllegalArgumentException e) {
            // The expected behavior.
        }
        try {
            constant1.valueAt(constant1.duration()); 
            fail("#valueAt should forbid an invalid time (duration).");
        } catch (IllegalArgumentException e) {
            // The expected behavior.
        }
    }

    /**
     * Checks that #constant answers an AnimationFunction that in turn
     * answers a sole value over the whole time interval.
     */
    public void testConstant() {
        long duration1 = constant1.duration();
        long step = duration1 / 10;
        for (long time = 0; time < duration1; time += step) {
            assertSame(
                "A constant function should answer a constant value.",
                constant1.valueAt(time),
                FLOAT_VALUE1);
        }
    }
        
    /**
     * Checks that #concat sums up the durations and answers the related values.
     */
    public void testConcat() {
        AnimationFunction concatenated =
            AnimationFunctions.concat(constant1, constant2);

        long duration1   = constant1.duration();
        long duration2   = constant2.duration();
        long durationSum = duration1 + duration2;    
        
        assertEquals("Concat does not sum up the durations.",
                   concatenated.duration(),
                   durationSum);        
            
        long t0 = 0;
        long t1 = duration1;
        long t2 = durationSum - 1;
        
        assertSame("concat.valueAt(" + (t0) + ") failed.",
                    concatenated.valueAt(t0),
                    FLOAT_VALUE1);
            
        assertSame("concat.valueAt(" + (t1 - 1) + ") failed.",
                    concatenated.valueAt(t1 -1),
                    FLOAT_VALUE1);
            
        assertSame("concat.valueAt(" + (t1) + ") failed.",
                    concatenated.valueAt(t1),
                    FLOAT_VALUE2);
            
        assertSame("concat.valueAt(" + (t2) + ") failed.",
                    concatenated.valueAt(t2),
                    FLOAT_VALUE2);

        try {
            concatenated.valueAt(durationSum); 
            fail("concat.valueAt(totalDuration) is illegal.");
        } catch (IllegalArgumentException e) {
            // The expected behavior.
        }
    }


    /**
     * Checks that a discrete animation function answers the correct values
     * over the duration.
     */
    public void testDiscrete() {
        long duration = discrete1.duration();
        long intervalLength = duration /4;
        long t0 = 0;
        long t1 = 1 * intervalLength;
        long t2 = 2 * intervalLength;
        long t3 = 3 * intervalLength;
        long t4 = duration - 1; 
        
        assertSame("discrete(" + t0 + ") failed", 
                   discrete1.valueAt(t0),
                   FLOAT_VALUE1);

        assertSame("discrete(" + (t1 - 1) + ") failed", 
                   discrete1.valueAt(t1 - 1),
                   FLOAT_VALUE1);

        assertSame("discrete(" + t1 + ") failed", 
                   discrete1.valueAt(t1),
                   FLOAT_VALUE2);

        assertSame("discrete(" + (t2 - 1) + ") failed", 
                   discrete1.valueAt(t2 - 1),
                   FLOAT_VALUE2);

        assertSame("discrete(" + t2 + ") failed", 
                   discrete1.valueAt(t2),
                   FLOAT_VALUE3);

        assertSame("discrete(" + (t3 - 1) + ") failed", 
                   discrete1.valueAt(t3 - 1),
                   FLOAT_VALUE3);

        assertSame("discrete(" + t3 + ") failed", 
                   discrete1.valueAt(t3),
                   FLOAT_VALUE4);

        assertSame("discrete(" + (t4) + ") failed", 
                   discrete1.valueAt(t4),
                   FLOAT_VALUE4);
    }

    /**
     * Checks that a discrete animation function answers the correct values
     * over the duration.
     */
    public void testDiscreteKeyTimes() {
        long duration = discrete2.duration();
        long t0 = 0;
        long t1 = (long) (duration * KEY_TIMES[1]);
        long t2 = (long) (duration * KEY_TIMES[2]);
        long t3 = (long) (duration * KEY_TIMES[3]);
        long t4 = duration - 1; 
        
        assertSame("discrete(" + t0 + ") failed", 
                   discrete1.valueAt(t0),
                   FLOAT_VALUE1);

        assertSame("discrete(" + (t1 - 1) + ") failed", 
                   discrete1.valueAt(t1 - 1),
                   FLOAT_VALUE1);

        assertSame("discrete(" + t1 + ") failed", 
                   discrete1.valueAt(t1),
                   FLOAT_VALUE2);

        assertSame("discrete(" + (t2 - 1) + ") failed", 
                   discrete1.valueAt(t2 - 1),
                   FLOAT_VALUE2);

        assertSame("discrete(" + t2 + ") failed", 
                   discrete1.valueAt(t2),
                   FLOAT_VALUE3);

        assertSame("discrete(" + (t3 - 1) + ") failed", 
                   discrete1.valueAt(t3 - 1),
                   FLOAT_VALUE3);

        assertSame("discrete(" + t3 + ") failed", 
                   discrete1.valueAt(t3),
                   FLOAT_VALUE4);

        assertSame("discrete(" + (t4) + ") failed", 
                   discrete1.valueAt(t4),
                   FLOAT_VALUE4);
    }

    /**
     * Checks that a linear animation function answers the correct values
     * over the duration.
     */
    public void testLinear() {
        if (!linear.valueAt(0).equals(new Float(FROM_VALUE)))
            fail("The linear function should answer the from value at t0.");

        Object expected = new Float(TO_VALUE);
        Object answered = linear.valueAt(linear.duration() - 1);
        if (!answered.equals(expected))
            fail("The linear function should answer the to value at t1." +
                "\nanswered=" + answered +
                "\nexpected=" + expected);
    }

}
