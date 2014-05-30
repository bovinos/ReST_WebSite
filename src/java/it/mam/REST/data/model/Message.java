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

    /**
     * foreign key with user
     *
     * @return
     */
    int getUserID();

    /**
     * foreign key with series
     *
     * @return
     */
    int getSeriesID();

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Message message);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
}
