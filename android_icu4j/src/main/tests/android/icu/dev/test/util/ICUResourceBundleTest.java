/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
/**
 *******************************************************************************
 * Copyright (C) 2001-2016, International Business Machines Corporation and
 * others. All Rights Reserved.
 *******************************************************************************
 */
package android.icu.dev.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.icu.dev.test.CoreTestFmwk;
import android.icu.impl.ICUData;
import android.icu.impl.ICUResourceBundle;
import android.icu.impl.UResource;
import android.icu.impl.Utility;
import android.icu.text.BreakIterator;
import android.icu.text.UTF16;
import android.icu.util.Calendar;
import android.icu.util.Holiday;
import android.icu.util.ULocale;
import android.icu.util.UResourceBundle;
import android.icu.util.UResourceBundleIterator;
import android.icu.util.UResourceTypeMismatchException;
import android.icu.testsharding.MainTestShard;

@MainTestShard
@RunWith(JUnit4.class)
public final class ICUResourceBundleTest extends CoreTestFmwk {
    private static final ClassLoader testLoader = ICUResourceBundleTest.class.getClassLoader();

    @Test
    public void TestGetResources(){
        try{
            // It does not work well in eclipse plug-in test because of class loader configuration??
            // For now, specify resource path explicitly in this test case
            //Enumeration en = testLoader.getResources("META-INF");
            Enumeration en = testLoader.getResources("android.icu.dev.data");
            for(;en.hasMoreElements();) {
                URL url = (URL)en.nextElement();
                if (url == null) {
                    warnln("could not load resource data");
                    return;
                }
                URLConnection c = url.openConnection();

                if (c instanceof JarURLConnection) {
                    JarURLConnection jc = (JarURLConnection)c;
                    JarEntry je = jc.getJarEntry();
                    logln("jar entry: " + je.toString());
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(c.getInputStream()));
                    logln("input stream:");
                    try {
                        String line = null;
                        int n = 0;
                        while ((line = br.readLine()) != null) {
                            logln("  " + ++n + ": " + line);
                        }
                    } finally {
                        br.close();
                    }
                }
            }
        }catch(SecurityException ex) {
            warnln("could not load resource data: " + ex);
            ex.printStackTrace();
    }catch(NullPointerException ex) {
        // thrown by ibm 1.4.2 windows jvm security manager
        warnln("could not load resource data: " + ex);
        }catch(Exception ex){
        ex.printStackTrace();
            errln("Unexpected exception: "+ ex);
        }
    }
    @Test
    public void TestResourceBundleWrapper(){
        UResourceBundle bundle = UResourceBundle.getBundleInstance("android.icu.impl.data.HolidayBundle", "da_DK");
        Object o = bundle.getObject("holidays");
        if(o instanceof Holiday[] ){
            logln("wrapper mechanism works for Weekend data");
        }else{
            errln("Did not get the expected output for Weekend data");
        }

        bundle = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "bogus");
        if(bundle instanceof UResourceBundle && bundle.getULocale().getName().equals("en_US")){
            logln("wrapper mechanism works for bogus locale");
        }else{
            errln("wrapper mechanism failed for bogus locale.");
        }

        try{
            bundle = UResourceBundle.getBundleInstance("bogus", "bogus");
            if(bundle!=null){
              errln("Did not get the expected exception");
            }
        }catch(MissingResourceException ex){
            logln("got the expected exception");
        }


    }
    @Test
    public void TestJB3879(){
        // this tests tests loading of root bundle when a resource bundle
        // for the default locale is requested
        try {
            UResourceBundle bundle = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata", ULocale.getDefault(), testLoader);
            if(bundle==null){
                errln("could not create the resource bundle");
            }
        }
        catch (MissingResourceException ex) {
            warnln("could not load test data: " + ex.getMessage());
        }
    }
    @Test
    public void TestOpen(){
        UResourceBundle bundle = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "en_US_POSIX");

        if(bundle==null){
            errln("could not create the resource bundle");
        }

        UResourceBundle obj =  bundle.get("NumberElements").get("latn").get("patterns");

        int size = obj.getSize();
        int type = obj.getType();
        if(type == UResourceBundle.TABLE){
            UResourceBundle sub;
            for(int i=0; i<size; i++) {
                sub = obj.get(i);
                String temp =sub.getString();
                if(temp.length()==0){
                    errln("Failed to get the items from number patterns table in bundle: "+
                            bundle.getULocale().getBaseName());
                }
                //System.out.println("\""+prettify(temp)+"\"");
            }
        }

        obj =  bundle.get("NumberElements").get("latn").get("symbols");

        size = obj.getSize();
        type = obj.getType();
        if(type == UResourceBundle.TABLE){
            UResourceBundle sub;
            for(int i=0; i<size; i++){
                sub = obj.get(i);
                String temp =sub.getString();
                if(temp.length()==0){
                    errln("Failed to get the items from number symbols table in bundle: "+
                            bundle.getULocale().getBaseName());
                }
                   // System.out.println("\""+prettify(temp)+"\"");
            }
        }

        if(bundle==null){
            errln("could not create the resource bundle");
        }

        bundle = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "zzz_ZZ_very_very_very_long_bogus_bundle");
        if(!bundle.getULocale().equals(ULocale.getDefault())){
            errln("UResourceBundle did not load the default bundle when bundle was not found. Default: " + ULocale.getDefault() +
                        ", Bundle locale: " + bundle.getULocale());
        }
    }

    @Test
    public void TestBasicTypes(){
        UResourceBundle bundle = null;
        try {
            bundle = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata", "testtypes", testLoader);
        }
        catch (MissingResourceException e) {
            warnln("could not load test data: " + e.getMessage());
            return;
        }
        {
            String expected = "abc\u0000def";
            UResourceBundle sub = bundle.get("zerotest");
            if(!expected.equals(sub.getString())){
                errln("Did not get the expected string for key zerotest in bundle testtypes");
            }
            sub = bundle.get("emptyexplicitstring");
            expected ="";
            if(!expected.equals(sub.getString())){
                errln("Did not get the expected string for key emptyexplicitstring in bundle testtypes");
            }
            sub = bundle.get("emptystring");
            expected ="";
            if(!expected.equals(sub.getString())){
                errln("Did not get the expected string for key emptystring in bundle testtypes");
            }
        }
        {
            int expected = 123;
            UResourceBundle sub = bundle.get("onehundredtwentythree");
            if(expected!=sub.getInt()){
                errln("Did not get the expected int value for key onehundredtwentythree in bundle testtypes");
            }
            sub = bundle.get("emptyint");
            expected=0;
            if(expected!=sub.getInt()){
                errln("Did not get the expected int value for key emptyint in bundle testtypes");
            }
        }
        {
            int expected = 1;
            UResourceBundle sub = bundle.get("one");
            if(expected!=sub.getInt()){
                errln("Did not get the expected int value for key one in bundle testtypes");
            }
        }
        {
            int expected = -1;
            UResourceBundle sub = bundle.get("minusone");
            int got = sub.getInt();
            if(expected!=got){
                errln("Did not get the expected int value for key minusone in bundle testtypes");
            }
            expected = 0xFFFFFFF;
            got = sub.getUInt();
            if(expected!=got){
                errln("Did not get the expected int value for key minusone in bundle testtypes");
            }
        }
        {
            int expected = 1;
            UResourceBundle sub = bundle.get("plusone");
            if(expected!=sub.getInt()){
                errln("Did not get the expected int value for key minusone in bundle testtypes");
            }

        }
        {
            int[] expected = new int[]{ 1, 2, 3, -3, 4, 5, 6, 7 }   ;
            UResourceBundle sub = bundle.get("integerarray");
            if(!Utility.arrayEquals(expected,sub.getIntVector())){
                errln("Did not get the expected int vector value for key integerarray in bundle testtypes");
            }
            sub = bundle.get("emptyintv");
            expected = new int[0];
            if(!Utility.arrayEquals(expected,sub.getIntVector())){
                errln("Did not get the expected int vector value for key emptyintv in bundle testtypes");
            }

        }
        {
            UResourceBundle sub = bundle.get("binarytest");
            ByteBuffer got = sub.getBinary();
            if(got.remaining()!=15){
                errln("Did not get the expected length for the binary ByteBuffer");
            }
            for(int i=0; i< got.remaining(); i++){
                byte b = got.get();
                if(b!=i){
                    errln("Did not get the expected value for binary buffer at index: "+i);
                }
            }
            sub = bundle.get("emptybin");
            got = sub.getBinary();
            if(got.remaining()!=0){
                errln("Did not get the expected length for the emptybin ByteBuffer");
            }

        }
        {
            UResourceBundle sub = bundle.get("emptyarray");
            String key = sub.getKey();
            if(!key.equals("emptyarray")){
                errln("Did not get the expected key for emptytable item");
            }
            if(sub.getSize()!=0){
                errln("Did not get the expected length for emptytable item");
            }
        }
        {
            UResourceBundle sub = bundle.get("menu");
            String key = sub.getKey();
            if(!key.equals("menu")){
                errln("Did not get the expected key for menu item");
            }
            UResourceBundle sub1 = sub.get("file");
            key = sub1.getKey();
            if(!key.equals("file")){
                errln("Did not get the expected key for file item");
            }
            UResourceBundle sub2 = sub1.get("open");
            key = sub2.getKey();
            if(!key.equals("open")){
                errln("Did not get the expected key for file item");
            }
            String value = sub2.getString();
            if(!value.equals("Open")){
                errln("Did not get the expected value for key for oen item");
            }

            sub = bundle.get("emptytable");
            key = sub.getKey();
            if(!key.equals("emptytable")){
                errln("Did not get the expected key for emptytable item");
            }
            if(sub.getSize()!=0){
                errln("Did not get the expected length for emptytable item");
            }
            sub = bundle.get("menu").get("file");
            int size = sub.getSize();
            String expected;
            for(int i=0; i<size; i++){
                sub1 = sub.get(i);

                switch(i){
                    case 0:
                        expected = "exit";
                        break;
                    case 1:
                        expected = "open";
                        break;
                    case 2:
                        expected = "save";
                        break;
                    default:
                        expected ="";
                }
                String got = sub1.getKey();
                if(!expected.equals(got)){
                    errln("Did not get the expected key at index"+i+". Expected: "+expected+" Got: "+got);
                }else{
                    logln("Got the expected key at index: "+i);
                }
            }
        }

    }
    private static final class TestCase{
        String key;
        int value;
        TestCase(String key, int value){
            this.key = key;
            this.value = value;
        }
    }
    @Test
    public void TestTable32(){
        TestCase[] arr = new TestCase[]{
          new TestCase  ( "ooooooooooooooooo", 0 ),
          new TestCase  ( "oooooooooooooooo1", 1 ),
          new TestCase  ( "ooooooooooooooo1o", 2 ),
          new TestCase  ( "oo11ooo1ooo11111o", 25150 ),
          new TestCase  ( "oo11ooo1ooo111111", 25151 ),
          new TestCase  ( "o1111111111111111", 65535 ),
          new TestCase  ( "1oooooooooooooooo", 65536 ),
          new TestCase  ( "1ooooooo11o11ooo1", 65969 ),
          new TestCase  ( "1ooooooo11o11oo1o", 65970 ),
          new TestCase  ( "1ooooooo111oo1111", 65999 )
        };
        UResourceBundle bundle = null;
        try {
            bundle = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","testtable32", testLoader);
        }
        catch (MissingResourceException ex) {
            warnln("could not load resource data: " + ex.getMessage());
            return;
        }

        if(bundle.getType()!= UResourceBundle.TABLE){
            errln("Could not get the correct type for bundle testtable32");
        }

        int size =bundle.getSize();
        if(size!=66000){
            errln("Could not get the correct size for bundle testtable32");
        }

        int number = -1;

        // get the items by index
        for(int i =0; i<size; i++){
            UResourceBundle item = bundle.get(i);
            String key = item.getKey();
            int parsedNumber = parseTable32Key(key);
            switch(item.getType()){
                case UResourceBundle.STRING:
                    String value = item.getString();
                    number = UTF16.charAt(value,0);
                    break;
                case UResourceBundle.INT:
                    number = item.getInt();
                    break;
                default:
                    errln("Got unexpected resource type in testtable32");
            }
            if(number!=parsedNumber){
                errln("Did not get expected value in testtypes32 for key"+
                      key+". Expected: "+parsedNumber+" Got:"+number);
            }

        }

        // search for some items by key
        for(int i=0;i<arr.length; i++){
            UResourceBundle item = bundle.get(arr[i].key);
            switch(item.getType()){
                case UResourceBundle.STRING:
                    String value = item.getString();
                    number = UTF16.charAt(value,0);
                    break;
                 case UResourceBundle.INT:
                    number = item.getInt();
                    break;
                default:
                    errln("Got unexpected resource type in testtable32");
            }

            if(number != arr[i].value){
                errln("Did not get expected value in testtypes32 for key" +
                      arr[i].key +". Expected: " + arr[i].value + " Got:" + number);
            }
        }
    }
    private static int  parseTable32Key(String key) {
        int number;
        char c;

        number=0;
        for(int i=0; i<key.length(); i++){
            c = key.charAt(i);
            number<<=1;
            if(c=='1') {
                number|=1;
            }
        }
        return number;
    }

    @Test
    public void TestAliases(){
       String simpleAlias   = "Open";

       UResourceBundle rb = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","testaliases", testLoader);
       if (rb == null) {
           warnln("could not load testaliases data");
           return;
       }
        UResourceBundle sub = rb.get("simplealias");
        String s1 = sub.getString("simplealias");
        if(s1.equals(simpleAlias)){
            logln("Alias mechanism works for simplealias");
        }else{
            errln("Did not get the expected output for simplealias");
        }
        {
            try{
                rb = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","testaliases",testLoader);
                sub = rb.get("nonexisting");
                errln("Did not get the expected exception for nonexisting");
            }catch(MissingResourceException ex){
                logln("Alias mechanism works for nonexisting alias");
            }
        }
        {
            rb = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","testaliases",testLoader);
            sub = rb.get("referencingalias");
            s1 = sub.getString();
            if(s1.equals("H:mm:ss")){
                logln("Alias mechanism works for referencingalias");
            }else{
                errln("Did not get the expected output for referencingalias");
            }
        }
        {
            UResourceBundle rb1 = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","testaliases",testLoader);
            if(rb1!=rb){
                errln("Caching of the resource bundle failed");
            }else{
                logln("Caching of resource bundle passed");
            }
            sub = rb1.get("testGetStringByKeyAliasing" );

            s1 = sub.get("KeyAlias0PST").getString();
            if(s1.equals("America/Los_Angeles")){
                logln("Alias mechanism works for KeyAlias0PST");
            }else{
                errln("Did not get the expected output for KeyAlias0PST");
            }

            s1 = sub.getString("KeyAlias1PacificStandardTime");
            if(s1.equals("Pacific Standard Time")){
                logln("Alias mechanism works for KeyAlias1PacificStandardTime");
            }else{
                errln("Did not get the expected output for KeyAlias1PacificStandardTime");
            }
            s1 = sub.getString("KeyAlias2PDT");
            if(s1.equals("PDT")){
                logln("Alias mechanism works for KeyAlias2PDT");
            }else{
                errln("Did not get the expected output for KeyAlias2PDT");
            }

            s1 = sub.getString("KeyAlias3LosAngeles");
            if(s1.equals("Los Angeles")){
                logln("Alias mechanism works for KeyAlias3LosAngeles. Got: "+s1);
            }else{
                errln("Did not get the expected output for KeyAlias3LosAngeles. Got: "+s1);
            }
        }
        {
            sub = rb.get("testGetStringByIndexAliasing" );
            s1 = sub.getString(0);
            if(s1.equals("America/Los_Angeles")){
                logln("Alias mechanism works for testGetStringByIndexAliasing/0. Got: "+s1);
            }else{
                errln("Did not get the expected output for testGetStringByIndexAliasing/0. Got: "+s1);
            }
            s1 = sub.getString(1);
            if(s1.equals("Pacific Standard Time")){
                logln("Alias mechanism works for testGetStringByIndexAliasing/1");
            }else{
                errln("Did not get the expected output for testGetStringByIndexAliasing/1");
            }
            s1 = sub.getString(2);
            if(s1.equals("PDT")){
                logln("Alias mechanism works for testGetStringByIndexAliasing/2");
            }else{
                errln("Did not get the expected output for testGetStringByIndexAliasing/2");
            }

            s1 = sub.getString(3);
            if(s1.equals("Los Angeles")){
                logln("Alias mechanism works for testGetStringByIndexAliasing/3. Got: "+s1);
            }else{
                errln("Did not get the expected output for testGetStringByIndexAliasing/3. Got: "+s1);
            }
        }

// Note: Following test cases are no longer working because collation data is now in the collation module
//        {
//            sub = rb.get("testAliasToTree" );
//
//            ByteBuffer buf = sub.get("standard").get("%%CollationBin").getBinary();
//            if(buf==null){
//                errln("Did not get the expected output for %%CollationBin");
//            }
//        }
//
//        rb = (UResourceBundle) UResourceBundle.getBundleInstance(ICUResourceBundle.ICU_COLLATION_BASE_NAME,"zh_TW");
//        UResourceBundle b = (UResourceBundle) rb.getObject("collations");
//        if(b != null){
//            if(b.get(0).getKey().equals( "default")){
//                logln("Alias mechanism works");
//            }else{
//                errln("Alias mechanism failed for zh_TW collations");
//            }
//        }else{
//            errln("Did not get the expected object for collations");
//        }

        // Test case for #7996
        {
            UResourceBundle bundle = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata", "te", testLoader);
            UResourceBundle table = bundle.get("tableT7996");
            try {
                String s = table.getString("a7996");
                logln("Alias in nested table referring one in sh worked - " + s);
            } catch (MissingResourceException e) {
                errln("Alias in nested table referring one in sh failed");
            }

            try {
                String s = ((ICUResourceBundle)table).getStringWithFallback("b7996");
                logln("Alias with /LOCALE/ in nested table in root referring back to another key in the current locale bundle worked - " + s);
            } catch (MissingResourceException e) {
                errln("Alias with /LOCALE/ in nested table in root referring back to another key in the current locale bundle failed");
            }
        }

    }
    @Test
    public void TestAlias(){
        logln("Testing %%ALIAS");
        UResourceBundle rb = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME,"iw_IL");
        UResourceBundle b = rb.get("NumberElements");
        if(b != null){
            if(b.getSize()>0){
                logln("%%ALIAS mechanism works");
            }else{
                errln("%%ALIAS mechanism failed for iw_IL NumberElements");
            }
        }else{
            errln("%%ALIAS mechanism failed for iw_IL");
        }
    }
    @Test
    public void TestXPathAlias(){
        UResourceBundle rb = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","te_IN",testLoader);
        UResourceBundle b = rb.get("aliasClient");
        String result = b.getString();
        String expResult= "correct";

        if(!result.equals(expResult)){
            errln("Did not get the expected result for XPath style alias");
        }
        try{
            UResourceBundle c = rb.get("rootAliasClient");
            result = c.getString();
            expResult = "correct";
            if(!result.equals(expResult)){
                errln("Did not get the expected result for XPath style alias for rootAliasClient");
            }
        }catch( MissingResourceException ex){
            errln("Could not get rootAliasClient");
        }
    }
    @Test
    public void TestCircularAliases(){
        try{
            UResourceBundle rb = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","testaliases",testLoader);
            UResourceBundle sub = rb.get("aaa");
            String s1 = sub.getString();
            if(s1!=null){
                errln("Did not get the expected exception");
            }
        }catch(IllegalArgumentException ex){
            logln("got expected exception for circular references");
        }
        catch (MissingResourceException ex) {
            warnln("could not load resource data: " + ex.getMessage());
        }
    }

    @Test
    public void TestPreventFallback() {
        String noFallbackResource = "string_in_te_no_te_IN_fallback";
        ICUResourceBundle rb = (ICUResourceBundle) UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","te_IN_NE",testLoader);
        try {
            rb.getStringWithFallback(noFallbackResource);
            fail("Expected MissingResourceException.");
        } catch (MissingResourceException e) {
            // Expected
        }
        rb.getStringWithFallback("string_only_in_te");
        rb = (ICUResourceBundle) UResourceBundle.getBundleInstance("android/icu/dev/data/testdata","te",testLoader);
        rb.getStringWithFallback(noFallbackResource);
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

        bundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME,"fr_FR");
        ICUResourceBundle b1 = bundle.getWithFallback("calendar");
        String defaultCal = b1.getStringWithFallback("default");
        if(!defaultCal.equals("gregorian")){
            errln("Did not get the expected default calendar string: Expected: gregorian, Got: "+defaultCal);
        }
        ICUResourceBundle b2 = b1.getWithFallback(defaultCal);
        ICUResourceBundle b3 = b2.getWithFallback("monthNames");
        ICUResourceBundle b4 = b3.getWithFallback("format");
        ICUResourceBundle b5 = b4.getWithFallback("narrow");
        if(b5.getSize()!=12){
            errln("Did not get the expected size for the monthNames");
        }
    }

    private static final String CALENDAR_RESNAME = "calendar";
    private static final String CALENDAR_KEYWORD = "calendar";

    @Test
    public void TestLocaleDisplayNames() {
        ULocale[] locales = ULocale.getAvailableLocales();

        Set<String> localCountryExceptions = new HashSet<>();
        if (logKnownIssue("cldrbug:8903",
                "No localized region name for bgc_IN, bho_IN, lrc_IQ, lrc_IR, nus_SS, nds_DE, raj_IN, su_Latn_ID")) {
            localCountryExceptions.add("bgc_IN");
            localCountryExceptions.add("bho_IN");
            localCountryExceptions.add("lrc_IQ");
            localCountryExceptions.add("lrc_IR");
            localCountryExceptions.add("nus_SS");
            localCountryExceptions.add("nds_DE");
            localCountryExceptions.add("nds_NL");
            localCountryExceptions.add("raj_IN");
            localCountryExceptions.add("su_Latn_ID");
        }

        Set<String> localLangExceptions = new HashSet<>();
        if (logKnownIssue("ICU-22681", "No localized language name for nmg")) {
            localLangExceptions.add("nmg");
        }

        for (int i = 0; i < locales.length; ++i) {
            if (!hasLocalizedCountryFor(ULocale.ENGLISH, locales[i])
                  && !localLangExceptions.contains(locales[i].getLanguage())) {
                 errln("Could not get English localized country for " + locales[i]);
            }
            if(!hasLocalizedLanguageFor(ULocale.ENGLISH, locales[i])
                && !localLangExceptions.contains(locales[i].getLanguage())) {
                errln("Could not get English localized language for " + locales[i]);
            }

            if(!hasLocalizedCountryFor(locales[i], locales[i])
                    && !localCountryExceptions.contains(locales[i].toString())) {
                errln("Could not get native localized country for " + locales[i]);
                hasLocalizedCountryFor(locales[i], locales[i]);
            }
            if(!hasLocalizedLanguageFor(locales[i], locales[i])
                    && !localLangExceptions.contains(locales[i].getLanguage())) {
                errln("Could not get native localized language for " + locales[i]);
            }

            logln(locales[i] + "\t" + locales[i].getDisplayName(ULocale.ENGLISH) + "\t" + locales[i].getDisplayName(locales[i]));
        }
    }

    private static boolean hasLocalizedLanguageFor(ULocale locale, ULocale otherLocale) {
        String lang = otherLocale.getLanguage();
        String localizedVersion = otherLocale.getDisplayLanguage(locale);
        return !lang.equals(localizedVersion);
    }

    private static boolean hasLocalizedCountryFor(ULocale locale, ULocale otherLocale) {
        String country = otherLocale.getCountry();
        if (country.equals("")) return true;
        String localizedVersion = otherLocale.getDisplayCountry(locale);
        return !country.equals(localizedVersion);
    }

    @Test
    public void TestFunctionalEquivalent(){
       // Android patch: Force default Gregorian calendar.
       String[] calCases = {
       //  avail    locale                              equiv
           "t",     "en_US_POSIX",                      "en@calendar=gregorian",
           "f",     "ja_JP_TOKYO",                      "ja@calendar=gregorian",
           "f",     "ja_JP_TOKYO@calendar=japanese",    "ja@calendar=japanese",
           "t",     "sr@calendar=gregorian",            "sr@calendar=gregorian",
           "t",     "en",                               "en@calendar=gregorian",
           "t",     "th_TH",                            "th@calendar=gregorian",
           "t",     "th_TH@calendar=gregorian",         "th@calendar=gregorian",
           "f",     "th_TH_Bangkok",                    "th@calendar=gregorian",
       };
       // Android patch end.

       logln("Testing functional equivalents for calendar...");
       getFunctionalEquivalentTestCases(ICUData.ICU_BASE_NAME,
                                        Calendar.class.getClassLoader(),
               CALENDAR_RESNAME, CALENDAR_KEYWORD, false, calCases);

       logln("Testing error conditions:");
       try {
           ClassLoader cl = BreakIterator.class.getClassLoader();
           ICUResourceBundle.getFunctionalEquivalent(ICUData.ICU_BRKITR_BASE_NAME, cl, "calendar",
              "calendar", new ULocale("ar_EG@calendar=islamic"), new boolean[1], true);
           errln("Err: expected MissingResourceException");
       } catch ( MissingResourceException t ) {
           logln("expected MissingResourceException caught (PASS): " + t.toString());
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

    @Test
    public void TestNorwegian(){
        try{
            UResourceBundle rb = UResourceBundle.getBundleInstance(ICUData.ICU_REGION_BASE_NAME, "no_NO_NY");
            UResourceBundle sub = rb.get("Countries");
            String s1 = sub.getString("NO");
            if(s1.equals("Noreg")){
                logln("got expected output ");
            }else{
                errln("did not get the expected result");
            }
        }catch(IllegalArgumentException ex){
            errln("Caught an unexpected expected");
        }
    }
    @Test
    public void TestJB4102(){
        try {
            ICUResourceBundle root =(ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "root");
            ICUResourceBundle t = null;
// AmPmMarkers now exist in root/islamic calendar, so this test is rendered useless.
//          try{
//              t = root.getWithFallback("calendar/islamic-civil/AmPmMarkers");
//              errln("Second resource does not exist. How did it get here?\n");
//          }catch(MissingResourceException ex){
//              logln("Got the expected exception");
//          }
            try{
                t = root.getWithFallback("calendar/islamic-civil/eras/abbreviated/0/mikimaus/pera");
                errln("Second resource does not exist. How did it get here?\n");
            }catch(MissingResourceException ex){
                logln("Got the expected exception");
            }
            if(t!=null){
                errln("t is not null!");
            }
        } catch (MissingResourceException e) {
           warnln("Could not load the locale data: " + e.getMessage());
        }
    }

    @Test
    public void TestCLDRStyleAliases() {
        String result = null;
        String expected = null;
        String[]expects = new String[] { "", "a41", "a12", "a03", "ar4" };

        logln("Testing CLDR style aliases......\n");

        UResourceBundle rb = UResourceBundle.getBundleInstance("android/icu/dev/data/testdata", "te_IN_REVISED", testLoader);
        ICUResourceBundle alias = (ICUResourceBundle)rb.get("a");

        for(int i = 1; i < 5 ; i++) {
          String resource="a"+i;
          UResourceBundle a = (alias).getWithFallback(resource);
          result = a.getString();
          if(result.equals(expected)) {
              errln("CLDR style aliases failed resource with name "+resource+"resource, exp "+expects[i] +" , got " + result);
          }
        }

    }

    @Test
    public void TestCoverage(){
        UResourceBundle bundle;
        bundle = UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME);
        if (bundle == null){
            errln("UResourceBundle.getBundleInstance(String baseName) failed");
        }
        bundle = null;
        bundle = UResourceBundle.getBundleInstance(ULocale.getDefault());
        if (bundle == null){
            errln("UResourceBundle.getBundleInstance(ULocale) failed");
            return;
        }
        if (new UResourceTypeMismatchException("coverage") == null){
            errln("Create UResourceTypeMismatchException error");
        }
        class Stub extends UResourceBundle{
            @Override
            public ULocale getULocale() {return ULocale.ROOT;}
            @Override
            protected String getLocaleID() {return null;}
            @Override
            protected String getBaseName() {return null;}
            @Override
            protected UResourceBundle getParent() {return null;}
            @Override
            public Enumeration getKeys() {return null;}
            @Override
            protected Object handleGetObject(String aKey) {return null;}
        }
        Stub stub = new Stub();

        if (!stub.getLocale().equals(ULocale.ROOT.toLocale())){
            errln("UResourceBundle.getLoclae(Locale) should delegate to (ULocale)");
        }
    }
    @Test
    public void TestJavaULocaleBundleLoading(){
        String baseName="android.icu.dev.data.resources.TestDataElements";
        String locName = "en_Latn_US";
        UResourceBundle bundle = UResourceBundle.getBundleInstance(baseName, locName, testLoader);
        String fromRoot = bundle.getString("from_root");
        if(!fromRoot.equals("This data comes from root")){
            errln("Did not get the expected string for from_root");
        }
        String fromEn = bundle.getString("from_en");
        if(!fromEn.equals("This data comes from en")){
            errln("Did not get the expected string for from_en");
        }
        String fromEnLatn = bundle.getString("from_en_Latn");
        if(!fromEnLatn.equals("This data comes from en_Latn")){
            errln("Did not get the expected string for from_en_Latn");
        }
        String fromEnLatnUs = bundle.getString("from_en_Latn_US");
        if(!fromEnLatnUs.equals("This data comes from en_Latn_US")){
            errln("Did not get the expected string for from_en_Latn_US");
        }
        UResourceBundle bundle1 = UResourceBundle.getBundleInstance(baseName, new ULocale(locName), testLoader);
        if(!bundle1.equals(bundle)){
            errln("Did not get the expected bundle for "+baseName +"."+locName);
        }
        if(bundle1!=bundle){
            errln("Did not load the bundle from cache");
        }

        UResourceBundle bundle2 = UResourceBundle.getBundleInstance(baseName, "en_IN", testLoader);
        if(!bundle2.getLocale().toString().equals("en")){
            errln("Did not get the expected fallback locale. Expected: en Got: "+bundle2.getLocale().toString());
        }
        UResourceBundle bundle3 = UResourceBundle.getBundleInstance(baseName, "te_IN", testLoader);
        if(!bundle3.getLocale().toString().equals("te")){
            errln("Did not get the expected fallback locale. Expected: te Got: "+bundle2.getLocale().toString());
        }
        // non-existent bundle .. should return default
        UResourceBundle defaultBundle = UResourceBundle.getBundleInstance(baseName, "hi_IN", testLoader);
        ULocale defaultLocale = ULocale.getDefault();
        if(!defaultBundle.getULocale().equals(defaultLocale)){
            errln("Did not get the default bundle for non-existent bundle");
        }
        // non-existent bundle, non-existent default locale
        // so return the root bundle.
        ULocale.setDefault(ULocale.CANADA_FRENCH);
        UResourceBundle root = UResourceBundle.getBundleInstance(baseName, "hi_IN", testLoader);
        if(!root.getULocale().toString().equals("")){
            errln("Did not get the root bundle for non-existent default bundle for non-existent bundle");
        }
        //reset the default
        ULocale.setDefault(defaultLocale);
        Enumeration keys = bundle.getKeys();
        int i=0;
        while(keys.hasMoreElements()){
            logln("key: "+ keys.nextElement());
            i++;
        }
        if(i!=4){
            errln("Did not get the expected number of keys: got " + i + ", expected 4");
        }
        UResourceBundle bundle4 = UResourceBundle.getBundleInstance(baseName, "fr_Latn_FR", testLoader);
        if(bundle4==null){
            errln("Could not load bundle fr_Latn_FR");
        }
    }
    @Test
    public void TestAliasFallback(){
        try{
            ULocale loc = new ULocale("en_US");
            ICUResourceBundle b = (ICUResourceBundle)UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, loc);
            ICUResourceBundle b1 = b.getWithFallback("calendar/hebrew/monthNames/format/abbreviated");
            if(b1!=null){
                logln("loaded data for abbreviated month names: "+ b1.getKey());
            }
        }catch(MissingResourceException ex){
            warnln("Failed to load data for abbreviated month names");
        }
    }
    private Set<String> setFromEnumeration(Enumeration<String> e) {
        TreeSet<String> set = new TreeSet<>();
        while (e.hasMoreElements()) {
            set.add(e.nextElement());
        }
        return set;
    }
    /**
     * Test ICUResourceBundle.getKeys() for a whole bundle (top-level resource).
     * JDK JavaDoc for ResourceBundle.getKeys() says that it returns
     * "an Enumeration of the keys contained in this ResourceBundle and its parent bundles."
     */
    @Test
    public void TestICUGetKeysAtTopLevel() {
        String baseName="android/icu/dev/data/testdata";
        UResourceBundle te_IN = UResourceBundle.getBundleInstance(baseName, "te_IN", testLoader);
        UResourceBundle te = UResourceBundle.getBundleInstance(baseName, "te", testLoader);
        Set<String> te_set = setFromEnumeration(te.getKeys());
        Set<String> te_IN_set = setFromEnumeration(te_IN.getKeys());
        assertTrue("te.getKeys().contains(string_only_in_Root)", te_set.contains("string_only_in_Root"));
        assertTrue("te.getKeys().contains(string_only_in_te)", te_set.contains("string_only_in_te"));
        assertFalse("te.getKeys().contains(string_only_in_te_IN)", te_set.contains("string_only_in_te_IN"));
        assertTrue("te_IN.getKeys().contains(string_only_in_Root)", te_IN_set.contains("string_only_in_Root"));
        assertTrue("te_IN.getKeys().contains(string_only_in_te)", te_IN_set.contains("string_only_in_te"));
        assertTrue("te_IN.getKeys().contains(string_only_in_te_IN)", te_IN_set.contains("string_only_in_te_IN"));
        // TODO: Check for keys of alias resource items
    }
    /**
     * Test ICUResourceBundle.getKeys() for a resource item (not a whole bundle/top-level resource).
     * This does not take parent bundles into account.
     */
    @Test
    public void TestICUGetKeysForResourceItem() {
        String baseName="android/icu/dev/data/testdata";
        UResourceBundle te = UResourceBundle.getBundleInstance(baseName, "te", testLoader);
        UResourceBundle tagged_array_in_Root_te = te.get("tagged_array_in_Root_te");
        Set<String> keys = setFromEnumeration(tagged_array_in_Root_te.getKeys());
        assertTrue("tagged_array_in_Root_te.getKeys().contains(tag0)", keys.contains("tag0"));
        assertTrue("tagged_array_in_Root_te.getKeys().contains(tag1)", keys.contains("tag1"));
        assertFalse("tagged_array_in_Root_te.getKeys().contains(tag7)", keys.contains("tag7"));
        assertFalse("tagged_array_in_Root_te.getKeys().contains(tag12)", keys.contains("tag12"));
        UResourceBundle array_in_Root_te = te.get("array_in_Root_te");
        assertFalse("array_in_Root_te.getKeys().hasMoreElements()", array_in_Root_te.getKeys().hasMoreElements());
        UResourceBundle string_in_Root_te = te.get("string_in_Root_te");
        assertFalse("string_in_Root_te.getKeys().hasMoreElements()", string_in_Root_te.getKeys().hasMoreElements());
    }

    /*
     * UResourceBundle should be able to load a resource bundle even if
     * a similarly named class (only case differences) exists in the
     * same package.  See Ticket#6844
     */
    @Test
    public void TestT6844() {
        try {
            UResourceBundle rb1
                = UResourceBundle.getBundleInstance("android.icu.dev.data.resources.TestMessages", ULocale.getDefault(), testLoader);
            assertEquals("bundleContainer in TestMessages", "TestMessages.class", rb1.getString("bundleContainer"));

            UResourceBundle rb2
                = UResourceBundle.getBundleInstance("android.icu.dev.data.resources.testmessages", ULocale.getDefault(), testLoader);
            assertEquals("bundleContainer in testmessages", "testmessages.properties", rb2.getString("bundleContainer"));
        } catch (Throwable t) {
            errln(t.getMessage());
        }
    }

    @Test
    public void TestUResourceBundleCoverage() {
        Locale locale = null;
        ULocale ulocale = null;
        String baseName = null;
        UResourceBundle rb1, rb2, rb3, rb4, rb5, rb6, rb7;

        rb1 = UResourceBundle.getBundleInstance(ulocale);
        rb2 = UResourceBundle.getBundleInstance(baseName);
        rb3 = UResourceBundle.getBundleInstance(baseName, ulocale);
        rb4 = UResourceBundle.getBundleInstance(baseName, locale);

        rb5 = UResourceBundle.getBundleInstance(baseName, ulocale, testLoader);
        rb6 = UResourceBundle.getBundleInstance(baseName, locale, testLoader);
        try {
            rb7 = UResourceBundle.getBundleInstance("bogus", Locale.getDefault(), testLoader);
            errln("Should have thrown exception with bogus baseName.");
        } catch (java.util.MissingResourceException ex) {
        }
        if (rb1 == null || rb2 == null || rb3 == null || rb4 == null || rb5 == null || rb6 == null) {
            errln("Error getting resource bundle.");
        }

        rb7 = UResourceBundle.getBundleInstance("android.icu.dev.data.resources.TestDataElements", Locale.getDefault(), testLoader);

        try {
            rb1.getBinary();
            errln("getBinary() call should have thrown UResourceTypeMismatchException.");
        } catch (UResourceTypeMismatchException ex) {
        }
        try {
            rb1.getStringArray();
            errln("getStringArray() call should have thrown UResourceTypeMismatchException.");
        } catch (UResourceTypeMismatchException ex) {
        }
        try {
            byte [] ba = { 0x00 };
            rb1.getBinary(ba);
            errln("getBinary(byte[]) call should have thrown UResourceTypeMismatchException.");
        } catch (UResourceTypeMismatchException ex) {
        }
        try {
            rb1.getInt();
            errln("getInt() call should have thrown UResourceTypeMismatchException.");
        } catch (UResourceTypeMismatchException ex) {
        }
        try {
            rb1.getIntVector();
            errln("getIntVector() call should have thrown UResourceTypeMismatchException.");
        } catch (UResourceTypeMismatchException ex) {
        }
        try {
            rb1.getUInt();
            errln("getUInt() call should have thrown UResourceTypeMismatchException.");
        } catch (UResourceTypeMismatchException ex) {
        }
        if (rb1.getVersion() != null) {
            errln("getVersion() call should have returned null.");
        }
        if (rb7.getType() != UResourceBundle.NONE) {
            errln("getType() call should have returned NONE.");
        }
        if (rb7.getKey() != null) {
            errln("getKey() call should have returned null.");
        }
        // The following test may no longer be exercised if
        // rb1 is for a locale like en_US with an empty resource bundle.
        // (Before ICU-21028 such a bundle would have contained at least a Version string.)
        if (rb1.getSize() != 0 && ((ICUResourceBundle)rb1).findTopLevel(0) == null) {
            errln("Error calling findTopLevel().");
        }
        if (ICUResourceBundle.getFullLocaleNameSet() == null) {
            errln("Error calling getFullLocaleNameSet().");
        }
        UResourceBundleIterator itr = rb1.getIterator();
        while (itr.hasNext()) {
            itr.next();
        }
        try {
            itr.next();
            errln("NoSuchElementException exception should have been thrown.");
        } catch (NoSuchElementException ex) {
        }
        try {
            itr.nextString();
            errln("NoSuchElementException exception should have been thrown.");
        } catch (NoSuchElementException ex) {
        }
    }

    @Test
    public void TestAlgorithmicParentFallback() {
        // Test for ICU-21125 and ICU-21126 -- cases where resource fallback isn't determined by lopping fields off
        // the end of the locale ID (or following a %%Parent directive in a resource bundle)
        // first column is input locale, second column is expected output locale
        String[][] testCases = {
            { "de_Latn_LI", "de_LI", "de_LI"     },
//            { "en_VA",      "en_150", "en"       },  // TODO: put this back in after https://unicode-org.atlassian.net/browse/CLDR-15893 is fixed
            { "yi_Latn_DE", "",       "yi"       },  // "" is just "root"-- or, actually, the system default locale
            { "yi_Hebr_DE", "yi",     "yi"       },
            { "zh_Hant_SG", "zh_Hant", "zh_Hant" },
            // would be nice to test that sr_Latn_ME falls back to sr_Latn, or sr_ME to sr_Latn_ME,
            // or sr_Latn to root, but all of these resource bundle files actually exist in the project
        };

        for (String[] testCase : testCases) {
            String localeID = testCase[0];
            String localeDefaultRootExpected = testCase[1];
            String localeRootExpected = testCase[2];

            ULocale locale = new ULocale(localeID);
            ICUResourceBundle localeDefaultRootRB = ICUResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, locale, ICUResourceBundle.OpenType.LOCALE_DEFAULT_ROOT);
            ICUResourceBundle localeRootRB = ICUResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, locale, ICUResourceBundle.OpenType.LOCALE_ROOT);
            String localeDefaultRootActual = localeDefaultRootRB.getULocale().toString();
            String localeRootActual = localeRootRB.getULocale().toString();

            if (localeDefaultRootExpected.isEmpty()) {
                assertEquals("Got wrong locale with LOCALE_DEFAULT_ROOT", ULocale.getDefault().toString(), localeDefaultRootActual);
            } else {
                assertEquals("Got wrong locale with LOCALE_DEFAULT_ROOT", localeDefaultRootExpected, localeDefaultRootActual);
            }
            assertEquals("Got wrong locale with LOCALE_ROOT", localeRootExpected, localeRootActual);
        }
    }

    @Test
    public void TestResourceBundleCrash() {
        final String[] TEST_LOCALE_IDS = new String[] { "nb", "nn", "ht", "hi-Latn" };

        ULocale oldDefaultLocale = ULocale.getDefault();
        for (String localeID : TEST_LOCALE_IDS) {
            ULocale locale = ULocale.forLanguageTag(localeID);
            ULocale.setDefault(locale);
            UResourceBundle rb = UResourceBundle.getBundleInstance(ICUData.ICU_TRANSLIT_BASE_NAME, locale);
            assertTrue("Failed to retrieve a resource bundle for " + localeID, rb != null);
            // the test is to make sure we fell back to root (or otherwise returned SOMETHING)-- all we're really trying to
            // enrure is that we don't crash with a StackOverflowError when trying to retrieve the bundle
        }
        ULocale.setDefault(oldDefaultLocale);
	}
        
    @Test
    public void TestPersonUnits() {
        // Test for ICU-21877: ICUResourceBundle.getAllChildrenWithFallback() doesn't return all of the children of the resource
        // that's passed into it.  If you have to follow an alias to get some of the children, we get the resources in the
        // bundle we're aliasing to, and its children, but things that the bundle we're aliasing to inherits from its parent
        // don't show up.
        // This example is from the en_CA resource in the "unit" tree: The four test cases below show what we get when we call
        // getStringWithFallback():
        // - units/duration/day-person/other doesn't exist in either en_CA or en, so we fall back on root.
        // - root/units aliases over to LOCALE/unitsShort.
        // - unitsShort/duration/day-person/other also doesn't exist in either en_CA or en, so we fall back to root again.
        // - root/unitsShort/duration/day-person aliases over to LOCALE/unitsShort/duration/day.
        // - unitsShort/duration/day/other also doesn't exist in en_CA, so we fallback to en.
        // - en/unitsShort/duration/day/other DOES exist and has "{0} days", so we return that.
        // It's the same basic story for week-person, month-person, and year-person, except that:
        // - unitsShort/duration/day doesn't exist at all in en_CA
        // - unitsShort/duration/week DOES exist in en_CA, but only contains "per", so we inherit "other" from en
        // - unitsShort/duration/month/other DOES exist in en_CA (it overrides "{0} mths" with "{0} mos")
        // - unitsShort/duration/year DOES exist in en_CA, but only contains "per", so we inherit "other" from en
        ICUResourceBundle en_ca = (ICUResourceBundle)UResourceBundle.getBundleInstance(ICUData.ICU_UNIT_BASE_NAME, "en_CA");
        assertEquals("Wrong result for units/duration/day-person/other", "{0} days", en_ca.getStringWithFallback("units/duration/day-person/other"));
        assertEquals("Wrong result for units/duration/week-person/other", "{0} wks", en_ca.getStringWithFallback("units/duration/week-person/other"));
        assertEquals("Wrong result for units/duration/month-person/other", "{0} mos", en_ca.getStringWithFallback("units/duration/month-person/other"));
        assertEquals("Wrong result for units/duration/year-person/other", "{0} yrs", en_ca.getStringWithFallback("units/duration/year-person/other"));

        // getAllChildrenWithFallback() wasn't bringing all of those things back, however.  When en_CA/units/year-person
        // aliased over to en_CA/unitsShort/year, the sink would only be called on en_CA/unitsShort/year, which means it'd
        // only see en_CA/unitsShort/year/per, because "per" was the only thing contained in en_CA/unitsShort/year.  But
        // en_CA/unitsShort/year should be inheriting "dnam", "one", and "other" from en/unitsShort/year.  getAllChildrenWithFallback()
        // had to be modified to walk the parent chain and call the sink again with en/unitsShort/year and root/unitsShort/year.
        final Map<String, Set<String>> foundResources = new HashMap<>();
        en_ca.getAllChildrenWithFallback("units/duration", new UResource.Sink() {
            @Override
            public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
                assertEquals(key + " isn't a table!", value.getType(), UResourceBundle.TABLE);

                Set<String> keys = foundResources.computeIfAbsent(key.toString(), k -> new HashSet<>());
                UResource.Table childTable = value.getTable();
                for (int i = 0; i < childTable.getSize(); i++) {
                    childTable.getKeyAndValue(i, key, value);
                    keys.add(key.toString());
                }
            }
        });
        for (String key : foundResources.keySet()) {
            if (!key.endsWith("-person")) {
                continue;
            }

            Set<String> foundChildren = foundResources.get(key);
            assertTrue(key + " doesn't contain 'dnam'!", foundChildren.contains("dnam"));
            assertTrue(key + " doesn't contain 'one'!", foundChildren.contains("one"));
            assertTrue(key + " doesn't contain 'other'!", foundChildren.contains("other"));
            assertTrue(key + " doesn't contain 'per'!", foundChildren.contains("per"));
        }
    }

    @Test
    public void TestZuluFields() {
        // Test for ICU-21877: Similar to the above test, except that here we're bringing back the wrong values.
        // In some resources under the "locales" tree, some resources under "fields" would either have no display
        // names or bring back the _wrong_ display names.  The underlying cause was the same as in TestPersonUnits()
        // above, except that there were two levels of indirection: At the root level, *-narrow aliased to *-short,
        // which in turn aliased to *.  If (say) day-narrow and day-short both didn't have "dn" resources, you could
        // iterate over fields/day-short and find the "dn" resource that should have been inherited over from
        // fields/day, but if you iterated over fields/day-narrow, you WOULDN'T get back the "dn" resource from
        // fields/day (or, with my original fix for ICU-21877, you'd get back the wrong value).  This test verifies
        // that the double indirection works correctly.
        ICUResourceBundle zu = (ICUResourceBundle)UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, "zu");
        assertEquals("Wrong getStringWithFallback() result for fields/day/dn", "Usuku", zu.getStringWithFallback("fields/day/dn"));
        assertEquals("Wrong getStringWithFallback() result for fields/day-short/dn", "Usuku", zu.getStringWithFallback("fields/day-short/dn"));
        assertEquals("Wrong getStringWithFallback() result for fields/day-narrow/dn", "Usuku", zu.getStringWithFallback("fields/day-narrow/dn"));

        assertEquals("Wrong getStringWithFallback() result for fields/month/dn", "Inyanga", zu.getStringWithFallback("fields/month/dn"));
        assertEquals("Wrong getStringWithFallback() result for fields/month-short/dn", "Inyanga", zu.getStringWithFallback("fields/month-short/dn"));
        assertEquals("Wrong getStringWithFallback() result for fields/month-narrow/dn", "Inyanga", zu.getStringWithFallback("fields/month-narrow/dn"));

        final Map<String, String> foundNames = new HashMap<>();
        zu.getAllChildrenWithFallback("fields", new UResource.Sink() {
            @Override
            public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
                assertEquals(key + " isn't a table!", value.getType(), UResourceBundle.TABLE);

                UResource.Table childTable = value.getTable();
                if (childTable.findValue("dn", value)) {
                    foundNames.putIfAbsent(key.toString(), value.toString());
                }
            }
        });
        assertEquals("Wrong getAllChildrenWithFallback() result for fields/day/dn", "Usuku", foundNames.get("day"));
        assertEquals("Wrong getAllChildrenWithFallback() result for fields/day-short/dn", "Usuku", foundNames.get("day-short"));
        assertEquals("Wrong getAllChildrenWithFallback() result for fields/day-narrow/dn", "Usuku", foundNames.get("day-narrow"));
        assertEquals("Wrong getAllChildrenWithFallback() result for fields/month/dn", "Inyanga", foundNames.get("month"));
        assertEquals("Wrong getAllChildrenWithFallback() result for fields/month-short/dn", "Inyanga", foundNames.get("month-short"));
        assertEquals("Wrong getAllChildrenWithFallback() result for fields/month-narrow/dn", "Inyanga", foundNames.get("month-narrow"));
    }
}
