package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alex
 */
public class CastMemberMySQL implements CastMember {

    private int ID;
    private String name;
    private String surname;
    private Date birthDate;
    private String gender;
    private String country;
    private String imageURL;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    List<Series> series;

    public CastMemberMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        name = "";
        surname = "";
        birthDate = null;
        gender = "";
        country = "";
        imageURL = "";
        dirty = false;

        this.dataLayer = dataLayer;

        series = null;
    }

    public CastMemberMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        name = rs.getString("name");
        surname = rs.getString("surname");
        birthDate = rs.getDate("birth_date");
        gender = rs.getString("gender");
        country = rs.getString("country");
        imageURL = rs.getString("image_URL");
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        dirty = true;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
        dirty = true;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        dirty = true;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender.toUpperCase();
        dirty = true;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
        dirty = true;
    }

    @Override
    public String getImageURL() {
        return imageURL;
    }

    @Override
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
    public List<Series> getSeries() {
        if (series == null) {
            series = dataLayer.getSeries(this);
        }
        return series;
    }

    @Override
    public void setSeries(List<Series> series) {
        this.series = series;
        dirty = true;
    }

    @Override
    // here?
    public List<Series> getSeries(String role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSeries(Series series) {
        if (this.series == null) {
            this.series = dataLayer.getSeries(this);
        }
        this.series.add(series);
        dirty = true;
    }

    @Override
    public void removeSeries(Series series) {
        if (this.series == null) {
            this.series = dataLayer.getSeries(this);
        }
        this.series.remove(series);
        dirty = true;
    }

    @Override
    public void removeAllSeries() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        series = null;
        dirty = true;
    }

    @Override
    public boolean isSeriesSet() {
        return series == null;
    }

    @Override
    public void copyFrom(CastMember castMember) {
        ID = castMember.getID();
        birthDate = castMember.getBirthDate();
        country = castMember.getCountry();
        gender = castMember.getGender();
        imageURL = castMember.getImageURL();
        name = castMember.getName();
        surname = castMember.getSurname();

        series = null;

        dirty = true;
    }

}
