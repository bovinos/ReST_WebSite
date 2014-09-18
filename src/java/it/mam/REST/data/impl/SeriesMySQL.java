package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
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
public class SeriesMySQL implements Series {

    private int ID;
    private String name;
    private int year;
    private String description;
    private String imageURL;
    private String state;
    private int addCount;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    private List<User> users;
    private List<UserSeries> userSeries;
    private List<Genre> genres;
    private List<Episode> episodes;
    private List<CastMember> castMembers;
    private List<CastMemberSeries> castMemberSeries;
    private List<News> news;
    private List<Comment> comments;
    private List<Message> messages;

    public SeriesMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        name = "";
        year = 0;
        description = "";
        imageURL = "";
        state = "";
        addCount = 0;
        dirty = false;

        this.dataLayer = dataLayer;

        users = null;
        userSeries = null;
        genres = null;
        episodes = null;
        castMembers = null;
        castMemberSeries = null;
        news = null;
        comments = null;
        messages = null;
    }

    public SeriesMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        name = rs.getString("name");
        year = rs.getInt("year");
        description = rs.getString("description");
        imageURL = rs.getString("image_URL");
        state = rs.getString("state");
        addCount = rs.getInt("add_count");
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
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
        dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state.toUpperCase();
        dirty = true;
    }

    @Override
    public int getAddCount() {
        return addCount;
    }

    @Override
    public void setAddCount(int addCount) {
        this.addCount = addCount;
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
    public List<User> getUsers() {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        return users;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
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
    public List<Episode> getEpisodes() {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
        }
        return episodes;
    }

    @Override
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        dirty = true;
    }

    @Override
    public List<CastMember> getCastMembers() {
        if (castMembers == null) {
            castMembers = dataLayer.getCastMembers(this);
        }
        return castMembers;
    }

    @Override
    public void setCastMembers(List<CastMember> castMembers) {
        this.castMembers = castMembers;
        dirty = true;
    }

    @Override
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
    public void addUser(User user) {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        users.add(user);
        dirty = true;
    }

    @Override
    public void removeUser(User user) {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        users.remove(user);
        dirty = true;
    }

    @Override
    public void removeAllUsers() {
        users.clear();
        dirty = true;
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
        genres.clear();
        dirty = true;
    }

    @Override
    public void addEpisode(Episode episode) {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
        }
        episodes.add(episode);
        dirty = true;
    }

    @Override
    public void removeEpisode(Episode episode) {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
        }
        episodes.remove(episode);
        dirty = true;
    }

    @Override
    public void removeAllEpisodes() {
        episodes.clear();
        dirty = true;
    }

    @Override
    public void addCastMember(CastMember castMember) {
        if (castMembers == null) {
            castMembers = dataLayer.getCastMembers(this);
        }
        castMembers.add(castMember);
        dirty = true;
    }

    @Override
    public void removeCastMember(CastMember castMember) {
        if (castMembers == null) {
            castMembers = dataLayer.getCastMembers(this);
        }
        castMembers.remove(castMember);
        dirty = true;
    }

    @Override
    public void removeAllCastMembers() {
        castMembers.clear();
        dirty = true;
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
    public void removeAllNews() {
        news.clear();
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
    public void removeAllComment() {
        comments.clear();
        dirty = true;
    }

    @Override
    public void addMessage(Message message) {
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
    public void removeAllMessages() {
        messages.clear();
        dirty = true;
    }

    @Override
    public void copyFrom(Series series) {
        ID = series.getID();
        addCount = series.getAddCount();
        description = series.getDescription();
        imageURL = series.getImageURL();
        name = series.getName();
        state = series.getState();
        year = series.getYear();

        castMembers = null;
        castMemberSeries = null;
        comments = null;
        episodes = null;
        genres = null;
        messages = null;
        news = null;
        users = null;
        userSeries = null;

        dirty = true;
    }

    @Override
    public void increaseAddCount() {
        addCount++;
        dirty = true;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "AddCount: " + addCount + "\n"
                + "Description: " + description + "\n"
                + "ImageURL: " + imageURL + "\n"
                + "Name: " + name + "\n"
                + "State: " + state + "\n"
                + "Year: " + year + "\n"
                + "Dirty: " + dirty + "\n"
                + "CastMember: " + castMembers + "\n"
                + "CastMemberSeries: " + castMemberSeries + "\n"
                + "Comments: " + comments + "\n"
                + "Episodes: " + episodes + "\n"
                + "Genres: " + genres + "\n"
                + "Messages " + messages + "\n"
                + "News: " + news + "\n"
                + "Users: " + users + "\n"
                + "UserSeries: " + userSeries;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // se hanno lo stesso riferimento restituisco true
            return true;
        }
        if (obj == null || !(obj instanceof Series)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo Series quindi posso fare il cast
        Series s = (Series) obj;
        return ID == s.getID();
    }

}
