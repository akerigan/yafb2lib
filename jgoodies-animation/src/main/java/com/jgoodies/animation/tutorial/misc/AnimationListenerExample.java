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

package com.jgoodies.animation.tutorial.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import com.jgoodies.animation.*;
import com.jgoodies.animation.animations.BasicTextAnimation;
import com.jgoodies.animation.components.BasicTextLabel;
import com.jgoodies.animation.tutorial.TutorialUtils;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** 
 * A test class for a bunch of animations.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.6 $
 * 
 * @see Animation
 * @see BasicTextLabel
 */
public final class AnimationListenerExample {
    
    private static final int DEFAULT_FRAME_RATE = 30;
	
	private JLabel		 	reportLabel;
	private BasicTextLabel label1;
	private BasicTextLabel label2;
	private BasicTextLabel label3;

	
	public static void main(String[] args) {
		// Try to get the framerate from the command-line.
		int fps = DEFAULT_FRAME_RATE;
		if (args.length > 0) {
			try {
				fps = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
                System.out.println("Could not parse the custom frame rate: " + args[0]);
            }
		}
		System.out.println("The desired framerate is " + fps + '.');	

        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Animation Tutorial :: Animation Listener");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        AnimationListenerExample example = new AnimationListenerExample();
        JComponent panel = example.buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
        example.startAnimation(fps);
	}
	
	
	/**
	 * Constructs an animation demo using the specified frame rate.
	 */
	private AnimationListenerExample() {
		initComponents();
    }
    

    /**
     * @param fps   the frames per second
     */
    private void startAnimation(int fps) {
        Runnable runnable = new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, "The End.");
            }
        };
        Animation animation = createComposedAnimation();
        AnimationUtils.invokeOnStop(animation, runnable);
		Animator animator = new Animator(animation, fps);
		animator.start();
	}


	// Building ***************************************************************
	
	/**
	 * Creates and configures the components used by the animations.
	 */
	private void initComponents() {
		Font font = new Font("dialog", Font.BOLD, 16);

		reportLabel = new JLabel();

		label1 = new BasicTextLabel(" ");
		label1.setFont(font);

		label2 = new BasicTextLabel(" ");
		label2.setFont(font);
		
		label3 = new BasicTextLabel(" ");
		label3.setFont(font);
	}
	

	/**
	 * Builds and returns the frame's content pane.
     * 
     * @return the built content
	 */
	private JComponent buildPanel() {
        initComponents();
        
        FormLayout layout = new FormLayout(
                "fill:max(250dlu;pref):grow",
                "fill:pref:grow, 6dlu, f:40dlu, 6dlu, f:40dlu, 6dlu, f:40dlu, 6dlu");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.getPanel().setBackground(Color.WHITE);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(reportLabel, cc.xy(1, 1));
        builder.add(label1,      cc.xy(1, 3));
        builder.add(label2,      cc.xy(1, 5));
        builder.add(label3,      cc.xy(1, 7));
        return builder.getPanel();
	}


	// Animation Creation *****************************************************
		
	/**
	 * Creates and returns a parallel animation that is composed of 
     * four animations: report, fade, scaling, and spacing.
     * 
     * @return the composed animation
	 */
	private Animation createComposedAnimation() {
		List animations = new LinkedList();
		
		animations.add(Animations.repeat(5f, new SimpleReportAnimation(reportLabel, 2000)));
		animations.add(BasicTextAnimation.defaultFade(label1, 
													  5000,
													  "Default Fade", 
													  Color.DARK_GRAY));
		animations.add(BasicTextAnimation.defaultScale(label2,  
													   4000,
													   "Default Scale", 
													   Color.DARK_GRAY));
		animations.add(BasicTextAnimation.defaultSpace(label3, 3000,
													   "Default Space", 
													   Color.DARK_GRAY));
		
		int count = 1;	
		for (Iterator i = animations.iterator(); i.hasNext();) {
			Animation element = (Animation) i.next();
			element.addAnimationListener(new AnimationLogger("Animation " + count++));
		}
		
		Animation composedAnimation = Animations.parallel(animations);
		composedAnimation.addAnimationListener(new AnimationLogger("Composed animation"));
		return composedAnimation;
	}


	// Helper Code ***********************************************************
	
	private static final class SimpleReportAnimation extends AbstractAnimation {
	
		private final JLabel target;
		
		private long frameNumber =  0;
		private long startTime   = -1;
	
		/**
		 * Constructs a simple report animation.
         * 
         * @param target   the JLabel used to display
         * @param duration the animation's duration
		 */
		private SimpleReportAnimation(JLabel target, long duration) {
			super(duration);
			this.target = target;
		}
	
		/**
		 * Applies the animation effect: sets a text for the time, total time, 
		 * iteration, frame number, and - after a second - the total frame rate.
         * 
         * @param time   the current time
		 */
		protected void applyEffect(long time) {
			frameNumber++;
			long  totalTime = totalTime();  
			float fps       = totalTime < 1000 ? 0 : frameNumber * 1000 / totalTime;
			int   iteration = (int) (totalTime / duration());

			target.setText(
				"time=" + time / 1000 + "s"
				+ "; total=" + totalTime / 1000 + "s"
				+ "; iteration=" + iteration 
				+ "; frame=" + frameNumber
				+ "; fps= " + fps);
		}
		
		private long totalTime() {
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			}
			return System.currentTimeMillis() - startTime;
		}
		
	}
	
	
	/**
     * Logs animation events to the console.
     */
	private static final class AnimationLogger extends AnimationAdapter {
		private final String name;
		
		AnimationLogger(String name) { this.name = name; }
		
		public void animationStarted(AnimationEvent e) {
			System.out.println(name + " started.");
		}
		public void animationStopped(AnimationEvent e) {
			System.out.println(name + " stopped.");
		}
	}
	

}