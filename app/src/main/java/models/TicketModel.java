package models;

/**
 * Created by Michał on 2015-04-13.
 */
public class TicketModel {

    private Integer id;
    private String title;
    private String description;
    private String status;
    private Integer customerId;
    private Integer executorId;
    private boolean assignToTicket;
    private Integer creatorId;

    public TicketModel()
    {

    }

    public TicketModel(Integer id, String title) {
        this.title = title;
        this.id = id;
    }

    public String toString() {
        return this.title;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Integer executorId) {
        this.executorId = executorId;
    }

    public boolean isAssignToTicket() {
        return assignToTicket;
    }

    public void setAssignToTicket(boolean assignToTicket) {
        this.assignToTicket = assignToTicket;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getAssignToTicket() {
        return assignToTicket;
    }
}
