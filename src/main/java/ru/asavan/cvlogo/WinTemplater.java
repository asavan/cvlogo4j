package ru.asavan.cvlogo;
/**
 * Created by asavan on 23.05.2020.
 */
public class WinTemplater implements Templater {
    public static final String MAIN_TEMPLATE = """
            git init {0}
            cd {0}
            echo # {0} >> README.md
            git add README.md
            echo off            
            {1}
            echo on
            git remote add origin {2}:{3}/{0}.git
            git pull origin master
            git push -u origin master
            cd ..
            """;

    public static final String COMMIT_TEMPLATE = """
            set GIT_AUTHOR_DATE="{0}" && set GIT_COMMITTER_DATE="{0}" && git commit --allow-empty -m "emulate {1}" > nul""";

    @Override
    public String getMainTemplate() {
        return MAIN_TEMPLATE;
    }

    @Override
    public String getCommitTemplate() {
        return COMMIT_TEMPLATE;
    }
}
