package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alex
 */
public class ChannelMySQL implements Channel {

    private int ID;
    private String name;
    private int number;
    private String type;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    List<Series> series;

    public ChannelMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.name = "";
        this.number = 0;
        this.type = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.series = null;
    }

    public ChannelMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.name = rs.getString("name");
        this.number = rs.getInt("number");
        this.type = rs.getString("type");
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
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
        this.dirty = true;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) { // set type onli if the string type is "FREE" or "PAY" otherwise do nothing
        if (type.equalsIgnoreCase("FREE") || type.equalsIgnoreCase("PAY")) {
            this.type = type;
            this.dirty = true;
        }
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
        this.series = null;
    }

    @Override
    public void copyFrom(Channel channel) {
        this.ID = channel.getID();
        this.name = channel.getName();
        this.number = channel.getNumber();
        this.type = channel.getType();

        this.series = null;

        this.dirty = true;
    }

}
