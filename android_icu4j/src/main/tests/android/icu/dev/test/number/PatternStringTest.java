/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
package android.icu.dev.test.number;

import org.junit.Test;

import android.icu.dev.test.CoreTestFmwk;
import android.icu.dev.test.format.FormattedValueTest;
import android.icu.impl.number.DecimalFormatProperties;
import android.icu.impl.number.MacroProps;
import android.icu.impl.number.PatternStringParser;
import android.icu.impl.number.PatternStringParser.ParsedPatternInfo;
import android.icu.impl.number.PatternStringUtils;
import android.icu.number.FormattedNumber;
import android.icu.number.LocalizedNumberFormatter;
import android.icu.number.NumberFormatter;
import android.icu.text.DecimalFormatSymbols;
import android.icu.util.Currency;
import android.icu.util.ULocale;
import android.icu.testsharding.MainTestShard;

/** @author sffc */
@MainTestShard
public class PatternStringTest extends CoreTestFmwk {

    @Test
    public void testLocalized() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(ULocale.ENGLISH);
        symbols.setDecimalSeparatorString("a");
        symbols.setPercentString("b");
        symbols.setMinusSignString(".");
        symbols.setPlusSignString("'");

        String standard = "+-abcb''a''#,##0.0%'a%'";
        String localized = "’.'ab'c'b''a'''#,##0a0b'a%'";
        String toStandard = "+-'ab'c'b''a'''#,##0.0%'a%'";

        assertEquals("Localized decimal format symbols", localized, PatternStringUtils.convertLocalized(standard, symbols, true));
        assertEquals("Standard (unlocalized) decimal format symbols", toStandard, PatternStringUtils.convertLocalized(localized, symbols, false));
    }

    @Test
    public void testToPatternSimple() {
        String[][] cases = {
                { "#", "0" },
                { "0", "0" },
                { "#0", "0" },
                { "###", "0" },
                { "0.##", "0.##" },
                { "0.00", "0.00" },
                { "0.00#", "0.00#" },
                { "0.05", "0.05" },
                { "#E0", "#E0" },
                { "0E0", "0E0" },
                { "#00E00", "#00E00" },
                { "#,##0", "#,##0" },
                { "0¤", "0¤"},
                { "0¤a", "0¤a"},
                { "0¤00", "0¤00"},
                { "#;#", "0;0" },
                { "#;-#", "0" }, // ignore a negative prefix pattern of '-' since that is the default
                { "pp#,000;(#)", "pp#,000;(#,000)" },
                { "**##0", "**##0" },
                { "*'x'##0", "*x##0" },
                { "a''b0", "a''b0" },
                { "*''##0", "*''##0" },
                { "*📺##0", "*'📺'##0" },
                { "*'நி'##0", "*'நி'##0" }, };

        for (String[] cas : cases) {
            String input = cas[0];
            String output = cas[1];

            DecimalFormatProperties properties = PatternStringParser.parseToProperties(input);
            String actual = PatternStringUtils.propertiesToPatternString(properties);
            assertEquals("Failed on input pattern '" + input + "', properties " + properties,
                    output,
                    actual);
        }
    }

    @Test
    public void testToPatternWithProperties() {
        Object[][] cases = {
                { new DecimalFormatProperties().setPositivePrefix("abc"), "abc#;-#" },
                { new DecimalFormatProperties().setPositiveSuffix("abc"), "#abc;-#" },
                { new DecimalFormatProperties().setPositivePrefixPattern("abc"), "abc#" },
                { new DecimalFormatProperties().setPositiveSuffixPattern("abc"), "#abc" },
                { new DecimalFormatProperties().setNegativePrefix("abc"), "#;abc#" },
                { new DecimalFormatProperties().setNegativeSuffix("abc"), "#;-#abc" },
                { new DecimalFormatProperties().setNegativePrefixPattern("abc"), "#;abc#" },
                { new DecimalFormatProperties().setNegativeSuffixPattern("abc"), "#;-#abc" },
                { new DecimalFormatProperties().setPositivePrefix("+"), "'+'#;-#" },
                { new DecimalFormatProperties().setPositivePrefixPattern("+"), "+#" },
                { new DecimalFormatProperties().setPositivePrefix("+'"), "'+'''#;-#" },
                { new DecimalFormatProperties().setPositivePrefix("'+"), "'''+'#;-#" },
                { new DecimalFormatProperties().setPositivePrefix("'"), "''#;-#" },
                { new DecimalFormatProperties().setPositivePrefixPattern("+''"), "+''#" }, };

        for (Object[] cas : cases) {
            DecimalFormatProperties input = (DecimalFormatProperties) cas[0];
            String output = (String) cas[1];

            String actual = PatternStringUtils.propertiesToPatternString(input);
            assertEquals("Failed on input properties " + input, output, actual);
        }
    }

    @Test
    public void testExceptionOnInvalid() {
        String[] invalidPatterns = {
                "#.#.#",
                "0#",
                "0#.",
                ".#0",
                "0#.#0",
                "@0",
                "0@",
                "0,",
                "0,,",
                "0,,0",
                "0,,0,",
                "#,##0E0" };

        for (String pattern : invalidPatterns) {
            try {
                PatternStringParser.parseToProperties(pattern);
                fail("Didn't throw IllegalArgumentException when parsing pattern: " + pattern);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testBug13117() {
        DecimalFormatProperties expected = PatternStringParser.parseToProperties("0");
        DecimalFormatProperties actual = PatternStringParser.parseToProperties("0;");
        assertEquals("Should not consume negative subpattern", expected, actual);
    }

    @Test
    public void testCurrencyDecimal() {
        // Manually create a NumberFormatter from a specific pattern
        ParsedPatternInfo patternInfo = PatternStringParser.parseToPatternInfo("a0¤00b");
        MacroProps macros = new MacroProps();
        macros.unit = Currency.getInstance("EUR");
        macros.affixProvider = patternInfo;
        LocalizedNumberFormatter nf = NumberFormatter.with().macros(macros).locale(ULocale.ROOT);
    
        // Test that the output is as expected
        FormattedNumber fn = nf.format(3.14);
        assertEquals("Should substitute currency symbol", "a3€14b", fn.toString());
    
        // Test field positions
        Object[][] expectedFieldPositions = new Object[][] {
                {android.icu.text.NumberFormat.Field.INTEGER, 1, 2},
                {android.icu.text.NumberFormat.Field.CURRENCY, 2, 3},
                {android.icu.text.NumberFormat.Field.FRACTION, 3, 5}};
        FormattedValueTest.checkFormattedValue(
            "Currency as decimal basic field positions",
            fn,
            "a3€14b",
            expectedFieldPositions
        );
    }
}
