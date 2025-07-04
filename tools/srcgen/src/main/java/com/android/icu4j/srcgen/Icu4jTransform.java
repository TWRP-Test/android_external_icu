/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.icu4j.srcgen;

import static com.google.currysrc.api.process.Rules.createMandatoryRule;
import static com.google.currysrc.api.process.Rules.createOptionalRule;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;

import com.google.common.collect.Lists;
import com.google.currysrc.Main;
import com.google.currysrc.aosp.Annotations;
import com.google.currysrc.api.RuleSet;
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.output.BasicOutputSourceFileGenerator;
import com.google.currysrc.api.output.OutputSourceFileGenerator;
import com.google.currysrc.api.process.Rule;
import com.google.currysrc.api.process.ast.BodyDeclarationLocator;
import com.google.currysrc.api.process.ast.BodyDeclarationLocators;
import com.google.currysrc.api.process.ast.TypeLocator;
import com.google.currysrc.processors.AddAnnotation;
import com.google.currysrc.processors.AddDefaultConstructor;
import com.google.currysrc.processors.AnnotationInfo;
import com.google.currysrc.processors.HidePublicClasses;
import com.google.currysrc.processors.InsertHeader;
import com.google.currysrc.processors.ModifyQualifiedNames;
import com.google.currysrc.processors.ModifyStringLiterals;
import com.google.currysrc.processors.RemoveJavaDocTags;
import com.google.currysrc.processors.RenamePackage;
import com.google.currysrc.processors.ReplaceTextCommentScanner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Applies Android's ICU4J source code transformation rules. If you make any changes to this class
 * then you should re-run the generate_android_icu4j.sh script.
 */
public class Icu4jTransform {

  // The list of public ICU API classes exposed on Android. If you change this, you should change
  // the INITIAL_DEPRECATED_SET below to include entries from the new classes.
  static final String[] PUBLIC_API_CLASSES = new String[] {
      /* ASCII order please. */
      "android.icu.lang.UCharacter",
      "android.icu.lang.UCharacter$BidiPairedBracketType",
      "android.icu.lang.UCharacter$DecompositionType",
      "android.icu.lang.UCharacter$EastAsianWidth",
      "android.icu.lang.UCharacter$GraphemeClusterBreak",
      "android.icu.lang.UCharacter$HangulSyllableType",
      "android.icu.lang.UCharacter$IndicPositionalCategory",
      "android.icu.lang.UCharacter$IndicSyllabicCategory",
      "android.icu.lang.UCharacter$JoiningGroup",
      "android.icu.lang.UCharacter$JoiningType",
      "android.icu.lang.UCharacter$LineBreak",
      "android.icu.lang.UCharacter$NumericType",
      "android.icu.lang.UCharacter$SentenceBreak",
      "android.icu.lang.UCharacter$UnicodeBlock",
      "android.icu.lang.UCharacter$VerticalOrientation",
      "android.icu.lang.UCharacter$WordBreak",
      "android.icu.lang.UCharacterCategory",
      "android.icu.lang.UCharacterDirection",
      "android.icu.lang.UCharacterEnums",
      "android.icu.lang.UCharacterEnums$ECharacterCategory",
      "android.icu.lang.UCharacterEnums$ECharacterDirection",
      "android.icu.lang.UProperty",
      "android.icu.lang.UProperty$NameChoice",
      "android.icu.lang.UScript",
      "android.icu.lang.UScript$ScriptUsage",
      "android.icu.math.BigDecimal",
      "android.icu.math.MathContext",
      "android.icu.number.CompactNotation",
      "android.icu.number.CurrencyPrecision",
      "android.icu.number.FormattedNumber",
      "android.icu.number.FormattedNumberRange",
      "android.icu.number.FractionPrecision",
      "android.icu.number.IntegerWidth",
      "android.icu.number.LocalizedNumberFormatter",
      "android.icu.number.LocalizedNumberRangeFormatter",
      "android.icu.number.Notation",
      "android.icu.number.NumberFormatter",
      "android.icu.number.NumberFormatter$DecimalSeparatorDisplay",
      "android.icu.number.NumberFormatter$GroupingStrategy",
      "android.icu.number.NumberFormatter$RoundingPriority",
      "android.icu.number.NumberFormatter$SignDisplay",
      "android.icu.number.NumberFormatter$TrailingZeroDisplay",
      "android.icu.number.NumberFormatter$UnitWidth",
      "android.icu.number.NumberFormatterSettings",
      "android.icu.number.NumberRangeFormatter",
      "android.icu.number.NumberRangeFormatter$RangeCollapse",
      "android.icu.number.NumberRangeFormatter$RangeIdentityFallback",
      "android.icu.number.NumberRangeFormatter$RangeIdentityResult",
      "android.icu.number.NumberRangeFormatterSettings",
      "android.icu.number.Precision",
      "android.icu.number.Scale",
      "android.icu.number.ScientificNotation",
      "android.icu.number.SimpleNotation",
      "android.icu.number.UnlocalizedNumberFormatter",
      "android.icu.number.UnlocalizedNumberRangeFormatter",
      "android.icu.text.AlphabeticIndex",
      "android.icu.text.AlphabeticIndex$Bucket",
      "android.icu.text.AlphabeticIndex$Bucket$LabelType",
      "android.icu.text.AlphabeticIndex$ImmutableIndex",
      "android.icu.text.AlphabeticIndex$Record",
      "android.icu.text.Bidi",
      "android.icu.text.BidiClassifier",
      "android.icu.text.BidiRun",
      "android.icu.text.BreakIterator",
      "android.icu.text.CaseMap",
      "android.icu.text.CaseMap$Fold",
      "android.icu.text.CaseMap$Lower",
      "android.icu.text.CaseMap$Title",
      "android.icu.text.CaseMap$Upper",
      "android.icu.text.CollationElementIterator",
      "android.icu.text.CollationKey",
      "android.icu.text.CollationKey$BoundMode",
      "android.icu.text.Collator",
      "android.icu.text.Collator$CollatorFactory",
      "android.icu.text.Collator$ReorderCodes",
      "android.icu.text.CompactDecimalFormat",
      "android.icu.text.CompactDecimalFormat$CompactStyle",
      "android.icu.text.ConstrainedFieldPosition",
      "android.icu.text.CurrencyPluralInfo",
      "android.icu.text.DateFormat",
      "android.icu.text.DateFormat$BooleanAttribute",
      "android.icu.text.DateFormat$Field",
      "android.icu.text.DateFormat$HourCycle",
      "android.icu.text.DateFormatSymbols",
      "android.icu.text.DateIntervalFormat",
      "android.icu.text.DateIntervalFormat$FormattedDateInterval",
      "android.icu.text.DateIntervalInfo",
      "android.icu.text.DateIntervalInfo$PatternInfo",
      "android.icu.text.DateTimePatternGenerator",
      "android.icu.text.DateTimePatternGenerator$DisplayWidth",
      "android.icu.text.DateTimePatternGenerator$PatternInfo",
      "android.icu.text.DecimalFormat",
      "android.icu.text.DecimalFormatSymbols",
      "android.icu.text.DisplayContext",
      "android.icu.text.DisplayContext$Type",
      "android.icu.text.DisplayOptions",
      "android.icu.text.DisplayOptions$Builder",
      "android.icu.text.DisplayOptions$Capitalization",
      "android.icu.text.DisplayOptions$DisplayLength",
      "android.icu.text.DisplayOptions$GrammaticalCase",
      "android.icu.text.DisplayOptions$NameStyle",
      "android.icu.text.DisplayOptions$NounClass",
      "android.icu.text.DisplayOptions$PluralCategory",
      "android.icu.text.DisplayOptions$SubstituteHandling",
      "android.icu.text.Edits",
      "android.icu.text.Edits$Iterator",
      "android.icu.text.FormattedValue",
      "android.icu.text.IDNA",
      "android.icu.text.IDNA$Error",
      "android.icu.text.IDNA$Info",
      "android.icu.text.ListFormatter",
      "android.icu.text.ListFormatter$FormattedList",
      "android.icu.text.ListFormatter$Type",
      "android.icu.text.ListFormatter$Width",
      "android.icu.text.LocaleDisplayNames",
      "android.icu.text.LocaleDisplayNames$DialectHandling",
      "android.icu.text.LocaleDisplayNames$UiListItem",
      "android.icu.text.MeasureFormat",
      "android.icu.text.MeasureFormat$FormatWidth",
      "android.icu.text.MessageFormat",
      "android.icu.text.MessageFormat$Field",
      "android.icu.text.MessagePattern",
      "android.icu.text.MessagePattern$ApostropheMode",
      "android.icu.text.MessagePattern$ArgType",
      "android.icu.text.MessagePattern$Part",
      "android.icu.text.MessagePattern$Part$Type",
      "android.icu.text.Normalizer",
      "android.icu.text.Normalizer$QuickCheckResult",
      "android.icu.text.Normalizer2",
      "android.icu.text.Normalizer2$Mode",
      "android.icu.text.NumberFormat",
      "android.icu.text.NumberFormat$Field",
      "android.icu.text.NumberingSystem",
      "android.icu.text.PluralFormat",
      "android.icu.text.PluralRules",
      "android.icu.text.PluralRules$PluralType",
      "android.icu.text.RelativeDateTimeFormatter",
      "android.icu.text.RelativeDateTimeFormatter$AbsoluteUnit",
      "android.icu.text.RelativeDateTimeFormatter$Direction",
      "android.icu.text.RelativeDateTimeFormatter$FormattedRelativeDateTime",
      "android.icu.text.RelativeDateTimeFormatter$RelativeDateTimeUnit",
      "android.icu.text.RelativeDateTimeFormatter$RelativeUnit",
      "android.icu.text.RelativeDateTimeFormatter$Style",
      "android.icu.text.Replaceable",
      "android.icu.text.RuleBasedCollator",
      "android.icu.text.ScientificNumberFormatter",
      "android.icu.text.SearchIterator",
      "android.icu.text.SearchIterator$ElementComparisonType",
      "android.icu.text.SelectFormat",
      "android.icu.text.SimpleDateFormat",
      "android.icu.text.StringPrepParseException",
      "android.icu.text.StringSearch",
      "android.icu.text.SymbolTable",
      "android.icu.text.TimeZoneFormat",
      "android.icu.text.TimeZoneFormat$GMTOffsetPatternType",
      "android.icu.text.TimeZoneFormat$ParseOption",
      "android.icu.text.TimeZoneFormat$Style",
      "android.icu.text.TimeZoneFormat$TimeType",
      "android.icu.text.TimeZoneNames",
      "android.icu.text.TimeZoneNames$NameType",
      "android.icu.text.Transliterator",
      "android.icu.text.Transliterator$Position",
      "android.icu.text.UCharacterIterator",
      "android.icu.text.UFormat",
      "android.icu.text.UnicodeFilter",
      "android.icu.text.UnicodeMatcher",
      "android.icu.text.UnicodeSet",
      "android.icu.text.UnicodeSet$ComparisonStyle",
      "android.icu.text.UnicodeSet$EntryRange",
      "android.icu.text.UnicodeSet$SpanCondition",
      "android.icu.text.UnicodeSetIterator",
      "android.icu.text.UnicodeSetSpanner",
      "android.icu.text.UnicodeSetSpanner$CountMethod",
      "android.icu.text.UnicodeSetSpanner$TrimOption",
      "android.icu.util.BuddhistCalendar",
      "android.icu.util.CECalendar",
      "android.icu.util.Calendar",
      "android.icu.util.Calendar$WeekData",
      "android.icu.util.ChineseCalendar",
      "android.icu.util.CopticCalendar",
      "android.icu.util.Currency",
      "android.icu.util.Currency$CurrencyUsage",
      "android.icu.util.CurrencyAmount",
      "android.icu.util.DateInterval",
      "android.icu.util.EthiopicCalendar",
      "android.icu.util.Freezable",
      "android.icu.util.GregorianCalendar",
      "android.icu.util.HebrewCalendar",
      "android.icu.util.ICUUncheckedIOException",
      "android.icu.util.IllformedLocaleException",
      "android.icu.util.IndianCalendar",
      "android.icu.util.IslamicCalendar",
      "android.icu.util.IslamicCalendar$CalculationType",
      "android.icu.util.JapaneseCalendar",
      "android.icu.util.LocaleData",
      "android.icu.util.LocaleData$MeasurementSystem",
      "android.icu.util.LocaleData$PaperSize",
      "android.icu.util.Measure",
      "android.icu.util.MeasureUnit",
      "android.icu.util.MeasureUnit$Complexity",
      "android.icu.util.MeasureUnit$MeasurePrefix",
      "android.icu.util.Output",
      "android.icu.util.RangeValueIterator",
      "android.icu.util.RangeValueIterator$Element",
      "android.icu.util.TaiwanCalendar",
      "android.icu.util.TimeUnit",
      "android.icu.util.TimeZone",
      "android.icu.util.TimeZone$SystemTimeZoneType",
      "android.icu.util.ULocale",
      "android.icu.util.ULocale$AvailableType",
      "android.icu.util.ULocale$Builder",
      "android.icu.util.ULocale$Category",
      "android.icu.util.UniversalTimeScale",
      "android.icu.util.ValueIterator",
      "android.icu.util.ValueIterator$Element",
      "android.icu.util.VersionInfo",

  };

  /**
   * The set of deprecated ICU types/methods/fields that must not be part of the public API as they
   * were deprecated when Android first exposed their classes as a public API. Methods deprecated by
   * ICU after this will be visible and deprecated in Android.
   */
  // This list was originally generated by the CaptureDeprecatedElements tool when run against
  // ICU56 with the original PUBLIC_API_CLASSES.
  private static final String[] INITIAL_DEPRECATED_SET = new String[] {
      /* ASCII order please. */
      "field:android.icu.lang.UProperty#ISO_COMMENT",
      "field:android.icu.lang.UProperty#UNDEFINED",
      "field:android.icu.lang.UProperty#UNICODE_1_NAME",
      "field:android.icu.lang.UScript#DUPLOYAN_SHORTAND",
      "field:android.icu.text.Bidi#CLASS_DEFAULT",
      "field:android.icu.text.CaseMap#internalOptions",
      "field:android.icu.text.DateFormat#ABBR_STANDALONE_MONTH",
      "field:android.icu.text.DateFormat#DATE_SKELETONS",
      "field:android.icu.text.DateFormat#HOUR_GENERIC_TZ",
      "field:android.icu.text.DateFormat#HOUR_MINUTE_GENERIC_TZ",
      "field:android.icu.text.DateFormat#HOUR_MINUTE_TZ",
      "field:android.icu.text.DateFormat#HOUR_TZ",
      "field:android.icu.text.DateFormat#STANDALONE_MONTH",
      "field:android.icu.text.DateFormat#TIME_SKELETONS",
      "field:android.icu.text.DateFormat#ZONE_SKELETONS",
      "field:android.icu.text.DateFormatSymbols#DT_CONTEXT_COUNT",
      "field:android.icu.text.DateFormatSymbols#DT_WIDTH_COUNT",
      "field:android.icu.text.DateFormatSymbols#NUMERIC",
      "field:android.icu.text.DateTimePatternGenerator#MATCH_MINUTE_FIELD_LENGTH",
      "field:android.icu.text.DateTimePatternGenerator#MATCH_SECOND_FIELD_LENGTH",
      "field:android.icu.text.IDNA#ALLOW_UNASSIGNED",
      "field:android.icu.text.Normalizer#COMPARE_NORM_OPTIONS_SHIFT",
      "field:android.icu.text.Normalizer#COMPOSE",
      "field:android.icu.text.Normalizer#COMPOSE_COMPAT",
      "field:android.icu.text.Normalizer#DECOMP",
      "field:android.icu.text.Normalizer#DECOMP_COMPAT",
      "field:android.icu.text.Normalizer#DEFAULT",
      "field:android.icu.text.Normalizer#DONE",
      "field:android.icu.text.Normalizer#FCD",
      "field:android.icu.text.Normalizer#IGNORE_HANGUL",
      "field:android.icu.text.Normalizer#NFC",
      "field:android.icu.text.Normalizer#NFD",
      "field:android.icu.text.Normalizer#NFKC",
      "field:android.icu.text.Normalizer#NFKD",
      "field:android.icu.text.Normalizer#NONE",
      "field:android.icu.text.Normalizer#NO_OP",
      "field:android.icu.text.Normalizer#UNICODE_3_2",
      "field:android.icu.text.PluralRules#CATEGORY_SEPARATOR",
      "field:android.icu.text.PluralRules#KEYWORD_RULE_SEPARATOR",
      "field:android.icu.text.PluralRules$FixedDecimal#decimalDigits",
      "field:android.icu.text.PluralRules$FixedDecimal#decimalDigitsWithoutTrailingZeros",
      "field:android.icu.text.PluralRules$FixedDecimal#hasIntegerValue",
      "field:android.icu.text.PluralRules$FixedDecimal#integerValue",
      "field:android.icu.text.PluralRules$FixedDecimal#isNegative",
      "field:android.icu.text.PluralRules$FixedDecimal#source",
      "field:android.icu.text.PluralRules$FixedDecimal#visibleDecimalDigitCount",
      "field:android.icu.text.PluralRules$FixedDecimal#visibleDecimalDigitCountWithoutTrailingZeros",
      "field:android.icu.text.PluralRules$FixedDecimalRange#end",
      "field:android.icu.text.PluralRules$FixedDecimalRange#start",
      "field:android.icu.text.PluralRules$FixedDecimalSamples#bounded",
      "field:android.icu.text.PluralRules$FixedDecimalSamples#sampleType",
      "field:android.icu.text.PluralRules$FixedDecimalSamples#samples",
      "field:android.icu.text.PluralRules$StandardPluralCategories#COUNT",
      "field:android.icu.text.PluralRules$StandardPluralCategories#VALUES",
      "field:android.icu.text.UnicodeSetIterator#endElement",
      "field:android.icu.text.UnicodeSetIterator#nextElement",
      "field:android.icu.util.Calendar#WEEKDAY",
      "field:android.icu.util.Calendar#WEEKEND",
      "field:android.icu.util.Calendar#WEEKEND_CEASE",
      "field:android.icu.util.Calendar#WEEKEND_ONSET",
      "field:android.icu.util.LocaleData#DELIMITER_COUNT",
      "field:android.icu.util.LocaleData#ES_COUNT",
      "field:android.icu.util.LocaleData#ES_CURRENCY",
      "field:android.icu.util.MeasureUnit#subType",
      "field:android.icu.util.MeasureUnit#type",
      "field:android.icu.util.VersionInfo#ICU_DATA_VERSION",
      "field:android.icu.util.VersionInfo#ICU_DATA_VERSION_PATH",
      "field:android.icu.util.VersionInfo#UCOL_TAILORINGS_VERSION",
      "method:android.icu.lang.UCharacter#getCharFromName1_0(String)",
      "method:android.icu.lang.UCharacter#getISOComment(int)",
      "method:android.icu.lang.UCharacter#getName1_0(int)",
      "method:android.icu.lang.UCharacter#getName1_0Iterator()",
      "method:android.icu.lang.UCharacter#getPropertyValueEnumNoThrow(int,CharSequence)",
      "method:android.icu.lang.UCharacter#getStringPropertyValue(int,int,int)",
      "method:android.icu.lang.UCharacter#isJavaLetter(int)",
      "method:android.icu.lang.UCharacter#isJavaLetterOrDigit(int)",
      "method:android.icu.lang.UCharacter#isSpace(int)",
      "method:android.icu.lang.UCharacter#toTitleFirst(ULocale,String)",
      "method:android.icu.text.AlphabeticIndex#getFirstCharactersInScripts()",
      "method:android.icu.text.BreakIterator#getBreakInstance(ULocale,int)",
      "method:android.icu.text.CollationElementIterator#getRuleBasedCollator()",
      "method:android.icu.text.CollationElementIterator#hashCode()",
      "method:android.icu.text.Collator#doCompare(CharSequence,CharSequence)",
      "method:android.icu.text.Collator#setStrength2(int)",
      "method:android.icu.text.Collator#setVariableTop(String)",
      "method:android.icu.text.Collator#setVariableTop(int)",
      "method:android.icu.text.CompactDecimalFormat#CompactDecimalFormat(String,DecimalFormatSymbols,CompactStyle,PluralRules,long[],Map<String,String[][]>,Map<String,String[]>,Collection<String>)",
      "method:android.icu.text.CurrencyPluralInfo#hashCode()",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Calendar,Locale)",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Calendar,ULocale)",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Class<? extends Calendar>,Locale)",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Class<? extends Calendar>,ULocale)",
      "method:android.icu.text.DateFormatSymbols#getLeapMonthPattern(int,int)",
      "method:android.icu.text.DateFormatSymbols#initializeData(ULocale,CalendarData)",
      "method:android.icu.text.DateFormatSymbols#setLeapMonthPattern(String,int,int)",
      "method:android.icu.text.DateIntervalFormat#DateIntervalFormat(String,DateIntervalInfo,SimpleDateFormat)",
      "method:android.icu.text.DateIntervalFormat#getPatterns(Calendar,Calendar,Output<String>)",
      "method:android.icu.text.DateIntervalFormat#getRawPatterns()",
      "method:android.icu.text.DateIntervalFormat#parseObject(String,ParsePosition)",
      "method:android.icu.text.DateIntervalInfo#DateIntervalInfo()",
      "method:android.icu.text.DateIntervalInfo#genPatternInfo(String,boolean)",
      "method:android.icu.text.DateIntervalInfo#getPatterns()",
      "method:android.icu.text.DateIntervalInfo#getRawPatterns()",
      "method:android.icu.text.DateTimePatternGenerator#addPatternWithSkeleton(String,String,boolean,PatternInfo)",
      "method:android.icu.text.DateTimePatternGenerator#getAppendFormatNumber(String)",
      "method:android.icu.text.DateTimePatternGenerator#getCanonicalSkeletonAllowingDuplicates(String)",
      "method:android.icu.text.DateTimePatternGenerator#getDefaultHourFormatChar()",
      "method:android.icu.text.DateTimePatternGenerator#getFields(String)",
      "method:android.icu.text.DateTimePatternGenerator#getFrozenInstance(ULocale)",
      "method:android.icu.text.DateTimePatternGenerator#getRedundants(Collection<String>)",
      "method:android.icu.text.DateTimePatternGenerator#getSkeletonAllowingDuplicates(String)",
      "method:android.icu.text.DateTimePatternGenerator#isSingleField(String)",
      "method:android.icu.text.DateTimePatternGenerator#setDefaultHourFormatChar(char)",
      "method:android.icu.text.DateTimePatternGenerator#skeletonsAreSimilar(String,String)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#FormatParser()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#getItems()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#hasDateAndTimeFields()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#quoteLiteral(String)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#set(String)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#set(String,boolean)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#toString()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#toString(int,int)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#VariableField(String)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#VariableField(String,boolean)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#getCanonicalCode(int)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#getType()",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#isNumeric()",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#toString()",
      "method:android.icu.text.DecimalFormat#getEffectiveCurrency()",
      "method:android.icu.text.DecimalFormatSymbols#getMinusString()",
      "method:android.icu.text.DecimalFormatSymbols#getPlusString()",
      "method:android.icu.text.IDNA#IDNA()",
      "method:android.icu.text.IDNA#addError(Info,Error)",
      "method:android.icu.text.IDNA#addLabelError(Info,Error)",
      "method:android.icu.text.IDNA#compare(String,String,int)",
      "method:android.icu.text.IDNA#compare(StringBuffer,StringBuffer,int)",
      "method:android.icu.text.IDNA#compare(UCharacterIterator,UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertIDNToASCII(String,int)",
      "method:android.icu.text.IDNA#convertIDNToASCII(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertIDNToASCII(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertIDNToUnicode(String,int)",
      "method:android.icu.text.IDNA#convertIDNToUnicode(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertIDNToUnicode(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertToASCII(String,int)",
      "method:android.icu.text.IDNA#convertToASCII(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertToASCII(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertToUnicode(String,int)",
      "method:android.icu.text.IDNA#convertToUnicode(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertToUnicode(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#hasCertainErrors(Info,EnumSet<Error>)",
      "method:android.icu.text.IDNA#hasCertainLabelErrors(Info,EnumSet<Error>)",
      "method:android.icu.text.IDNA#isBiDi(Info)",
      "method:android.icu.text.IDNA#isOkBiDi(Info)",
      "method:android.icu.text.IDNA#promoteAndResetLabelErrors(Info)",
      "method:android.icu.text.IDNA#resetInfo(Info)",
      "method:android.icu.text.IDNA#setBiDi(Info)",
      "method:android.icu.text.IDNA#setNotOkBiDi(Info)",
      "method:android.icu.text.IDNA#setTransitionalDifferent(Info)",
      "method:android.icu.text.LocaleDisplayNames#LocaleDisplayNames()",
      "method:android.icu.text.LocaleDisplayNames#scriptDisplayNameInContext(String)",
      "method:android.icu.text.MeasureFormat#formatMeasureRange(Measure,Measure)",
      "method:android.icu.text.MeasureFormat#getRangeFormat(ULocale,FormatWidth)",
      "method:android.icu.text.MeasureFormat#getRangePattern(ULocale,FormatWidth)",
      "method:android.icu.text.Normalizer#Normalizer(CharacterIterator,Mode,int)",
      "method:android.icu.text.Normalizer#Normalizer(String,Mode,int)",
      "method:android.icu.text.Normalizer#Normalizer(UCharacterIterator,Mode,int)",
      "method:android.icu.text.Normalizer#clone()",
      "method:android.icu.text.Normalizer#compose(String,boolean)",
      "method:android.icu.text.Normalizer#compose(String,boolean,int)",
      "method:android.icu.text.Normalizer#compose(char[],char[],boolean,int)",
      "method:android.icu.text.Normalizer#compose(char[],int,int,char[],int,int,boolean,int)",
      "method:android.icu.text.Normalizer#concatenate(String,String,Mode,int)",
      "method:android.icu.text.Normalizer#concatenate(char[],char[],Mode,int)",
      "method:android.icu.text.Normalizer#concatenate(char[],int,int,char[],int,int,char[],int,int,Normalizer.Mode,int)",
      "method:android.icu.text.Normalizer#current()",
      "method:android.icu.text.Normalizer#decompose(String,boolean)",
      "method:android.icu.text.Normalizer#decompose(String,boolean,int)",
      "method:android.icu.text.Normalizer#decompose(char[],char[],boolean,int)",
      "method:android.icu.text.Normalizer#decompose(char[],int,int,char[],int,int,boolean,int)",
      "method:android.icu.text.Normalizer#endIndex()",
      "method:android.icu.text.Normalizer#first()",
      "method:android.icu.text.Normalizer#getBeginIndex()",
      "method:android.icu.text.Normalizer#getEndIndex()",
      "method:android.icu.text.Normalizer#getFC_NFKC_Closure(int)",
      "method:android.icu.text.Normalizer#getFC_NFKC_Closure(int,char[])",
      "method:android.icu.text.Normalizer#getIndex()",
      "method:android.icu.text.Normalizer#getLength()",
      "method:android.icu.text.Normalizer#getMode()",
      "method:android.icu.text.Normalizer#getOption(int)",
      "method:android.icu.text.Normalizer#getText()",
      "method:android.icu.text.Normalizer#getText(char[])",
      "method:android.icu.text.Normalizer#isNormalized(String,Mode,int)",
      "method:android.icu.text.Normalizer#isNormalized(char[],int,int,Mode,int)",
      "method:android.icu.text.Normalizer#isNormalized(int,Mode,int)",
      "method:android.icu.text.Normalizer#last()",
      "method:android.icu.text.Normalizer#next()",
      "method:android.icu.text.Normalizer#normalize(String,Mode)",
      "method:android.icu.text.Normalizer#normalize(String,Mode,int)",
      "method:android.icu.text.Normalizer#normalize(char[],char[],Mode,int)",
      "method:android.icu.text.Normalizer#normalize(char[],int,int,char[],int,int,Mode,int)",
      "method:android.icu.text.Normalizer#normalize(int,Mode)",
      "method:android.icu.text.Normalizer#normalize(int,Mode,int)",
      "method:android.icu.text.Normalizer#previous()",
      "method:android.icu.text.Normalizer#quickCheck(String,Mode)",
      "method:android.icu.text.Normalizer#quickCheck(String,Mode,int)",
      "method:android.icu.text.Normalizer#quickCheck(char[],Mode,int)",
      "method:android.icu.text.Normalizer#quickCheck(char[],int,int,Mode,int)",
      "method:android.icu.text.Normalizer#reset()",
      "method:android.icu.text.Normalizer#setIndex(int)",
      "method:android.icu.text.Normalizer#setIndexOnly(int)",
      "method:android.icu.text.Normalizer#setMode(Mode)",
      "method:android.icu.text.Normalizer#setOption(int,boolean)",
      "method:android.icu.text.Normalizer#setText(CharacterIterator)",
      "method:android.icu.text.Normalizer#setText(String)",
      "method:android.icu.text.Normalizer#setText(StringBuffer)",
      "method:android.icu.text.Normalizer#setText(UCharacterIterator)",
      "method:android.icu.text.Normalizer#setText(char[])",
      "method:android.icu.text.Normalizer#startIndex()",
      "method:android.icu.text.Normalizer$Mode#Mode()",
      "method:android.icu.text.Normalizer$Mode#getNormalizer2(int)",
      "method:android.icu.text.Normalizer2#Normalizer2()",
      "method:android.icu.text.NumberFormat#getEffectiveCurrency()",
      "method:android.icu.text.NumberFormat#getPattern(Locale,int)",
      "method:android.icu.text.PluralFormat#setLocale(ULocale)",
      "method:android.icu.text.PluralRules#addSample(String,Number,int,Set<Double>)",
      "method:android.icu.text.PluralRules#compareTo(PluralRules)",
      "method:android.icu.text.PluralRules#computeLimited(String,SampleType)",
      "method:android.icu.text.PluralRules#getAllKeywordValues(String,SampleType)",
      "method:android.icu.text.PluralRules#getDecimalSamples(String,SampleType)",
      "method:android.icu.text.PluralRules#getKeywordStatus(String,int,Set<Double>,Output<Double>,SampleType)",
      "method:android.icu.text.PluralRules#getRules(String)",
      "method:android.icu.text.PluralRules#getSamples(String,SampleType)",
      "method:android.icu.text.PluralRules#hashCode()",
      "method:android.icu.text.PluralRules#isLimited(String)",
      "method:android.icu.text.PluralRules#isLimited(String,SampleType)",
      "method:android.icu.text.PluralRules#matches(FixedDecimal,String)",
      "method:android.icu.text.PluralRules#select(FixedDecimal)",
      "method:android.icu.text.PluralRules#select(double,int,long)",
      "method:android.icu.text.PluralRules$Factory#Factory()",
      "method:android.icu.text.PluralRules$Factory#forLocale(ULocale)",
      "method:android.icu.text.PluralRules$Factory#forLocale(ULocale,PluralType)",
      "method:android.icu.text.PluralRules$Factory#getAvailableULocales()",
      "method:android.icu.text.PluralRules$Factory#getDefaultFactory()",
      "method:android.icu.text.PluralRules$Factory#getFunctionalEquivalent(ULocale,boolean[])",
      "method:android.icu.text.PluralRules$Factory#hasOverride(ULocale)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(String)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(double)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(double,int)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(double,int,long)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(long)",
      "method:android.icu.text.PluralRules$FixedDecimal#compareTo(FixedDecimal)",
      "method:android.icu.text.PluralRules$FixedDecimal#decimals(double)",
      "method:android.icu.text.PluralRules$FixedDecimal#doubleValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#equals(Object)",
      "method:android.icu.text.PluralRules$FixedDecimal#floatValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#get(Operand)",
      "method:android.icu.text.PluralRules$FixedDecimal#getBaseFactor()",
      "method:android.icu.text.PluralRules$FixedDecimal#getDecimalDigits()",
      "method:android.icu.text.PluralRules$FixedDecimal#getDecimalDigitsWithoutTrailingZeros()",
      "method:android.icu.text.PluralRules$FixedDecimal#getIntegerValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#getOperand(String)",
      "method:android.icu.text.PluralRules$FixedDecimal#getShiftedValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#getSource()",
      "method:android.icu.text.PluralRules$FixedDecimal#getVisibleDecimalDigitCount()",
      "method:android.icu.text.PluralRules$FixedDecimal#getVisibleDecimalDigitCountWithoutTrailingZeros()",
      "method:android.icu.text.PluralRules$FixedDecimal#hasIntegerValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#hashCode()",
      "method:android.icu.text.PluralRules$FixedDecimal#intValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#isHasIntegerValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#isNegative()",
      "method:android.icu.text.PluralRules$FixedDecimal#longValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#toString()",
      "method:android.icu.text.PluralRules$FixedDecimalRange#FixedDecimalRange(FixedDecimal,FixedDecimal)",
      "method:android.icu.text.PluralRules$FixedDecimalRange#toString()",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#addSamples(Set<Double>)",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#getSamples()",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#getStartEndSamples(Set<FixedDecimal>)",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#toString()",
      "method:android.icu.text.RuleBasedCollator#doCompare(CharSequence,CharSequence)",
      "method:android.icu.text.RuleBasedCollator#internalGetCEs(CharSequence)",
      "method:android.icu.text.RuleBasedCollator#isHiraganaQuaternary()",
      "method:android.icu.text.RuleBasedCollator#setHiraganaQuaternary(boolean)",
      "method:android.icu.text.RuleBasedCollator#setHiraganaQuaternaryDefault()",
      "method:android.icu.text.RuleBasedCollator#setVariableTop(String)",
      "method:android.icu.text.RuleBasedCollator#setVariableTop(int)",
      "method:android.icu.text.SearchIterator#setMatchNotFound()",
      "method:android.icu.text.SimpleDateFormat#SimpleDateFormat(String,DateFormatSymbols,ULocale)",
      "method:android.icu.text.SimpleDateFormat#getInstance(Calendar.FormatConfiguration)",
      "method:android.icu.text.SimpleDateFormat#intervalFormatByAlgorithm(Calendar,Calendar,StringBuffer,FieldPosition)",
      "method:android.icu.text.SimpleDateFormat#subFormat(StringBuffer,char,int,int,int,DisplayContext,FieldPosition,Calendar)",
      "method:android.icu.text.SimpleDateFormat#subFormat(char,int,int,int,DisplayContext,FieldPosition,Calendar)",
      "method:android.icu.text.SimpleDateFormat#zeroPaddingNumber(NumberFormat,StringBuffer,int,int,int)",
      "method:android.icu.text.StringPrepParseException#hashCode()",
      "method:android.icu.text.StringSearch#setMatchNotFound()",
      "method:android.icu.text.TimeZoneNames#getDisplayNames(String,NameType[],long,String[],int)",
      "method:android.icu.text.TimeZoneNames#loadAllDisplayNames()",
      "method:android.icu.text.TimeZoneNames$Factory#Factory()",
      "method:android.icu.text.TimeZoneNames$Factory#getTimeZoneNames(ULocale)",
      "method:android.icu.text.UnicodeFilter#UnicodeFilter()",
      "method:android.icu.text.UnicodeSet#addBridges(UnicodeSet)",
      "method:android.icu.text.UnicodeSet#applyPattern(String,ParsePosition,SymbolTable,int)",
      "method:android.icu.text.UnicodeSet#compare(Iterator<T>,Iterator<T>)",
      "method:android.icu.text.UnicodeSet#findIn(CharSequence,int,boolean)",
      "method:android.icu.text.UnicodeSet#findLastIn(CharSequence,int,boolean)",
      "method:android.icu.text.UnicodeSet#getDefaultXSymbolTable()",
      "method:android.icu.text.UnicodeSet#getRegexEquivalent()",
      "method:android.icu.text.UnicodeSet#getSingleCodePoint(CharSequence)",
      "method:android.icu.text.UnicodeSet#matchesAt(CharSequence,int)",
      "method:android.icu.text.UnicodeSet#setDefaultXSymbolTable(XSymbolTable)",
      "method:android.icu.text.UnicodeSet#spanAndCount(CharSequence,int,SpanCondition,OutputInt)",
      "method:android.icu.text.UnicodeSet#stripFrom(CharSequence,boolean)",
      "method:android.icu.text.UnicodeSetIterator#getSet()",
      "method:android.icu.text.UnicodeSetIterator#loadRange(int)",
      "method:android.icu.text.Transliterator#addSourceTargetSet(UnicodeSet,UnicodeSet,UnicodeSet)",
      "method:android.icu.text.Transliterator#getFilterAsUnicodeSet(UnicodeSet)",
      "method:android.icu.text.Transliterator#registerAny()",
      "method:android.icu.util.Calendar#getDateTimePattern(Calendar,ULocale,int)",
      "method:android.icu.util.Calendar#getDayOfWeekType(int)",
      "method:android.icu.util.Calendar#getRelatedYear()",
      "method:android.icu.util.Calendar#getWeekendTransition(int)",
      "method:android.icu.util.Calendar#haveDefaultCentury()",
      "method:android.icu.util.Calendar#setRelatedYear(int)",
      "method:android.icu.util.Calendar$FormatConfiguration#getCalendar()",
      "method:android.icu.util.Calendar$FormatConfiguration#getDateFormatSymbols()",
      "method:android.icu.util.Calendar$FormatConfiguration#getLocale()",
      "method:android.icu.util.Calendar$FormatConfiguration#getOverrideString()",
      "method:android.icu.util.Calendar$FormatConfiguration#getPatternString()",
      "method:android.icu.util.ChineseCalendar#ChineseCalendar(TimeZone,ULocale,int,TimeZone)",
      "method:android.icu.util.ChineseCalendar#haveDefaultCentury()",
      "method:android.icu.util.CopticCalendar#getJDEpochOffset()",
      "method:android.icu.util.CopticCalendar#handleComputeFields(int)",
      "method:android.icu.util.CopticCalendar#handleGetExtendedYear()",
      "method:android.icu.util.Currency#parse(ULocale,String,int,ParsePosition)",
      "method:android.icu.util.HebrewCalendar#isLeapYear(int)",
      "method:android.icu.util.HebrewCalendar#validateField(int)",
      "method:android.icu.util.JapaneseCalendar#haveDefaultCentury()",
      "method:android.icu.util.MeasureUnit#MeasureUnit(String,String)",
      "method:android.icu.util.MeasureUnit#addUnit(String,String,Factory)",
      "method:android.icu.util.MeasureUnit#internalGetInstance(String,String)",
      "method:android.icu.util.MeasureUnit#resolveUnitPerUnit(MeasureUnit,MeasureUnit)",
      "method:android.icu.util.MeasureUnit$Factory#create(String,String)",
      "method:android.icu.util.TimeZone#TimeZone(String)",
      "method:android.icu.util.ULocale#getDisplayScriptInContext()",
      "method:android.icu.util.ULocale#getDisplayScriptInContext(String,String)",
      "method:android.icu.util.ULocale#getDisplayScriptInContext(String,ULocale)",
      "method:android.icu.util.ULocale#getDisplayScriptInContext(ULocale)",
      "method:android.icu.util.ULocale#minimizeSubtags(ULocale,Minimize)",
      "method:android.icu.util.VersionInfo#getVersionString(int,int)",
      "method:android.icu.util.VersionInfo#javaVersion()",
      "type:android.icu.text.DateTimePatternGenerator$FormatParser",
      "type:android.icu.text.DateTimePatternGenerator$VariableField",
      "type:android.icu.text.Normalizer$Mode",
      "type:android.icu.text.PluralRules$Factory",
      "type:android.icu.text.PluralRules$FixedDecimal",
      "type:android.icu.text.PluralRules$FixedDecimalRange",
      "type:android.icu.text.PluralRules$FixedDecimalSamples",
      "type:android.icu.text.PluralRules$SampleType",
      "type:android.icu.text.PluralRules$StandardPluralCategories",
      "type:android.icu.text.TimeZoneNames$Factory",
      "type:android.icu.util.Calendar$FormatConfiguration",
      "type:android.icu.util.MeasureUnit$Factory",
      "type:android.icu.util.ULocale$Minimize",
  };

  /**
   * A set of declarations we don't want to expose in Android.
   * We generally hide:
   * Any API we find that relates to a final static primitive that a compiler could inline
   * and could change between ICU releases.
   * Methods that relate to registration of static defaults / factories, which cannot be
   * configured on Android "before use", because a lot of initialization happens in the zygote.
   */
  private static final String[] DECLARATIONS_TO_HIDE = {
      /* ASCII order please. */
      "field:android.icu.lang.UCharacter$BidiPairedBracketType#COUNT",
      "field:android.icu.lang.UCharacter$DecompositionType#COUNT",
      "field:android.icu.lang.UCharacter$EastAsianWidth#COUNT",
      "field:android.icu.lang.UCharacter$GraphemeClusterBreak#COUNT",
      "field:android.icu.lang.UCharacter$HangulSyllableType#COUNT",
      "field:android.icu.lang.UCharacter$JoiningGroup#COUNT",
      "field:android.icu.lang.UCharacter$JoiningType#COUNT",
      "field:android.icu.lang.UCharacter$LineBreak#COUNT",
      "field:android.icu.lang.UCharacter$NumericType#COUNT",
      "field:android.icu.lang.UCharacter$SentenceBreak#COUNT",
      "field:android.icu.lang.UCharacter$UnicodeBlock#COUNT",
      "field:android.icu.lang.UCharacter$WordBreak#COUNT",
      "field:android.icu.lang.UCharacterEnums$ECharacterCategory#CHAR_CATEGORY_COUNT",
      "field:android.icu.lang.UCharacterEnums$ECharacterDirection#CHAR_DIRECTION_COUNT",
      "field:android.icu.lang.UProperty#BINARY_LIMIT",
      "field:android.icu.lang.UProperty#DOUBLE_LIMIT",
      "field:android.icu.lang.UProperty#INT_LIMIT",
      "field:android.icu.lang.UProperty#MASK_LIMIT",
      "field:android.icu.lang.UProperty#OTHER_PROPERTY_LIMIT",
      "field:android.icu.lang.UProperty#STRING_LIMIT",
      "field:android.icu.lang.UProperty$NameChoice#COUNT",
      "field:android.icu.lang.UScript#CODE_LIMIT",
      "field:android.icu.text.BidiClassifier#context",
      "field:android.icu.text.CollationKey$BoundMode#COUNT",
      "field:android.icu.text.Collator$ReorderCodes#LIMIT",
      "field:android.icu.text.DateFormat#FIELD_COUNT",
      "field:android.icu.text.DateTimePatternGenerator#TYPE_LIMIT",
      "field:android.icu.util.LocaleData#ES_AUXILIARY",
      "field:android.icu.util.LocaleData#ES_INDEX",
      "field:android.icu.util.LocaleData#ES_PUNCTUATION",
      "field:android.icu.util.LocaleData#ES_STANDARD",
      // Hide new measure units until a clear use case on Android is found.
      // The cost of these APIs is the storage due to the additional locale data embedded
      // in the ICU data file.
      "field:android.icu.util.MeasureUnit#DUNAM",
      "field:android.icu.util.MeasureUnit#MOLE",
      "field:android.icu.util.MeasureUnit#PERMYRIAD",
      "field:android.icu.util.MeasureUnit#DAY_PERSON",
      "field:android.icu.util.MeasureUnit#MONTH_PERSON",
      "field:android.icu.util.MeasureUnit#WEEK_PERSON",
      "field:android.icu.util.MeasureUnit#YEAR_PERSON",
      "field:android.icu.util.MeasureUnit#BRITISH_THERMAL_UNIT",
      "field:android.icu.util.MeasureUnit#ELECTRONVOLT",
      "field:android.icu.util.MeasureUnit#NEWTON",
      "field:android.icu.util.MeasureUnit#POUND_FORCE",
      "field:android.icu.util.MeasureUnit#SOLAR_RADIUS",
      "field:android.icu.util.MeasureUnit#SOLAR_LUMINOSITY",
      "field:android.icu.util.MeasureUnit#DALTON",
      "field:android.icu.util.MeasureUnit#EARTH_MASS",
      "field:android.icu.util.MeasureUnit#SOLAR_MASS",
      "field:android.icu.util.MeasureUnit#KILOPASCAL",
      "field:android.icu.util.MeasureUnit#MEGAPASCAL",
      "field:android.icu.util.MeasureUnit#NEWTON_METER",
      "field:android.icu.util.MeasureUnit#POUND_FOOT",
      "field:android.icu.util.MeasureUnit#BARREL",
      "field:android.icu.util.MeasureUnit#FLUID_OUNCE_IMPERIAL",
      "field:android.icu.util.MeasureUnit#BAR",
      "field:android.icu.util.MeasureUnit#PASCAL",
      "field:android.icu.util.MeasureUnit#THERM_US",
      "field:android.icu.util.MeasureUnit#EARTH_RADIUS",
      "field:android.icu.util.MeasureUnit#GRAIN",
      "field:android.icu.util.MeasureUnit#DESSERT_SPOON",
      "field:android.icu.util.MeasureUnit#DESSERT_SPOON_IMPERIAL",
      "field:android.icu.util.MeasureUnit#DRAM",
      "field:android.icu.util.MeasureUnit#DROP",
      "field:android.icu.util.MeasureUnit#JIGGER",
      "field:android.icu.util.MeasureUnit#PINCH",
      "field:android.icu.util.MeasureUnit#QUART_IMPERIAL",
      // Skeleton syntax can evolve over time. Currently, the skeleton APIs are not prioritized to
      // be public. Android developers could easily miss the API version check for new syntax and
      // cause app crashing on older devices.
      // See the syntax details in https://github.com/unicode-org/icu/blob/master/docs/userguide/format_parse/numbers/skeletons.md.
      "method:android.icu.number.NumberFormatterSettings#toSkeleton()",
      "method:android.icu.number.NumberFormatter#forSkeleton(String)",
      "method:android.icu.text.BreakIterator#registerInstance(BreakIterator,Locale,int)",
      "method:android.icu.text.BreakIterator#registerInstance(BreakIterator,ULocale,int)",
      "method:android.icu.text.BreakIterator#unregister(Object)",
      "method:android.icu.text.CollationKey#CollationKey(String,RawCollationKey)",
      "method:android.icu.text.Collator#getRawCollationKey(String,RawCollationKey)",
      "method:android.icu.text.Collator#registerFactory(CollatorFactory)",
      "method:android.icu.text.Collator#registerInstance(Collator,ULocale)",
      "method:android.icu.text.Collator#unregister(Object)",
      "method:android.icu.text.DecimalFormat#toNumberFormatter()",
      "method:android.icu.text.DecimalFormatSymbols#getCurrencyPattern()",
      "method:android.icu.text.NumberFormat#registerFactory(NumberFormatFactory)",
      "method:android.icu.text.NumberFormat#unregister(Object)",
      "method:android.icu.text.RuleBasedCollator#getRawCollationKey(String,RawCollationKey)",
      "method:android.icu.text.UnicodeSet#addAllTo(Iterable<T>,T[])",
      "method:android.icu.text.UnicodeSet#addAllTo(Iterable<T>,U)",
      "method:android.icu.text.UnicodeSet#addAllTo(String[])",
      "method:android.icu.text.UnicodeSet#compare(CharSequence,int)",
      "method:android.icu.text.UnicodeSet#compare(Collection<T>,Collection<T>,ComparisonStyle)",
      "method:android.icu.text.UnicodeSet#compare(Iterable<T>,Iterable<T>)",
      "method:android.icu.text.UnicodeSet#compare(int,CharSequence)",
      "method:android.icu.text.UnicodeSet#resemblesPattern(String,int)",
      "method:android.icu.text.UnicodeSet#toArray(UnicodeSet)",
      "method:android.icu.text.Transliterator#Transliterator(String,UnicodeFilter)",
      "method:android.icu.text.Transliterator#baseToRules(boolean)",
      "method:android.icu.text.Transliterator#handleGetSourceSet()",
      "method:android.icu.text.Transliterator#handleTransliterate(Replaceable,Position,boolean)",
      "method:android.icu.text.Transliterator#registerAlias(String,String)",
      "method:android.icu.text.Transliterator#registerClass(String,Class<? extends Transliterator>,String)",
      "method:android.icu.text.Transliterator#registerFactory(String,Factory)",
      "method:android.icu.text.Transliterator#registerInstance(Transliterator)",
      "method:android.icu.text.Transliterator#transform(String)",
      "method:android.icu.text.Transliterator#unregister(String)",
      "method:android.icu.text.Transliterator#setID(String)",
      "method:android.icu.text.Transliterator#setMaximumContextLength(int)",
      "method:android.icu.util.CECalendar#ceToJD(long,int,int,int)",
      "method:android.icu.util.CECalendar#getJDEpochOffset()",
      "method:android.icu.util.CECalendar#jdToCE(int,int,int[])",
      "method:android.icu.util.Currency#registerInstance(Currency,ULocale)",
      "method:android.icu.util.Currency#unregister(Object)",
      "method:android.icu.util.IslamicCalendar#isCivil()",
      "method:android.icu.util.IslamicCalendar#setCivil(boolean)",
      "method:android.icu.util.LocaleData#getExemplarSet(int,int)",
      "method:android.icu.util.LocaleData#getExemplarSet(ULocale,int)",
      "method:android.icu.util.LocaleData#getExemplarSet(ULocale,int,int)",
      "method:android.icu.util.LocaleData#getLocaleDisplayPattern()",
      "method:android.icu.util.LocaleData#getLocaleSeparator()",
      "method:android.icu.util.TimeZone#clearCachedDefault()",
      "method:android.icu.util.TimeZone#getDefaultTimeZoneType()",
      "method:android.icu.util.TimeZone#setDefault(TimeZone)",
      "method:android.icu.util.TimeZone#setDefaultTimeZoneType(int)",
      "method:android.icu.util.ULocale#setDefault(Category,ULocale)",
      "method:android.icu.util.ULocale#setDefault(ULocale)",
      "method:android.icu.util.VersionInfo#main(String[])",
      "type:android.icu.text.Collator$CollatorFactory",
      "type:android.icu.text.NumberFormat$NumberFormatFactory",
      "type:android.icu.text.NumberFormat$SimpleNumberFormatFactory",
  };

  /**
   * ICU APIs that are in the Android SDK API but are deprecated on Android and not deprecated in
   * ICU. Entries can be removed if ICU also decide to deprecate.
   * Entries are usually the result of Android mistakenly exposing an API, an ICU API problem,
   * and/or ICU's stability guarantees differing from Android's requirements.
   */
  private static final Map<String, String> ANDROID_DEPRECATED_SET = Map.of(
      /* ASCII order please. */

      // Unstable "constant" value - different values in different API levels. http://b/77850660.
      "field:android.icu.util.JapaneseCalendar#CURRENT_ERA",
          "Use era constants, e.g. {@link #REIWA}, instead.",
      // The method reads unstable .nrm file format. http://b/173821060
      "method:android.icu.text.Normalizer2#getInstance(InputStream,String,Mode)",
          "Don't use because the binary {@code data} format is not stable across API levels."
  );

  /**
   * ICU APIs that are in the Android SDK API but are removed on Android and @stable in ICU.
   * Entries can be removed if ICU also decide to remove the APIs.
   * Entries are usually the result of Android mistakenly exposing an API, an ICU API problem,
   * and/or ICU's stability guarantees differing from Android's requirements.
   */
  private static final String[] ANDROID_REMOVED_SET = {
      /* ASCII order please. */
      // Unstable "constant" value - different values in different API levels. http://b/77850660.
      "field:android.icu.util.JapaneseCalendar#CURRENT_ERA",
  };

  // The declarations with JavaDocs that have @.jcite tags that should be transformed to doclava
  // @sample tags. Ones not on this list will just be escaped and could show up in the generated
  // docs. It is assumed that the complete set of ones that should appear in the public API are
  // listed below and it's ok to escape those that are not.
  private static final String[] JCITE_TRANSFORM_SET = {
      "method:android.icu.text.DateIntervalFormat#getInstance(String,Locale)",
      "method:android.icu.text.DateIntervalFormat#getInstance(String,Locale,DateIntervalInfo)",
      "method:android.icu.text.DateTimePatternGenerator#addPattern(String,boolean,PatternInfo)",
      "method:android.icu.text.DateTimePatternGenerator#getBestPattern(String)",
      "method:android.icu.text.DateTimePatternGenerator#replaceFieldTypes(String,String)",
      "method:android.icu.text.PluralFormat#PluralFormat(ULocale,String)",
  };

  private static final String[] DEFAULT_CONSTRUCTORS = {
      "android.icu.text.DateTimePatternGenerator$DistanceInfo",
      "android.icu.text.SpoofChecker$ScriptSet",
      "android.icu.text.TimeZoneNames$DefaultTimeZoneNames$FactoryImpl",
  };

  // Metalava disallows hidden abstract methods in public abstract classes because classes
  // inheriting such class can cause runtime crashes instead of a compile error. Due to historic
  // reason, we have such classes in the public SDK. This is the allowlist of hidden abstract
  // methods, and @SuppressWarnings("HiddenAbstractMethod") annotation will be added into those
  // methods. See http://b/147445486 for more details.
  private static final String[] HIDDEN_ABSTRACT_METHODS = {
      "method:android.icu.text.Collator#getRawCollationKey(String,RawCollationKey)",
      "method:android.icu.text.Collator#setVariableTop(String)",
      "method:android.icu.text.Collator#setVariableTop(int)",
  };

  public static final String ANDROID_ICU4J_SAMPLE_DIR =
      "external/icu/android_icu4j/src/samples/java";

  private static final boolean DEBUG = false;

  static final String ORIGINAL_ICU_PACKAGE = "com.ibm.icu";
  static final String ANDROID_ICU_PACKAGE = "android.icu";

  private Icu4jTransform() {
  }

  /**
   * Usage: See {@link Icu4jRules#COMMAND_USAGE}
   *
   * The option --hide-non-allowlisted-api can be used to explicitly describe the API surface to be
   * exposed; anything not in the list will be hidden in additional to other rules. This is useful
   * when upgrading ICU when we haven't yet added new classes/methods to various hard-coded lists
   * described below.
   *
   * This tool hides the following in the knowledge that classes that are not explicitly
   * hidden are exposed in the public API set:
   *
   * 1) Public classes that are not in the PUBLIC_API_CLASSES list.
   * 2) Types / fields / methods that were deprecated when the class was first exposed in Android,
   *    listed in INITIAL_DEPRECATED_SET
   * 3) Types / fields / methods that we explicitly want to hide, listed in DECLARATIONS_TO_HIDE
   * 4) Types / fields / methods that are flagged with ICU javadoc as draft / provisional or
   *    internal.
   * 5) If the --hide-non-allowlisted-api option is provided, types / fields / methods that are not
   *    in the allowlisted-api-file.
   */
  public static void main(String[] args) throws Exception {
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
    options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");

    new Main(DEBUG)
        .setJdtOptions(options)
        .execute(new Icu4jRules(args));
  }

  static class Icu4jRules implements RuleSet {

    private static final String SOURCE_CODE_HEADER = "/* GENERATED SOURCE. DO NOT MODIFY. */\n";
    private static final String COMMAND_USAGE = "Usage: " + Icu4jTransform.class.getCanonicalName()
            + " [--hide-non-allowlisted-api <allowlisted-api-file>]"
            + " [--flagged-api <flagged-api-file>]"
            + " <source-dir>+ <target-dir> <core-platform-api-file> <intra-core-api-file>"
            + " <unsupported-app-usage-file>";

    private final InputFileGenerator inputFileGenerator;
    private final List<Rule> rules;
    private final BasicOutputSourceFileGenerator outputSourceFileGenerator;

    public Icu4jRules(String[] args) {
      Path allowlistedApiPath = null;
      if ("--hide-non-allowlisted-api".equals(args[0])) {
        allowlistedApiPath = Paths.get(args[1]);
        String[] newArgs = new String[args.length - 2];
        System.arraycopy(args, 2, newArgs, 0, args.length - 2);
        args = newArgs;
      }

      Path flaggedApiListPath = null;
      if ("--flagged-api".equals(args[0])) {
        flaggedApiListPath = Paths.get(args[1]);
        String[] newArgs = new String[args.length - 2];
        System.arraycopy(args, 2, newArgs, 0, args.length - 2);
        args = newArgs;
      }

      if (args.length < 5) {
          throw new IllegalArgumentException(COMMAND_USAGE);
      }

      // Extract the source directories.
      String[] inputDirNames = new String[args.length - 4];
      System.arraycopy(args, 0, inputDirNames, 0, args.length - 4);
      inputFileGenerator = Icu4jTransformRules.createInputFileGenerator(inputDirNames);

      // Extract the additional arguments.
      int argIndex = inputDirNames.length;
      String targetDir = args[argIndex++];
      Path corePlatformApiFile = Paths.get(args[argIndex++]);
      Path intraCoreApiFile = Paths.get(args[argIndex++]);
      Path unsupportedAppUsageFile = Paths.get(args[argIndex++]);

      // Ensure that all the arguments were used.
      if (argIndex != args.length) {
        throw new IllegalArgumentException(COMMAND_USAGE);
      }

      rules = createTransformRules(corePlatformApiFile, intraCoreApiFile,
          unsupportedAppUsageFile, allowlistedApiPath, flaggedApiListPath);
      outputSourceFileGenerator = Icu4jTransformRules.createOutputFileGenerator(targetDir);
    }

    @Override
    public List<Rule> getRuleList(File ignored) {
      return rules;
    }

    @Override
    public InputFileGenerator getInputFileGenerator() {
      return inputFileGenerator;
    }

    @Override
    public OutputSourceFileGenerator getOutputSourceFileGenerator() {
      return outputSourceFileGenerator;
    }

    // Rules for migrating com.ibm.icu source over to android.icu. Pulled out separately so they
    // can be used for modifying ICU4J sample code as well.
    static Rule[] getRepackagingRules() {
      return new Rule[] {
          // Doc change: Insert a warning about the source code being generated.
          createMandatoryRule(new InsertHeader(SOURCE_CODE_HEADER)),
          // AST change: Change the package of each CompilationUnit from com.ibm.icu to android.icu.
          createMandatoryRule(new RenamePackage(ORIGINAL_ICU_PACKAGE, ANDROID_ICU_PACKAGE)),
          // AST change: Change all qualified names in code and javadoc.
          createOptionalRule(new ModifyQualifiedNames(ORIGINAL_ICU_PACKAGE, ANDROID_ICU_PACKAGE)),
          // AST change: Change all string literals containing package names in code.
          createOptionalRule(new ModifyStringLiterals(ORIGINAL_ICU_PACKAGE, ANDROID_ICU_PACKAGE)),
          // AST change: Change all string literals containing paths in code.
          createOptionalRule(new ModifyStringLiterals("com/ibm/icu", "android/icu")),
      };
    }

    private static List<Rule> createTransformRules(Path corePlatformApiFile,
            Path intraCoreApiFile,
            Path unsupportedAppUsagePath,
            Path allowlistedApiPath, Path flaggedApiListPath) {
      // The rules needed to repackage source code that declares or references com.ibm.icu code
      // so it references android.icu instead.
      Rule[] repackageRules = getRepackagingRules();

      // The rules needed to fix up Android's documentation rules.
      Rule[] apiDocsRules = new Rule[] {
          // Below are the fixes that ensure the Android API documentation generation can be run
          // over the source.

          // Doc change: Switch all documentation references from com.ibm.icu to android.icu.
          // e.g. importantly in <code> blocks and unimportantly in non-Javadoc comments.
          createOptionalRule(
              new ReplaceTextCommentScanner(ORIGINAL_ICU_PACKAGE, ANDROID_ICU_PACKAGE)),

          // AST change: Hide all ICU public classes except those in the PUBLIC_API_CLASSES
          // allowlist.
          createHidePublicClassesRule(),

          // AST change: Hide ICU methods that are in INITIAL_DEPRECATED_SET and Android does not
          // want to make public.
          createHideOriginalDeprecatedClassesRule(),
          // AST change: Explicitly hide blocklisted methods in DECLARATIONS_TO_HIDE such as those
          // that get/set static default values that might lead to confusion or strange interactions
          // between Android's ICU4J and java.text / java.util classes.
          createHideBlocklistedDeclarationsRule(),
          // AST change: Explicitly hide any elements that are marked as
          // @draft / @provisional / @internal
          createOptionalRule(new HideDraftProvisionalInternal()),

          // AST change: Hide new non-allowlisted API in Android temporarily
          // Usually used for avoiding the new API introduced by upstream to show up in Android.
          createHideNonAllowlistedRule(allowlistedApiPath),

          // AST change: Add @Deprecated annotation and @deprecated doc to deprecated API in Android
          createMarkElementsWithDeprecatedAnnotationRule(),
          createMarkElementsWithDeprecatedJavadocTagRule(),

          // AST change: Add @removed doc to removed API in Android
          createMarkElementsWithRemovedJavadocTagRule(),


          // AST change: Remove JavaDoc tags that Android has no need of:
          // @hide has been added in place of @draft, @provisional and @internal
          // @stable <ICU version> will not mean much on Android.
          createOptionalRule(new RemoveJavaDocTags(
              "@stable", "@draft", "@provisional", "@internal", "@since")),
          // AST change: Replace @icu and @icuenhanced with standard text.
          createOptionalRule(new ReplaceIcuTags()),

          // AST change: Translate some of the @.jcite tags used by ICU into @sample tags used by
          // doclava. Those that are not translated are escaped.
          createTranslateJciteInclusionRule(),

          // AST change: Add CorePlatformApi to specified classes and members
          createOptionalRule(AddAnnotation.markerAnnotationFromFlatFile(
              "libcore.api.CorePlatformApi", corePlatformApiFile)),

          // AST change: Add IntraCoreApi to specified classes and members
          createOptionalRule(AddAnnotation.markerAnnotationFromFlatFile(
              "libcore.api.IntraCoreApi", intraCoreApiFile)),

          // AST change: Add default constructors, must come before processor to add
          // UnsupportedAppUsage.
          createOptionalRule(new AddDefaultConstructor(
              TypeLocator.createLocatorsFromStrings(DEFAULT_CONSTRUCTORS))),

          // AST change: Add UnsupportedAppUsage to specified classes and members
          createOptionalRule(Annotations.addUnsupportedAppUsage(unsupportedAppUsagePath)),

          // AST change: Add @SuppressWarnings("HiddenAbstractMethod") to specified methods
          createHiddenAbstractMethodRule(),
      };

      List<Rule> rulesList = Lists.newArrayList(repackageRules);
      rulesList.addAll(Arrays.asList(apiDocsRules));
      if (flaggedApiListPath != null) {
          // AST change: Add FlaggedApi to specified classes and members
          rulesList.add(createOptionalRule(Annotations.addFlaggedApi(flaggedApiListPath)));
      }
      return rulesList;
    }

    private static Rule createTranslateJciteInclusionRule() {
      List<BodyDeclarationLocator> allowlist =
          BodyDeclarationLocators.createLocatorsFromStrings(JCITE_TRANSFORM_SET);
      TranslateJcite.InclusionHandler transformer =
          new TranslateJcite.InclusionHandler(ANDROID_ICU4J_SAMPLE_DIR, allowlist);
      return createOptionalRule(transformer);
    }

    private static Rule createHideOriginalDeprecatedClassesRule() {
      List<BodyDeclarationLocator> blocklist =
          BodyDeclarationLocators.createLocatorsFromStrings(INITIAL_DEPRECATED_SET);
      return createOptionalRule(
          new TagMatchingDeclarations(blocklist, "@hide original deprecated declaration"));
    }

    private static Rule createMarkElementsWithDeprecatedAnnotationRule() {
      List<BodyDeclarationLocator> locators =
          BodyDeclarationLocators.createLocatorsFromStrings(
                  ANDROID_DEPRECATED_SET.keySet().toArray(new String[0]));
      return createOptionalRule(AddAnnotation.markerAnnotationFromLocators(
          "Deprecated", locators));
    }

    private static Rule createMarkElementsWithDeprecatedJavadocTagRule() {
      Map<BodyDeclarationLocator, String> locatorTags = ANDROID_DEPRECATED_SET.entrySet().stream()
              .collect(Collectors.toMap(
                      (entry) -> BodyDeclarationLocators.fromStringForm(entry.getKey()),
                      (entry) -> "@deprecated " + entry.getValue())
              );
      return createOptionalRule(new TagMatchingDeclarations(locatorTags));
    }

    private static Rule createMarkElementsWithRemovedJavadocTagRule() {
      List<BodyDeclarationLocator> locators =
          BodyDeclarationLocators.createLocatorsFromStrings(ANDROID_REMOVED_SET);
      return createOptionalRule(new TagMatchingDeclarations(locators,
          "@removed on Android but @stable in ICU"));
    }

    private static Rule createHideBlocklistedDeclarationsRule() {
      List<BodyDeclarationLocator> blocklist =
          BodyDeclarationLocators.createLocatorsFromStrings(DECLARATIONS_TO_HIDE);
      return createOptionalRule(
          new TagMatchingDeclarations(blocklist, "@hide unsupported on Android"));
    }

    private static Rule createHidePublicClassesRule() {
      List<TypeLocator> allowlist = TypeLocator.createLocatorsFromStrings(PUBLIC_API_CLASSES);
      return createOptionalRule(
          new HidePublicClasses(allowlist, "Only a subset of ICU is exposed in Android"));
    }

    private static Rule createHiddenAbstractMethodRule() {
      List<BodyDeclarationLocator> loactors =
              BodyDeclarationLocators.createLocatorsFromStrings(HIDDEN_ABSTRACT_METHODS);
      return createOptionalRule(
              AddAnnotation.markerAnnotationWithPropertyFromLocators("SuppressWarnings",
                      "value", String.class, "HiddenAbstractMethod", loactors));
    }
  }

  private static Rule createHideNonAllowlistedRule(Path allowlistedApiPath) {
    List<BodyDeclarationLocator> bodyDeclarationLocators = null;
    if (allowlistedApiPath != null) {
      bodyDeclarationLocators = BodyDeclarationLocators.readBodyDeclarationLocators(
              allowlistedApiPath);
    }
    return createOptionalRule(new HideNonAllowlistedDeclarations(bodyDeclarationLocators,
            "@hide Hide new API in Android temporarily"));
  }
}
