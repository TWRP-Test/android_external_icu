/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
/*
 *******************************************************************************
 * Copyright (C) 2007-2015, International Business Machines Corporation and
 * others. All Rights Reserved.
 *******************************************************************************
 */
package android.icu.dev.test.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.icu.dev.test.CoreTestFmwk;
import android.icu.dev.test.serializable.SerializableTestUtility;
import android.icu.impl.Relation;
import android.icu.impl.Utility;
import android.icu.impl.number.DecimalQuantity;
import android.icu.impl.number.DecimalQuantity_DualStorageBCD;
import android.icu.number.FormattedNumber;
import android.icu.number.FormattedNumberRange;
import android.icu.number.LocalizedNumberFormatter;
import android.icu.number.NumberFormatter;
import android.icu.number.NumberRangeFormatter;
import android.icu.number.Precision;
import android.icu.number.UnlocalizedNumberFormatter;
import android.icu.text.NumberFormat;
import android.icu.text.PluralRules;
import android.icu.text.PluralRules.DecimalQuantitySamples;
import android.icu.text.PluralRules.DecimalQuantitySamplesRange;
import android.icu.text.PluralRules.FixedDecimal;
import android.icu.text.PluralRules.KeywordStatus;
import android.icu.text.PluralRules.PluralType;
import android.icu.text.PluralRules.SampleType;
import android.icu.text.UFieldPosition;
import android.icu.util.Output;
import android.icu.util.ULocale;
import android.icu.testsharding.MainTestShard;

/**
 * @author dougfelt (Doug Felt)
 * @author markdavis (Mark Davis) [for fractional support]
 */
@MainTestShard
@RunWith(JUnit4.class)
public class PluralRulesTest extends CoreTestFmwk {

    PluralRulesFactory factory = PluralRulesFactory.NORMAL;

    @Test
    public void testOverUnderflow() {
        logln(String.valueOf(Long.MAX_VALUE + 1d));
        for (double[] testDouble : new double[][] {
                { 1E18, 0, 0, 1E18 }, // check overflow
                { 10000000000000.1d, 1, 1, 10000000000000d }, { -0.00001d, 1, 5, 0 }, { 1d, 0, 0, 1 },
                { 1.1d, 1, 1, 1 }, { 12345d, 0, 0, 12345 }, { 12345.678912d, 678912, 6, 12345 },
                { 12345.6789123d, 678912, 6, 12345 }, // we only go out 6 digits
                { 1E18, 0, 0, 1E18 }, // check overflow
                { 1E19, 0, 0, 1E18 }, // check overflow
        }) {
            FixedDecimal fd = new FixedDecimal(testDouble[0]);
            assertEquals(testDouble[0] + "=doubleValue()", testDouble[0], fd.doubleValue());
            assertEquals(testDouble[0] + " decimalDigits", (int) testDouble[1], fd.getDecimalDigits());
            assertEquals(testDouble[0] + " visibleDecimalDigitCount", (int) testDouble[2], fd.getVisibleDecimalDigitCount());
            assertEquals(testDouble[0] + " decimalDigitsWithoutTrailingZeros", (int) testDouble[1],
                    fd.getDecimalDigitsWithoutTrailingZeros());
            assertEquals(testDouble[0] + " visibleDecimalDigitCountWithoutTrailingZeros", (int) testDouble[2],
                    fd.getVisibleDecimalDigitCountWithoutTrailingZeros());
            assertEquals(testDouble[0] + " integerValue", (long) testDouble[3], fd.getIntegerValue());
        }

        for (ULocale locale : new ULocale[] { ULocale.ENGLISH, new ULocale("cy"), new ULocale("ar") }) {
            PluralRules rules = factory.forLocale(locale);

            assertEquals(locale + " NaN", "other", rules.select(Double.NaN));
            assertEquals(locale + " ∞", "other", rules.select(Double.POSITIVE_INFINITY));
            assertEquals(locale + " -∞", "other", rules.select(Double.NEGATIVE_INFINITY));
        }
    }

    @Test
    public void testSyntaxRestrictions() {
        Object[][] shouldFail = {
                { "a:n in 3..10,13..19" },

                // = and != always work
                { "a:n=1" },
                { "a:n=1,3" },
                { "a:n!=1" },
                { "a:n!=1,3" },

                // with spacing
                { "a: n = 1" },
                { "a: n = 1, 3" },
                { "a: n != 1" },
                { "a: n != 1, 3" },
                { "a: n ! = 1" },
                { "a: n ! = 1, 3" },
                { "a: n = 1 , 3" },
                { "a: n != 1 , 3" },
                { "a: n ! = 1 , 3" },
                { "a: n = 1 .. 3" },
                { "a: n != 1 .. 3" },
                { "a: n ! = 1 .. 3" },

                // more complicated
                { "a:n in 3 .. 10 , 13 .. 19" },

                // singles have special exceptions
                { "a: n is 1" },
                { "a: n is not 1" },
                { "a: n not is 1", ParseException.class }, // hacked to fail
                { "a: n in 1" },
                { "a: n not in 1" },

                // multiples also have special exceptions
                // TODO enable the following once there is an update to CLDR
                // {"a: n is 1,3", ParseException.class},
                { "a: n is not 1,3", ParseException.class }, // hacked to fail
                { "a: n not is 1,3", ParseException.class }, // hacked to fail
                { "a: n in 1,3" },
                { "a: n not in 1,3" },

                // disallow not with =
                { "a: n not= 1", ParseException.class }, // hacked to fail
                { "a: n not= 1,3", ParseException.class }, // hacked to fail

                // disallow double negatives
                { "a: n ! is not 1", ParseException.class },
                { "a: n ! is not 1", ParseException.class },
                { "a: n not not in 1", ParseException.class },
                { "a: n is not not 1", NumberFormatException.class },

                // disallow screwy cases
                { null, NullPointerException.class }, { "djkl;", ParseException.class },
                { "a: n = 1 .", ParseException.class }, { "a: n = 1 ..", ParseException.class },
                { "a: n = 1 2", ParseException.class }, { "a: n = 1 ,", ParseException.class },
                { "a:n in 3 .. 10 , 13 .. 19 ,", ParseException.class }, };
        for (Object[] shouldFailTest : shouldFail) {
            String rules = (String) shouldFailTest[0];
            Class exception = shouldFailTest.length < 2 ? null : (Class) shouldFailTest[1];
            Class actualException = null;
            try {
                PluralRules.parseDescription(rules);
            } catch (Exception e) {
                actualException = e.getClass();
            }
            assertEquals("Exception " + rules, exception, actualException);
        }
    }

    @Test
    public void testSamples() {
        String description = "one: n is 3 or f is 5 @integer  3,19, @decimal 3.50 ~ 3.53,   …; other:  @decimal 99.0~99.2, 999.0, …";
        PluralRules test = PluralRules.createRules(description);

        checkNewSamples(description, test, "one", PluralRules.SampleType.INTEGER, "@integer 3, 19", true,
                DecimalQuantity_DualStorageBCD.fromExponentString("3"));
        checkNewSamples(description, test, "one", PluralRules.SampleType.DECIMAL, "@decimal 3.50~3.53, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("3.50"));
        checkOldSamples(description, test, "one", SampleType.INTEGER,
                DecimalQuantity_DualStorageBCD.fromExponentString("3"),
                DecimalQuantity_DualStorageBCD.fromExponentString("19"));
        checkOldSamples(description, test, "one", SampleType.DECIMAL,
                DecimalQuantity_DualStorageBCD.fromExponentString("3.50"),
                DecimalQuantity_DualStorageBCD.fromExponentString("3.51"),
                DecimalQuantity_DualStorageBCD.fromExponentString("3.52"),
                DecimalQuantity_DualStorageBCD.fromExponentString("3.53"));

        checkNewSamples(description, test, "other", PluralRules.SampleType.INTEGER, "", true, null);
        checkNewSamples(description, test, "other", PluralRules.SampleType.DECIMAL, "@decimal 99.0~99.2, 999.0, …",
                false, DecimalQuantity_DualStorageBCD.fromExponentString("99.0"));
        checkOldSamples(description, test, "other", SampleType.INTEGER);
        checkOldSamples(description, test, "other", SampleType.DECIMAL,
                DecimalQuantity_DualStorageBCD.fromExponentString("99.0"),
                DecimalQuantity_DualStorageBCD.fromExponentString("99.1"),
                DecimalQuantity_DualStorageBCD.fromExponentString("99.2"),
                DecimalQuantity_DualStorageBCD.fromExponentString("999.0"));
    }

    /**
     * This test is for the support of X.YeZ scientific notation of numbers in
     * the plural sample string.
     */
    @Test
    public void testSamplesWithExponent() {
        String description = "one: i = 0,1 @integer 0, 1, 1e5 @decimal 0.0~1.5, 1.1e5; "
                + "many: e = 0 and i != 0 and i % 1000000 = 0 and v = 0 or e != 0..5"
                + " @integer 1000000, 2e6, 3e6, 4e6, 5e6, 6e6, 7e6, … @decimal 2.1e6, 3.1e6, 4.1e6, 5.1e6, 6.1e6, 7.1e6, …; "
                + "other:  @integer 2~17, 100, 1000, 10000, 100000, 2e5, 3e5, 4e5, 5e5, 6e5, 7e5, …"
                + " @decimal 2.0~3.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 2.1e5, 3.1e5, 4.1e5, 5.1e5, 6.1e5, 7.1e5, …"
                ;
        // Creating the PluralRules object means being able to parse numbers
        // like 1e5 and 1.1e5
        PluralRules test = PluralRules.createRules(description);

        // Currently, 'c' is the canonical representation of numbers with suppressed exponent, and 'e'
        // is an alias. The test helpers here skip 'e' for round-trip sample string parsing and formatting.

        checkNewSamples(description, test, "one", PluralRules.SampleType.INTEGER, "@integer 0, 1, 1e5", true,
                DecimalQuantity_DualStorageBCD.fromExponentString("0"));
        checkNewSamples(description, test, "one", PluralRules.SampleType.DECIMAL, "@decimal 0.0~1.5, 1.1e5", true,
                DecimalQuantity_DualStorageBCD.fromExponentString("0.0"));
        checkNewSamples(description, test, "many", PluralRules.SampleType.INTEGER, "@integer 1000000, 2e6, 3e6, 4e6, 5e6, 6e6, 7e6, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("1000000"));
        checkNewSamples(description, test, "many", PluralRules.SampleType.DECIMAL, "@decimal 2.1e6, 3.1e6, 4.1e6, 5.1e6, 6.1e6, 7.1e6, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("2.1c6"));
        checkNewSamples(description, test, "other", PluralRules.SampleType.INTEGER, "@integer 2~17, 100, 1000, 10000, 100000, 2e5, 3e5, 4e5, 5e5, 6e5, 7e5, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("2"));
        checkNewSamples(description, test, "other", PluralRules.SampleType.DECIMAL, "@decimal 2.0~3.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 2.1e5, 3.1e5, 4.1e5, 5.1e5, 6.1e5, 7.1e5, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("2.0"));
    }

    /**
     * This test is for the support of X.YcZ compact notation of numbers in
     * the plural sample string.
     */
    @Test
    public void testSamplesWithCompactNotation() {
        String description = "one: i = 0,1 @integer 0, 1, 1c5 @decimal 0.0~1.5, 1.1c5; "
                + "many: c = 0 and i != 0 and i % 1000000 = 0 and v = 0 or c != 0..5"
                + " @integer 1000000, 2c6, 3c6, 4c6, 5c6, 6c6, 7c6, … @decimal 2.1c6, 3.1c6, 4.1c6, 5.1c6, 6.1c6, 7.1c6, …; "
                + "other:  @integer 2~17, 100, 1000, 10000, 100000, 2c5, 3c5, 4c5, 5c5, 6c5, 7c5, …"
                + " @decimal 2.0~3.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 2.1c5, 3.1c5, 4.1c5, 5.1c5, 6.1c5, 7.1c5, …"
                ;
        // Creating the PluralRules object means being able to parse numbers
        // like 1c5 and 1.1c5.
        // Note: Since `c` is currently an alias to `e`, the toString() of
        // FixedDecimal will return "1e5" even when input is "1c5".
        PluralRules test = PluralRules.createRules(description);

        checkNewSamples(description, test, "one", PluralRules.SampleType.INTEGER, "@integer 0, 1, 1c5", true,
                DecimalQuantity_DualStorageBCD.fromExponentString("0"));
        checkNewSamples(description, test, "one", PluralRules.SampleType.DECIMAL, "@decimal 0.0~1.5, 1.1c5", true,
                DecimalQuantity_DualStorageBCD.fromExponentString("0.0"));
        checkNewSamples(description, test, "many", PluralRules.SampleType.INTEGER, "@integer 1000000, 2c6, 3c6, 4c6, 5c6, 6c6, 7c6, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("1000000"));
        checkNewSamples(description, test, "many", PluralRules.SampleType.DECIMAL, "@decimal 2.1c6, 3.1c6, 4.1c6, 5.1c6, 6.1c6, 7.1c6, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("2.1c6"));
        checkNewSamples(description, test, "other", PluralRules.SampleType.INTEGER, "@integer 2~17, 100, 1000, 10000, 100000, 2c5, 3c5, 4c5, 5c5, 6c5, 7c5, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("2"));
        checkNewSamples(description, test, "other", PluralRules.SampleType.DECIMAL, "@decimal 2.0~3.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 2.1c5, 3.1c5, 4.1c5, 5.1c5, 6.1c5, 7.1c5, …", false,
                DecimalQuantity_DualStorageBCD.fromExponentString("2.0"));
    }

    public void checkOldSamples(String description, PluralRules rules, String keyword, SampleType sampleType,
            DecimalQuantity... expected) {
        Collection<DecimalQuantity> oldSamples = rules.getDecimalQuantitySamples(keyword, sampleType);

        // Collect actual (oldSamples) and expected (expectedSamplesList) into the
        // same concrete collection for comparison purposes.
        ArrayList<DecimalQuantity> oldSamplesList = new ArrayList(oldSamples);
        ArrayList<DecimalQuantity> expectedSamplesList = new ArrayList(Arrays.asList(expected));

        if (!assertEquals("getOldSamples; " + keyword + "; " + description, expectedSamplesList,
                oldSamplesList)) {
            rules.getSamples(keyword, sampleType);
        }
    }

    public void checkNewSamples(String description, PluralRules test, String keyword, SampleType sampleType,
            String samplesString, boolean isBounded, DecimalQuantity firstInRange) {
        String title = description + ", " + sampleType;
        DecimalQuantitySamples samples = test.getDecimalSamples(keyword, sampleType);
        if (samples != null) {

            // For now, skip round-trip formatting test when samples string uses
            // 'e' instead of 'c' for compact notation.

            // We are skipping tests for 'e' by replacing 'e' with 'c' in exponent
            // notation.
            Pattern p = Pattern.compile("(\\d+)(e)([-]?\\d+)");
            Matcher m = p.matcher(samplesString);
            if (m.find()) {
                samplesString = m.replaceAll("$1c$3");
            }

            assertEquals("samples; " + title, samplesString, samples.toString());
            assertEquals("bounded; " + title, isBounded, samples.bounded);
            assertEquals("first; " + title, firstInRange, samples.samples.iterator().next().start);
        }
        assertEquals("limited: " + title, isBounded, test.isLimited(keyword, sampleType));
    }

    private static final String[] parseTestData = { "a: n is 1", "a:1", "a: n mod 10 is 2", "a:2,12,22",
            "a: n is not 1", "a:0,2,3,4,5", "a: n mod 3 is not 1", "a:0,2,3,5,6,8,9", "a: n in 2..5", "a:2,3,4,5",
            "a: n within 2..5", "a:2,3,4,5", "a: n not in 2..5", "a:0,1,6,7,8", "a: n not within 2..5", "a:0,1,6,7,8",
            "a: n mod 10 in 2..5", "a:2,3,4,5,12,13,14,15,22,23,24,25", "a: n mod 10 within 2..5",
            "a:2,3,4,5,12,13,14,15,22,23,24,25", "a: n mod 10 is 2 and n is not 12", "a:2,22,32,42",
            "a: n mod 10 in 2..3 or n mod 10 is 5", "a:2,3,5,12,13,15,22,23,25",
            "a: n mod 10 within 2..3 or n mod 10 is 5", "a:2,3,5,12,13,15,22,23,25", "a: n is 1 or n is 4 or n is 23",
            "a:1,4,23", "a: n mod 2 is 1 and n is not 3 and n in 1..11", "a:1,5,7,9,11",
            "a: n mod 2 is 1 and n is not 3 and n within 1..11", "a:1,5,7,9,11",
            "a: n mod 2 is 1 or n mod 5 is 1 and n is not 6", "a:1,3,5,7,9,11,13,15,16",
            "a: n in 2..5; b: n in 5..8; c: n mod 2 is 1", "a:2,3,4,5;b:6,7,8;c:1,9,11",
            "a: n within 2..5; b: n within 5..8; c: n mod 2 is 1", "a:2,3,4,5;b:6,7,8;c:1,9,11",
            "a: n in 2,4..6; b: n within 7..9,11..12,20", "a:2,4,5,6;b:7,8,9,11,12,20",
            "a: n in 2..8,12 and n not in 4..6", "a:2,3,7,8,12", "a: n mod 10 in 2,3,5..7 and n is not 12",
            "a:2,3,5,6,7,13,15,16,17", "a: n in 2..6,3..7", "a:2,3,4,5,6,7", };

    private String[] getTargetStrings(String targets) {
        List list = new ArrayList(50);
        String[] valSets = Utility.split(targets, ';');
        for (int i = 0; i < valSets.length; ++i) {
            String[] temp = Utility.split(valSets[i], ':');
            String key = temp[0].trim();
            String[] vals = Utility.split(temp[1], ',');
            for (int j = 0; j < vals.length; ++j) {
                String valString = vals[j].trim();
                int val = Integer.parseInt(valString);
                while (list.size() <= val) {
                    list.add(null);
                }
                if (list.get(val) != null) {
                    fail("test data error, key: " + list.get(val) + " already set for: " + val);
                }
                list.set(val, key);
            }
        }

        String[] result = (String[]) list.toArray(new String[list.size()]);
        for (int i = 0; i < result.length; ++i) {
            if (result[i] == null) {
                result[i] = "other";
            }
        }
        return result;
    }

    private void checkTargets(PluralRules rules, String[] targets) {
        for (int i = 0; i < targets.length; ++i) {
            assertEquals("value " + i, targets[i], rules.select(i));
        }
    }

    @Test
    public void testParseEmpty() throws ParseException {
        PluralRules rules = PluralRules.parseDescription("a:n");
        assertEquals("empty", "a", rules.select(0));
    }

    @Test
    public void testParsing() {
        for (int i = 0; i < parseTestData.length; i += 2) {
            String pattern = parseTestData[i];
            String expected = parseTestData[i + 1];

            logln("pattern[" + i + "] " + pattern);
            try {
                PluralRules rules = PluralRules.createRules(pattern);
                String[] targets = getTargetStrings(expected);
                checkTargets(rules, targets);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static String[][] operandTestData = { { "a: n 3", "FAIL" },
            { "a: n=1,2; b: n != 3..5; c:n!=5", "a:1,2; b:6,7; c:3,4" },
            { "a: n=1,2; b: n!=3..5; c:n!=5", "a:1,2; b:6,7; c:3,4" },
            { "a: t is 1", "a:1.1,1.1000,99.100; other:1.2,1.0" }, { "a: f is 1", "a:1.1; other:1.1000,99.100" },
            { "a: i is 2; b:i is 3", "b: 3.5; a: 2.5" }, { "a: f is 0; b:f is 50", "a: 1.00; b: 1.50" },
            { "a: v is 1; b:v is 2", "a: 1.0; b: 1.00" }, { "one: n is 1 AND v is 0", "one: 1 ; other: 1.00,1.0" }, // English
                                                                                                                    // rules
            { "one: v is 0 and i mod 10 is 1 or f mod 10 is 1", "one: 1, 1.1, 3.1; other: 1.0, 3.2, 5" }, // Last
                                                                                                          // visible
                                                                                                          // digit
            { "one: j is 0", "one: 0; other: 0.0, 1.0, 3" }, // Last visible digit
    // one → n is 1; few → n in 2..4;
    };

    @Test
    public void testOperands() {
        for (String[] pair : operandTestData) {
            String pattern = pair[0].trim();
            String categoriesAndExpected = pair[1].trim();

            // logln("pattern[" + i + "] " + pattern);
            boolean FAIL_EXPECTED = categoriesAndExpected.equalsIgnoreCase("fail");
            try {
                logln(pattern);
                PluralRules rules = PluralRules.createRules(pattern);
                if (FAIL_EXPECTED) {
                    assertNull("Should fail with 'null' return.", rules);
                } else {
                    logln(rules == null ? "null rules" : rules.toString());
                    checkCategoriesAndExpected(pattern, categoriesAndExpected, rules);
                }
            } catch (Exception e) {
                if (!FAIL_EXPECTED) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    @Test
    public void testUniqueRules() {
        main: for (ULocale locale : factory.getAvailableULocales()) {
            PluralRules rules = factory.forLocale(locale);
            Map<String, PluralRules> keywordToRule = new HashMap<>();

            // get the set of all rule samples from all of the keywords of the locale's rule
            Set<DecimalQuantitySamples> samples =
                    rules.getKeywords()
                        .stream()
                        .flatMap(keyword -> {
                            return Arrays.stream(SampleType.values())
                                    .map(sampleType -> rules.getDecimalSamples(keyword, sampleType));
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

            // take the rule substring per keyword, and create a map keyed by keyword for a new rules object of that rule substring
            for (String keyword : rules.getKeywords()) {
                if (keyword.equals("other")) {
                    continue;
                }
                String rules2 = keyword + ":" + rules.getRules(keyword);
                PluralRules singleRule = PluralRules.createRules(rules2);
                if (singleRule == null) {
                    errln("Can't generate single rule for " + rules2);
                    PluralRules.createRules(rules2); // for debugging
                    continue main;
                }
                keywordToRule.put(keyword, singleRule);
            }

            Map<DecimalQuantity, String> collisionTest = new LinkedHashMap();

            // get all of the sample ranges from all of the samples
            Stream<DecimalQuantitySamplesRange> ranges = samples.stream()
                .map(DecimalQuantitySamples::getSamples)
                .filter(Objects::nonNull)
                .flatMap(Set::stream);

            // get all of the sample values at the endpoints of each sample range
            Stream<DecimalQuantity> items = ranges.flatMap(range -> {
                return Arrays.stream(new DecimalQuantity[] {range.start, range.end});
            });

            items.forEach(item -> {
                collisionTest.clear();
                for (Entry<String, PluralRules> entry : keywordToRule.entrySet()) {
                    PluralRules rule = entry.getValue();
                    String foundKeyword = rule.select(item);
                    if (foundKeyword.equals("other")) {
                        continue;
                    }
                    String old = collisionTest.get(item);
                    if (old != null) {
                        errln(locale + "\tNon-unique rules: " + item + " => " + old + " & " + foundKeyword);
                        rule.select(item);
                    } else {
                        collisionTest.put(item, foundKeyword);
                    }
                }
            });
        }
    }

    private void checkCategoriesAndExpected(String title1, String categoriesAndExpected, PluralRules rules) {
        for (String categoryAndExpected : categoriesAndExpected.split("\\s*;\\s*")) {
            String[] categoryFromExpected = categoryAndExpected.split("\\s*:\\s*");
            String expected = categoryFromExpected[0];
            for (String value : categoryFromExpected[1].split("\\s*,\\s*")) {
                if (value.startsWith("@") || value.equals("…") || value.equals("null")) {
                    continue;
                }
                String[] values = value.split("\\s*~\\s*");
                checkValue(title1, rules, expected, values[0]);
                if (values.length > 1) {
                    checkValue(title1, rules, expected, values[1]);
                }
            }
        }
    }

    public void checkValue(String title1, PluralRules rules, String expected, String value) {
        DecimalQuantity dqNum = DecimalQuantity_DualStorageBCD.fromExponentString(value);

        String result = rules.select(dqNum);
        ULocale locale = null;
        assertEquals(getAssertMessage(title1, locale, rules, expected) + "; value: " + value, expected, result);
    }

    /**
     * Check the testing helper method checkValue(), which parses a plural
     * rule's sample string as a {@link FormattedNumber} in order to call
     * {@code PluralRules.select(FormattedNumber)}, which in turn can support
     * the exponent in plural sample numbers like 1e6 and 2.8c3.
     */
    @Test
    public void testCheckValue() {
        String ruleString =
            "many: e = 0 and i != 0 and i % 1000000 = 0 and v = 0 or e != 0..5"
            + " @integer 1000000, 1e6, 2e6, 3e6, 4e6, 5e6, 6e6, …"
            + " @decimal 1.0000001e6, 1.1e6, 2.0000001e6, 2.1e6, 3.0000001e6, 3.1e6, …;  "
            + "one: i = 1 and v = 0"
            + " @integer 1;  "
            + "other: "
            + " @integer 0, 2~16, 100, 1000, 10000, 100000, 1e3, 2e3, 3e3, 4e3, 5e3, 6e3, …"
            + " @decimal 0.0~1.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 1.0001e3, 1.1e3, 2.0001e3, 2.1e3, 3.0001e3, 3.1e3, …";
        PluralRules rules = PluralRules.createRules(ruleString);

        Object[][] casesData = {
                // expected category, value string
                {"many",  "1000000"},
                {"many",  "1e6"},
                {"many",  "1.1e6"},
                {"one",   "1"},
                {"other", "0"},
                {"other", "1e5"},
                {"other", "100000"},
                {"other", "0.0"},
                {"other", "100000.0"},
                {"other", "1000000.0"}
        };

        for (Object[] caseDatum : casesData) {
            String expCategory = (String) caseDatum[0];
            String inputValueStr = (String) caseDatum[1];

            String msg = "checkValue(" + inputValueStr + ")";

            checkValue(msg, rules, expCategory, inputValueStr);
        }
    }

    private static String[][] equalityTestData = {
            // once we add fractions, we had to retract the "test all possibilities" for equality,
            // so we only have a limited set of equality tests now.
            { "c: n%11!=5", "c: n mod 11 is not 5" }, { "c: n is not 7", "c: n != 7" }, { "a:n in 2;", "a: n = 2" },
            { "b:n not in 5;", "b: n != 5" },

    // { "a: n is 5",
    // "a: n in 2..6 and n not in 2..4 and n is not 6" },
    // { "a: n in 2..3",
    // "a: n is 2 or n is 3",
    // "a: n is 3 and n in 2..5 or n is 2" },
    // { "a: n is 12; b:n mod 10 in 2..3",
    // "b: n mod 10 in 2..3 and n is not 12; a: n in 12..12",
    // "b: n is 13; a: n is 12; b: n mod 10 is 2 or n mod 10 is 3" },
    };

    private static String[][] inequalityTestData = { { "a: n mod 8 is 3", "a: n mod 7 is 3" },
            { "a: n mod 3 is 2 and n is not 5", "a: n mod 6 is 2 or n is 8 or n is 11" },
            // the following are currently inequal, but we may make them equal in the future.
            { "a: n in 2..5", "a: n in 2..4,5" }, };

    private void compareEquality(String id, Object[] objects, boolean shouldBeEqual) {
        for (int i = 0; i < objects.length; ++i) {
            Object lhs = objects[i];
            int start = shouldBeEqual ? i : i + 1;
            for (int j = start; j < objects.length; ++j) {
                Object rhs = objects[j];
                if (rhs == null || shouldBeEqual != lhs.equals(rhs)) {
                    String msg = shouldBeEqual ? "should be equal" : "should not be equal";
                    fail(id + " " + msg + " (" + i + ", " + j + "):\n    " + lhs + "\n    " + rhs);
                }
                // assertEquals("obj " + i + " and " + j, lhs, rhs);
            }
        }
    }

    private void compareEqualityTestSets(String[][] sets, boolean shouldBeEqual) {
        for (int i = 0; i < sets.length; ++i) {
            String[] patterns = sets[i];
            PluralRules[] rules = new PluralRules[patterns.length];
            for (int j = 0; j < patterns.length; ++j) {
                rules[j] = PluralRules.createRules(patterns[j]);
            }
            compareEquality("test " + i, rules, shouldBeEqual);
        }
    }

    @Test
    public void testEquality() {
        compareEqualityTestSets(equalityTestData, true);
    }

    @Test
    public void testInequality() {
        compareEqualityTestSets(inequalityTestData, false);
    }

    @Test
    public void testBuiltInRules() {
        Object[][] cases = {
                {"en-US", PluralRules.KEYWORD_OTHER, 0},
                {"en-US", PluralRules.KEYWORD_ONE, 1},
                {"en-US", PluralRules.KEYWORD_OTHER, 2},
                {"ja-JP", PluralRules.KEYWORD_OTHER, 0},
                {"ja-JP", PluralRules.KEYWORD_OTHER, 1},
                {"ja-JP", PluralRules.KEYWORD_OTHER, 2},
                {"ru", PluralRules.KEYWORD_MANY, 0},
                {"ru", PluralRules.KEYWORD_ONE, 1},
                {"ru", PluralRules.KEYWORD_FEW, 2}
        };
        for (Object[] cas : cases) {
            ULocale locale = new ULocale((String) cas[0]);
            PluralRules rules = factory.forLocale(locale);
            String expectedKeyword = (String) cas[1];
            double number = (Integer) cas[2];
            String message = locale + " " + number;
            // Check both as double and as FormattedNumber.
            assertEquals(message, expectedKeyword, rules.select(number));
            FormattedNumber fn = NumberFormatter.withLocale(locale).format(number);
            assertEquals(message, expectedKeyword, rules.select(fn));
        }
    }

    @Test
    public void testSelectTrailingZeros() {
        UnlocalizedNumberFormatter unf = NumberFormatter.with()
                .precision(Precision.fixedFraction(2));
        Object[][] cases = {
                // 1) locale
                // 2) double expected keyword
                // 3) formatted number expected keyword (2 fraction digits)
                // 4) input number
                {"bs",  PluralRules.KEYWORD_FEW,   PluralRules.KEYWORD_OTHER, 5.2},  // 5.2 => two, but 5.20 => other
                {"si",  PluralRules.KEYWORD_ONE,   PluralRules.KEYWORD_ONE,   0.0},
                {"si",  PluralRules.KEYWORD_ONE,   PluralRules.KEYWORD_ONE,   1.0},
                {"si",  PluralRules.KEYWORD_ONE,   PluralRules.KEYWORD_OTHER, 0.1},  // 0.1 => one, but 0.10 => other
                {"si",  PluralRules.KEYWORD_ONE,   PluralRules.KEYWORD_ONE,   0.01}, // 0.01 => one
                {"hsb", PluralRules.KEYWORD_FEW,   PluralRules.KEYWORD_FEW,   1.03}, // (f % 100 == 3) => few
                {"hsb", PluralRules.KEYWORD_FEW,   PluralRules.KEYWORD_OTHER, 1.3},  // 1.3 => few, but 1.30 => other
        };
        for (Object[] cas : cases) {
            ULocale locale = new ULocale((String) cas[0]);
            PluralRules rules = factory.forLocale(locale);
            String expectedDoubleKeyword = (String) cas[1];
            String expectedFormattedKeyword = (String) cas[2];
            double number = (Double) cas[3];
            String message = locale + " " + number;
            // Check both as double and as FormattedNumber.
            assertEquals(message, expectedDoubleKeyword, rules.select(number));
            FormattedNumber fn = unf.locale(locale).format(number);
            assertEquals(message, expectedFormattedKeyword, rules.select(fn));
        }
    }

    private void compareLocaleResults(String loc1, String loc2, String loc3) {
        PluralRules rules1 = PluralRules.forLocale(new ULocale(loc1));
        PluralRules rules2 = PluralRules.forLocale(new ULocale(loc2));
        PluralRules rules3 = PluralRules.forLocale(new ULocale(loc3));
        for (int value = 0; value <= 12; value++) {
            String result1 = rules1.select(value);
            String result2 = rules2.select(value);
            String result3 = rules3.select(value);
            if (!result1.equals(result2) || !result1.equals(result3)) {
                errln("PluralRules.select(" + value + ") does not return the same values for "
                        + loc1 + ", " + loc2 + ", " + loc3);
            }
        }
    }

    @Test
    public void testLocaleExtension() {
        PluralRules rules = PluralRules.forLocale(new ULocale("pt@calendar=gregorian"));
        String key = rules.select(1);
        assertEquals("pt@calendar=gregorian select(1)", "one", key);
        compareLocaleResults("ar", "ar_SA", "ar_SA@calendar=gregorian");
        compareLocaleResults("ru", "ru_UA", "ru-u-cu-RUB");
        compareLocaleResults("fr", "fr_CH", "fr@ms=uksystem");
    }

    @Test
    public void testFunctionalEquivalent() {
        // spot check
        ULocale unknown = ULocale.createCanonical("zz_ZZ");
        ULocale un_equiv = PluralRules.getFunctionalEquivalent(unknown, null);
        assertEquals("unknown locales have root", ULocale.ROOT, un_equiv);

        ULocale jp_equiv = PluralRules.getFunctionalEquivalent(ULocale.JAPAN, null);
        ULocale cn_equiv = PluralRules.getFunctionalEquivalent(ULocale.CHINA, null);
        assertEquals("japan and china equivalent locales", jp_equiv, cn_equiv);

        boolean[] available = new boolean[1];
        ULocale russia = ULocale.createCanonical("ru_RU");
        ULocale ru_ru_equiv = PluralRules.getFunctionalEquivalent(russia, available);
        assertFalse("ru_RU not listed", available[0]);

        ULocale russian = ULocale.createCanonical("ru");
        ULocale ru_equiv = PluralRules.getFunctionalEquivalent(russian, available);
        assertTrue("ru listed", available[0]);
        assertEquals("ru and ru_RU equivalent locales", ru_ru_equiv, ru_equiv);
    }

    @Test
    public void testAvailableULocales() {
        ULocale[] locales = factory.getAvailableULocales();
        Set localeSet = new HashSet();
        localeSet.addAll(Arrays.asList(locales));

        assertEquals("locales are unique in list", locales.length, localeSet.size());
    }

    /*
     * Test the method public static PluralRules parseDescription(String description)
     */
    @Test
    public void TestParseDescription() {
        try {
            if (PluralRules.DEFAULT != PluralRules.parseDescription("")) {
                errln("PluralRules.parseDescription(String) was suppose "
                        + "to return PluralRules.DEFAULT when String is of " + "length 0.");
            }
        } catch (ParseException e) {
            errln("PluralRules.parseDescription(String) was not suppose " + "to return an exception.");
        }
    }

    /*
     * Tests the method public static PluralRules createRules(String description)
     */
    @Test
    public void TestCreateRules() {
        try {
            if (PluralRules.createRules(null) != null) {
                errln("PluralRules.createRules(String) was suppose to "
                        + "return null for an invalid String descrtiption.");
            }
        } catch (Exception e) {
        }
    }

    /*
     * Tests the method public int hashCode()
     */
    @Test
    public void TestHashCode() {
        // Bad test, breaks whenever PluralRules implementation changes.
        // PluralRules pr = PluralRules.DEFAULT;
        // if (106069776 != pr.hashCode()) {
        // errln("PluralRules.hashCode() was suppose to return 106069776 " + "when PluralRules.DEFAULT.");
        // }
    }

    /*
     * Tests the method public boolean equals(PluralRules rhs)
     */
    @Test
    public void TestEquals() {
        PluralRules pr = PluralRules.DEFAULT;

        if (pr.equals((PluralRules) null)) {
            errln("PluralRules.equals(PluralRules) was supposed to return false " + "when passing null.");
        }
    }

    private void assertRuleValue(String rule, DecimalQuantity value) {
        assertRuleKeyValue("a:" + rule, "a", value);
    }

    private void assertRuleKeyValue(String rule, String key, DecimalQuantity value) {
        PluralRules pr = PluralRules.createRules(rule);

        // as a DecimalQuantity
        assertEquals(rule, value, pr.getUniqueKeywordDecimalQuantityValue(key));

        // as a double
        double expDouble = value.equals(PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY) ?
                PluralRules.NO_UNIQUE_VALUE : value.toDouble();
        assertEquals(rule, expDouble, pr.getUniqueKeywordValue(key));
    }

    /*
     * Tests getUniqueKeywordValue()
     */
    @Test
    public void TestGetUniqueKeywordValue() {
        LocalizedNumberFormatter fmtr = NumberFormatter.withLocale(ULocale.ROOT);

        assertRuleKeyValue("a: n is 1", "not_defined", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY); // key not defined
        assertRuleValue("n within 2..2", new DecimalQuantity_DualStorageBCD(2));
        assertRuleValue("n is 1", new DecimalQuantity_DualStorageBCD(1));
        assertRuleValue("n in 2..2", new DecimalQuantity_DualStorageBCD(2));
        assertRuleValue("n in 3..4", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
        assertRuleValue("n within 3..4", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
        assertRuleValue("n is 2 or n is 2", new DecimalQuantity_DualStorageBCD(2));
        assertRuleValue("n is 2 and n is 2", new DecimalQuantity_DualStorageBCD(2));
        assertRuleValue("n is 2 or n is 3", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
        assertRuleValue("n is 2 and n is 3", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
        assertRuleValue("n is 2 or n in 2..3", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
        assertRuleValue("n is 2 and n in 2..3", new DecimalQuantity_DualStorageBCD(2));
        assertRuleKeyValue("a: n is 1", "other", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY); // key matches default rule
        assertRuleValue("n in 2,3", PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
        assertRuleValue("n in 2,3..6 and n not in 2..3,5..6", new DecimalQuantity_DualStorageBCD(4));
    }

    /**
     * The version in PluralFormatUnitTest is not really a test, and it's in the wrong place anyway, so I'm putting a
     * variant of it here.
     *
     * Using the double API for getting plural samples, assert all samples match the keyword
     * they are listed under, for all locales.
     *
     * Specifically, iterate over all locales, get plural rules for the locale, iterate over every rule,
     * then iterate over every sample in the rule, parse sample to a number (double), use that number
     * as an input to .select() for the rules object, and assert the actual return plural keyword matches
     * what we expect based on the plural rule string.
     */
    @Test
    public void TestGetSamples() {
        Set<ULocale> uniqueRuleSet = new HashSet<>();
        for (ULocale locale : factory.getAvailableULocales()) {
            uniqueRuleSet.add(PluralRules.getFunctionalEquivalent(locale, null));
        }
        for (ULocale locale : uniqueRuleSet) {
            PluralRules rules = factory.forLocale(locale);
            logln("\nlocale: " + (locale == ULocale.ROOT ? "root" : locale.toString()) + ", rules: " + rules);
            Set<String> keywords = rules.getKeywords();
            for (String keyword : keywords) {
                Collection<Double> list = rules.getSamples(keyword);
                logln("keyword: " + keyword + ", samples: " + list);
                // with fractions, the samples can be empty and thus the list null. In that case, however, there will be
                // FixedDecimal values.
                // So patch the test for that.
                if (list.size() == 0) {
                    // when the samples (meaning integer samples) are null, then then integerSamples must be, and the
                    // decimalSamples must not be
                    DecimalQuantitySamples integerSamples = rules.getDecimalSamples(keyword, SampleType.INTEGER);
                    DecimalQuantitySamples decimalSamples = rules.getDecimalSamples(keyword, SampleType.DECIMAL);
                    assertTrue(getAssertMessage("List is not null", locale, rules, keyword), integerSamples == null
                            && decimalSamples != null && decimalSamples.samples.size() != 0);
                } else {
                    if (!assertTrue(getAssertMessage("Test getSamples.isEmpty", locale, rules, keyword),
                            !list.isEmpty())) {
                        rules.getSamples(keyword);
                    }
                    if (rules.toString().contains(": j")) {
                        // hack until we remove j
                    } else {
                        for (double value : list) {
                            assertEquals(getAssertMessage("Match keyword", locale, rules, keyword) + "; value '"
                                    + value + "'", keyword, rules.select(value));
                        }
                    }
                }
            }

            assertNull(locale + ", list is null", rules.getSamples("@#$%^&*"));
            assertNull(locale + ", list is null", rules.getSamples("@#$%^&*", SampleType.DECIMAL));
        }
    }

    /**
     * This replicates the setup of TestGetSamples(), but parses samples as DecimalQuantity instead of double.
     *
     * Using the DecimalQuantity API for getting plural samples, assert all samples match the keyword
     * they are listed under, for all locales.
     *
     * Specifically, iterate over all locales, get plural rules for the locale, iterate over every rule,
     * then iterate over every sample in the rule, parse sample to a number (DecimalQuantity), use that number
     * as an input to .select() for the rules object, and assert the actual return plural keyword matches
     * what we expect based on the plural rule string.
     */
    @Test
    public void TestGetDecimalQuantitySamples() {
        Set<ULocale> uniqueRuleSet = new HashSet<>();
        for (ULocale locale : factory.getAvailableULocales()) {
            uniqueRuleSet.add(PluralRules.getFunctionalEquivalent(locale, null));
        }
        for (ULocale locale : uniqueRuleSet) {
            PluralRules rules = factory.forLocale(locale);
            logln("\nlocale: " + (locale == ULocale.ROOT ? "root" : locale.toString()) + ", rules: " + rules);
            Set<String> keywords = rules.getKeywords();
            for (String keyword : keywords) {
                Collection<DecimalQuantity> list = rules.getDecimalQuantitySamples(keyword);
                logln("keyword: " + keyword + ", samples: " + list);
                // with fractions, the samples can be empty and thus the list null. In that case, however, there will be
                // FixedDecimal values.
                // So patch the test for that.
                if (list.size() == 0) {
                    // when the samples (meaning integer samples) are null, then then integerSamples must be, and the
                    // decimalSamples must not be
                    DecimalQuantitySamples integerSamples = rules.getDecimalSamples(keyword, SampleType.INTEGER);
                    DecimalQuantitySamples decimalSamples = rules.getDecimalSamples(keyword, SampleType.DECIMAL);
                    assertTrue(getAssertMessage("List is not null", locale, rules, keyword), integerSamples == null
                            && decimalSamples != null && decimalSamples.samples.size() != 0);
                } else {
                    if (!assertTrue(getAssertMessage("Test getSamples.isEmpty", locale, rules, keyword),
                            !list.isEmpty())) {
                        rules.getDecimalQuantitySamples(keyword);
                    }
                    if (rules.toString().contains(": j")) {
                        // hack until we remove j
                    } else {
                        for (DecimalQuantity value : list) {
                            assertEquals(getAssertMessage("Match keyword", locale, rules, keyword) + "; value '"
                                    + value + "'", keyword, rules.select(value));
                        }
                    }
                }
            }

            assertNull(locale + ", list is null", rules.getDecimalQuantitySamples("@#$%^&*"));
            assertNull(locale + ", list is null", rules.getDecimalQuantitySamples("@#$%^&*", SampleType.DECIMAL));
        }
    }

    /**
     * Test addSamples (Java) / getSamplesFromString (C++) to ensure the expansion of plural rule sample range
     * expands to a sequence of sample numbers that is incremented as the right scale.
     *
     *  Do this for numbers with fractional digits but no exponent.
     */
    @Test
    public void testGetOrAddSamplesFromString() {
        PluralRules rules = PluralRules.createRules("testkeyword: e != 0 @decimal 2.0~4.0, …");

        Set<String> keywords = rules.getKeywords();
        assertTrue("At least parse the test keyword in the test rule string", 0 < keywords.size());

        String expKeyword = "testkeyword";
        Collection<DecimalQuantity> list = rules.getDecimalQuantitySamples(expKeyword, SampleType.DECIMAL);

        String[] expDqStrs = {
                "2.0", "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9",
                "3.0", "3.1", "3.2", "3.3", "3.4", "3.5", "3.6", "3.7", "3.8", "3.9",
                "4.0"
        };
        assertEquals("Number of parsed samples from test string incorrect", expDqStrs.length, list.size());
        ArrayList<DecimalQuantity> actSamples = new ArrayList<>(list);
        for (int i = 0; i < list.size(); i++) {
            String expDqStr = expDqStrs[i];
            DecimalQuantity sample = actSamples.get(i);
            String sampleStr = sample.toExponentString();

            assertEquals("Expansion of sample range to sequence of sample values should increment at the right scale",
                    expDqStr, sampleStr);

        }
    }

    /**
     * Test addSamples (Java) / getSamplesFromString (C++) to ensure the expansion of plural rule sample range
     * expands to a sequence of sample numbers that is incremented as the right scale.
     *
     *  Do this for numbers written in a notation that has an exponent, for which the number is an
     *  integer (also as defined in the UTS 35 spec for the plural operands) but whose representation
     *  has fractional digits in the significand written before the exponent.
     */
    @Test
    public void testGetOrAddSamplesFromStringCompactNotation() {
        PluralRules rules = PluralRules.createRules("testkeyword: e != 0 @decimal 2.0c6~4.0c6, …");

        Set<String> keywords = rules.getKeywords();
        assertTrue("At least parse the test keyword in the test rule string", 0 < keywords.size());

        String expKeyword = "testkeyword";
        Collection<DecimalQuantity> list = rules.getDecimalQuantitySamples(expKeyword, SampleType.DECIMAL);

        String[] expDqStrs = {
                "2.0c6", "2.1c6", "2.2c6", "2.3c6", "2.4c6", "2.5c6", "2.6c6", "2.7c6", "2.8c6", "2.9c6",
                "3.0c6", "3.1c6", "3.2c6", "3.3c6", "3.4c6", "3.5c6", "3.6c6", "3.7c6", "3.8c6", "3.9c6",
                "4.0c6"
        };
        assertEquals("Number of parsed samples from test string incorrect", expDqStrs.length, list.size());
        ArrayList<DecimalQuantity> actSamples = new ArrayList<>(list);
        for (int i = 0; i < list.size(); i++) {
            String expDqStr = expDqStrs[i];
            DecimalQuantity sample = actSamples.get(i);
            String sampleStr = sample.toExponentString();

            assertEquals("Expansion of sample range to sequence of sample values should increment at the right scale",
                    expDqStr, sampleStr);

        }
    }

    public String getAssertMessage(String message, ULocale locale, PluralRules rules, String keyword) {
        String ruleString = "";
        if (keyword != null) {
            if (keyword.equals("other")) {
                for (String keyword2 : rules.getKeywords()) {
                    ruleString += " NOR " + rules.getRules(keyword2).split("@")[0];
                }
            } else {
                String rule = rules.getRules(keyword);
                ruleString = rule == null ? null : rule.split("@")[0];
            }
            ruleString = "; rule: '" + keyword + ": " + ruleString + "'";
            // !keyword.equals("other") ? "'; keyword: '" + keyword + "'; rule: '" + rules.getRules(keyword) + "'"
            // : "'; keyword: '" + keyword + "'; rules: '" + rules.toString() + "'";
        }
        return message + (locale == null ? "" : "; locale: '" + locale + "'") + ruleString;
    }

    /**
     * Returns the empty set if the keyword is not defined, null if there are an unlimited number of values for the
     * keyword, or the set of values that trigger the keyword.
     */
    @Test
    public void TestGetAllKeywordValues() {
        // data is pairs of strings, the rule, and the expected values as arguments
        String[] data = {
                "other: ; a: n mod 3 is 0",
                "a: null",
                "a: n in 2..5 and n within 5..8",
                "a: 5",
                "a: n in 2..5",
                "a: 2,3,4,5; other: null",
                "a: n not in 2..5",
                "a: null; other: null",
                "a: n within 2..5",
                "a: 2,3,4,5; other: null",
                "a: n not within 2..5",
                "a: null; other: null",
                "a: n in 2..5 or n within 6..8",
                "a: 2,3,4,5,6,7,8", // ignore 'other' here on out, always null
                "a: n in 2..5 and n within 6..8",
                "a: null",
                // we no longer support 'degenerate' rules
                // "a: n within 2..5 and n within 6..8", "a:", // our sampling catches these
                // "a: n within 2..5 and n within 5..8", "a: 5", // ''
                // "a: n within 1..2 and n within 2..3 or n within 3..4 and n within 4..5", "a: 2,4",
                // "a: n mod 3 is 0 and n within 0..5", "a: 0,3",
                "a: n within 1..2 and n within 2..3 or n within 3..4 and n within 4..5 or n within 5..6 and n within 6..7",
                "a: 2,4,6", // but not this...
                "a: n mod 3 is 0 and n within 1..2", "a: null", "a: n mod 3 is 0 and n within 0..6", "a: 0,3,6",
                "a: n mod 3 is 0 and n in 3..12", "a: 3,6,9,12", "a: n in 2,4..6 and n is not 5", "a: 2,4,6", };
        for (int i = 0; i < data.length; i += 2) {
            String ruleDescription = data[i];
            String result = data[i + 1];

            PluralRules p = PluralRules.createRules(ruleDescription);
            if (p == null) { // for debugging
                PluralRules.createRules(ruleDescription);
            }
            for (String ruleResult : result.split(";")) {
                String[] ruleAndValues = ruleResult.split(":");
                String keyword = ruleAndValues[0].trim();
                String valueList = ruleAndValues.length < 2 ? null : ruleAndValues[1];
                if (valueList != null) {
                    valueList = valueList.trim();
                }
                Collection<DecimalQuantity> values;
                if (valueList == null || valueList.length() == 0) {
                    values = Collections.EMPTY_SET;
                } else if ("null".equals(valueList)) {
                    values = null;
                } else {
                    values = new LinkedHashSet<>();
                    for (String value : valueList.split(",")) {
                        values.add(DecimalQuantity_DualStorageBCD.fromExponentString(value));
                    }
                }

                Collection<DecimalQuantity> results = p.getAllKeywordDecimalQuantityValues(keyword);

                // Convert DecimalQuantity using a 1:1 conversion to String for comparison purposes
                Set<String> valuesForComparison = new HashSet<>();
                if (values != null) {
                    for (DecimalQuantity dq : values) {
                        valuesForComparison.add(dq.toExponentString());
                    }
                }
                Set<String> resultsForComparison = new HashSet<>();
                if (results != null) {
                    for (DecimalQuantity dq : results) {
                        resultsForComparison.add(dq.toExponentString());
                    }
                }

                assertEquals(keyword + " in " + ruleDescription,
                        values == null ? null : valuesForComparison,
                        results == null ? null : resultsForComparison
                );

                if (results != null) {
                    try {
                        results.add(PluralRules.NO_UNIQUE_VALUE_DECIMAL_QUANTITY);
                        fail("returned set is modifiable");
                    } catch (UnsupportedOperationException e) {
                        // pass
                    }
                }
            }
        }
    }

    @Test
    public void TestOrdinal() {
        PluralRules pr = factory.forLocale(ULocale.ENGLISH, PluralType.ORDINAL);
        assertEquals("PluralRules(en-ordinal).select(2)", "two", pr.select(2));
    }

    @Test
    public void TestBasicFraction() {
        String[][] tests = { { "en", "one: j is 1" }, { "1", "0", "1", "one" }, { "1", "2", "1.00", "other" }, };
        ULocale locale = null;
        NumberFormat nf = null;
        PluralRules pr = null;

        for (String[] row : tests) {
            switch (row.length) {
            case 2:
                locale = ULocale.forLanguageTag(row[0]);
                nf = NumberFormat.getInstance(locale);
                pr = PluralRules.createRules(row[1]);
                break;
            case 4:
                double n = Double.parseDouble(row[0]);
                int minFracDigits = Integer.parseInt(row[1]);
                nf.setMinimumFractionDigits(minFracDigits);
                String expectedFormat = row[2];
                String expectedKeyword = row[3];

                UFieldPosition pos = new UFieldPosition();
                String formatted = nf.format(1.0, new StringBuffer(), pos).toString();
                int countVisibleFractionDigits = pos.getCountVisibleFractionDigits();
                long fractionDigits = pos.getFractionDigits();
                String keyword = pr.select(n, countVisibleFractionDigits, fractionDigits);
                assertEquals("Formatted " + n + "\t" + minFracDigits, expectedFormat, formatted);
                assertEquals("Keyword " + n + "\t" + minFracDigits, expectedKeyword, keyword);
                break;
            default:
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void TestLimitedAndSamplesConsistency() {
        for (ULocale locale : PluralRules.getAvailableULocales()) {
            ULocale loc2 = PluralRules.getFunctionalEquivalent(locale, null);
            if (!loc2.equals(locale)) {
                continue; // only need "unique" rules
            }
            for (PluralType type : PluralType.values()) {
                PluralRules rules = PluralRules.forLocale(locale, type);
                for (SampleType sampleType : SampleType.values()) {
                    if (type == PluralType.ORDINAL) {
                        logKnownIssue("10783", "Fix issues with isLimited vs computeLimited on ordinals");
                        continue;
                    }
                    for (String keyword : rules.getKeywords()) {
                        boolean isLimited = rules.isLimited(keyword, sampleType);
                        boolean computeLimited = rules.computeLimited(keyword, sampleType);
                        Collection<DecimalQuantity> samples = rules.getDecimalQuantitySamples(keyword, sampleType);
                        assertNotNull(getAssertMessage("Samples must not be null", locale, rules, keyword), samples);
                        rules.getDecimalSamples(keyword, sampleType);
                        // assertNotNull(getAssertMessage("Decimal samples must be null if unlimited", locale, rules,
                        // keyword), decimalSamples);
                    }
                }
            }
        }
    }

    @Test
    public void TestKeywords() {
        Set<String> possibleKeywords = new LinkedHashSet(Arrays.asList("zero", "one", "two", "few", "many", "other"));
        DecimalQuantity ONE_INTEGER = DecimalQuantity_DualStorageBCD.fromExponentString("1");
        Object[][][] tests = {
                // format is locale, explicits, then triples of keyword, status, unique value.
                { { "en", null }, { "one", KeywordStatus.UNIQUE, ONE_INTEGER }, { "other", KeywordStatus.UNBOUNDED, null } },
                { { "pl", null }, { "one", KeywordStatus.UNIQUE, ONE_INTEGER }, { "few", KeywordStatus.UNBOUNDED, null },
                        { "many", KeywordStatus.UNBOUNDED, null },
                        { "other", KeywordStatus.SUPPRESSED, null, KeywordStatus.UNBOUNDED, null } // note that it is
                                                                                                   // suppressed in
                                                                                                   // INTEGER but not
                                                                                                   // DECIMAL
                }, { { "en", new HashSet<>(Arrays.asList(ONE_INTEGER)) }, // check that 1 is suppressed
                        { "one", KeywordStatus.SUPPRESSED, null }, { "other", KeywordStatus.UNBOUNDED, null } }, };
        Output<DecimalQuantity> uniqueValue = new Output<>();
        for (Object[][] test : tests) {
            ULocale locale = new ULocale((String) test[0][0]);
            // NumberType numberType = (NumberType) test[1];
            Set<DecimalQuantity> explicits = (Set<DecimalQuantity>) test[0][1];
            PluralRules pluralRules = factory.forLocale(locale);
            LinkedHashSet<String> remaining = new LinkedHashSet(possibleKeywords);
            for (int i = 1; i < test.length; ++i) {
                Object[] row = test[i];
                String keyword = (String) row[0];
                KeywordStatus statusExpected = (KeywordStatus) row[1];
                DecimalQuantity uniqueExpected = (DecimalQuantity) row[2];
                remaining.remove(keyword);
                KeywordStatus status = pluralRules.getKeywordStatus(keyword, 0, explicits, uniqueValue);
                assertEquals(getAssertMessage("Unique Value", locale, pluralRules, keyword), uniqueExpected,
                        uniqueValue.value);
                assertEquals(getAssertMessage("Keyword Status", locale, pluralRules, keyword), statusExpected, status);
                if (row.length > 3) {
                    statusExpected = (KeywordStatus) row[3];
                    uniqueExpected = (DecimalQuantity) row[4];
                    status = pluralRules.getKeywordStatus(keyword, 0, explicits, uniqueValue, SampleType.DECIMAL);
                    assertEquals(getAssertMessage("Unique Value - decimal", locale, pluralRules, keyword),
                            uniqueExpected, uniqueValue.value);
                    assertEquals(getAssertMessage("Keyword Status - decimal", locale, pluralRules, keyword),
                            statusExpected, status);
                }
            }
            for (String keyword : remaining) {
                KeywordStatus status = pluralRules.getKeywordStatus(keyword, 0, null, uniqueValue);
                assertEquals("Invalid keyword " + keyword, status, KeywordStatus.INVALID);
                assertNull("Invalid keyword " + keyword, uniqueValue.value);
            }
        }
    }

    // For the time being, the compact notation exponent operand `c` is an alias
    // for the scientific exponent operand `e` and compact notation.
    /**
     * Test the proper plural rule keyword selection given an input number that is
     * already formatted into scientific notation. This exercises the {@code e} plural operand
     * for the formatted number.
     */
    @Test
    public void testScientificPluralKeyword() {
        PluralRules rules = PluralRules.createRules("one: i = 0,1 @integer 0, 1 @decimal 0.0~1.5;  many: e = 0 and i % 1000000 = 0 and v = 0 or " +
                "e != 0 .. 5;  other:  @integer 2~17, 100, 1000, 10000, 100000, 1000000, @decimal 2.0~3.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, …");
        ULocale locale = new ULocale("fr-FR");

        Object[][] casesData = {
                // unlocalized formatter skeleton, input, string output, plural rule keyword
                {"",           0, "0", "one"},
                {"scientific", 0, "0", "one"},

                {"",           1, "1", "one"},
                {"scientific", 1, "1", "one"},

                {"",           2, "2", "other"},
                {"scientific", 2, "2", "other"},

                {"",           1000000, "1 000 000", "many"},
                {"scientific", 1000000, "1 million", "many"},

                {"",           1000001, "1 000 001", "other"},
                {"scientific", 1000001, "1 million", "many"},

                {"",           120000, "1 200 000", "other"},
                {"scientific", 1200000, "1,2 millions", "many"},

                {"",           1200001, "1 200 001", "other"},
                {"scientific", 1200001, "1,2 millions", "many"},

                {"",           2000000, "2 000 000", "many"},
                {"scientific", 2000000, "2 millions", "many"},
        };

        for (Object[] caseDatum : casesData) {
            String skeleton = (String) caseDatum[0];
            int input = (int) caseDatum[1];
            // String expectedString = (String) caseDatum[2];
            String expectPluralRuleKeyword = (String) caseDatum[3];

            String actualPluralRuleKeyword =
                    getPluralKeyword(rules, locale, input, skeleton);

            assertEquals(
                    String.format("PluralRules select %s: %d", skeleton, input),
                    expectPluralRuleKeyword,
                    actualPluralRuleKeyword);
        }
    }

    /**
     * Test the proper plural rule keyword selection given an input number that is
     * already formatted into compact notation. This exercises the {@code c} plural operand
     * for the formatted number.
     */
    @Test
    public void testCompactDecimalPluralKeyword() {
        PluralRules rules = PluralRules.createRules("one: i = 0,1 @integer 0, 1 @decimal 0.0~1.5;  many: c = 0 and i % 1000000 = 0 and v = 0 or " +
                "c != 0 .. 5;  other:  @integer 2~17, 100, 1000, 10000, 100000, 1000000, @decimal 2.0~3.5, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, …");
        ULocale locale = new ULocale("fr-FR");

        Object[][] casesData = {
                // unlocalized formatter skeleton, input, string output, plural rule keyword
                {"",             0, "0", "one"},
                {"compact-long", 0, "0", "one"},

                {"",             1, "1", "one"},
                {"compact-long", 1, "1", "one"},

                {"",             2, "2", "other"},
                {"compact-long", 2, "2", "other"},

                {"",             1000000, "1 000 000", "many"},
                {"compact-long", 1000000, "1 million", "many"},

                {"",             1000001, "1 000 001", "other"},
                {"compact-long", 1000001, "1 million", "many"},

                {"",             120000, "1 200 000", "other"},
                {"compact-long", 1200000, "1,2 millions", "many"},

                {"",             1200001, "1 200 001", "other"},
                {"compact-long", 1200001, "1,2 millions", "many"},

                {"",             2000000, "2 000 000", "many"},
                {"compact-long", 2000000, "2 millions", "many"},
        };

        for (Object[] caseDatum : casesData) {
            String skeleton = (String) caseDatum[0];
            int input = (int) caseDatum[1];
            // String expectedString = (String) caseDatum[2];
            String expectPluralRuleKeyword = (String) caseDatum[3];

            String actualPluralRuleKeyword =
                    getPluralKeyword(rules, locale, input, skeleton);

            assertEquals(
                    String.format("PluralRules select %s: %d", skeleton, input),
                    expectPluralRuleKeyword,
                    actualPluralRuleKeyword);
        }
    }

    private String getPluralKeyword(PluralRules rules, ULocale locale, double number, String skeleton) {
        LocalizedNumberFormatter formatter =
                NumberFormatter.forSkeleton(skeleton)
                    .locale(locale);
        FormattedNumber fn = formatter.format(number);
        String pluralKeyword = rules.select(fn);
        return pluralKeyword;
    }

    @Test
    public void testDoubleValue() {
        Object[][] intCasesData = {
                // source number, expected double value
                {-101, -101.0},
                {-100, -100.0},
                {-1,   -1.0},
                {0,     0.0},
                {1,     1.0},
                {100,   100.0}
        };

        for (Object[] caseDatum : intCasesData) {
            double inputNum = (int) caseDatum[0];
            double expVal = (double) caseDatum[1];
            FixedDecimal fd = new FixedDecimal(inputNum);
            assertEquals("FixedDecimal.doubleValue() for " + inputNum, expVal, fd.doubleValue());
        }

        Object[][] doubleCasesData = {
                // source number, expected double value
                {-0.0,     -0.0},
                {0.1,       0.1},
                {1.999,     1.999},
                {2.0,       2.0},
                {100.001, 100.001}
        };

        for (Object[] caseDatum : doubleCasesData) {
            double inputNum = (double) caseDatum[0];
            double expVal = (double) caseDatum[1];
            FixedDecimal fd = new FixedDecimal(inputNum);
            assertEquals("FixedDecimal.doubleValue() for " + inputNum, expVal, fd.doubleValue());
        }
    }

    @Test
    public void testLongValue() {
        Object[][] intCasesData = {
                // source number, expected double value
                {-101,  101},
                {-100,  100},
                {-1,    1},
                {0,     0},
                {1,     1},
                {100,   100}
        };

        for (Object[] caseDatum : intCasesData) {
            long inputNum = (int) caseDatum[0];
            long expVal = (int) caseDatum[1];
            FixedDecimal fd = new FixedDecimal(inputNum);
            assertEquals("FixedDecimal.longValue() for " + inputNum, expVal, fd.longValue());
        }

        Object[][] doubleCasesData = {
                // source number, expected double value
                {-0.0,      0},
                {0.1,       0},
                {1.999,     1},
                {2.0,       2},
                {100.001,   100}
        };

        for (Object[] caseDatum : doubleCasesData) {
            double inputNum = (double) caseDatum[0];
            long expVal = (int) caseDatum[1];
            FixedDecimal fd = new FixedDecimal(inputNum);
            assertEquals("FixedDecimal.longValue() for " + inputNum, expVal, fd.longValue());
        }
    }

    enum StandardPluralCategories {
        zero, one, two, few, many, other;
        /**
         *
         */
        private static final Set<StandardPluralCategories> ALL = Collections.unmodifiableSet(EnumSet
                .allOf(StandardPluralCategories.class));

        /**
         * Return a mutable set
         *
         * @param source
         * @return
         */
        static final EnumSet<StandardPluralCategories> getSet(Collection<String> source) {
            EnumSet<StandardPluralCategories> result = EnumSet.noneOf(StandardPluralCategories.class);
            for (String s : source) {
                result.add(StandardPluralCategories.valueOf(s));
            }
            return result;
        }

        static final Comparator<Set<StandardPluralCategories>> SHORTEST_FIRST = new Comparator<Set<StandardPluralCategories>>() {
            @Override
            public int compare(Set<StandardPluralCategories> arg0, Set<StandardPluralCategories> arg1) {
                int diff = arg0.size() - arg1.size();
                if (diff != 0) {
                    return diff;
                }
                // otherwise first...
                // could be optimized, but we don't care here.
                for (StandardPluralCategories value : ALL) {
                    if (arg0.contains(value)) {
                        if (!arg1.contains(value)) {
                            return 1;
                        }
                    } else if (arg1.contains(value)) {
                        return -1;
                    }

                }
                return 0;
            }

        };
    }

    @Test
    public void TestLocales() {
        // This test will fail when the locale snapshot gets out of sync with the real CLDR data.
        // In that case, temporarily use "if (true)",
        // copy & paste the output into the initializer above,
        // and revert to "if (false)" for normal testing.
        if (false) {
            generateLOCALE_SNAPSHOT();
            return;
        }
        for (String test : LOCALE_SNAPSHOT) {
            test = test.trim();
            String[] parts = test.split("\\s*;\\s*");
            for (String localeString : parts[0].split("\\s*,\\s*")) {
                ULocale locale = new ULocale(localeString);
                if (factory.hasOverride(locale)) {
                    continue; // skip for now
                }
                PluralRules rules = factory.forLocale(locale);
                for (int i = 1; i < parts.length; ++i) {
                    checkCategoriesAndExpected(localeString, parts[i], rules);
                }
            }
        }
    }

    private static final Comparator<PluralRules> PLURAL_RULE_COMPARATOR = new Comparator<PluralRules>() {
        @Override
        public int compare(PluralRules o1, PluralRules o2) {
            return o1.compareTo(o2);
        }
    };

    private static final Comparator<Collection<StandardPluralCategories>> STDPLURALCATEG_COLLECTION_COMPARATOR = (o1, o2) -> {
        int diff = o1.size() - o2.size();
        if (diff != 0) {
            return diff;
        }
        Iterator<StandardPluralCategories> iterator1 = o1.iterator();
        Iterator<StandardPluralCategories> iterator2 = o2.iterator();
        while (true) {
            // We already know they have the same length, we tested if first thing.
            if (!iterator1.hasNext() || !iterator2.hasNext()) {
                // At the end of both iterators, and everything was equal until here.
                return 0;
            }
            diff = iterator1.next().compareTo(iterator2.next());
            if (diff != 0) {
                return diff;
            }
        }
    };

    private void generateLOCALE_SNAPSHOT() {
        Relation<Set<StandardPluralCategories>, PluralRules> setsToRules = Relation.of(
                new TreeMap<>(STDPLURALCATEG_COLLECTION_COMPARATOR), TreeSet.class, PLURAL_RULE_COMPARATOR);
        Relation<PluralRules, ULocale> data = Relation.of(
                new TreeMap<>(PLURAL_RULE_COMPARATOR), TreeSet.class);
        for (ULocale locale : PluralRules.getAvailableULocales()) {
            PluralRules pr = PluralRules.forLocale(locale);
            EnumSet<StandardPluralCategories> set = getCanonicalSet(pr.getKeywords());
            setsToRules.put(set, pr);
            data.put(pr, locale);
        }
        for (Entry<Set<StandardPluralCategories>, Set<PluralRules>> entry1 : setsToRules.keyValuesSet()) {
            Set<StandardPluralCategories> set = entry1.getKey();
            Set<PluralRules> rules = entry1.getValue();
            System.out.println("\n        // " + set);
            for (PluralRules rule : rules) {
                Set<ULocale> locales = data.get(rule);
                String toShow = locales.stream().map(ULocale::toString).collect(Collectors.joining(","));
                System.out.print("        \"" + toShow);
                for (StandardPluralCategories spc : set) {
                    String keyword = spc.toString();
                    DecimalQuantitySamples samples = rule.getDecimalSamples(keyword, SampleType.INTEGER);
                    System.out.print("; " + spc + ": " + samples);
                }
                System.out.println("\",");
            }
        }
    }

    /**
     * @param keywords
     * @return
     */
    private EnumSet<StandardPluralCategories> getCanonicalSet(Set<String> keywords) {
        EnumSet<StandardPluralCategories> result = EnumSet.noneOf(StandardPluralCategories.class);
        for (String s : keywords) {
            result.add(StandardPluralCategories.valueOf(s));
        }
        return result;
    }

    static final String[] LOCALE_SNAPSHOT = {
            // Last regenerated 2022-08-21 as part of CLDR 42 alpha2 integration
            // [other]
            "bm,bo,dz,hnj,id,ig,ii,ja,jbo,jv,kde,kea,km,ko,lkt,lo,ms,my,nqo,osa,root,sah,ses,sg,su,th,to,tpi,vi,wo,yo,yue,zh; other: @integer 0~15, 100, 1000, 10000, 100000, 1000000, …",

            // [one, other]
            "am,as,bn,doi,fa,gu,hi,kn,pcm,zu; one: @integer 0, 1; other: @integer 2~17, 100, 1000, 10000, 100000, 1000000, …",
            "ff,hy,kab; one: @integer 0, 1; other: @integer 2~17, 100, 1000, 10000, 100000, 1000000, …",
            "ast,de,en,et,fi,fy,gl,ia,io,lij,nl,sc,sv,sw,ur,yi; one: @integer 1; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1000000, …",
            "si; one: @integer 0, 1; other: @integer 2~17, 100, 1000, 10000, 100000, 1000000, …",
            "ak,bho,guw,ln,mg,nso,pa,ti,wa; one: @integer 0, 1; other: @integer 2~17, 100, 1000, 10000, 100000, 1000000, …",
            "tzm; one: @integer 0, 1, 11~24; other: @integer 2~10, 100~106, 1000, 10000, 100000, 1000000, …",
            "af,an,asa,az,bal,bem,bez,bg,brx,ce,cgg,chr,ckb,dv,ee,el,eo,eu,fo,fur,gsw,ha,haw,hu,jgo,jmc,ka,kaj,kcg,kk,kkj,kl,ks,ksb,ku,ky,lb,lg,mas,mgo,ml,mn,mr,nah,nb,nd,ne,nn,nnh,no,nr,ny,nyn,om,or,os,pap,ps,rm,rof,rwk,saq,sd,sdh,seh,sn,so,sq,ss,ssy,st,syr,ta,te,teo,tig,tk,tn,tr,ts,ug,uz,ve,vo,vun,wae,xh,xog; one: @integer 1; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1000000, …",
            "da; one: @integer 1; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1000000, …",
            "is; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1000000, …",
            "mk; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1000000, …",
            "ceb,fil; one: @integer 0~3, 5, 7, 8, 10~13, 15, 17, 18, 20, 21, 100, 1000, 10000, 100000, 1000000, …; other: @integer 4, 6, 9, 14, 16, 19, 24, 26, 104, 1004, …",

            // [zero, one, other]
            "lag; zero: @integer 0; one: @integer 1; other: @integer 2~17, 100, 1000, 10000, 100000, 1000000, …",
            "lv,prg; zero: @integer 0, 10~20, 30, 40, 50, 60, 100, 1000, 10000, 100000, 1000000, …; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; other: @integer 2~9, 22~29, 102, 1002, …",
            "ksh; zero: @integer 0; one: @integer 1; other: @integer 2~17, 100, 1000, 10000, 100000, 1000000, …",

            // [one, two, other]
            "he; one: @integer 1; two: @integer 2; other: @integer 0, 3~17, 100, 1000, 10000, 100000, 1000000, …",
            "iu,naq,sat,se,sma,smi,smj,smn,sms; one: @integer 1; two: @integer 2; other: @integer 0, 3~17, 100, 1000, 10000, 100000, 1000000, …",

            // [one, few, other]
            "shi; one: @integer 0, 1; few: @integer 2~10; other: @integer 11~26, 100, 1000, 10000, 100000, 1000000, …",
            "ro; one: @integer 1; few: @integer 0, 2~16, 101, 1001, …; other: @integer 20~35, 100, 1000, 10000, 100000, 1000000, …",
            "bs,hr,sr,sr_Latn; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; few: @integer 2~4, 22~24, 32~34, 42~44, 52~54, 62, 102, 1002, …; other: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …",

            // [one, many, other]
            "fr; one: @integer 0, 1; many: @integer 1000000, 1c6, 2c6, 3c6, 4c6, 5c6, 6c6, …; other: @integer 2~17, 100, 1000, 10000, 100000, 1c3, 2c3, 3c3, 4c3, 5c3, 6c3, …",
            "pt; one: @integer 0, 1; many: @integer 1000000, 1c6, 2c6, 3c6, 4c6, 5c6, 6c6, …; other: @integer 2~17, 100, 1000, 10000, 100000, 1c3, 2c3, 3c3, 4c3, 5c3, 6c3, …",
            "ca,it,pt_PT,scn,vec; one: @integer 1; many: @integer 1000000, 1c6, 2c6, 3c6, 4c6, 5c6, 6c6, …; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1c3, 2c3, 3c3, 4c3, 5c3, 6c3, …",
            "es; one: @integer 1; many: @integer 1000000, 1c6, 2c6, 3c6, 4c6, 5c6, 6c6, …; other: @integer 0, 2~16, 100, 1000, 10000, 100000, 1c3, 2c3, 3c3, 4c3, 5c3, 6c3, …",

            // [one, two, few, other]
            "gd; one: @integer 1, 11; two: @integer 2, 12; few: @integer 3~10, 13~19; other: @integer 0, 20~34, 100, 1000, 10000, 100000, 1000000, …",
            "dsb,hsb; one: @integer 1, 101, 201, 301, 401, 501, 601, 701, 1001, …; two: @integer 2, 102, 202, 302, 402, 502, 602, 702, 1002, …; few: @integer 3, 4, 103, 104, 203, 204, 303, 304, 403, 404, 503, 504, 603, 604, 703, 704, 1003, …; other: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …",
            "sl; one: @integer 1, 101, 201, 301, 401, 501, 601, 701, 1001, …; two: @integer 2, 102, 202, 302, 402, 502, 602, 702, 1002, …; few: @integer 3, 4, 103, 104, 203, 204, 303, 304, 403, 404, 503, 504, 603, 604, 703, 704, 1003, …; other: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …",

            // [one, few, many, other]
            "cs,sk; one: @integer 1; few: @integer 2~4; many: null; other: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …",
            "be; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; few: @integer 2~4, 22~24, 32~34, 42~44, 52~54, 62, 102, 1002, …; many: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …; other: null",
            "lt; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; few: @integer 2~9, 22~29, 102, 1002, …; many: null; other: @integer 0, 10~20, 30, 40, 50, 60, 100, 1000, 10000, 100000, 1000000, …",
            "pl; one: @integer 1; few: @integer 2~4, 22~24, 32~34, 42~44, 52~54, 62, 102, 1002, …; many: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …; other: null",
            "ru,uk; one: @integer 1, 21, 31, 41, 51, 61, 71, 81, 101, 1001, …; few: @integer 2~4, 22~24, 32~34, 42~44, 52~54, 62, 102, 1002, …; many: @integer 0, 5~19, 100, 1000, 10000, 100000, 1000000, …; other: null",

            // [one, two, few, many, other]
            "br; one: @integer 1, 21, 31, 41, 51, 61, 81, 101, 1001, …; two: @integer 2, 22, 32, 42, 52, 62, 82, 102, 1002, …; few: @integer 3, 4, 9, 23, 24, 29, 33, 34, 39, 43, 44, 49, 103, 1003, …; many: @integer 1000000, …; other: @integer 0, 5~8, 10~20, 100, 1000, 10000, 100000, …",
            "mt; one: @integer 1; two: @integer 2; few: @integer 0, 3~10, 103~109, 1003, …; many: @integer 11~19, 111~117, 1011, …; other: @integer 20~35, 100, 1000, 10000, 100000, 1000000, …",
            "ga; one: @integer 1; two: @integer 2; few: @integer 3~6; many: @integer 7~10; other: @integer 0, 11~25, 100, 1000, 10000, 100000, 1000000, …",
            "gv; one: @integer 1, 11, 21, 31, 41, 51, 61, 71, 101, 1001, …; two: @integer 2, 12, 22, 32, 42, 52, 62, 72, 102, 1002, …; few: @integer 0, 20, 40, 60, 80, 100, 120, 140, 1000, 10000, 100000, 1000000, …; many: null; other: @integer 3~10, 13~19, 23, 103, 1003, …",

            // [zero, one, two, few, many, other]
            "kw; zero: @integer 0; one: @integer 1; two: @integer 2, 22, 42, 62, 82, 102, 122, 142, 1000, 10000, 100000, …; few: @integer 3, 23, 43, 63, 83, 103, 123, 143, 1003, …; many: @integer 21, 41, 61, 81, 101, 121, 141, 161, 1001, …; other: @integer 4~19, 100, 1004, 1000000, …",
            "ar,ars; zero: @integer 0; one: @integer 1; two: @integer 2; few: @integer 3~10, 103~110, 1003, …; many: @integer 11~26, 111, 1011, …; other: @integer 100~102, 200~202, 300~302, 400~402, 500~502, 600, 1000, 10000, 100000, 1000000, …",
            "cy; zero: @integer 0; one: @integer 1; two: @integer 2; few: @integer 3; many: @integer 6; other: @integer 4, 5, 7~20, 100, 1000, 10000, 100000, 1000000, …", };

    private <T extends Serializable> T serializeAndDeserialize(T original, Output<Integer> size) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ostream = new ObjectOutputStream(baos);
            ostream.writeObject(original);
            ostream.flush();
            byte bytes[] = baos.toByteArray();
            size.value = bytes.length;
            ObjectInputStream istream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            T reconstituted = (T) istream.readObject();
            return reconstituted;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestSerialization() {
        Output<Integer> size = new Output<>();
        int max = 0;
        for (ULocale locale : PluralRules.getAvailableULocales()) {
            PluralRules item = PluralRules.forLocale(locale);
            PluralRules item2 = serializeAndDeserialize(item, size);
            logln(locale + "\tsize:\t" + size.value);
            max = Math.max(max, size.value);
            if (!assertEquals(locale + "\tPlural rules before and after serialization", item, item2)) {
                // for debugging
                PluralRules item3 = serializeAndDeserialize(item, size);
                item.equals(item3);
            }
        }
        logln("max \tsize:\t" + max);
    }

    public static class FixedDecimalHandler implements SerializableTestUtility.Handler {
        @Override
        public Object[] getTestObjects() {
            FixedDecimal items[] = { new FixedDecimal(3d), new FixedDecimal(3d, 2), new FixedDecimal(3.1d, 1),
                    new FixedDecimal(3.1d, 2), };
            return items;
        }

        @Override
        public boolean hasSameBehavior(Object a, Object b) {
            FixedDecimal a1 = (FixedDecimal) a;
            FixedDecimal b1 = (FixedDecimal) b;
            return a1.equals(b1);
        }
    }

    @Test
    public void TestSerial() {
        PluralRules s = PluralRules.forLocale(ULocale.ENGLISH);
        checkStreamingEquality(s);
    }

    public void checkStreamingEquality(PluralRules s) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOut);
            objectOutputStream.writeObject(s);
            objectOutputStream.close();
            byte[] contents = byteOut.toByteArray();
            logln(s.getClass() + ": " + showBytes(contents));
            ByteArrayInputStream byteIn = new ByteArrayInputStream(contents);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteIn);
            Object obj = objectInputStream.readObject();
            assertEquals("Streamed Object equals ", s, obj);
        } catch (Exception e) {
            assertNull("TestSerial", e);
        }
    }

    /**
     * @param contents
     * @return
     */
    private String showBytes(byte[] contents) {
        StringBuilder b = new StringBuilder("[");
        for (int i = 0; i < contents.length; ++i) {
            int item = contents[i] & 0xFF;
            if (item >= 0x20 && item <= 0x7F) {
                b.append((char) item);
            } else {
                b.append('(').append(Utility.hex(item, 2)).append(')');
            }
        }
        return b.append(']').toString();
    }

    @Test
    public void testJavaLocaleFactory() {
        PluralRules rulesU0 = PluralRules.forLocale(ULocale.FRANCE);
        PluralRules rulesJ0 = PluralRules.forLocale(Locale.FRANCE);
        assertEquals("forLocale()", rulesU0, rulesJ0);

        PluralRules rulesU1 = PluralRules.forLocale(ULocale.FRANCE, PluralType.ORDINAL);
        PluralRules rulesJ1 = PluralRules.forLocale(Locale.FRANCE, PluralType.ORDINAL);
        assertEquals("forLocale() with type", rulesU1, rulesJ1);
    }

    @Test
    public void testBug20264() {
        Locale startLocale = Locale.getDefault();

        String expected = "1.23400";
        FixedDecimal fd = new FixedDecimal(1.234, 5, 2);
        assertEquals("FixedDecimal toString", expected, fd.toString());
        Locale.setDefault(Locale.FRENCH);
        assertEquals("FixedDecimal toString", expected, fd.toString());
        Locale.setDefault(Locale.GERMAN);
        assertEquals("FixedDecimal toString", expected, fd.toString());

        Locale.setDefault(startLocale);
    }

    @Test
    public void testSelectRange() {
        int d1 = 102;
        int d2 = 201;
        ULocale locale = new ULocale("sl");

        // Locale sl has interesting data: one + two => few
        FormattedNumberRange range = NumberRangeFormatter.withLocale(locale).formatRange(d1, d2);
        PluralRules rules = PluralRules.forLocale(locale);

        // For testing: get plural form of first and second numbers
        FormattedNumber a = NumberFormatter.withLocale(locale).format(d1);
        FormattedNumber b = NumberFormatter.withLocale(locale).format(d2);
        assertEquals("First plural", "two", rules.select(a));
        assertEquals("Second plural", "one", rules.select(b));

        // Check the range plural now:
        String form = rules.select(range);
        assertEquals("Range plural", "few", form);

        // Test when plural ranges data is unavailable:
        PluralRules bare = PluralRules.createRules("a: i = 0,1");
        try {
            form = bare.select(range);
            fail("Expected exception");
        } catch (UnsupportedOperationException e) {}

        // However, they should not throw when no data is available for a language.
        PluralRules xyz = PluralRules.forLocale(new ULocale("xyz"));
        form = xyz.select(range);
        assertEquals("Fallback form", "other", form);
    }
    @Test
    public void test22638LongNumberValue() {
        PluralRules test = PluralRules.createRules(
            "g:c%4422322222232222222222232222222322222223222222232222222322222223" +
            "2222222322222232222222322222223222232222222222222322222223222222");
        assertEquals("Long number value should get null", null, test);
    }
}
