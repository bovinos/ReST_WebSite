package it.mam.REST.data.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author alex
 */
public interface Cast {

    int getID();

    String getName();

    void setName(String name);

    String getSurname();

    void setSurname(String surname);

    Date getBirthDate();

    void setBirthDate(Date BirthDate);

    String getGender();

    void setGender(String gender);

    String getCountry();

    void setCountry(String country);

    String getImageURL();

    void setImageURL(String imageURL);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Cast castMember);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Series> getSeries();

    List<Series> getSeries(String role);

    void setSeries(List<Series> series);

    void addSeries(Series series);

    void removeSeries(Series series);

    void removeAllSeries();

    boolean isSeriesSet();

}
