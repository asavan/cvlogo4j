package ru.asavan.cvlogo;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by asavan on 23.05.2020.
 */
public class Pictures {
    public static final String ASAVAN3 = """
              ***   ****   ***  *     *  ***   **  *
              * *   *      * *  *     *  * *   **  *
             ** **  *     ** ** **   ** ** **  *** *
             *   *  ****  *   *  *   *  *   *  * * *
             *****     *  *****  ** **  *****  * ***
             *   *     *  *   *   * *   *   *  *  **
            **   ** **** **   **  ***  **   ** *  **
            """;
    public static final String ASAVAN4 = """
           __  ***   ****   ***  *     *  ***   **  *__
           __  * *   *      * *  *     *  * *   **  *__
           __ ** **  *     ** ** **   ** ** **  *** *__
           __ *   *  ****  *   *  *   *  *   *  * * *__
           __ *****     *  *****  ** **  *****  * ***__
           __ *   *     *  *   *   * *   *   *  *  **__
           __**   ** **** **   **  ***  **   ** *  **__
            """;

    public static final String ASAVAN2 = """
               ***    ****    ***  *     *  ***    **   *
               * *    *       * *  *     *  * *    ***  *
              ** **   *      ** **  *   *  ** **   * *  *
              *   *   ****   *   *  *   *  *   *   * ** *
              *****      *   *****   * *   *****   *  * *
              *   *      *   *   *   * *   *   *   *  ***
             **   **  ****  **   **  ***  **   **  *   **
            """;
    private static final Map<Character, Integer> ASCII_TO_NUMBER = Map.of(
            ' ', 0,
            '-', 0,
            '_', 1,
            '~', 2,
            '=', 3,
            '*', 4);

    public static Integer[][] strToSprite(String content) {
        String[] lines = content.split("\n");
        int w = lines[0].length();
        int h = lines.length;
        Integer[][] res = new Integer[h][w];
        int i = 0;
        for (String line : lines) {
            for (int j = 0; j < w; j++) {
                char c = line.charAt(j);
                res[i][j] = ASCII_TO_NUMBER.get(c);
            }
            ++i;
        }
        return res;
    }

    public static Color getColor(int index) {
        return Color.values()[index];
    }

    public static String printImage(Integer[][] image) {
        return Arrays
                .stream(image)
                .map(Arrays::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
