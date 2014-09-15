package it.mam.REST.data.impl;

import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author alex
 */
public class MessageMySQL implements Message {

    private int ID;
    private String title;
    private String text;
    private Date date;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private User user;
    private int userID;
    private Series series;
    private int seriesID;

    public MessageMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        title = "";
        text = "";
        date = null;
        dirty = false;

        this.dataLayer = dataLayer;

        user = null;
        userID = 0;
        series = null;
        seriesID = 0;
    }

    public MessageMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        title = rs.getString("title");
        text = rs.getString("text");
        date = rs.getDate("date"); // on DB the type of attribute date is TIMESTAMP

        userID = rs.getInt("ID_user");
        seriesID = rs.getInt("ID_series");
    }

    @Override
    public int getID() {
        return ID;
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
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
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
    public User getUser() {
        if (user == null && userID > 0) {
            user = dataLayer.getUser(userID);
        }

        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        userID = user.getID();
        dirty = true;
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
    public void copyFrom(Message message) {
        ID = message.getID();
        date = message.getDate();
        text = message.getText();
        title = message.getTitle();

        if (message.getSeries() != null) {
            seriesID = message.getSeries().getID();
        }
        if (message.getUser() != null) {
            userID = message.getUser().getID();
        }

        dirty = true;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "Date: " + date + "\n"
                + "Text: " + text + "\n"
                + "Title: " + title + "\n"
                + "Dirty: " + dirty + "\n"
                + "UserID: " + userID + "\n"
                + "User: " + user + "\n"
                + "SeriesID: " + seriesID + "\n"
                + "Series: " + series;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // se hanno lo stesso riferimento restituisco true
            return true;
        }
        if (obj == null || !(obj instanceof Message)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo Message quindi posso fare il cast
        Message m = (Message) obj;
        return ID == m.getID();
    }

}
