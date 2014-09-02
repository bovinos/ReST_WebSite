package it.mam.REST.data.impl;

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

        ID = 0;
        name = "";
        dirty = false;

        this.dataLayer = dataLayer;

        series = null;
    }

    public GenreMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        name = rs.getString("name");
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
    public List<Series> getSeries() {
        if (series == null) {
            series = dataLayer.getSeries(this);
        }
        return series;
    }

    @Override
    public void setSeries(List<Series> series) {
        this.series = series;
        dirty = true;
    }

    @Override
    public void addSeries(Series series) {
        if (this.series == null) {
            getSeries();
        }
        this.series.add(series);
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
        series = null;
    }

    @Override
    public void copyFrom(Genre genre) {
        ID = genre.getID();
        name = genre.getName();

        series = null;

        dirty = true;
    }

}
