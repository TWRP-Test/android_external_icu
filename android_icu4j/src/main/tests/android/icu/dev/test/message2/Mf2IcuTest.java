/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2022 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package android.icu.dev.test.message2;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.icu.dev.test.CoreTestFmwk;
import android.icu.message2.MessageFormatter;
import android.icu.text.MessageFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.icu.testsharding.MainTestShard;

/**
 * Ported the unit tests from {@link android.icu.text.MessageFormat} to show that they work.
 *
 * <p>It does not include all the tests for edge cases and error handling, only the ones that show real functionality.</p>
 */
@MainTestShard
@RunWith(JUnit4.class)
@SuppressWarnings({"static-method", "javadoc"})
public class Mf2IcuTest extends CoreTestFmwk {

    @Test
    public void testSample() {
        MessageFormatter form = MessageFormatter.builder()
                .setPattern("There are {$count} files on {$where}")
                .build();
        assertEquals("format", "There are abc files on def",
                form.formatToString(Args.of("count", "abc", "where", "def")));
    }

    @Test
    public void testStaticFormat() {
        Map<String, Object> arguments = Args.of(
                "planet", 7,
                "when", new Date(871068000000L),
                "what", "a disturbance in the Force");

        assertEquals("format", "At 12:20:00\u202FPM on Aug 8, 1997, there was a disturbance in the Force on planet 7.",
                MessageFormatter.builder()
                .setPattern("At {$when :datetime timeStyle=medium} on {$when :datetime dateStyle=medium}, "
                        + "there was {$what} on planet {$planet :number kind=integer}.")
                .build()
                .formatToString(arguments));
    }

    static final int FieldPosition_DONT_CARE = -1;

    @Test
    public void testSimpleFormat() {
        Map<String, Object> testArgs1 = Args.of("fileCount", 0, "diskName", "MyDisk");
        Map<String, Object> testArgs2 = Args.of("fileCount", 1, "diskName", "MyDisk");
        Map<String, Object> testArgs3 = Args.of("fileCount", 12, "diskName", "MyDisk");

        MessageFormatter form = MessageFormatter.builder()
                .setPattern("The disk \"{$diskName}\" contains {$fileCount} file(s).")
                .build();

        assertEquals("format", "The disk \"MyDisk\" contains 0 file(s).", form.formatToString(testArgs1));

        form.formatToString(testArgs2);
        assertEquals("format", "The disk \"MyDisk\" contains 1 file(s).", form.formatToString(testArgs2));

        form.formatToString(testArgs3);
        assertEquals("format", "The disk \"MyDisk\" contains 12 file(s).", form.formatToString(testArgs3));
    }

    @Test
    public void testSelectFormatToPattern() {
        String pattern = ""
                + ".match {$userGender :string}\n"
                + "  female {{{$userName} est all\u00E9e \u00E0 Paris.}}"
                + "   *     {{{$userName} est all\u00E9 \u00E0 Paris.}}"
                ;

            MessageFormatter mf = MessageFormatter.builder()
                    .setPattern(pattern)
                    .build();
            assertEquals("old icu test",
                    "Charlotte est allée à Paris.",
                    mf.formatToString(Args.of("userName", "Charlotte", "userGender", "female")));
            assertEquals("old icu test",
                    "Guillaume est allé à Paris.",
                    mf.formatToString(Args.of("userName", "Guillaume", "userGender", "male")));
            assertEquals("old icu test",
                    "Dominique est allé à Paris.",
                    mf.formatToString(Args.of("userName", "Dominique", "userGender", "unnown")));
    }

    private static void doTheRealDateTimeSkeletonTesting(Date date, String messagePattern, Locale locale,
            String expected) {

        MessageFormatter msgf = MessageFormatter.builder()
                .setPattern(messagePattern).setLocale(locale)
                .build();
        assertEquals(messagePattern, expected, msgf.formatToString(Args.of("when", date)));
    }

    @Test
    public void testMessageFormatDateTimeSkeleton() {
        Date date = new GregorianCalendar(2021, Calendar.NOVEMBER, 23, 16, 42, 55).getTime();

        doTheRealDateTimeSkeletonTesting(date, "{$when :datetime icu:skeleton=MMMMd}",
                Locale.forLanguageTag("en"), "November 23");
        doTheRealDateTimeSkeletonTesting(date, "{$when :datetime icu:skeleton=yMMMMdjm}",
                Locale.forLanguageTag("en"), "November 23, 2021 at 4:42\u202FPM");
        doTheRealDateTimeSkeletonTesting(date, "{$when :datetime icu:skeleton=|yMMMMd|}",
                Locale.forLanguageTag("en"), "November 23, 2021");
        doTheRealDateTimeSkeletonTesting(date, "{$when :datetime icu:skeleton=yMMMMd}",
                Locale.forLanguageTag("fr"), "23 novembre 2021");
        doTheRealDateTimeSkeletonTesting(date, "Expiration: {$when :datetime icu:skeleton=yMMM}!",
                Locale.forLanguageTag("en"), "Expiration: Nov 2021!");
    }

    @Test
    public void testMessageFormatDateTimeOptions() {
        Date date = new GregorianCalendar(2021, Calendar.NOVEMBER, 23, 16, 42, 55).getTime();

        doTheRealDateTimeSkeletonTesting(date, "{$when :date month=long day=numeric}",
                Locale.forLanguageTag("en"), "November 23");
        doTheRealDateTimeSkeletonTesting(date, "{$when :datetime year=numeric month=long day=numeric hour=numeric minute=numeric}",
                Locale.forLanguageTag("en"), "November 23, 2021 at 4:42\u202FPM");
        doTheRealDateTimeSkeletonTesting(date, "{$when :date year=numeric month=short day=numeric}",
                Locale.forLanguageTag("en"), "Nov 23, 2021");
        doTheRealDateTimeSkeletonTesting(date, "{$when :datetime year=numeric month=long day=numeric}",
                Locale.forLanguageTag("fr"), "23 novembre 2021");
        doTheRealDateTimeSkeletonTesting(date, "Expiration: {$when :datetime year=numeric month=short}!",
                Locale.forLanguageTag("en"), "Expiration: Nov 2021!");
    }

    @Test
    public void checkMf1Behavior() {
        Date testDate = new Date(1671782400000L); // 2022-12-23
        Map<String, Object> goodArg = Args.of("user", "John", "today", testDate);
        Map<String, Object> badArg = Args.of("userX", "John", "todayX", testDate);

        MessageFormat mf1 = new MessageFormat("Hello {user}, today is {today,date,long}.");
        assertEquals("old icu test", "Hello {user}, today is {today}.", mf1.format(badArg));
        assertEquals("old icu test", "Hello John, today is December 23, 2022.", mf1.format(goodArg));

        MessageFormatter mf2 = MessageFormatter.builder()
                .setPattern("Hello {$user}, today is {$today :datetime dateStyle=long}.")
                .build();
        assertEquals("old icu test", "Hello {$user}, today is {$today}.", mf2.formatToString(badArg));
        assertEquals("old icu test", "Hello John, today is December 23, 2022.", mf2.formatToString(goodArg));
    }
}
