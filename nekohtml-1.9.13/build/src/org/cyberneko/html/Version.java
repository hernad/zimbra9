/* (C) Copyright 2002-2008, Andy Clark, Marc Guillemot. All rights reserved. */

package org.cyberneko.html;

/**
 * This class holds version information for the CyberNeko HTML Parser.
 *
 * @author Andy Clark, Marc Guillemot
 */
public class Version {

    /** Returns the version string. */
    public static String getVersion() { return "NekoHTML 1.9.13"; }

    /** Prints the version string to standard output. */
    public static void main(String[] argv) {
        System.out.println(getVersion());
    } // main(String[])

} // class Version