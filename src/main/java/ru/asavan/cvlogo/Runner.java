package ru.asavan.cvlogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by asavan on 21.05.2020.
 */
public class Runner {
    public static final String USERNAME = "asavan";
    public static final String GITHUB_BASE_URL = "https://github.com/";
    private static final boolean DEBUG_PRINTING = true;

    public static void main(String[] args) throws IOException {
        draw(false, Pictures.strToSprite(Pictures.ASAVAN3));
    }

    private static void draw(boolean isNew, Integer[][] image) throws IOException {
        Calendar cal = getCalendar();
        cal.setFillColor(Color.ONE);
        cal.setMinColor(Color.FOUR, 32);
        if (DEBUG_PRINTING) {
            System.out.println(cal.minCountPrintable());
        }

        int offset = 9;
        if (DEBUG_PRINTING && image != null) {
            System.out.println(Pictures.printImage(image));
        }
        String repo = "cvlogo";
        OsName osName = chooseOs();
        String output = isNew ? Commiter.fake_it(image, cal, USERNAME, repo, offset, osName) : Commiter.fill(image, cal, repo, offset);
        if (!output.isEmpty()) {
            writeOnDisk(repo, output, osName);
        } else {
            System.out.println("No changes");
        }
    }

    private static void writeOnDisk(String repo, String output, OsName osName) throws IOException {
        String scriptName = getFileName(repo, osName);
        save(output, scriptName);
        System.out.println(scriptName + " saved.");
        System.out.println(String.format("Create a new(!) repo named %s at %s and run the script", repo, GITHUB_BASE_URL));
    }

    private static Calendar getCalendar() {
        CalendarExtractor calendarExtractor = getCalendarExtractor(Runner::retrieveContributionsCalendar);
        calendarExtractor.adjustDay("2019-08-31");
        calendarExtractor.adjustDay("2020-04-12");
        calendarExtractor.adjustDay("2020-06-06");
        return calendarExtractor.getCalendar();
    }

    static CalendarExtractor getCalendarExtractor(Consumer<Predicate<String>> consumer) {
        MemoryCollector memoryCollector = new MemoryCollector();
        SvgExtractor svgExtractor = new SvgExtractor(memoryCollector);
        ColorExtractor colorExtractor = new ColorExtractor();
        LegendExtractor legendExtractor = new LegendExtractor(colorExtractor);
        ListPredicate predicate = new ListPredicate(Arrays.asList(svgExtractor, legendExtractor));
        consumer.accept(predicate);
        CalendarExtractor calendarExtractor = new CalendarExtractor(colorExtractor.getColorMap());
        calendarExtractor.extract(memoryCollector.getArr());
        return calendarExtractor;
    }

    private static void dot() throws IOException {
        Calendar cal = getCalendar();
        cal.setMinColor(Color.FOUR, 60);
        if (DEBUG_PRINTING) {
            System.out.println(cal.minCountPrintable());
        }
        int offset = 0;
        Integer[][] image = new Integer[1][1];
        image[0][0] = 4;
        String repo = "dotcvlogo6";
        OsName osName = chooseOs();
        String output = Commiter.fake_it(image, cal, USERNAME, repo, offset, osName);
        writeOnDisk(repo, output, osName);
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
