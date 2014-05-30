package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.RESTDataLayer;
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

    private int seriesID;

    public EpisodeMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.number = -1; // because 0 can be the number of plot 
        this.season = 0;
        this.title = "";
        this.description = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.seriesID = 0;
    }

    public EpisodeMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this.ID = rs.getInt("ID");
        this.number = rs.getInt("number");
        this.season = rs.getInt("season");
        this.title = rs.getString("title");
        this.description = rs.getString("description");

        this.seriesID = rs.getInt("ID_series");
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
        this.dirty = true;
    }

    @Override
    public int getSeason() {
        return season;
    }

    @Override
    public void setSeason(int season) {
        this.season = season;
        this.dirty = true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        this.dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        this.dirty = true;
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
    public int getSeriesID() {
        return seriesID;
    }

    @Override
    public void copyFrom(Episode episode) {
        this.ID = episode.getID();
        this.description = episode.getDescription();
        this.number = episode.getNumber();
        this.season = episode.getSeason();
        this.title = episode.getTitle();

        this.seriesID = episode.getSeriesID();

        this.dirty = true;
    }

}
