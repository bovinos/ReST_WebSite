package it.mam.REST.data.impl;

import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
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

    private User user;
    private int userID;
    private List<Comment> comments;
    private List<Series> series;

    public NewsMySQL(RESTDataLayer dataLayer) {

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
        comments = null;
        series = null;
    }

    public NewsMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        title = rs.getString("title");
        text = rs.getString("text");
        date = rs.getDate("date");
        likes = rs.getInt("likes");
        dislikes = rs.getInt("dislikes");

        userID = rs.getInt("ID_user");
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
    // gestire il fatto che l'utente deve essere un admin per poter postare news
    public void setUser(User user) {
        this.user = user;
        userID = user.getID();
        dirty = true;
    }

    @Override
    public List<Comment> getComments() {
        if (comments == null) {
            comments = dataLayer.getComments(this);
        }
        return comments;
    }

    @Override
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        dirty = true;
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
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = dataLayer.getComments(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        comments.add(comment);
        dirty = true;
    }

    @Override
    public void removeComment(Comment comment) {
        if (comments == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        comments.remove(comment);
        dirty = true;
    }

    @Override
    public void removeAllComment() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        comments = null;
        dirty = true;
    }

    @Override
    public void addSeries(Series series) {
        if (this.series == null) {
            this.series = dataLayer.getSeries(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        this.series.add(series);
        dirty = true;
    }

    @Override
    public void removeSeries(Series series) {
        if (this.series == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        this.series.remove(series);
        dirty = true;
    }

    @Override
    public void removeAllSeries() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        series = null;
        dirty = true;
    }

    @Override
    public void copyFrom(News news) {
        ID = news.getID();
        date = news.getDate();
        dislikes = news.getDislikes();
        likes = news.getLikes();
        text = news.getText();
        title = news.getTitle();

        if (news.getUser() != null) {
            userID = news.getUser().getID();
        }

        comments = null;
        series = null;

        dirty = true;
    }

}
