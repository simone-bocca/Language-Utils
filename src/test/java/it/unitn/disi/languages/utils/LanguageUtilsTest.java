package it.unitn.disi.languages.utils;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.util.Locale;

public class LanguageUtilsTest {


    private static LanguageUtils langUtils = new LanguageUtils();
    @Test
    public void testReadTable() {
        String label = langUtils.getISO2Code("eng");
        System.out.println(label);

        label = langUtils.getISO3Code("en");
        System.out.println(label);


    }

    @Test
    public void isValidLanguage() {
        //System.out.println(langUtils.isValidLanguage("en"));
        //System.out.println(langUtils.isValidLanguage("eng"));
        System.out.println(langUtils.isValidLanguage("enr"));
        System.out.println(langUtils.isValidLanguage("ita-pm"));
        System.out.println(langUtils.isValidLanguage("null"));


    }

    @Test
    public void getLanguageCodes() {
        System.out.println(langUtils.getLanguageCodes("Bislama"));
        System.out.println(langUtils.getLanguageCodes("English"));
    }

    @Test
    public void getAllLanguagesAvaliable() {
        //for (String code: langUtils.getAllLanguagesAvaliable()) System.out.println(code);
        System.out.println("#:"+langUtils.getAllLanguagesAvaliable().length);
    }

    @Test
    public void getAllLanguagesAvaliableString() {
        System.out.println(langUtils.getAllLanguagesAvaliableString());
    }

    @Test
    public void getLocale() {
        Locale loc = langUtils.getLocale("en");
        assertNotNull(loc);
        System.out.println(loc.getLanguage());
    }

    @Test
    public void getLanguageCodeFromLocale() {
        Locale loc = langUtils.getLocale("eng");
        String code = langUtils.getLanguageCodeFromLocale(loc);

        assertNotNull(code);
        System.out.println(code);
    }

    @Test
    public void getISO3Code() {
        assertNotNull(langUtils.getISO3Code("eno"));
        System.out.println(langUtils.getISO3Code("en"));
    }

    @Test
    public void getCodeName() {
        System.out.println(langUtils.getCodeName("en"));
        System.out.println(langUtils.getCodeName("eng"));
        System.out.println(langUtils.getCodeName("ita-pm"));
    }
}