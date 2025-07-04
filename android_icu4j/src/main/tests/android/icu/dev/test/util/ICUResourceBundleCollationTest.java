/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
/**
 *******************************************************************************
 * Copyright (C) 2009-2016, International Business Machines Corporation and
 * others. All Rights Reserved.
 *******************************************************************************
 */
package android.icu.dev.test.util;

import java.util.MissingResourceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.icu.dev.test.TestFmwk;
import android.icu.impl.ICUData;
import android.icu.impl.ICUResourceBundle;
import android.icu.text.Collator;
import android.icu.util.ULocale;
import android.icu.util.UResourceBundle;
import android.icu.testsharding.MainTestShard;

@MainTestShard
@RunWith(JUnit4.class)
public final class ICUResourceBundleCollationTest extends TestFmwk {
    private static final String COLLATION_RESNAME = "collations";
    private static final String COLLATION_KEYWORD = "collation";
    private static final String DEFAULT_NAME = "default";
    private static final String STANDARD_NAME = "standard";

    @Test
    public void TestFunctionalEquivalent(){
       String[] collCases = {
       // note: in ICU 64, empty locales are shown as available for collation
       //  avail   locale                               equiv
           "f",     "sv_US_CALIFORNIA",                 "sv",
           "f",     "zh_TW@collation=stroke",           "zh@collation=stroke", /* alias of zh_Hant_TW */
           "t",     "zh_Hant_TW@collation=stroke",      "zh@collation=stroke",
           "f",     "sv_CN@collation=pinyin",           "sv",
           "t",     "zh@collation=pinyin",              "zh",
           "f",     "zh_CN@collation=pinyin",           "zh", /* alias of zh_Hans_CN */
           "t",     "zh_Hans_CN@collation=pinyin",      "zh",
           "f",     "zh_HK@collation=pinyin",           "zh", /* alias of zh_Hant_HK */
           "t",     "zh_Hant_HK@collation=pinyin",      "zh",
           "f",     "zh_HK@collation=stroke",           "zh@collation=stroke", /* alias of zh_Hant_HK */
           "t",     "zh_Hant_HK@collation=stroke",      "zh@collation=stroke",
           "f",     "zh_HK",                            "zh@collation=stroke", /* alias of zh_Hant_HK */
           "t",     "zh_Hant_HK",                       "zh@collation=stroke",
           "f",     "zh_MO",                            "zh@collation=stroke", /* alias of zh_Hant_MO */
           "t",     "zh_Hant_MO",                       "zh@collation=stroke",
           "f",     "zh_TW_STROKE",                     "zh@collation=stroke",
           "f",     "zh_TW_STROKE@collation=zhuyin",    "zh@collation=zhuyin",
           "f",     "sv_CN@calendar=japanese",          "sv",
           "t",     "sv@calendar=japanese",             "sv",
           "f",     "zh_TW@collation=zhuyin",           "zh@collation=zhuyin", /* alias of zh_Hant_TW */
           "t",     "zh_Hant_TW@collation=zhuyin",      "zh@collation=zhuyin",
           "f",     "zh_TW@collation=unihan",           "zh@collation=unihan", /* alias of zh_Hant_TW */
           "t",     "zh_Hant_TW@collation=unihan",      "zh@collation=unihan",
           "f",     "zh_CN@collation=zhuyin",           "zh@collation=zhuyin", /* alias of zh_Hans_CN */
           "t",     "zh_Hans_CN@collation=zhuyin",      "zh@collation=zhuyin",
           "f",     "zh_CN@collation=unihan",           "zh@collation=unihan", /* alias of zh_Hans_CN */
           "t",     "zh_Hans_CN@collation=unihan",      "zh@collation=unihan",
           "t",     "zh@collation=zhuyin",              "zh@collation=zhuyin",
           "t",     "zh@collation=unihan",              "zh@collation=unihan",
           "t",     "hi@collation=standard",            "hi",
           "f",     "hi_AU@collation=standard;currency=CHF;calendar=buddhist",  "hi",
           "f",     "sv_SE@collation=pinyin",           "sv", /* bug 4582 tests */
           "f",     "sv_SE_BONN@collation=pinyin",      "sv",
           "t",     "nl",                               "root",
           "f",     "nl_NL",                            "root",
           "f",     "nl_NL_EEXT",                       "root",
           "t",     "nl@collation=stroke",              "root",
           "f",     "nl_NL@collation=stroke",           "root",
           "f",     "nl_NL_EEXT@collation=stroke",      "root",
       };

       logln("Testing functional equivalents for collation...");
       getFunctionalEquivalentTestCases(ICUData.ICU_COLLATION_BASE_NAME,
                                        Collator.class.getClassLoader(),
               COLLATION_RESNAME, COLLATION_KEYWORD, true, collCases);
    }

    @Test
    public void TestGetWithFallback(){
        /*
        UResourceBundle bundle =(UResourceBundle) UResourceBundle.getBundleInstance("com/ibm/icu/dev/data/testdata","te_IN");
        String key = bundle.getStringWithFallback("Keys/collation");
        if(!key.equals("COLLATION")){
            errln("Did not get the expected result from getStringWithFallback method.");
        }
        String type = bundle.getStringWithFallback("Types/collation/direct");
        if(!type.equals("DIRECT")){
            errln("Did not get the expected result form getStringWithFallback method.");
        }
        */
        ICUResourceBundle bundle = null;
        String key = null;
        try{
            bundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(
                    ICUData.ICU_COLLATION_BASE_NAME,
                    ULocale.canonicalize("de@collation=phonebook"));

            if(!bundle.getULocale().getName().equals("de")){
                errln("did not get the expected bundle");
            }
            key = bundle.getStringWithFallback("collations/collation/default");
            if(!key.equals("phonebook")){
                errln("Did not get the expected result from getStringWithFallback method.");
            }

        }catch(MissingResourceException ex){
            logln("got the expected exception");
        }


        bundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_COLLATION_BASE_NAME,"fr_FR");
        key = bundle.getStringWithFallback("collations/default");
        if(!key.equals("standard")){
            errln("Did not get the expected result from getStringWithFallback method.");
        }
    }

    @Test
    public void TestKeywordValues(){
        String kwVals[];
        boolean foundStandard = false;
        int n;

        logln("Testing getting collation values:");
        kwVals = ICUResourceBundle.getKeywordValues(ICUData.ICU_COLLATION_BASE_NAME,COLLATION_RESNAME);
        for(n=0;n<kwVals.length;n++) {
            logln(Integer.valueOf(n).toString() + ": " + kwVals[n]);
            if(DEFAULT_NAME.equals(kwVals[n])) {
                errln("getKeywordValues for collation returned 'default' in the list.");
            } else if(STANDARD_NAME.equals(kwVals[n])) {
                if(foundStandard == false) {
                    foundStandard = true;
                    logln("found 'standard'");
                } else {
                    errln("Error - 'standard' is in the keyword list twice!");
                }
            }
        }

        if(foundStandard == false) {
            errln("Error - 'standard' was not in the collation tree as a keyword.");
        } else {
            logln("'standard' was found as a collation keyword.");
        }
    }

    @Test
    public void TestGetFunctionalEquivalentVariantLengthWithinLimit() {
        String valid =
            "_" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678";

        ULocale equivLocale = ICUResourceBundle.getFunctionalEquivalent(
            ICUData.ICU_BASE_NAME, ICUResourceBundle.ICU_DATA_CLASS_LOADER,
            "calendar", "calendar", new ULocale(valid), new boolean[1], false);
        ULocale localeExpected = new ULocale("_@calendar=gregorian");
        if(!equivLocale.equals(localeExpected)) {
            errln("Get unexpected locale:" + equivLocale.toString() +
                " while expecting " + localeExpected.toString());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestGetFunctionalEquivalentVariantLengthOverLimit() {
        String invalid =
            "_" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678" +
            "_12345678X";  // One character too long.
        ULocale equivLocale2 = ICUResourceBundle.getFunctionalEquivalent(
            ICUData.ICU_BASE_NAME, ICUResourceBundle.ICU_DATA_CLASS_LOADER,
            "calendar", "calendar", new ULocale(invalid), new boolean[1], false);
    }

    @Test
    public void TestOpen(){
        UResourceBundle bundle = UResourceBundle.getBundleInstance(ICUData.ICU_COLLATION_BASE_NAME, "en_US_POSIX");
        if(bundle==null){
            errln("could not load the stream");
        }
    }

    private void getFunctionalEquivalentTestCases(String path, ClassLoader cl, String resName, String keyword,
            boolean truncate, String[] testCases) {
        //String F_STR = "f";
        String T_STR = "t";
        boolean isAvail[] = new boolean[1];

        logln("Testing functional equivalents...");
        for(int i = 0; i < testCases.length ;i+=3) {
            boolean expectAvail = T_STR.equals(testCases[i+0]);
            ULocale inLocale = new ULocale(testCases[i+1]);
            ULocale expectLocale = new ULocale(testCases[i+2]);

            logln("" + i/3 + ": " + expectAvail + "\t\t" +
                    inLocale + "\t\t" + expectLocale);

            ULocale equivLocale = ICUResourceBundle.getFunctionalEquivalent(path, cl, resName, keyword, inLocale, isAvail, truncate);
            boolean gotAvail = isAvail[0];

            if((gotAvail != expectAvail) || !equivLocale.equals(expectLocale)) {
                errln("" + i/3 + ":  Error, expected  Equiv=" + expectAvail + "\t\t" +
                        inLocale + "\t\t--> " + expectLocale + ",  but got " + gotAvail + " " +
                        equivLocale);
            }
        }
    }
}
