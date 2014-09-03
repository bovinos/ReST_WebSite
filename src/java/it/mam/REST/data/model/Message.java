package it.mam.REST.data.model;

import java.util.Date;

/**
 *
 * @author alex
 */
public interface Message {

    int getID();

    String getTitle();

    void setTitle(String Title);

    String getText();

    void setText(String text);

    Date getDate();

    void setDate(Date date);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Message message);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    User getUser();

    void setUser(User user);

    Series getSeries();

    void setSeries(Series series);

}
