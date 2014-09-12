package it.mam.REST.data.model;

import java.util.Date;

/**
 *
 * @author alex
 */
public interface UserSeries {

    String ONE = "1";
    String TWO = "2";
    String THREE = "3";
    String FOUR = "4";
    String FIVE = "5";

    int getUserID();

    void setUserID(int ID);

    int getSeriesID();

    void setSeriesID(int ID);

    String getRating();

    void setRating(String rating);

    /**
     * <Date? sul DB è Time!! >
     */
    Date getAnticipationNotification();

    void setAnticipationNotification(Date anticipation);

    Date getAddDate();

    void setAddDate(Date addDate);

    int getSeason();

    void setSeason(int season);

    int getEpisode();

    void setEpisode(int episode);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(UserSeries userSeries);

}
