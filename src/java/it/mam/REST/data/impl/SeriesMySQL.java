package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
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

    List<User> users;
    List<Genre> genres;
    List<Episode> episodes;
    List<CastMember> cast;
    List<Channel> channels;
    List<News> news;
    List<Comment> comments;
    List<Message> messages;

    public SeriesMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.name = "";
        this.year = 0;
        this.description = "";
        this.imageURL = "";
        this.state = "";
        this.addCount = 0;
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.users = null;
        this.genres = null;
        this.episodes = null;
        this.cast = null;
        this.channels = null;
        this.news = null;
        this.comments = null;
        this.messages = null;
    }

    public SeriesMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this.ID = rs.getInt("ID");
        this.name = rs.getString("name");
        this.year = rs.getInt("year");
        this.description = rs.getString("description");
        this.imageURL = rs.getString("image_URL");
        this.state = rs.getString("state");
        this.addCount = rs.getInt("add_count");
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
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
        this.dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) { // set the state only if the parameter is "ONGOING" or "COMPLETE" otherwise do nothing
        if (state.equalsIgnoreCase("ONGOING") || state.equalsIgnoreCase("COMPLETE")) {
            this.state = state;
            this.dirty = true;
        }
    }

    @Override
    public int getAddCount() {
        return addCount;
    }

    @Override
    public void setAddCount(int addCount) {
        this.addCount = addCount;
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
    public List<User> getUsers() {
        if (this.users == null) {
            this.users = this.dataLayer.getUsers(this);
        }
        return users;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
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
    public List<Episode> getEpisodes() {
        if (this.episodes == null) {
            this.episodes = this.dataLayer.getEpisodes(this);
        }
        return episodes;
    }

    @Override
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        this.dirty = true;
    }

    @Override
    public List<CastMember> getCastMembers() {
        if (this.cast == null) {
            this.cast = this.dataLayer.getCastMembers(this);
        }
        return cast;
    }

    @Override
    public void setCastMembers(List<CastMember> cast) {
        this.cast = cast;
        this.dirty = true;
    }

    @Override
    public List<Channel> getChannels() {
        if (this.channels == null) {
            this.channels = this.dataLayer.getChannels(this);
        }
        return channels;
    }

    @Override
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        this.dirty = true;
    }

    @Override
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
    public void addUser(User user) {
        if (this.users == null) {
            this.getUsers();
        }
        this.users.add(user);
    }

    @Override
    public void removeUser(User user) {
        if (this.users == null) {
            return;
        }
        this.users.remove(user);
    }

    @Override
    public void removeAllUsers() {
        this.users = null;
    }

    @Override
    public void addGenre(Genre genre) {
        if (this.genres == null) {
            this.getGenres();
        }
        this.genres.add(genre);
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
    public void addEpisode(Episode episode) {
        if (this.episodes == null) {
            this.getEpisodes();
        }
        this.episodes.add(episode);
    }

    @Override
    public void removeEpisode(Episode episode) {
        if (this.episodes == null) {
            return;
        }
        this.episodes.remove(episode);
    }

    @Override
    public void removeAllEpisodes() {
        this.episodes = null;
    }

    @Override
    public void addCastMember(CastMember castMember) {
        if (this.cast == null) {
            this.getCastMembers();
        }
        this.cast.add(castMember);
    }

    @Override
    public void removeCastMember(CastMember castMember) {
        if (this.cast == null) {
            return;
        }
        this.cast.remove(castMember);
    }

    @Override
    public void removeCastMembers() {
        this.cast = null;
    }

    @Override
    public void addChannel(Channel channel) {
        if (this.channels == null) {
            this.getChannels();
        }
        this.channels.add(channel);
    }

    @Override
    public void removeChannel(Channel channel) {
        if (this.channels == null) {
            return;
        }
        this.channels.remove(channel);
    }

    @Override
    public void removeAllChannels() {
        this.channels = null;
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
    public void removeAllNews() {
        this.news = null;
    }

    @Override
    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = this.getComments();
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
    public void removeAllComment() {
        this.comments = null;
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
    public void removeAllMessages() {
        this.messages = null;
    }

    @Override
    public void copyFrom(Series series) {
        this.ID = series.getID();
        this.addCount = series.getAddCount();
        this.description = series.getDescription();
        this.imageURL = series.getImageURL();
        this.name = series.getName();
        this.state = series.getState();
        this.year = series.getYear();

        this.cast = null;
        this.channels = null;
        this.comments = null;
        this.episodes = null;
        this.genres = null;
        this.messages = null;
        this.news = null;
        this.users = null;

        this.dirty = true;
    }

    @Override
    public void increaseAddCount() {
        this.addCount++;
    }

}
