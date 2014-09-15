package it.mam.REST.data.model;

import it.univaq.f4i.iw.framework.data.DataLayer;
import java.util.List;

/**
 *
 * @author alex
 */
public interface RESTDataLayer extends DataLayer {

    // =============================================================
    // CAST MEMBER
    // =============================================================
    CastMember createCastMember();

    CastMember getCastMember(int castMemberID);

    List<CastMember> getCastMembers();

    List<CastMember> getCastMembers(Series series);

    List<CastMember> getCastMembers(Series series, String role);

    void storeCastMember(CastMember castMember);

    void removeCastMember(CastMember castMember);

    // =============================================================
    // CHANNEL
    // =============================================================
    Channel createChannel();

    Channel getChannel(int channelID);

    List<Channel> getChannels();

    List<Channel> getChannels(Episode episode);

    List<Channel> getChannels(String type);

    void storeChannel(Channel channel);

    void removeChannel(Channel channel);

    // =============================================================
    // COMMENT
    // =============================================================
    Comment createComment();

    Comment getComment(int commentID);

    List<Comment> getComments();

    List<Comment> getComments(Series series);

    List<Comment> getComments(News news);

    List<Comment> getComments(User user);

    void storeComment(Comment comment);

    void removeComment(Comment comment);

    // =============================================================
    // EPISODE
    // =============================================================
    Episode createEpisode();

    Episode getEpisode(int episodeID);

    Episode getEpisodeBySeriesAndSeasonAndNumber(Series series, int season, int number);

    List<Episode> getEpisodes();

    List<Episode> getEpisodes(Series series);

    List<Episode> getEpisodes(Series series, int season);

    List<Episode> getEpisodes(Channel channel);

    void storeEpisode(Episode episode);

    void removeEpisode(Episode episode);

    // =============================================================
    // GENRE
    // =============================================================
    Genre createGenre();

    Genre getGenre(int genreID);

    List<Genre> getGenres();

    List<Genre> getGenres(Series series);

    List<Genre> getGenres(User user);

    void storeGenre(Genre genre);

    void removeGenre(Genre genre);

    // =============================================================
    // GROUP
    // =============================================================
    Group createGroup();

    Group getGroup(int groupID);

    Group getGroup(User user);

    List<Group> getGroups();

    List<Group> getGroups(Service service);

    void storeGroup(Group group);

    void removeGroup(Group group);

    // =============================================================
    // MESSAGE
    // =============================================================
    Message createMessage();

    Message getMessage(int messageID);

    List<Message> getMessages();

    List<Message> getMessages(User user);

    List<Message> getMessages(Series series);

    List<Message> getMessages(Series series, User user);

    void storeMessage(Message message);

    void removeMessage(Message message);

    // =============================================================
    // NEWS
    // =============================================================
    News createNews();

    News getNews(int newsID);

    News getNews(Comment comment);

    List<News> getNews();

    List<News> getNews(User user);

    List<News> getNews(Series series);

    void storeNews(News news);

    void removeNews(News news);

    // =============================================================
    // SERIES
    // =============================================================
    Series createSeries();

    Series getSeries(int seriesID);

    Series getSeries(Message message);

    Series getSeries(Episode episode);

    Series getSeries(Comment comment);

    List<Series> getSeries();

    List<Series> getSeries(News news);

    List<Series> getSeries(Genre genre);

    List<Series> getSeries(CastMember castMember);

    List<Series> getSeries(CastMember castMember, String role);

    List<Series> getSeries(User user);

    void storeSeries(Series series);

    void removeSeries(Series series);

    // =============================================================
    // SERVICE
    // =============================================================
    Service createService();

    Service getService(int serviceID);

    List<Service> getServices();

    List<Service> getServices(Group group);

    void storeService(Service service);

    void removeService(Service service);

    // =============================================================
    // USER
    // =============================================================
    User createUser();

    User getUser(int userID);

    User getUser(String username, String password);

    User getUser(Message message);

    User getUser(Comment comment);

    User getUser(News news);

    List<User> getUsers();

    List<User> getUsers(Series series);

    List<User> getUsers(Genre genre);

    List<User> getUsers(Group group);

    void storeUser(User user);

    void removeUser(User user);

    // =============================================================
    // CAST MEMBER SERIES
    // =============================================================
    CastMemberSeries createCastMemberSeries();

    CastMemberSeries getCastMemberSeries(int castMemberSeriesID);

    List<CastMemberSeries> getCastMemberSeries();

    List<CastMemberSeries> getCastMemberSeries(CastMember castMember);

    List<CastMemberSeries> getCastMemberSeries(Series series);

    List<CastMemberSeries> getCastMembeSeries(CastMember castMember, Series series);

    void storeCastMemberSeries(CastMemberSeries castMemberSeries);

    void removeCastMemberSeries(CastMemberSeries castMemberSeries);

    // =============================================================
    // CHANNEL EPISODE
    // =============================================================
    ChannelEpisode createChannelEpisode();

    ChannelEpisode getChannelEpisode(int channelEpisodeID);

    List<ChannelEpisode> getChannelEpisode();

    List<ChannelEpisode> getChannelEpisode(Channel channel);

    List<ChannelEpisode> getChannelEpisode(Episode episode);

    List<ChannelEpisode> getChannelEpisode(Channel channel, Episode episode);

    void storeChannelEpisode(ChannelEpisode channelEpisode);

    void removeChannelEpisode(ChannelEpisode channelEpisode);

    // =============================================================
    // USER SERIES
    // =============================================================
    UserSeries createUserSeries();

    UserSeries getUserSeries(int userSeriesID);

    UserSeries getUserSeries(User user, Series series);

    List getUserSeries();

    List<UserSeries> getUserSeries(User user);

    List<UserSeries> getUserSeries(Series series);

    void storeUserSeries(UserSeries userSeries);

    void removeUserSeries(UserSeries userSeries);

    // =============================================================
    // OTHER
    // =============================================================
    Episode getLastEpisodeSeen(User user, Series series);

    int getSeriesGeneralRating(Series series);

}
