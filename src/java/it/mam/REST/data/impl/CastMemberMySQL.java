package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
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

    private List<Series> series;
    private List<CastMemberSeries> castMemberSeries;

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
        castMemberSeries = null;
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
    public List<Series> getSeries(String role) {
        if (series == null) {
            series = dataLayer.getSeries(this);
        }
        return series;
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
        series.clear();
        dirty = true;
    }

    @Override
    public boolean isSeriesSet() {
        return series == null;
    }

    @Override
    public List<CastMemberSeries> getCastMemberSeries() {
        if (castMemberSeries == null) {
            castMemberSeries = dataLayer.getCastMemberSeries(this);
        }
        return castMemberSeries;
    }

    @Override
    public void setCastMemberSeries(List<CastMemberSeries> castMemberSeries) {
        this.castMemberSeries = castMemberSeries;
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
        castMemberSeries = null;

        dirty = true;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "BirthDate: " + birthDate + "\n"
                + "Country: " + country + "\n"
                + "Gender: " + gender + "\n"
                + "Gender: " + imageURL + "\n"
                + "name: " + name + "\n"
                + "Surname: " + surname + "\n"
                + "Dirty: " + dirty + "\n"
                + "Series: " + series + "\n"
                + "CastMemberSeries: " + castMemberSeries;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // se hanno lo stesso riferimento restituisco true
            return true;
        }
        if (obj == null || !(obj instanceof CastMember)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo CastMember quindi posso fare il cast
        CastMember cm = (CastMember) obj;
        return ID == cm.getID();
    }

}
