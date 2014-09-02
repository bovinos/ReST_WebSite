package it.mam.REST.data.impl;

import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void copyFrom(Episode episode) {
        ID = episode.getID();
        description = episode.getDescription();
        number = episode.getNumber();
        season = episode.getSeason();
        title = episode.getTitle();

        if (episode.getSeries() != null) {
            seriesID = episode.getSeries().getID();
        }

        dirty = true;
    }

}
