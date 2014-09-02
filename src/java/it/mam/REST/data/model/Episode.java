package it.mam.REST.data.model;

/**
 *
 * @author alex
 */
public interface Episode {

    int getID();

    int getNumber();

    void setNumber(int number);

    int getSeason();

    void setSeason(int season);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Series getSeries();

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Episode episode);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
}
