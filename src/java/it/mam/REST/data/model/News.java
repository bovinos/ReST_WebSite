package it.mam.REST.data.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author alex
 */
public interface News {

    int getID();

    String getTitle();

    void setTitle(String title);

    String getText();

    void setText(String text);

    Date getDate();

    void setDate(Date date);

    String getImageURL();

    void setImageURL(String imageURL);

    int getLikes();

    void setLikes(int likes);

    int getDislikes();

    void setDislikes(int dislikes);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(News news);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Comment> getComments();

    void setComments(List<Comment> comments);

    void addComment(Comment comment);

    void removeComment(Comment comment);

    void removeAllComment();

    List<Series> getSeries();

    void setSeries(List<Series> series);

    void addSeries(Series series);

    void removeSeries(Series series);

    void removeAllSeries();

    User getAuthor();

    void setAuthor(User user);

}
