package ru.asavan.cvlogo;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by asavan on 21.05.2020.
 */
public class Runner {
    private static final String USERNAME = "asavan";
    private static final String REPO_TO_DRAW = "cvlogo";
    private static final boolean DEBUG_PRINTING = true;
    private static final LocalDate GITHUB_ERROR_SINCE = null;

    public static void main(String[] args) throws IOException {
        asavanPic(true);
    }

    public static void asavanPic(boolean isNew) throws IOException {
        draw(isNew, Pictures.strToSprite(Pictures.ASAVAN3), 12, Color.ONE, 0);
    }

    public static void dot() throws IOException {
        int offset = 0;
        Integer[][] image = new Integer[1][1];
        image[0][0] = 4;
        draw(true, image, offset, null, 60);
    }

    public static void fill() throws IOException {
        draw(false, null, 0, Color.ONE, 0);
    }

    private static void draw(boolean isNew, Integer[][] image, int offset, Color fillColor, int minFourColor) throws IOException {
        Calendar cal = getCalendar();
        if (fillColor != null) {
            cal.setFillColor(fillColor);
        }

        if (minFourColor > 0) {
            cal.setMinColor(Color.FOUR, minFourColor);
        }
        if (DEBUG_PRINTING) {
            System.out.println(cal.minCountPrintable());
        }

        if (DEBUG_PRINTING && image != null) {
            System.out.println(Pictures.printImage(image));
        }
        OsName osName = OsSpecific.chooseOs();
        String output = Commiter.fake_it(image, cal, USERNAME, REPO_TO_DRAW, offset, osName, isNew, GITHUB_ERROR_SINCE);
        if (!output.isEmpty()) {
            OsSpecific.writeOnDisk(REPO_TO_DRAW, output, osName);
            if (isNew) {
                System.out.println(GitHub.createRepo(USERNAME, REPO_TO_DRAW));
            }
        } else {
            System.out.println("No changes");
        }
    }

    private static Calendar getCalendar() {
        return GitHub.getCalendarExtractor(pred ->
                GitHub.retrieveContributionsCalendar(pred, USERNAME, DEBUG_PRINTING)).getCalendar();
    }
}
