package it.mam.REST.data.impl;

import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
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

    private List<Series> series;
    private List<User> users;

    public GenreMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        name = "";
        dirty = false;

        this.dataLayer = dataLayer;

        series = null;
        users = null;
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
            this.series = dataLayer.getSeries(this);
        }
        this.series.add(series);
        dirty = true;
    }

    @Override
    public void removeSeries(Series series) {
        if (this.series == null) {
            this.series = dataLayer.getSeries(this);
        }
        this.series.remove(series);
        dirty = true;
    }

    @Override
    public void removeAllSeries() {
        series.clear();
        dirty = true;
    }

    @Override
    public List<User> getUsers() {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        return users;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
        dirty = true;
    }

    @Override
    public void addUser(User user) {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        users.add(user);
        dirty = true;
    }

    @Override
    public void removeUser(User user) {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        users.remove(user);
        dirty = true;
    }

    @Override
    public void removeAllUser() {
        users.clear();
        dirty = true;
    }

    @Override
    public void copyFrom(Genre genre) {
        ID = genre.getID();
        name = genre.getName();

        series = null;
        users = null;

        dirty = true;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "Name: " + name + "\n"
                + "Dirty: " + dirty + "\n"
                + "Series: " + series + "\n"
                + "Users: " + users;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // se hanno lo stesso riferimento restituisco true
            return true;
        }
        if (obj == null || !(obj instanceof Genre)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo Genre quindi posso fare il cast
        Genre g = (Genre) obj;
        return ID == g.getID();
    }

}
