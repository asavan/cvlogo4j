package ru.asavan.cvlogo;
/**
 * Created by asavan on 23.05.2020.
 */
public class LinuxTemplater implements Templater {
    public static final String MAIN_TEMPLATE = """
            #!/usr/bin/env bash
            git init {0}
            cd {0}
            touch README.md
            git add README.md
            {1}
            git remote add origin {2}:{3}/{0}.git
            git pull origin master
            git push -u origin master
            """;

    public static final String MAIN_FILL_TEMPLATE = """
            #!/usr/bin/env bash
            cd {0}
            git add README.md
            echo off            
            {1}
            echo on
            git pull
            git push
            cd ..
            """;

    public static final String COMMIT_TEMPLATE = """
            GIT_AUTHOR_DATE={0} GIT_COMMITTER_DATE={0} git commit --allow-empty -m "emulate {1}" > /dev/null""";

    @Override
    public String getMainTemplate(boolean isNew) {
        return isNew ? MAIN_TEMPLATE : MAIN_FILL_TEMPLATE;
    }

    @Override
    public String getCommitTemplate() {
        return COMMIT_TEMPLATE;
    }
}
