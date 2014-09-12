package it.mam.REST.data.impl;

import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.RESTDataLayer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author alex
 */
public class ChannelEpisodeMySQL implements ChannelEpisode {

    private int channelID;
    private int episodeID;
    private Date date;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    public ChannelEpisodeMySQL(RESTDataLayer dl) {

        channelID = 0;
        episodeID = 0;
        date = null;
        dirty = false;

        dataLayer = dl;

    }

    public ChannelEpisodeMySQL(RESTDataLayer dl, ResultSet rs) throws SQLException {

        this(dl);
        channelID = rs.getInt("ID_channel");
        episodeID = rs.getInt("ID_episode");
        date = new Date(rs.getTimestamp("date").getTime());

    }

    @Override
    public int getChannelID() {
        return channelID;
    }

    @Override
    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    @Override
    public int getEpisodeID() {
        return episodeID;
    }

    @Override
    public void setEpisodeID(int episodeID) {
        this.episodeID = episodeID;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
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

        channelID = channelEpisode.getChannelID();
        episodeID = channelEpisode.getEpisodeID();
        date = channelEpisode.getDate();

        dirty = true;
    }

}
