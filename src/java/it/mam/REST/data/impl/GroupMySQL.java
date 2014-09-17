package it.mam.REST.data.impl;

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

    private List<User> users;
    private List<Service> services;

    public GroupMySQL(RESTDataLayer dataLayer) {

        ID = 0;
        name = "";
        description = "";
        dirty = false;

        this.dataLayer = dataLayer;

        users = null;
        services = null;
    }

    public GroupMySQL(RESTDataLayer dataLayer, ResultSet rs) throws SQLException {

        this(dataLayer);
        ID = rs.getInt("ID");
        name = rs.getString("name");
        description = rs.getString("description");
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
        dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        dirty = true;
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
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        return users;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
        dirty = true;
    }

    @Override
    public List<Service> getServices() {
        if (services == null) {
            services = dataLayer.getServices(this);
        }
        return services;
    }

    @Override
    public void setServices(List<Service> services) {
        this.services = services;
        dirty = true;
    }

    @Override
    public void addUser(User user) {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        users.add(user);
        dirty = true;
    }

    @Override
    public void removeUser(User user) {
        if (users == null) {
            users = dataLayer.getUsers(this);
        }
        users.remove(user);
        dirty = true;
    }

    @Override
    public void removeAllUsers() {
        users = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    public void addService(Service service) {
        if (services == null) {
            services = dataLayer.getServices(this);
        }
        services.add(service);
        dirty = true;
    }

    @Override
    public void removeService(Service service) {
        if (services == null) {
            services = dataLayer.getServices(this);
        }
        services.remove(service);
        dirty = true;
    }

    @Override
    public void removeAllServices() {
        services = null;
        dirty = true;
        /**
         * <qui dobbiamo eliminare anche dal DB? oppure è meglio che si faccia
         * al momento della store?>
         */
    }

    @Override
    public void copyFrom(Group group) {
        ID = group.getID();
        description = group.getDescription();
        name = group.getName();

        services = null;
        users = null;

        dirty = true;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "Description: " + description + "\n"
                + "Name: " + name + "\n"
                + "Dirty: " + dirty + "\n"
                + "Services: " + services + "\n"
                + "Users: " + users;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // se hanno lo stesso riferimento restituisco true
            return true;
        }
        if (obj == null || !(obj instanceof Group)) { // se non sono dello stesso "tipo" restituisco false
            return false;
        }
        // vuol dire che obj è di tipo Group quindi posso fare il cast
        Group g = (Group) obj;
        return ID == g.getID();
    }

}
