package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.RESTDataLayer;
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

    private int userID;

    public CommentMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.title = "";
        this.text = "";
        this.date = null;
        this.likes = 0;
        this.dislikes = 0;
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.userID = 0;
    }

    public CommentMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.ID = rs.getInt("ID");
        this.title = rs.getString("title");
        this.text = rs.getString("text");
        this.date = rs.getDate("date"); // on DB the type of attribute date is TIMESTAMP
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
    public void copyFrom(Comment comment) {
        this.ID = comment.getID();
        this.date = comment.getDate();
        this.dislikes = comment.getDislikes();
        this.likes = comment.getLikes();
        this.text = comment.getText();
        this.title = comment.getTitle();

        this.userID = comment.getUserID();

        this.dirty = true;
    }

}
