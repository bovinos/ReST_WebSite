package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Channel {

    String FREE = "FREE";
    String PAY = "PAY";

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
    List<Episode> getEpisodes();

    void setEpisodes(List<Episode> episodes);

    void addEpisode(Episode episode);

    void removeEpisode(Episode episode);

    void removeAllEpisodes();

    List<ChannelEpisode> getChannelEpisode();

    void setChannelEpisode(List<ChannelEpisode> channelEpisode);

}
