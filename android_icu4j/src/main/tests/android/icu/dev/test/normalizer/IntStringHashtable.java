/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
package android.icu.dev.test.normalizer;

import java.util.HashMap;
import java.util.Map;
import android.icu.testsharding.MainTestShard;


/**
 *******************************************************************************
 * Copyright (C) 1998-2010, International Business Machines Corporation and    *
 * Unicode, Inc. All Rights Reserved.                                          *
 *******************************************************************************
 *
 * Integer-String hash table. Uses Java Hashtable for now.
 * @author Mark Davis
 */
 
@MainTestShard
public class IntStringHashtable {

    public IntStringHashtable (String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void put(int key, String value) {
        if (value == defaultValue) {
            table.remove(key);
        } else {
            table.put(key, value);
        }
    }

    public String get(int key) {
        String value = table.get(key);
        if (value == null) return defaultValue;
        return value;
    }

    private String defaultValue;
    private Map<Integer, String> table = new HashMap<Integer, String>();
}
