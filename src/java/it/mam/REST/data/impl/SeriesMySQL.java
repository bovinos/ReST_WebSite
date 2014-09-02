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
    List<CastMember> castMembers;
    List<Channel> channels;
    List<News> news;
    List<Comment> comments;
    List<Message> messages;

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
        genres = null;
        episodes = null;
        castMembers = null;
        channels = null;
        news = null;
        comments = null;
        messages = null;
    }

    public SeriesMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

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
    public void setState(String state) { // set the state only if the parameter is "ONGOING" or "COMPLETE" otherwise do nothing
        if (state.equalsIgnoreCase("ONGOING") || state.equalsIgnoreCase("COMPLETE")) {
            this.state = state;
            dirty = true;
        }
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
    public List<Channel> getChannels() {
        if (channels == null) {
            channels = dataLayer.getChannels(this);
        }
        return channels;
    }

    @Override
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
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
            getUsers();
        }
        users.add(user);
    }

    @Override
    public void removeUser(User user) {
        if (users == null) {
            return;
        }
        users.remove(user);
    }

    @Override
    public void removeAllUsers() {
        users = null;
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
    public void addEpisode(Episode episode) {
        if (episodes == null) {
            getEpisodes();
        }
        episodes.add(episode);
    }

    @Override
    public void removeEpisode(Episode episode) {
        if (episodes == null) {
            return;
        }
        episodes.remove(episode);
    }

    @Override
    public void removeAllEpisodes() {
        episodes = null;
    }

    @Override
    public void addCastMember(CastMember castMember) {
        if (castMembers == null) {
            getCastMembers();
        }
        castMembers.add(castMember);
    }

    @Override
    public void removeCastMember(CastMember castMember) {
        if (castMembers == null) {
            return;
        }
        castMembers.remove(castMember);
    }

    @Override
    public void removeCastMembers() {
        castMembers = null;
    }

    @Override
    public void addChannel(Channel channel) {
        if (channels == null) {
            getChannels();
        }
        channels.add(channel);
    }

    @Override
    public void removeChannel(Channel channel) {
        if (channels == null) {
            return;
        }
        channels.remove(channel);
    }

    @Override
    public void removeAllChannels() {
        channels = null;
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
    public void removeAllNews() {
        news = null;
    }

    @Override
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = getComments();
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
    public void removeAllComment() {
        comments = null;
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
    public void removeAllMessages() {
        messages = null;
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
        channels = null;
        comments = null;
        episodes = null;
        genres = null;
        messages = null;
        news = null;
        users = null;

        dirty = true;
    }

    @Override
    public void increaseAddCount() {
        addCount++;
    }

}
