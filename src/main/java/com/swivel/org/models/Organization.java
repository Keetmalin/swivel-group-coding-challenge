package com.swivel.org.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keet Sugathadasa
 * @project Test-code
 */
@Getter
public class Organization {

    long _id;
    String url;
    String name;
    String[] domain_names;
    String created_at;
    String details;
    boolean shared_tickets;
    String[] tags;

    List<String> users = new ArrayList<>();
    List<String> tickets = new ArrayList<>();

    /**
     * This method will detext the users associated with the organization
     *
     * @param userObjects All Users List
     */
    public void setUsers(List<User> userObjects) {
        for (User u : userObjects) {
            if (u.getOrganization_id() == this._id) {
                users.add(String.valueOf(u.get_id()));
            }
        }
    }

    /**
     * This method will detect the tickets associated with the organization
     *
     * @param ticketObjects All the tickets list
     */
    public void setTickets(List<Ticket> ticketObjects) {
        for (Ticket t : ticketObjects) {
            if (t.getOrganization_id() == this._id) {
                users.add(String.valueOf(t.get_id()));
            }
        }
    }
}
