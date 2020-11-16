package ru.asavan.cvlogo;

/**
 * Created by asavan on 23.05.2020.
 */
public interface Templater {
    String getMainTemplate(boolean isNew);

    String getCommitTemplate();
}
