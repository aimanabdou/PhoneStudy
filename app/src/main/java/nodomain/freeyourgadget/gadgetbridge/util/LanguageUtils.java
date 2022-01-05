/*  Copyright (C) 2017-2021 Andreas Shimokawa, Aniruddha Adhikary, Daniele
    Gobbetti, ivanovlev, kalaee, lazarosfs, McSym28, M. Hadi, Roi Greenberg,
    Taavi Eomäe, Ted Stein, Thomas, Yaron Shahrabani

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package nodomain.freeyourgadget.gadgetbridge.util;

import static nodomain.freeyourgadget.gadgetbridge.activities.devicesettings.DeviceSettingsPreferenceConst.PREF_TRANSLITERATION_ENABLED;

import android.content.SharedPreferences;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import nodomain.freeyourgadget.gadgetbridge.GBApplication;
import nodomain.freeyourgadget.gadgetbridge.R;
import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;

public class LanguageUtils {
    private static final Logger LOG = LoggerFactory.getLogger(LanguageUtils.class);
    // Transliteration map with english equivalent for unsupported chars
    @SuppressWarnings("OverwrittenKey")
    private static final Map<Character, String> transliterateMap = new HashMap<Character, String>() {
        {
            // Extended ASCII characters
            put('œ', "oe"); put('ª', "a"); put('º', "o"); put('«',"\""); put('»',"\"");
            
            // Scandinavian characters
            put('Æ',"Ae"); put('æ',"ae");
            put('Ø',"Oe"); put('ø',"oe");
            put('Å',"Aa"); put('å',"aa");
            
            // German characters
            put('ä',"ae"); put('ö',"oe"); put('ü',"ue");
            put('Ä',"Ae"); put('Ö',"Oe"); put('Ü',"Üe");
            put('ß',"ss"); put('ẞ',"SS");

            // Russian chars
            put('а', "a"); put('б', "b"); put('в', "v");  put('г', "g"); put('д', "d"); put('е', "e"); put('ё', "jo"); put('ж', "zh");
            put('з', "z"); put('и', "i"); put('й', "jj"); put('к', "k"); put('л', "l"); put('м', "m"); put('н', "n");  put('о', "o");
            put('п', "p"); put('р', "r"); put('с', "s");  put('т', "t"); put('у', "u"); put('ф', "f"); put('х', "kh"); put('ц', "c");
            put('ч', "ch");put('ш', "sh");put('щ', "shh");put('ъ', "\"");put('ы', "y"); put('ь', "'"); put('э', "eh"); put('ю', "ju");
            put('я', "ja");

            // Hebrew chars
            put('א', "a"); put('ב', "b"); put('ג', "g");  put('ד', "d"); put('ה', "h"); put('ו', "u"); put('ז', "z"); put('ח', "kh");
            put('ט', "t"); put('י', "y"); put('כ', "c"); put('ל', "l"); put('מ', "m"); put('נ', "n"); put('ס', "s");  put('ע', "'");
            put('פ', "p"); put('צ', "ts"); put('ק', "k");  put('ר', "r"); put('ש', "sh"); put('ת', "th"); put('ף', "f"); put('ץ', "ts");
            put('ך', "ch");put('ם', "m");put('ן', "n");

            // Greek chars
            put('α',"a");put('ά',"a");put('β',"v");put('γ',"g");put('δ',"d");put('ε',"e");put('έ',"e");put('ζ',"z");put('η',"i");
            put('ή',"i");put('θ',"th");put('ι',"i");put('ί',"i");put('ϊ',"i");put('ΐ',"i");put('κ',"k");put('λ',"l");put('μ',"m");
            put('ν',"n");put('ξ',"ks");put('ο',"o");put('ό',"o");put('π',"p");put('ρ',"r");put('σ',"s");put('ς',"s");put('τ',"t");
            put('υ',"y");put('ύ',"y");put('ϋ',"y");put('ΰ',"y");put('φ',"f");put('χ',"ch");put('ψ',"ps");put('ω',"o");put('ώ',"o");
            put('Α',"A");put('Ά',"A");put('Β',"B");put('Γ',"G");put('Δ',"D");put('Ε',"E");put('Έ',"E");put('Ζ',"Z");put('Η',"I");
            put('Ή',"I");put('Θ',"TH");put('Ι',"I");put('Ί',"I");put('Ϊ',"I");put('Κ',"K");put('Λ',"L");put('Μ',"M");put('Ν',"N");
            put('Ξ',"KS");put('Ο',"O");put('Ό',"O");put('Π',"P");put('Ρ',"R");put('Σ',"S");put('Τ',"T");put('Υ',"Y");put('Ύ',"Y");
            put('Ϋ',"Y");put('Φ',"F");put('Χ',"CH");put('Ψ',"PS");put('Ω',"O");put('Ώ',"O");

            // Ukrainian characters
            put('ґ', "gh"); put('є', "je"); put('і', "i"); put('ї', "ji"); put('Ґ', "GH"); put('Є', "JE"); put('І', "I"); put('Ї', "JI");

            // Arabic
            put('ا', "a"); put('ب', "b"); put('ت', "t"); put('ث', "th"); put('ج', "j"); put('ح', "7"); put('خ', "5");
            put('د', "d"); put('ذ', "th"); put('ر', "r"); put('ز', "z"); put('س', "s"); put('ش', "sh"); put('ص', "9");
            put('ض', "9'"); put('ط', "6"); put('ظ', "6'"); put('ع', "3"); put('غ', "3'"); put('ف', "f");
            put('ق', "q"); put('ك', "k"); put('ل', "l"); put('م', "m"); put('ن', "n"); put('ه', "h");
            put('و', "w"); put('ي', "y"); put('ى', "a"); put('ﺓ', "");
            put('آ', "2"); put('ئ', "2"); put('إ', "2"); put('ؤ', "2"); put('أ', "2"); put('ء', "2");
            put('٠', "0"); put('١', "1"); put('٢', "2"); put('٣', "3"); put('٤', "4"); put('٥', "5");
            put('٦', "6"); put('٧', "7"); put('٨', "8"); put('٩', "9");

            // Persian(Farsi)
            put('پ', "p"); put('چ', "ch"); put('ژ', "zh"); put('ک', "k"); put('گ', "g"); put('ی', "y"); put('‌', " "); 
            put('؟', "?"); put('٪', "%"); put('؛', ";"); put('،', ","); put('۱', "1"); put('۲', "2"); put('۳', "3"); 
            put('۴', "4"); put('۵', "5"); put('۶', "6"); put('۷', "7"); put('۸', "8"); put('۹', "9"); put('۰', "0"); 
            put('»', "<"); put('«', ">"); put('ِ', "e"); put('َ', "a"); put('ُ', "o"); put('ّ', "");

            // Polish
            put('Ł', "L"); put('ł', "l");

            //Lithuanian
            put('ą', "a"); put('č', "c"); put('ę', "e"); put('ė', "e"); put('į', "i"); put('š', "s"); put('ų', "u"); put('ū', "u"); put('ž', "z");


            // Estonian
            put('ä', "a"); put('Ä', "A");
            put('ö', "o"); put('õ', "o");
            put('Ö', "O"); put('Õ', "O");
            put('ü', "u"); put('Ü', "U");

            // Icelandic
            put('Þ',"Th"); put('þ',"th"); put('Ð',"D"); put('ð',"d");

            // Czech
            put('ř',"r"); put('ě',"e"); put('ý',"y"); put('á',"a"); put('í',"i"); put('é',"e");
            put('ó',"o"); put('ú',"u"); put('ů',"u"); put('ď',"d"); put('ť',"t"); put('ň',"n");

            //TODO: these must be configurable. If someone wants to transliterate cyrillic it does not mean his device has no German umlauts
            // all or nothing is really bad here
        }
    };

    /**
     * Replaces unsupported symbols to english
     * @param txt input text
     * @return transliterated text
     */
    public static String transliterate(String txt){
        if (txt == null || txt.isEmpty()) {
            return txt;
        }

        StringBuilder messageBuilder = new StringBuilder();

        // Simple, char-by-char transliteration.
        char[] chars = txt.toCharArray();
        for (char c : chars)
        {
            messageBuilder.append(transliterate(c));
        }
        String message = messageBuilder.toString();

        // More complex transliteration for specific languages.
        message = BengaliLanguageUtils.transliterate(message);
        message = KoreanLanguageUtils.transliterate(message);

        return flattenToAscii(message);
    }

    /**
     * Replaces unsupported symbol to english by {@code transliterateMap}
     * @param c input char
     * @return replacement text
     */
    private static String transliterate(char c){
        char lowerChar = Character.toLowerCase(c);

        if (transliterateMap.containsKey(lowerChar)) {
            String replace = transliterateMap.get(lowerChar);

            if (lowerChar != c)
            {
                return WordUtils.capitalize(replace);
            }

            return replace;
        }

        return String.valueOf(c);
    }

    /**
     * Converts the diacritics
     * @param string input text
     * @return converted text
     */
    private static String flattenToAscii(String string) {
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        return string.replaceAll("\\p{M}", "");
    }
}