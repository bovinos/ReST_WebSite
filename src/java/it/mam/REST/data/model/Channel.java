package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Channel {

    int getID();

    String getName();

    void setName(String name);

    int getNumber();

    void setNumber(int number);

    String getType();

    void setType(String Type);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Channel channel);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Series> getSeries();

    void setSeries(List<Series> series);

    void addSeries(Series series);

    void removeSeries(Series series);

    void removeAllSeries();

}
