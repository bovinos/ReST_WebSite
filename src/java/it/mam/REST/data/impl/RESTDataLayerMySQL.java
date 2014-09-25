package it.mam.REST.data.impl;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.Service;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import it.mam.REST.utility.RESTSortLayer;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.data.DataLayerMysqlImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author alex
 */
public class RESTDataLayerMySQL extends DataLayerMysqlImpl implements RESTDataLayer {

    private PreparedStatement sCastMemberByID, sCastMembers, sCastMembersBySeries, sCastMembersBySeriesAndRole,
            iCastMember,
            uCastMember,
            dCastMember;
    private PreparedStatement sChannelByID, sChannels, sChannelsByEpisode, sChannelsByType,
            iChannel,
            uChannel,
            dChannel;
    private PreparedStatement sCommentByID, sComments, sCommentsBySeries,
            sCommentsByNews, sCommentsByUser,
            iComment,
            uComment,
            dComment;
    private PreparedStatement sEpisodeByID, sEpisodeBySeriesAndSeasonAndNumber,
            sEpisodes, sEpisodesBySeries, sEpisodesBySeriesAndSeason, sEpisodesByChannel,
            iEpisode,
            uEpisode,
            dEpisode;
    private PreparedStatement sGenreByID, sGenres, sGenresBySeries, sGenresByUser,
            iGenre,
            uGenre,
            dGenre;
    private PreparedStatement sGroupByID, sGroupByUser, sGroups, sGroupsByService,
            iGroup,
            uGroup,
            dGroup;
    private PreparedStatement sMessageByID, sMessages, sMessagesByUser, sMessagesBySeries,
            sMessagesByUserAndSeries,
            iMessage,
            uMessage,
            dMessage;
    private PreparedStatement sNewsByID, sNewsByComment, sNews, sNewsByUser, sNewsbySeries,
            iNews,
            uNews,
            dNews;
    private PreparedStatement sSeriesByID, sSeriesByMessage, sSeriesByComment, sSeriesByEpisode,
            sSeries, sSeriesByNews, sSeriesByGenre, sSeriesByCastMember,
            sSeriesByCastMemberAndRole, sSeriesByUser,
            iSeries,
            uSeries,
            dSeries;
    private PreparedStatement sServiceByID, sServices, sServicesByGroup,
            iService,
            uService,
            dService;
    private PreparedStatement sUserByID, sUserByUsernameAndPassword, sUserByComment, sUserByMessage, sUserByNews,
            sUsers, sUsersBySeries, sUsersByGenre, sUsersByGroup,
            iUser,
            uUser,
            dUser;

    // Relationship
    // Relationship with attribute
    private PreparedStatement sCastMemberSeriesByID, sCastMemberSeriesByCastMemberAndSeriesAndRole, sCastMemberSeries, sCastMemberSeriesByCastMember, sCastMemberSeriesBySeries, sCastMemberSeriesByCastMemberAndSeries,
            iCastMemberSeries,
            uCastMemberSeries,
            dCastMemberSeries;
    private PreparedStatement sChannelEpisodeByID, sChannelEpisodeByChannelAndEpisodeAndDate, sChannelEpisode, sChannelEpisodeByChannel, sChannelEpisodeByEpisode, sChannelEpisodeByChannelAndEpisode,
            iChannelEpisode,
            uChannelEpisode,
            dChannelEpisode;
    private PreparedStatement sUserSeriesByID, sUserSeriesByUserAndSeries, sUserSeries, sUserSeriesByUser, sUserSeriesBySeries,
            iUserSeries,
            uUserSeries,
            dUserSeries;

    // Relationship without attribute
    private PreparedStatement iCommentSeries,
            dCommentSeries;
    private PreparedStatement iGenreSeries,
            dGenreSeries;
    private PreparedStatement iNewsComment,
            dNewsComment;
    private PreparedStatement iNewsSeries,
            dNewsSeries;
    private PreparedStatement iServiceGroup,
            dServiceGroup;
    private PreparedStatement iUserGenre,
            dUserGenre;

    // Other
    private PreparedStatement sLastEpisodeSeen;
    private PreparedStatement sSeriesGeneralRating;

    public RESTDataLayerMySQL(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        try {
            super.init();

            // CastMember
            sCastMemberByID = connection.prepareStatement("SELECT * FROM e_cast_member WHERE ID=?");
            sCastMembers = connection.prepareStatement("SELECT ID FROM e_cast_member");
            sCastMembersBySeries = connection.prepareStatement("SELECT ID_cast_member FROM r_cast_member_series WHERE ID_series=?");
            sCastMembersBySeriesAndRole = connection.prepareStatement("SELECT ID_cast_member FROM r_cast_member_series WHERE ID_series=? AND role=?");
            iCastMember = connection.prepareStatement("INSERT INTO e_cast_member (name, surname, birth_date, gender, country, image_URL) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uCastMemberSeries = connection.prepareStatement("UPDATE e_cast_member SET name=?, surname=?, birth_date=?, gender=?, country=?, image_URL=? WHERE ID=?");
            dCastMember = connection.prepareStatement("DELETE FROM e_cast_member WHERE ID=?");

            // Channel
            sChannelByID = connection.prepareStatement("SELECT * FROM e_channel WHERE ID=?");
            sChannels = connection.prepareStatement("SELECT ID FROM e_channel");
            sChannelsByEpisode = connection.prepareStatement("SELECT ID_channel FROM r_channel_episode WHERE ID_episode=?");
            sChannelsByType = connection.prepareStatement("SELECT ID FROM e_channel WHERE type=?");
            iChannel = connection.prepareStatement("INSERT INTO e_channel (name, number, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uChannel = connection.prepareStatement("UPDATE e_channel SET name=?, number=?, type=? WHERE ID=?");
            dChannel = connection.prepareStatement("DELETE FROM e_channel WHERE ID=?");

            // Comment
            sCommentByID = connection.prepareStatement("SELECT * FROM e_comment WHERE ID=?");
            sComments = connection.prepareStatement("SELECT ID FROM e_comment");
            sCommentsBySeries = connection.prepareStatement("SELECT ID_comment FROM r_comment_series WHERE ID_series=?");
            sCommentsByNews = connection.prepareStatement("SELECT ID_comment FROM r_news_comment WHERE ID_news=?");
            sCommentsByUser = connection.prepareStatement("SELECT ID_comment FROM e_comment WHERE ID_user=?");
            iComment = connection.prepareStatement("INSERT INTO e_comment (title, text, date, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uComment = connection.prepareStatement("UPDATE e_comment SET title=?, text=?, date=?, likes=?, dislikes=?, ID_user=? WHERE ID=?");
            dComment = connection.prepareStatement("DELETE FROM e_comment WHERE ID=?");

            // Episode
            sEpisodeByID = connection.prepareStatement("SELECT * FROM e_episode WHERE ID=?");
            sEpisodeBySeriesAndSeasonAndNumber = connection.prepareStatement("SELECT * FROM e_episode WHERE ID_series=? AND season=? AND number=?");
            sEpisodes = connection.prepareStatement("SELECT ID FROM e_episode");
            sEpisodesBySeries = connection.prepareStatement("SELECT ID FROM e_episode WHERE ID_series=?");
            sEpisodesBySeriesAndSeason = connection.prepareStatement("SELECT ID FROM e_episode WHERE ID_series=? AND season=?");
            sEpisodesByChannel = connection.prepareStatement("SELECT ID_episode FROM r_channel_episode WHERE ID_episode=?");
            iEpisode = connection.prepareStatement("INSERT INTO e_episode (number, season, title, description, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uEpisode = connection.prepareStatement("UPDATE e_episode SET number=?, season=?, title=?, description=?, ID_series=? WHERE ID=?");
            dEpisode = connection.prepareStatement("DELETE FROM e_episode WHERE ID=?");

            //Genre
            sGenreByID = connection.prepareStatement("SELECT * FROM e_genre WHERE ID=?");
            sGenres = connection.prepareStatement("SELECT ID FROM e_genre");
            sGenresBySeries = connection.prepareStatement("SELECT ID_genre FROM r_genre_series WHERE ID_series=?");
            sGenresByUser = connection.prepareStatement("SELECT ID_genre FROM r_user_genre WHERE ID_user=?");
            iGenre = connection.prepareStatement("INSERT INTO e_genre (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            uGenre = connection.prepareStatement("UPDATE e_genre SET name=? WHERE ID=?");
            dGenre = connection.prepareStatement("DELETE FROM e_genre WHERE ID=?");

            // Group
            sGroupByID = connection.prepareStatement("SELECT * FROM e_group WHERE ID=?");
            sGroupByUser = connection.prepareStatement("SELECT ID_group FROM e_user WHERE ID=?");
            sGroups = connection.prepareStatement("SELECT ID FROM e_group");
            sGroupsByService = connection.prepareStatement("SELECT ID_group FROM r_service_group WHERE ID_service=?");
            iGroup = connection.prepareStatement("INSERT INTO e_group (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            uGroup = connection.prepareStatement("UPDATE e_group SET name=?, description=? WHERE ID=?");
            dGroup = connection.prepareStatement("DELETE FROM e_group WHERE ID=?");

            // Message
            sMessageByID = connection.prepareStatement("SELECT * FROM e_message WHERE ID=?");
            sMessages = connection.prepareStatement("SELECT ID FROM e_message");
            sMessagesByUser = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=?");
            sMessagesBySeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_series=?");
            sMessagesByUserAndSeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=? AND ID_series=?"); //redoundant query
            iMessage = connection.prepareStatement("INSERT INTO e_message (title, text, date, ID_user, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uMessage = connection.prepareStatement("UPDATE e_message SET title=?, text=?, date=?, ID_user=?, ID_series=? WHERE ID=?");
            dMessage = connection.prepareStatement("DELETE FROM e_message WHERE ID=?");

            // News
            sNewsByID = connection.prepareStatement("SELECT * FROM e_news WHERE ID=?");
            sNewsByComment = connection.prepareStatement("SELECT ID_news FROM r_news_comment WHERE ID_comment=?");
            sNews = connection.prepareStatement("SELECT ID FROM e_news");
            sNewsByUser = connection.prepareStatement("SELECT ID FROM e_news WHERE ID_user=?");
            sNewsbySeries = connection.prepareStatement("SELECT ID_news FROM r_news_series WHERE ID_series=?");
            iNews = connection.prepareStatement("INSERT INTO e_news (title, text, date, image_URL, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uNews = connection.prepareStatement("UPDATE e_news SET title=?, text=?, date=?, image_URL=?, likes=?, dislikes=?, ID_user=? WHERE ID=?");
            dNews = connection.prepareStatement("DELETE FROM e_news WHERE ID=?");

            // Series
            sSeriesByID = connection.prepareStatement("SELECT * FROM e_series WHERE ID=?");
            sSeriesByMessage = connection.prepareStatement("SELECT ID_series FROM e_message WHERE ID=?");
            sSeriesByComment = connection.prepareStatement("SELECT ID_series FROM r_comment_series WHERE ID_comment=?");
            sSeriesByEpisode = connection.prepareStatement("SELECT ID_series FROM e_episode WHERE ID=?");
            sSeries = connection.prepareStatement("SELECT ID FROM e_series");
            sSeriesByNews = connection.prepareStatement("SELECT ID_series FROM r_news_series WHERE ID_news=?");
            sSeriesByGenre = connection.prepareStatement("SELECT ID_series FROM r_genre_series WHERE ID_genre=?");
            sSeriesByCastMember = connection.prepareStatement("SELECT ID_series FROM r_cast_member_series WHERE ID_cast_member=?");
            sSeriesByCastMemberAndRole = connection.prepareStatement("SELECT ID_series FROM r_cast_member_series WHERE ID_cast_member=? AND role=?");
            sSeriesByUser = connection.prepareStatement("SELECT ID_series FROM r_user_series WHERE ID_user=?");
            iSeries = connection.prepareStatement("INSERT INTO e_series (name, year, description, image_URL, state, add_count) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uSeries = connection.prepareStatement("UPDATE e_Series SET name=?, year=?, description=?, image_URL=?, state=?, add_count=? WHERE ID=?");
            dSeries = connection.prepareStatement("DELETE FROM e_series WHERE ID=?");

            // Service
            sServiceByID = connection.prepareStatement("SELECT * FROM e_service WHERE ID=?");
            sServices = connection.prepareStatement("SELECT ID FROM e_service");
            sServicesByGroup = connection.prepareStatement("SELECT ID_service FROM r_service_group WHERE ID_group=?");
            iService = connection.prepareStatement("INSERT INTO e_service (name, description, script_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uService = connection.prepareStatement("UPDATE e_service SET name=?, description=?, script_name=? WHERE ID=?");
            dService = connection.prepareStatement("DELETE FROM e_service WHERE ID=?");

            // User
            sUserByID = connection.prepareStatement("SELECT * FROM e_user WHERE ID=?");
            sUserByUsernameAndPassword = connection.prepareStatement("SELECT ID FROM e_user WHERE username=? AND password=?");
            sUserByComment = connection.prepareStatement("SELECT ID_user FROM e_comment WHERE ID=?");
            sUserByMessage = connection.prepareStatement("Select ID_user FROM e_message WHERE ID=?");
            sUserByNews = connection.prepareStatement("SELECT ID_user FROM e_news WHERE ID=?");
            sUsers = connection.prepareStatement("SELECT ID FROM e_user");
            sUsersBySeries = connection.prepareStatement("SELECT ID_user FROM r_user_series WHERE ID_series=?");
            sUsersByGenre = connection.prepareStatement("SELECT ID_user FROM r_user_genre WHERE ID_genre=?");
            sUsersByGroup = connection.prepareStatement("SELECT ID FROM e_user WHERE ID_group=?");
            iUser = connection.prepareStatement("INSERT INTO e_user (username, password, mail, name, surname, age, gender, image_URL, notification_status, ID_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uUser = connection.prepareStatement("UPDATE e_user SET username=?, password=?, mail=?, name=?, surname=?, age=?, gender=?, image_URL=?, notification_status=?, ID_group=? WHERE ID=?");
            dUser = connection.prepareStatement("DELETE FROM e_user WHERE ID=?");

            // Relationship
            // Relationship with attribute
            // CastMemberSeries
            sCastMemberSeriesByID = connection.prepareStatement("SELECT * FROM r_cast_member_series WHERE ID=?");
            sCastMemberSeriesByCastMemberAndSeriesAndRole = connection.prepareStatement("SELECT ID FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?");
            sCastMemberSeries = connection.prepareStatement("SELECT ID FROM r_cast_member_series");
            sCastMemberSeriesByCastMember = connection.prepareStatement("SELECT ID FROM r_cast_member_series WHERE ID_cast_member=?");
            sCastMemberSeriesBySeries = connection.prepareStatement("SELECT ID FROM r_cast_member_series WHERE ID_series=?");
            sCastMemberSeriesByCastMemberAndSeries = connection.prepareStatement("SELECT ID FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=?");
            iCastMemberSeries = connection.prepareStatement("INSERT INTO r_cast_member_series (ID_cast_member, ID_series, role) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uCastMemberSeries = connection.prepareStatement("UPDATE r_cast_member_series SET ID_cast_member=?, ID_series=?, ID_role=? WHERE ID=?");
            dCastMemberSeries = connection.prepareCall("DELETE FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?");

            // ChannelEpisode
            sChannelEpisodeByID = connection.prepareStatement("SELECT * FROM r_channel_episode WHERE ID=?");
            sChannelEpisodeByChannelAndEpisodeAndDate = connection.prepareStatement("SELECT ID FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?");
            sChannelEpisode = connection.prepareStatement("SELECT ID FROM r_channel_episode");
            sChannelEpisodeByChannel = connection.prepareStatement("SELECT ID FROM r_channel_episode WHERE ID_channel=?");
            sChannelEpisodeByEpisode = connection.prepareStatement("SELECT ID FROM r_channel_episode WHERE ID_episode=?");
            sChannelEpisodeByChannelAndEpisode = connection.prepareStatement("SELECT ID FROM r_channel_episode WHERE ID_channel=? AND ID_episode=?");
            iChannelEpisode = connection.prepareStatement("INSERT INTO r_channel_episode (ID_channel, ID_episode, date) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uChannelEpisode = connection.prepareStatement("UPDATE r_channel_episode SET ID_channel=?, ID_episode=?, date=? WHERE ID=?");
            dChannelEpisode = connection.prepareStatement("DELETE FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?");

            // UserSeries
            sUserSeriesByID = connection.prepareStatement("SELECT * FROM r_user_series WHERE ID=?");
            sUserSeriesByUserAndSeries = connection.prepareStatement("SELECT ID FROM r_user_series WHERE ID_user=? AND ID_series=?");
            sUserSeries = connection.prepareStatement("SELECT ID FROM r_user_series");
            sUserSeriesBySeries = connection.prepareStatement("SELECT ID FROM r_user_series WHERE ID_series=?");
            sUserSeriesByUser = connection.prepareStatement("SELECT ID FROM r_user_series WHERE ID_user=?");
            iUserSeries = connection.prepareStatement("INSERT INTO r_user_series (ID_user, ID_series, rating, anticipation_notification, add_date, season, episode) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uUserSeries = connection.prepareStatement("UPDATE r_user_series SET ID_user=?, ID_series=?, rating=?, anticipation_notification=?, add_date=?, season=?, episode=? WHERE ID=?");
            dUserSeries = connection.prepareStatement("DELETE FROM r_user_series WHERE ID_user=? AND ID_series=?");

            // Relationship without attribute
            // CommentSeries
            iCommentSeries = connection.prepareStatement("INSERT INTO r_comment_series (ID_comment, ID_series) VALUES (?, ?)");
            dCommentSeries = connection.prepareStatement("DELETE FROM r_comment_series WHERE ID_comment=? AND ID_Series=?");

            // GenreSeries
            iGenreSeries = connection.prepareStatement("INSERT INTO r_genre_series (ID_genre , ID_series) VALUES (?, ?)");
            dGenreSeries = connection.prepareStatement("DELETE FROM r_genre_series WHERE ID_genre=? AND ID_series=?");

            // NewsComment
            iNewsComment = connection.prepareStatement("INSERT INTO r_news_comment (ID_news , ID_comment) VALUES (?, ?)");
            dNewsComment = connection.prepareStatement("DELETE FROM r_news_comment WHERE ID_news=? AND ID_comment=?");

            // NewsSeries
            iNewsSeries = connection.prepareStatement("INSERT INTO r_news_series (ID_news , ID_series) VALUES (?, ?)");
            dNewsSeries = connection.prepareStatement("DELETE FROM r_news_series WHERE ID_news=? AND ID_series=?");

            // ServiceGroup
            iServiceGroup = connection.prepareStatement("INSERT INTO r_service_group (ID_service , ID_group) VALUES (?, ?)");
            dServiceGroup = connection.prepareStatement("DELETE FROM r_service_group WHERE ID_service=? AND ID_group=?");

            // UserGenre
            iUserGenre = connection.prepareStatement("INSERT INTO r_user_genre (ID_user , ID_genre) VALUES (?, ?)");
            dUserGenre = connection.prepareStatement("DELETE FROM r_user_genre WHERE ID_user=? AND ID_genre=?");

            // Other
            sLastEpisodeSeen = connection.prepareStatement("SELECT season, episode FROM r_user_series WHERE  ID_user=? AND ID_series=?");
            sSeriesGeneralRating = connection.prepareStatement("SELECT SUM(rating) COUNT(1) FROM r_user_series WHERE ID_series=?");

        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // CastMember
    // ============================================================================================
    @Override
    public CastMember createCastMember() {
        return new CastMemberMySQL(this);
    }

    @Override
    // sCastMemberByID = "SELECT * FROM e_cast_member WHERE ID=?"
    public CastMember getCastMember(int castID) {
        ResultSet rs = null;
        CastMember result = null;
        try {
            sCastMemberByID.setInt(1, castID);
            rs = sCastMemberByID.executeQuery();
            if (rs.next()) {
                result = new CastMemberMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMembers = "SELECT ID FROM e_cast_member"
    public List<CastMember> getCastMembers() {
        ResultSet rs = null;
        List<CastMember> result = new ArrayList();
        try {
            rs = sCastMembers.executeQuery();
            while (rs.next()) {
                result.add(getCastMember(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMembersBySeries = "SELECT ID_cast_member FROM r_cast_member_series WHERE ID_series=?"
    public List<CastMember> getCastMembers(Series series) {
        ResultSet rs = null;
        List<CastMember> result = new ArrayList();
        try {
            sCastMembersBySeries.setInt(1, series.getID());
            rs = sCastMembersBySeries.executeQuery();
            while (rs.next()) {
                result.add(getCastMember(rs.getInt("ID_cast_member")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMembersBySeriesAndRole = "SELECT ID_cast_member FROM r_cast_member_series WHERE ID_series=? AND role=?"
    public List<CastMember> getCastMembers(Series series, String role) {
        ResultSet rs = null;
        List<CastMember> result = new ArrayList();
        try {
            sCastMembersBySeriesAndRole.setInt(1, series.getID());
            sCastMembersBySeriesAndRole.setString(2, role);
            rs = sCastMembersBySeriesAndRole.executeQuery();
            while (rs.next()) {
                result.add(getCastMember(rs.getInt("ID_cast_member")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iCastMember = "INSERT INTO e_cast_member (name, surname, birth_date, gender, country, image_URL) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeCastMember(CastMember castMember) {
        ResultSet rs = null;
        int ID = castMember.getID();
        try {
            if (ID > 0) { // Update
                if (!castMember.isDirty()) { // if not modified do nothing
                    return;
                }
                // else update on DB
                // uCastMemberSeries = "UPDATE e_cast_member SET name=?, surname=?, birth_date=?, gender=?, country=?, image_URL=? WHERE ID=?"
                uCastMember.setString(1, castMember.getName());
                uCastMember.setString(2, castMember.getSurname());
                if (castMember.getBirthDate() != null) {
                    uCastMember.setDate(3, new java.sql.Date(castMember.getBirthDate().getTime()));
                } else {
                    uCastMember.setDate(3, new java.sql.Date(new Date().getTime()));
                }
                uCastMember.setString(4, castMember.getGender());
                uCastMember.setString(5, castMember.getCountry());
                if (castMember.getImageURL() != null && !castMember.getImageURL().isEmpty()) {
                    uCastMember.setString(6, castMember.getImageURL());
                } else {
                    uCastMember.setNull(6, java.sql.Types.VARCHAR);
                }
                uCastMember.setInt(7, ID);
                uCastMember.executeUpdate();
            } else { // Insert
                iCastMember.setString(1, castMember.getName());
                iCastMember.setString(2, castMember.getSurname());
                if (castMember.getBirthDate() != null) {
                    iCastMember.setDate(3, new java.sql.Date(castMember.getBirthDate().getTime()));
                } else {
                    iCastMember.setDate(3, new java.sql.Date(new Date().getTime()));
                }
                iCastMember.setString(4, castMember.getGender());
                iCastMember.setString(5, castMember.getCountry());
                if (castMember.getImageURL() != null && !castMember.getImageURL().isEmpty()) {
                    iCastMember.setString(6, castMember.getImageURL());
                } else {
                    iCastMember.setNull(6, java.sql.Types.VARCHAR);
                }
                if (iCastMember.executeUpdate() == 1) { // query successful
                    rs = iCastMember.getGeneratedKeys(); // to get the key of record inserted
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // Store relationship
            List<CastMemberSeries> oldCastMemberSeries = RESTDataLayerMySQL.this.getCastMemberSeries(castMember);
            List<CastMemberSeries> newCastMemberSeries = castMember.getCastMemberSeries();
            if (newCastMemberSeries != null) {
                for (CastMemberSeries cms : oldCastMemberSeries) {
                    if (!newCastMemberSeries.contains(cms)) {
                        removeCastMemberSeries(cms);
                    }
                }
                for (CastMemberSeries cms : newCastMemberSeries) {
                    if (!oldCastMemberSeries.contains(cms)) {
                        storeCastMemberSeries(cms);
                    }
                }
            }
            if (ID > 0) { // the object is on DB and have a key
                castMember.copyFrom(getCastMember(ID)); // copy the DB object on the current object
            }
            castMember.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dCastMember = "DELETE FROM e_cast_member WHERE ID=?"
    public void removeCastMember(CastMember castMember) {
        try {
            dCastMember.setInt(1, castMember.getID());
            dCastMember.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Channel
    // ============================================================================================
    @Override
    public Channel createChannel() {
        return new ChannelMySQL(this);
    }

    @Override
    // sChannelByID = "SELECT * FROM e_channel WHERE ID=?"
    public Channel getChannel(int channelID) {
        ResultSet rs = null;
        Channel result = null;
        try {
            sChannelByID.setInt(1, channelID);
            rs = sChannelByID.executeQuery();
            if (rs.next()) {
                result = new ChannelMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannels = "SELECT ID FROM e_channel"
    public List<Channel> getChannels() {
        ResultSet rs = null;
        List<Channel> result = new ArrayList();
        try {
            rs = sChannels.executeQuery();
            while (rs.next()) {
                result.add(getChannel(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelsByEpisode = "SELECT ID_channel FROM r_channel_episodeWHERE ID_episode=?"
    public List<Channel> getChannels(Episode episode) {
        ResultSet rs = null;
        List<Channel> result = new ArrayList();
        try {
            sChannelsByEpisode.setInt(1, episode.getID());
            rs = sChannelsByEpisode.executeQuery();
            while (rs.next()) {
                result.add(getChannel(rs.getInt("ID_channel")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelsByType = "SELECT ID FROM e_channel WHERE type=?"
    public List<Channel> getChannels(String type) {
        ResultSet rs = null;
        List<Channel> result = new ArrayList();
        try {
            sChannelsByType.setString(1, type);
            rs = sChannelsByType.executeQuery();
            while (rs.next()) {
                result.add(getChannel(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iChannel = "INSERT INTO e_channel (name, number, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeChannel(Channel channel) {
        ResultSet rs = null;
        int ID = channel.getID();
        try {
            if (ID > 0) {
                // Update
                if (!channel.isDirty()) {
                    return;
                }
                // uChannel = "UPDATE e_channel SET name=?, number=?, type=? WHERE ID=?"
                uChannel.setString(1, channel.getName());
                uChannel.setInt(2, channel.getNumber());
                uChannel.setString(3, channel.getType());
                uChannel.setInt(4, ID);
                uChannel.executeUpdate();
            } else { // Insert 
                iChannel.setString(1, channel.getName());
                iChannel.setInt(2, channel.getNumber());
                iChannel.setString(3, channel.getType());
                if (iChannel.executeUpdate() == 1) {
                    rs = iChannel.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            List<ChannelEpisode> oldChannelEpisode = getChannelEpisode(channel);
            List<ChannelEpisode> newChannelEpisode = channel.getChannelEpisode();
            if (newChannelEpisode != null) {
                for (ChannelEpisode ce : oldChannelEpisode) {
                    if (!newChannelEpisode.contains(ce)) {
                        removeChannelEpisode(ce);
                    }
                }
                for (ChannelEpisode ce : newChannelEpisode) {
                    if (!oldChannelEpisode.contains(ce)) {
                        storeChannelEpisode(ce);
                    }
                }
            }
            if (ID > 0) {
                channel.copyFrom(getChannel(ID));
            }
            channel.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    //  dChannel = "DELETE FROM e_channel WHERE ID=?"
    public void removeChannel(Channel channel) {
        try {
            dChannel.setInt(1, channel.getID());
            dChannel.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Comment
    // ============================================================================================
    @Override
    public Comment createComment() {
        return new CommentMySQL(this);
    }

    @Override
    // sCommentByID = "SELECT * FROM e_comment WHERE ID=?"
    public Comment getComment(int commentID) {
        ResultSet rs = null;
        Comment result = null;
        try {
            sCommentByID.setInt(1, commentID);
            rs = sCommentByID.executeQuery();
            if (rs.next()) {
                result = new CommentMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sComments = "SELECT ID FROM e_comment"
    public List<Comment> getComments() {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            rs = sComments.executeQuery();
            while (rs.next()) {
                result.add(getComment(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCommentsBySeries = "SELECT ID_comment FROM r_comment_series WHERE ID_series=?"
    public List<Comment> getComments(Series series) {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            sCommentsBySeries.setInt(1, series.getID());
            rs = sCommentsBySeries.executeQuery();
            while (rs.next()) {
                result.add(getComment(rs.getInt("ID_comment")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCommentsByNews = "SELECT ID_comment FROM r_news_comment WHERE ID_news=?"
    public List<Comment> getComments(News news) {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            sCommentsByNews.setInt(1, news.getID());
            rs = sCommentsByNews.executeQuery();
            while (rs.next()) {
                result.add(getComment(rs.getInt("ID_comment")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCommentsByUser = "SELECT ID_comment FROM e_comment WHERE ID_user=?"
    public List<Comment> getComments(User user) {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            sCommentsByUser.setInt(1, user.getID());
            rs = sCommentsByUser.executeQuery();
            while (rs.next()) {
                result.add(getComment(rs.getInt("ID_comment")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iComment = "INSERT INTO e_comment (title, text, date, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeComment(Comment comment) {
        ResultSet rs = null;
        int ID = comment.getID();
        try {
            if (ID > 0) { // Update
                if (!comment.isDirty()) {
                    return;
                }
                // uComment = "UPDATE e_comment SET title=?, text=?, date=?, likes=?, dislikes=?, ID_user=? WHERE ID=?"
                uComment.setString(1, comment.getTitle());
                uComment.setString(2, comment.getText());
                uComment.setTimestamp(3, new java.sql.Timestamp(comment.getDate().getTime()));
                uComment.setInt(4, comment.getLikes());
                uComment.setInt(5, comment.getDislikes());
                if (comment.getUser() != null) {
                    uComment.setInt(6, comment.getUser().getID());
                } else {
                    uComment.setInt(6, 0);
                }
                uComment.setInt(7, ID);
                uComment.executeUpdate();
            } else { // Insert
                iComment.setString(1, comment.getTitle());
                iComment.setString(2, comment.getText());
                iComment.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
                iComment.setInt(4, comment.getLikes());
                iComment.setInt(5, comment.getDislikes());
                if (comment.getUser() != null) {
                    iComment.setInt(6, comment.getUser().getID());
                } else {
                    iComment.setInt(6, 0);
                }
                if (iComment.executeUpdate() == 1) {
                    rs = iComment.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            News oldNews = getNews(comment);
            News newNews = comment.getNews();
            if (newNews != null) {
                if (oldNews == null) {
                    storeNewsComment(newNews.getID(), ID);
                } else if (!oldNews.equals(newNews)) {
                    removeNewsComment(oldNews.getID(), ID);
                    storeNewsComment(newNews.getID(), ID);
                }
            }
            Series oldSeries = getSeries(comment);
            Series newSeries = comment.getSeries();
            if (newSeries != null) {
                if (oldSeries == null) {
                    storeCommentSeries(ID, newSeries.getID());
                } else if (!oldSeries.equals(newSeries)) {
                    removeCommentSeries(ID, oldSeries.getID());
                    storeCommentSeries(ID, newSeries.getID());
                }
            }
            if (ID > 0) {
                comment.copyFrom(getComment(ID));
            }
            comment.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dComment = "DELETE FROM e_comment WHERE ID=?"
    public void removeComment(Comment comment) {
        try {
            dComment.setInt(1, comment.getID());
            dComment.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Episode
    // ============================================================================================
    @Override
    public Episode createEpisode() {
        return new EpisodeMySQL(this);
    }

    @Override
    // sEpisodeByID = "SELECT * FROM e_episode WHERE ID=?"
    public Episode getEpisode(int episodeID) {
        ResultSet rs = null;
        Episode result = null;
        try {
            sEpisodeByID.setInt(1, episodeID);
            rs = sEpisodeByID.executeQuery();
            if (rs.next()) {
                result = new EpisodeMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sEpisodeBySeriesAndSeasonAndNumber = "SELECT * FROM e_episode WHERE ID_series=? AND season=? AND number=?"
    public Episode getEpisodeBySeriesAndSeasonAndNumber(Series series, int season, int number) {
        ResultSet rs = null;
        Episode result = null;
        try {
            sEpisodeBySeriesAndSeasonAndNumber.setInt(1, series.getID());
            sEpisodeBySeriesAndSeasonAndNumber.setInt(2, season);
            sEpisodeBySeriesAndSeasonAndNumber.setInt(3, number);
            rs = sEpisodeBySeriesAndSeasonAndNumber.executeQuery();
            if (rs.next()) {
                result = new EpisodeMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sEpisodes = "SELECT ID FROM e_episode"
    public List<Episode> getEpisodes() {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            rs = sEpisodes.executeQuery();
            while (rs.next()) {
                result.add(getEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sEpisodesBySeries = "SELECT ID DROM e_episode WHERE ID_series=?"
    public List<Episode> getEpisodes(Series series) {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            sEpisodesBySeries.setInt(1, series.getID());
            rs = sEpisodesBySeries.executeQuery();
            while (rs.next()) {
                result.add(getEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sEpisodesBySeriesAndSeason = "SELECT ID FROM e_episode WHERE ID_series=? AND season=?"
    public List<Episode> getEpisodes(Series series, int season) {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            sEpisodesBySeriesAndSeason.setInt(1, series.getID());
            sEpisodesBySeriesAndSeason.setInt(2, season);
            rs = sEpisodesBySeriesAndSeason.executeQuery();
            while (rs.next()) {
                result.add(getEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sEpisodesByChannel = "SELECT ID_episode FROM r_channel_episode WHERE ID_episode=?"
    public List<Episode> getEpisodes(Channel channel) {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            sEpisodesByChannel.setInt(1, channel.getID());
            rs = sEpisodesByChannel.executeQuery();
            while (rs.next()) {
                result.add(getEpisode(rs.getInt("ID_episode")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iEpisode = "INSERT INTO e_episode (number, season, title, description, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeEpisode(Episode episode) {
        ResultSet rs = null;
        int ID = episode.getID();
        try {
            if (ID > 0) { // Update
                if (!episode.isDirty()) {
                    return;
                }
                // uEpisode = "UPDATE e_episode SET number=?, season=?, title=?, description=?, ID_series=? WHERE ID=?"
                uEpisode.setInt(1, episode.getNumber());
                uEpisode.setInt(2, episode.getSeason());
                uEpisode.setString(3, episode.getTitle());
                uEpisode.setString(4, episode.getDescription());
                if (episode.getSeries() != null) {
                    uEpisode.setInt(5, episode.getSeries().getID());
                } else {
                    uEpisode.setInt(5, 0);
                }
                uEpisode.setInt(6, ID);
                uEpisode.executeUpdate();
            } else { // Insert
                iEpisode.setInt(1, episode.getNumber());
                iEpisode.setInt(2, episode.getSeason());
                iEpisode.setString(3, episode.getTitle());
                iEpisode.setString(4, episode.getDescription());
                if (episode.getSeries() != null) {
                    iEpisode.setInt(5, episode.getSeries().getID());
                } else {
                    iEpisode.setInt(5, 0);
                }
                if (iEpisode.executeUpdate() == 1) {
                    rs = iEpisode.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            // store relationship
            List<ChannelEpisode> oldChannelEpisode = getChannelEpisode(episode);
            List<ChannelEpisode> newChannelEpisode = episode.getChannelEpisode();
            if (newChannelEpisode != null) {
                for (ChannelEpisode ce : oldChannelEpisode) {
                    if (!newChannelEpisode.contains(ce)) {
                        removeChannelEpisode(ce);
                    }
                }
                for (ChannelEpisode ce : newChannelEpisode) {
                    if (!oldChannelEpisode.contains(ce)) {
                        storeChannelEpisode(ce);
                    }
                }
            }
            if (ID > 0) {
                episode.copyFrom(getEpisode(ID));
            }
            episode.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dEpisode = "DELETE FROM e_episode WHERE ID=?"
    public void removeEpisode(Episode episode) {
        try {
            dEpisode.setInt(1, episode.getID());
            dEpisode.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Genre
    // ============================================================================================
    @Override
    public Genre createGenre() {
        return new GenreMySQL(this);
    }

    @Override
    // sGenreByID = "SELECT * FROM e_genre WHERE ID=?"
    public Genre getGenre(int genreID) {
        ResultSet rs = null;
        Genre result = null;
        try {
            sGenreByID.setInt(1, genreID);
            rs = sGenreByID.executeQuery();
            if (rs.next()) {
                result = new GenreMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sGenres = "SELECT ID FROM e_genre"
    public List<Genre> getGenres() {
        ResultSet rs = null;
        List<Genre> result = new ArrayList();
        try {
            rs = sGenres.executeQuery();
            while (rs.next()) {
                result.add(getGenre(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sGenresBySeries = "SELECT ID_genre FROM r_genre_series WHERE ID_series=?"
    public List<Genre> getGenres(Series series) {
        ResultSet rs = null;
        List<Genre> result = new ArrayList();
        try {
            sGenresBySeries.setInt(1, series.getID());
            rs = sGenresBySeries.executeQuery();
            while (rs.next()) {
                result.add(getGenre(rs.getInt("ID_genre")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sGenresByUser = "SELECT ID_genre FROM r_user_genre WHERE ID_user=?"
    public List<Genre> getGenres(User user) {
        ResultSet rs = null;
        List<Genre> result = new ArrayList();
        try {
            sGenresByUser.setInt(1, user.getID());
            rs = sGenresByUser.executeQuery();
            while (rs.next()) {
                result.add(getGenre(rs.getInt("ID_genre")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iGenre = "INSERT INTO e_genre (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS
    public void storeGenre(Genre genre) {
        ResultSet rs = null;
        int ID = genre.getID();
        try {
            if (ID > 0) { // Update
                if (!genre.isDirty()) {
                    return;
                }
                // uGenre = "UPDATE e_genre name=? WHERE ID=?"
                uGenre.setString(1, genre.getName());
                uGenre.setInt(2, ID);
                uGenre.executeUpdate();
            } else { // Insert
                iGenre.setString(1, genre.getName());
                if (iGenre.executeUpdate() == 1) {
                    rs = iGenre.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            // store relationship
            List<Series> oldSeries = getSeries(genre);
            List<Series> newSeries = genre.getSeries();
            if (newSeries != null) {
                for (Series s : oldSeries) {
                    if (!newSeries.contains(s)) {
                        removeGenreSeries(ID, s.getID());
                    }
                }
                for (Series s : newSeries) {
                    if (!oldSeries.contains(s)) {
                        storeGenreSeries(ID, s.getID());
                    }
                }
            }
            List<User> oldUsers = getUsers(genre);
            List<User> newUsers = genre.getUsers();
            if (newUsers != null) {
                for (User u : oldUsers) {
                    if (!newUsers.contains(u)) {
                        removeUserGenre(u.getID(), ID);
                    }
                }
                for (User u : newUsers) {
                    if (!oldUsers.contains(u)) {
                        storeUserGenre(u.getID(), ID);
                    }
                }
            }
            if (ID > 0) {
                genre.copyFrom(getGenre(ID));
            }
            genre.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dGenre = "DELETE FROM e_genre WHERE ID=?"
    public void removeGenre(Genre genre) {
        try {
            dGenre.setInt(1, genre.getID());
            dGenre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Group
    // ============================================================================================
    @Override
    public Group createGroup() {
        return new GroupMySQL(this);
    }

    @Override
    // sGroupByID = "SELECT * FROM e_group WHERE ID=?"
    public Group getGroup(int groupID) {
        ResultSet rs = null;
        Group result = null;
        try {
            sGroupByID.setInt(1, groupID);
            rs = sGroupByID.executeQuery();
            if (rs.next()) {
                result = new GroupMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sGroupByUser = "SELECT ID_group FROM e_user WHERE ID=?"
    public Group getGroup(User user) {
        ResultSet rs = null;
        Group result = null;
        try {
            sGenresByUser.setInt(1, user.getID());
            rs = sGroupByUser.executeQuery();
            if (rs.next()) {
                result = getGroup(rs.getInt("ID_group"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sGroups = "SELECT ID FROM e_groups"
    public List<Group> getGroups() {
        ResultSet rs = null;
        List<Group> result = new ArrayList();
        try {
            rs = sGroups.executeQuery();
            while (rs.next()) {
                result.add(getGroup(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sGroupsByService = "SELECT ID_group FROM r_service_group WHERE ID_service=?"
    public List<Group> getGroups(Service service) {
        ResultSet rs = null;
        List<Group> result = new ArrayList();
        try {
            sGroupsByService.setInt(1, service.getID());
            rs = sGroupsByService.executeQuery();
            while (rs.next()) {
                result.add(getGroup(rs.getInt("ID_group")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iGroup = "INSERT INTO e_group (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeGroup(Group group) {
        ResultSet rs = null;
        int ID = group.getID();
        try {
            if (ID > 0) { // Update
                if (!group.isDirty()) {
                    return;
                }
                // uGroup = "UPDATE e_group name=?, description=? WHERE ID=?"
                uGroup.setString(1, group.getName());
                uGroup.setString(2, group.getDescription());
                uGroup.setInt(3, ID);
                uGroup.executeUpdate();
            } else { // Insert
                iGroup.setString(1, group.getName());
                iGroup.setString(2, group.getDescription());
                if (iGroup.executeUpdate() == 1) {
                    rs = iGroup.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            List<Service> oldServices = getServices(group);
            List<Service> newServices = group.getServices();
            if (newServices != null) {
                for (Service s : oldServices) {
                    if (!newServices.contains(s)) {
                        removeServiceGroup(s.getID(), ID);
                    }
                }
                for (Service s : newServices) {
                    if (!oldServices.contains(s)) {
                        storeServiceGroup(s.getID(), ID);
                    }
                }
            }
            if (ID > 0) {
                group.copyFrom(getGroup(ID));
            }
            group.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dGroup = "DELETE FROM e_group WHERE ID=?"
    public void removeGroup(Group group) {
        try {
            dGroup.setInt(1, group.getID());
            dGroup.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Message
    // ============================================================================================
    @Override
    public Message createMessage() {
        return new MessageMySQL(this);
    }

    @Override
    // sMessageByID = "SELECT * FROM e_message WHERE ID=?"
    public Message getMessage(int messageID) {
        ResultSet rs = null;
        Message result = null;
        try {
            sMessageByID.setInt(1, messageID);
            rs = sMessageByID.executeQuery();
            if (rs.next()) {
                result = new MessageMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sMessages = "SELECT ID FROM e_message"
    public List<Message> getMessages() {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            rs = sMessages.executeQuery();
            while (rs.next()) {
                result.add(getMessage(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sMessagesByUser = "SELECT ID FROM e_message WHERE ID_user=?"
    public List<Message> getMessages(User user) {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            sMessagesByUser.setInt(1, user.getID());
            rs = sMessagesByUser.executeQuery();
            while (rs.next()) {
                result.add(getMessage(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sMessagesBySeries = "SELECT ID FROM e_message WHERE ID_series=?"
    public List<Message> getMessages(Series series) {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            sMessagesBySeries.setInt(1, series.getID());
            rs = sMessagesBySeries.executeQuery();
            while (rs.next()) {
                result.add(getMessage(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sMessagesByUserAndSeries = "SELECT ID FROM e_message WHERE ID_user=? AND ID_series=?"
    public List<Message> getMessages(Series series, User user) {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            sMessagesByUserAndSeries.setInt(1, user.getID());
            sMessagesByUserAndSeries.setInt(2, series.getID());
            rs = sMessagesByUserAndSeries.executeQuery();
            while (rs.next()) {
                result.add(getMessage(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iMessage = "INSERT INTO e_message (title, text, date, ID_user, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeMessage(Message message) {
        ResultSet rs = null;
        int ID = message.getID();
        try {
            if (ID > 0) { // Update
                if (!message.isDirty()) {
                    return;
                }
                // uMessage = "UPDATE e_message title=?, text=?, date=?, ID_user=?, ID_series=? WHERE ID=?"
                uMessage.setString(1, message.getTitle());
                uMessage.setString(2, message.getText());
                uMessage.setTimestamp(3, new java.sql.Timestamp(message.getDate().getTime()));
                uMessage.setInt(4, message.getUser().getID());
                uMessage.setInt(5, message.getSeries().getID());
                uMessage.setInt(6, ID);
                uMessage.executeUpdate();
            } else { // Insert
                iMessage.setString(1, message.getTitle());
                iMessage.setString(2, message.getText());
                iMessage.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
                if (message.getUser() != null) {
                    iMessage.setInt(4, message.getUser().getID());
                } else {
                    iMessage.setInt(4, 0);
                }
                if (message.getSeries() != null) {
                    iMessage.setInt(5, message.getSeries().getID());
                } else {
                    iMessage.setInt(5, 0);
                }
                if (iMessage.executeUpdate() == 1) {
                    rs = iMessage.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            if (ID > 0) {
                message.copyFrom(getMessage(ID));
            }
            message.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dMessage = "DELETE FROM e_message WHERE ID=?"
    public void removeMessage(Message message) {
        try {
            dMessage.setInt(1, message.getID());
            dMessage.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // News
    // ============================================================================================
    @Override
    public News createNews() {
        return new NewsMySQL(this);
    }

    @Override
    // sNewsByID = "SELECT * FROM e_news"
    public News getNews(int newsID) {
        ResultSet rs = null;
        News result = null;
        try {
            sNewsByID.setInt(1, newsID);
            rs = sNewsByID.executeQuery();
            if (rs.next()) {
                result = new NewsMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sNewsByComment = "SELECT ID_news FROM r_news_comment WHERE ID_comment=?"
    public News getNews(Comment comment) {
        ResultSet rs = null;
        News result = null;
        try {
            sNewsByComment.setInt(1, comment.getID());
            rs = sNewsByComment.executeQuery();
            while (rs.next()) {
                result = getNews(rs.getInt("ID_news"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sNews = "SELECT ID FROM e_news"
    public List<News> getNews() {
        ResultSet rs = null;
        List<News> result = new ArrayList();
        try {
            rs = sNews.executeQuery();
            while (rs.next()) {
                result.add(getNews(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sNewsByUser = "SELECT ID FROM e_news WHERE ID_user=?"
    public List<News> getNews(User user) {
        ResultSet rs = null;
        List<News> result = new ArrayList();
        try {
            sNewsByUser.setInt(1, user.getID());
            rs = sNewsByUser.executeQuery();
            while (rs.next()) {
                result.add(getNews(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sNewsbySeries = "SELECT ID_news FROM r_news_series WHERE ID_series=?"
    public List<News> getNews(Series series) {
        ResultSet rs = null;
        List<News> result = new ArrayList();
        try {
            sNewsbySeries.setInt(1, series.getID());
            rs = sNewsbySeries.executeQuery();
            while (rs.next()) {
                result.add(getNews(rs.getInt("ID_news")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iNews = "INSERT INTO e_news (title, text, date, image_URL, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeNews(News news) {
        ResultSet rs = null;
        int ID = news.getID();
        try {
            if (ID > 0) { // Update
                if (!news.isDirty()) {
                    return;
                }
                // uNews = "UPDATE e_news title=?, text=?, date=?, image_URL=?, likes=?, dislikes=?, ID_user=? WHERE ID=?"
                uNews.setString(1, news.getTitle());
                uNews.setString(2, news.getText());
                uNews.setDate(3, new java.sql.Date(news.getDate().getTime()));
                uNews.setString(4, news.getImageURL());
                uNews.setInt(5, news.getLikes());
                uNews.setInt(6, news.getDislikes());
                if (news.getUser() != null) {
                    uNews.setInt(7, news.getUser().getID());
                } else {
                    uNews.setInt(7, 0);
                }
                uNews.setInt(8, ID);
                uNews.executeUpdate();
            } else { // Insert
                iNews.setString(1, news.getTitle());
                iNews.setString(2, news.getText());
                iNews.setDate(3, new java.sql.Date(new Date().getTime()));
                iNews.setString(4, news.getImageURL());
                iNews.setInt(5, news.getLikes());
                iNews.setInt(6, news.getDislikes());
                if (news.getUser() != null) {
                    iNews.setInt(7, news.getUser().getID());
                } else {
                    iNews.setInt(7, 0);
                }
                if (iNews.executeUpdate() == 1) {
                    rs = iNews.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            List<Series> oldSeries = getSeries(news);
            List<Series> newSeries = news.getSeries();
            if (newSeries != null) {
                for (Series s : oldSeries) {
                    if (!newSeries.contains(s)) {
                        removeNewsSeries(ID, s.getID());
                    }
                }
                for (Series s : news.getSeries()) {
                    if (!oldSeries.contains(s)) {
                        storeNewsSeries(ID, s.getID());
                    }
                }
            }
            List<Comment> oldComments = getComments(news);
            List<Comment> newComments = news.getComments();
            if (newComments != null) {
                for (Comment c : oldComments) {
                    if (!newComments.contains(c)) {
                        removeNewsComment(ID, c.getID());
                    }
                }
                for (Comment c : newComments) {
                    if (!oldComments.contains(c)) {
                        storeNewsComment(ID, c.getID());
                    }
                }
            }
            if (ID > 0) {
                news.copyFrom(getNews(ID));
            }
            news.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dNews = "DELETE FROM e_news WHERE ID=?"
    public void removeNews(News news) {
        try {
            dNews.setInt(1, news.getID());
            dNews.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Series
    // ============================================================================================
    @Override
    public Series createSeries() {
        return new SeriesMySQL(this);
    }

    @Override
    // sSeriesByID = "SELECT * FROM e_series WHERE ID=?"
    public Series getSeries(int seriesID) {
        ResultSet rs = null;
        Series result = null;
        try {
            sSeriesByID.setInt(1, seriesID);
            rs = sSeriesByID.executeQuery();
            if (rs.next()) {
                result = new SeriesMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByMessage = "SELECT ID_series FROM e_message WHERE ID=?"
    public Series getSeries(Message message) {
        ResultSet rs = null;
        Series result = null;
        try {
            sSeriesByMessage.setInt(1, message.getID());
            rs = sSeriesByMessage.executeQuery();
            while (rs.next()) {
                result = getSeries(rs.getInt("ID_series"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByEpisode = "SELECT ID_series FROM e_episode WHERE ID=?"
    public Series getSeries(Episode episode) {
        ResultSet rs = null;
        Series result = null;
        try {
            sSeriesByEpisode.setInt(1, episode.getID());
            rs = sSeriesByEpisode.executeQuery();
            while (rs.next()) {
                result = getSeries(rs.getInt("ID_series"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByComment = "SELECT ID_series FROM r_comment_series WHERE ID_comment=?"
    public Series getSeries(Comment comment) {
        ResultSet rs = null;
        Series result = null;
        try {
            sSeriesByComment.setInt(1, comment.getID());
            rs = sSeriesByComment.executeQuery();
            while (rs.next()) {
                result = getSeries(rs.getInt("ID_series"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeries = "SELECT ID FROM e_series"
    public List<Series> getSeries() {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            rs = sSeries.executeQuery();
            while (rs.next()) {
                result.add(getSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByNews = "SELECT ID_series FROM r_news_series WHERE ID_news=?"
    public List<Series> getSeries(News news) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            sSeriesByNews.setInt(1, news.getID());
            rs = sSeriesByNews.executeQuery();
            while (rs.next()) {
                result.add(getSeries(rs.getInt("ID_series")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByGenre = "SELECT ID_series FROM r_genre_series WHERE ID_genre=?"
    public List<Series> getSeries(Genre genre) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            sSeriesByGenre.setInt(1, genre.getID());
            rs = sSeriesByGenre.executeQuery();
            while (rs.next()) {
                result.add(getSeries(rs.getInt("ID_series")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByCastMember = "SELECT ID_series FROM r_cast_member_series WHERE ID_cast_member=?"
    public List<Series> getSeries(CastMember castMember) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            sSeriesByCastMember.setInt(1, castMember.getID());
            rs = sSeriesByCastMember.executeQuery();
            while (rs.next()) {
                result.add(getSeries(rs.getInt("ID_series")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByCastMemberAndRole = "SELECT ID_series FROM r_cast_member_series WHERE ID_cast_member=? AND role=?"
    public List<Series> getSeries(CastMember castMember, String role) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            sSeriesByCastMemberAndRole.setInt(1, castMember.getID());
            sSeriesByCastMemberAndRole.setString(2, role);
            rs = sSeriesByCastMemberAndRole.executeQuery();
            while (rs.next()) {
                result.add(getSeries(rs.getInt("ID_series")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesByUser = "SELECT ID_series FROM r_user_series WHERE ID_user=?"
    public List<Series> getSeries(User user) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            sSeriesByUser.setInt(1, user.getID());
            rs = sSeriesByUser.executeQuery();
            while (rs.next()) {
                result.add(getSeries(rs.getInt("ID_series")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iSeries = "INSERT INTO e_series (name, year, description, image_URL, state, add_count) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeSeries(Series series) {
        ResultSet rs = null;
        int ID = series.getID();
        try {
            if (ID > 0) { // Update
                if (!series.isDirty()) {
                    return;
                }
                // uSeries = "UPDATE e_Series SET name=?, year=?, description=?, image_URL=?, state=?, add_count=? WHERE ID=?"
                uSeries.setString(1, series.getName());
                uSeries.setInt(2, series.getYear());
                uSeries.setString(3, series.getDescription());
                uSeries.setString(4, series.getImageURL());
                uSeries.setString(5, series.getState());
                uSeries.setInt(6, series.getAddCount());
                uSeries.setInt(7, ID);
                uSeries.executeUpdate();
            } else { // Insert
                iSeries.setString(1, series.getName());
                iSeries.setInt(2, series.getYear());
                iSeries.setString(3, series.getDescription());
                iSeries.setString(4, series.getImageURL());
                iSeries.setString(5, series.getState());
                iSeries.setInt(6, series.getAddCount());
                if (iSeries.executeUpdate() == 1) {
                    rs = iSeries.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            List<UserSeries> oldUserSeries = getUserSeries(series);
            List<UserSeries> newUserSeries = series.getUserSeries();
            if (newUserSeries != null) {
                for (UserSeries us : oldUserSeries) {
                    if (!newUserSeries.contains(us)) {
                        removeUserSeries(us);
                    }
                }
                for (UserSeries us : newUserSeries) {
                    if (!oldUserSeries.contains(us)) {
                        storeUserSeries(us);
                    }
                }
            }
            List<CastMemberSeries> oldCastMemberSeries = getCastMemberSeries(series);
            List<CastMemberSeries> newCastMemberSeries = series.getCastMemberSeries();
            if (newCastMemberSeries != null) {
                for (CastMemberSeries cms : oldCastMemberSeries) {
                    if (!newCastMemberSeries.contains(cms)) {
                        removeCastMemberSeries(cms);
                    }
                }
                for (CastMemberSeries cms : newCastMemberSeries) {
                    if (!oldCastMemberSeries.contains(cms)) {
                        storeCastMemberSeries(cms);
                    }
                }
            }
            List<Genre> oldGenres = getGenres(series);
            List<Genre> newGenres = series.getGenres();
            if (newGenres != null) {
                for (Genre g : oldGenres) {
                    if (!newGenres.contains(g)) {
                        removeGenreSeries(g.getID(), ID);
                    }
                }
                for (Genre g : newGenres) {
                    if (!oldGenres.contains(g)) {
                        storeGenreSeries(g.getID(), ID);
                    }
                }
            }
            List<News> oldNews = getNews(series);
            List<News> newNews = series.getNews();
            if (newNews != null) {
                for (News n : oldNews) {
                    if (!newNews.contains(n)) {
                        removeNewsSeries(n.getID(), ID);
                    }
                }
                for (News n : newNews) {
                    if (!oldNews.contains(n)) {
                        storeNewsSeries(n.getID(), ID);
                    }
                }
            }
            List<Comment> oldComments = getComments(series);
            List<Comment> newComments = series.getComments();
            if (newComments != null) {
                for (Comment c : oldComments) {
                    if (!newComments.contains(c)) {
                        removeCommentSeries(c.getID(), ID);
                    }
                }
                for (Comment c : newComments) {
                    if (!oldComments.contains(c)) {
                        storeCommentSeries(c.getID(), ID);
                    }
                }
            }
            if (ID > 0) {
                series.copyFrom(getSeries(ID));
            }
            series.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dSeries = "DELETE FROM e_series WHERE ID=?"
    public void removeSeries(Series series) {
        try {
            dSeries.setInt(1, series.getID());
            dSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Service
    // ============================================================================================
    @Override
    public Service createService() {
        return new ServiceMySQL(this);
    }

    @Override
    // sServiceByID = "SELECT * FROM e_service WHERE ID=?"
    public Service getService(int serviceID) {
        ResultSet rs = null;
        Service result = null;
        try {
            sServiceByID.setInt(1, serviceID);
            rs = sServiceByID.executeQuery();
            if (rs.next()) {
                result = new ServiceMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sServices = "SELECT ID FROM e_service"
    public List<Service> getServices() {
        ResultSet rs = null;
        List<Service> result = new ArrayList();
        try {
            rs = sServices.executeQuery();
            while (rs.next()) {
                result.add(getService(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sServicesByGroup = "SELECT ID_service FROM r_service_group WHERE ID_group=?"
    public List<Service> getServices(Group group) {
        ResultSet rs = null;
        List<Service> result = new ArrayList();
        try {
            sServicesByGroup.setInt(1, group.getID());
            rs = sServicesByGroup.executeQuery();
            while (rs.next()) {
                result.add(getService(rs.getInt("ID_service")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iService = "INSERT INTO e_service (name, description, script_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeService(Service service) {
        ResultSet rs = null;
        int ID = service.getID();
        try {
            if (ID > 0) { // Update
                if (!service.isDirty()) {
                    return;
                }
                // uService = "UPDATE e_service name=?, description=?, script_name=? WHERE ID=?"
                uService.setString(1, service.getName());
                uService.setString(2, service.getDescription());
                uService.setString(3, service.getScriptName());
                uService.setInt(4, ID);
                uService.executeUpdate();
            } else { // Insert
                iService.setString(1, service.getName());
                iService.setString(2, service.getDescription());
                iService.setString(3, service.getScriptName());
                if (iService.executeUpdate() == 1) {
                    rs = iService.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            List<Group> oldGroups = getGroups(service);
            List<Group> newGroups = service.getGroups();
            if (newGroups != null) {
                for (Group g : oldGroups) {
                    if (!newGroups.contains(g)) {
                        removeServiceGroup(ID, g.getID());
                    }
                }
                for (Group g : newGroups) {
                    if (!oldGroups.contains(g)) {
                        storeServiceGroup(ID, g.getID());
                    }
                }
            }
            if (ID > 0) {
                service.copyFrom(getService(ID));
            }
            service.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dService = "DELETE FROM e_service WHERE ID=?"
    public void removeService(Service service) {
        try {
            dService.setInt(1, service.getID());
            dService.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // User
    // ============================================================================================
    @Override
    public User createUser() {
        return new UserMySQL(this);
    }

    @Override
    // sUserByID = "SELECT * FROM e_user WHERE ID=?"
    public User getUser(int userID) {
        ResultSet rs = null;
        User result = null;
        try {
            sUserByID.setInt(1, userID);
            rs = sUserByID.executeQuery();
            if (rs.next()) {
                result = new UserMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserByUsernameAndPassword = "SELECT * FROM e_user WHERE username=? AND password=?"
    public User getUser(String username, String password) {
        ResultSet rs = null;
        User result = null;
        try {
            sUserByUsernameAndPassword.setString(1, username);
            sUserByUsernameAndPassword.setString(2, password);
            rs = sUserByUsernameAndPassword.executeQuery();
            if (rs.next()) {
                result = getUser(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserByMessage = "Select ID_user FROM e_message WHERE ID=?"
    public User getUser(Message message) {
        ResultSet rs = null;
        User result = null;
        try {
            sUserByMessage.setInt(1, message.getID());
            rs = sUserByMessage.executeQuery();
            while (rs.next()) {
                result = getUser(rs.getInt("ID_user"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserByComment = "SELECT ID_user FROM e_comment WHERE ID=?"
    public User getUser(Comment comment) {
        ResultSet rs = null;
        User result = null;
        try {
            sUserByComment.setInt(1, comment.getID());
            rs = sUserByComment.executeQuery();
            while (rs.next()) {
                result = getUser(rs.getInt("ID_user"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserByNews = "SELECT ID_user FROM e_news WHERE ID=?"
    public User getUser(News news) {
        ResultSet rs = null;
        User result = null;
        try {
            sUserByNews.setInt(1, news.getID());
            rs = sUserByNews.executeQuery();
            while (rs.next()) {
                result = getUser(rs.getInt("ID_user"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUsers = "SELECT ID FROM e_user"
    public List<User> getUsers() {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            rs = sUsers.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUsersBySeries = "SELECT ID_user FROM r_user_series WHERE ID_series=?"
    public List<User> getUsers(Series series) {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            sUsersBySeries.setInt(1, series.getID());
            rs = sUsersBySeries.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("ID_user")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUsersByGenre = "SELECT ID_user FROM r_user_genre WHERE ID_genre=?"
    public List<User> getUsers(Genre genre) {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            sUsersByGenre.setInt(1, genre.getID());
            rs = sUsersByGenre.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("ID_user")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUsersByGroup = "SELECT ID FROM e_user WHERE ID_group=?"
    public List<User> getUsers(Group group) {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            sUsersByGroup.setInt(1, group.getID());
            rs = sUsersByGroup.executeQuery();
            while (rs.next()) {
                result.add(getUser(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iUser = "INSERT INTO e_user (username, password, mail, name, surname, age, gender, image_URL, notification_status, ID_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
    public void storeUser(User user) {
        ResultSet rs = null;
        int ID = user.getID();
        try {
            if (ID > 0) { // Update
                if (!user.isDirty()) {
                    return;
                }
                // uUser = "UPDATE e_user username=?, password=?, mail=?, name=? surname=?, age=?, gender=?, image_URL=?, notification_status=?, ID_group=? WHERE ID=?"
                uUser.setString(1, user.getUsername());
                uUser.setString(2, user.getPassword());
                uUser.setString(3, user.getMail());
                if (user.getName() != null && !user.getName().isEmpty()) {
                    uUser.setString(4, user.getName());
                } else {
                    uUser.setNull(4, java.sql.Types.VARCHAR);
                }
                if (user.getSurname() != null && !user.getSurname().isEmpty()) {
                    uUser.setString(5, user.getSurname());
                } else {
                    uUser.setNull(5, java.sql.Types.VARCHAR);
                }
                if (user.getAge() != 0) {
                    uUser.setInt(6, user.getAge());
                } else {
                    uUser.setNull(6, java.sql.Types.INTEGER);
                }
                if (user.getGender() != null && !user.getGender().isEmpty()) {
                    uUser.setString(7, user.getGender());
                } else {
                    uUser.setNull(7, java.sql.Types.CHAR);
                }
                if (user.getImageURL() != null && !user.getImageURL().isEmpty()) {
                    uUser.setString(8, user.getImageURL());
                } else {
                    uUser.setNull(8, java.sql.Types.VARCHAR);
                }
                uUser.setBoolean(9, user.getNotificationStatus());
                if (user.getGroup() != null) {
                    uUser.setInt(10, user.getGroup().getID());
                } else {
                    iUser.setInt(10, Group.USER);
                }
                uUser.setInt(11, ID);
                uUser.executeUpdate();
            } else { // Insert
                iUser.setString(1, user.getUsername());
                iUser.setString(2, user.getPassword());
                iUser.setString(3, user.getMail());
                if (user.getName() != null && !user.getName().isEmpty()) {
                    iUser.setString(4, user.getName());
                } else {
                    iUser.setNull(4, java.sql.Types.VARCHAR);
                }
                if (user.getSurname() != null && !user.getSurname().isEmpty()) {
                    iUser.setString(5, user.getSurname());
                } else {
                    iUser.setNull(5, java.sql.Types.VARCHAR);
                }
                if (user.getAge() != 0) {
                    iUser.setInt(6, user.getAge());
                } else {
                    iUser.setNull(6, java.sql.Types.INTEGER);
                }
                if (user.getGender() != null && !user.getGender().isEmpty()) {
                    iUser.setString(7, user.getGender());
                } else {
                    iUser.setNull(7, java.sql.Types.CHAR);
                }
                if (user.getImageURL() != null && !user.getImageURL().isEmpty()) {
                    iUser.setString(8, user.getImageURL());
                } else {
                    iUser.setNull(8, java.sql.Types.VARCHAR);
                }
                iUser.setBoolean(9, user.getNotificationStatus());
                if (user.getGroup() != null) {
                    iUser.setInt(10, user.getGroup().getID());
                } else {
                    iUser.setInt(10, Group.USER);
                }
                if (iUser.executeUpdate() == 1) {
                    rs = iUser.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // store relationship
            List<UserSeries> oldUserSeries = getUserSeries(user);
            List<UserSeries> newUserSeries = user.getUserSeries();
            if (newUserSeries != null) {
                for (UserSeries us : oldUserSeries) {
                    if (!newUserSeries.contains(us)) {
                        removeUserSeries(us);
                    }
                }
                for (UserSeries us : newUserSeries) {
                    if (!oldUserSeries.contains(us)) {
                        storeUserSeries(us);
                    }
                }
            }
            List<Genre> oldGenres = getGenres(user);
            List<Genre> newGenres = user.getGenres();
            if (newGenres != null) {
                for (Genre g : oldGenres) {
                    if (!newGenres.contains(g)) {
                        removeUserGenre(ID, g.getID());
                    }
                }
                for (Genre g : newGenres) {
                    if (!oldGenres.contains(g)) {
                        storeUserGenre(ID, g.getID());
                    }
                }
            }
            if (ID > 0) {
                user.copyFrom(getUser(ID));
            }
            user.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dUser = "DELETE FROM e_user WHERE ID=?"
    public void removeUser(User user) {
        try {
            dUser.setInt(1, user.getID());
            dUser.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // CastMemberSeries
    // ============================================================================================
    @Override
    public CastMemberSeries createCastMemberSeries() {
        return new CastMemberSeriesMySQL(this);
    }

    @Override
    // sCastMemberSeriesByID = "SELECT * FROM r_cast_member_series WHERE ID=?"
    public CastMemberSeries getCastMemberSeries(int castMemberSeriesID) {
        CastMemberSeries result = null;
        ResultSet rs = null;
        try {
            sCastMemberSeriesByID.setInt(1, castMemberSeriesID);
            rs = sCastMemberSeriesByID.executeQuery();
            if (rs.next()) {
                result = new CastMemberSeriesMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMemberSeriesByCastMemberAndSeriesAndRole = "SELECT ID FROM r_cast_member_series WHERE ID_cast_member=?, ID_series=?, role=?"
    public CastMemberSeries getCastMemberSeries(CastMember castMember, Series series, String role) {
        CastMemberSeries result = null;
        ResultSet rs = null;
        try {
            sCastMemberSeriesByCastMemberAndSeriesAndRole.setInt(1, castMember.getID());
            sCastMemberSeriesByCastMemberAndSeriesAndRole.setInt(2, series.getID());
            sCastMemberSeriesByCastMemberAndSeriesAndRole.setString(3, role);
            rs = sCastMemberSeriesByCastMemberAndSeriesAndRole.executeQuery();
            if (rs.next()) {
                result = RESTDataLayerMySQL.this.getCastMemberSeries(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMemberSeries = "SELECT ID FROM r_cast_member_series"
    public List<CastMemberSeries> getCastMemberSeries() {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sCastMemberSeries.executeQuery();
            while (rs.next()) {
                result.add(RESTDataLayerMySQL.this.getCastMemberSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMemberSeriesByCastMember = "SELECT ID FROM r_cast_member_series WHERE ID_cast_member=?"
    public List<CastMemberSeries> getCastMemberSeries(CastMember castMember) {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sCastMemberSeriesByCastMember.setInt(1, castMember.getID());
            rs = sCastMemberSeriesByCastMember.executeQuery();
            while (rs.next()) {
                result.add(RESTDataLayerMySQL.this.getCastMemberSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMemberSeriesBySeries = "SELECT ID FROM r_cast_member_series WHERE ID_series=?"
    public List<CastMemberSeries> getCastMemberSeries(Series series) {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sCastMemberSeriesBySeries.setInt(1, series.getID());
            rs = sCastMemberSeriesBySeries.executeQuery();
            while (rs.next()) {
                result.add(RESTDataLayerMySQL.this.getCastMemberSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sCastMemberSeriesByCastMemberAndSeries = "SELECT ID FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=?"
    public List<CastMemberSeries> getCastMembeSeries(CastMember castMember, Series series) {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sCastMemberSeriesByCastMemberAndSeries.setInt(1, castMember.getID());
            sCastMemberSeriesByCastMemberAndSeries.setInt(2, series.getID());
            rs = sCastMemberSeriesByCastMemberAndSeries.executeQuery();
            while (rs.next()) {
                result.add(RESTDataLayerMySQL.this.getCastMemberSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iCastMemberSeries = "INSERT INTO r_cast_member_series (ID_cast_member, ID_series, role) VALUES(?, ?, ?), Statement.RETURN_GENERATED_KEYS"
    public void storeCastMemberSeries(CastMemberSeries castMemberSeries) {
        ResultSet rs = null;
        int ID = castMemberSeries.getID();
        try {
            if (ID > 0) { // update
                if (!castMemberSeries.isDirty()) {
                    return;
                }
                // uCastMemberSeries = "UPDATE r_cast_member_series SET ID_cast_member=?, ID_series=?, ID_role=? WHERE ID=?"
                if (castMemberSeries.getCastMember() != null) {
                    uCastMemberSeries.setInt(1, castMemberSeries.getCastMember().getID());
                } else {
                    uCastMemberSeries.setNull(1, java.sql.Types.INTEGER);
                }
                if (castMemberSeries.getSeries() != null) {
                    uCastMemberSeries.setInt(2, castMemberSeries.getSeries().getID());
                } else {
                    uCastMemberSeries.setNull(2, java.sql.Types.INTEGER);
                }
                uCastMemberSeries.setString(3, castMemberSeries.getRole());
                uCastMemberSeries.setInt(4, ID);
                uCastMemberSeries.executeUpdate();
            } else { // insert
                if (castMemberSeries.getCastMember() != null) {
                    iCastMemberSeries.setInt(1, castMemberSeries.getCastMember().getID());
                } else {
                    iCastMemberSeries.setNull(1, java.sql.Types.INTEGER);
                }
                if (castMemberSeries.getSeries() != null) {
                    iCastMemberSeries.setInt(2, castMemberSeries.getSeries().getID());
                } else {
                    iCastMemberSeries.setNull(2, java.sql.Types.INTEGER);
                }
                iCastMemberSeries.setString(3, castMemberSeries.getRole());
                if (iCastMemberSeries.executeUpdate() == 1) {
                    rs = iCastMemberSeries.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            if (ID > 0) {
                castMemberSeries.copyFrom(RESTDataLayerMySQL.this.getCastMemberSeries(ID));
            }
            castMemberSeries.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dCastMemberSeries = "DELETE FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?"
    public void removeCastMemberSeries(CastMemberSeries castMemberSeries) {
        try {
            if (castMemberSeries.getCastMember() != null) {
                dCastMemberSeries.setInt(1, castMemberSeries.getCastMember().getID());
            } else {
                dCastMemberSeries.setNull(1, java.sql.Types.INTEGER);
            }
            if (castMemberSeries.getSeries() != null) {
                dCastMemberSeries.setInt(2, castMemberSeries.getSeries().getID());
            } else {
                dCastMemberSeries.setNull(2, java.sql.Types.INTEGER);
            }
            dCastMemberSeries.setString(3, castMemberSeries.getRole());
            dCastMemberSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // ChannelEpisode
    // ============================================================================================
    @Override
    public ChannelEpisode createChannelEpisode() {
        return new ChannelEpisodeMySQL(this);
    }

    @Override
    // sChannelEpisodeByID = "SELECT * FROM r_channel_episode WHERE ID=?"
    public ChannelEpisode getChannelEpisode(int channelEpisodeID) {
        ChannelEpisode result = null;
        ResultSet rs = null;
        try {
            sChannelEpisodeByID.setInt(1, channelEpisodeID);
            rs = sChannelEpisodeByID.executeQuery();
            if (rs.next()) {
                result = new ChannelEpisodeMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelEpisodeByChannelAndEpisodeAndDate = "SELECT ID FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?"
    public ChannelEpisode getChannelEpisode(Channel channel, Episode episode, Date date) {
        ChannelEpisode result = null;
        ResultSet rs = null;
        try {
            sChannelEpisodeByChannelAndEpisodeAndDate.setInt(1, channel.getID());
            sChannelEpisodeByChannelAndEpisodeAndDate.setInt(2, episode.getID());
            sChannelEpisodeByChannelAndEpisodeAndDate.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
            rs = sChannelEpisodeByChannelAndEpisodeAndDate.executeQuery();
            if (rs.next()) {
                result = getChannelEpisode(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelEpisode = "SELECT ID FROM r_channel_episode"
    public List<ChannelEpisode> getChannelEpisode() {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sChannelEpisode.executeQuery();
            while (rs.next()) {
                result.add(getChannelEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelEpisodeByChannel = "SELECT ID FROM r_channel_episode WHERE ID_channel=?"
    public List<ChannelEpisode> getChannelEpisode(Channel channel) {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChannelEpisodeByChannel.setInt(1, channel.getID());
            rs = sChannelEpisodeByChannel.executeQuery();
            while (rs.next()) {
                result.add(getChannelEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelEpisodeByEpisode  = "SELECT ID FROM r_channel_episode WHERE ID_episode=?"
    public List<ChannelEpisode> getChannelEpisode(Episode episode) {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChannelEpisodeByEpisode.setInt(1, episode.getID());
            rs = sChannelEpisodeByEpisode.executeQuery();
            while (rs.next()) {
                result.add(getChannelEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sChannelEpisodeByChannelAndEpisode = "SELECT ID FROM r_channel_episode WHERE ID_channel=? AND ID_episode=?"
    public List<ChannelEpisode> getChannelEpisode(Channel channel, Episode episode) {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChannelEpisodeByChannelAndEpisode.setInt(1, channel.getID());
            sChannelEpisodeByChannelAndEpisode.setInt(2, episode.getID());
            rs = sChannelEpisodeByChannelAndEpisode.executeQuery();
            while (rs.next()) {
                result.add(getChannelEpisode(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iChannelEpisode = "INSERT INTO r_channel_episode (ID_channel, ID_episode, date) VALUES(?, ?, ?), Statement.RETURN_GENERATED_KEYS"
    public void storeChannelEpisode(ChannelEpisode channelEpisode) {
        ResultSet rs = null;
        int ID = channelEpisode.getID();
        try {
            if (ID > 0) { // update
                if (!channelEpisode.isDirty()) {
                    return;
                }
                // uChannelEpisode = "UPDATE r_channel_episode SET ID_channel=?, ID_episode=?, date=? WHERE ID=?"
                if (channelEpisode.getChannel() != null) {
                    uChannelEpisode.setInt(1, channelEpisode.getChannel().getID());
                } else {
                    uChannelEpisode.setNull(1, java.sql.Types.INTEGER);
                }
                if (channelEpisode.getEpisode() != null) {
                    uChannelEpisode.setInt(2, channelEpisode.getEpisode().getID());
                } else {
                    uChannelEpisode.setNull(2, java.sql.Types.INTEGER);
                }
                uChannelEpisode.setTimestamp(3, new java.sql.Timestamp(channelEpisode.getDate().getTime()));
                uChannelEpisode.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
                uChannelEpisode.setInt(4, ID);
                uChannelEpisode.executeUpdate();
            } else { // insert
                if (channelEpisode.getChannel() != null) {
                    iChannelEpisode.setInt(1, channelEpisode.getChannel().getID());
                } else {
                    iChannelEpisode.setNull(1, java.sql.Types.INTEGER);
                }
                if (channelEpisode.getEpisode() != null) {
                    iChannelEpisode.setInt(2, channelEpisode.getEpisode().getID());
                } else {
                    iChannelEpisode.setNull(2, java.sql.Types.INTEGER);
                }
                iChannelEpisode.setTimestamp(3, new java.sql.Timestamp(channelEpisode.getDate().getTime()));
                if (iChannelEpisode.executeUpdate() == 1) {
                    rs = iChannelEpisode.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            if (ID > 0) {
                channelEpisode.copyFrom(getChannelEpisode(ID));
            }
            channelEpisode.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dChannelEpisode = "DELETE FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?"
    public void removeChannelEpisode(ChannelEpisode channelEpisode) {
        try {
            if (channelEpisode.getChannel() != null) {
                dChannelEpisode.setInt(1, channelEpisode.getChannel().getID());
            } else {
                dChannelEpisode.setNull(1, java.sql.Types.INTEGER);
            }
            if (channelEpisode.getEpisode() != null) {
                dChannelEpisode.setInt(2, channelEpisode.getEpisode().getID());
            } else {
                dChannelEpisode.setNull(2, java.sql.Types.INTEGER);
            }
            dChannelEpisode.setTimestamp(3, new java.sql.Timestamp(channelEpisode.getDate().getTime()));
            dChannelEpisode.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // UserSeries
    // ============================================================================================
    @Override
    public UserSeries createUserSeries() {
        return new UserSeriesMySQL(this);
    }

    // sUserSeriesByID = "SELECT * FROM r_user_series WHERE ID=?"
    @Override
    public UserSeries getUserSeries(int userSeriesID) {
        UserSeries result = null;
        ResultSet rs = null;
        try {
            sUserSeriesByID.setInt(1, userSeriesID);
            rs = sUserSeriesByID.executeQuery();
            if (rs.next()) {
                result = new UserSeriesMySQL(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserSeriesByUserAndSeries = "SELECT ID FROM r_user_series WHERE ID_user=? AND ID_series=?"
    public UserSeries getUserSeries(User user, Series series) {
        UserSeries result = null;
        ResultSet rs = null;
        try {
            sUserSeriesByUserAndSeries.setInt(1, user.getID());
            sUserSeriesByUserAndSeries.setInt(2, series.getID());
            rs = sUserSeriesByUserAndSeries.executeQuery();
            if (rs.next()) {
                result = getUserSeries(rs.getInt("ID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserSeries = "SELECT ID FROM r_user_series"
    public List<UserSeries> getUserSeries() {
        List<UserSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sUserSeries.executeQuery();
            while (rs.next()) {
                result.add(getUserSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserSeriesBySeries = "SELECT ID FROM r_user_series WHERE ID_series=?"
    public List<UserSeries> getUserSeries(User user) {
        List<UserSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUserSeriesByUser.setInt(1, user.getID());
            rs = sUserSeriesByUser.executeQuery();
            while (rs.next()) {
                result.add(getUserSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sUserSeriesByUser = "SELECT ID FROM r_user_series WHERE ID_user=?"
    public List<UserSeries> getUserSeries(Series series) {
        List<UserSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUserSeriesBySeries.setInt(1, series.getID());
            rs = sUserSeriesBySeries.executeQuery();
            while (rs.next()) {
                result.add(getUserSeries(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // iUserSeries = "INSERT INTO r_user_series (ID_user, ID_series, rating, anticipation_notification, add_date, season, episode) VALUES(?, ?, ?, ?, ?, ?, ?), Statement.RETURN_GENERATED_KEYS"
    public void storeUserSeries(UserSeries userSeries) {
        ResultSet rs = null;
        int ID = userSeries.getID();
        try {
            if (ID > 0) { // update
                if (!userSeries.isDirty()) {
                    return;
                }
                // uUserSeries = "UPDATE r_user_series SET ID_user=?, ID_series=?, rating=?, anticipation_notification=?, add_date=?, season=?, episode=? WHERE ID=?"
                if (userSeries.getUser() != null) {
                    uUserSeries.setInt(1, userSeries.getUser().getID());
                } else {
                    uUserSeries.setNull(1, java.sql.Types.INTEGER);
                }
                if (userSeries.getSeries() != null) {
                    uUserSeries.setInt(2, userSeries.getSeries().getID());
                } else {
                    uUserSeries.setNull(2, java.sql.Types.INTEGER);
                }
                uUserSeries.setString(3, userSeries.getRating());
                if (userSeries.getAnticipationNotification() != null) {
                    uUserSeries.setTime(4, new java.sql.Time(userSeries.getAnticipationNotification().getTime()));
                } else {
                    uUserSeries.setNull(4, java.sql.Types.TIME);
                }
                uUserSeries.setDate(5, new java.sql.Date(userSeries.getAddDate().getTime()));
                uUserSeries.setInt(6, userSeries.getSeason());
                uUserSeries.setInt(7, userSeries.getEpisode());
                uUserSeries.setInt(8, ID);
                uUserSeries.executeUpdate();
            } else { // insert
                if (userSeries.getUser() != null) {
                    iUserSeries.setInt(1, userSeries.getUser().getID());
                } else {
                    iUserSeries.setNull(1, java.sql.Types.INTEGER);
                }
                if (userSeries.getSeries() != null) {
                    iUserSeries.setInt(2, userSeries.getSeries().getID());
                } else {
                    iUserSeries.setNull(2, java.sql.Types.INTEGER);
                }
                iUserSeries.setString(3, userSeries.getRating());
                if (userSeries.getAnticipationNotification() != null) {
                    iUserSeries.setTime(4, new java.sql.Time(userSeries.getAnticipationNotification().getTime()));
                } else {
                    iUserSeries.setNull(4, java.sql.Types.TIME);
                }
                iUserSeries.setDate(5, new java.sql.Date(new Date().getTime()));
                iUserSeries.setInt(6, userSeries.getSeason());
                iUserSeries.setInt(7, userSeries.getEpisode());
                if (iUserSeries.executeUpdate() == 1) {
                    rs = iUserSeries.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            if (ID > 0) {
                userSeries.copyFrom(getUserSeries(ID));
            }
            userSeries.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    // dUserSeries = "DELETE FROM r_user_series WHERE ID_user=? AND ID_series=?"
    public void removeUserSeries(UserSeries userSeries) {
        try {
            if (userSeries.getUser() != null) {
                dUserSeries.setInt(1, userSeries.getUser().getID());
            } else {
                dUserSeries.setNull(1, java.sql.Types.INTEGER);
            }
            if (userSeries.getSeries() != null) {
                dUserSeries.setInt(2, userSeries.getSeries().getID());
            } else {
                dUserSeries.setNull(2, java.sql.Types.INTEGER);
            }
            dUserSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Other
    // ============================================================================================
    @Override
    // sLastEpisodeSeen = "SELECT season, episode FROM r_user_series WHERE  ID_user=? AND ID_series=?"
    public Episode getLastEpisodeSeen(User user, Series series) {
        ResultSet rs = null;
        Episode result = null;
        try {
            sLastEpisodeSeen.setInt(1, user.getID());
            sLastEpisodeSeen.setInt(2, series.getID());
            rs = sLastEpisodeSeen.executeQuery();
            if (rs.next()) {
                result = getEpisodeBySeriesAndSeasonAndNumber(series, rs.getInt("season"), rs.getInt("episode"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    // sSeriesGeneralRating = "SELECT SUM(rating) COUNT(1) FROM r_user_series WHERE ID_series=?"
    public int getSeriesGeneralRating(Series series) {
        int result = -1; // per segnalare l'errore
        ResultSet rs = null;
        try {
            sSeriesGeneralRating.setInt(1, series.getID());
            rs = sSeriesGeneralRating.executeQuery();
            if (rs.next()) {
                if (rs.getInt(2) != 0) {
                    result = rs.getInt(1) / rs.getInt(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    public List<Series> getHintSeries(User user) {

        List<Series> result = new ArrayList();

        List<Series> userSeries = user.getSeries();
        if (userSeries.isEmpty()) { // cerca in generale perché l'utente non ha serie preferite
            // se l'utente non ha serie semplicemente consigliamo quelle più trendy
            result = RESTSortLayer.trendify(getSeries());
        } else { // l'utente ha almeno una serie

            for (User u : getUsers()) { // per tutti gli utenti
                if (u.getSeries().containsAll(userSeries)) { // se l'utente in questione ha tutte le serie dell'utente preso in input
                    result.addAll(u.getSeries());  // aggiungi tutte le sue serie al risultato
                }
            }

            // ora ho una lista di serie da cui togliere quelle dell'utente in input
            result = getDifferentSeries(result, userSeries);
            // ora che ho tolto le serie dell'utente in input devo semplicemente ordinare la lista in base a quante volte
            // compare la serie 

            // se la lista è vuota non andiamo avanti
            if (result.isEmpty()) {
                return result;
            }

            // ordino le serie alfabeticamente
            result = RESTSortLayer.sortSeriesByName(result);

            // lista che conterrà un insieme di liste oneSeriesList
            List<List<Series>> oneListForOneSeries = new ArrayList();
            // lista che la stessa serie ripetuta TOT volte
            List<Series> oneSeriesList = new ArrayList();
            // assegno a oneSeriesList il primo elemento di result
            // che so non essere vuoto
            oneSeriesList.add(result.get(0));
            for (int i = 1; i < result.size(); i++) {
                // se la serie corrente è uguale a quella precedente
                // allora inseriscila nella lista che sta contenendo i doppioni di quella serie
                if (result.get(i).equals(result.get(i - 1))) {
                    oneSeriesList.add(result.get(i));
                } else {
                    // altrimenti aggiungi la oneSeriesList alla lista di liste
                    // crea una nuova lista che conterrà i doppioni di questa nuova serie
                    // ed aggiungici questa nuova serie
                    oneListForOneSeries.add(oneSeriesList);
                    oneSeriesList = new ArrayList();
                    oneSeriesList.add(result.get(i));
                }

            }
            oneListForOneSeries.add(oneSeriesList); // usciti dal ciclo devo aggiungere l'ultima lista

            // non ho più bisogno del contenuto di result e mi serve una
            // lista pulita in cui reinserire le serie
            result.clear();

            // ordiniamo la lista di liste in base alla lunghezza di ogni lista al suo interno
            // cioè sto ordinando in base al numero di occorrenze della serie
            RESTSortLayer.sortByListSize(oneListForOneSeries);

            for (List<Series> sl : oneListForOneSeries) {
                result.add(sl.get(0)); // aggiungo a result una sola serie per ogni lista
            }
        }
        return result;
    }

    private List<Series> getDifferentSeries(List<Series> l1, List<Series> l2) {

        List<Series> result = new ArrayList();

        for (Series s : l1) {
            if (!l2.contains(s)) {
                result.add(s);
            }
        }

        return result;
    }

    // ============================================================================================
    // Private Metods (SAVING THE RELATIONSHIP)
    // ============================================================================================
    // ============================================================================================
    // CommentSeries
    // ============================================================================================
    // iCommentSeries = "INSERT INTO r_comment_series (ID_comment, ID_series) VALUES (?, ?)"
    private void storeCommentSeries(int commentID, int seriesID) {
        try {
            iCommentSeries.setInt(1, commentID);
            iCommentSeries.setInt(2, seriesID);
            iCommentSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // dCommentSeries = "DELETE FROM r_comment_series WHERE ID_comment=? AND ID_Series=?"
    private void removeCommentSeries(int commentID, int seriesID) {
        try {
            dCommentSeries.setInt(1, commentID);
            dCommentSeries.setInt(2, seriesID);
            dCommentSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // GenreSeries
    // ============================================================================================
    // iGenreSeries = "INSERT INTO r_genre_series (ID_genre , ID_series) VALUES (?, ?)"
    private void storeGenreSeries(int genreID, int seriesID) {
        try {
            iGenreSeries.setInt(1, genreID);
            iGenreSeries.setInt(2, seriesID);
            iGenreSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // dGenreSeries = "DELETE FROM r_genre_series WHERE ID_genre=? AND ID_series=?"
    private void removeGenreSeries(int genreID, int seriesID) {
        try {
            dGenreSeries.setInt(1, genreID);
            dGenreSeries.setInt(2, seriesID);
            dGenreSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // NewsComment
    // ============================================================================================
    // iNewsComment = "INSERT INTO r_news_comment (ID_news , ID_comment) VALUES (?, ?)"
    private void storeNewsComment(int newsID, int commentID) {
        try {
            iNewsComment.setInt(1, newsID);
            iNewsComment.setInt(2, commentID);
            iNewsComment.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // dNewsComment = "DELETE FROM r_news_comment WHERE ID_news=? AND ID_comment=?"
    private void removeNewsComment(int newsID, int commentID) {
        try {
            dNewsComment.setInt(1, newsID);
            dNewsComment.setInt(2, commentID);
            dNewsComment.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // NewsSeries
    // ============================================================================================
    // iNewsSeries = "INSERT INTO r_news_series (ID_news , ID_series) VALUES (?, ?)"
    private void storeNewsSeries(int newsID, int seriesID) {
        try {
            iNewsSeries.setInt(1, newsID);
            iNewsSeries.setInt(2, seriesID);
            iNewsSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // dNewsSeries = "DELETE FROM r_news_series WHERE ID_news=? AND ID_series=?"
    private void removeNewsSeries(int newsID, int seriesID) {
        try {
            dNewsSeries.setInt(1, newsID);
            dNewsSeries.setInt(2, seriesID);
            dNewsSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // ServiceGroup
    // ============================================================================================
    // iServiceGroup = "INSERT INTO r_service_group (ID_service , ID_group) VALUES (?, ?)"
    private void storeServiceGroup(int serviceID, int groupID) {
        try {
            iServiceGroup.setInt(1, serviceID);
            iServiceGroup.setInt(2, groupID);
            iServiceGroup.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // dServiceGroup  = "DELETE FROM r_service_group WHERE ID_service=? AND ID_group=?"
    private void removeServiceGroup(int serviceID, int groupID) {
        try {
            dServiceGroup.setInt(1, serviceID);
            dServiceGroup.setInt(2, groupID);
            dServiceGroup.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // UserGenre
    // ============================================================================================
    // iUserGenre = "INSERT INTO r_user_genre (ID_user , ID_genre) VALUES (?, ?)"
    private void storeUserGenre(int userID, int genreID) {
        try {
            iUserGenre.setInt(1, userID);
            iUserGenre.setInt(2, genreID);
            iUserGenre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // dUserGenre  = "DELETE FROM r_user_genre WHERE ID_user=? AND ID_genre=?"
    private void removeUserGenre(int userID, int genreID) {
        try {
            dUserGenre.setInt(1, userID);
            dUserGenre.setInt(2, genreID);
            dUserGenre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
