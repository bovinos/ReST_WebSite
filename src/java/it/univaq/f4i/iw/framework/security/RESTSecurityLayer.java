
package it.univaq.f4i.iw.framework.security;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.Comment;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.Message;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.Service;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;

/**
 *
 * @author Mirko
 */
public class RESTSecurityLayer {
    public static User stripSlashes(User user){
        user.setUsername(SecurityLayer.stripSlashes(user.getUsername()));
        if(user.getName() != null) {user.setName(SecurityLayer.stripSlashes(user.getName()));}
        if(user.getSurname() != null) { user.setSurname(SecurityLayer.stripSlashes(user.getSurname()));}
        user.setMail(SecurityLayer.stripSlashes(user.getMail()));
         if(user.getImageURL()!= null) {user.setImageURL(SecurityLayer.stripSlashes(user.getImageURL()));}
         if(user.getGender()!= null) {user.setGender(SecurityLayer.stripSlashes(user.getGender()));}
        return user;
    }
    public static News stripSlashes(News news){
         news.setTitle(SecurityLayer.stripSlashes(news.getTitle()));
        news.setText(SecurityLayer.stripSlashes(news.getText()));
        news.setImageURL(SecurityLayer.stripSlashes(news.getImageURL()));
        return news;
    }
    public static CastMember stripSlashes(CastMember castmember){
        castmember.setName(SecurityLayer.stripSlashes(castmember.getName()));
        castmember.setSurname(SecurityLayer.stripSlashes(castmember.getSurname()));
        castmember.setGender(SecurityLayer.stripSlashes(castmember.getGender()));
        castmember.setCountry(SecurityLayer.stripSlashes(castmember.getCountry()));
        castmember.setImageURL(SecurityLayer.stripSlashes(castmember.getImageURL()));
        return castmember;
    }
    public static Channel stripSlashes(Channel channel){
        channel.setName(SecurityLayer.stripSlashes(channel.getName()));
        channel.setType(SecurityLayer.stripSlashes(channel.getType()));
        return channel;
    }
    public static Comment stripSlashes(Comment comment){
        comment.setTitle(SecurityLayer.stripSlashes(comment.getTitle()));
        comment.setText(SecurityLayer.stripSlashes(comment.getText()));
        return comment;
    }
    public static Episode stripSlashes(Episode episode){
        episode.setTitle(SecurityLayer.stripSlashes(episode.getTitle()));
        episode.setDescription(SecurityLayer.stripSlashes(episode.getDescription()));
        return episode;
    }
    public static Genre stripSlashes(Genre genre){
        genre.setName(SecurityLayer.stripSlashes(genre.getName()));
        return genre;
    }
    public static Series stripSlashes(Series series){
        series.setName(SecurityLayer.stripSlashes(series.getName()));
        series.setDescription(SecurityLayer.stripSlashes(series.getDescription()));
        series.setState(SecurityLayer.stripSlashes(series.getState()));
        series.setImageURL(SecurityLayer.stripSlashes(series.getImageURL()));
        return series;
    }
    
        public static CastMemberSeries stripSlashes(CastMemberSeries cms){
        cms.setRole(SecurityLayer.stripSlashes(cms.getRole()));
        return cms;
    }
        
        public static Message stripSlashes(Message m){
        m.setTitle(SecurityLayer.stripSlashes(m.getTitle()));
        m.setText(SecurityLayer.stripSlashes(m.getText()));
        return m;
    }
        
       public static Group stripSlashes(Group g){
        g.setName(SecurityLayer.stripSlashes(g.getName()));
        g.setDescription(SecurityLayer.stripSlashes(g.getDescription()));
        return g;
    }
       
        public static Service stripSlashes(Service s){
        s.setName(SecurityLayer.stripSlashes(s.getName()));
        s.setDescription(SecurityLayer.stripSlashes(s.getDescription()));
        return s;
    }
    /* -----------------ADD SLASHES --------------------------*/
        
        public static User addSlashes(User user){
        user.setUsername(SecurityLayer.addSlashes(user.getUsername()));
        if(user.getName() != null) {user.setName(SecurityLayer.addSlashes(user.getName()));}
        if(user.getSurname() != null) { user.setSurname(SecurityLayer.addSlashes(user.getSurname()));}
        user.setMail(SecurityLayer.addSlashes(user.getMail()));
         if(user.getImageURL()!= null) {user.setImageURL(SecurityLayer.addSlashes(user.getImageURL()));}
         if(user.getGender()!= null) {user.setGender(SecurityLayer.addSlashes(user.getGender()));}
        return user;
    }
    public static News addSlashes(News news){
         news.setTitle(SecurityLayer.addSlashes(news.getTitle()));
        news.setText(SecurityLayer.addSlashes(news.getText()));
        news.setImageURL(SecurityLayer.addSlashes(news.getImageURL()));
        return news;
    }
    public static CastMember addSlashes(CastMember castmember){
        castmember.setName(SecurityLayer.addSlashes(castmember.getName()));
        castmember.setSurname(SecurityLayer.addSlashes(castmember.getSurname()));
        castmember.setGender(SecurityLayer.addSlashes(castmember.getGender()));
        castmember.setCountry(SecurityLayer.addSlashes(castmember.getCountry()));
        castmember.setImageURL(SecurityLayer.addSlashes(castmember.getImageURL()));
        return castmember;
    }
    public static Channel addSlashes(Channel channel){
        channel.setName(SecurityLayer.addSlashes(channel.getName()));
        channel.setType(SecurityLayer.addSlashes(channel.getType()));
        return channel;
    }
    public static Comment addSlashes(Comment comment){
        comment.setTitle(SecurityLayer.addSlashes(comment.getTitle()));
        comment.setText(SecurityLayer.addSlashes(comment.getText()));
        return comment;
    }
    public static Episode addSlashes(Episode episode){
        episode.setTitle(SecurityLayer.addSlashes(episode.getTitle()));
        episode.setDescription(SecurityLayer.addSlashes(episode.getDescription()));
        return episode;
    }
    public static Genre addSlashes(Genre genre){
        genre.setName(SecurityLayer.addSlashes(genre.getName()));
        return genre;
    }
    public static Series addSlashes(Series series){
        series.setName(SecurityLayer.addSlashes(series.getName()));
        series.setDescription(SecurityLayer.addSlashes(series.getDescription()));
        series.setState(SecurityLayer.addSlashes(series.getState()));
        series.setImageURL(SecurityLayer.addSlashes(series.getImageURL()));
        return series;
    }
    
     public static CastMemberSeries addSlashes(CastMemberSeries cms){
        cms.setRole(SecurityLayer.addSlashes(cms.getRole()));
        return cms;
    }
     
      public static Message addSlashes(Message m){
        m.setTitle(SecurityLayer.addSlashes(m.getTitle()));
        m.setText(SecurityLayer.addSlashes(m.getText()));
        return m;
    }
     
     public static UserSeries addSlashes(UserSeries us){
        us.setRating(SecurityLayer.addSlashes(us.getRating()));
        return us;
    }
     
     public static Group addSlashes(Group g){
        g.setName(SecurityLayer.addSlashes(g.getName()));
        g.setDescription(SecurityLayer.addSlashes(g.getDescription()));
        return g;
    }
       
        public static Service addSlashes(Service s){
        s.setName(SecurityLayer.addSlashes(s.getName()));
        s.setDescription(SecurityLayer.addSlashes(s.getDescription()));
        return s;
    }
}
