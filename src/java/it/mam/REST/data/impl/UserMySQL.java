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

        ID = 0;
        username = "";
        password = "";
        mail = "";
        name = "";
        surname = "";
        age = 0;
        gender = "u";
        imageURL = "";
        personalMessage = "";
        dirty = false;

        this.dataLayer = dataLayer;

        group = null;
        groupID = 0;

        comments = null;
        series = null;
        genres = null;
        messages = null;
        news = null;
    }

    public UserMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        ID = rs.getInt("ID");
        username = rs.getString("username");
        password = rs.getString("password");
        mail = rs.getString("mail");
        name = rs.getString("name");
        surname = rs.getString("surname");
        age = rs.getInt("age");
        gender = rs.getString("gender");
        imageURL = rs.getString("image_URL");
        personalMessage = rs.getString("personal_message");

        groupID = rs.getInt("ID_group");
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
        dirty = true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    // reminder: encrypt or decript the password
    public void setPassword(String password) {
        this.password = password;
        dirty = true;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    // reminder: check the mail with RegExp
    public void setMail(String mail) {
        this.mail = mail;
        dirty = true;
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
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
        dirty = true;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
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
    public String getPersonalMessage() {
        return personalMessage;
    }

    @Override
    public void setPersonalMessage(String personalMessage) {
        this.personalMessage = personalMessage;
        dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        dirty = true;
    }

    @Override
    public Group getGroup() {
        if (group == null && groupID > 0) {
            group = dataLayer.getGroup(groupID);
        }

        return group;
    }

    @Override
    public List<Comment> getComments() {
        if (comments == null) {
            comments = dataLayer.getComments(this);
        }
        return comments;
    }

    @Override
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        dirty = true;
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
    public List<Genre> getGenres() {
        if (genres == null) {
            genres = dataLayer.getGenres(this);
        }
        return genres;
    }

    @Override
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        dirty = true;
    }

    @Override
    public List<Message> getMessages() {
        if (messages == null) {
            messages = dataLayer.getMessages(this);
        }
        return messages;
    }

    @Override
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        dirty = true;
    }

    @Override
    // reminder: menage the admin & news question
    public List<News> getNews() {
        if (news == null) {
            news = dataLayer.getNews(this);
        }
        return news;
    }

    @Override
    public void setNews(List<News> news) {
        this.news = news;
        dirty = true;
    }

    @Override
    public void addComment(Comment comment) {
        if (comments == null) {
            getComments();
        }
        comments.add(comment);
    }

    @Override
    public void removeComment(Comment comment) {
        if (comments == null) {
            return;
        }
        comments.remove(comment);
    }

    @Override
    public Comment editComment(Comment comment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllComment() {
        comments = null;
    }

    @Override
    // it's ok insert here this method?
    public List<Series> getSeries(int rating) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSeries(Series series) {
        if (this.series == null) {
            getSeries();
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
        series = null;
    }

    @Override
    public void addGenre(Genre genre) {
        if (genres == null) {
            getGenres();
        }
        genres.add(genre);
    }

    @Override
    public void removeGenre(Genre genre) {
        if (genres == null) {
            return;
        }
        genres.remove(genre);
    }

    @Override
    public void removeAllGenre() {
        genres = null;
    }

    @Override
    public void addMessage(Series series, Message message) {
        if (messages == null) {
            getMessages();
        }
        messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        if (messages == null) {
            return;
        }
        messages.remove(message);
    }

    @Override
    // it's ok insert here this method?
    public Message editMessage(Series series, Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllMessages() {
        messages = null;
    }

    @Override
    public void addNews(News news) {
        if (this.news == null) {
            getNews();
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
        news = null;
    }

    @Override
    public void copyFrom(User user) {
        ID = user.getID();
        age = user.getAge();
        gender = user.getGender();
        imageURL = user.getImageURL();
        mail = user.getMail();
        name = user.getName();
        password = user.getPassword();
        personalMessage = user.getPersonalMessage();
        surname = user.getSurname();
        username = user.getUsername();

        if (user.getGroup() != null) {
            groupID = user.getGroup().getID();
        }

        dirty = true;

        comments = null;
        genres = null;
        messages = null;
        news = null;
        series = null;
    }

}
