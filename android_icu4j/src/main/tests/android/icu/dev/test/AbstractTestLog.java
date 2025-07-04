/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
/**
 *******************************************************************************
 * Copyright (C) 2003-2011, International Business Machines Corporation and
 * others. All Rights Reserved.
 *******************************************************************************
 */
package android.icu.dev.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;

public abstract class AbstractTestLog implements TestLog {
    /**
     * Add a message.
     */
    public static final void log(String message) {
        // TODO(stuartg): turned off - causing OOM running under ant
        // Probably temporary - must decide what to do with these
        //System.out.print(message);
        //msg(message, LOG, true, false);
    }

    /**
     * Add a message and newline.
     */
    public static final void logln(String message) {
        // TODO(stuartg): turned off - causing OOM running under ant
        // Probably temporary - must decide what to do with these
        //System.out.println(message);
        //msg(message, LOG, true, true);
    }

    /**
     * Report an error.
     */
    public static final void err(String message) {
        Assert.fail(message);
        //msg(message, ERR, true, false);
    }

    /**
     * Report an error and newline.
     */
    public static final void errln(String message) {
        Assert.fail(message);
        //msg(message, ERR, true, true);
    }

    /**
     * Report a warning (generally missing tests or data).
     */
    public static final void warn(String message) {
        Assert.fail(message);
        // TODO(stuartg): turned off - causing OOM running under ant
        //System.out.print(message);
        //msg(message, WARN, true, false);
    }

    /**
     * Report a warning (generally missing tests or data) and newline.
     */
    public static final void warnln(String message) {
        Assert.fail(message);
        // TODO(stuartg): turned off - causing OOM running under ant
        //System.out.println(message);
        //msg(message, WARN, true, true);
    }

    /**
     * Vector for logging.  Callers can force the logging system to
     * not increment the error or warning level by passing false for incCount.
     *
     * @param message the message to output.
     * @param level the message level, either LOG, WARN, or ERR.
     * @param incCount if true, increments the warning or error count
     * @param newln if true, forces a newline after the message
     */
    //public abstract void msg(String message, int level, boolean incCount, boolean newln);

    public boolean isDateAtLeast(int year, int month, int day){
        Date now = new Date();
        Calendar c = new GregorianCalendar(year, month, day);
        Date dt = c.getTime();
        if(now.compareTo(dt)>=0){
            return true;
        }
        return false;
    }
}
