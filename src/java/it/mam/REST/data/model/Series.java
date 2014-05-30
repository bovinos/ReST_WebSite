package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Series {

    int getID();

    String getName();

    void setName(String name);

    int getYear();

    void setYear(int year);

    String getDescription();

    void setDescription(String description);

    String getImageURL();

    void setImageURL(String imageURL);

    String getState();

    void setState(String state);

    int getAddCount();

    void setAddCount(int addCount);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Series series);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<User> getUsers();

    void setUsers(List<User> users);

    void addUser(User user);

    void removeUser(User user);

    void removeAllUsers();

    List<Genre> getGenres();

    void setGenres(List<Genre> genres);

    void addGenre(Genre genre);

    void removeGenre(Genre genre);

    void removeAllGenre();

    List<Episode> getEpisodes();

    void setEpisodes(List<Episode> episodes);

    void addEpisode(Episode episode);

    void removeEpisode(Episode episode);

    void removeAllEpisodes();

    List<Cast> getCast();

    void setCast(List<Cast> cast);

    void addCastMember(Cast castMember);

    void removeCastMember(Cast castMember);

    void removeCast();

    List<Channel> getChannels();

    void setChannels(List<Channel> channels);

    void addChannel(Channel channel);

    void removeChannel(Channel channel);

    void removeAllChannels();

    List<News> getNews();

    void setNews(List<News> news);

    void addNews(News news);

    void removeNews(News news);

    void removeAllNews();

    List<Comment> getComments();

    void setComments(List<Comment> comments);

    void addComment(Comment comment);

    void removeComment(Comment comment);

    void removeAllComment();

    List<Message> getMessages();

    void setMessages(List<Message> messages);

    void addMessage(Series series, Message message);

    void removeMessage(Message message);

    void removeAllMessages();

    //====================================
    //                     OTHER                      //
    //====================================
    void increaseAddCount();

}
