package it.mam.REST.data.impl;

import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.RESTDataLayer;
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

    List<Episode> episodes;

    public ChannelMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        name = "";
        number = 0;
        type = "";
        dirty = false;

        this.dataLayer = dataLayer;

        episodes = null;
    }

    public ChannelMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        name = rs.getString("name");
        number = rs.getInt("number");
        type = rs.getString("type");
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
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
        dirty = true;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) { // set type onli if the string type is "FREE" or "PAY" otherwise do nothing
        if (type.equalsIgnoreCase(FREE) || type.equalsIgnoreCase(PAY)) {
            this.type = type.toUpperCase();
            dirty = true;
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
    public List<Episode> getEpisodes() {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
        }
        return episodes;
    }

    @Override
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        dirty = true;
    }

    @Override
    public void addEpisode(Episode episode) {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
        }
        episodes.add(episode);
        dirty = true;
    }

    @Override
    public void removeEpisode(Episode episode) {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
        }
        episodes.remove(episode);
        dirty = true;
    }

    @Override
    public void removeAllEpisodes() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        episodes = null;
        dirty = true;
    }

    @Override
    public void copyFrom(Channel channel) {
        ID = channel.getID();
        name = channel.getName();
        number = channel.getNumber();
        type = channel.getType();

        episodes = null;

        dirty = true;
    }

}
