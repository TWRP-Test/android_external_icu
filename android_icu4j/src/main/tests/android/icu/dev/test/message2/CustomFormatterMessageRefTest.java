/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2022 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package android.icu.dev.test.message2;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.icu.dev.test.CoreTestFmwk;
import android.icu.message2.FormattedPlaceholder;
import android.icu.message2.Formatter;
import android.icu.message2.FormatterFactory;
import android.icu.message2.MFFunctionRegistry;
import android.icu.message2.MessageFormatter;
import android.icu.message2.PlainStringFormattedValue;
import android.icu.testsharding.MainTestShard;

/**
 * Showing a custom formatter that can implement message references.
 *
 * <p>Supporting this functionality was strongly requested as a part of the core specification.
 * But this shows that it can be easily implemented as a custom function.</p>
 */
@MainTestShard
@RunWith(JUnit4.class)
@SuppressWarnings({"static-method", "javadoc"})
public class CustomFormatterMessageRefTest extends CoreTestFmwk {

    static class ResourceManagerFactory implements FormatterFactory {

        @Override
        public Formatter createFormatter(Locale locale, Map<String, Object> fixedOptions) {
            return new ResourceManagerFactoryImpl(locale, fixedOptions);
        }

        static class ResourceManagerFactoryImpl implements Formatter {
            final Map<String, Object> options;

            @SuppressWarnings("unused")
            ResourceManagerFactoryImpl(Locale locale, Map<String, Object> options) {
                this.options = options;
            }

            @Override
            public FormattedPlaceholder format(Object toFormat, Map<String, Object> variableOptions) {
                String result = null;
                Object oProps = options.get("resbundle");
                // If it was not in the fixed options, try in the variable ones
                if (oProps == null) {
                    oProps = variableOptions.get("resbundle");
                }
                if (oProps != null && oProps instanceof Properties) {
                    Properties props = (Properties) oProps;
                    Object msg = props.get(toFormat.toString());
                    MessageFormatter mf = MessageFormatter.builder()
                            .setPattern(msg.toString())
                            .build();
                    result = mf.formatToString(options);
                }
                return new FormattedPlaceholder(toFormat, new PlainStringFormattedValue(result));
            }

            @Override
            public String formatToString(Object toFormat, Map<String, Object> variableOptions) {
                return format(toFormat, variableOptions).toString();
            }
        }
    }

    static final MFFunctionRegistry REGISTRY = MFFunctionRegistry.builder()
            .setFormatter("msgRef", new ResourceManagerFactory())
            .build();

    static final Properties PROPERTIES = new Properties();

    @BeforeClass
    public static void beforeClass() {
        PROPERTIES.put("firefox", ".match {$gcase :string} genitive {{Firefoxin}} * {{Firefox}}");
        PROPERTIES.put("chrome", ".match {$gcase :string} genitive {{Chromen}} * {{Chrome}}");
        PROPERTIES.put("safari", ".match {$gcase :string} genitive {{Safarin}} * {{Safari}}");
    }

    @Test
    public void testSimpleGrammarSelection() {
        MessageFormatter mf = MessageFormatter.builder()
                .setPattern(PROPERTIES.getProperty("firefox"))
                .build();
        assertEquals("cust-grammar", "Firefox", mf.formatToString(Args.of("gcase", "whatever")));
        assertEquals("cust-grammar", "Firefoxin", mf.formatToString(Args.of("gcase", "genitive")));

        mf = MessageFormatter.builder()
                .setPattern(PROPERTIES.getProperty("chrome"))
                .build();
        assertEquals("cust-grammar", "Chrome", mf.formatToString(Args.of("gcase", "whatever")));
        assertEquals("cust-grammar", "Chromen", mf.formatToString(Args.of("gcase", "genitive")));
    }

    @Test
    public void test() {
        StringBuffer browser = new StringBuffer();
        Map<String, Object> arguments = Args.of(
                "browser", browser,
                "res", PROPERTIES);

        MessageFormatter mf1 = MessageFormatter.builder()
                .setFunctionRegistry(REGISTRY)
                .setPattern("Please start {$browser :msgRef gcase=genitive resbundle=$res}")
                .build();
        MessageFormatter mf2 = MessageFormatter.builder()
                .setFunctionRegistry(REGISTRY)
                .setPattern("Please start {$browser :msgRef resbundle=$res}")
                .build();

        browser.replace(0, browser.length(), "firefox");
        assertEquals("cust-grammar", "Please start Firefoxin", mf1.formatToString(arguments));
        assertEquals("cust-grammar", "Please start Firefox", mf2.formatToString(arguments));

        browser.replace(0, browser.length(), "chrome");
        assertEquals("cust-grammar", "Please start Chromen", mf1.formatToString(arguments));
        assertEquals("cust-grammar", "Please start Chrome", mf2.formatToString(arguments));

        browser.replace(0, browser.length(), "safari");
        assertEquals("cust-grammar", "Please start Safarin", mf1.formatToString(arguments));
        assertEquals("cust-grammar", "Please start Safari", mf2.formatToString(arguments));
    }
}
