package it.mam.REST.data.impl;

import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
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
    private boolean notificationStatus;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private Group group;
    private int groupID;

    private List<Comment> comments;
    private List<Series> series;
    private List<UserSeries> userSeries;
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
        gender = "";
        imageURL = "";
        notificationStatus = true;
        dirty = false;

        this.dataLayer = dataLayer;

        group = null;
        groupID = 0;

        comments = null;
        series = null;
        userSeries = null;
        genres = null;
        messages = null;
        news = null;
    }

    public UserMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        username = rs.getString("username");
        password = rs.getString("password");
        mail = rs.getString("mail");
        name = rs.getString("name");
        surname = rs.getString("surname");
        age = rs.getInt("age");
        gender = rs.getString("gender");
        imageURL = rs.getString("image_URL");
        notificationStatus = rs.getBoolean("notification_status");

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
        this.gender = gender.toUpperCase();
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
    public boolean getNotificationStatus() {
        return notificationStatus;
    }

    @Override
    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
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
    public Group getGroup() {
        if (group == null && groupID > 0) {
            group = dataLayer.getGroup(groupID);
        }

        return group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
        groupID = group.getID();
        dirty = true;
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
            comments = dataLayer.getComments(this);
        }
        comments.add(comment);
        dirty = true;
    }

    @Override
    public void removeComment(Comment comment) {
        if (comments == null) {
            comments = dataLayer.getComments(this);
        }
        comments.remove(comment);
        dirty = true;
    }

    @Override
    public Comment editComment(Comment comment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllComment() {
        comments = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    // it's ok insert here this method?
    public List<Series> getSeries(int rating) {
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
        series = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    public List<UserSeries> getUserSeries() {
        if (userSeries == null) {
            userSeries = dataLayer.getUserSeries(this);
        }
        return userSeries;
    }

    @Override
    public void setUserSeries(List<UserSeries> userSeries) {
        this.userSeries = userSeries;
    }

    @Override
    public void addGenre(Genre genre) {
        if (genres == null) {
            genres = dataLayer.getGenres(this);
        }
        genres.add(genre);
        dirty = true;
    }

    @Override
    public void removeGenre(Genre genre) {
        if (genres == null) {
            genres = dataLayer.getGenres(this);
        }
        genres.remove(genre);
        dirty = true;
    }

    @Override
    public void removeAllGenre() {
        genres = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    public void addMessage(Series series, Message message) {
        if (messages == null) {
            messages = dataLayer.getMessages(this);
        }
        messages.add(message);
        dirty = true;
    }

    @Override
    public void removeMessage(Message message) {
        if (messages == null) {
            messages = dataLayer.getMessages(this);
        }
        messages.remove(message);
        dirty = true;
    }

    @Override
    // it's ok insert here this method?
    public Message editMessage(Series series, Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllMessages() {
        messages = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    public void addNews(News news) {
        if (this.news == null) {
            this.news = dataLayer.getNews(this);
        }
        this.news.add(news);
        dirty = true;
    }

    @Override
    public void removeNews(News news) {
        if (this.news == null) {
            this.news = dataLayer.getNews(this);
        }
        this.news.remove(news);
        dirty = true;
    }

    @Override
    // // it's ok insert here this method?
    public News editNews(News news) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllNews() {
        news = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    public void copyFrom(User user) {
        ID = user.getID();
        age = user.getAge();
        gender = user.getGender();
        imageURL = user.getImageURL();
        notificationStatus = user.getNotificationStatus();
        mail = user.getMail();
        name = user.getName();
        password = user.getPassword();
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
        userSeries = null;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "Age: " + age + "\n"
                + "Gender: " + gender + "\n"
                + "ImageURL: " + imageURL + "\n"
                + "NotificationStatus: " + notificationStatus + "\n"
                + "Mail: " + mail + "\n"
                + "Name: " + name + "\n"
                + "Password: " + password + "\n"
                + "Surname: " + surname + "\n"
                + "Username: " + username + "\n"
                + "Dirty: " + dirty + "\n"
                + "GroupID: " + groupID + "\n"
                + "Group: " + group + "\n"
                + "Comments: " + comments + "\n"
                + "Genres: " + genres + "\n"
                + "Messages: " + messages + "\n"
                + "News: " + news + "\n"
                + "Series: " + series + "\n"
                + "UserSeries: " + userSeries;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // se hanno lo stesso riferimento restituisco true
            return true;
        }
        if (obj == null || !(obj instanceof User)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo User quindi posso fare il cast
        User u = (User) obj;
        return ID == u.getID();
    }

}
