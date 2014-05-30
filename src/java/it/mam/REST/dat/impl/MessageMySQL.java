package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.RESTDataLayer;
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

    private int userID;
    private int seriesID;

    public MessageMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.title = "";
        this.text = "";
        this.date = null;
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.userID = 0;
        this.seriesID = 0;
    }

    public MessageMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.ID = rs.getInt("ID");
        this.title = rs.getString("title");
        this.text = rs.getString("text");
        this.date = rs.getDate("date"); // on DB the type of attribute date is TIMESTAMP

        this.userID = rs.getInt("ID_user");
        this.seriesID = rs.getInt("ID_series");
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
        this.dirty = true;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.dirty = true;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
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
    public int getUserID() {
        return userID;
    }

    @Override
    public int getSeriesID() {
        return seriesID;
    }

    @Override
    public void copyFrom(Message message) {
        this.ID = message.getID();
        this.date = message.getDate();
        this.text = message.getText();
        this.title = message.getTitle();

        this.seriesID = message.getSeriesID();
        this.userID = message.getUserID();

        this.dirty = true;
    }

}
