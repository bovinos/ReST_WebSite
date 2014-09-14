package it.mam.REST.data.model;

import java.util.Date;

/**
 *
 * @author alex
 */
public interface ChannelEpisode {

    int getID();

    Channel getChannel();

    void setChannel(Channel channel);

    Episode getEpisode();

    void setEpisode(Episode episode);

    Date getDate();

    void setDate(Date date);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(ChannelEpisode channelEpisode);

}
