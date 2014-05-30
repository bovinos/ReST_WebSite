package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Genre {

    int getID();

    String getName();

    void setName(String name);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Genre genre);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Series> getSeries();

    void setSeries(List<Series> series);

    void addSeries(Series series);

    void removeSeries(Series series);

    void removeAllSeries();

}
