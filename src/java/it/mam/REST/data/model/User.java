package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface User {

    String MALE = "M";
    String FEMALE = "F";

    int getID();

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getMail();

    void setMail(String mail);

    String getName();

    void setName(String name);

    String getSurname();

    void setSurname(String surname);

    int getAge();

    void setAge(int age);

    String getGender();

    void setGender(String gender);

    String getImageURL();

    void setImageURL(String imageURL);

    boolean getNotificationStatus();

    void setNotificationStatus(boolean status);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(User user);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Comment> getComments();

    void setComments(List<Comment> comments);

    void addComment(Comment comment);

    void removeComment(Comment comment);

    Comment editComment(Comment comment);

    void removeAllComment();

    List<Series> getSeries();

    List<Series> getSeries(int rating);

    void setSeries(List<Series> series);

    void addSeries(Series series);

    void removeSeries(Series series);

    void removeAllSeries();

    List<UserSeries> getUserSeries();

    void setUserSeries(List<UserSeries> userSeries);

    List<Genre> getGenres();

    void setGenres(List<Genre> genres);

    void addGenre(Genre genre);

    void removeGenre(Genre genre);

    void removeAllGenre();

    List<Message> getMessages();

    void setMessages(List<Message> messages);

    void addMessage(Series series, Message message);

    void removeMessage(Message message);

    Message editMessage(Series series, Message message);

    void removeAllMessages();

    /**
     * We assumed that news are created only by admins
     *
     * @return
     */
    List<News> getNews();

    void setNews(List<News> news);

    void addNews(News news);

    void removeNews(News news);

    News editNews(News news);

    void removeAllNews();

    Group getGroup();

    void setGroup(Group group);

}
