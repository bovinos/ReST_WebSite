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
            dCastMember;
    private PreparedStatement sChannelByID, sChannels, sChannelsByEpisode, sChannelsByType,
            iChannel,
            dChannel;
    private PreparedStatement sCommentByID, sComments, sCommentsBySeries,
            sCommentsByNews, sCommentsByUser,
            iComment,
            dComment;
    private PreparedStatement sEpisodeByID, sEpisodeBySeriesAndSeasonAndNumber,
            sEpisodes, sEpisodesBySeries, sEpisodesBySeriesAndSeason, sEpisodesByChannel,
            iEpisode,
            dEpisode;
    private PreparedStatement sGenreByID, sGenres, sGenresBySeries, sGenresByUser,
            iGenre,
            dGenre;
    private PreparedStatement sGroupByID, sGroupByUser, sGroups, sGroupsByService,
            iGroup,
            dGroup;
    private PreparedStatement sMessageByID, sMessages, sMessagesByUser, sMessagesBySeries,
            sMessagesByUserAndSeries,
            iMessage,
            dMessage;
    private PreparedStatement sNewsByID, sNewsByComment, sNews, sNewsByUser, sNewsbySeries,
            iNews,
            dNews;
    private PreparedStatement sSeriesByID, sSeriesByMessage, sSeriesByComment, sSeriesByEpisode,
            sSeries, sSeriesByNews, sSeriesByGenre, sSeriesByCastMember,
            sSeriesByCastMemberAndRole, sSeriesByUser,
            iSeries,
            dSeries;
    private PreparedStatement sServiceByID, sServices, sServicesByGroup,
            iService,
            dService;
    private PreparedStatement sUserByID, sUserByUsernameAndPassword, sUserByComment, sUserByMessage, sUserByNews,
            sUsers, sUsersBySeries, sUsersByGenre, sUsersByGroup,
            iUser,
            dUser;

    // Relationship
    private PreparedStatement sCastMemberSeries, sCastMemberSeriesByCastMember, sCastMemberSeriesBySeries, sCastMemberSeriesByCastMemberAndSeries,
            iCastMemberSeries,
            dCastMemberSeries;
    private PreparedStatement sChannelEpisode, sChannelEpisodeByChannel, sChannelEpisodeByEpisode, sChannelEpisodeByChannelAndEpisode,
            iChannelEpisode,
            dChannelEpisode;
    private PreparedStatement sUserSeries, sUserSeriesByUser, sUserSeriesBySeries,
            iUserSeries,
            dUserSeries;

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
            dCastMember = connection.prepareStatement("DELETE FROM e_cast_member WHERE ID=?");

            // Channel
            sChannelByID = connection.prepareStatement("SELECT * FROM e_channel WHERE ID=?");
            sChannels = connection.prepareStatement("SELECT ID FROM e_channel");
            sChannelsByEpisode = connection.prepareStatement("SELECT ID_channel FROM r_channel_episode WHERE ID_episode=?");
            sChannelsByType = connection.prepareStatement("SELECT ID FROM e_channel WHERE type=?");
            iChannel = connection.prepareStatement("INSERT INTO e_channel (name, number, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dChannel = connection.prepareStatement("DELETE FROM e_channel WHERE ID=?");

            // Comment
            sCommentByID = connection.prepareStatement("SELECT * FROM e_comment WHERE ID=?");
            sComments = connection.prepareStatement("SELECT ID FROM e_comment");
            sCommentsBySeries = connection.prepareStatement("SELECT ID_comment FROM r_comment_series WHERE ID_series=?");
            sCommentsByNews = connection.prepareStatement("SELECT ID_comment FROM r_news_comment WHERE ID_news=?");
            sCommentsByUser = connection.prepareStatement("SELECT ID_comment FROM e_comment WHERE ID_user=?");
            iComment = connection.prepareStatement("INSERT INTO e_comment (title, text, date, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dComment = connection.prepareStatement("DELETE FROM e_comment WHERE ID=?");

            // Episode
            sEpisodeByID = connection.prepareStatement("SELECT * FROM e_episode WHERE ID=?");
            sEpisodeBySeriesAndSeasonAndNumber = connection.prepareStatement("SELECT * FROM e_episode WHERE ID_series=? AND season=? AND number=?");
            sEpisodes = connection.prepareStatement("SELECT ID FROM e_episode");
            sEpisodesBySeries = connection.prepareStatement("SELECT ID FROM e_episode WHERE ID_series=?");
            sEpisodesBySeriesAndSeason = connection.prepareStatement("SELECT ID FROM e_episode WHERE ID_series=? AND season=?");
            sEpisodesByChannel = connection.prepareStatement("SELECT ID_episode FROM r_channel_episode WHERE ID_episode=?");
            iEpisode = connection.prepareStatement("INSERT INTO e_episode (number, season, title, description, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dEpisode = connection.prepareStatement("DELETE FROM e_episode WHERE ID=?");

            //Genre
            sGenreByID = connection.prepareStatement("SELECT * FROM e_genre WHERE ID=?");
            sGenres = connection.prepareStatement("SELECT ID FROM e_genre");
            sGenresBySeries = connection.prepareStatement("SELECT ID_genre FROM r_genre_series WHERE ID_series=?");
            sGenresByUser = connection.prepareStatement("SELECT ID_genre FROM r_user_genre WHERE ID_user=?");
            iGenre = connection.prepareStatement("INSERT INTO e_genre (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            dGenre = connection.prepareStatement("DELETE FROM e_genre WHERE ID=?");

            // Group
            sGroupByID = connection.prepareStatement("SELECT * FROM e_group WHERE ID=?");
            sGroupByUser = connection.prepareStatement("SELECT ID_group FROM e_user WHERE ID=?");
            sGroups = connection.prepareStatement("SELECT ID FROM e_groups");
            sGroupsByService = connection.prepareStatement("SELECT ID_group FROM r_service_group WHERE ID_service=?");
            iGroup = connection.prepareStatement("INSERT INTO e_group (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            dGroup = connection.prepareStatement("DELETE FROM e_group WHERE ID=?");

            // Message
            sMessageByID = connection.prepareStatement("SELECT * FROM e_message WHERE ID=?");
            sMessages = connection.prepareStatement("SELECT ID FROM e_message");
            sMessagesByUser = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=?");
            sMessagesBySeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_series=?");
            sMessagesByUserAndSeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=? AND ID_series=?"); //redoundant query
            iMessage = connection.prepareStatement("INSERT INTO e_message (title, text, date, ID_user, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dMessage = connection.prepareStatement("DELETE FROM e_message WHERE ID=?");

            // News
            sNewsByID = connection.prepareStatement("SELECT * FROM e_news WHERE ID=?");
            sNewsByComment = connection.prepareStatement("SELECT ID_news FROM r_news_comment WHERE ID_comment=?");
            sNews = connection.prepareStatement("SELECT ID FROM e_news");
            sNewsByUser = connection.prepareStatement("SELECT ID FROM e_news WHERE ID_user=?");
            sNewsbySeries = connection.prepareStatement("SELECT ID_news FROM r_news_series WHERE ID_series=?");
            iNews = connection.prepareStatement("INSERT INTO e_news (title, text, date, image_URL, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            dSeries = connection.prepareStatement("DELETE FROM e_series WHRE ID=?");

            // Service
            sServiceByID = connection.prepareStatement("SELECT * FROM e_service WHERE ID=?");
            sServices = connection.prepareStatement("SELECT ID FROM e_service");
            sServicesByGroup = connection.prepareStatement("SELECT ID_service FROM r_service_group WHERE ID_group=?");
            iService = connection.prepareStatement("INSERT INTO e_service (name, description, script_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            dUser = connection.prepareStatement("DELETE FROM e_user WHERE ID=?");

            // Relationship
            // CastMemberSeries
            sCastMemberSeries = connection.prepareStatement("SELECT * FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?");
            sCastMemberSeriesByCastMember = connection.prepareStatement("SELECT * FROM r_cast_member_series WHERE ID_cast_member=?");
            sCastMemberSeriesBySeries = connection.prepareStatement("SELECT * FROM r_cast_member_series WHERE ID_series=?");
            sCastMemberSeriesByCastMemberAndSeries = connection.prepareStatement("SELECT * FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=?");
            iCastMemberSeries = connection.prepareStatement("INSERT INTO r_cast_member_series (ID_cast_member, ID_series, role) VALUES(?, ?, ?)");
            dCastMemberSeries = connection.prepareCall("DELETE FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?");

            // ChannelEpisode
            sChannelEpisode = connection.prepareStatement("SELECT * FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?");
            sChannelEpisodeByChannel = connection.prepareStatement("SELECT * FROM r_channel_episode WHERE ID_channel=?");
            sChannelEpisodeByEpisode = connection.prepareStatement("SELECT * FROM r_channel_episode WHERE ID_episode=?");
            sChannelEpisodeByChannelAndEpisode = connection.prepareStatement("SELECT * FROM r_channel_episode WHERE ID_channel=? AND ID_episode=?");
            iChannelEpisode = connection.prepareStatement("INSERT INTO r_channel_episode (ID_channel, ID_episode, date) VALUES(?, ?, ?)");
            dChannelEpisode = connection.prepareStatement("DELETE FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?");

            // UserSeries
            sUserSeries = connection.prepareStatement("SELECT * FROM r_user_series WHERE ID_user=? AND ID_series=?");
            sUserSeriesBySeries = connection.prepareStatement("SELECT * FROM r_user_series WHERE ID_series=?");
            sUserSeriesByUser = connection.prepareStatement("SELECT * FROM r_user_series WHERE ID_user=?");
            iUserSeries = connection.prepareStatement("INSERT INTO r_user_series (ID_user, ID_series, rating, anticipation_notification, add_date, season, episode) VALUES(?, ?, ?, ?, ?, ?, ?)");
            dUserSeries = connection.prepareStatement("DELETE FROM r_user_series WHERE ID_user=? AND ID_series=?");

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
                iCastMember.setString(1, castMember.getName());
                iCastMember.setString(2, castMember.getSurname());
                iCastMember.setDate(3, new java.sql.Date(castMember.getBirthDate().getTime()));
                iCastMember.setString(4, castMember.getGender());
                iCastMember.setString(5, castMember.getCountry());
                iCastMember.setString(6, castMember.getImageURL());
                iCastMember.executeUpdate();
            } else { // Insert
                iCastMember.setString(1, castMember.getName());
                iCastMember.setString(2, castMember.getSurname());
                iCastMember.setDate(3, null);
                iCastMember.setString(4, castMember.getGender());
                iCastMember.setString(5, castMember.getCountry());
                iCastMember.setString(6, castMember.getImageURL());
                if (iCastMember.executeUpdate() == 1) { // query successful
                    rs = iCastMember.getGeneratedKeys(); // to get the key of record inserted
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // now we store the relatoinship only if they are not null
            if (castMember.isSeriesSet()) {
                storeCastMemberSeriesRelationship(ID, castMember.getSeries());
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

    private void storeCastMemberSeriesRelationship(int ID, List<Series> series) {
        // iCastMemberSeries = "INSERT INTO r_cast_member_series (ID_cast_member, ID_series, role) VALUES(?, ?, ?)"
        for (Series s : series) {
            try {
                if (s.getID() > 0) { // series is on DB already
                    iCastMemberSeries.setInt(1, ID);
                    iCastMemberSeries.setInt(2, s.getID());
                    iCastMemberSeries.setString(3, null); // role?
                    iCastMemberSeries.executeUpdate();
                } else { //what are we doing if the series isn't on DB?

                }
            } catch (SQLException ex) {
                Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
            }

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
                iChannel.setString(1, channel.getName());
                iChannel.setInt(2, channel.getNumber());
                iChannel.setString(3, channel.getType());
                iChannel.executeUpdate();
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
                iComment.setString(1, comment.getTitle());
                iComment.setString(2, comment.getText());
                iComment.setDate(3, new java.sql.Date(comment.getDate().getTime()));
                iComment.setInt(4, comment.getLikes());
                iComment.setInt(5, comment.getDislikes());
                if (comment.getUser() != null) {
                    iComment.setInt(6, comment.getUser().getID());
                } else {
                    iComment.setInt(6, 0);
                }
                iComment.executeUpdate();
            } else { // Insert
                iComment.setString(1, comment.getTitle());
                iComment.setString(2, comment.getText());
                iComment.setDate(3, null);
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
                iEpisode.setInt(1, episode.getNumber());
                iEpisode.setInt(2, episode.getSeason());
                iEpisode.setString(3, episode.getTitle());
                iEpisode.setString(4, episode.getDescription());
                if (episode.getSeries() != null) {
                    iEpisode.setInt(5, episode.getSeries().getID());
                } else {
                    iEpisode.setInt(5, 0);
                }
                iEpisode.executeUpdate();
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
                iGenre.setString(1, genre.getName());
                iGenre.executeUpdate();
            } else { // Insert
                iGenre.setString(1, genre.getName());
                if (iGenre.executeUpdate() == 1) {
                    rs = iGenre.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
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
                iGroup.setString(1, group.getName());
                iGroup.setString(2, group.getDescription());
                iGroup.executeUpdate();
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
                iMessage.setString(1, message.getTitle());
                iMessage.setString(2, message.getText());
                iMessage.setDate(3, new java.sql.Date(message.getDate().getTime()));
                iMessage.setInt(4, message.getUser().getID());
                iMessage.setInt(5, message.getSeries().getID());
                iMessage.executeUpdate();
            } else { // Insert
                iMessage.setString(1, message.getTitle());
                iMessage.setString(2, message.getText());
                iMessage.setDate(3, null);
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
                iMessage.executeUpdate();
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
                iNews.setString(1, news.getTitle());
                iNews.setString(2, news.getText());
                iNews.setDate(3, new java.sql.Date(news.getDate().getTime()));
                iNews.setString(4, news.getImageURL());
                iNews.setInt(5, news.getLikes());
                iNews.setInt(6, news.getDislikes());
                if (news.getUser() != null) {
                    iNews.setInt(7, news.getUser().getID());
                } else {
                    iNews.setInt(7, 0);
                }
                iNews.executeUpdate();
            } else { // Insert
                iNews.setString(1, news.getTitle());
                iNews.setString(2, news.getText());
                iNews.setDate(3, null);
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
            dNews.executeLargeUpdate();
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
                iSeries.setString(1, series.getName());
                iSeries.setInt(2, series.getYear());
                iSeries.setString(3, series.getDescription());
                iSeries.setString(4, series.getImageURL());
                iSeries.setString(5, series.getState());
                iSeries.setInt(6, series.getAddCount());
                iSeries.executeUpdate();
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
    // dSeries = "DELETE FROM e_series WHRE ID=?"
    public void removeSeries(Series series) {
        try {
            dSeries.setInt(1, series.getID());
            iSeries.executeUpdate();
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
                iService.setString(1, service.getName());
                iService.setString(2, service.getDescription());
                iService.setString(3, service.getScriptName());
                iService.executeUpdate();
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
                iUser.setString(1, user.getUsername());
                iUser.setString(2, user.getPassword());
                iUser.setString(3, user.getMail());
                iUser.setString(4, user.getName());
                iUser.setString(5, user.getSurname());
                iUser.setInt(6, user.getAge());
                iUser.setString(7, user.getGender());
                iUser.setString(8, user.getImageURL());
                iUser.setBoolean(9, user.getNotificationStatus());
                if (user.getGroup() != null) {
                    iUser.setInt(10, user.getGroup().getID());
                } else {
                    iUser.setNull(10, java.sql.Types.INTEGER);
                }
                iUser.executeUpdate();
            } else { // Insert
                iUser.setString(1, user.getUsername());
                iUser.setString(2, user.getPassword());
                iUser.setString(3, user.getMail());
                iUser.setString(4, user.getName());
                iUser.setString(5, user.getSurname());
                iUser.setInt(6, user.getAge());
                iUser.setString(7, user.getGender());
                iUser.setString(8, user.getImageURL());
                iUser.setBoolean(9, user.getNotificationStatus());
                if (user.getGroup() != null) {
                    iUser.setInt(10, user.getGroup().getID());
                } else {
                    iUser.setNull(10, java.sql.Types.INTEGER);
                }
                if (iUser.executeUpdate() == 1) {
                    rs = iUser.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
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
    // CAST MEMBER SERIES
    // ============================================================================================
    @Override
    public CastMemberSeries createCastMemberSeries() {
        return new CastMemberSeriesMySQL(this);
    }

    @Override
    // sCastMemberSeries = "SELECT * FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?"
    /**
     * <Questo metodo ha un problema: se dopo il salvataggio il DB mi tronca il
     * ruolo, non avrò più modo di riprendere la riga appena inserita>
     */
    public CastMemberSeries getCastMemberSeries(CastMember castMember, Series series, String role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // sCastMemberSeriesByCastMember = "SELECT * FROM r_cast_member_series WHERE ID_cast_member=?"
    public List<CastMemberSeries> getCastMemberSeriesByCastMember(CastMember castMember) {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sCastMemberSeriesByCastMember.setInt(1, castMember.getID());
            rs = sCastMemberSeriesByCastMember.executeQuery();
            while (rs.next()) {
                result.add(new CastMemberSeriesMySQL(this, rs));
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
    // sCastMemberSeriesBySeries = "SELECT * FROM r_cast_member_series WHERE ID_series=?"
    public List<CastMemberSeries> getCastMemberSeriesBySeries(Series series) {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sCastMemberSeriesBySeries.setInt(1, series.getID());
            rs = sCastMemberSeriesBySeries.executeQuery();
            while (rs.next()) {
                result.add(new CastMemberSeriesMySQL(this, rs));
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
    // sCastMemberSeriesByCastMemberAndSeries = "SELECT * FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=?"
    public List<CastMemberSeries> getCastMembeSeriesByCastMemberAndSeries(CastMember castMember, Series series) {
        List<CastMemberSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sCastMemberSeriesByCastMemberAndSeries.setInt(1, castMember.getID());
            sCastMemberSeriesByCastMemberAndSeries.setInt(2, series.getID());
            rs = sCastMemberSeriesByCastMemberAndSeries.executeQuery();
            while (rs.next()) {
                result.add(new CastMemberSeriesMySQL(this, rs));
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
    // iCastMemberSeries = "INSERT INTO r_cast_member_series (ID_cast_member, ID_series, role) VALUES(?, ?, ?)"
    public void storeCastMemberSeries(CastMemberSeries castMemberSeries) {
        if (!castMemberSeries.isDirty()) {
            return;
        }
        try {
            iCastMemberSeries.setInt(1, castMemberSeries.getCastMemberID());
            iCastMemberSeries.setInt(2, castMemberSeries.getSeriesID());
            iCastMemberSeries.setString(3, castMemberSeries.getRole());
            iCastMemberSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    // dCastMemberSeries = "DELETE FROM r_cast_member_series WHERE ID_cast_member=? AND ID_series=? AND role=?"
    public void removeCastMemberSeries(CastMemberSeries castMemberSeries) {
        try {
            dCastMemberSeries.setInt(1, castMemberSeries.getCastMemberID());
            dCastMemberSeries.setInt(2, castMemberSeries.getSeriesID());
            dCastMemberSeries.setString(3, castMemberSeries.getRole());
            dCastMemberSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // CHANNEL EPISODE
    // ============================================================================================
    @Override
    public ChannelEpisode createChannelEpisode() {
        return new ChannelEpisodeMySQL(this);
    }

    @Override
    // sChannelEpisode = "SELECT * FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?"
    /**
     * <Questo metodo ha un problema: se dopo il salvataggio il DB mi tronca la
     * data, non avrò più modo di riprendere la riga appena inserita>
     */
    public ChannelEpisode getChannelEpisode(Channel channel, Episode episode, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // sChannelEpisodeByChannel = "SELECT * FROM r_channel_episode WHERE ID_channel=?"
    public List<ChannelEpisode> getChannelEpisodeByChannel(Channel channel) {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChannelEpisodeByChannel.setInt(1, channel.getID());
            rs = sChannelEpisodeByChannel.executeQuery();
            while (rs.next()) {
                result.add(new ChannelEpisodeMySQL(this, rs));
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
    // sChannelEpisodeByEpisode  = "SELECT * FROM r_channel_episode WHERE ID_episode=?"
    public List<ChannelEpisode> getChannelEpisodeByEpisode(Episode episode) {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChannelEpisodeByEpisode.setInt(1, episode.getID());
            rs = sChannelEpisodeByEpisode.executeQuery();
            while (rs.next()) {
                result.add(new ChannelEpisodeMySQL(this, rs));
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
    // sChannelEpisodeByChannelAndEpisode = "SELECT * FROM r_channel_episode WHERE ID_channel=? AND ID_episode=?"
    public List<ChannelEpisode> getChannelEpisodeByChannelAndEpisode(Channel channel, Episode episode) {
        List<ChannelEpisode> result = new ArrayList();
        ResultSet rs = null;
        try {
            sChannelEpisodeByChannelAndEpisode.setInt(1, channel.getID());
            sChannelEpisodeByChannelAndEpisode.setInt(2, episode.getID());
            rs = sChannelEpisodeByChannelAndEpisode.executeQuery();
            while (rs.next()) {
                result.add(new ChannelEpisodeMySQL(this, rs));
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
    // iChannelEpisode = "INSERT INTO r_channel_episode (ID_channel, ID_episode, date) VALUES(?, ?, ?)"
    public void storeChannelEpisode(ChannelEpisode channelEpisode) {
        if (!channelEpisode.isDirty()) {
            return;
        }
        try {
            iChannelEpisode.setInt(1, channelEpisode.getChannelID());
            iChannelEpisode.setInt(2, channelEpisode.getEpisodeID());
            iChannelEpisode.setTimestamp(3, new java.sql.Timestamp(channelEpisode.getDate().getTime()));
            iChannelEpisode.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    // dChannelEpisode = "DELETE FROM r_channel_episode WHERE ID_channel=? AND ID_episode=? AND date=?"
    public void removeChannelEpisode(ChannelEpisode channelEpisode) {
        try {
            dChannelEpisode.setInt(1, channelEpisode.getChannelID());
            dChannelEpisode.setInt(2, channelEpisode.getEpisodeID());
            dChannelEpisode.setTimestamp(3, new java.sql.Timestamp(channelEpisode.getDate().getTime()));
            dChannelEpisode.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // USER SERIES
    // ============================================================================================
    @Override
    public UserSeries createUserSeries() {
        return new UserSeriesMySQL(this);
    }

    @Override
    // sUserSeries = "SELECT * FROM r_user_series WHERE ID_user=? AND ID_series=?"
    public UserSeries getUserSeries(User user, Series series) {
        UserSeries result = null;
        ResultSet rs = null;
        try {
            sUserSeries.setInt(1, user.getID());
            sUserSeries.setInt(2, series.getID());
            rs = sUserSeries.executeQuery();
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
    // sUserSeriesBySeries = "SELECT * FROM r_user_series WHERE ID_series=?"
    public List<UserSeries> getUserSeriesByUser(User user) {
        List<UserSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUserSeriesByUser.setInt(1, user.getID());
            rs = sUserSeriesByUser.executeQuery();
            while (rs.next()) {
                result.add(new UserSeriesMySQL(this, rs));
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
    // sUserSeriesByUser = "SELECT * FROM r_user_series WHERE ID_user=?"
    public List<UserSeries> getUserSeriesBySeries(Series series) {
        List<UserSeries> result = new ArrayList();
        ResultSet rs = null;
        try {
            sUserSeriesBySeries.setInt(1, series.getID());
            rs = sUserSeriesBySeries.executeQuery();
            while (rs.next()) {
                result.add(new UserSeriesMySQL(this, rs));
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
    // iUserSeries = "INSERT INTO r_user_series (ID_user, ID_series, rating, anticipation_notification, add_date, season, episode) VALUES(?, ?, ?, ?, ?, ?, ?)"
    public void storeUserSeries(UserSeries userSeries) {
        try {
            iUserSeries.setInt(1, userSeries.getUserID());
            iUserSeries.setInt(2, userSeries.getSeriesID());
            iUserSeries.setString(3, userSeries.getRating());
            iUserSeries.setTime(4, new java.sql.Time(userSeries.getAnticipationNotification().getTime()));
            iUserSeries.setDate(5, new java.sql.Date(userSeries.getAddDate().getTime()));
            iUserSeries.setInt(6, userSeries.getSeason());
            iUserSeries.setInt(7, userSeries.getEpisode());
            iUserSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    // dUserSeries = "DELETE FROM r_user_series WHERE ID_user=? AND ID_series=?"
    public void removeUserSeries(UserSeries userSeries) {
        try {
            dUserSeries.setInt(1, userSeries.getUserID());
            dUserSeries.setInt(2, userSeries.getSeriesID());
            dUserSeries.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // OTHER
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

}
