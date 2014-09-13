package it.mam.REST.data.impl;

import it.mam.REST.data.model.RESTDataLayer;
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
    private int userID;
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
        userID = 0;
        seriesID = 0;
        rating = "";
        anticipationNotification = null;
        addDate = null;
        season = 0;
        episode = -1; // poichè episode potrebbe essere 0 ( il plot )
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
    public int getUserID() {
        return userID;
    }

    @Override
    public void setUserID(int userID) {
        this.userID = userID;
        dirty = true;
    }

    @Override
    public int getSeriesID() {
        return seriesID;
    }

    @Override
    public void setSeriesID(int seriesID) {
        this.seriesID = seriesID;
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
        userID = userSeries.getUserID();
        seriesID = userSeries.getSeriesID();
        rating = userSeries.getRating();
        anticipationNotification = userSeries.getAnticipationNotification();
        addDate = userSeries.getAddDate();
        season = userSeries.getSeason();
        episode = userSeries.getEpisode();

        dirty = true;

    }

}
