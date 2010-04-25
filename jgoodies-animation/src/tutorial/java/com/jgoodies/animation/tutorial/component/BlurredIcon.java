package com.jgoodies.animation.tutorial.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.jgoodies.binding.beans.Model;


public final class BlurredIcon extends Model implements Icon {
    
	public static final String EAGER_INITIALIZATION = "eager";
	public static final String LAZY_INITIALIZATION = "lazy";

    private static final int PAD = 0;

    // Parameters
	private final ImageIcon icon;
	private final long duration;
	private final int numOfBuffers;
    
    private int index; 
    private long time;
    
    // Cached data
	private boolean hasValidCache = false;

	private BufferedImage[] buffer = null;
    
    
    // Instance Creation ******************************************************
    
    public BlurredIcon(
            ImageIcon icon,
            long duration,
            int numOfBuffers) {
        this(icon, duration, numOfBuffers, EAGER_INITIALIZATION);
    }

    public BlurredIcon(
		ImageIcon icon,
		long duration,
		int numOfBuffers,
		String initializationMode) {
		this.icon = icon;
		this.duration = duration;
		this.numOfBuffers = numOfBuffers;
        index = 0;
		if (EAGER_INITIALIZATION.equals(initializationMode))
			validateCache();
	}
    
    // ************************************************************************
    
	private ConvolveOp createSimpleBlur() {
		float f = 1.0f / 8.9f;
		return new ConvolveOp(
			new Kernel(3, 3, new float[] { f, f, f, f, f, f, f, f, f }),
			ConvolveOp.EDGE_NO_OP,
			null);
	}
    
	private void ensureValidCache() {
		if (hasValidCache)
			return;

		validateCache();
	}
    
	private int computeIndex(long time) {
		float f = (float) time / duration;
		return (int) ((buffer.length - 1) * f);
	}
    
	private void initialize() {
		int iconWidth  = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		int paddedWidth  = iconWidth  + 2 * PAD;
		int paddedHeight = iconHeight + 2 * PAD;

		// Allocating the arrays.
		buffer = new BufferedImage[numOfBuffers];

		ConvolveOp blur = createSimpleBlur();

		// Initialize the image.
		for (int i = 0; i < numOfBuffers; i++) {
			buffer[i] = /*0 == i ? icon.getImage() :*/
			new BufferedImage(paddedWidth, paddedHeight, BufferedImage.TYPE_INT_ARGB);
			if (i == 0) {
				Graphics g = buffer[i].getGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, paddedWidth, paddedHeight);
				g.drawImage(icon.getImage(), PAD, PAD, null);
			} else {
				blur.filter(buffer[i - 1], buffer[i]);
			}
		}
	}
    
	public void release() {
		hasValidCache = false;
		buffer = null;
		//System.out.println("Releasing cache for text " + text);
	}
    
    private void validateCache() {
        initialize();
        hasValidCache = true;
        //System.out.println("Validating cache for text " + text);
    }
    
    
    // Implementing the Icon Interface ****************************************
    
    public int getIconWidth() {
        return icon.getIconWidth() + 2 * PAD;
    }
    
    public int getIconHeight() {
        return icon.getIconHeight() + 2 * PAD;
    }
    
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
		ensureValidCache();

		//int x0 = (width - buffer[0].getWidth()) / 2;
		//int y0 = (height - buffer[0].getHeight()) / 2;

		g.drawImage(buffer[getIndex()], x, y, null);
	}
    
    
    // API ********************************************************************
    
    public int getIndex() {
        return index;
    }
    
    public long getTime() {
        return time;
    }
    
    public void setTime(long newTime) {
        long oldTime = getTime();
        time = newTime;
        index = computeIndex(time);
        firePropertyChange("time", oldTime, newTime);
    }
    
}