package it.mam.REST.data.impl;

import it.mam.REST.data.model.Cast;
import it.mam.REST.data.model.Channel;
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
import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.data.DataLayerMysqlImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

    private PreparedStatement sCastMemberByID, sCast, sCastBySeries, sCastBySeriesAndRole,
            iCastMember,
            dCastMember;
    private PreparedStatement sChannelByID, sChannels, sChannelsBySeries, sChannelsByType,
            iChannel,
            dChannel;
    private PreparedStatement sCommentByID, sComments, sCommentsBySeries,
            sCommentsByNews, sCommentsByUser,
            iComment,
            dComment;
    private PreparedStatement sEpisodeByID, sEpisodeBySeriesAndSeasonAndNumber,
            sEpisodes, sEpisodesBySeries, sEpisodesBySeriesAndSeason,
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
            sSeries, sSeriesByNews, sSeriesByGenre, sSeriesByChannel, sSeriesByCastMember,
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
    private PreparedStatement iCastSeriesRel;
    private PreparedStatement sLastEpisodeSeen;

    public RESTDataLayerMySQL(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        try {
            super.init();

            // Cast
            sCastMemberByID = connection.prepareStatement("SELECT * FROM e_cast WHERE ID=?");
            sCast = connection.prepareStatement("SELECT ID FROM e_cast");
            sCastBySeries = connection.prepareStatement("SELECT ID_cast FROM r_cast_series WHERE ID_series=?");
            sCastBySeriesAndRole = connection.prepareStatement("SELECT ID_cast FROM r_cast_series WHERE ID_series=? AND role=?");
            iCastMember = connection.prepareStatement("INSERT INTO e_cast (name, surname, birth_date, gender, country, image_URL) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dCastMember = connection.prepareStatement("DELETE FROM e_cast WHERE ID=?");

            // Channel
            sChannelByID = connection.prepareStatement("SELECT * FROM e_channel WHERE ID=?");
            sChannels = connection.prepareStatement("SELECT ID FROM e_channel");
            sChannelsBySeries = connection.prepareStatement("SELECT ID_channel FROM r_channel_series WHERE ID_series=?");
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
            sMessageByID = connection.prepareStatement("SELECT FROM e_message WHERE ID=?");
            sMessages = connection.prepareStatement("SELECT ID FROM e_message");
            sMessagesByUser = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=?");
            sMessagesBySeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_series=?");
            sMessagesByUserAndSeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=? AND ID_series=?"); //redoundant query
            iMessage = connection.prepareStatement("INSERT INTO e_message (title, text, date, ID_user, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dMessage = connection.prepareStatement("DELETE FROM e_message WHERE ID=?");

            // News
            sNewsByID = connection.prepareStatement("SELECT * FROM e_news");
            sNewsByComment = connection.prepareStatement("SELECT ID_news FROM r_news_comment WHERE ID_comment=?");
            sNews = connection.prepareStatement("SELECT ID FROM e_news");
            sNewsByUser = connection.prepareStatement("SELECT ID FROM e_news WHERE ID_user=?");
            sNewsbySeries = connection.prepareStatement("SELECT ID_news FROM r_news_series WHERE ID_series=?");
            iNews = connection.prepareStatement("INSERT INTO e_news (title, text, date, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dNews = connection.prepareStatement("DELETE FROM e_news WHERE ID=?");

            // Series
            sSeriesByID = connection.prepareStatement("SELECT * FROM e_series WHERE ID=?");
            sSeriesByMessage = connection.prepareStatement("SELECT ID_series FROM e_message WHERE ID=?");
            sSeriesByComment = connection.prepareStatement("SELECT ID_series FROM r_comment_series WHERE ID_comment=?");
            sSeriesByEpisode = connection.prepareStatement("SELECT ID_series FROM e_episode WHERE ID=?");
            sSeries = connection.prepareStatement("SELECT ID FROM e_series");
            sSeriesByNews = connection.prepareStatement("SELECT ID_series FROM r_news_series WHERE ID_news=?");
            sSeriesByGenre = connection.prepareStatement("SELECT ID_series FROM r_genre_series WHERE ID_genre=?");
            sSeriesByChannel = connection.prepareStatement("SELECT ID_series FROM r_channel_series WHERE ID_channel=?");
            sSeriesByCastMember = connection.prepareStatement("SELECT ID_series FROM r_cast_series WHERE ID_cast=?");
            sSeriesByCastMemberAndRole = connection.prepareStatement("SELECT ID_series FROM r_cast_series WHERE ID_cast=? AND role=?");
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
            iUser = connection.prepareStatement("INSERT INTO e_user (username, password, mail, name, surname, age, gender, image_URL, personal_message, ID_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dUser = connection.prepareStatement("DELETE FROM e_user WHERE ID=?");

            // Relationship
            iCastSeriesRel = connection.prepareStatement("INSERT INTO r_cast_series (ID_cast, ID_series, role) VALUES(?, ?, ?)"); // role?

            // Other
            sLastEpisodeSeen = connection.prepareStatement("SELECT season, episode FROM r_user_series WHERE  ID_user=? AND ID_series=?");

        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ============================================================================================
    // Cast
    // ============================================================================================
    @Override
    public Cast createCastMember() {
        return new CastMySQL(this);
    }

    @Override
    // sCastMemberByID = connection.prepareStatement("SELECT * FROM e_cast WHERE ID=?");
    public Cast getCastMember(int castID) {
        ResultSet rs = null;
        Cast result = null;
        try {
            this.sCastMemberByID.setInt(1, castID);
            rs = this.sCastMemberByID.executeQuery();
            if (rs.next()) {
                result = new CastMySQL(this, rs);
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
    // sCast = connection.prepareStatement("SELECT ID FROM e_cast"); 
    public List<Cast> getCast() {
        ResultSet rs = null;
        List<Cast> result = new ArrayList();
        try {
            rs = sCast.executeQuery();
            while (rs.next()) {
                result.add(this.getCastMember(rs.getInt("ID")));
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
    // sCastBySeries = connection.prepareStatement("SELECT ID_cast FROM r_cast_series WHERE ID_series=?");
    public List<Cast> getCast(Series series) {
        ResultSet rs = null;
        List<Cast> result = new ArrayList();
        try {
            this.sCastBySeries.setInt(1, series.getID());
            rs = this.sCastBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getCastMember(rs.getInt("ID_cast")));
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
    // sCastBySeriesAndRole = connection.prepareStatement("SELECT ID_cast FROM r_cast_series WHERE ID_series=? AND role=?");
    public List<Cast> getCast(Series series, String role) {
        ResultSet rs = null;
        List<Cast> result = new ArrayList();
        try {
            this.sCastBySeriesAndRole.setInt(1, series.getID());
            this.sCastBySeriesAndRole.setString(2, role);
            rs = this.sCastBySeriesAndRole.executeQuery();
            while (rs.next()) {
                result.add(this.getCastMember(rs.getInt("ID_cast")));
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
    // iCastMember = connection.prepareStatement("INSERT INTO e_cast (name, surname, birth_date, gender, country, image_URL) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeCastMember(Cast castMember) {
        ResultSet rs = null;
        int ID = castMember.getID();
        try {
            if (ID > 0) { // Update
                if (!castMember.isDirty()) { // if not modified do nothing
                    return;
                }
                // else update on DB
                this.iCastMember.setString(1, castMember.getName());
                this.iCastMember.setString(2, castMember.getSurname());
                this.iCastMember.setDate(3, new java.sql.Date(castMember.getBirthDate().getTime()));
                this.iCastMember.setString(4, castMember.getGender());
                this.iCastMember.setString(5, castMember.getCountry());
                this.iCastMember.setString(6, castMember.getImageURL());
                this.iCastMember.executeUpdate();
            } else { // Insert
                this.iCastMember.setString(1, castMember.getName());
                this.iCastMember.setString(2, castMember.getSurname());
                this.iCastMember.setDate(3, null);
                this.iCastMember.setString(4, castMember.getGender());
                this.iCastMember.setString(5, castMember.getCountry());
                this.iCastMember.setString(6, castMember.getImageURL());
                if (this.iCastMember.executeUpdate() == 1) { // query successful
                    rs = this.iCastMember.getGeneratedKeys(); // to get the key of record inserted
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            // now we store the relatoinship only if they are not null
            if (castMember.isSeriesSet()) {
                this.storeCastMemberToSeriesRelationship(ID, castMember.getSeries());
            }
            if (ID > 0) { // the object is on DB and have a key
                castMember.copyFrom(this.getCastMember(ID)); // copy the DB object on the current object
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
    // dCastMember = connection.prepareStatement("DELETE FROM e_cast WHERE ID=?");
    public void removeCastMember(Cast castMember) {
        try {
            this.dCastMember.setInt(1, castMember.getID());
            this.dCastMember.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void storeCastMemberToSeriesRelationship(int ID, List<Series> series) {
        for (Series s : series) {
            try {
                if (s.getID() > 0) { // series is on DB already
                    this.iCastSeriesRel.setInt(1, ID);
                    this.iCastSeriesRel.setInt(2, s.getID());
                    this.iCastSeriesRel.setString(3, null); // role?
                    this.iCastSeriesRel.executeUpdate();
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
    // sChannelByID = connection.prepareStatement("SELECT * FROM e_channel WHERE ID=?");
    public Channel getChannel(int channelID) {
        ResultSet rs = null;
        Channel result = null;
        try {
            this.sChannelByID.setInt(1, channelID);
            rs = this.sChannelByID.executeQuery();
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
    // sChannels = connection.prepareStatement("SELECT ID FROM e_channel");
    public List<Channel> getChannels() {
        ResultSet rs = null;
        List<Channel> result = new ArrayList();
        try {
            rs = this.sChannels.executeQuery();
            while (rs.next()) {
                result.add(this.getChannel(rs.getInt("ID")));
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
    // sChannelsBySeries = connection.prepareStatement("SELECT ID_channel FROM r_channel_series WHERE ID_series=?");
    public List<Channel> getChannels(Series series) {
        ResultSet rs = null;
        List<Channel> result = new ArrayList();
        try {
            this.sChannelsBySeries.setInt(1, series.getID());
            rs = this.sChannelsBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getChannel(rs.getInt("ID_channel")));
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
    // sChannelsByType = connection.prepareStatement("SELECT ID FROM e_channel WHERE type=?");
    public List<Channel> getChannels(String type) {
        ResultSet rs = null;
        List<Channel> result = new ArrayList();
        try {
            this.sChannelsByType.setString(1, type);
            rs = this.sChannelsByType.executeQuery();
            while (rs.next()) {
                result.add(this.getChannel(rs.getInt("ID")));
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
    // iChannel = connection.prepareStatement("INSERT INTO e_channel (name, number, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeChannel(Channel channel) {
        ResultSet rs = null;
        int ID = channel.getID();
        try {
            if (ID > 0) {
                // Update
                if (!channel.isDirty()) {
                    return;
                }
                this.iChannel.setString(1, channel.getName());
                this.iChannel.setInt(2, channel.getNumber());
                this.iChannel.setString(3, channel.getType());
                this.iChannel.executeUpdate();
            } else { // Insert 
                this.iChannel.setString(1, channel.getName());
                this.iChannel.setInt(2, channel.getNumber());
                this.iChannel.setString(3, channel.getType());
                if (this.iChannel.executeUpdate() == 1) {
                    rs = this.iChannel.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            if (ID > 0) {
                channel.copyFrom(this.getChannel(ID));
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
    //  dChannel = connection.prepareStatement("DELETE FROM e_channel WHERE ID=?");
    public void removeChannel(Channel channel) {
        try {
            this.dChannel.setInt(1, channel.getID());
            this.dChannel.executeUpdate();
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
    // sCommentByID = connection.prepareStatement("SELECT * FROM e_comment WHERE ID=?");
    public Comment getComment(int commentID) {
        ResultSet rs = null;
        Comment result = null;
        try {
            this.sCommentByID.setInt(1, commentID);
            rs = this.sCommentByID.executeQuery();
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
    // sComments = connection.prepareStatement("SELECT ID FROM e_comment");
    public List<Comment> getComments() {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            rs = this.sComments.executeQuery();
            while (rs.next()) {
                result.add(this.getComment(rs.getInt("ID")));
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
    // sCommentsBySeries = connection.prepareStatement("SELECT ID_comment FROM r_comment_series WHERE ID_series=?");
    public List<Comment> getComments(Series series) {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            this.sCommentsBySeries.setInt(1, series.getID());
            rs = this.sCommentsBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getComment(rs.getInt("ID_comment")));
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
    // sCommentsByNews = connection.prepareStatement("SELECT ID_comment FROM r_news_comment WHERE ID_news=?");
    public List<Comment> getComments(News news) {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            this.sCommentsByNews.setInt(1, news.getID());
            rs = this.sCommentsByNews.executeQuery();
            while (rs.next()) {
                result.add(this.getComment(rs.getInt("ID_comment")));
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
    // sCommentsByUser = connection.prepareStatement("SELECT ID_comment FROM e_comment WHERE ID_user=?");
    public List<Comment> getComments(User user) {
        ResultSet rs = null;
        List<Comment> result = new ArrayList();
        try {
            this.sCommentsByUser.setInt(1, user.getID());
            rs = this.sCommentsByUser.executeQuery();
            while (rs.next()) {
                result.add(this.getComment(rs.getInt("ID_comment")));
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
    // iComment = connection.prepareStatement("INSERT INTO e_comment (title, text, date, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeComment(Comment comment) {
        ResultSet rs = null;
        int ID = comment.getID();
        try {
            if (ID > 0) { // Update
                if (!comment.isDirty()) {
                    return;
                }
                this.iComment.setString(1, comment.getTitle());
                this.iComment.setString(2, comment.getText());
                this.iComment.setDate(3, new java.sql.Date(comment.getDate().getTime()));
                this.iComment.setInt(4, comment.getLikes());
                this.iComment.setInt(5, comment.getDislikes());
                if (comment.getUser() != null) {
                    this.iComment.setInt(6, comment.getUser().getID());
                } else {
                    this.iComment.setInt(6, 0);
                }
                this.iComment.executeUpdate();
            } else { // Insert
                this.iComment.setString(1, comment.getTitle());
                this.iComment.setString(2, comment.getText());
                this.iComment.setDate(3, null);
                this.iComment.setInt(4, comment.getLikes());
                this.iComment.setInt(5, comment.getDislikes());
                if (comment.getUser() != null) {
                    this.iComment.setInt(6, comment.getUser().getID());
                } else {
                    this.iComment.setInt(6, 0);
                }
                if (this.iComment.executeUpdate() == 1) {
                    rs = this.iComment.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
            if (ID > 0) {
                comment.copyFrom(this.getComment(ID));
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
    // dComment = connection.prepareStatement("DELETE FROM e_comment WHERE ID=?");
    public void removeComment(Comment comment) {
        try {
            this.dComment.setInt(1, comment.getID());
            this.dComment.executeUpdate();
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
    // sEpisodeByID = connection.prepareStatement("SELECT * FROM e_episode WHERE ID=?");
    public Episode getEpisode(int episodeID) {
        ResultSet rs = null;
        Episode result = null;
        try {
            this.sEpisodeByID.setInt(1, episodeID);
            rs = this.sEpisodeByID.executeQuery();
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
    // sEpisodeBySeriesAndSeasonAndNumber = connection.prepareStatement("SELECT * FROM e_episode WHERE ID_series=? AND season=? AND number=?");
    public Episode getEpisodeBySeriesAndSeasonAndNumber(Series series, int season, int number) {
        ResultSet rs = null;
        Episode result = null;
        try {
            this.sEpisodeBySeriesAndSeasonAndNumber.setInt(1, series.getID());
            this.sEpisodeBySeriesAndSeasonAndNumber.setInt(2, season);
            this.sEpisodeBySeriesAndSeasonAndNumber.setInt(3, number);
            rs = this.sEpisodeBySeriesAndSeasonAndNumber.executeQuery();
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
    // sEpisodes = connection.prepareStatement("SELECT ID FROM e_episode");
    public List<Episode> getEpisodes() {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            rs = this.sEpisodes.executeQuery();
            while (rs.next()) {
                result.add(this.getEpisode(rs.getInt("ID")));
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
    // sEpisodesBySeries = connection.prepareStatement("SELECT ID DROM e_episode WHERE ID_series=?");
    public List<Episode> getEpisodes(Series series) {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            this.sEpisodesBySeries.setInt(1, series.getID());
            rs = this.sEpisodesBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getEpisode(rs.getInt("ID")));
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
    // sEpisodesBySeriesAndSeason = connection.prepareStatement("SELECT ID FROM e_episode WHERE ID_series=? AND season=?");
    public List<Episode> getEpisodes(Series series, int season) {
        ResultSet rs = null;
        List<Episode> result = new ArrayList();
        try {
            this.sEpisodesBySeriesAndSeason.setInt(1, series.getID());
            this.sEpisodesBySeriesAndSeason.setInt(2, season);
            rs = this.sEpisodesBySeriesAndSeason.executeQuery();
            while (rs.next()) {
                result.add(this.getEpisode(rs.getInt("ID")));
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
    // iEpisode = connection.prepareStatement("INSERT INTO e_episode (number, season, title, description, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeEpisode(Episode episode) {
        ResultSet rs = null;
        int ID = episode.getID();
        try {
            if (ID > 0) { // Update
                if (!episode.isDirty()) {
                    return;
                }
                this.iEpisode.setInt(1, episode.getNumber());
                this.iEpisode.setInt(2, episode.getSeason());
                this.iEpisode.setString(3, episode.getTitle());
                this.iEpisode.setString(4, episode.getDescription());
                if (episode.getSeries() != null) {
                    this.iEpisode.setInt(5, episode.getSeries().getID());
                } else {
                    this.iEpisode.setInt(5, 0);
                }
                this.iEpisode.executeUpdate();
            } else { // Insert
                this.iEpisode.setInt(1, episode.getNumber());
                this.iEpisode.setInt(2, episode.getSeason());
                this.iEpisode.setString(3, episode.getTitle());
                this.iEpisode.setString(4, episode.getDescription());
                if (episode.getSeries() != null) {
                    this.iEpisode.setInt(5, episode.getSeries().getID());
                } else {
                    this.iEpisode.setInt(5, 0);
                }
                if (this.iEpisode.executeUpdate() == 1) {
                    rs = this.iEpisode.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                episode.copyFrom(this.getEpisode(ID));
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
    // sLastEpisodeSeen = connection.prepareStatement("SELECT season, episode FROM r_user_series WHERE  ID_user=? AND ID_series=?");
    public Episode getLastEpisodeSeen(User user, Series series) {
        ResultSet rs = null;
        Episode result = null;
        try {
            this.sLastEpisodeSeen.setInt(1, user.getID());
            this.sLastEpisodeSeen.setInt(2, series.getID());
            rs = this.sLastEpisodeSeen.executeQuery();
            if (rs.next()) {
                result = this.getEpisodeBySeriesAndSeasonAndNumber(series, rs.getInt("season"), rs.getInt("episode"));
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
    // dEpisode = connection.prepareStatement("DELETE FROM e_episode WHERE ID=?");
    public void removeEpisode(Episode episode) {
        try {
            this.dEpisode.setInt(1, episode.getID());
            this.dEpisode.executeUpdate();
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
    // sGenreByID = connection.prepareStatement("SELECT * FROM e_genre WHERE ID=?");
    public Genre getGenre(int genreID) {
        ResultSet rs = null;
        Genre result = null;
        try {
            this.sGenreByID.setInt(1, genreID);
            rs = this.sGenreByID.executeQuery();
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
    // sGenres = connection.prepareStatement("SELECT ID FROM e_genre");
    public List<Genre> getGenres() {
        ResultSet rs = null;
        List<Genre> result = new ArrayList();
        try {
            rs = this.sGenres.executeQuery();
            while (rs.next()) {
                result.add(this.getGenre(rs.getInt("ID")));
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
    // sGenresBySeries = connection.prepareStatement("SELECT ID_genre FROM r_genre_series WHERE ID_series=?");
    public List<Genre> getGenres(Series series) {
        ResultSet rs = null;
        List<Genre> result = new ArrayList();
        try {
            this.sGenresBySeries.setInt(1, series.getID());
            rs = this.sGenresBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getGenre(rs.getInt("ID_genre")));
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
    // sGenresByUser = connection.prepareStatement("SELECT ID_genre FROM r_user_genre WHERE ID_user=?");
    public List<Genre> getGenres(User user) {
        ResultSet rs = null;
        List<Genre> result = new ArrayList();
        try {
            this.sGenresByUser.setInt(1, user.getID());
            rs = this.sGenresByUser.executeQuery();
            while (rs.next()) {
                result.add(this.getGenre(rs.getInt("ID_genre")));
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
    // iGenre = connection.prepareStatement("INSERT INTO e_genre (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
    public void storeGenre(Genre genre) {
        ResultSet rs = null;
        int ID = genre.getID();
        try {
            if (ID > 0) { // Update
                if (!genre.isDirty()) {
                    return;
                }
                this.iGenre.setString(1, genre.getName());
                this.iGenre.executeUpdate();
            } else { // Insert
                this.iGenre.setString(1, genre.getName());
                if (this.iGenre.executeUpdate() == 1) {
                    rs = this.iGenre.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                genre.copyFrom(this.getGenre(ID));
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
    // dGenre = connection.prepareStatement("DELETE FROM e_genre WHERE ID=?");
    public void removeGenre(Genre genre) {
        try {
            this.dGenre.setInt(1, genre.getID());
            this.dGenre.executeUpdate();
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
    // sGroupByID = connection.prepareStatement("SELECT * FROM e_group WHERE ID=?");
    public Group getGroup(int groupID) {
        ResultSet rs = null;
        Group result = null;
        try {
            this.sGroupByID.setInt(1, groupID);
            rs = this.sGroupByID.executeQuery();
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
    // sGroupByUser = connection.prepareStatement("SELECT ID_group FROM e_user WHERE ID=?");
    public Group getGroup(User user) {
        ResultSet rs = null;
        Group result = null;
        try {
            this.sGenresByUser.setInt(1, user.getID());
            rs = this.sGroupByUser.executeQuery();
            if (rs.next()) {
                result = this.getGroup(rs.getInt("ID_group"));
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
    // sGroups = connection.prepareStatement("SELECT ID FROM e_groups");
    public List<Group> getGroups() {
        ResultSet rs = null;
        List<Group> result = new ArrayList();
        try {
            rs = this.sGroups.executeQuery();
            while (rs.next()) {
                result.add(this.getGroup(rs.getInt("ID")));
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
    // sGroupsByService = connection.prepareStatement("SELECT ID_group FROM r_service_group WHERE ID_service=?");
    public List<Group> getGroups(Service service) {
        ResultSet rs = null;
        List<Group> result = new ArrayList();
        try {
            this.sGroupsByService.setInt(1, service.getID());
            rs = this.sGroupsByService.executeQuery();
            while (rs.next()) {
                result.add(this.getGroup(rs.getInt("ID_group")));
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
    // iGroup = connection.prepareStatement("INSERT INTO e_group (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeGroup(Group group) {
        ResultSet rs = null;
        int ID = group.getID();
        try {
            if (ID > 0) { // Update
                if (!group.isDirty()) {
                    return;
                }
                this.iGroup.setString(1, group.getName());
                this.iGroup.setString(2, group.getDescription());
                this.iGroup.executeUpdate();
            } else { // Insert
                this.iGroup.setString(1, group.getName());
                this.iGroup.setString(2, group.getDescription());
                if (this.iGroup.executeUpdate() == 1) {
                    rs = this.iGroup.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                group.copyFrom(this.getGroup(ID));
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
    // dGroup = connection.prepareStatement("DELETE FROM e_group WHERE ID=?");
    public void removeGroup(Group group) {
        try {
            this.dGroup.setInt(1, group.getID());
            this.dGroup.executeUpdate();
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
    // sMessageByID = connection.prepareStatement("SELECT FROM e_message WHERE ID=?");
    public Message getMessage(int messageID) {
        ResultSet rs = null;
        Message result = null;
        try {
            this.sMessageByID.setInt(1, messageID);
            rs = this.sMessageByID.executeQuery();
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
    // sMessages = connection.prepareStatement("SELECT ID FROM e_message");
    public List<Message> getMessages() {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            rs = this.sMessages.executeQuery();
            while (rs.next()) {
                result.add(this.getMessage(rs.getInt("ID")));
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
    // sMessagesByUser = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=?");
    public List<Message> getMessages(User user) {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            this.sMessagesByUser.setInt(1, user.getID());
            rs = this.sMessagesByUser.executeQuery();
            while (rs.next()) {
                result.add(this.getMessage(rs.getInt("ID")));
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
    // sMessagesBySeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_series=?");
    public List<Message> getMessages(Series series) {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            this.sMessagesBySeries.setInt(1, series.getID());
            rs = this.sMessagesBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getMessage(rs.getInt("ID")));
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
    // sMessagesByUserAndSeries = connection.prepareStatement("SELECT ID FROM e_message WHERE ID_user=? AND ID_series=?");
    public List<Message> getMessages(Series series, User user) {
        ResultSet rs = null;
        List<Message> result = new ArrayList();
        try {
            this.sMessagesByUserAndSeries.setInt(1, user.getID());
            this.sMessagesByUserAndSeries.setInt(2, series.getID());
            rs = this.sMessagesByUserAndSeries.executeQuery();
            while (rs.next()) {
                result.add(this.getMessage(rs.getInt("ID")));
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
    // iMessage = connection.prepareStatement("INSERT INTO e_message (title, text, date, ID_user, ID_series) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeMessage(Message message) {
        ResultSet rs = null;
        int ID = message.getID();
        try {
            if (ID > 0) { // Update
                if (!message.isDirty()) {
                    return;
                }
                this.iMessage.setString(1, message.getTitle());
                this.iMessage.setString(2, message.getText());
                this.iMessage.setDate(3, new java.sql.Date(message.getDate().getTime()));
                this.iMessage.setInt(4, message.getUser().getID());
                this.iMessage.setInt(5, message.getSeries().getID());
                this.iMessage.executeUpdate();
            } else { // Insert
                this.iMessage.setString(1, message.getTitle());
                this.iMessage.setString(2, message.getText());
                this.iMessage.setDate(3, null);
                if (message.getUser() != null) {
                    this.iMessage.setInt(4, message.getUser().getID());
                } else {
                    this.iMessage.setInt(4, 0);
                }
                if (message.getSeries() != null) {
                    this.iMessage.setInt(5, message.getSeries().getID());
                } else {
                    this.iMessage.setInt(5, 0);
                }
                this.iMessage.executeUpdate();
                if (this.iMessage.executeUpdate() == 1) {
                    rs = this.iMessage.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                message.copyFrom(this.getMessage(ID));
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
    // dMessage = connection.prepareStatement("DELETE FROM e_message WHERE ID=?");
    public void removeMessage(Message message) {
        try {
            this.dMessage.setInt(1, message.getID());
            this.dMessage.executeUpdate();
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
    // sNewsByID = connection.prepareStatement("SELECT * FROM e_news");
    public News getNews(int newsID) {
        ResultSet rs = null;
        News result = null;
        try {
            this.sNewsByID.setInt(1, newsID);
            rs = this.sNewsByID.executeQuery();
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
    // sNewsByComment = connection.prepareStatement("SELECT ID_news FROM r_news_comment WHERE ID_comment=?");
    public News getNews(Comment comment) {
        ResultSet rs = null;
        News result = null;
        try {
            this.sNewsByComment.setInt(1, comment.getID());
            rs = this.sNewsByComment.executeQuery();
            while (rs.next()) {
                result = this.getNews(rs.getInt("ID_news"));
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
    // sNews = connection.prepareStatement("SELECT ID FROM e_news");
    public List<News> getNews() {
        ResultSet rs = null;
        List<News> result = new ArrayList();
        try {
            rs = this.sNews.executeQuery();
            while (rs.next()) {
                result.add(this.getNews(rs.getInt("ID")));
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
    // sNewsByUser = connection.prepareStatement("SELECT ID FROM e_news WHERE ID_user=?");
    public List<News> getNews(User user) {
        ResultSet rs = null;
        List<News> result = new ArrayList();
        try {
            this.sNewsByUser.setInt(1, user.getID());
            rs = this.sNewsByUser.executeQuery();
            while (rs.next()) {
                result.add(this.getNews(rs.getInt("ID")));
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
    // sNewsbySeries = connection.prepareStatement("SELECT ID_news FROM r_news_series WHERE ID_series=?");
    public List<News> getNews(Series series) {
        ResultSet rs = null;
        List<News> result = new ArrayList();
        try {
            this.sNewsbySeries.setInt(1, series.getID());
            rs = this.sNewsbySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getNews(rs.getInt("ID_news")));
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
    // iNews = connection.prepareStatement("INSERT INTO e_news (title, text, date, likes, dislikes, ID_user) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeNews(News news) {
        ResultSet rs = null;
        int ID = news.getID();
        try {
            if (ID > 0) { // Update
                if (!news.isDirty()) {
                    return;
                }
                this.iNews.setString(1, news.getTitle());
                this.iNews.setString(2, news.getText());
                this.iNews.setDate(3, new java.sql.Date(news.getDate().getTime()));
                this.iNews.setInt(4, news.getLikes());
                this.iNews.setInt(5, news.getDislikes());
                if (news.getUser() != null) {
                    this.iNews.setInt(6, news.getUser().getID());
                } else {
                    this.iNews.setInt(6, 0);
                }
                this.iNews.executeUpdate();
            } else { // Insert
                this.iNews.setString(1, news.getTitle());
                this.iNews.setString(2, news.getText());
                this.iNews.setDate(3, null);
                this.iNews.setInt(4, news.getLikes());
                this.iNews.setInt(5, news.getDislikes());
                if (news.getUser() != null) {
                    this.iNews.setInt(6, news.getUser().getID());
                } else {
                    this.iNews.setInt(6, 0);
                }
                if (this.iNews.executeUpdate() == 1) {
                    rs = this.iNews.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                news.copyFrom(this.getNews(ID));
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
    // dNews = connection.prepareStatement("DELETE FROM e_news WHERE ID=?");
    public void removeNews(News news) {
        try {
            this.dNews.setInt(1, news.getID());
            this.dNews.executeLargeUpdate();
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
    // sSeriesByID = connection.prepareStatement("SELECT * FROM e_series WHERE ID=?");
    public Series getSeries(int seriesID) {
        ResultSet rs = null;
        Series result = null;
        try {
            this.sSeriesByID.setInt(1, seriesID);
            rs = this.sSeriesByID.executeQuery();
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
    // sSeriesByMessage = connection.prepareStatement("SELECT ID_series FROM e_message WHERE ID=?");
    public Series getSeries(Message message) {
        ResultSet rs = null;
        Series result = null;
        try {
            this.sSeriesByMessage.setInt(1, message.getID());
            rs = this.sSeriesByMessage.executeQuery();
            while (rs.next()) {
                result = this.getSeries(rs.getInt("ID_series"));
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
    // sSeriesByEpisode = connection.prepareStatement("SELECT ID_series FROM e_episode WHERE ID=?");
    public Series getSeries(Episode episode) {
        ResultSet rs = null;
        Series result = null;
        try {
            this.sSeriesByEpisode.setInt(1, episode.getID());
            rs = this.sSeriesByEpisode.executeQuery();
            while (rs.next()) {
                result = this.getSeries(rs.getInt("ID_series"));
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
    // sSeriesByComment = connection.prepareStatement("SELECT ID_series FROM r_comment_series WHERE ID_comment=?");
    public Series getSeries(Comment comment) {
        ResultSet rs = null;
        Series result = null;
        try {
            this.sSeriesByComment.setInt(1, comment.getID());
            rs = this.sSeriesByComment.executeQuery();
            while (rs.next()) {
                result = this.getSeries(rs.getInt("ID_series"));
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
    // sSeries = connection.prepareStatement("SELECT ID FROM e_series");
    public List<Series> getSeries() {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            rs = this.sSeries.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID")));
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
    // sSeriesByNews = connection.prepareStatement("SELECT ID_series FROM r_news_series WHERE ID_news=?");
    public List<Series> getSeries(News news) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            this.sSeriesByNews.setInt(1, news.getID());
            rs = this.sSeriesByNews.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID_series")));
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
    // sSeriesByGenre = connection.prepareStatement("SELECT ID_series FROM r_genre_series WHERE ID_genre=?");
    public List<Series> getSeries(Genre genre) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            this.sSeriesByGenre.setInt(1, genre.getID());
            rs = this.sSeriesByGenre.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID_series")));
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
    // sSeriesByChannel = connection.prepareStatement("SELECT ID_series FROM r_channel_series WHERE ID_channel=?");
    public List<Series> getSeries(Channel channel) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            this.sSeriesByChannel.setInt(1, channel.getID());
            rs = this.sSeriesByChannel.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID_series")));
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
    // sSeriesByCastMember = connection.prepareStatement("SELECT ID_series FROM r_cast_series WHERE ID_cast=?");
    public List<Series> getSeries(Cast castMember) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            this.sSeriesByCastMember.setInt(1, castMember.getID());
            rs = this.sSeriesByCastMember.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID_series")));
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
    // sSeriesByCastMemberAndRole = connection.prepareStatement("SELECT ID_series FROM r_cast_series WHERE ID_cast=? AND role=?");
    public List<Series> getSeries(Cast castMember, String role) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            this.sSeriesByCastMemberAndRole.setInt(1, castMember.getID());
            this.sSeriesByCastMemberAndRole.setString(2, role);
            rs = this.sSeriesByCastMemberAndRole.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID_series")));
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
    // sSeriesByUser = connection.prepareStatement("SELECT ID_series FROM r_user_series WHERE ID_user=?");
    public List<Series> getSeries(User user) {
        ResultSet rs = null;
        List<Series> result = new ArrayList();
        try {
            this.sSeriesByUser.setInt(1, user.getID());
            rs = this.sSeriesByUser.executeQuery();
            while (rs.next()) {
                result.add(this.getSeries(rs.getInt("ID_series")));
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
    // iSeries = connection.prepareStatement("INSERT INTO e_series (name, year, description, image_URL, state, add_count) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeSeries(Series series) {
        ResultSet rs = null;
        int ID = series.getID();
        try {
            if (ID > 0) { // Update
                if (!series.isDirty()) {
                    return;
                }
                this.iSeries.setString(1, series.getName());
                this.iSeries.setInt(2, series.getYear());
                this.iSeries.setString(3, series.getDescription());
                this.iSeries.setString(4, series.getImageURL());
                this.iSeries.setString(5, series.getState());
                this.iSeries.setInt(6, series.getAddCount());
                this.iSeries.executeUpdate();
            } else { // Insert
                this.iSeries.setString(1, series.getName());
                this.iSeries.setInt(2, series.getYear());
                this.iSeries.setString(3, series.getDescription());
                this.iSeries.setString(4, series.getImageURL());
                this.iSeries.setString(5, series.getState());
                this.iSeries.setInt(6, series.getAddCount());
                if (this.iSeries.executeUpdate() == 1) {
                    rs = this.iSeries.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                series.copyFrom(this.getSeries(ID));
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
    // dSeries = connection.prepareStatement("DELETE FROM e_series WHRE ID=?");
    public void removeSeries(Series series) {
        try {
            this.dSeries.setInt(1, series.getID());
            this.iSeries.executeUpdate();
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
    // sServiceByID = connection.prepareStatement("SELECT * FROM e_service WHERE ID=?");
    public Service getService(int serviceID) {
        ResultSet rs = null;
        Service result = null;
        try {
            this.sServiceByID.setInt(1, serviceID);
            rs = this.sServiceByID.executeQuery();
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
    // sServices = connection.prepareStatement("SELECT ID FROM e_service");
    public List<Service> getServices() {
        ResultSet rs = null;
        List<Service> result = new ArrayList();
        try {
            rs = this.sServices.executeQuery();
            while (rs.next()) {
                result.add(this.getService(rs.getInt("ID")));
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
    // sServicesByGroup = connection.prepareStatement("SELECT ID_service FROM r_service_group WHERE ID_group=?");
    public List<Service> getServices(Group group) {
        ResultSet rs = null;
        List<Service> result = new ArrayList();
        try {
            this.sServicesByGroup.setInt(1, group.getID());
            rs = this.sServicesByGroup.executeQuery();
            while (rs.next()) {
                result.add(this.getService(rs.getInt("ID_service")));
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
    // iService = connection.prepareStatement("INSERT INTO e_service (name, description, script_name) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeService(Service service) {
        ResultSet rs = null;
        int ID = service.getID();
        try {
            if (ID > 0) { // Update
                if (!service.isDirty()) {
                    return;
                }
                this.iService.setString(1, service.getName());
                this.iService.setString(2, service.getDescription());
                this.iService.setString(3, service.getScriptName());
                this.iService.executeUpdate();
            } else { // Insert
                this.iService.setString(1, service.getName());
                this.iService.setString(2, service.getDescription());
                this.iService.setString(3, service.getScriptName());
                if (this.iService.executeUpdate() == 1) {
                    rs = this.iService.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                service.copyFrom(this.getService(ID));
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
    // dService = connection.prepareStatement("DELETE FROM e_service WHERE ID=?");
    public void removeService(Service service) {
        try {
            this.dService.setInt(1, service.getID());
            this.dService.executeUpdate();
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
    // sUserByID = connection.prepareStatement("SELECT * FROM e_user WHERE ID=?");
    public User getUser(int userID) {
        ResultSet rs = null;
        User result = null;
        try {
            this.sUserByID.setInt(1, userID);
            rs = this.sUserByID.executeQuery();
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
    // sUserByUsernameAndPassword = connection.prepareStatement("SELECT * FROM e_user WHERE username=? AND password=?");
    public User getUser(String username, String password) {
        ResultSet rs = null;
        User result = null;
        try {
            this.sUserByUsernameAndPassword.setString(1, username);
            this.sUserByUsernameAndPassword.setString(2, password);
            rs = this.sUserByUsernameAndPassword.executeQuery();
            if (rs.next()) {
                result = this.getUser(rs.getInt("ID"));
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
    // sUserByMessage = connection.prepareStatement("Select ID_user FROM e_message WHERE ID=?");
    public User getUser(Message message) {
        ResultSet rs = null;
        User result = null;
        try {
            this.sUserByMessage.setInt(1, message.getID());
            rs = this.sUserByMessage.executeQuery();
            while (rs.next()) {
                result = this.getUser(rs.getInt("ID_user"));
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
    // sUserByComment = connection.prepareStatement("SELECT ID_user FROM e_comment WHERE ID=?");
    public User getUser(Comment comment) {
        ResultSet rs = null;
        User result = null;
        try {
            this.sUserByComment.setInt(1, comment.getID());
            rs = this.sUserByComment.executeQuery();
            while (rs.next()) {
                result = this.getUser(rs.getInt("ID_user"));
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
    // sUserByNews = connection.prepareStatement("SELECT ID_user FROM e_news WHERE ID=?");
    public User getUser(News news) {
        ResultSet rs = null;
        User result = null;
        try {
            this.sUserByNews.setInt(1, news.getID());
            rs = this.sUserByNews.executeQuery();
            while (rs.next()) {
                result = this.getUser(rs.getInt("ID_user"));
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
    // sUsers = connection.prepareStatement("SELECT ID FROM e_user");
    public List<User> getUsers() {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            rs = this.sUsers.executeQuery();
            while (rs.next()) {
                result.add(this.getUser(rs.getInt("ID")));
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
    // sUsersBySeries = connection.prepareStatement("SELECT ID_user FROM r_user_series WHERE ID_series=?");
    public List<User> getUsers(Series series) {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            this.sUsersBySeries.setInt(1, series.getID());
            rs = this.sUsersBySeries.executeQuery();
            while (rs.next()) {
                result.add(this.getUser(rs.getInt("ID_user")));
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
    // sUsersByGenre = connection.prepareStatement("SELECT ID_user FROM r_user_genre WHERE ID_genre=?");
    public List<User> getUsers(Genre genre) {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            this.sUsersByGenre.setInt(1, genre.getID());
            rs = this.sUsersByGenre.executeQuery();
            while (rs.next()) {
                result.add(this.getUser(rs.getInt("ID_user")));
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
    // sUsersByGroup = connection.prepareStatement("SELECT ID FROM e_user WHERE ID_group=?");
    public List<User> getUsers(Group group) {
        ResultSet rs = null;
        List<User> result = new ArrayList();
        try {
            this.sUsersByGroup.setInt(1, group.getID());
            rs = this.sUsersByGroup.executeQuery();
            while (rs.next()) {
                result.add(this.getUser(rs.getInt("ID")));
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
    // iUser = connection.prepareStatement("INSERT INTO e_user (username, password, mail, name, surname, age, gender, image_URL, presonal_message, ID_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    public void storeUser(User user) {
        ResultSet rs = null;
        int ID = user.getID();
        try {
            if (ID > 0) { // Update
                if (!user.isDirty()) {
                    return;
                }
                this.iUser.setString(1, user.getUsername());
                this.iUser.setString(2, user.getPassword());
                this.iUser.setString(3, user.getMail());
                this.iUser.setString(4, user.getName());
                this.iUser.setString(5, user.getSurname());
                this.iUser.setInt(6, user.getAge());
                this.iUser.setString(7, user.getGender());
                this.iUser.setString(8, user.getImageURL());
                this.iUser.setString(9, user.getPersonalMessage());
                if (user.getGroup() != null) {
                    this.iUser.setInt(10, user.getGroup().getID());
                } else {
                    this.iUser.setNull(10, java.sql.Types.INTEGER);
                }
                this.iUser.executeUpdate();
            } else { // Insert
                this.iUser.setString(1, user.getUsername());
                this.iUser.setString(2, user.getPassword());
                this.iUser.setString(3, user.getMail());
                this.iUser.setString(4, user.getName());
                this.iUser.setString(5, user.getSurname());
                this.iUser.setInt(6, user.getAge());
                this.iUser.setString(7, user.getGender());
                this.iUser.setString(8, user.getImageURL());
                this.iUser.setString(9, user.getPersonalMessage());
                if (user.getGroup() != null) {
                    this.iUser.setInt(10, user.getGroup().getID());
                } else {
                    this.iUser.setNull(10, java.sql.Types.INTEGER);
                }
                if (this.iUser.executeUpdate() == 1) {
                    rs = this.iUser.getGeneratedKeys();
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }

            }
            if (ID > 0) {
                user.copyFrom(this.getUser(ID));
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
    // dUser = connection.prepareStatement("DELETE FROM e_user WHERE ID=?");
    public void removeUser(User user) {
        try {
            this.dUser.setInt(1, user.getID());
            this.dUser.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RESTDataLayerMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
