package it.mam.REST.data.impl;

import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.RESTDataLayer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author alex
 */
public class ChannelEpisodeMySQL implements ChannelEpisode {

    private int ID;
    private Channel channel;
    private int channelID;
    private Episode episode;
    private int episodeID;
    private Date date;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    public ChannelEpisodeMySQL(RESTDataLayer dl) {

        ID = 0;
        channel = null;
        channelID = 0;
        episode = null;
        episodeID = 0;
        date = null;
        dirty = false;

        dataLayer = dl;

    }

    public ChannelEpisodeMySQL(RESTDataLayer dl, ResultSet rs) throws SQLException {

        this(dl);
        ID = rs.getInt("ID");
        channelID = rs.getInt("ID_channel");
        episodeID = rs.getInt("ID_episode");
        date = new Date(rs.getTimestamp("date").getTime());

    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public Channel getChannel() {
        if (channel == null && channelID > 0) {
            channel = dataLayer.getChannel(channelID);
        }
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
        channelID = channel.getID();
        dirty = true;
    }

    @Override
    public Episode getEpisode() {
        if (episode == null && episodeID > 0) {
            episode = dataLayer.getEpisode(episodeID);
        }
        return episode;
    }

    @Override
    public void setEpisode(Episode episode) {
        this.episode = episode;
        episodeID = episode.getID();
        dirty = true;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
        dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void copyFrom(ChannelEpisode channelEpisode) {

        ID = channelEpisode.getID();
        date = channelEpisode.getDate();

        if (channelEpisode.getChannel() != null) {
            channelID = channelEpisode.getChannel().getID();
        }
        if (channelEpisode.getEpisode() != null) {
            episodeID = channelEpisode.getEpisode().getID();
        }

        channel = null;
        episode = null;

        dirty = true;
    }

}
