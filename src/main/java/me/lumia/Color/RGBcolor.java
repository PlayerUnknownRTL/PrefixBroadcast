package me.lumia.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RGBcolor {

    public static class RGBcolors {
        public static final char COLOR_CHAR = '§';
        private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F\\d]{6})");

        public RGBcolors() {
        }

        public static String colorize(String message) {
            if (message != null && !message.isEmpty()) {
                Matcher matcher = HEX_PATTERN.matcher(message); // Исправлено 'mather' на 'matcher'
                StringBuilder builder = new StringBuilder(message.length() + 32);

                while (matcher.find()) {
                    String group = matcher.group(1);
                    matcher.appendReplacement(builder,
                            "§x§" + group.charAt(0) + "§" + group.charAt(1) +
                                    "§" + group.charAt(2) + "§" + group.charAt(3) +
                                    "§" + group.charAt(4) + "§" + group.charAt(5));
                }

                message = matcher.appendTail(builder).toString();
                return translateAlternateColorCodes('&', message);
            } else {
                return message;
            }
        }

        public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
            char[] b = textToTranslate.toCharArray();
            int i = 0;

            for (int length = b.length - 1; i < length; ++i) {
                if (b[i] == altColorChar && isValidColorCharacter(b[i + 1])) {
                    b[i++] = 167;
                    b[i] = (char) (b[i] | 32);
                }
            }

            return new String(b);
        }

        private static boolean isValidColorCharacter(char c) {
            return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c == 'r' ||
                    c >= 'k' && c <= 'o' || c == 'x' ||
                    c >= 'A' && c <= 'F' || c == 'R' ||
                    c >= 'K' && c <= 'O' || c == 'X';
        }
    }
}
