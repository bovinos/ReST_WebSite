package it.mam.REST.data.impl;

import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alex
 */
public class EpisodeMySQL implements Episode {

    private int ID;
    private int number;
    private int season;
    private String title;
    private String description;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private Series series;
    private int seriesID;
    private List<Channel> channels;
    private List<ChannelEpisode> channelEpisode;

    public EpisodeMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        number = -1; // because 0 can be the number of plot 
        season = 0;
        title = "";
        description = "";
        dirty = false;

        this.dataLayer = dataLayer;

        series = null;
        seriesID = 0;
        channels = null;
        channelEpisode = null;
    }

    public EpisodeMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        number = rs.getInt("number");
        season = rs.getInt("season");
        title = rs.getString("title");
        description = rs.getString("description");

        seriesID = rs.getInt("ID_series");
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
        dirty = true;
    }

    @Override
    public int getSeason() {
        return season;
    }

    @Override
    public void setSeason(int season) {
        this.season = season;
        dirty = true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public Series getSeries() {
        if (series == null && seriesID > 0) {
            series = dataLayer.getSeries(seriesID);
        }

        return series;
    }

    @Override
    public void setSeries(Series series) {
        this.series = series;
        seriesID = series.getID();
        dirty = true;
    }

    @Override
    public List<Channel> getChannels() {
        if (channels == null) {
            channels = dataLayer.getChannels(this);
        }
        return channels;
    }

    @Override
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        dirty = true;
    }

    @Override
    public void addChannel(Channel channel) {
        if (channels == null) {
            channels = dataLayer.getChannels(this);
        }
        channels.add(channel);
        dirty = true;
    }

    @Override
    public void removeChannel(Channel channel) {
        if (channels == null) {
            channels = dataLayer.getChannels(this);
        }
        channels.remove(channel);
        dirty = true;
    }

    @Override
    public void removeAllChannels() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        channels = null;
        dirty = true;
    }

    @Override
    public List<ChannelEpisode> getChannelEpisode() {
        if (channelEpisode == null) {
            channelEpisode = dataLayer.getChannelEpisodeByEpisode(this);
        }
        return channelEpisode;
    }

    @Override
    public void setChannelEpisode(List<ChannelEpisode> channelEpisode) {
        this.channelEpisode = channelEpisode;
    }

    @Override
    public void copyFrom(Episode episode) {
        ID = episode.getID();
        description = episode.getDescription();
        number = episode.getNumber();
        season = episode.getSeason();
        title = episode.getTitle();

        if (episode.getSeries() != null) {
            seriesID = episode.getSeries().getID();
        }

        channels = null;
        channelEpisode = null;

        dirty = true;
    }

}
