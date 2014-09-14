package it.mam.REST.data.model;

/**
 *
 * @author alex
 */
public interface CastMemberSeries {

    int getID();

    CastMember getCastMember();

    void setCastMember(CastMember castMember);

    Series getSeries();

    void setSeries(Series series);

    String getRole();

    void setRole(String role);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(CastMemberSeries castMemberSeries);

}
