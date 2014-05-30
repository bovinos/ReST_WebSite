package it.mam.REST.data.model;

import it.univaq.f4i.iw.framework.data.DataLayer;
import java.util.List;

/**
 *
 * @author alex
 */
public interface RESTDataLayer extends DataLayer {

    Cast createCastMember();

    Cast getCastMember(int castID);

    List<Cast> getCast();

    List<Cast> getCast(Series series);

    List<Cast> getCast(Series series, String role);

    void storeCastMember(Cast castMember);

    void removeCastMember(Cast castMember);

    Channel createChannel();

    Channel getChannel(int channelID);

    List<Channel> getChannels();

    List<Channel> getChannels(Series series);

    List<Channel> getChannels(String type);

    void storeChannel(Channel channel);

    void removeChannel(Channel channel);

    Comment createComment();

    Comment getComment(int commentID);

    List<Comment> getComments();

    List<Comment> getComments(Series series);

    List<Comment> getComments(News news);

    List<Comment> getComments(User user);

    void storeComment(Comment comment);

    void removeComment(Comment comment);

    Episode createEpisode();

    Episode getEpisode(int episodeID);

    Episode getEpisodeBySeriesAndSeasonAndNumber(Series series, int season, int number);

    List<Episode> getEpisodes();

    List<Episode> getEpisodes(Series series);

    List<Episode> getEpisodes(Series series, int season);

    void storeEpisode(Episode episode);

    void removeEpisode(Episode episode);

    //**************************************************
    Episode getLastEpisodeSeen(User user, Series series);

    Genre createGenre();

    Genre getGenre(int genreID);

    List<Genre> getGenres();

    List<Genre> getGenres(Series series);

    List<Genre> getGenres(User user);

    void storeGenre(Genre genre);

    void removeGenre(Genre genre);

    Group createGroup();

    Group getGroup(int groupID);

    Group getGroup(User user);

    List<Group> getGroups();

    List<Group> getGroups(Service service);

    void storeGroup(Group group);

    void removeGroup(Group group);

    Message createMessage();

    Message getMessage(int messageID);

    List<Message> getMessages();

    List<Message> getMessages(User user);

    List<Message> getMessages(Series series);

    List<Message> getMessages(Series series, User user);

    void storeMessage(Message message);

    void removeMessage(Message message);

    News createNews();

    News getNews(int newsID);

    News getNews(Comment comment);

    List<News> getNews();

    List<News> getNews(User user);

    List<News> getNews(Series series);

    void storeNews(News news);

    void removeNews(News news);

    Series createSeries();

    Series getSeries(int seriesID);

    Series getSeries(Message message);

    Series getSeries(Episode episode);

    Series getSeries(Comment comment);

    List<Series> getSeries();

    List<Series> getSeries(News news);

    List<Series> getSeries(Genre genre);

    List<Series> getSeries(Channel channel);

    List<Series> getSeries(Cast castMember);

    List<Series> getSeries(Cast castMember, String role);

    List<Series> getSeries(User user);

    void storeSeries(Series series);

    void removeSeries(Series series);

    Service createService();

    Service getService(int serviceID);

    List<Service> getServices();

    List<Service> getServices(Group group);

    void storeService(Service service);

    void removeService(Service service);

    User createUser();

    User getUser(int userID);

    User getUser(Message message);

    User getUser(Comment comment);

    User getUser(News news);

    List<User> getUsers();

    List<User> getUsers(Series series);

    List<User> getUsers(Genre genre);

    List<User> getUsers(Group group);

    void storeUser(User user);

    void removeUser(User user);

}
