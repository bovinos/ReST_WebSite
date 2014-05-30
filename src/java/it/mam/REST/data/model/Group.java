package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Group {

    int getID();

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Group group);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<User> getUsers();

    void setUsers(List<User> users);

    void addUser(User user);

    void removeUser(User user);

    void removeAllUsers();

    List<Service> getServices();

    void setServices(List<Service> services);

    void addService(Service service);

    void removeService(Service service);

    void removeAllServices();

}
