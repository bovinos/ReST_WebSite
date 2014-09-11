
package it.univaq.f4i.iw.framework.security;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;

/**
 *
 * @author Mirko
 */
public class RESTSecurityLayer {
    public static User stripSlashesUser(User user){
        user.setName(SecurityLayer.stripSlashes(user.getName()));
        user.setSurname(SecurityLayer.stripSlashes(user.getSurname()));
        user.setMail(SecurityLayer.stripSlashes(user.getMail()));
        user.setImageURL(SecurityLayer.stripSlashes(user.getImageURL()));
        user.setGender(SecurityLayer.stripSlashes(user.getGender()));
        return user;
    }
    public static News stripSlashesNews(News news){
         news.setTitle(SecurityLayer.stripSlashes(news.getTitle()));
        news.setText(SecurityLayer.stripSlashes(news.getText()));
        news.setImageURL(SecurityLayer.stripSlashes(news.getImageURL()));
        return news;
    }
    public static CastMember stripSlashesCastMember(CastMember castmember){
        castmember.setName(SecurityLayer.stripSlashes(castmember.getName()));
        castmember.setSurname(SecurityLayer.stripSlashes(castmember.getSurname()));
        castmember.setGender(SecurityLayer.stripSlashes(castmember.getGender()));
        castmember.setCountry(SecurityLayer.stripSlashes(castmember.getCountry()));
        castmember.setImageURL(SecurityLayer.stripSlashes(castmember.getImageURL()));
        return castmember;
    }
    public static Channel stripSlashesChannerl(Channel channel){
        channel.setName(SecurityLayer.stripSlashes(channel.getName()));
        channel.setType(SecurityLayer.stripSlashes(channel.getType()));
        return channel;
    }
    public static Comment stripSlashesComment(Comment comment){
        comment.setTitle(SecurityLayer.stripSlashes(comment.getTitle()));
        comment.setText(SecurityLayer.stripSlashes(comment.getText()));
        return comment;
    }
    public static Episode stripSlashesEpisode(Episode episode){
        episode.setTitle(SecurityLayer.stripSlashes(episode.getTitle()));
        episode.setDescription(SecurityLayer.stripSlashes(episode.getDescription()));
        return episode;
    }
    public static Genre stripSlashesGenre(Genre genre){
        genre.setName(SecurityLayer.stripSlashes(genre.getName()));
        return genre;
    }
    public static Series stripSlashesSeries(Series series){
        series.setName(SecurityLayer.stripSlashes(series.getName()));
        series.setDescription(SecurityLayer.stripSlashes(series.getDescription()));
        series.setState(SecurityLayer.stripSlashes(series.getState()));
        series.setImageURL(SecurityLayer.stripSlashes(series.getImageURL()));
        return series;
    }
    
        public static User addSlashesUser(User user){
        user.setName(SecurityLayer.addSlashes(user.getName()));
        user.setSurname(SecurityLayer.addSlashes(user.getSurname()));
        user.setMail(SecurityLayer.addSlashes(user.getMail()));
        user.setImageURL(SecurityLayer.addSlashes(user.getImageURL()));
        user.setGender(SecurityLayer.addSlashes(user.getGender()));
        return user;
    }
    public static News addSlashesNews(News news){
         news.setTitle(SecurityLayer.addSlashes(news.getTitle()));
        news.setText(SecurityLayer.addSlashes(news.getText()));
        news.setImageURL(SecurityLayer.addSlashes(news.getImageURL()));
        return news;
    }
    public static CastMember addSlashesCastMember(CastMember castmember){
        castmember.setName(SecurityLayer.addSlashes(castmember.getName()));
        castmember.setSurname(SecurityLayer.addSlashes(castmember.getSurname()));
        castmember.setGender(SecurityLayer.addSlashes(castmember.getGender()));
        castmember.setCountry(SecurityLayer.addSlashes(castmember.getCountry()));
        castmember.setImageURL(SecurityLayer.addSlashes(castmember.getImageURL()));
        return castmember;
    }
    public static Channel addSlashesChannerl(Channel channel){
        channel.setName(SecurityLayer.addSlashes(channel.getName()));
        channel.setType(SecurityLayer.addSlashes(channel.getType()));
        return channel;
    }
    public static Comment addSlashesComment(Comment comment){
        comment.setTitle(SecurityLayer.addSlashes(comment.getTitle()));
        comment.setText(SecurityLayer.addSlashes(comment.getText()));
        return comment;
    }
    public static Episode addSlashesEpisode(Episode episode){
        episode.setTitle(SecurityLayer.addSlashes(episode.getTitle()));
        episode.setDescription(SecurityLayer.addSlashes(episode.getDescription()));
        return episode;
    }
    public static Genre addSlashesGenre(Genre genre){
        genre.setName(SecurityLayer.addSlashes(genre.getName()));
        return genre;
    }
    public static Series addSlashesSeries(Series series){
        series.setName(SecurityLayer.addSlashes(series.getName()));
        series.setDescription(SecurityLayer.addSlashes(series.getDescription()));
        series.setState(SecurityLayer.addSlashes(series.getState()));
        series.setImageURL(SecurityLayer.addSlashes(series.getImageURL()));
        return series;
    }
}
