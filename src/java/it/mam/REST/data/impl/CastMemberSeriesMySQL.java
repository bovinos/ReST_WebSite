package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alex
 */
public class CastMemberSeriesMySQL implements CastMemberSeries {

    private int ID;
    private CastMember castMember;
    private int castMemberID;
    private Series series;
    private int seriesID;
    private String role;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    public CastMemberSeriesMySQL(RESTDataLayer dl) {

        ID = 0;
        castMember = null;
        castMemberID = 0;
        series = null;
        seriesID = 0;
        role = "";
        dirty = false;

        dataLayer = dl;

    }

    public CastMemberSeriesMySQL(RESTDataLayer dl, ResultSet rs) throws SQLException {

        this(dl);
        ID = rs.getInt("ID");
        castMemberID = rs.getInt("ID_cast_member");
        seriesID = rs.getInt("ID_series");
        role = rs.getString("role");

    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public CastMember getCastMember() {
        if (castMember == null && castMemberID > 0) {
            castMember = dataLayer.getCastMember(castMemberID);
        }
        return castMember;
    }

    @Override
    public void setCastMember(CastMember castMember) {
        this.castMember = castMember;
        castMemberID = castMember.getID();
        dirty = true;
    }

    @Override
    public Series getSeries() {
        if (series == null && seriesID > 0) {
            series = dataLayer.getSeries(seriesID);
        }
        return series;
    }

    @Override
    public void setSeries(Series series) {
        this.series = series;
        seriesID = series.getID();
        dirty = true;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
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
    public void copyFrom(CastMemberSeries castMemberSeries) {

        ID = castMemberSeries.getID();
        role = castMemberSeries.getRole();

        if (castMemberSeries.getCastMember() != null) {
            castMemberID = castMemberSeries.getCastMember().getID();
        }
        if (castMemberSeries.getSeries() != null) {
            seriesID = castMemberSeries.getSeries().getID();
        }

        castMember = null;
        series = null;

        dirty = true;
    }

}
