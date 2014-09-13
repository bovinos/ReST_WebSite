package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.RESTDataLayer;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alex
 */
public class CastMemberSeriesMySQL implements CastMemberSeries {

    private int ID;
    private int castMemberID;
    private int seriesID;
    private String role;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    public CastMemberSeriesMySQL(RESTDataLayer dl) {

        ID = 0;
        castMemberID = 0;
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
    public int getCastMemberID() {
        return castMemberID;
    }

    @Override
    public void setCastMemberID(int castMemberID) {
        this.castMemberID = castMemberID;
        dirty = true;
    }

    @Override
    public int getSeriesID() {
        return seriesID;
    }

    @Override
    public void setSeriesID(int seriesID) {
        this.seriesID = seriesID;
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
        castMemberID = castMemberSeries.getCastMemberID();
        seriesID = castMemberSeries.getSeriesID();
        role = castMemberSeries.getRole();

        dirty = true;
    }

}
