package com.swivel.org.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keet Sugathadasa
 * @project Test-code
 */
@Getter
public class User {

    private long _id;
    private String url;
    private String external_id;
    private String name;
    private String alias;
    private String created_at;
    private boolean active;
    private boolean verified;
    private boolean shared;
    private String locale;
    private String timezone;
    private String last_login_at;
    private String email;
    private String phone;
    private String signature;
    private long organization_id;
    private String[] tags;
    private boolean suspended;
    private String role;

    private List<String> assigneeTickets = new ArrayList<>();
    private List<String> submittedTickets = new ArrayList<>();

    /**
     * This will set the assigned tickets for this user
     * @param tickets All the tickets in a list
     */
    public void setAssigneeTickets(List<Ticket> tickets) {
        for (Ticket t : tickets
        ) {
            if (t.getAssignee_id() == this._id) {
                this.assigneeTickets.add(t.get_id());
            }
        }
    }

    /**
     * This will set the submitted tickets for this user
     * @param tickets All the tickets in a list
     */
    public void setSubmittedTickets(List<Ticket> tickets) {
        for (Ticket t : tickets
        ) {
            if (t.getSubmitter_id() == this._id) {
                this.submittedTickets.add(t.get_id());
            }
        }
    }


}
