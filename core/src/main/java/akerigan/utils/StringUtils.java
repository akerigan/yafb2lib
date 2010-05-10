package akerigan.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 25.04.2010
 * Time: 21:28:03
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class StringUtils {

    static final Map<String, String> patternsMap = new HashMap<String, String>();

    static {
        patternsMap.put(" ", "\\s+");
        patternsMap.put("-", "\\s*-\\s*");
        patternsMap.put("_", "\\s*_\\s*");
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static String hexencode(byte[] bs) {
        StringBuffer sb = new StringBuffer(bs.length * 2);
        for (byte element : bs) {
            int c = element & 0xFF;
            if (c < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(c));
        }

        return sb.toString();
    }

    public static String capitalize(String src) {
        if (src != null) {
            return src.substring(0, 1).toUpperCase() + src.substring(1);
        } else {
            return null;
        }
    }

    public static String capitalize2(String src, boolean useDelimiters) {
        if (src != null) {
            StringBuilder result = new StringBuilder(src);
            boolean processed = false;
            for (String patternReplacement : patternsMap.keySet()) {
                String[] splitted = result.toString().split(patternsMap.get(patternReplacement));
                if (splitted.length > 1) {
                    result = new StringBuilder();
                    processed = true;
                    for (String subString : splitted) {
                        if (useDelimiters && result.length() != 0) {
                            result.append(patternReplacement);
                        }
                        result.append(capitalize(subString));
                    }
                    break;
                }
            }
            if (processed) {
                return result.toString();
            } else {
                return capitalize(src);
            }
        } else {
            return null;
        }
    }

    public static String capitalize3(String src, String delimiter) {
        if (src != null) {
            String[] splitted = src.toLowerCase().split("\\s*[\\.\\s\\(\\)_-]+\\s*");
            if (splitted.length > 1) {
                StringBuilder result = new StringBuilder();
                for (String subString : splitted) {
                    if (!StringUtils.isEmpty(subString)) {
                        if (delimiter != null && result.length() != 0) {
                            result.append(delimiter);
                        }
                        result.append(capitalize(subString));
                    }
                }
                return result.toString();
            } else {
                return capitalize(src.toLowerCase());
            }
        } else {
            return null;
        }
    }

    public static String decapitalize(String src) {
        return src.substring(0, 1).toLowerCase() + src.substring(1);
    }
}
