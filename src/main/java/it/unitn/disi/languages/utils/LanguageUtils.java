package it.unitn.disi.languages.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class for languages manipulation and translation
 *
 * @author simone bocca
 *
 * */
public class LanguageUtils {

    /**
     * map of codes ISO 639-3 to ISO 639-2
     */
    private static final Map<String, String> LANG_CODE_MAP = new HashMap<String, String>();
    /**
     * map of codes ISO 639-3 to language name
     */
    private static final Map<String, String> LANG_NAME_MAP = new HashMap<String, String>();

    /**
     * get key of a maps from value
     */
    private static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public LanguageUtils() {
        readTable("complete_languages_conversion_table.tab");
    }


    /**
     * Load the languages labels from language conversion table resource,
     * into util language maps.
     *
     * @param path path of conversion table resource
     */
    private static void readTable(String path) {

        InputStream is = LanguageUtils.class.getClassLoader().getResourceAsStream(path);

        try{
            BufferedReader buf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            ArrayList<String> words = new ArrayList<String>();
            String lineJustFetched = null;
            String[] wordsArray;

            while(true){
                lineJustFetched = buf.readLine();
                if (lineJustFetched == null){
                    break;
                }else{
                    wordsArray = lineJustFetched.split("\t");
                    if (!"".equals(wordsArray[1])){
                        LANG_CODE_MAP.put(wordsArray[0], wordsArray[1]);
                        LANG_NAME_MAP.put(wordsArray[0], wordsArray[2]);
                    } else {
                        LANG_CODE_MAP.put(wordsArray[0], "1");
                        LANG_NAME_MAP.put(wordsArray[0], wordsArray[2]);
                    }
                }
            }

            buf.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param code ISO 639-3 label for language
     * @return ISO 639-2 label for the same language
     */
    public static String getISO2Code(String code){
        if (code.length() == 2 && isValidLanguage(code))
            return code;

        if (isValidLanguage(code))
            return LANG_CODE_MAP.get(code);
        else
            throw new IllegalArgumentException("invalid language code");
    }

    /**
     * @param code ISO 639-2 label for language
     * @return ISO 639-3 label for the same language
     */
    public static String getISO3Code(String code){
        if (code.length() == 3 && isValidLanguage(code))
            return code;

        if (isValidLanguage(code))
            return getKey(LANG_CODE_MAP, code);
        else
            throw new IllegalArgumentException("invalid language code");
    }

    /**
     * get the name of language specified by code
     *
     * @param code ISO 639-2 or ISO 639-3 label for language
     * @return name of language
     */
    public static String getCodeName(String code){

        // 2-chars code
        if (code.length() == 2 && isValidLanguage(code))
            return LANG_NAME_MAP.get(getKey(LANG_CODE_MAP, code));

        // 3-chars code
        else if (code.length() == 3 && isValidLanguage(code))
            return LANG_NAME_MAP.get(code);

        // 3-chars code plus extension
        else if (code.length()>3 && (code.charAt(3)=='-')){
            String lang = code.substring(0, 3);
            String ext = code.substring(4, code.length());
            if (isValidLanguage(lang)) return LANG_NAME_MAP.get(lang)+"-"+ext;
            else throw new IllegalArgumentException("invalid language code:"+code);
        }
        else
            throw new IllegalArgumentException("invalid language code");
    }



    /**
     * @param code language code to verify
     * @return True, if code is a valid language code. False otherwise.
     */
    public static boolean isValidLanguage(String code){
        if (code == null || code.equals("") || code.equals("1"))
            throw new IllegalArgumentException("Empty or Null language code");

        if (code.length()>3 && (code.charAt(3)=='-')){
            String lang = code.substring(0, 3);
            String ext = code.substring(4, code.length());

            if (LANG_CODE_MAP.containsKey(lang)) return true;
            else if (LANG_CODE_MAP.containsValue(lang)) return true;
            else return false;

        } else {
            if (LANG_CODE_MAP.containsKey(code)) return true;
            else if (LANG_CODE_MAP.containsValue(code)) return true;
            else return false;
        }
    }

    /**
     * get the list of language codes from language name
     *
     * @param name Language name
     * @return list of codes
     */
    public static ArrayList<String> getLanguageCodes(String name){
        if (name == null || name.equals(""))
            throw new IllegalArgumentException("Empty or Null language name");

        if (!LANG_NAME_MAP.containsValue(name))
            throw new IllegalArgumentException("Invalid language name: "+name);

        String code3 = getKey(LANG_NAME_MAP, name);
        String code2 = LANG_CODE_MAP.get(code3);

        ArrayList<String> codeSet = new ArrayList<String>();
        codeSet.add(code3);
        codeSet.add(code2);

        return codeSet;
    }

    /**
     * create the Locale object for a specific valid language
     * and, if exist, language extension.
     *
     * @param code language code
     * @return Locale object of corresponding language
     */

    public static Locale getLocale(String code){
        Locale locale;

        if (code == null || code.equals("") || code.length() == 1)
            throw new IllegalArgumentException("Empty or Null language name");

        if (code.length() > 3) {
            String lang = code.substring(0, 3);
            String ext = code.substring(4, code.length());

            if (isValidLanguage(lang))
                locale = new Locale.Builder().setLanguage(lang).setExtension('e', ext).build();
            else
                throw new IllegalArgumentException("Language not recognized: "+lang);

        } else {
            if (isValidLanguage(code))
                locale = new Locale.Builder().setLanguage(code).build();
            else
                throw new IllegalArgumentException("Language not recognized: " + code);
        }
        return  locale;
    }


    /**
     * Get complete language code (language + extension) from the Locale Object
     *
     * @param locale object of the language
     * @return language code
     */
    public static String getLanguageCodeFromLocale(Locale locale){
        if (locale == null)
            throw new IllegalArgumentException("Null Locale");

        String lang = getISO3Code(locale.getLanguage());
        if (locale.getExtension('e') != null)
            lang+="-"+locale.getExtension('e');

        return lang;
    }

    /**
     * Get codes for all languages managed by this utils.
     *
     * @return array of all ISO 639-3 languages labels
     */
    public static String[] getAllLanguagesAvaliable(){
        String[] all = new String[LANG_CODE_MAP.size()];
        int i=0;
        for (String code: LANG_CODE_MAP.keySet()) {
            all[i] = code;
            i++;
        }
        return all;
    }

    /**
     * Get codes for all languages managed by this utils.
     *
     * @return array of all ISO 639-3 languages labels
     */
    public static String getAllLanguagesAvaliableString(){
        String all = "";
        int i=0;
        for (String code: LANG_CODE_MAP.keySet()) {
            all += code+",";
            i++;
        }
        return all.substring(0,all.length()-1);
    }


}
