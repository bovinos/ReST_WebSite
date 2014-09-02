package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
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
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        users.add(user);
    }

    @Override
    public void removeUser(User user) {
        if (users == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        users.remove(user);
    }

    @Override
    public void removeAllUsers() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        users = null;
    }

    @Override
    public void addGenre(Genre genre) {
        if (genres == null) {
            genres = dataLayer.getGenres(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        genres.add(genre);
    }

    @Override
    public void removeGenre(Genre genre) {
        if (genres == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        genres.remove(genre);
    }

    @Override
    public void removeAllGenre() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        genres = null;
    }

    @Override
    public void addEpisode(Episode episode) {
        if (episodes == null) {
            episodes = dataLayer.getEpisodes(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        episodes.add(episode);
    }

    @Override
    public void removeEpisode(Episode episode) {
        if (episodes == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        episodes.remove(episode);
    }

    @Override
    public void removeAllEpisodes() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        episodes = null;
    }

    @Override
    public void addCastMember(CastMember castMember) {
        if (castMembers == null) {
            castMembers = dataLayer.getCastMembers(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        castMembers.add(castMember);
    }

    @Override
    public void removeCastMember(CastMember castMember) {
        if (castMembers == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        castMembers.remove(castMember);
    }

    @Override
    public void removeAllCastMembers() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        castMembers = null;
    }

    @Override
    public void addNews(News news) {
        if (this.news == null) {
            this.news = dataLayer.getNews(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        this.news.add(news);
    }

    @Override
    public void removeNews(News news) {
        if (this.news == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        this.news.remove(news);
    }

    @Override
    public void removeAllNews() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        news = null;
    }

    @Override
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = dataLayer.getComments(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        comments.add(comment);
    }

    @Override
    public void removeComment(Comment comment) {
        if (comments == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        comments.remove(comment);
    }

    @Override
    public void removeAllComment() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
        comments = null;
    }

    @Override
    public void addMessage(Message message) {
        if (messages == null) {
            messages = dataLayer.getMessages(this);
            /**
             * <ma se dopo questa chiamata series è ancora null perché il membro
             * del cast non ha partecipato a serie?>
             */
        }
        messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        if (messages == null) {
            return;
            /**
             * <oppure dobbiamo prima caricarlo dal DB e poi vedere se è null?>
             */
        }
        messages.remove(message);
    }

    @Override
    public void removeAllMessages() {
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
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
