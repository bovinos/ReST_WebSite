package it.mam.REST.data.impl;

import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alex
 */
public class UserMySQL implements User {

    private int ID;
    private String username;
    private String password;
    private String mail;
    private String name;
    private String surname;
    private int age;
    private String gender;
    private String imageURL;
    private String personalMessage;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private Group group;
    private int groupID;

    private List<Comment> comments;
    private List<Series> series;
    private List<Genre> genres;
    private List<Message> messages;
    private List<News> news; // is not null only if the user is an admin

    public UserMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.username = "";
        this.password = "";
        this.mail = "";
        this.name = "";
        this.surname = "";
        this.age = 0;
        this.gender = "u";
        this.imageURL = "";
        this.personalMessage = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.group = null;
        this.groupID = 0;

        this.comments = null;
        this.series = null;
        this.genres = null;
        this.messages = null;
        this.news = null;
    }

    public UserMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this.ID = rs.getInt("ID");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.mail = rs.getString("mail");
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
        this.age = rs.getInt("age");
        this.gender = rs.getString("gender");
        this.imageURL = rs.getString("image_URL");
        this.personalMessage = rs.getString("personal_message");

        this.groupID = rs.getInt("ID_group");
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        this.dirty = true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    // reminder: encrypt or decript the password
    public void setPassword(String password) {
        this.password = password;
        this.dirty = true;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    // reminder: check the mail with RegExp
    public void setMail(String mail) {
        this.mail = mail;
        this.dirty = true;
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
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
        this.dirty = true;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
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
    public String getPersonalMessage() {
        return personalMessage;
    }

    @Override
    public void setPersonalMessage(String personalMessage) {
        this.personalMessage = personalMessage;
        this.dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        this.dirty = true;
    }

    @Override
    public Group getGroup() {
        if (this.group == null && this.groupID > 0) {
            this.group = this.dataLayer.getGroup(groupID);
        }

        return this.group;
    }

    @Override
    public List<Comment> getComments() {
        if (this.comments == null) {
            this.comments = this.dataLayer.getComments(this);
        }
        return comments;
    }

    @Override
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.dirty = true;
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
    public List<Genre> getGenres() {
        if (this.genres == null) {
            this.genres = this.dataLayer.getGenres(this);
        }
        return genres;
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        this.dirty = true;
    }

    @Override
    public List<Message> getMessages() {
        if (this.messages == null) {
            this.messages = this.dataLayer.getMessages(this);
        }
        return messages;
    }

    @Override
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        this.dirty = true;
    }

    @Override
    // reminder: menage the admin & news question
    public List<News> getNews() {
        if (this.news == null) {
            this.news = this.dataLayer.getNews(this);
        }
        return news;
    }

    @Override
    public void setNews(List<News> news) {
        this.news = news;
        this.dirty = true;
    }

    @Override
    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.getComments();
        }
        this.comments.add(comment);
    }

    @Override
    public void removeComment(Comment comment) {
        if (this.comments == null) {
            return;
        }
        this.comments.remove(comment);
    }

    @Override
    public Comment editComment(Comment comment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllComment() {
        this.comments = null;
    }

    @Override
    // it's ok insert here this method?
    public List<Series> getSeries(int rating) {
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
    public void addGenre(Genre genre) {
        if (this.genres == null) {
            this.getGenres();
        }
        genres.add(genre);
    }

    @Override
    public void removeGenre(Genre genre) {
        if (this.genres == null) {
            return;
        }
        this.genres.remove(genre);
    }

    @Override
    public void removeAllGenre() {
        this.genres = null;
    }

    @Override
    public void addMessage(Series series, Message message) {
        if (this.messages == null) {
            this.getMessages();
        }
        this.messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        if (this.messages == null) {
            return;
        }
        this.messages.remove(message);
    }

    @Override
    // it's ok insert here this method?
    public Message editMessage(Series series, Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllMessages() {
        this.messages = null;
    }

    @Override
    public void addNews(News news) {
        if (this.news == null) {
            this.getNews();
        }
        this.news.add(news);
    }

    @Override
    public void removeNews(News news) {
        if (this.news == null) {
            return;
        }
        this.news.remove(news);
    }

    @Override
    // // it's ok insert here this method?
    public News editNews(News news) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllNews() {
        this.news = null;
    }

    @Override
    public void copyFrom(User user) {
        this.ID = user.getID();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.imageURL = user.getImageURL();
        this.mail = user.getMail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.personalMessage = user.getPersonalMessage();
        this.surname = user.getSurname();
        this.username = user.getUsername();

        if (user.getGroup() != null) {
            this.groupID = user.getGroup().getID();
        }

        this.dirty = true;

        this.comments = null;
        this.genres = null;
        this.messages = null;
        this.news = null;
        this.series = null;
    }

}
