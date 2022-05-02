package ru.asavan.cvlogo.templater;

import ru.asavan.cvlogo.OsName;

/**
 * Created by asavan on 02.05.2022.
 */
public class TemplaterFactory {
    private TemplaterFactory(){}
    public static Templater chooseTemplater(OsName osName) {
        if (osName == OsName.WIN) {
            return new WinTemplater();
        }
        return new LinuxTemplater();
    }
}
