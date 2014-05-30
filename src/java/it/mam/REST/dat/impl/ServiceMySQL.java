package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alex
 */
public class ServiceMySQL implements Service {

    private int ID;
    private String name;
    private String description;
    private String scriptName;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    List<Group> groups;

    public ServiceMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.name = "";
        this.description = "";
        this.scriptName = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.groups = null;
    }

    public ServiceMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.ID = rs.getInt("ID");
        this.name = rs.getString("name");
        this.description = rs.getString("description");
        this.scriptName = rs.getString("script_name");
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        this.dirty = true;
    }

    @Override
    public String getScriptName() {
        return scriptName;
    }

    @Override
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
        this.dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public List<Group> getGroups() {
        if (this.groups == null) {
            this.groups = this.dataLayer.getGroups(this);
        }
        return groups;
    }

    @Override
    public void setGroups(List<Group> groups) {
        this.groups = groups;
        this.dirty = true;
    }

    @Override
    // ask to prof: if i first add a group and then i want to getGroups()? they aren't loaded from DB!!!
    // ask to prof: where are the List<> initialized?
    public void addGroup(Group group) {
        if (this.groups == null) {
            this.getGroups();
        }
        this.groups.add(group);
    }

    @Override
    public void removeGroup(Group group) {
        if (this.groups == null) {
            return;
        }
        this.groups.remove(group);
    }

    @Override
    public void removeAllGroup() {
        this.groups = null;
    }

    @Override
    public void copyFrom(Service service) {
        this.ID = service.getID();
        this.description = service.getDescription();
        this.name = service.getName();
        this.scriptName = service.getScriptName();

        this.groups = null;

        this.dirty = true;
    }

}
