/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.binding.beans;

/**
 * A runtime exception that is the abstract superclass for all exceptions
 * around Java Bean properties in the JGoodies Data Binding framework.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 * 
 * @see com.jgoodies.binding.beans.PropertyAdapter
 */
public abstract class PropertyException extends RuntimeException {

    // Instance Creation ******************************************************

    /**
     * Constructs a new exception instance with the specified detail message.
     * The cause is not initialized.
     * 
     * @param message the detail message. The detail message is saved for later
     *        retrieval by the {@link #getMessage()}method.
     */
    public PropertyException(String message) {
        this(message, null);
    }

    /**
     * Constructs a new exception instance with the specified detail message
     * and cause.
     * 
     * @param message the detail message (which is saved for later retrieval by
     *        the {@link #getMessage()}method).
     * @param cause the cause (which is saved for later retrieval by the
     *        {@link #getCause()}method). (A<code>null</code> value is
     *        permitted, and indicates that the cause is nonexistent or
     *        unknown.)
     */
    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }

}
