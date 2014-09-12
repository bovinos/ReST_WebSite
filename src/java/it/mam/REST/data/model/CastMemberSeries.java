package it.mam.REST.data.model;

/**
 *
 * @author alex
 */
public interface CastMemberSeries {

    int getCastMemberID();

    void setCastMemberID(int ID);

    int getSeriesID();

    void setSeriesID(int ID);

    String getRole();

    void setRole(String role);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(CastMemberSeries castMemberSeries);

}
