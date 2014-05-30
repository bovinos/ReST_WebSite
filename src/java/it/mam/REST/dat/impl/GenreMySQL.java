package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alex
 */
public class GenreMySQL implements Genre {

    private int ID;
    private String name;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    List<Series> series;

    public GenreMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.name = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.series = null;
    }

    public GenreMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.ID = rs.getInt("ID");
        this.name = rs.getString("name");
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public List<Series> getSeries() {
        if (this.series == null) {
            this.series = this.dataLayer.getSeries(this);
        }
        return series;
    }

    @Override
    public void setSeries(List<Series> series) {
        this.series = series;
        this.dirty = true;
    }

    @Override
    public void addSeries(Series series) {
        if (this.series == null) {
            this.getSeries();
        }
    }

    @Override
    public void removeSeries(Series series) {
        if (this.series == null) {
            return;
        }
        this.series.remove(series);
    }

    @Override
    public void removeAllSeries() {
        this.series = null;
    }

    @Override
    public void copyFrom(Genre genre) {
        this.ID = genre.getID();
        this.name = genre.getName();

        this.series = null;

        this.dirty = true;
    }

}
