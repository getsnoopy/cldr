package org.unicode.cldr.unittest;


import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.unicode.cldr.draft.XLocaleDistance;
import org.unicode.cldr.draft.XLocaleMatcher;

import com.ibm.icu.dev.test.TestFmwk;
import com.ibm.icu.util.LocaleMatcher;
import com.ibm.icu.util.LocalePriorityList;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;

/**
 * Test the XLocaleMatcher.
 * 
 * @author markdavis
 */
public class XLocaleMatcherTest extends TestFmwk {
    private static final int REGION_DISTANCE = 4;


    private static final ULocale ZH_MO = new ULocale("zh_MO");
    private static final ULocale ZH_HK = new ULocale("zh_HK");
    private static final XLocaleDistance LANGUAGE_MATCHER_DATA = XLocaleDistance.getDefault();

    private XLocaleMatcher newXLocaleMatcher() {
        return new XLocaleMatcher("");
    }

    private XLocaleMatcher newXLocaleMatcher(LocalePriorityList build) {
        return new XLocaleMatcher(build);
    }

    private XLocaleMatcher newXLocaleMatcher(String string, int d) {
        return new XLocaleMatcher(string, d);
    }

    private XLocaleMatcher newXLocaleMatcher(String string) {
        return new XLocaleMatcher(LocalePriorityList.add(string).build());
    }

    // public XLocaleMatcher(LocalePriorityList LocalePriorityList,
    // XLocaleMatcherData matcherData, double threshold)

    public static void main(String[] args) throws Exception {
        new XLocaleMatcherTest().run(args);
    }

    public void testParentLocales() {
        assertCloser("es_AR", "es_419", "es_ES");
        assertCloser("es_AR", "es_419", "es");

        assertCloser("es_AR", "es_MX", "es");
        assertCloser("es_AR", "es_MX", "es");

        assertCloser("en_AU", "en_GB", "en_US");
        assertCloser("en_AU", "en_GB", "en");

        assertCloser("en_AU", "en_NZ", "en_US");
        assertCloser("en_AU", "en_NZ", "en");

        assertCloser("pt_AO", "pt_PT", "pt_BR");
        assertCloser("pt_AO", "pt_PT", "pt");

        assertCloser("zh_HK", "zh_MO", "zh_TW");
        assertCloser("zh_HK", "zh_MO", "zh_CN");
        assertCloser("zh_HK", "zh_MO", "zh");
    }

    private void assertCloser(String a, String closer, String further) {
        XLocaleMatcher matcher = newXLocaleMatcher(further + ", " + closer);
        assertEquals("test " + a + " is closer to " + closer + " than to " + further, new ULocale(closer), matcher.getBestMatch(a));
        matcher = newXLocaleMatcher(closer + ", " + further);
        assertEquals("test " + a + " is closer to " + closer + " than to " + further, new ULocale(closer), matcher.getBestMatch(a));
    }

    //    public void testParentLocales() {
    //        // find all the regions that have a closer relation because of an explicit parent
    //        Set<String> explicitParents = new HashSet<>(INFO.getExplicitParents());
    //        explicitParents.remove("root");
    //        Set<String> otherParents = new HashSet<>(INFO.getExplicitParents());
    //        for (String locale : explicitParents) {
    //            while (true) {
    //                locale = LocaleIDParser.getParent(locale);
    //                if (locale == null || locale.equals("root")) {
    //                    break;
    //                }
    //                otherParents.add(locale);
    //            }
    //        }
    //        otherParents.remove("root");
    //
    //        for (String locale : CONFIG.getCldrFactory().getAvailable()) {
    //            String parentId = LocaleIDParser.getParent(locale);
    //            String parentIdSimple = LocaleIDParser.getSimpleParent(locale);
    //            if (!explicitParents.contains(parentId) && !otherParents.contains(parentIdSimple)) {
    //                continue;
    //            }
    //            System.out.println(locale + "\t" + CONFIG.getEnglish().getName(locale) + "\t" + parentId + "\t" + parentIdSimple);
    //        }
    //    }

    public void testChinese() {
        XLocaleMatcher matcher = newXLocaleMatcher("zh_CN, zh_TW, iw");
        ULocale taiwanChinese = new ULocale("zh_TW");
        ULocale chinaChinese = new ULocale("zh_CN");
        assertEquals("zh_CN, zh_TW, iw;", taiwanChinese, matcher.getBestMatch("zh_Hant_TW"));
        assertEquals("zh_CN, zh_TW, iw;", taiwanChinese, matcher.getBestMatch("zh_Hant"));
        assertEquals("zh_CN, zh_TW, iw;", taiwanChinese, matcher.getBestMatch("zh_TW"));
        assertEquals("zh_CN, zh_TW, iw;", chinaChinese, matcher.getBestMatch("zh_Hans_CN"));
        assertEquals("zh_CN, zh_TW, iw;", chinaChinese, matcher.getBestMatch("zh_CN"));
        assertEquals("zh_CN, zh_TW, iw;", chinaChinese, matcher.getBestMatch("zh"));
        assertEquals("zh_CN, zh_TW, iw;", taiwanChinese, matcher.getBestMatch("zh_Hant_HK"));
    }

    public void testenGB() {
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, en, en_GB, es_419, es_MX, es");
        assertEquals("en_GB", matcher.getBestMatch("en_NZ").toString());
        assertEquals("es", matcher.getBestMatch("es_ES").toString());
        assertEquals("es_419", matcher.getBestMatch("es_AR").toString());
        assertEquals("es_MX", matcher.getBestMatch("es_MX").toString());
    }

    public void testFallbacks() {
        final XLocaleMatcher matcher = newXLocaleMatcher("en, hi", 91);
        assertEquals("hi", matcher.getBestMatch("sa").toString());
    }

// TBD reenable
//    public void testOverrideData() {
//        double threshold = 0.05;
//        XLocaleDistance XLocaleMatcherData = new XLocaleDistance()
//        .addDistance("br", "fr", 10, true)
//        .addDistance("es", "cy", 10, true);
//        logln(XLocaleMatcherData.toString());
//
//        final XLocaleMatcher matcher = newXLocaleMatcher(
//            LocalePriorityList
//            .add(ULocale.ENGLISH)
//            .add(ULocale.FRENCH)
//            .add(ULocale.UK)
//            .build(), XLocaleMatcherData, threshold);
//        logln(matcher.toString());
//
//        assertEquals(ULocale.FRENCH, matcher.getBestMatch(new ULocale("br")));
//        assertEquals(ULocale.ENGLISH, matcher.getBestMatch(new ULocale("es"))); // one
//        // way
//    }

    public void testBasics() {
        final XLocaleMatcher matcher = newXLocaleMatcher(LocalePriorityList.add(ULocale.FRENCH).add(ULocale.UK)
            .add(ULocale.ENGLISH).build());
        logln(matcher.toString());

        assertEquals(ULocale.UK, matcher.getBestMatch(ULocale.UK));
        assertEquals(ULocale.ENGLISH, matcher.getBestMatch(ULocale.US));
        assertEquals(ULocale.FRENCH, matcher.getBestMatch(ULocale.FRANCE));
        assertEquals(ULocale.FRENCH, matcher.getBestMatch(ULocale.JAPAN));
    }

    public void testFallback() {
        // check that script fallbacks are handled right
        final XLocaleMatcher matcher = newXLocaleMatcher("zh_CN, zh_TW, iw");
        assertEquals(new ULocale("zh_TW"), matcher.getBestMatch("zh_Hant"));
        assertEquals(new ULocale("zh_CN"), matcher.getBestMatch("zh"));
        assertEquals(new ULocale("zh_CN"), matcher.getBestMatch("zh_Hans_CN"));
        assertEquals(new ULocale("zh_TW"), matcher.getBestMatch("zh_Hant_HK"));
        assertEquals(new ULocale("iw"), matcher.getBestMatch("he_IT"));
    }

    public void testSpecials() {
        // check that nearby languages are handled
        final XLocaleMatcher matcher = newXLocaleMatcher("en, fil, ro, nn");
        assertEquals(new ULocale("fil"), matcher.getBestMatch("tl"));
        assertEquals(new ULocale("ro"), matcher.getBestMatch("mo"));
        assertEquals(new ULocale("nn"), matcher.getBestMatch("nb"));
        // make sure default works
        assertEquals(new ULocale("en"), matcher.getBestMatch("ja"));
    }

    public void testRegionalSpecials() {
        // verify that en_AU is closer to en_GB than to en (which is en_US)
        final XLocaleMatcher matcher = newXLocaleMatcher("en, en_GB, es, es_419");
        assertEquals("es_MX in {en, en_GB, es, es_419}", new ULocale("es_419"), matcher.getBestMatch("es_MX"));
        assertEquals("en_AU in {en, en_GB, es, es_419}", new ULocale("en_GB"), matcher.getBestMatch("en_AU"));
        assertEquals("es_ES in {en, en_GB, es, es_419}", new ULocale("es"), matcher.getBestMatch("es_ES"));
    }

    public void testHK() {
        // HK and MO are closer to each other for Hant than to TW
        final XLocaleMatcher matcher = newXLocaleMatcher("zh, zh_TW, zh_MO");
        assertEquals("zh_HK in {zh, zh_TW, zh_MO}", ZH_MO, matcher.getBestMatch("zh_HK"));
        final XLocaleMatcher matcher2 = newXLocaleMatcher("zh, zh_TW, zh_HK");
        assertEquals("zh_MO in {zh, zh_TW, zh_HK}", ZH_HK, matcher2.getBestMatch("zh_MO"));
    }

//    public void TestXLocaleMatcherCoverage() {
//        // Add tests for better code coverage
//        XLocaleMatcher matcher = newXLocaleMatcher(LocalePriorityList.add(null, 0).build(), null);
//        logln(matcher.toString());
//
//        XLocaleMatcherData data = new XLocaleMatcherData();
//
//        XLocaleMatcherData clone = data.cloneAsThawed();
//
//        if (clone.equals(data)) {
//            errln("Error cloneAsThawed() is equal.");
//        }
//
//        if (data.isFrozen()) {
//            errln("Error XLocaleMatcherData is frozen!");
//        }
//    }

    private void assertEquals(Object expected, Object string) {
        assertEquals("", expected, string);
    }

    private void assertNull(Object bestMatch) {
        assertNull("", bestMatch);
    }

    public void testEmpty() {
        final XLocaleMatcher matcher = newXLocaleMatcher("");
        assertNull(matcher.getBestMatch(ULocale.FRENCH));
    }

    static final ULocale ENGLISH_CANADA = new ULocale("en_CA");

    public void testMatch_exact() {
        assertEquals(1.0, match(ENGLISH_CANADA, ENGLISH_CANADA));
    }

    private double match(ULocale desired, ULocale supported) {
        return newXLocaleMatcher().match(desired, supported);
    }

    public void testMatch_none() {
        double match = match(new ULocale("ar_MK"), ENGLISH_CANADA);
        assertTrue("Actual < 0: " + match, 0 <= match);
        assertTrue("Actual > 0.15 (~ language + script distance): " + match, 0.2 > match);
    }

    public void testMatch_matchOnMazimized() {
        ULocale undTw = new ULocale("und_TW");
        ULocale zhHant = new ULocale("zh_Hant");
        double matchZh = match(undTw, new ULocale("zh"));
        double matchZhHant = match(undTw, zhHant);
        assertTrue("und_TW should be closer to zh_Hant (" + matchZhHant +
            ") than to zh (" + matchZh + ")",
            matchZh < matchZhHant);
        double matchEnHantTw = match(new ULocale("en_Hant_TW"),
            zhHant);
        assertTrue("zh_Hant should be closer to und_TW (" + matchZhHant +
            ") than to en_Hant_TW (" + matchEnHantTw + ")",
            matchEnHantTw < matchZhHant);
        assertTrue("zh should be closer to und_TW (" + matchZh +
            ") than to en_Hant_TW (" + matchEnHantTw + ")",
            matchEnHantTw < matchZh);
    }

    public void testMatchGrandfatheredCode() {
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, i_klingon, en_Latn_US");
        assertEquals("en_Latn_US", matcher.getBestMatch("en_GB_oed").toString());
        // assertEquals("tlh", matcher.getBestMatch("i_klingon").toString());
    }

    public void testGetBestMatchForList_exactMatch() {
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, en_GB, ja, es_ES, es_MX");
        assertEquals("ja", matcher.getBestMatch("ja, de").toString());
    }

    public void testGetBestMatchForList_simpleVariantMatch() {
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, en_GB, ja, es_ES, es_MX");
        // Intentionally avoiding a perfect_match or two candidates for variant
        // matches.
        assertEquals("en_GB", matcher.getBestMatch("de, en_US").toString());
        // Fall back.
        assertEquals("fr", matcher.getBestMatch("de, zh").toString());
    }

    public void testGetBestMatchForList_matchOnMaximized() {
        final XLocaleMatcher matcher = newXLocaleMatcher("en, ja");
        // final XLocaleMatcher matcher =
        // newXLocaleMatcher("fr, en, ja, es_ES, es_MX");
        // Check that if the preference is maximized already, it works as well.
        assertEquals("Match for ja_Jpan_JP (maximized already)",
            "ja", matcher.getBestMatch("ja_Jpan_JP, en-AU").toString());
        if (true)
            return;
        // ja_JP matches ja on likely subtags, and it's listed first, thus it
        // wins over
        // thus it wins over the second preference en_GB.
        assertEquals("Match for ja_JP, with likely region subtag",
            "ja", matcher.getBestMatch("ja_JP, en_US").toString());
        // Check that if the preference is maximized already, it works as well.
        assertEquals("Match for ja_Jpan_JP (maximized already)",
            "ja", matcher.getBestMatch("ja_Jpan_JP, en_US").toString());
    }

    public void testGetBestMatchForList_noMatchOnMaximized() {
        // Regression test for http://b/5714572 .
        final XLocaleMatcher matcher = newXLocaleMatcher("en, de, fr, ja");
        // de maximizes to de_DE. Pick the exact match for the secondary
        // language instead.
        assertEquals("de", matcher.getBestMatch("de_CH, fr").toString());
    }

    public void testBestMatchForTraditionalChinese() {
        // Scenario: An application that only supports Simplified Chinese (and
        // some other languages),
        // but does not support Traditional Chinese. zh_Hans_CN could be
        // replaced with zh_CN, zh, or
        // zh_Hans, it wouldn't make much of a difference.
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, zh_Hans_CN, en_US");

        // The script distance (simplified vs. traditional Han) is considered
        // small enough
        // to be an acceptable match. The regional difference is considered
        // almost insignificant.
        assertEquals("zh_Hans_CN", matcher.getBestMatch("zh_TW").toString());
        assertEquals("zh_Hans_CN", matcher.getBestMatch("zh_Hant").toString());

        // For geo_political reasons, you might want to avoid a zh_Hant ->
        // zh_Hans match.
        // In this case, if zh_TW, zh_HK or a tag starting with zh_Hant is
        // requested, you can
        // change your call to getBestMatch to include a 2nd language
        // preference.
        // "en" is a better match since its distance to "en_US" is closer than
        // the distance
        // from "zh_TW" to "zh_CN" (script distance).
        assertEquals("en_US", matcher.getBestMatch("zh_TW, en").toString());
        assertEquals("en_US", matcher.getBestMatch("zh_Hant_CN, en").toString());
        assertEquals("zh_Hans_CN", matcher.getBestMatch("zh_Hans, en").toString());
    }

    public void testUndefined() {
        // When the undefined language doesn't match anything in the list,
        // getBestMatch returns
        // the default, as usual.
        XLocaleMatcher matcher = newXLocaleMatcher("it,fr");
        assertEquals("it", matcher.getBestMatch("und").toString());

        // When it *does* occur in the list, BestMatch returns it, as expected.
        matcher = newXLocaleMatcher("it,und");
        assertEquals("und", matcher.getBestMatch("und").toString());

        // The unusual part:
        // max("und") = "en_Latn_US", and since matching is based on maximized
        // tags, the undefined
        // language would normally match English. But that would produce the
        // counterintuitive results
        // that getBestMatch("und", XLocaleMatcher("it,en")) would be "en", and
        // getBestMatch("en", XLocaleMatcher("it,und")) would be "und".
        //
        // To avoid that, we change the matcher's definitions of max
        // (AddLikelySubtagsWithDefaults)
        // so that max("und")="und". That produces the following, more desirable
        // results:
        matcher = newXLocaleMatcher("it,en");
        assertEquals("it", matcher.getBestMatch("und").toString());
        matcher = newXLocaleMatcher("it,und");
        assertEquals("it", matcher.getBestMatch("en").toString());
    }

    // public void testGetBestMatch_emptyList() {
    // final XLocaleMatcher matcher = newXLocaleMatcher(
    // new LocalePriorityList(new HashMap()));
    // assertNull(matcher.getBestMatch(ULocale.ENGLISH));
    // }

//    public void testGetBestMatch_googlePseudoLocales() {
//        // Google pseudo locales are primarily based on variant subtags.
//        // See http://sites/intl_eng/pseudo_locales.
//        // (See below for the region code based fall back options.)
//        final XLocaleMatcher matcher = newXLocaleMatcher(
//            "fr, pt");
//        assertEquals("fr", matcher.getBestMatch("de").toString());
//        assertEquals("fr", matcher.getBestMatch("en_US").toString());
//        assertEquals("fr", matcher.getBestMatch("en").toString());
//        assertEquals("pt", matcher.getBestMatch("pt_BR").toString());
//    }

    public void testGetBestMatch_regionDistance() {
        XLocaleMatcher matcher = newXLocaleMatcher("es_AR, es");
        assertEquals("es_AR", matcher.getBestMatch("es_MX").toString());

        matcher = newXLocaleMatcher("fr, en, en_GB");
        assertEquals("en_GB", matcher.getBestMatch("en_CA").toString());

        matcher = newXLocaleMatcher("de_AT, de_DE, de_CH");
        assertEquals("de_DE", matcher.getBestMatch("de").toString());

        showDistance(matcher, "en", "en_CA");
        showDistance(matcher, "en_CA", "en");
        showDistance(matcher, "en_US", "en_CA");
        showDistance(matcher, "en_CA", "en_US");
        showDistance(matcher, "en_GB", "en_CA");
        showDistance(matcher, "en_CA", "en_GB");
        showDistance(matcher, "en", "en_UM");
        showDistance(matcher, "en_UM", "en");
    }

    private void showDistance(XLocaleMatcher matcher, String desired, String supported) {
        ULocale desired2 = new ULocale(desired);
        ULocale supported2 = new ULocale(supported);
        int distance = matcher.distance(desired2, supported2);
        logln(desired + " to " + supported + " :\t" + distance);
    }

    /**
     * If all the base languages are the same, then each sublocale matches
     * itself most closely
     */
    public void testExactMatches() {
        String lastBase = "";
        TreeSet<ULocale> sorted = new TreeSet<ULocale>();
        for (ULocale loc : ULocale.getAvailableLocales()) {
            String language = loc.getLanguage();
            if (!lastBase.equals(language)) {
                check(sorted);
                sorted.clear();
                lastBase = language;
            }
            sorted.add(loc);
        }
        check(sorted);
    }

    private void check(Set<ULocale> sorted) {
        if (sorted.isEmpty()) {
            return;
        }
        check2(sorted);
        ULocale first = sorted.iterator().next();
        ULocale max = ULocale.addLikelySubtags(first);
        sorted.add(max);
        check2(sorted);
    }

    /**
     * @param sorted
     */
    private void check2(Set<ULocale> sorted) {
        // TODO Auto-generated method stub
        logln("Checking: " + sorted);
        XLocaleMatcher matcher = newXLocaleMatcher(
            LocalePriorityList.add(
                sorted.toArray(new ULocale[sorted.size()]))
                .build());
        for (ULocale loc : sorted) {
            String stringLoc = loc.toString();
            assertEquals(stringLoc, matcher.getBestMatch(stringLoc).toString());
        }
    }

    public void testAsymmetry() {
        XLocaleMatcher matcher;
        matcher = new XLocaleMatcher("mul, nl");
        assertEquals("nl", matcher.getBestMatch("af").toString()); // af => nl

        matcher = new XLocaleMatcher("mul, af");
        assertEquals("mul", matcher.getBestMatch("nl").toString()); // but nl !=> af
    }


    public void testComputeDistance_monkeyTest() {
        String[] codes = ULocale.getISOCountries();
        Random random = new Random();
        XLocaleMatcher lm = newXLocaleMatcher();
        for (int i = 0; i < 1000; ++i) {
            String x = codes[random.nextInt(codes.length)];
            String y = codes[random.nextInt(codes.length)];
            double d = lm.distance(ULocale.forLanguageTag("xx-Xxxx-"+x), ULocale.forLanguageTag("xx-Xxxx-"+y));
            if (x.equals("ZZ") || y.equals("ZZ")) {
                assertEquals("dist(regionDistance," + x + ") = 0", REGION_DISTANCE, d);
            } else if (x.equals(y)) {
                assertEquals("dist(x,x) = 0", 0.0, d);
            } else {
                assertTrue("dist(" + x + "," + y + ") > 0", d > 0);
                assertTrue("dist(" + x + "," + y + ") ≤ " + REGION_DISTANCE, d <= REGION_DISTANCE);
            }
        }
    }

    public void testGetBestMatchForList_matchOnMaximized2() {
//        if (logKnownIssue("Cldrbug:8811", "Problems with XLocaleMatcher test")) {
//            return;
//        }
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, en-GB, ja, es-ES, es-MX");
        // ja-JP matches ja on likely subtags, and it's listed first, thus it wins over
        // thus it wins over the second preference en-GB.
        assertEquals("Match for ja-JP, with likely region subtag",
            "ja", matcher.getBestMatch("ja-JP, en-GB").toString());
        // Check that if the preference is maximized already, it works as well.
        assertEquals("Match for ja-Jpan-JP (maximized already)",
            "ja", matcher.getBestMatch("ja-Jpan-JP, en-GB").toString());
    }

    public void testGetBestMatchForList_closeEnoughMatchOnMaximized() {
//        if (logKnownIssue("Cldrbug:8811", "Problems with XLocaleMatcher test")) {
//            return;
//        }
        final XLocaleMatcher matcher = newXLocaleMatcher("en-GB, en, de, fr, ja");
        assertEquals("de", matcher.getBestMatch("de-CH, fr").toString());
        assertEquals("en", matcher.getBestMatch("en-US, ar, nl, de, ja").toString());
    }

    public void testGetBestMatchForPortuguese() {

//        if (logKnownIssue("Cldrbug:8811", "Problems with XLocaleMatcher test")) {
//            return;
//        }

        final XLocaleMatcher withPTExplicit = newXLocaleMatcher("pt_PT, pt_BR, es, es_419");
        final XLocaleMatcher withPTImplicit = newXLocaleMatcher("pt_PT, pt, es, es_419");
        // Could happen because "pt_BR" is a tier_1 language and "pt_PT" is tier_2.

        final XLocaleMatcher withoutPT = newXLocaleMatcher("pt_BR, es, es_419");
        // European user who prefers Spanish over Brazillian Portuguese as a fallback.

        assertEquals("pt_PT", withPTExplicit.getBestMatch("pt_PT, es, pt").toString());
        assertEquals("pt_PT", withPTImplicit.getBestMatch("pt_PT, es, pt").toString());
        // assertEquals("es", withoutPT.getBestMatch("pt_PT, es, pt").toString());

        // Brazillian user who prefers South American Spanish over European Portuguese as a fallback.
        // The asymmetry between this case and above is because it's "pt_PT" that's missing between the
        // matchers as "pt_BR" is a much more common language.
        assertEquals("pt_BR", withPTExplicit.getBestMatch("pt, es_419, pt_PT").toString());
        assertEquals("pt", withPTImplicit.getBestMatch("pt, es_419, pt_PT").toString());
        assertEquals("pt_BR", withoutPT.getBestMatch("pt, es_419, pt_PT").toString());

        // Code that adds the user's country can get "pt_US" for a user's language.
        // That should fall back to "pt_BR".
        assertEquals("pt_BR", withPTExplicit.getBestMatch("pt_US, pt_PT").toString());
        assertEquals("pt", withPTImplicit.getBestMatch("pt_US, pt_PT").toString());
    }

    public void testVariantWithScriptMatch() {
//        if (logKnownIssue("Cldrbug:8811", "Problems with XLocaleMatcher test")) {
//            return;
//        }
        final XLocaleMatcher matcher = newXLocaleMatcher("fr, en, sv");
        assertEquals("en", matcher.getBestMatch("en-GB").toString());
        assertEquals("en", matcher.getBestMatch("en-GB, sv").toString());
    }

    public void testVariantWithScriptMatch2() {
//        if (logKnownIssue("Cldrbug:8811", "Problems with XLocaleMatcher test")) {
//            return;
//        }
        final XLocaleMatcher matcher = newXLocaleMatcher("en, sv");
        assertEquals("en", matcher.getBestMatch("en-GB, sv").toString());
    }

    public void testPerf() {
        if (LANGUAGE_MATCHER_DATA == null) {
            return; // skip except when testing data
        }
        final ULocale desired = new ULocale("sv");
        
        final String shortList = "en, sv";
        final String longList = "af, am, ar, az, be, bg, bn, bs, ca, cs, cy, cy, da, de, el, en, en-GB, es, es-419, et, eu, fa, fi, fil, fr, ga, gl, gu, hi, hr, hu, hy, id, is, it, iw, ja, ka, kk, km, kn, ko, ky, lo, lt, lv, mk, ml, mn, mr, ms, my, ne, nl, no, pa, pl, pt, pt-PT, ro, ru, si, sk, sl, sq, sr, sr-Latn, sv, sw, ta, te, th, tr, uk, ur, uz, vi, zh-CN, zh-TW, zu";
        final String veryLongList = "af, af_NA, af_ZA, agq, agq_CM, ak, ak_GH, am, am_ET, ar, ar_001, ar_AE, ar_BH, ar_DJ, ar_DZ, ar_EG, ar_EH, ar_ER, ar_IL, ar_IQ, ar_JO, ar_KM, ar_KW, ar_LB, ar_LY, ar_MA, ar_MR, ar_OM, ar_PS, ar_QA, ar_SA, ar_SD, ar_SO, ar_SS, ar_SY, ar_TD, ar_TN, ar_YE, as, as_IN, asa, asa_TZ, ast, ast_ES, az, az_Cyrl, az_Cyrl_AZ, az_Latn, az_Latn_AZ, bas, bas_CM, be, be_BY, bem, bem_ZM, bez, bez_TZ, bg, bg_BG, bm, bm_ML, bn, bn_BD, bn_IN, bo, bo_CN, bo_IN, br, br_FR, brx, brx_IN, bs, bs_Cyrl, bs_Cyrl_BA, bs_Latn, bs_Latn_BA, ca, ca_AD, ca_ES, ca_ES_VALENCIA, ca_FR, ca_IT, ce, ce_RU, cgg, cgg_UG, chr, chr_US, ckb, ckb_IQ, ckb_IR, cs, cs_CZ, cu, cu_RU, cy, cy_GB, da, da_DK, da_GL, dav, dav_KE, de, de_AT, de_BE, de_CH, de_DE, de_LI, de_LU, dje, dje_NE, dsb, dsb_DE, dua, dua_CM, dyo, dyo_SN, dz, dz_BT, ebu, ebu_KE, ee, ee_GH, ee_TG, el, el_CY, el_GR, en, en_001, en_150, en_AG, en_AI, en_AS, en_AT, en_AU, en_BB, en_BE, en_BI, en_BM, en_BS, en_BW, en_BZ, en_CA, en_CC, en_CH, en_CK, en_CM, en_CX, en_CY, en_DE, en_DG, en_DK, en_DM, en_ER, en_FI, en_FJ, en_FK, en_FM, en_GB, en_GD, en_GG, en_GH, en_GI, en_GM, en_GU, en_GY, en_HK, en_IE, en_IL, en_IM, en_IN, en_IO, en_JE, en_JM, en_KE, en_KI, en_KN, en_KY, en_LC, en_LR, en_LS, en_MG, en_MH, en_MO, en_MP, en_MS, en_MT, en_MU, en_MW, en_MY, en_NA, en_NF, en_NG, en_NL, en_NR, en_NU, en_NZ, en_PG, en_PH, en_PK, en_PN, en_PR, en_PW, en_RW, en_SB, en_SC, en_SD, en_SE, en_SG, en_SH, en_SI, en_SL, en_SS, en_SX, en_SZ, en_TC, en_TK, en_TO, en_TT, en_TV, en_TZ, en_UG, en_UM, en_US, en_US_POSIX, en_VC, en_VG, en_VI, en_VU, en_WS, en_ZA, en_ZM, en_ZW, eo, eo_001, es, es_419, es_AR, es_BO, es_CL, es_CO, es_CR, es_CU, es_DO, es_EA, es_EC, es_ES, es_GQ, es_GT, es_HN, es_IC, es_MX, es_NI, es_PA, es_PE, es_PH, es_PR, es_PY, es_SV, es_US, es_UY, es_VE, et, et_EE, eu, eu_ES, ewo, ewo_CM, fa, fa_AF, fa_IR, ff, ff_CM, ff_GN, ff_MR, ff_SN, fi, fi_FI, fil, fil_PH, fo, fo_DK, fo_FO, fr, fr_BE, fr_BF, fr_BI, fr_BJ, fr_BL, fr_CA, fr_CD, fr_CF, fr_CG, fr_CH, fr_CI, fr_CM, fr_DJ, fr_DZ, fr_FR, fr_GA, fr_GF, fr_GN, fr_GP, fr_GQ, fr_HT, fr_KM, fr_LU, fr_MA, fr_MC, fr_MF, fr_MG, fr_ML, fr_MQ, fr_MR, fr_MU, fr_NC, fr_NE, fr_PF, fr_PM, fr_RE, fr_RW, fr_SC, fr_SN, fr_SY, fr_TD, fr_TG, fr_TN, fr_VU, fr_WF, fr_YT, fur, fur_IT, fy, fy_NL, ga, ga_IE, gd, gd_GB, gl, gl_ES, gsw, gsw_CH, gsw_FR, gsw_LI, gu, gu_IN, guz, guz_KE, gv, gv_IM, ha, ha_GH, ha_NE, ha_NG, haw, haw_US, he, he_IL, hi, hi_IN, hr, hr_BA, hr_HR, hsb, hsb_DE, hu, hu_HU, hy, hy_AM, id, id_ID, ig, ig_NG, ii, ii_CN, is, is_IS, it, it_CH, it_IT, it_SM, ja, ja_JP, jgo, jgo_CM, jmc, jmc_TZ, ka, ka_GE, kab, kab_DZ, kam, kam_KE, kde, kde_TZ, kea, kea_CV, khq, khq_ML, ki, ki_KE, kk, kk_KZ, kkj, kkj_CM, kl, kl_GL, kln, kln_KE, km, km_KH, kn, kn_IN, ko, ko_KP, ko_KR, kok, kok_IN, ks, ks_IN, ksb, ksb_TZ, ksf, ksf_CM, ksh, ksh_DE, kw, kw_GB, ky, ky_KG, lag, lag_TZ, lb, lb_LU, lg, lg_UG, lkt, lkt_US, ln, ln_AO, ln_CD, ln_CF, ln_CG, lo, lo_LA, lrc, lrc_IQ, lrc_IR, lt, lt_LT, lu, lu_CD, luo, luo_KE, luy, luy_KE, lv, lv_LV, mas, mas_KE, mas_TZ, mer, mer_KE, mfe, mfe_MU, mg, mg_MG, mgh, mgh_MZ, mgo, mgo_CM, mk, mk_MK, ml, ml_IN, mn, mn_MN, mr, mr_IN, ms, ms_BN, ms_MY, ms_SG, mt, mt_MT, mua, mua_CM, my, my_MM, mzn, mzn_IR, naq, naq_NA, nb, nb_NO, nb_SJ, nd, nd_ZW, ne, ne_IN, ne_NP, nl, nl_AW, nl_BE, nl_BQ, nl_CW, nl_NL, nl_SR, nl_SX, nmg, nmg_CM, nn, nn_NO, nnh, nnh_CM, nus, nus_SS, nyn, nyn_UG, om, om_ET, om_KE, or, or_IN, os, os_GE, os_RU, pa, pa_Arab, pa_Arab_PK, pa_Guru, pa_Guru_IN, pl, pl_PL, prg, prg_001, ps, ps_AF, pt, pt_AO, pt_BR, pt_CV, pt_GW, pt_MO, pt_MZ, pt_PT, pt_ST, pt_TL, qu, qu_BO, qu_EC, qu_PE, rm, rm_CH, rn, rn_BI, ro, ro_MD, ro_RO, rof, rof_TZ, root, ru, ru_BY, ru_KG, ru_KZ, ru_MD, ru_RU, ru_UA, rw, rw_RW, rwk, rwk_TZ, sah, sah_RU, saq, saq_KE, sbp, sbp_TZ, se, se_FI, se_NO, se_SE, seh, seh_MZ, ses, ses_ML, sg, sg_CF, shi, shi_Latn, shi_Latn_MA, shi_Tfng, shi_Tfng_MA, si, si_LK, sk, sk_SK, sl, sl_SI, smn, smn_FI, sn, sn_ZW, so, so_DJ, so_ET, so_KE, so_SO, sq, sq_AL, sq_MK, sq_XK, sr, sr_Cyrl, sr_Cyrl_BA, sr_Cyrl_ME, sr_Cyrl_RS, sr_Cyrl_XK, sr_Latn, sr_Latn_BA, sr_Latn_ME, sr_Latn_RS, sr_Latn_XK, sv, sv_AX, sv_FI, sv_SE, sw, sw_CD, sw_KE, sw_TZ, sw_UG, ta, ta_IN, ta_LK, ta_MY, ta_SG, te, te_IN, teo, teo_KE, teo_UG, th, th_TH, ti, ti_ER, ti_ET, tk, tk_TM, to, to_TO, tr, tr_CY, tr_TR, twq, twq_NE, tzm, tzm_MA, ug, ug_CN, uk, uk_UA, ur, ur_IN, ur_PK, uz, uz_Arab, uz_Arab_AF, uz_Cyrl, uz_Cyrl_UZ, uz_Latn, uz_Latn_UZ, vai, vai_Latn, vai_Latn_LR, vai_Vaii, vai_Vaii_LR, vi, vi_VN, vo, vo_001, vun, vun_TZ, wae, wae_CH, xog, xog_UG, yav, yav_CM, yi, yi_001, yo, yo_BJ, yo_NG, zgh, zgh_MA, zh, zh_Hans, zh_Hans_CN, zh_Hans_HK, zh_Hans_MO, zh_Hans_SG, zh_Hant, zh_Hant_HK, zh_Hant_MO, zh_Hant_TW, zu, zu_ZA";

        final XLocaleMatcher matcherShort = newXLocaleMatcher(shortList);
        final XLocaleMatcher matcherLong = newXLocaleMatcher(longList);
        final XLocaleMatcher matcherVeryLong = newXLocaleMatcher(veryLongList);

        final LocaleMatcher matcherShortOld = new LocaleMatcher(shortList);
        final LocaleMatcher matcherLongOld = new LocaleMatcher(longList);
        final LocaleMatcher matcherVeryLongOld = new LocaleMatcher(veryLongList);

        //XLocaleMatcher.DEBUG = true;
        ULocale expected = new ULocale("sv");
        assertEquals(expected, matcherShort.getBestMatch(desired));
        assertEquals(expected, matcherLong.getBestMatch(desired));
        assertEquals(expected, matcherVeryLong.getBestMatch(desired));
        //XLocaleMatcher.DEBUG = false;

        for (int i = 0; i < 2; ++i) {
            int iterations = i == 0 ? 1000 : 100000;
            boolean showMessage = i != 0;
            long timeShort = timeXLocaleMatcher("Duration (few  supported):\t", desired, matcherShort, showMessage, iterations, 0);
            long timeMedium = timeXLocaleMatcher("Duration (med. supported):\t", desired, matcherLong, showMessage, iterations, timeShort);
            long timeLong = timeXLocaleMatcher("Duration (many supported):\t", desired, matcherVeryLong, showMessage, iterations, timeShort);
        }
        
        for (int i = 0; i < 2; ++i) {
            int iterations = i == 0 ? 1000 : 100000;
            boolean showMessage = i != 0;
            long timeShort = timeLocaleMatcher("Old Duration (few  supported):\t", desired, matcherShortOld, showMessage, iterations, 0);
            long timeMedium = timeLocaleMatcher("Old Duration (med. supported):\t", desired, matcherLongOld, showMessage, iterations, timeShort);
            long timeLong = timeLocaleMatcher("Old Duration (many supported):\t", desired, matcherVeryLongOld, showMessage, iterations, timeShort);
        }
    }

    private long timeXLocaleMatcher(String title, ULocale desired, XLocaleMatcher matcher, 
        boolean showmessage, int iterations, long comparisonTime) {
        long start = System.nanoTime();
        for (int i = iterations; i > 0; --i) {
            matcher.getBestMatch(desired);
        }
        long delta = System.nanoTime() - start;
        if (showmessage) warnln(title + (delta / iterations) + " nanos, "
            + (comparisonTime > 0 ? (delta * 100 / comparisonTime - 100) + "% longer" : ""));
        return delta;
    }

    private long timeLocaleMatcher(String title, ULocale desired, LocaleMatcher matcher, 
        boolean showmessage, int iterations, long comparisonTime) {
        long start = System.nanoTime();
        for (int i = iterations; i > 0; --i) {
            matcher.getBestMatch(desired);
        }
        long delta = System.nanoTime() - start;
        if (showmessage) warnln(title + (delta / iterations) + " nanos, "
            + (comparisonTime > 0 ? (delta * 100 / comparisonTime - 100) + "% longer" : ""));
        return delta;
    }

    public void Test8288() {
        final XLocaleMatcher matcher = newXLocaleMatcher("it, en");
        assertEquals(new ULocale("it"), matcher.getBestMatch("und"));
        assertEquals(new ULocale("en"), matcher.getBestMatch("und, en"));
    }

    public void testEuHack() {
        // Verify that the EU-hack (cl/105138489) prevents breakage.
        // The region-distance is currently based on the number of edges in
        // the shortest path in the region-containment graph. For example,
        // the distance between US and NZ is 6 (+ is "up"; - is "down"):
        //     US + 021 + 019 + 001 - 009 - 053 - NZ
        // Before CLDR 28, the distance from US to IT was also 6:
        //     US + 021 + 019 + 001 - 150 - EU - IT
        // So given a priority list with those two regions, whichever one
        // came first would be considered the better match.  But with CLDR
        // 28, which put EU into 001, the distance from US to IT got
        // shortened to 5:
        //     US + 021 + 019 + 001 - EU - IT
        // This would break any unit test that assumed that they were
        // the same, so the EU-hack changes the StandardXLocaleMatcher to
        // restore the previous behavior. A major overhaul of the
        // StandardXLocaleMatcher is planned, but the hack will allow us
        // to release CLDR 28 (and ICU) before that is fixed.
        final XLocaleMatcher matcher = newXLocaleMatcher("en-NZ, en-IT");
        assertEquals(new ULocale("en-NZ"), matcher.getBestMatch("en-US"));
    }


//    public void testMatchGooglePrivateUseSubtag() {
//        final XLocaleMatcher matcher = newXLocaleMatcher("fr, en-GB, x-bork, es-ES, es-419");
//        assertEquals("fr", matcher.getBestMatch("x-piglatin").toString());
//        assertEquals("x-bork", matcher.getBestMatch("x-bork").toString());
//    }



    public void testGetBestMatch_emptyList() {
        XLocaleMatcher lm = newXLocaleMatcher();
        assertNull(lm.getBestMatch(ULocale.ENGLISH));
    }


//    public void testGetBestMatch_googlePseudoLocaleWithFallbacks() {
//        // Pseudo locales based on the fall back option (XA..XC region codes).
//        final XLocaleMatcher matcher = newXLocaleMatcher(
//            "fr, en-XA, ar-XB, en-XC, zh-Hans-XC, pt");
//        assertEquals("fr", matcher.getBestMatch("de").toString());
//        assertEquals("fr", matcher.getBestMatch("en-US").toString());
//        assertEquals("fr", matcher.getBestMatch("en").toString());
//        assertEquals("ar-XB", matcher.getBestMatch("ar-XB").toString());
//        assertEquals("en-XA", matcher.getBestMatch("en-XA").toString());
//        assertEquals("en-XC", matcher.getBestMatch("en-XC").toString());
//        assertEquals("pt", matcher.getBestMatch("pt-BR").toString());
//        assertEquals("zh-Hans-XC", matcher.getBestMatch("zh-Hans-XC").toString());
//    }

//    public void testGetBestMatch_doNotMatchGooglePseudoLocale() {
//        final XLocaleMatcher matcher = newXLocaleMatcher(
//            "fr, en-XA, ar-XB, en-PSACCENT, ar-PSBIDI, en-DE, pt, ar-SY, ar-PSCRACK");
//        assertEquals("fr", matcher.getBestMatch("de").toString());
//        // We wouldn't want to return pseudo locales when there's a good match for an
//        // ordinary locale.
//        // Note: If XLocaleMatcher was not aware of PSACCENT, it would consider the
//        // distance from "en" to "en-PSACCENT" smaller than to "en-DE" (the standard
//        // variant distance is smaller than a region distance).
//        assertEquals(new ULocale("en-DE"), matcher.getBestMatch("en"));
//        assertEquals(new ULocale("ar-SY"), matcher.getBestMatch("ar-EG"));
//        // No competition here.
//        assertEquals(new ULocale("pt"), matcher.getBestMatch("pt-BR"));
//        // Pseudo locales match pseudo locales.
//        assertEquals(new ULocale("ar-XB"), matcher.getBestMatch("ar-XB"));
//        assertEquals(new ULocale("ar-PSBIDI"), matcher.getBestMatch("ar-PSBIDI"));
//        assertEquals(new ULocale("en-XA"), matcher.getBestMatch("en-XA"));
//        assertEquals(new ULocale("en-PSACCENT"), matcher.getBestMatch("en-PSACCENT"));
//        assertEquals(new ULocale("ar-PSCRACK"), matcher.getBestMatch("ar-PSCRACK"));
//    }

    public void testGetBestMatch_bestMatchCrackedLocale() {
        // Given an application which has cracked locales only.
        // A request for "fr" should match "fr-PSCRACK" rather than
        // falling through to the default locale "en-PSCRACK".
        final XLocaleMatcher matcher =
            newXLocaleMatcher("en-PSCRACK, de-PSCRACK, fr-PSCRACK, pt-PT-PSCRACK");
        assertEquals(new ULocale("fr-PSCRACK"), matcher.getBestMatch("fr-PSCRACK"));
        assertEquals(new ULocale("fr-PSCRACK"), matcher.getBestMatch("fr"));
        assertEquals(new ULocale("de-PSCRACK"), matcher.getBestMatch("de-CH"));
    }

    public void testEcEu() {
        XLocaleMatcher lm = newXLocaleMatcher();
        assertTrue("xx-EC to xx-EU", lm.distance(ULocale.forLanguageTag("xx-Xxxx-EC"), ULocale.forLanguageTag("xx-Xxxx-EU")) <= 4);
    }

    public void testVariantsAndExtensions() {
        // examples from
        // http://unicode.org/repos/cldr/tags/latest/common/bcp47/
        // http://unicode.org/repos/cldr/tags/latest/common/validity/variant.xml
        String[][] tests = {
            // format: supported, desired, expected
            {"en-GB-u-sd-gbsct, fr-fonupa", "en-fonipa-u-nu-Arab-ca-buddhist-t-m0-iso-i0-pinyin", "en-GB-fonipa-u-nu-Arab-ca-buddhist-t-m0-iso-i0-pinyin"},
            {"en-GB-u-sd-gbsct, fr-fonupa", "fr-BE-fonipa", "fr-BE-fonipa"},
        };

        for (String[] supportedDesiredExpected : tests) {
            checkFull(supportedDesiredExpected[0], supportedDesiredExpected[1], supportedDesiredExpected[2], true);
        }
    }
    
    public void testPrimaries() {
        // examples from
        // http://unicode.org/repos/cldr/tags/latest/common/bcp47/
        // http://unicode.org/repos/cldr/tags/latest/common/validity/variant.xml
        String[][] tests = {
            // format: supported, desired, expected
            // we favor es-419 over others in cluster. Clusters: es- {ES, MA, EA} {419, AR, MX}
            {"und, es, es-MA, es-MX, es-419", "es-AR", "es-419"},
            {"und, es-MA, es, es-419, es-MX", "es-AR", "es-419"},
            {"und, es, es-MA, es-MX, es-419", "es-EA", "es"},
            {"und, es-MA, es, es-419, es-MX", "es-EA", "es"},
            // of course, fall back to within cluster
            {"und, es, es-MA, es-MX", "es-AR", "es-MX"},
            {"und, es-MA, es, es-MX", "es-AR", "es-MX"},
            {"und, es-MA, es-MX, es-419", "es-EA", "es-MA"},
            {"und, es-MA, es-419, es-MX", "es-EA", "es-MA"},
            // we favor es-GB over others in cluster. Clusters: en- {US, GU, VI} {GB, IN, ZA}
            {"und, en, en-GU, en-IN, en-GB", "en-ZA", "en-GB"},
            {"und, en-GU, en, en-GB, en-IN", "en-ZA", "en-GB"},
            {"und, en, en-GU, en-IN, en-GB", "en-VI", "en"},
            {"und, en-GU, en, en-GB, en-IN", "en-VI", "en"},
            // of course, fall back to within cluster
            {"und, en, en-GU, en-IN", "en-ZA", "en-IN"},
            {"und, en-GU, en, en-IN", "en-ZA", "en-IN"},
            {"und, en-GU, en-IN, en-GB", "en-VI", "en-GU"},
            {"und, en-GU, en-GB, en-IN", "en-VI", "en-GU"},
        };

        for (String[] supportedDesiredExpected : tests) {
            checkFull(supportedDesiredExpected[0], supportedDesiredExpected[1], supportedDesiredExpected[2], false);
        }
    }


    private void checkFull(final String supported, final String desired, final String expected, boolean combineResults) {
        XLocaleMatcher matcher = newXLocaleMatcher(supported);
        Output<ULocale> bestDesired = new Output<>();
        ULocale bestMatch = matcher.getBestMatch(LocalePriorityList.add(desired).build(), bestDesired);
        ULocale bestFixed = !combineResults || bestDesired.value == null ? bestMatch : XLocaleMatcher.combine(bestMatch, bestDesired.value);
        logln("matcher: " + matcher + "; desired: " + desired + "; bestMatch: " + bestMatch + "; bestDesired: " + bestDesired + "; bestFixed: " + bestFixed);
        assertEquals("matcher: " + matcher + "; desired: " + desired, ULocale.forLanguageTag(expected), bestFixed);
    }
}