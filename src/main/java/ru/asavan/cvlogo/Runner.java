package ru.asavan.cvlogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by asavan on 21.05.2020.
 */
public class Runner {
    private static final String USERNAME = "asavan";
    private static final String REPO_TO_DRAW = "cvlogo";
    private static final String GITHUB_BASE_URL = "https://github.com/";
    private static final boolean DEBUG_PRINTING = true;
    private static final LocalDate GITHUB_ERROR_SINCE = Parser.getLocalDate("2020-07-04"); // or null to switch off

    public static void main(String[] args) throws IOException {
        asavanPic(true);
    }

    public static void asavanPic(boolean isNew) throws IOException {
        draw(isNew, Pictures.strToSprite(Pictures.ASAVAN3), 9, Color.ONE, 0);
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
        OsName osName = chooseOs();
        String output = Commiter.fake_it(image, cal, USERNAME, REPO_TO_DRAW, offset, osName, isNew, GITHUB_ERROR_SINCE);
        if (!output.isEmpty()) {
            writeOnDisk(REPO_TO_DRAW, output, osName, isNew);
        } else {
            System.out.println("No changes");
        }
    }

    private static void writeOnDisk(String repo, String output, OsName osName, boolean isNew) throws IOException {
        String scriptName = getFileName(repo, osName);
        save(output, scriptName);
        System.out.println(scriptName + " saved.");
        if (isNew) {
            System.out.println(String.format("Create a new(!) repo named %s at %s and run the script", repo, GITHUB_BASE_URL));
        }
    }

    private static Calendar getCalendar() {
        return getCalendarExtractorWithAdjustment(Runner::retrieveContributionsCalendar).getCalendar();
    }

    static CalendarExtractor getCalendarExtractorWithAdjustment(Consumer<Predicate<String>> consumer) {
        CalendarExtractor calendarExtractor = getCalendarExtractor(consumer);
        calendarExtractor.adjustDay("2020-04-12");
        calendarExtractor.adjustDay("2020-08-16");
        return calendarExtractor;
    }

    static CalendarExtractor getCalendarExtractor(Consumer<Predicate<String>> consumer) {
        MemoryCollector memoryCollector = new MemoryCollector();
        SvgExtractor svgExtractor = new SvgExtractor(memoryCollector);
        ColorExtractor colorExtractor = new ColorExtractor();
        consumer.accept(svgExtractor);
        CalendarExtractor calendarExtractor = new CalendarExtractor(colorExtractor);
        calendarExtractor.extract(memoryCollector.getArr());
        return calendarExtractor;
    }


    private static void retrieveContributionsCalendar(Predicate<String> pred) {
        String base_url = GITHUB_BASE_URL + "users/" + USERNAME;
        String url = base_url + "/contributions";
        if (DEBUG_PRINTING) {
            System.out.println(url);
        }
        try {
            URL u = new URL(url);

            try (InputStream in = u.openStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    if (pred.test(inputLine)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static OsName chooseOs() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return OsName.WIN;
        }
        if (isUnix(OS)) {
            return OsName.LINUX;
        }
        return OsName.OTHER;
    }

    public static boolean isUnix(String OS) {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }

    private static void save(String output, String scriptName) throws IOException {
        Files.writeString(Paths.get(scriptName), output);
    }

    private static String getExtension(OsName os) {
        if (os == OsName.WIN) {
            return ".bat";
        }
        return ".sh";
    }

    private static String getFileName(String name, OsName os) {
        return name + getExtension(os);
    }
}
