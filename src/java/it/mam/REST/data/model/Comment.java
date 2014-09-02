package it.mam.REST.data.model;

import java.util.Date;

/**
 *
 * @author alex
 */
public interface Comment {

    int getID();

    String getTitle();

    void setTitle(String title);

    String getText();

    void setText(String text);

    Date getDate();

    void setDate(Date date);

    int getLikes();

    void setLikes(int likes);

    int getDislikes();

    void setDislikes(int dislikes);

    User getUser();

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Comment comment);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
}
