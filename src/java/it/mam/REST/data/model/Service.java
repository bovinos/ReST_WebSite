package it.mam.REST.data.model;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Service {

    int getID();

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String decription);

    String getScriptName();

    void setScriptName(String scriptName);

    boolean isDirty();

    void setDirty(boolean dirty);

    void copyFrom(Service service);

    //====================================
    //                 RELATIONSHIP                //
    //====================================
    List<Group> getGroups();

    void setGroups(List<Group> groups);

    void addGroup(Group group);

    void removeGroup(Group group);

    void removeAllGroup();

}
