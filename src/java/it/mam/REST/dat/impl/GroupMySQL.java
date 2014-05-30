package it.mam.REST.dat.impl;

import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.RESTDataLayer;
import it.mam.REST.data.model.Service;
import it.mam.REST.data.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alex
 */
public class GroupMySQL implements Group {

    private int ID;
    private String name;
    private String description;
    protected boolean dirty;

    protected RESTDataLayer dataLayer;

    List<User> users;
    List<Service> services;

    public GroupMySQL(RESTDataLayer dataLayer) {

        this.ID = 0;
        this.name = "";
        this.description = "";
        this.dirty = false;

        this.dataLayer = dataLayer;

        this.users = null;
        this.services = null;
    }

    public GroupMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        this.ID = rs.getInt("ID");
        this.name = rs.getString("name");
        this.description = rs.getString("desription");
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
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public List<User> getUsers() {
        if (this.users == null) {
            this.users = this.dataLayer.getUsers(this);
        }
        return users;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
        this.dirty = true;
    }

    @Override
    public List<Service> getServices() {
        if (this.services == null) {
            this.services = this.dataLayer.getServices(this);
        }
        return services;
    }

    @Override
    public void setServices(List<Service> services) {
        this.services = services;
        this.dirty = true;
    }

    @Override
    public void addUser(User user) {
        if (this.users == null) {
            this.getUsers();
        }
        this.users.add(user);
    }

    @Override
    public void removeUser(User user) {
        if (this.users == null) {
            return;
        }
        this.users.remove(user);
    }

    @Override
    public void removeAllUsers() {
        this.users = null;
    }

    @Override
    public void addService(Service service) {
        if (this.services == null) {
            this.getServices();
        }
        this.services.add(service);
    }

    @Override
    public void removeService(Service service) {
        if (this.services == null) {
            return;
        }
        this.services.remove(service);
    }

    @Override
    public void removeAllServices() {
        this.services = null;
    }

    @Override
    public void copyFrom(Group group) {
        this.ID = group.getID();
        this.description = group.getDescription();
        this.name = group.getName();

        this.services = null;
        this.users = null;

        this.dirty = true;
    }

}
