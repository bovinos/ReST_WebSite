package it.mam.REST.data.impl;

import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author alex
 */
public class UserSeriesMySQL implements UserSeries {

    private int ID;
    private User user;
    private int userID;
    private Series series;
    private int seriesID;
    private String rating;
    private Date anticipationNotification;
    private Date addDate;
    private int season;
    private int episode;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    public UserSeriesMySQL(RESTDataLayer dl) {

        ID = 0;
        user = null;
        userID = 0;
        series = null;
        seriesID = 0;
        rating = UserSeries.ZERO;
        anticipationNotification = null;
        addDate = null;
        season = 0;
        episode = 0;
        dirty = false;

        dataLayer = dl;

    }

    public UserSeriesMySQL(RESTDataLayer dl, ResultSet rs) throws SQLException {

        this(dl);
        ID = rs.getInt("ID");
        userID = rs.getInt("ID_user");
        seriesID = rs.getInt("ID_series");
        rating = rs.getString("rating");
        anticipationNotification = rs.getTime("anticipation_notification");
        addDate = rs.getDate("add_date");
        season = rs.getInt("season");
        episode = rs.getInt("episode");

    }

    @Override
    public int getID() {
        return ID;
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
    public String getRating() {
        return rating;
    }

    @Override
    public void setRating(String rating) {
        this.rating = rating;
        dirty = true;
    }

    @Override
    public Date getAnticipationNotification() {
        return anticipationNotification;
    }

    @Override
    public void setAnticipationNotification(Date anticipationNotification) {
        this.anticipationNotification = anticipationNotification;
        dirty = true;
    }

    @Override
    public Date getAddDate() {
        return addDate;
    }

    @Override
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
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
    public int getEpisode() {
        return episode;
    }

    @Override
    public void setEpisode(int episode) {
        this.episode = episode;
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
    public void copyFrom(UserSeries userSeries) {

        ID = userSeries.getID();
        rating = userSeries.getRating();
        anticipationNotification = userSeries.getAnticipationNotification();
        addDate = userSeries.getAddDate();
        season = userSeries.getSeason();
        episode = userSeries.getEpisode();

        if (userSeries.getUser() != null) {
            userID = userSeries.getUser().getID();
        }
        if (userSeries.getSeries() != null) {
            seriesID = userSeries.getSeries().getID();
        }

        user = null;
        series = null;

        dirty = true;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "Rating: " + rating + "\n"
                + "AnticipationNotification: " + anticipationNotification + "\n"
                + "AddDate: " + addDate + "\n"
                + "Season: " + season + "\n"
                + "Episode: " + episode + "\n"
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
        if (obj == null || !(obj instanceof UserSeries)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo UserSeries quindi posso fare il cast
        UserSeries us = (UserSeries) obj;
        return ID == us.getID();
    }

}
