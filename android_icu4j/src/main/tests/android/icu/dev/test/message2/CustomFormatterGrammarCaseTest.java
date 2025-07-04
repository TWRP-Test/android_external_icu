/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2022 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package android.icu.dev.test.message2;

import java.util.Locale;
import java.util.Map;

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
 * Showing a custom formatter that can handle grammatical cases.
 */
@MainTestShard
@RunWith(JUnit4.class)
@SuppressWarnings({"static-method", "javadoc"})
public class CustomFormatterGrammarCaseTest extends CoreTestFmwk {

    static class GrammarCasesFormatterFactory implements FormatterFactory {

        @Override
        public Formatter createFormatter(Locale locale, Map<String, Object> fixedOptions) {
            Object grammarCase = fixedOptions.get("case");
            return new GrammarCasesFormatterImpl(grammarCase == null ? "" : grammarCase.toString());
        }

        static class GrammarCasesFormatterImpl implements Formatter {
            final String grammarCase;

            GrammarCasesFormatterImpl(String grammarCase) {
                this.grammarCase = grammarCase;
            }

            // Romanian naive and incomplete rules, just to make things work for testing.
            private static String getDativeAndGenitive(String value) {
                if (value.endsWith("ana")) {
                    return value.substring(0, value.length() - 3) + "nei";
                }
                if (value.endsWith("ca")) {
                    return value.substring(0, value.length() - 2) + "căi";
                }
                if (value.endsWith("ga")) {
                    return value.substring(0, value.length() - 2) + "găi";
                }
                if (value.endsWith("a")) {
                    return value.substring(0, value.length() - 1) + "ei";
                }
                return "lui " + value;
            }

            @Override
            public String formatToString(Object toFormat, Map<String, Object> variableOptions) {
                return format(toFormat, variableOptions).toString();
            }

            @Override
            public FormattedPlaceholder format(Object toFormat, Map<String, Object> variableOptions) {
                String result;
                if (toFormat == null) {
                    result = null;
                } else if (toFormat instanceof CharSequence) {
                    String value = (String) toFormat;
                    switch (grammarCase) {
                        case "dative": // intentional fallback
                        case "genitive":
                            result = getDativeAndGenitive(value);
                            // and so on for other cases, but I don't care to add more for now
                            break;
                        default:
                            result = value;
                    }
                } else {
                    result = toFormat.toString();
                }
                return new FormattedPlaceholder(toFormat, new PlainStringFormattedValue(result));
            }
        }
    }

    static final MFFunctionRegistry REGISTRY = MFFunctionRegistry.builder()
            .setFormatter("grammarBB", new GrammarCasesFormatterFactory())
            .build();

    @Test
    public void test() {
        MessageFormatter mf = MessageFormatter.builder()
                .setFunctionRegistry(REGISTRY)
                .setLocale(Locale.forLanguageTag("ro"))
                .setPattern("Cartea {$owner :grammarBB case=genitive}")
                .build();

        assertEquals("case - genitive", "Cartea Mariei", mf.formatToString(Args.of("owner", "Maria")));
        assertEquals("case - genitive", "Cartea Rodicăi", mf.formatToString(Args.of("owner", "Rodica")));
        assertEquals("case - genitive", "Cartea Ilenei", mf.formatToString(Args.of("owner", "Ileana")));
        assertEquals("case - genitive", "Cartea lui Petre", mf.formatToString(Args.of("owner", "Petre")));

        mf = MessageFormatter.builder()
                .setFunctionRegistry(REGISTRY)
                .setLocale(Locale.forLanguageTag("ro"))
                .setPattern("M-a sunat {$owner :grammarBB case=nominative}")
                .build();

        assertEquals("case - nominative", "M-a sunat Maria", mf.formatToString(Args.of("owner", "Maria")));
        assertEquals("case - nominative", "M-a sunat Rodica", mf.formatToString(Args.of("owner", "Rodica")));
        assertEquals("case - nominative", "M-a sunat Ileana", mf.formatToString(Args.of("owner", "Ileana")));
        assertEquals("case - nominative", "M-a sunat Petre", mf.formatToString(Args.of("owner", "Petre")));
    }
}
