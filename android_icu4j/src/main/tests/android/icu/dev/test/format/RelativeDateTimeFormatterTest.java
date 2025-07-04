/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
/*
 *******************************************************************************
 * Copyright (C) 2013-2016, International Business Machines Corporation and
 * others. All Rights Reserved.
 *******************************************************************************
 */
package android.icu.dev.test.format;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.icu.dev.test.CoreTestFmwk;
import android.icu.math.BigDecimal;
import android.icu.text.DisplayContext;
import android.icu.text.NumberFormat;
import android.icu.text.RelativeDateTimeFormatter;
import android.icu.text.RelativeDateTimeFormatter.AbsoluteUnit;
import android.icu.text.RelativeDateTimeFormatter.Direction;
import android.icu.text.RelativeDateTimeFormatter.FormattedRelativeDateTime;
import android.icu.text.RelativeDateTimeFormatter.RelativeDateTimeUnit;
import android.icu.text.RelativeDateTimeFormatter.RelativeUnit;
import android.icu.text.RelativeDateTimeFormatter.Style;
import android.icu.text.RuleBasedNumberFormat;
import android.icu.util.ULocale;
import android.icu.testsharding.MainTestShard;

@MainTestShard
@RunWith(JUnit4.class)
public class RelativeDateTimeFormatterTest extends CoreTestFmwk {
    @Test
    public void TestRelativeDateWithQuantity() {
        Object[][] data = {
                {0.0, Direction.NEXT, RelativeUnit.SECONDS, "in 0 seconds"},
                {0.5, Direction.NEXT, RelativeUnit.SECONDS, "in 0.5 seconds"},
                {1.0, Direction.NEXT, RelativeUnit.SECONDS, "in 1 second"},
                {2.0, Direction.NEXT, RelativeUnit.SECONDS, "in 2 seconds"},
                {0.0, Direction.NEXT, RelativeUnit.MINUTES, "in 0 minutes"},
                {0.5, Direction.NEXT, RelativeUnit.MINUTES, "in 0.5 minutes"},
                {1.0, Direction.NEXT, RelativeUnit.MINUTES, "in 1 minute"},
                {2.0, Direction.NEXT, RelativeUnit.MINUTES, "in 2 minutes"},
                {0.0, Direction.NEXT, RelativeUnit.HOURS, "in 0 hours"},
                {0.5, Direction.NEXT, RelativeUnit.HOURS, "in 0.5 hours"},
                {1.0, Direction.NEXT, RelativeUnit.HOURS, "in 1 hour"},
                {2.0, Direction.NEXT, RelativeUnit.HOURS, "in 2 hours"},
                {0.0, Direction.NEXT, RelativeUnit.DAYS, "in 0 days"},
                {0.5, Direction.NEXT, RelativeUnit.DAYS, "in 0.5 days"},
                {1.0, Direction.NEXT, RelativeUnit.DAYS, "in 1 day"},
                {2.0, Direction.NEXT, RelativeUnit.DAYS, "in 2 days"},
                {0.0, Direction.NEXT, RelativeUnit.WEEKS, "in 0 weeks"},
                {0.5, Direction.NEXT, RelativeUnit.WEEKS, "in 0.5 weeks"},
                {1.0, Direction.NEXT, RelativeUnit.WEEKS, "in 1 week"},
                {2.0, Direction.NEXT, RelativeUnit.WEEKS, "in 2 weeks"},
                {0.0, Direction.NEXT, RelativeUnit.MONTHS, "in 0 months"},
                {0.5, Direction.NEXT, RelativeUnit.MONTHS, "in 0.5 months"},
                {1.0, Direction.NEXT, RelativeUnit.MONTHS, "in 1 month"},
                {2.0, Direction.NEXT, RelativeUnit.MONTHS, "in 2 months"},
                {0.0, Direction.NEXT, RelativeUnit.YEARS, "in 0 years"},
                {0.5, Direction.NEXT, RelativeUnit.YEARS, "in 0.5 years"},
                {1.0, Direction.NEXT, RelativeUnit.YEARS, "in 1 year"},
                {2.0, Direction.NEXT, RelativeUnit.YEARS, "in 2 years"},
                {0.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 0 quarters"},
                {0.5, Direction.NEXT, RelativeUnit.QUARTERS, "in 0.5 quarters"},
                {1.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 1 quarter"},
                {2.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 2 quarters"},
                {0.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 0 Sundays"},
                {0.5, Direction.NEXT, RelativeUnit.SUNDAYS, "in 0.5 Sundays"},
                {1.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 1 Sunday"},
                {2.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 2 Sundays"},
                {0.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 0 Mondays"},
                {0.5, Direction.NEXT, RelativeUnit.MONDAYS, "in 0.5 Mondays"},
                {1.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 1 Monday"},
                {2.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 2 Mondays"},
                {0.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 0 Tuesdays"},
                {0.5, Direction.NEXT, RelativeUnit.TUESDAYS, "in 0.5 Tuesdays"},
                {1.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 1 Tuesday"},
                {2.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 2 Tuesdays"},
                {0.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 0 Wednesdays"},
                {0.5, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 0.5 Wednesdays"},
                {1.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 1 Wednesday"},
                {2.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 2 Wednesdays"},
                {0.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 0 Thursdays"},
                {0.5, Direction.NEXT, RelativeUnit.THURSDAYS, "in 0.5 Thursdays"},
                {1.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 1 Thursday"},
                {2.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 2 Thursdays"},
                {0.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 0 Fridays"},
                {0.5, Direction.NEXT, RelativeUnit.FRIDAYS, "in 0.5 Fridays"},
                {1.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 1 Friday"},
                {2.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 2 Fridays"},
                {0.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 0 Saturdays"},
                {0.5, Direction.NEXT, RelativeUnit.SATURDAYS, "in 0.5 Saturdays"},
                {1.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 1 Saturday"},
                {2.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 2 Saturdays"},

                {0.0, Direction.LAST, RelativeUnit.SECONDS, "0 seconds ago"},
                {0.5, Direction.LAST, RelativeUnit.SECONDS, "0.5 seconds ago"},
                {1.0, Direction.LAST, RelativeUnit.SECONDS, "1 second ago"},
                {2.0, Direction.LAST, RelativeUnit.SECONDS, "2 seconds ago"},
                {0.0, Direction.LAST, RelativeUnit.MINUTES, "0 minutes ago"},
                {0.5, Direction.LAST, RelativeUnit.MINUTES, "0.5 minutes ago"},
                {1.0, Direction.LAST, RelativeUnit.MINUTES, "1 minute ago"},
                {2.0, Direction.LAST, RelativeUnit.MINUTES, "2 minutes ago"},
                {0.0, Direction.LAST, RelativeUnit.HOURS, "0 hours ago"},
                {0.5, Direction.LAST, RelativeUnit.HOURS, "0.5 hours ago"},
                {1.0, Direction.LAST, RelativeUnit.HOURS, "1 hour ago"},
                {2.0, Direction.LAST, RelativeUnit.HOURS, "2 hours ago"},
                {0.0, Direction.LAST, RelativeUnit.DAYS, "0 days ago"},
                {0.5, Direction.LAST, RelativeUnit.DAYS, "0.5 days ago"},
                {1.0, Direction.LAST, RelativeUnit.DAYS, "1 day ago"},
                {2.0, Direction.LAST, RelativeUnit.DAYS, "2 days ago"},
                {0.0, Direction.LAST, RelativeUnit.WEEKS, "0 weeks ago"},
                {0.5, Direction.LAST, RelativeUnit.WEEKS, "0.5 weeks ago"},
                {1.0, Direction.LAST, RelativeUnit.WEEKS, "1 week ago"},
                {2.0, Direction.LAST, RelativeUnit.WEEKS, "2 weeks ago"},
                {0.0, Direction.LAST, RelativeUnit.MONTHS, "0 months ago"},
                {0.5, Direction.LAST, RelativeUnit.MONTHS, "0.5 months ago"},
                {1.0, Direction.LAST, RelativeUnit.MONTHS, "1 month ago"},
                {2.0, Direction.LAST, RelativeUnit.MONTHS, "2 months ago"},
                {0.0, Direction.LAST, RelativeUnit.YEARS, "0 years ago"},
                {0.5, Direction.LAST, RelativeUnit.YEARS, "0.5 years ago"},
                {1.0, Direction.LAST, RelativeUnit.YEARS, "1 year ago"},
                {2.0, Direction.LAST, RelativeUnit.YEARS, "2 years ago"},
                {0.0, Direction.LAST, RelativeUnit.QUARTERS, "0 quarters ago"},
                {0.5, Direction.LAST, RelativeUnit.QUARTERS, "0.5 quarters ago"},
                {1.0, Direction.LAST, RelativeUnit.QUARTERS, "1 quarter ago"},
                {2.0, Direction.LAST, RelativeUnit.QUARTERS, "2 quarters ago"},
                {0.0, Direction.LAST, RelativeUnit.SUNDAYS, "0 Sundays ago"},
                {0.5, Direction.LAST, RelativeUnit.SUNDAYS, "0.5 Sundays ago"},
                {1.0, Direction.LAST, RelativeUnit.SUNDAYS, "1 Sunday ago"},
                {2.0, Direction.LAST, RelativeUnit.SUNDAYS, "2 Sundays ago"},
                {0.0, Direction.LAST, RelativeUnit.MONDAYS, "0 Mondays ago"},
                {0.5, Direction.LAST, RelativeUnit.MONDAYS, "0.5 Mondays ago"},
                {1.0, Direction.LAST, RelativeUnit.MONDAYS, "1 Monday ago"},
                {2.0, Direction.LAST, RelativeUnit.MONDAYS, "2 Mondays ago"},
                {0.0, Direction.LAST, RelativeUnit.TUESDAYS, "0 Tuesdays ago"},
                {0.5, Direction.LAST, RelativeUnit.TUESDAYS, "0.5 Tuesdays ago"},
                {1.0, Direction.LAST, RelativeUnit.TUESDAYS, "1 Tuesday ago"},
                {2.0, Direction.LAST, RelativeUnit.TUESDAYS, "2 Tuesdays ago"},
                {0.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "0 Wednesdays ago"},
                {0.5, Direction.LAST, RelativeUnit.WEDNESDAYS, "0.5 Wednesdays ago"},
                {1.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "1 Wednesday ago"},
                {2.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "2 Wednesdays ago"},
                {0.0, Direction.LAST, RelativeUnit.THURSDAYS, "0 Thursdays ago"},
                {0.5, Direction.LAST, RelativeUnit.THURSDAYS, "0.5 Thursdays ago"},
                {1.0, Direction.LAST, RelativeUnit.THURSDAYS, "1 Thursday ago"},
                {2.0, Direction.LAST, RelativeUnit.THURSDAYS, "2 Thursdays ago"},
                {0.0, Direction.LAST, RelativeUnit.FRIDAYS, "0 Fridays ago"},
                {0.5, Direction.LAST, RelativeUnit.FRIDAYS, "0.5 Fridays ago"},
                {1.0, Direction.LAST, RelativeUnit.FRIDAYS, "1 Friday ago"},
                {2.0, Direction.LAST, RelativeUnit.FRIDAYS, "2 Fridays ago"},
                {0.0, Direction.LAST, RelativeUnit.SATURDAYS, "0 Saturdays ago"},
                {0.5, Direction.LAST, RelativeUnit.SATURDAYS, "0.5 Saturdays ago"},
                {1.0, Direction.LAST, RelativeUnit.SATURDAYS, "1 Saturday ago"},
                {2.0, Direction.LAST, RelativeUnit.SATURDAYS, "2 Saturdays ago"},
        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("en_US"));
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity", row[3], actual);
        }
    }

    @Test
    public void TestRelativeDateWithQuantityCaps() {
        Object[][] data = {
                {0.0, Direction.NEXT, RelativeUnit.SECONDS, "In 0 seconds"},
                {0.5, Direction.NEXT, RelativeUnit.SECONDS, "In 0.5 seconds"},

                {1.0, Direction.NEXT, RelativeUnit.SECONDS, "In 1 second"},
                {2.0, Direction.NEXT, RelativeUnit.SECONDS, "In 2 seconds"},
                {0.0, Direction.NEXT, RelativeUnit.MINUTES, "In 0 minutes"},
                {0.5, Direction.NEXT, RelativeUnit.MINUTES, "In 0.5 minutes"},
                {1.0, Direction.NEXT, RelativeUnit.MINUTES, "In 1 minute"},
                {2.0, Direction.NEXT, RelativeUnit.MINUTES, "In 2 minutes"},
                {0.0, Direction.NEXT, RelativeUnit.HOURS, "In 0 hours"},
                {0.5, Direction.NEXT, RelativeUnit.HOURS, "In 0.5 hours"},
                {1.0, Direction.NEXT, RelativeUnit.HOURS, "In 1 hour"},
                {2.0, Direction.NEXT, RelativeUnit.HOURS, "In 2 hours"},
                {0.0, Direction.NEXT, RelativeUnit.DAYS, "In 0 days"},
                {0.5, Direction.NEXT, RelativeUnit.DAYS, "In 0.5 days"},
                {1.0, Direction.NEXT, RelativeUnit.DAYS, "In 1 day"},
                {2.0, Direction.NEXT, RelativeUnit.DAYS, "In 2 days"},
                {0.0, Direction.NEXT, RelativeUnit.WEEKS, "In 0 weeks"},
                {0.5, Direction.NEXT, RelativeUnit.WEEKS, "In 0.5 weeks"},
                {1.0, Direction.NEXT, RelativeUnit.WEEKS, "In 1 week"},
                {2.0, Direction.NEXT, RelativeUnit.WEEKS, "In 2 weeks"},
                {0.0, Direction.NEXT, RelativeUnit.MONTHS, "In 0 months"},
                {0.5, Direction.NEXT, RelativeUnit.MONTHS, "In 0.5 months"},
                {1.0, Direction.NEXT, RelativeUnit.MONTHS, "In 1 month"},
                {2.0, Direction.NEXT, RelativeUnit.MONTHS, "In 2 months"},
                {0.0, Direction.NEXT, RelativeUnit.YEARS, "In 0 years"},
                {0.5, Direction.NEXT, RelativeUnit.YEARS, "In 0.5 years"},
                {1.0, Direction.NEXT, RelativeUnit.YEARS, "In 1 year"},
                {2.0, Direction.NEXT, RelativeUnit.YEARS, "In 2 years"},
                {0.0, Direction.NEXT, RelativeUnit.QUARTERS, "In 0 quarters"},
                {0.5, Direction.NEXT, RelativeUnit.QUARTERS, "In 0.5 quarters"},
                {1.0, Direction.NEXT, RelativeUnit.QUARTERS, "In 1 quarter"},
                {2.0, Direction.NEXT, RelativeUnit.QUARTERS, "In 2 quarters"},
                {0.0, Direction.NEXT, RelativeUnit.SUNDAYS, "In 0 Sundays"},
                {0.5, Direction.NEXT, RelativeUnit.SUNDAYS, "In 0.5 Sundays"},
                {1.0, Direction.NEXT, RelativeUnit.SUNDAYS, "In 1 Sunday"},
                {2.0, Direction.NEXT, RelativeUnit.SUNDAYS, "In 2 Sundays"},
                {0.0, Direction.NEXT, RelativeUnit.MONDAYS, "In 0 Mondays"},
                {0.5, Direction.NEXT, RelativeUnit.MONDAYS, "In 0.5 Mondays"},
                {1.0, Direction.NEXT, RelativeUnit.MONDAYS, "In 1 Monday"},
                {2.0, Direction.NEXT, RelativeUnit.MONDAYS, "In 2 Mondays"},
                {0.0, Direction.NEXT, RelativeUnit.TUESDAYS, "In 0 Tuesdays"},
                {0.5, Direction.NEXT, RelativeUnit.TUESDAYS, "In 0.5 Tuesdays"},
                {1.0, Direction.NEXT, RelativeUnit.TUESDAYS, "In 1 Tuesday"},
                {2.0, Direction.NEXT, RelativeUnit.TUESDAYS, "In 2 Tuesdays"},
                {0.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "In 0 Wednesdays"},
                {0.5, Direction.NEXT, RelativeUnit.WEDNESDAYS, "In 0.5 Wednesdays"},
                {1.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "In 1 Wednesday"},
                {2.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "In 2 Wednesdays"},
                {0.0, Direction.NEXT, RelativeUnit.THURSDAYS, "In 0 Thursdays"},
                {0.5, Direction.NEXT, RelativeUnit.THURSDAYS, "In 0.5 Thursdays"},
                {1.0, Direction.NEXT, RelativeUnit.THURSDAYS, "In 1 Thursday"},
                {2.0, Direction.NEXT, RelativeUnit.THURSDAYS, "In 2 Thursdays"},
                {0.0, Direction.NEXT, RelativeUnit.FRIDAYS, "In 0 Fridays"},
                {0.5, Direction.NEXT, RelativeUnit.FRIDAYS, "In 0.5 Fridays"},
                {1.0, Direction.NEXT, RelativeUnit.FRIDAYS, "In 1 Friday"},
                {2.0, Direction.NEXT, RelativeUnit.FRIDAYS, "In 2 Fridays"},
                {0.0, Direction.NEXT, RelativeUnit.SATURDAYS, "In 0 Saturdays"},
                {0.5, Direction.NEXT, RelativeUnit.SATURDAYS, "In 0.5 Saturdays"},
                {1.0, Direction.NEXT, RelativeUnit.SATURDAYS, "In 1 Saturday"},
                {2.0, Direction.NEXT, RelativeUnit.SATURDAYS, "In 2 Saturdays"},

                {0.0, Direction.LAST, RelativeUnit.SECONDS, "0 seconds ago"},
                {0.5, Direction.LAST, RelativeUnit.SECONDS, "0.5 seconds ago"},
                {1.0, Direction.LAST, RelativeUnit.SECONDS, "1 second ago"},
                {2.0, Direction.LAST, RelativeUnit.SECONDS, "2 seconds ago"},
                {0.0, Direction.LAST, RelativeUnit.MINUTES, "0 minutes ago"},
                {0.5, Direction.LAST, RelativeUnit.MINUTES, "0.5 minutes ago"},
                {1.0, Direction.LAST, RelativeUnit.MINUTES, "1 minute ago"},
                {2.0, Direction.LAST, RelativeUnit.MINUTES, "2 minutes ago"},
                {0.0, Direction.LAST, RelativeUnit.HOURS, "0 hours ago"},
                {0.5, Direction.LAST, RelativeUnit.HOURS, "0.5 hours ago"},
                {1.0, Direction.LAST, RelativeUnit.HOURS, "1 hour ago"},
                {2.0, Direction.LAST, RelativeUnit.HOURS, "2 hours ago"},
                {0.0, Direction.LAST, RelativeUnit.DAYS, "0 days ago"},
                {0.5, Direction.LAST, RelativeUnit.DAYS, "0.5 days ago"},
                {1.0, Direction.LAST, RelativeUnit.DAYS, "1 day ago"},
                {2.0, Direction.LAST, RelativeUnit.DAYS, "2 days ago"},
                {0.0, Direction.LAST, RelativeUnit.WEEKS, "0 weeks ago"},
                {0.5, Direction.LAST, RelativeUnit.WEEKS, "0.5 weeks ago"},
                {1.0, Direction.LAST, RelativeUnit.WEEKS, "1 week ago"},
                {2.0, Direction.LAST, RelativeUnit.WEEKS, "2 weeks ago"},
                {0.0, Direction.LAST, RelativeUnit.MONTHS, "0 months ago"},
                {0.5, Direction.LAST, RelativeUnit.MONTHS, "0.5 months ago"},
                {1.0, Direction.LAST, RelativeUnit.MONTHS, "1 month ago"},
                {2.0, Direction.LAST, RelativeUnit.MONTHS, "2 months ago"},
                {0.0, Direction.LAST, RelativeUnit.YEARS, "0 years ago"},
                {0.5, Direction.LAST, RelativeUnit.YEARS, "0.5 years ago"},
                {1.0, Direction.LAST, RelativeUnit.YEARS, "1 year ago"},
                {2.0, Direction.LAST, RelativeUnit.YEARS, "2 years ago"},
                {0.0, Direction.LAST, RelativeUnit.QUARTERS, "0 quarters ago"},
                {0.5, Direction.LAST, RelativeUnit.QUARTERS, "0.5 quarters ago"},
                {1.0, Direction.LAST, RelativeUnit.QUARTERS, "1 quarter ago"},
                {2.0, Direction.LAST, RelativeUnit.QUARTERS, "2 quarters ago"},
                {0.0, Direction.LAST, RelativeUnit.SUNDAYS, "0 Sundays ago"},
                {0.5, Direction.LAST, RelativeUnit.SUNDAYS, "0.5 Sundays ago"},
                {1.0, Direction.LAST, RelativeUnit.SUNDAYS, "1 Sunday ago"},
                {2.0, Direction.LAST, RelativeUnit.SUNDAYS, "2 Sundays ago"},
                {0.0, Direction.LAST, RelativeUnit.MONDAYS, "0 Mondays ago"},
                {0.5, Direction.LAST, RelativeUnit.MONDAYS, "0.5 Mondays ago"},
                {1.0, Direction.LAST, RelativeUnit.MONDAYS, "1 Monday ago"},
                {2.0, Direction.LAST, RelativeUnit.MONDAYS, "2 Mondays ago"},
                {0.0, Direction.LAST, RelativeUnit.TUESDAYS, "0 Tuesdays ago"},
                {0.5, Direction.LAST, RelativeUnit.TUESDAYS, "0.5 Tuesdays ago"},
                {1.0, Direction.LAST, RelativeUnit.TUESDAYS, "1 Tuesday ago"},
                {2.0, Direction.LAST, RelativeUnit.TUESDAYS, "2 Tuesdays ago"},
                {0.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "0 Wednesdays ago"},
                {0.5, Direction.LAST, RelativeUnit.WEDNESDAYS, "0.5 Wednesdays ago"},
                {1.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "1 Wednesday ago"},
                {2.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "2 Wednesdays ago"},
                {0.0, Direction.LAST, RelativeUnit.THURSDAYS, "0 Thursdays ago"},
                {0.5, Direction.LAST, RelativeUnit.THURSDAYS, "0.5 Thursdays ago"},
                {1.0, Direction.LAST, RelativeUnit.THURSDAYS, "1 Thursday ago"},
                {2.0, Direction.LAST, RelativeUnit.THURSDAYS, "2 Thursdays ago"},
                {0.0, Direction.LAST, RelativeUnit.FRIDAYS, "0 Fridays ago"},
                {0.5, Direction.LAST, RelativeUnit.FRIDAYS, "0.5 Fridays ago"},
                {1.0, Direction.LAST, RelativeUnit.FRIDAYS, "1 Friday ago"},
                {2.0, Direction.LAST, RelativeUnit.FRIDAYS, "2 Fridays ago"},
                {0.0, Direction.LAST, RelativeUnit.SATURDAYS, "0 Saturdays ago"},
                {0.5, Direction.LAST, RelativeUnit.SATURDAYS, "0.5 Saturdays ago"},
                {1.0, Direction.LAST, RelativeUnit.SATURDAYS, "1 Saturday ago"},
                {2.0, Direction.LAST, RelativeUnit.SATURDAYS, "2 Saturdays ago"},

        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.LONG,
                DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE);
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity", row[3], actual);
        }
    }

    @Test
    public void TestRelativeDateWithQuantityShort() {
        Object[][] data = {
                {0.0, Direction.NEXT, RelativeUnit.SECONDS, "in 0 sec."},
                {0.5, Direction.NEXT, RelativeUnit.SECONDS, "in 0.5 sec."},

                {1.0, Direction.NEXT, RelativeUnit.SECONDS, "in 1 sec."},
                {2.0, Direction.NEXT, RelativeUnit.SECONDS, "in 2 sec."},
                {0.0, Direction.NEXT, RelativeUnit.MINUTES, "in 0 min."},
                {0.5, Direction.NEXT, RelativeUnit.MINUTES, "in 0.5 min."},
                {1.0, Direction.NEXT, RelativeUnit.MINUTES, "in 1 min."},
                {2.0, Direction.NEXT, RelativeUnit.MINUTES, "in 2 min."},
                {0.0, Direction.NEXT, RelativeUnit.HOURS, "in 0 hr."},
                {0.5, Direction.NEXT, RelativeUnit.HOURS, "in 0.5 hr."},
                {1.0, Direction.NEXT, RelativeUnit.HOURS, "in 1 hr."},
                {2.0, Direction.NEXT, RelativeUnit.HOURS, "in 2 hr."},
                {0.0, Direction.NEXT, RelativeUnit.DAYS, "in 0 days"},
                {0.5, Direction.NEXT, RelativeUnit.DAYS, "in 0.5 days"},
                {1.0, Direction.NEXT, RelativeUnit.DAYS, "in 1 day"},
                {2.0, Direction.NEXT, RelativeUnit.DAYS, "in 2 days"},
                {0.0, Direction.NEXT, RelativeUnit.WEEKS, "in 0 wk."},
                {0.5, Direction.NEXT, RelativeUnit.WEEKS, "in 0.5 wk."},
                {1.0, Direction.NEXT, RelativeUnit.WEEKS, "in 1 wk."},
                {2.0, Direction.NEXT, RelativeUnit.WEEKS, "in 2 wk."},
                {0.0, Direction.NEXT, RelativeUnit.MONTHS, "in 0 mo."},
                {0.5, Direction.NEXT, RelativeUnit.MONTHS, "in 0.5 mo."},
                {1.0, Direction.NEXT, RelativeUnit.MONTHS, "in 1 mo."},
                {2.0, Direction.NEXT, RelativeUnit.MONTHS, "in 2 mo."},
                {0.0, Direction.NEXT, RelativeUnit.YEARS, "in 0 yr."},
                {0.5, Direction.NEXT, RelativeUnit.YEARS, "in 0.5 yr."},
                {1.0, Direction.NEXT, RelativeUnit.YEARS, "in 1 yr."},
                {2.0, Direction.NEXT, RelativeUnit.YEARS, "in 2 yr."},
                {0.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 0 qtrs."},
                {0.5, Direction.NEXT, RelativeUnit.QUARTERS, "in 0.5 qtrs."},
                {1.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 1 qtr."},
                {2.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 2 qtrs."},
                {0.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 0 Sun."},
                {0.5, Direction.NEXT, RelativeUnit.SUNDAYS, "in 0.5 Sun."},
                {1.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 1 Sun."},
                {2.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 2 Sun."},
                {0.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 0 Mon."},
                {0.5, Direction.NEXT, RelativeUnit.MONDAYS, "in 0.5 Mon."},
                {1.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 1 Mon."},
                {2.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 2 Mon."},
                {0.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 0 Tue."},
                {0.5, Direction.NEXT, RelativeUnit.TUESDAYS, "in 0.5 Tue."},
                {1.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 1 Tue."},
                {2.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 2 Tue."},
                {0.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 0 Wed."},
                {0.5, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 0.5 Wed."},
                {1.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 1 Wed."},
                {2.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 2 Wed."},
                {0.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 0 Thu."},
                {0.5, Direction.NEXT, RelativeUnit.THURSDAYS, "in 0.5 Thu."},
                {1.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 1 Thu."},
                {2.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 2 Thu."},
                {0.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 0 Fri."},
                {0.5, Direction.NEXT, RelativeUnit.FRIDAYS, "in 0.5 Fri."},
                {1.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 1 Fri."},
                {2.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 2 Fri."},
                {0.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 0 Sat."},
                {0.5, Direction.NEXT, RelativeUnit.SATURDAYS, "in 0.5 Sat."},
                {1.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 1 Sat."},
                {2.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 2 Sat."},

                {0.0, Direction.LAST, RelativeUnit.SECONDS, "0 sec. ago"},
                {0.5, Direction.LAST, RelativeUnit.SECONDS, "0.5 sec. ago"},
                {1.0, Direction.LAST, RelativeUnit.SECONDS, "1 sec. ago"},
                {2.0, Direction.LAST, RelativeUnit.SECONDS, "2 sec. ago"},
                {0.0, Direction.LAST, RelativeUnit.MINUTES, "0 min. ago"},
                {0.5, Direction.LAST, RelativeUnit.MINUTES, "0.5 min. ago"},
                {1.0, Direction.LAST, RelativeUnit.MINUTES, "1 min. ago"},
                {2.0, Direction.LAST, RelativeUnit.MINUTES, "2 min. ago"},
                {0.0, Direction.LAST, RelativeUnit.HOURS, "0 hr. ago"},
                {0.5, Direction.LAST, RelativeUnit.HOURS, "0.5 hr. ago"},
                {1.0, Direction.LAST, RelativeUnit.HOURS, "1 hr. ago"},
                {2.0, Direction.LAST, RelativeUnit.HOURS, "2 hr. ago"},
                {0.0, Direction.LAST, RelativeUnit.DAYS, "0 days ago"},
                {0.5, Direction.LAST, RelativeUnit.DAYS, "0.5 days ago"},
                {1.0, Direction.LAST, RelativeUnit.DAYS, "1 day ago"},
                {2.0, Direction.LAST, RelativeUnit.DAYS, "2 days ago"},
                {0.0, Direction.LAST, RelativeUnit.WEEKS, "0 wk. ago"},
                {0.5, Direction.LAST, RelativeUnit.WEEKS, "0.5 wk. ago"},
                {1.0, Direction.LAST, RelativeUnit.WEEKS, "1 wk. ago"},
                {2.0, Direction.LAST, RelativeUnit.WEEKS, "2 wk. ago"},
                {0.0, Direction.LAST, RelativeUnit.MONTHS, "0 mo. ago"},
                {0.5, Direction.LAST, RelativeUnit.MONTHS, "0.5 mo. ago"},
                {1.0, Direction.LAST, RelativeUnit.MONTHS, "1 mo. ago"},
                {2.0, Direction.LAST, RelativeUnit.MONTHS, "2 mo. ago"},
                {0.0, Direction.LAST, RelativeUnit.YEARS, "0 yr. ago"},
                {0.5, Direction.LAST, RelativeUnit.YEARS, "0.5 yr. ago"},
                {1.0, Direction.LAST, RelativeUnit.YEARS, "1 yr. ago"},
                {2.0, Direction.LAST, RelativeUnit.YEARS, "2 yr. ago"},
                {0.0, Direction.LAST, RelativeUnit.QUARTERS, "0 qtrs. ago"},
                {0.5, Direction.LAST, RelativeUnit.QUARTERS, "0.5 qtrs. ago"},
                {1.0, Direction.LAST, RelativeUnit.QUARTERS, "1 qtr. ago"},
                {2.0, Direction.LAST, RelativeUnit.QUARTERS, "2 qtrs. ago"},
                {0.0, Direction.LAST, RelativeUnit.SUNDAYS, "0 Sun. ago"},
                {0.5, Direction.LAST, RelativeUnit.SUNDAYS, "0.5 Sun. ago"},
                {1.0, Direction.LAST, RelativeUnit.SUNDAYS, "1 Sun. ago"},
                {2.0, Direction.LAST, RelativeUnit.SUNDAYS, "2 Sun. ago"},
                {0.0, Direction.LAST, RelativeUnit.MONDAYS, "0 Mon. ago"},
                {0.5, Direction.LAST, RelativeUnit.MONDAYS, "0.5 Mon. ago"},
                {1.0, Direction.LAST, RelativeUnit.MONDAYS, "1 Mon. ago"},
                {2.0, Direction.LAST, RelativeUnit.MONDAYS, "2 Mon. ago"},
                {0.0, Direction.LAST, RelativeUnit.TUESDAYS, "0 Tue. ago"},
                {0.5, Direction.LAST, RelativeUnit.TUESDAYS, "0.5 Tue. ago"},
                {1.0, Direction.LAST, RelativeUnit.TUESDAYS, "1 Tue. ago"},
                {2.0, Direction.LAST, RelativeUnit.TUESDAYS, "2 Tue. ago"},
                {0.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "0 Wed. ago"},
                {0.5, Direction.LAST, RelativeUnit.WEDNESDAYS, "0.5 Wed. ago"},
                {1.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "1 Wed. ago"},
                {2.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "2 Wed. ago"},
                {0.0, Direction.LAST, RelativeUnit.THURSDAYS, "0 Thu. ago"},
                {0.5, Direction.LAST, RelativeUnit.THURSDAYS, "0.5 Thu. ago"},
                {1.0, Direction.LAST, RelativeUnit.THURSDAYS, "1 Thu. ago"},
                {2.0, Direction.LAST, RelativeUnit.THURSDAYS, "2 Thu. ago"},
                {0.0, Direction.LAST, RelativeUnit.FRIDAYS, "0 Fri. ago"},
                {0.5, Direction.LAST, RelativeUnit.FRIDAYS, "0.5 Fri. ago"},
                {1.0, Direction.LAST, RelativeUnit.FRIDAYS, "1 Fri. ago"},
                {2.0, Direction.LAST, RelativeUnit.FRIDAYS, "2 Fri. ago"},
                {0.0, Direction.LAST, RelativeUnit.SATURDAYS, "0 Sat. ago"},
                {0.5, Direction.LAST, RelativeUnit.SATURDAYS, "0.5 Sat. ago"},
                {1.0, Direction.LAST, RelativeUnit.SATURDAYS, "1 Sat. ago"},
                {2.0, Direction.LAST, RelativeUnit.SATURDAYS, "2 Sat. ago"},

        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.SHORT,
                DisplayContext.CAPITALIZATION_NONE);
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity", row[3], actual);
        }
    }

    @Test
    public void TestRelativeDateWithQuantityNarrow() {
        Object[][] data = {
                {0.0, Direction.NEXT, RelativeUnit.SECONDS, "in 0s"},
                {0.5, Direction.NEXT, RelativeUnit.SECONDS, "in 0.5s"},

                {1.0, Direction.NEXT, RelativeUnit.SECONDS, "in 1s"},
                {2.0, Direction.NEXT, RelativeUnit.SECONDS, "in 2s"},
                {0.0, Direction.NEXT, RelativeUnit.MINUTES, "in 0m"},
                {0.5, Direction.NEXT, RelativeUnit.MINUTES, "in 0.5m"},
                {1.0, Direction.NEXT, RelativeUnit.MINUTES, "in 1m"},
                {2.0, Direction.NEXT, RelativeUnit.MINUTES, "in 2m"},
                {0.0, Direction.NEXT, RelativeUnit.HOURS, "in 0h"},
                {0.5, Direction.NEXT, RelativeUnit.HOURS, "in 0.5h"},
                {1.0, Direction.NEXT, RelativeUnit.HOURS, "in 1h"},
                {2.0, Direction.NEXT, RelativeUnit.HOURS, "in 2h"},
                {0.0, Direction.NEXT, RelativeUnit.DAYS, "in 0d"},
                {0.5, Direction.NEXT, RelativeUnit.DAYS, "in 0.5d"},
                {1.0, Direction.NEXT, RelativeUnit.DAYS, "in 1d"},
                {2.0, Direction.NEXT, RelativeUnit.DAYS, "in 2d"},
                {0.0, Direction.NEXT, RelativeUnit.WEEKS, "in 0w"},
                {0.5, Direction.NEXT, RelativeUnit.WEEKS, "in 0.5w"},
                {1.0, Direction.NEXT, RelativeUnit.WEEKS, "in 1w"},
                {2.0, Direction.NEXT, RelativeUnit.WEEKS, "in 2w"},
                {0.0, Direction.NEXT, RelativeUnit.MONTHS, "in 0mo"},
                {0.5, Direction.NEXT, RelativeUnit.MONTHS, "in 0.5mo"},
                {1.0, Direction.NEXT, RelativeUnit.MONTHS, "in 1mo"},
                {2.0, Direction.NEXT, RelativeUnit.MONTHS, "in 2mo"},
                {0.0, Direction.NEXT, RelativeUnit.YEARS, "in 0y"},
                {0.5, Direction.NEXT, RelativeUnit.YEARS, "in 0.5y"},
                {1.0, Direction.NEXT, RelativeUnit.YEARS, "in 1y"},
                {2.0, Direction.NEXT, RelativeUnit.YEARS, "in 2y"},
                {0.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 0q"},
                {0.5, Direction.NEXT, RelativeUnit.QUARTERS, "in 0.5q"},
                {1.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 1q"},
                {2.0, Direction.NEXT, RelativeUnit.QUARTERS, "in 2q"},
                {0.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 0 Su"},
                {0.5, Direction.NEXT, RelativeUnit.SUNDAYS, "in 0.5 Su"},
                {1.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 1 Su"},
                {2.0, Direction.NEXT, RelativeUnit.SUNDAYS, "in 2 Su"},
                {0.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 0 M"},
                {0.5, Direction.NEXT, RelativeUnit.MONDAYS, "in 0.5 M"},
                {1.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 1 M"},
                {2.0, Direction.NEXT, RelativeUnit.MONDAYS, "in 2 M"},
                {0.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 0 Tu"},
                {0.5, Direction.NEXT, RelativeUnit.TUESDAYS, "in 0.5 Tu"},
                {1.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 1 Tu"},
                {2.0, Direction.NEXT, RelativeUnit.TUESDAYS, "in 2 Tu"},
                {0.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 0 W"},
                {0.5, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 0.5 W"},
                {1.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 1 W"},
                {2.0, Direction.NEXT, RelativeUnit.WEDNESDAYS, "in 2 W"},
                {0.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 0 Th"},
                {0.5, Direction.NEXT, RelativeUnit.THURSDAYS, "in 0.5 Th"},
                {1.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 1 Th"},
                {2.0, Direction.NEXT, RelativeUnit.THURSDAYS, "in 2 Th"},
                {0.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 0 F"},
                {0.5, Direction.NEXT, RelativeUnit.FRIDAYS, "in 0.5 F"},
                {1.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 1 F"},
                {2.0, Direction.NEXT, RelativeUnit.FRIDAYS, "in 2 F"},
                {0.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 0 Sa"},
                {0.5, Direction.NEXT, RelativeUnit.SATURDAYS, "in 0.5 Sa"},
                {1.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 1 Sa"},
                {2.0, Direction.NEXT, RelativeUnit.SATURDAYS, "in 2 Sa"},

                {0.0, Direction.LAST, RelativeUnit.SECONDS, "0s ago"},
                {0.5, Direction.LAST, RelativeUnit.SECONDS, "0.5s ago"},
                {1.0, Direction.LAST, RelativeUnit.SECONDS, "1s ago"},
                {2.0, Direction.LAST, RelativeUnit.SECONDS, "2s ago"},
                {0.0, Direction.LAST, RelativeUnit.MINUTES, "0m ago"},
                {0.5, Direction.LAST, RelativeUnit.MINUTES, "0.5m ago"},
                {1.0, Direction.LAST, RelativeUnit.MINUTES, "1m ago"},
                {2.0, Direction.LAST, RelativeUnit.MINUTES, "2m ago"},
                {0.0, Direction.LAST, RelativeUnit.HOURS, "0h ago"},
                {0.5, Direction.LAST, RelativeUnit.HOURS, "0.5h ago"},
                {1.0, Direction.LAST, RelativeUnit.HOURS, "1h ago"},
                {2.0, Direction.LAST, RelativeUnit.HOURS, "2h ago"},
                {0.0, Direction.LAST, RelativeUnit.DAYS, "0d ago"},
                {0.5, Direction.LAST, RelativeUnit.DAYS, "0.5d ago"},
                {1.0, Direction.LAST, RelativeUnit.DAYS, "1d ago"},
                {2.0, Direction.LAST, RelativeUnit.DAYS, "2d ago"},
                {0.0, Direction.LAST, RelativeUnit.WEEKS, "0w ago"},
                {0.5, Direction.LAST, RelativeUnit.WEEKS, "0.5w ago"},
                {1.0, Direction.LAST, RelativeUnit.WEEKS, "1w ago"},
                {2.0, Direction.LAST, RelativeUnit.WEEKS, "2w ago"},
                {0.0, Direction.LAST, RelativeUnit.MONTHS, "0mo ago"},
                {0.5, Direction.LAST, RelativeUnit.MONTHS, "0.5mo ago"},
                {1.0, Direction.LAST, RelativeUnit.MONTHS, "1mo ago"},
                {2.0, Direction.LAST, RelativeUnit.MONTHS, "2mo ago"},
                {0.0, Direction.LAST, RelativeUnit.YEARS, "0y ago"},
                {0.5, Direction.LAST, RelativeUnit.YEARS, "0.5y ago"},
                {1.0, Direction.LAST, RelativeUnit.YEARS, "1y ago"},
                {2.0, Direction.LAST, RelativeUnit.YEARS, "2y ago"},
                {0.0, Direction.LAST, RelativeUnit.QUARTERS, "0q ago"},
                {0.5, Direction.LAST, RelativeUnit.QUARTERS, "0.5q ago"},
                {1.0, Direction.LAST, RelativeUnit.QUARTERS, "1q ago"},
                {2.0, Direction.LAST, RelativeUnit.QUARTERS, "2q ago"},
                {0.0, Direction.LAST, RelativeUnit.SUNDAYS, "0 Su ago"},
                {0.5, Direction.LAST, RelativeUnit.SUNDAYS, "0.5 Su ago"},
                {1.0, Direction.LAST, RelativeUnit.SUNDAYS, "1 Su ago"},
                {2.0, Direction.LAST, RelativeUnit.SUNDAYS, "2 Su ago"},
                {0.0, Direction.LAST, RelativeUnit.MONDAYS, "0 M ago"},
                {0.5, Direction.LAST, RelativeUnit.MONDAYS, "0.5 M ago"},
                {1.0, Direction.LAST, RelativeUnit.MONDAYS, "1 M ago"},
                {2.0, Direction.LAST, RelativeUnit.MONDAYS, "2 M ago"},
                {0.0, Direction.LAST, RelativeUnit.TUESDAYS, "0 Tu ago"},
                {0.5, Direction.LAST, RelativeUnit.TUESDAYS, "0.5 Tu ago"},
                {1.0, Direction.LAST, RelativeUnit.TUESDAYS, "1 Tu ago"},
                {2.0, Direction.LAST, RelativeUnit.TUESDAYS, "2 Tu ago"},
                {0.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "0 W ago"},
                {0.5, Direction.LAST, RelativeUnit.WEDNESDAYS, "0.5 W ago"},
                {1.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "1 W ago"},
                {2.0, Direction.LAST, RelativeUnit.WEDNESDAYS, "2 W ago"},
                {0.0, Direction.LAST, RelativeUnit.THURSDAYS, "0 Th ago"},
                {0.5, Direction.LAST, RelativeUnit.THURSDAYS, "0.5 Th ago"},
                {1.0, Direction.LAST, RelativeUnit.THURSDAYS, "1 Th ago"},
                {2.0, Direction.LAST, RelativeUnit.THURSDAYS, "2 Th ago"},
                {0.0, Direction.LAST, RelativeUnit.FRIDAYS, "0 F ago"},
                {0.5, Direction.LAST, RelativeUnit.FRIDAYS, "0.5 F ago"},
                {1.0, Direction.LAST, RelativeUnit.FRIDAYS, "1 F ago"},
                {2.0, Direction.LAST, RelativeUnit.FRIDAYS, "2 F ago"},
                {0.0, Direction.LAST, RelativeUnit.SATURDAYS, "0 Sa ago"},
                {0.5, Direction.LAST, RelativeUnit.SATURDAYS, "0.5 Sa ago"},
                {1.0, Direction.LAST, RelativeUnit.SATURDAYS, "1 Sa ago"},
                {2.0, Direction.LAST, RelativeUnit.SATURDAYS, "2 Sa ago"},

        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.NARROW,
                DisplayContext.CAPITALIZATION_NONE);
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity", row[3], actual);
        }
    }



    @Test
    public void TestRelativeDateWithQuantitySr() {
        Object[][] data = {
                {0.0, Direction.NEXT, RelativeUnit.MONTHS, "\u0437\u0430 0 \u043C\u0435\u0441\u0435\u0446\u0438"},
                {1.2, Direction.NEXT, RelativeUnit.MONTHS, "\u0437\u0430 1,2 \u043C\u0435\u0441\u0435\u0446\u0430"},
                {21.0, Direction.NEXT, RelativeUnit.MONTHS, "\u0437\u0430 21 \u043C\u0435\u0441\u0435\u0446"},
        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("sr"));
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity", row[3], actual);
        }
    }

    @Test
    public void TestRelativeDateWithQuantitySrFallback() {
        Object[][] data = {
                {0.0, Direction.NEXT, RelativeUnit.MONTHS, "\u0437\u0430 0 \u043C."},
                {1.2, Direction.NEXT, RelativeUnit.MONTHS, "\u0437\u0430 1,2 \u043C."},
                {21.0, Direction.NEXT, RelativeUnit.MONTHS, "\u0437\u0430 21 \u043C."},
        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("sr"),
                null,
                RelativeDateTimeFormatter.Style.NARROW,
                DisplayContext.CAPITALIZATION_NONE);
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity fallback", row[3], actual);
        }
    }

    @Test
    public void TestRelativeDateWithoutQuantity() {
        Object[][] data = {
                {Direction.NEXT_2, AbsoluteUnit.DAY, null},

                {Direction.NEXT, AbsoluteUnit.DAY, "tomorrow"},
                {Direction.NEXT, AbsoluteUnit.WEEK, "next week"},
                {Direction.NEXT, AbsoluteUnit.MONTH, "next month"},
                {Direction.NEXT, AbsoluteUnit.QUARTER, "next quarter"},
                {Direction.NEXT, AbsoluteUnit.YEAR, "next year"},
                {Direction.NEXT, AbsoluteUnit.MONDAY, "next Monday"},
                {Direction.NEXT, AbsoluteUnit.TUESDAY, "next Tuesday"},
                {Direction.NEXT, AbsoluteUnit.WEDNESDAY, "next Wednesday"},
                {Direction.NEXT, AbsoluteUnit.THURSDAY, "next Thursday"},
                {Direction.NEXT, AbsoluteUnit.FRIDAY, "next Friday"},
                {Direction.NEXT, AbsoluteUnit.SATURDAY, "next Saturday"},
                {Direction.NEXT, AbsoluteUnit.SUNDAY, "next Sunday"},

                {Direction.LAST_2, AbsoluteUnit.DAY, null},

                {Direction.LAST, AbsoluteUnit.DAY, "yesterday"},
                {Direction.LAST, AbsoluteUnit.WEEK, "last week"},
                {Direction.LAST, AbsoluteUnit.MONTH, "last month"},
                {Direction.LAST, AbsoluteUnit.QUARTER, "last quarter"},
                {Direction.LAST, AbsoluteUnit.YEAR, "last year"},
                {Direction.LAST, AbsoluteUnit.MONDAY, "last Monday"},
                {Direction.LAST, AbsoluteUnit.TUESDAY, "last Tuesday"},
                {Direction.LAST, AbsoluteUnit.WEDNESDAY, "last Wednesday"},
                {Direction.LAST, AbsoluteUnit.THURSDAY, "last Thursday"},
                {Direction.LAST, AbsoluteUnit.FRIDAY, "last Friday"},
                {Direction.LAST, AbsoluteUnit.SATURDAY, "last Saturday"},
                {Direction.LAST, AbsoluteUnit.SUNDAY, "last Sunday"},

                {Direction.THIS, AbsoluteUnit.DAY, "today"},
                {Direction.THIS, AbsoluteUnit.WEEK, "this week"},
                {Direction.THIS, AbsoluteUnit.MONTH, "this month"},
                {Direction.THIS, AbsoluteUnit.QUARTER, "this quarter"},
                {Direction.THIS, AbsoluteUnit.YEAR, "this year"},
                {Direction.THIS, AbsoluteUnit.MONDAY, "this Monday"},
                {Direction.THIS, AbsoluteUnit.TUESDAY, "this Tuesday"},
                {Direction.THIS, AbsoluteUnit.WEDNESDAY, "this Wednesday"},
                {Direction.THIS, AbsoluteUnit.THURSDAY, "this Thursday"},
                {Direction.THIS, AbsoluteUnit.FRIDAY, "this Friday"},
                {Direction.THIS, AbsoluteUnit.SATURDAY, "this Saturday"},
                {Direction.THIS, AbsoluteUnit.SUNDAY, "this Sunday"},
                {Direction.THIS, AbsoluteUnit.HOUR, "this hour"},
                {Direction.THIS, AbsoluteUnit.MINUTE, "this minute"},

                {Direction.PLAIN, AbsoluteUnit.DAY, "day"},
                {Direction.PLAIN, AbsoluteUnit.WEEK, "week"},
                {Direction.PLAIN, AbsoluteUnit.MONTH, "month"},
                {Direction.PLAIN, AbsoluteUnit.QUARTER, "quarter"},
                {Direction.PLAIN, AbsoluteUnit.YEAR, "year"},
                {Direction.PLAIN, AbsoluteUnit.MONDAY, "Monday"},
                {Direction.PLAIN, AbsoluteUnit.TUESDAY, "Tuesday"},
                {Direction.PLAIN, AbsoluteUnit.WEDNESDAY, "Wednesday"},
                {Direction.PLAIN, AbsoluteUnit.THURSDAY, "Thursday"},
                {Direction.PLAIN, AbsoluteUnit.FRIDAY, "Friday"},
                {Direction.PLAIN, AbsoluteUnit.SATURDAY, "Saturday"},
                {Direction.PLAIN, AbsoluteUnit.SUNDAY, "Sunday"},

                {Direction.PLAIN, AbsoluteUnit.NOW, "now"},
        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("en_US"));
        for (Object[] row : data) {
            String actual = fmt.format((Direction) row[0], (AbsoluteUnit) row[1]);
            assertEquals("Relative date without quantity", row[2], actual);
        }
    }

    @Test
    public void TestRelativeDateWithoutQuantityCaps() {
        Object[][] data = {
                {Direction.NEXT_2, AbsoluteUnit.DAY, null},

                {Direction.NEXT, AbsoluteUnit.DAY, "Tomorrow"},
                {Direction.NEXT, AbsoluteUnit.WEEK, "Next week"},
                {Direction.NEXT, AbsoluteUnit.MONTH, "Next month"},
                {Direction.NEXT, AbsoluteUnit.QUARTER, "Next quarter"},
                {Direction.NEXT, AbsoluteUnit.YEAR, "Next year"},

                {Direction.NEXT, AbsoluteUnit.MONDAY, "Next Monday"},
                {Direction.NEXT, AbsoluteUnit.TUESDAY, "Next Tuesday"},
                {Direction.NEXT, AbsoluteUnit.WEDNESDAY, "Next Wednesday"},
                {Direction.NEXT, AbsoluteUnit.THURSDAY, "Next Thursday"},
                {Direction.NEXT, AbsoluteUnit.FRIDAY, "Next Friday"},
                {Direction.NEXT, AbsoluteUnit.SATURDAY, "Next Saturday"},
                {Direction.NEXT, AbsoluteUnit.SUNDAY, "Next Sunday"},

                {Direction.LAST_2, AbsoluteUnit.DAY, null},

                {Direction.LAST, AbsoluteUnit.DAY, "Yesterday"},
                {Direction.LAST, AbsoluteUnit.WEEK, "Last week"},
                {Direction.LAST, AbsoluteUnit.MONTH, "Last month"},
                {Direction.LAST, AbsoluteUnit.QUARTER, "Last quarter"},
                {Direction.LAST, AbsoluteUnit.YEAR, "Last year"},
                {Direction.LAST, AbsoluteUnit.MONDAY, "Last Monday"},
                {Direction.LAST, AbsoluteUnit.TUESDAY, "Last Tuesday"},
                {Direction.LAST, AbsoluteUnit.WEDNESDAY, "Last Wednesday"},
                {Direction.LAST, AbsoluteUnit.THURSDAY, "Last Thursday"},
                {Direction.LAST, AbsoluteUnit.FRIDAY, "Last Friday"},
                {Direction.LAST, AbsoluteUnit.SATURDAY, "Last Saturday"},
                {Direction.LAST, AbsoluteUnit.SUNDAY, "Last Sunday"},

                {Direction.THIS, AbsoluteUnit.DAY, "Today"},
                {Direction.THIS, AbsoluteUnit.WEEK, "This week"},
                {Direction.THIS, AbsoluteUnit.MONTH, "This month"},
                {Direction.THIS, AbsoluteUnit.QUARTER, "This quarter"},
                {Direction.THIS, AbsoluteUnit.YEAR, "This year"},
                {Direction.THIS, AbsoluteUnit.MONDAY, "This Monday"},
                {Direction.THIS, AbsoluteUnit.TUESDAY, "This Tuesday"},
                {Direction.THIS, AbsoluteUnit.WEDNESDAY, "This Wednesday"},
                {Direction.THIS, AbsoluteUnit.THURSDAY, "This Thursday"},
                {Direction.THIS, AbsoluteUnit.FRIDAY, "This Friday"},
                {Direction.THIS, AbsoluteUnit.SATURDAY, "This Saturday"},
                {Direction.THIS, AbsoluteUnit.SUNDAY, "This Sunday"},

                {Direction.PLAIN, AbsoluteUnit.DAY, "Day"},
                {Direction.PLAIN, AbsoluteUnit.WEEK, "Week"},
                {Direction.PLAIN, AbsoluteUnit.MONTH, "Month"},
                {Direction.PLAIN, AbsoluteUnit.QUARTER, "Quarter"},
                {Direction.PLAIN, AbsoluteUnit.YEAR, "Year"},
                {Direction.PLAIN, AbsoluteUnit.MONDAY, "Monday"},
                {Direction.PLAIN, AbsoluteUnit.TUESDAY, "Tuesday"},
                {Direction.PLAIN, AbsoluteUnit.WEDNESDAY, "Wednesday"},
                {Direction.PLAIN, AbsoluteUnit.THURSDAY, "Thursday"},
                {Direction.PLAIN, AbsoluteUnit.FRIDAY, "Friday"},
                {Direction.PLAIN, AbsoluteUnit.SATURDAY, "Saturday"},
                {Direction.PLAIN, AbsoluteUnit.SUNDAY, "Sunday"},

                {Direction.PLAIN, AbsoluteUnit.NOW, "Now"},

        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.LONG,
                DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE);
        for (Object[] row : data) {
            String actual = fmt.format((Direction) row[0], (AbsoluteUnit) row[1]);
            assertEquals("Relative date without quantity caps", row[2], actual);
        }
    }

    @Test
    public void TestRelativeDateWithoutQuantityShort() {
        Object[][] data = {
                {Direction.NEXT_2, AbsoluteUnit.DAY, null},

                {Direction.NEXT, AbsoluteUnit.DAY, "tomorrow"},
                {Direction.NEXT, AbsoluteUnit.WEEK, "next wk."},

                {Direction.NEXT, AbsoluteUnit.MONTH, "next mo."},
                {Direction.NEXT, AbsoluteUnit.QUARTER, "next qtr."},
                {Direction.NEXT, AbsoluteUnit.YEAR, "next yr."},

                {Direction.NEXT, AbsoluteUnit.MONDAY, "next Mon."},

                {Direction.NEXT, AbsoluteUnit.TUESDAY, "next Tue."},
                {Direction.NEXT, AbsoluteUnit.WEDNESDAY, "next Wed."},
                {Direction.NEXT, AbsoluteUnit.THURSDAY, "next Thu."},
                {Direction.NEXT, AbsoluteUnit.FRIDAY, "next Fri."},
                {Direction.NEXT, AbsoluteUnit.SATURDAY, "next Sat."},
                {Direction.NEXT, AbsoluteUnit.SUNDAY, "next Sun."},

                {Direction.LAST_2, AbsoluteUnit.DAY, null},

                {Direction.LAST, AbsoluteUnit.DAY, "yesterday"},
                {Direction.LAST, AbsoluteUnit.WEEK, "last wk."},
                {Direction.LAST, AbsoluteUnit.MONTH, "last mo."},
                {Direction.LAST, AbsoluteUnit.QUARTER, "last qtr."},
                {Direction.LAST, AbsoluteUnit.YEAR, "last yr."},
                {Direction.LAST, AbsoluteUnit.MONDAY, "last Mon."},
                {Direction.LAST, AbsoluteUnit.TUESDAY, "last Tue."},
                {Direction.LAST, AbsoluteUnit.WEDNESDAY, "last Wed."},
                {Direction.LAST, AbsoluteUnit.THURSDAY, "last Thu."},

                {Direction.LAST, AbsoluteUnit.FRIDAY, "last Fri."},

                {Direction.LAST, AbsoluteUnit.SATURDAY, "last Sat."},
                {Direction.LAST, AbsoluteUnit.SUNDAY, "last Sun."},

                {Direction.THIS, AbsoluteUnit.DAY, "today"},
                {Direction.THIS, AbsoluteUnit.WEEK, "this wk."},
                {Direction.THIS, AbsoluteUnit.MONTH, "this mo."},
                {Direction.THIS, AbsoluteUnit.QUARTER, "this qtr."},
                {Direction.THIS, AbsoluteUnit.YEAR, "this yr."},
                {Direction.THIS, AbsoluteUnit.MONDAY, "this Mon."},
                {Direction.THIS, AbsoluteUnit.TUESDAY, "this Tue."},
                {Direction.THIS, AbsoluteUnit.WEDNESDAY, "this Wed."},
                {Direction.THIS, AbsoluteUnit.THURSDAY, "this Thu."},
                {Direction.THIS, AbsoluteUnit.FRIDAY, "this Fri."},
                {Direction.THIS, AbsoluteUnit.SATURDAY, "this Sat."},
                {Direction.THIS, AbsoluteUnit.SUNDAY, "this Sun."},

                {Direction.PLAIN, AbsoluteUnit.DAY, "day"},
                {Direction.PLAIN, AbsoluteUnit.WEEK, "wk."},
                {Direction.PLAIN, AbsoluteUnit.MONTH, "mo."},
                {Direction.PLAIN, AbsoluteUnit.QUARTER, "qtr."},
                {Direction.PLAIN, AbsoluteUnit.YEAR, "yr."},
                {Direction.PLAIN, AbsoluteUnit.MONDAY, "Mo"},
                {Direction.PLAIN, AbsoluteUnit.TUESDAY, "Tu"},
                {Direction.PLAIN, AbsoluteUnit.WEDNESDAY, "We"},
                {Direction.PLAIN, AbsoluteUnit.THURSDAY, "Th"},
                {Direction.PLAIN, AbsoluteUnit.FRIDAY, "Fr"},
                {Direction.PLAIN, AbsoluteUnit.SATURDAY, "Sa"},
                {Direction.PLAIN, AbsoluteUnit.SUNDAY, "Su"},

                {Direction.PLAIN, AbsoluteUnit.NOW, "now"},

        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.SHORT,
                DisplayContext.CAPITALIZATION_NONE);
        for (Object[] row : data) {
            String actual = fmt.format((Direction) row[0], (AbsoluteUnit) row[1]);
            assertEquals("Relative date without quantity short", row[2], actual);
        }
    }

    @Test
    public void TestRelativeDateWithoutQuantityNarrow() {
        Object[][] data = {
                {Direction.NEXT_2, AbsoluteUnit.DAY, null},

                {Direction.NEXT, AbsoluteUnit.DAY, "tomorrow"},
                {Direction.NEXT, AbsoluteUnit.WEEK, "next wk."},

                {Direction.NEXT, AbsoluteUnit.MONTH, "next mo."},
                {Direction.NEXT, AbsoluteUnit.QUARTER, "next qtr."},
                {Direction.NEXT, AbsoluteUnit.YEAR, "next yr."},

                {Direction.NEXT, AbsoluteUnit.MONDAY, "next M"},

                {Direction.NEXT, AbsoluteUnit.TUESDAY, "next Tu"},
                {Direction.NEXT, AbsoluteUnit.WEDNESDAY, "next W"},
                {Direction.NEXT, AbsoluteUnit.THURSDAY, "next Th"},
                {Direction.NEXT, AbsoluteUnit.FRIDAY, "next F"},
                {Direction.NEXT, AbsoluteUnit.SATURDAY, "next Sa"},
                {Direction.NEXT, AbsoluteUnit.SUNDAY, "next Su"},

                {Direction.LAST_2, AbsoluteUnit.DAY, null},

                {Direction.LAST, AbsoluteUnit.DAY, "yesterday"},
                {Direction.LAST, AbsoluteUnit.WEEK, "last wk."},
                {Direction.LAST, AbsoluteUnit.MONTH, "last mo."},
                {Direction.LAST, AbsoluteUnit.QUARTER, "last qtr."},
                {Direction.LAST, AbsoluteUnit.YEAR, "last yr."},
                {Direction.LAST, AbsoluteUnit.MONDAY, "last M"},
                {Direction.LAST, AbsoluteUnit.TUESDAY, "last Tu"},
                {Direction.LAST, AbsoluteUnit.WEDNESDAY, "last W"},
                {Direction.LAST, AbsoluteUnit.THURSDAY, "last Th"},
                {Direction.LAST, AbsoluteUnit.FRIDAY, "last F"},
                {Direction.LAST, AbsoluteUnit.SATURDAY, "last Sa"},
                {Direction.LAST, AbsoluteUnit.SUNDAY, "last Su"},

                {Direction.THIS, AbsoluteUnit.DAY, "today"},
                {Direction.THIS, AbsoluteUnit.WEEK, "this wk."},
                {Direction.THIS, AbsoluteUnit.MONTH, "this mo."},
                {Direction.THIS, AbsoluteUnit.QUARTER, "this qtr."},
                {Direction.THIS, AbsoluteUnit.YEAR, "this yr."},
                {Direction.THIS, AbsoluteUnit.MONDAY, "this M"},
                {Direction.THIS, AbsoluteUnit.TUESDAY, "this Tu"},
                {Direction.THIS, AbsoluteUnit.WEDNESDAY, "this W"},
                {Direction.THIS, AbsoluteUnit.THURSDAY, "this Th"},

                {Direction.THIS, AbsoluteUnit.FRIDAY, "this F"},

                {Direction.THIS, AbsoluteUnit.SATURDAY, "this Sa"},
                {Direction.THIS, AbsoluteUnit.SUNDAY, "this Su"},

                {Direction.PLAIN, AbsoluteUnit.DAY, "day"},
                {Direction.PLAIN, AbsoluteUnit.WEEK, "wk"},
                {Direction.PLAIN, AbsoluteUnit.MONTH, "mo"},
                {Direction.PLAIN, AbsoluteUnit.QUARTER, "qtr"},
                {Direction.PLAIN, AbsoluteUnit.YEAR, "yr"},
                {Direction.PLAIN, AbsoluteUnit.MONDAY, "M"},
                {Direction.PLAIN, AbsoluteUnit.TUESDAY, "T"},
                {Direction.PLAIN, AbsoluteUnit.WEDNESDAY, "W"},
                {Direction.PLAIN, AbsoluteUnit.THURSDAY, "T"},
                {Direction.PLAIN, AbsoluteUnit.FRIDAY, "F"},
                {Direction.PLAIN, AbsoluteUnit.SATURDAY, "S"},
                {Direction.PLAIN, AbsoluteUnit.SUNDAY, "S"},

                {Direction.PLAIN, AbsoluteUnit.NOW, "now"},

        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.NARROW,
                DisplayContext.CAPITALIZATION_NONE);
        for (Object[] row : data) {
            String actual = fmt.format((Direction) row[0], (AbsoluteUnit) row[1]);
            assertEquals("Relative date without quantity narrow", row[2], actual);
        }
    }

    @Test
    public void TestRelativeDateTimeUnitFormatters() {
        double[] offsets = { -5.0, -2.2, -2.0, -1.0, -0.7, -0.0, 0.0, 0.7, 1.0, 2.0, 5.0 };

        String[] en_decDef_long_midSent_sec = {
        /*  text                    numeric */
            "5 seconds ago",        "5 seconds ago",      /* -5   */
            "2.2 seconds ago",      "2.2 seconds ago",    /* -2.2 */
            "2 seconds ago",        "2 seconds ago",      /* -2   */
            "1 second ago",         "1 second ago",       /* -1   */
            "0.7 seconds ago",      "0.7 seconds ago",    /* -0.7 */
            "now",                  "0 seconds ago",      /*  -0  */
            "now",                  "in 0 seconds",       /*  0   */
            "in 0.7 seconds",       "in 0.7 seconds",     /*  0.7 */
            "in 1 second",          "in 1 second",        /*  1   */
            "in 2 seconds",         "in 2 seconds",       /*  2   */
            "in 5 seconds",         "in 5 seconds"        /*  5   */
        };

        String[] en_decDef_long_midSent_week = {
        /*  text                    numeric */
            "5 weeks ago",          "5 weeks ago",        /* -5   */
            "2.2 weeks ago",        "2.2 weeks ago",      /* -2.2 */
            "2 weeks ago",          "2 weeks ago",        /* -2   */
            "last week",            "1 week ago",         /* -1   */
            "0.7 weeks ago",        "0.7 weeks ago",      /* -0.7 */
            "this week",            "0 weeks ago",        /* -0   */
            "this week",            "in 0 weeks",         /*  0   */
            "in 0.7 weeks",         "in 0.7 weeks",       /*  0.7 */
            "next week",            "in 1 week",          /*  1   */
            "in 2 weeks",           "in 2 weeks",         /*  2   */
            "in 5 weeks",           "in 5 weeks"          /*  5   */
        };

        String[] en_dec0_long_midSent_week = {
        /*  text                    numeric */
            "5 weeks ago",          "5 weeks ago",        /* -5   */
            "2 weeks ago",          "2 weeks ago",        /* -2.2 */
            "2 weeks ago",          "2 weeks ago",        /* -2   */
            "last week",            "1 week ago",         /* -1   */
            "0 weeks ago",          "0 weeks ago",        /* -0.7 */
            "this week",            "0 weeks ago",        /* -0   */
            "this week",            "in 0 weeks",         /*  0   */
            "in 0 weeks",           "in 0 weeks",         /*  0.7 */
            "next week",            "in 1 week",          /*  1   */
            "in 2 weeks",           "in 2 weeks",         /*  2   */
            "in 5 weeks",           "in 5 weeks"          /*  5   */
        };

        String[] en_decDef_short_midSent_week = {
        /*  text                    numeric */
            "5 wk. ago",            "5 wk. ago",          /* -5   */
            "2.2 wk. ago",          "2.2 wk. ago",        /* -2.2 */
            "2 wk. ago",            "2 wk. ago",          /* -2   */
            "last wk.",             "1 wk. ago",          /* -1   */
            "0.7 wk. ago",          "0.7 wk. ago",        /* -0.7 */
            "this wk.",             "0 wk. ago",          /* -0   */
            "this wk.",             "in 0 wk.",           /*  0   */
            "in 0.7 wk.",           "in 0.7 wk.",         /*  0.7 */
            "next wk.",             "in 1 wk.",           /*  1   */
            "in 2 wk.",             "in 2 wk.",           /*  2   */
            "in 5 wk.",             "in 5 wk."            /*  5   */
        };

        String[] en_decDef_long_midSent_min = {
        /*  text                    numeric */
            "5 minutes ago",        "5 minutes ago",      /* -5   */
            "2.2 minutes ago",      "2.2 minutes ago",    /* -2.2 */
            "2 minutes ago",        "2 minutes ago",      /* -2   */
            "1 minute ago",         "1 minute ago",       /* -1   */
            "0.7 minutes ago",      "0.7 minutes ago",    /* -0.7 */
            "this minute",          "0 minutes ago",      /* -0   */
            "this minute",          "in 0 minutes",       /*  0   */
            "in 0.7 minutes",       "in 0.7 minutes",     /*  0.7 */
            "in 1 minute",          "in 1 minute",        /*  1   */
            "in 2 minutes",         "in 2 minutes",       /*  2   */
            "in 5 minutes",         "in 5 minutes"        /*  5   */
        };

        String[] en_dec0_long_midSent_tues = {
        /*  text                    numeric */
            "5 Tuesdays ago",       "5 Tuesdays ago",     /* -5   */
            "2.2 Tuesdays ago",     "2.2 Tuesdays ago",   /* -2.2 */
            "2 Tuesdays ago",       "2 Tuesdays ago",     /* -2   */
            "last Tuesday",         "1 Tuesday ago",      /* -1   */
            "0.7 Tuesdays ago",     "0.7 Tuesdays ago",   /* -0.7 */
            "this Tuesday",         "0 Tuesdays ago",     /* -0   */
            "this Tuesday",         "in 0 Tuesdays",      /*  0   */
            "in 0.7 Tuesdays",      "in 0.7 Tuesdays",    /*  0.7 */
            "next Tuesday",         "in 1 Tuesday",       /*  1   */
            "in 2 Tuesdays",        "in 2 Tuesdays",      /*  2   */
            "in 5 Tuesdays",        "in 5 Tuesdays",      /*  5   */
        };

        String[] fr_decDef_long_midSent_day = {
        /*  text                    numeric */
            "il y a 5 jours",       "il y a 5 jours",     /* -5   */
            "il y a 2,2 jours",     "il y a 2,2 jours",   /* -2.2 */
            "avant-hier",           "il y a 2 jours",     /* -2   */
            "hier",                 "il y a 1 jour",      /* -1   */
            "il y a 0,7 jour",      "il y a 0,7 jour",    /* -0.7 */
            "aujourd’hui",          "il y a 0 jour",      /* -0   */
            "aujourd’hui",          "dans 0 jour",        /*  0   */
            "dans 0,7 jour",        "dans 0,7 jour",      /*  0.7 */
            "demain",               "dans 1 jour",        /*  1   */
            "après-demain",         "dans 2 jours",       /*  2   */
            "dans 5 jours",         "dans 5 jours"        /*  5   */
        };

        String[] ak_decDef_long_stdAlon_sec = { // falls back to root
        /*  text                    numeric */
            "nnibuo 5 a atwam",     "nnibuo 5 a atwam",               /* -5   */
            "nnibuo 2.2 a atwam",   "nnibuo 2.2 a atwam",             /* -2.2 */
            "nnibuo 2 a atwam",     "nnibuo 2 a atwam",               /* -2   */
            "anibuo 1 a atwam",     "anibuo 1 a atwam",               /* -1   */
            "nnibuo 0.7 a atwam",   "nnibuo 0.7 a atwam",             /* -0.7 */
            "seesei",               "anibuo 0 a atwam",               /*  -0  */
            "seesei",               "anibuo 0 mu",               /*  0   */
            "nnibuo 0.7 mu",        "nnibuo 0.7 mu",             /*  0.7 */
            "anibuo 1 mu",          "anibuo 1 mu",               /*  1   */
            "nnibuo 2 mu",          "nnibuo 2 mu",               /*  2   */
            "nnibuo 5 mu",          "nnibuo 5 mu",               /*  5   */
        };

        String[] enIN_decDef_short_midSent_sunday = {
            /*  text                    numeric */
            "5 Sun ago",            "5 Sun ago",          /* -5   */
            "2.2 Sun ago",          "2.2 Sun ago",        /* -2.2 */
            "2 Sun ago",            "2 Sun ago",          /* -2   */
            "last Sun",             "1 Sun ago",          /* -1   */
            "0.7 Sun ago",          "0.7 Sun ago",        /* -0.7 */
            "this Sun",             "0 Sun ago",          /*  -0  */
            "this Sun",             "in 0 Sun",           /*  0   */
            "in 0.7 Sun",           "in 0.7 Sun",         /*  0.7 */
            "next Sun",             "in 1 Sun",           /*  1   */ // in 1 Sun missing in logical group
            "in 2 Sun",             "in 2 Sun",           /*  2   */
            "in 5 Sun",             "in 5 Sun"            /*  5   */
        };

        String[] enIN_decDef_short_midSent_monday = {
            /*  text                    numeric */
            "5 Mon ago",            "5 Mon ago",          /* -5   */
            "2.2 Mon ago",          "2.2 Mon ago",        /* -2.2 */
            "2 Mon ago",            "2 Mon ago",          /* -2   */
            "last Mon",             "1 Mon ago",          /* -1   */
            "0.7 Mon ago",          "0.7 Mon ago",        /* -0.7 */
            "this Mon",             "0 Mon ago",          /*  -0  */
            "this Mon",             "in 0 Mon",           /*  0   */
            "in 0.7 Mon",           "in 0.7 Mon",         /*  0.7 */
            "next Mon",             "in 1 Mon",           /*  1   */ // in 1 Mon missing in logical group
            "in 2 Mon",             "in 2 Mon",           /*  2   */
            "in 5 Mon",             "in 5 Mon"            /*  5   */
        };

        String[] enIN_decDef_short_midSent_tuesday = {
            /*  text                    numeric */
            "5 Tue ago",            "5 Tue ago",          /* -5   */
            "2.2 Tue ago",          "2.2 Tue ago",        /* -2.2 */
            "2 Tue ago",            "2 Tue ago",          /* -2   */
            "last Tue",             "1 Tue ago",          /* -1   */
            "0.7 Tue ago",          "0.7 Tue ago",        /* -0.7 */
            "this Tue",             "0 Tue ago",          /*  -0  */
            "this Tue",             "in 0 Tue",           /*  0   */
            "in 0.7 Tue",           "in 0.7 Tue",         /*  0.7 */
            "next Tue",             "in 1 Tue",           /*  1   */ // in 1 Tue missing in logical group
            "in 2 Tue",             "in 2 Tue",           /*  2   */
            "in 5 Tue",             "in 5 Tue"            /*  5   */
        };

        String[] enIN_decDef_short_midSent_wednesday = {
            /*  text                    numeric */
            "5 Wed ago",            "5 Wed ago",          /* -5   */
            "2.2 Wed ago",          "2.2 Wed ago",        /* -2.2 */
            "2 Wed ago",            "2 Wed ago",          /* -2   */
            "last Wed",             "1 Wed ago",          /* -1   */
            "0.7 Wed ago",          "0.7 Wed ago",        /* -0.7 */
            "this Wed",             "0 Wed ago",          /*  -0  */
            "this Wed",             "in 0 Wed",           /*  0   */
            "in 0.7 Wed",           "in 0.7 Wed",         /*  0.7 */
            "next Wed",             "in 1 Wed",           /*  1   */ // in 1 Wed missing in logical group
            "in 2 Wed",             "in 2 Wed",           /*  2   */
            "in 5 Wed",             "in 5 Wed"            /*  5   */
        };

        String[] enIN_decDef_short_midSent_thursday = {
            /*  text                    numeric */
            "5 Thu ago",            "5 Thu ago",          /* -5   */
            "2.2 Thu ago",          "2.2 Thu ago",        /* -2.2 */
            "2 Thu ago",            "2 Thu ago",          /* -2   */
            "last Thu",             "1 Thu ago",          /* -1   */
            "0.7 Thu ago",          "0.7 Thu ago",        /* -0.7 */
            "this Thu",             "0 Thu ago",          /*  -0  */
            "this Thu",             "in 0 Thu",           /*  0   */
            "in 0.7 Thu",           "in 0.7 Thu",         /*  0.7 */
            "next Thu",             "in 1 Thu",           /*  1   */ // in 1 Thu missing in logical group
            "in 2 Thu",             "in 2 Thu",           /*  2   */
            "in 5 Thu",             "in 5 Thu"            /*  5   */
        };

        String[] enIN_decDef_short_midSent_friday = {
            /*  text                    numeric */
            "5 Fri ago",            "5 Fri ago",          /* -5   */
            "2.2 Fri ago",          "2.2 Fri ago",        /* -2.2 */
            "2 Fri ago",            "2 Fri ago",          /* -2   */
            "last Fri",             "1 Fri ago",          /* -1   */
            "0.7 Fri ago",          "0.7 Fri ago",        /* -0.7 */
            "this Fri",             "0 Fri ago",          /*  -0  */
            "this Fri",             "in 0 Fri",           /*  0   */
            "in 0.7 Fri",           "in 0.7 Fri",         /*  0.7 */
            "next Fri",             "in 1 Fri",           /*  1   */ // in 1 Fri missing in logical group
            "in 2 Fri",             "in 2 Fri",           /*  2   */
            "in 5 Fri",             "in 5 Fri"            /*  5   */
        };

        String[] enIN_decDef_short_midSent_saturday = {
            /*  text                    numeric */
            "5 Sat ago",            "5 Sat ago",          /* -5   */
            "2.2 Sat ago",          "2.2 Sat ago",        /* -2.2 */
            "2 Sat ago",            "2 Sat ago",          /* -2   */
            "last Sat",             "1 Sat ago",          /* -1   */
            "0.7 Sat ago",          "0.7 Sat ago",        /* -0.7 */
            "this Sat",             "0 Sat ago",          /*  -0  */
            "this Sat",             "in 0 Sat",           /*  0   */
            "in 0.7 Sat",           "in 0.7 Sat",         /*  0.7 */
            "next Sat",             "in 1 Sat",           /*  1   */ // in 1 Sat missing in logical group
            "in 2 Sat",             "in 2 Sat",           /*  2   */
            "in 5 Sat",             "in 5 Sat"            /*  5   */
        };

        class TestRelativeDateTimeUnitItem {
            public String               localeID;
            public int                  decPlaces; /* fixed decimal places; -1 to use default num formatter */
            public Style                width;
            public DisplayContext       capContext;
            public RelativeDateTimeUnit unit;
            public String[]             expectedResults; /* for the various offsets */
            public TestRelativeDateTimeUnitItem(String locID, int decP, RelativeDateTimeFormatter.Style wid,
                                                DisplayContext capC, RelativeDateTimeUnit ut, String[] expR) {
                localeID    = locID;
                decPlaces   = decP;
                width       = wid;
                capContext  = capC;
                unit        = ut;
                expectedResults = expR;
            }
        };
        final TestRelativeDateTimeUnitItem[] items = {
            new TestRelativeDateTimeUnitItem("en", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.SECOND, en_decDef_long_midSent_sec),
            new TestRelativeDateTimeUnitItem("en", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.WEEK, en_decDef_long_midSent_week),
            new TestRelativeDateTimeUnitItem("en",  0, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.WEEK, en_dec0_long_midSent_week),
            new TestRelativeDateTimeUnitItem("en", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.WEEK, en_decDef_short_midSent_week),
            new TestRelativeDateTimeUnitItem("en", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.MINUTE, en_decDef_long_midSent_min),
            new TestRelativeDateTimeUnitItem("en", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.TUESDAY, en_dec0_long_midSent_tues),
            new TestRelativeDateTimeUnitItem("fr", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                                                                    RelativeDateTimeUnit.DAY, fr_decDef_long_midSent_day),
            new TestRelativeDateTimeUnitItem("ak", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_STANDALONE,
                                                                    RelativeDateTimeUnit.SECOND, ak_decDef_long_stdAlon_sec),

            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.SUNDAY, enIN_decDef_short_midSent_sunday),
            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.MONDAY, enIN_decDef_short_midSent_monday),
            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.TUESDAY, enIN_decDef_short_midSent_tuesday),
            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.WEDNESDAY, enIN_decDef_short_midSent_wednesday),
            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.THURSDAY, enIN_decDef_short_midSent_thursday),
            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.FRIDAY, enIN_decDef_short_midSent_friday),
            new TestRelativeDateTimeUnitItem("en_IN", -1, Style.SHORT, DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.SATURDAY, enIN_decDef_short_midSent_saturday),

            new TestRelativeDateTimeUnitItem("en@calendar=iso8601", -1, Style.LONG,  DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
                RelativeDateTimeUnit.SECOND, en_decDef_long_midSent_sec),
        };
        for (TestRelativeDateTimeUnitItem item: items) {
            ULocale uloc = new ULocale(item.localeID);
            NumberFormat nf = null;
            if (item.decPlaces >= 0) {
                nf = NumberFormat.getInstance(uloc, NumberFormat.NUMBERSTYLE);
                nf.setMinimumFractionDigits(item.decPlaces);
                nf.setMaximumFractionDigits(item.decPlaces);
                nf.setRoundingMode(BigDecimal.ROUND_DOWN);
            }
            RelativeDateTimeFormatter reldatefmt = RelativeDateTimeFormatter.getInstance(uloc, nf, item.width, item.capContext);
            for (int iOffset = 0; iOffset < offsets.length; iOffset++) {
                double offset = offsets[iOffset];
                String result = reldatefmt.format(offset, item.unit);
                assertEquals("RelativeDateTimeUnit format locale "+item.localeID +", dec "+item.decPlaces +", width "+item.width + ", unit "+item.unit,
                             item.expectedResults[iOffset*2], result);

                result = reldatefmt.formatNumeric(offset, item.unit);
                assertEquals("RelativeDateTimeUnit formatNum locale "+item.localeID +", dec "+item.decPlaces +", width "+item.width + ", unit "+item.unit,
                             item.expectedResults[iOffset*2 + 1], result);
            }
        }
    }

    @Test
    public void TestTwoBeforeTwoAfter() {
        Object[][] data = {
                {Direction.NEXT_2, AbsoluteUnit.DAY, "pasado ma\u00F1ana"},
                {Direction.LAST_2, AbsoluteUnit.DAY, "anteayer"},
        };
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("es"));
        for (Object[] row : data) {
            String actual = fmt.format((Direction) row[0], (AbsoluteUnit) row[1]);
            assertEquals("Two before two after", row[2], actual);
        }
    }

    @Test
    public void TestFormatWithQuantityIllegalArgument() {
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("en_US"));
        try {
            fmt.format(1.0, Direction.PLAIN, RelativeUnit.DAYS);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            fmt.format(1.0, Direction.THIS, RelativeUnit.DAYS);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void TestFormatWithoutQuantityIllegalArgument() {
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("en_US"));
        try {
            fmt.format(Direction.LAST, AbsoluteUnit.NOW);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            fmt.format(Direction.NEXT, AbsoluteUnit.NOW);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            fmt.format(Direction.THIS, AbsoluteUnit.NOW);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void TestCustomNumberFormat() {
        ULocale loc = new ULocale("en_US");
        NumberFormat nf = NumberFormat.getInstance(loc);
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(loc, nf);

        // Change nf after the fact to prove that we made a defensive copy
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);

        // Change getNumberFormat to prove we made defensive copy going out.
        fmt.getNumberFormat().setMinimumFractionDigits(5);
        assertEquals(
                "TestCustomNumberformat", 1, fmt.getNumberFormat().getMinimumFractionDigits());

        Object[][] data = {
            {0.0, Direction.NEXT, RelativeUnit.SECONDS, "in 0.0 seconds"},
            {0.5, Direction.NEXT, RelativeUnit.SECONDS, "in 0.5 seconds"},
            {1.0, Direction.NEXT, RelativeUnit.SECONDS, "in 1.0 seconds"},
            {2.0, Direction.NEXT, RelativeUnit.SECONDS, "in 2.0 seconds"},
        };
        for (Object[] row : data) {
            String actual = fmt.format(
                    ((Double) row[0]).doubleValue(), (Direction) row[1], (RelativeUnit) row[2]);
            assertEquals("Relative date with quantity special NumberFormat", row[3], actual);
        }
    }

    @Test
    public void TestCombineDateAndTime() {
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(new ULocale("en_US"));
        assertEquals("TestcombineDateAndTime", "yesterday, 3:50", fmt.combineDateAndTime("yesterday", "3:50"));
    }

    @Test
    public void TestJavaLocale() {
        Locale loc = Locale.US;
        double amount = 12.3456d;

        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(loc);
        String s = fmt.format(amount, Direction.LAST, RelativeUnit.SECONDS);
        assertEquals("Java Locale.US", "12.346 seconds ago", s);

        // Modified instance
        NumberFormat nf = fmt.getNumberFormat();
        nf.setMaximumFractionDigits(1);
        fmt = RelativeDateTimeFormatter.getInstance(loc, nf);

        s = fmt.format(amount, Direction.LAST, RelativeUnit.SECONDS);
        assertEquals("Java Locale.US", "12.3 seconds ago", s);
    }

    @Test
    public void TestGetters() {
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_US"),
                null,
                RelativeDateTimeFormatter.Style.SHORT,
                DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE);
        assertEquals("", RelativeDateTimeFormatter.Style.SHORT, fmt.getFormatStyle());
        assertEquals(
                "",
                DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE,
                fmt.getCapitalizationContext());

        // test the no-arguments getInstance();
        RelativeDateTimeFormatter fmt_default = RelativeDateTimeFormatter.getInstance();
        assertEquals("", RelativeDateTimeFormatter.Style.LONG, fmt_default.getFormatStyle());
        assertEquals(
                "",
                DisplayContext.CAPITALIZATION_NONE,
                fmt_default.getCapitalizationContext());
    }

    @Test
    public void TestBadDisplayContext() {
        try {
            RelativeDateTimeFormatter.getInstance(
                    new ULocale("en_US"),
                    null,
                    RelativeDateTimeFormatter.Style.LONG,
                    DisplayContext.STANDARD_NAMES);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void TestSidewaysDataLoading() {
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(
                new ULocale("en_GB"),
                null,
                RelativeDateTimeFormatter.Style.NARROW,
                DisplayContext.CAPITALIZATION_NONE);
        String s = fmt.format(3.0, Direction.NEXT, RelativeUnit.MONTHS);
        assertEquals("narrow: in 3 months", "in 3 mo", s);
        String t = fmt.format(1.0, Direction.LAST, RelativeUnit.QUARTERS);
        assertEquals("narrow: 1 qtr ago", "1 qtr ago", t);
        // Check for fallback to SHORT
        String u = fmt.format(3.0, Direction.LAST, RelativeUnit.QUARTERS);
        assertEquals("narrow: 3 qtr ago", "3 qtr ago", u);
        // Do not expect fall back to SHORT
        String v = fmt.format(1.0, Direction.LAST, RelativeUnit.QUARTERS);
        assertEquals("narrow: 1 qtr ago", "1 qtr ago", v);
        String w = fmt.format(6.0, Direction.NEXT, RelativeUnit.QUARTERS);
        assertEquals("narrow: in 6 qtr", "in 6 qtr", w);
    }

    @Test
    public void TestLocales() {
        ULocale[] availableLocales = ULocale.getAvailableLocales();
        for (ULocale loc: availableLocales) {
            RelativeDateTimeFormatter.getInstance(loc);
        }
    }

    @Test
    public void TestFields() {
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(ULocale.US);

        {
            String message = "automatic absolute unit";
            FormattedRelativeDateTime fv = fmt.formatToValue(1, RelativeDateTimeUnit.DAY);
            String expectedString = "tomorrow";
            Object[][] expectedFieldPositions = new Object[][]{
                {RelativeDateTimeFormatter.Field.LITERAL, 0, 8}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }
        {
            String message = "automatic numeric unit";
            FormattedRelativeDateTime fv = fmt.formatToValue(3, RelativeDateTimeUnit.DAY);
            String expectedString = "in 3 days";
            Object[][] expectedFieldPositions = new Object[][]{
                {RelativeDateTimeFormatter.Field.LITERAL, 0, 2},
                {NumberFormat.Field.INTEGER, 3, 4},
                {RelativeDateTimeFormatter.Field.NUMERIC, 3, 4},
                {RelativeDateTimeFormatter.Field.LITERAL, 5, 9}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }
        {
            String message = "manual absolute unit";
            FormattedRelativeDateTime fv = fmt.formatToValue(Direction.NEXT, AbsoluteUnit.MONDAY);
            String expectedString = "next Monday";
            Object[][] expectedFieldPositions = new Object[][]{
                {RelativeDateTimeFormatter.Field.LITERAL, 0, 11}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }
        {
            String message = "manual numeric unit";
            FormattedRelativeDateTime fv = fmt.formatNumericToValue(1.5, RelativeDateTimeUnit.WEEK);
            String expectedString = "in 1.5 weeks";
            Object[][] expectedFieldPositions = new Object[][]{
                {RelativeDateTimeFormatter.Field.LITERAL, 0, 2},
                {NumberFormat.Field.INTEGER, 3, 4},
                {NumberFormat.Field.DECIMAL_SEPARATOR, 4, 5},
                {NumberFormat.Field.FRACTION, 5, 6},
                {RelativeDateTimeFormatter.Field.NUMERIC, 3, 6},
                {RelativeDateTimeFormatter.Field.LITERAL, 7, 12}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }
        {
            String message = "manual numeric resolved unit";
            FormattedRelativeDateTime fv = fmt.formatToValue(12, Direction.LAST, RelativeUnit.HOURS);
            String expectedString = "12 hours ago";
            Object[][] expectedFieldPositions = new Object[][]{
                {NumberFormat.Field.INTEGER, 0, 2},
                {RelativeDateTimeFormatter.Field.NUMERIC, 0, 2},
                {RelativeDateTimeFormatter.Field.LITERAL, 3, 12}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }

        // Test when the number field is at the end
        fmt = RelativeDateTimeFormatter.getInstance(new ULocale("sw"));
        {
            String message = "numeric field at end";
            FormattedRelativeDateTime fv = fmt.formatToValue(12, RelativeDateTimeUnit.HOUR);
            String expectedString = "baada ya saa 12";
            Object[][] expectedFieldPositions = new Object[][]{
                {RelativeDateTimeFormatter.Field.LITERAL, 0, 12},
                {NumberFormat.Field.INTEGER, 13, 15},
                {RelativeDateTimeFormatter.Field.NUMERIC, 13, 15}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }
    }

    @Test
    public void TestRBNF() {
        RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(ULocale.US, RuleBasedNumberFormat.SPELLOUT);
        RelativeDateTimeFormatter fmt = RelativeDateTimeFormatter.getInstance(ULocale.US, rbnf);
        assertEquals("format (direction)", "in five seconds", fmt.format(5, Direction.NEXT, RelativeUnit.SECONDS));
        assertEquals("formatNumeric", "one week ago", fmt.formatNumeric(-1, RelativeDateTimeUnit.WEEK));
        assertEquals("format (absolute)", "yesterday", fmt.format(Direction.LAST, AbsoluteUnit.DAY));
        assertEquals("format (relative)", "in forty-two months", fmt.format(42, RelativeDateTimeUnit.MONTH));

        {
            String message = "formatToValue (relative)";
            FormattedRelativeDateTime fv = fmt.formatToValue(-100, RelativeDateTimeUnit.YEAR);
            String expectedString = "one hundred years ago";
            Object[][] expectedFieldPositions = new Object[][]{
                {RelativeDateTimeFormatter.Field.NUMERIC, 0, 11},
                {RelativeDateTimeFormatter.Field.LITERAL, 12, 21}};
            FormattedValueTest.checkFormattedValue(message, fv, expectedString, expectedFieldPositions);
        }
    }

}
