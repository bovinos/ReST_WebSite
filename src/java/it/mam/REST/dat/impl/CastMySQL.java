package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Cast;
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
public class CastMySQL implements Cast {

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

    public CastMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.name = "";
        this.surname = "";
        this.birthDate = null;
        this.gender = "";
        this.country = "";
        this.imageURL = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.series = null;
    }

    public CastMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.ID = rs.getInt("ID");
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
        this.birthDate = rs.getDate("birth_date");
        this.gender = rs.getString("gender");
        this.country = rs.getString("country");
        this.imageURL = rs.getString("image_URL");
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
        this.dirty = true;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
        this.dirty = true;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        this.dirty = true;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) { // gender is set only if the parameter is the string "M" or "F" otherwise do nothing
        if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F")) {
            this.gender = gender;
            this.dirty = true;
        }
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
        this.dirty = true;
    }

    @Override
    public String getImageURL() {
        return imageURL;
    }

    @Override
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
        this.dirty = true;
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
        if (this.series == null) {
            this.series = this.dataLayer.getSeries(this);
        }
        return series;
    }

    @Override
    public void setSeries(List<Series> series) {
        this.series = series;
        this.dirty = true;
    }

    @Override
    // here?
    public List<Series> getSeries(String role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSeries(Series series) {
        if (this.series == null) {
            this.getSeries();
        }
        this.series.add(series);
    }

    @Override
    public void removeSeries(Series series) {
        if (this.series == null) {
            return;
        }
        this.series.remove(series);
    }

    @Override
    public void removeAllSeries() {
        this.series = null;
    }

    @Override
    public void copyFrom(Cast castMember) {
        this.ID = castMember.getID();
        this.birthDate = castMember.getBirthDate();
        this.country = castMember.getCountry();
        this.gender = castMember.getGender();
        this.imageURL = castMember.getImageURL();
        this.name = castMember.getName();
        this.surname = castMember.getSurname();

        this.series = null;

        this.dirty = true;
    }

}
