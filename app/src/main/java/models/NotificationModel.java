package models;

import java.io.Serializable;

/**
 * Created by Michał on 2015-05-20.
 */
public class NotificationModel implements Serializable {
    private String type;
    private Integer id;
    private String title;
    private String description;
    private Integer ticketId;

    public NotificationModel()
    {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id= id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }
}
