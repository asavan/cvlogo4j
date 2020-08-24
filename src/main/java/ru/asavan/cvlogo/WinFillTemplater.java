package ru.asavan.cvlogo;
/**
 * Created by asavan on 23.05.2020.
 */
public class WinFillTemplater implements Templater {
    public static final String MAIN_TEMPLATE = """
            cd {0}
            git add README.md
            echo off            
            {1}
            echo on
            git pull
            git push
            cd ..
            """;

    @Override
    public String getMainTemplate() {
        return MAIN_TEMPLATE;
    }

    @Override
    public String getCommitTemplate() {
        return WinTemplater.COMMIT_TEMPLATE;
    }
}
