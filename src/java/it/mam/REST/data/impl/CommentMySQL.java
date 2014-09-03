package it.mam.REST.data.impl;

import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.News;
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
public class CommentMySQL implements Comment {

    private int ID;
    private String title;
    private String text;
    private Date date;
    private int likes;
    private int dislikes;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private User user;
    private int userID;
    private News news;
    private int newsID;
    private Series series;
    private int seriesID;

    public CommentMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        title = "";
        text = "";
        date = null;
        likes = 0;
        dislikes = 0;
        dirty = false;

        this.dataLayer = dataLayer;

        user = null;
        userID = 0;
        news = null;
        newsID = 0;
        series = null;
        seriesID = 0;
    }

    public CommentMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        title = rs.getString("title");
        text = rs.getString("text");
        date = new Date(rs.getTimestamp("date").getTime()); // on DB the type of attribute date is TIMESTAMP
        likes = rs.getInt("likes");
        dislikes = rs.getInt("dislikes");

        userID = rs.getInt("ID_user");
        newsID = rs.getInt("ID_news");
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
    public int getLikes() {
        return likes;
    }

    @Override
    public void setLikes(int likes) {
        this.likes = likes;
        dirty = true;
    }

    @Override
    public int getDislikes() {
        return dislikes;
    }

    @Override
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
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
    public News getNews() {
        if (news == null && newsID > 0) {
            news = dataLayer.getNews(newsID);
        }

        return news;
    }

    @Override
    public void setNews(News news) {
        this.news = news;
        newsID = news.getID();
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
    public void copyFrom(Comment comment) {
        ID = comment.getID();
        date = comment.getDate();
        dislikes = comment.getDislikes();
        likes = comment.getLikes();
        text = comment.getText();
        title = comment.getTitle();

        if (comment.getUser() != null) {
            userID = comment.getUser().getID();
        }
        if (comment.getNews() != null) {
            newsID = comment.getNews().getID();
        }
        if (comment.getSeries() != null) {
            seriesID = comment.getSeries().getID();
        }

        dirty = true;
    }

}
