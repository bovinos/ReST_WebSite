package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alex
 */
public class NewsMySQL implements News {

    private int ID;
    private String title;
    private String text;
    private Date date;
    private int likes;
    private int dislikes;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private int userID;
    List<Comment> comments;
    List<Series> series;

    public NewsMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.title = "";
        this.text = "";
        this.date = null;
        this.likes = 0;
        this.dislikes = 0;
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.userID = 0;
        this.comments = null;
        this.series = null;
    }

    public NewsMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this.ID = rs.getInt("ID");
        this.title = rs.getString("title");
        this.text = rs.getString("text");
        this.date = rs.getDate("date");
        this.likes = rs.getInt("likes");
        this.dislikes = rs.getInt("dislikes");

        this.userID = rs.getInt("ID_user");
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
    public int getLikes() {
        return likes;
    }

    @Override
    public void setLikes(int likes) {
        this.likes = likes;
        this.dirty = true;
    }

    @Override
    public int getDislikes() {
        return dislikes;
    }

    @Override
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
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
    public List<Comment> getComments() {
        if (this.comments == null) {
            this.comments = this.dataLayer.getComments(this);
        }
        return comments;
    }

    @Override
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.dirty = true;
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
    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.getComments();
        }
        this.comments.add(comment);
    }

    @Override
    public void removeComment(Comment comment) {
        if (this.comments == null) {
            return;
        }
        this.comments.remove(comment);
    }

    @Override
    public void removeAllComment() {
        this.comments = null;
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
    public void copyFrom(News news) {
        this.ID = news.getID();
        this.date = news.getDate();
        this.dislikes = news.getDislikes();
        this.likes = news.getLikes();
        this.text = news.getText();
        this.title = news.getTitle();

        this.userID = news.getUserID();

        this.comments = null;
        this.series = null;

        this.dirty = true;
    }

}
