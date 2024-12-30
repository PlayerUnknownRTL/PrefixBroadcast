package me.lumia;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RGBcolor {

    public static class RGBcolors {
        public static final char COLOR_CHAR = '§';
        private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F\\d]{6})");
        private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:#([a-fA-F\\d]{6}):#([a-fA-F\\d]{6})>(.*?)</gradient>");

        public RGBcolors() {
        }

        public static String colorize(String message) {
            if (message != null && !message.isEmpty()) {
                // Process gradient tags
                Matcher gradientMatcher = GRADIENT_PATTERN.matcher(message);
                StringBuilder gradientBuilder = new StringBuilder(message.length() + 32);

                while (gradientMatcher.find()) {
                    String startColor = gradientMatcher.group(1);
                    String endColor = gradientMatcher.group(2);
                    String text = gradientMatcher.group(3);

                    String gradientText = applyGradient(startColor, endColor, text);
                    gradientMatcher.appendReplacement(gradientBuilder, gradientText);
                }

                message = gradientMatcher.appendTail(gradientBuilder).toString();

                // Process hex colors
                Matcher hexMatcher = HEX_PATTERN.matcher(message);
                StringBuilder hexBuilder = new StringBuilder(message.length() + 32);

                while (hexMatcher.find()) {
                    String group = hexMatcher.group(1);
                    hexMatcher.appendReplacement(hexBuilder,
                            "§x§" + group.charAt(0) + "§" + group.charAt(1) +
                                    "§" + group.charAt(2) + "§" + group.charAt(3) +
                                    "§" + group.charAt(4) + "§" + group.charAt(5));
                }

                message = hexMatcher.appendTail(hexBuilder).toString();
                return translateAlternateColorCodes('&', message);
            } else {
                return message;
            }
        }

        private static String applyGradient(String startColor, String endColor, String text) {
            Color start = Color.decode("#" + startColor);
            Color end = Color.decode("#" + endColor);

            int length = text.length();
            StringBuilder gradientText = new StringBuilder(length * 14); // Each char takes ~14 chars in output

            for (int i = 0; i < length; i++) {
                float ratio = (float) i / (length - 1);
                int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
                int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
                int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));

                String hexColor = String.format("%02X%02X%02X", red, green, blue);
                gradientText.append("§x");
                for (char c : hexColor.toCharArray()) {
                    gradientText.append("§").append(c);
                }
                gradientText.append(text.charAt(i));
            }

            return gradientText.toString();
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
