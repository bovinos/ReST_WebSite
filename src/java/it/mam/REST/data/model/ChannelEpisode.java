package it.mam.REST.data.model;

import java.util.Date;

/**
 *
 * @author alex
 */
public interface ChannelEpisode {

    int getChannelID();

    void setChannelID(int ID);

    int getEpisodeID();

    void setEpisodeID(int ID);

    Date getDate();

    void setDate(Date date);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(ChannelEpisode channelEpisode);

}
