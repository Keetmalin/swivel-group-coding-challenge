package com.swivel.org.models;

import lombok.Getter;

/**
 * @author Keet Sugathadasa
 * @project Test-code
 */
@Getter
public class Ticket {

    private String _id;
    private String url;
    private String external_id;
    private String created_at;
    private String type;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private long submitter_id;
    private long assignee_id;
    private long organization_id;
    private String[] tags;
    private boolean has_incidents;
    private String due_at;
    private String via;

}
