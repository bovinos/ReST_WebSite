package it.mam.REST.data.model;

import java.util.List;

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

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Episode episode);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Channel> getChannels();

    void setChannels(List<Channel> channels);

    void addChannel(Channel channel);

    void removeChannel(Channel channel);

    void removeAllChannels();

    Series getSeries();

    void setSeries(Series series);

}
