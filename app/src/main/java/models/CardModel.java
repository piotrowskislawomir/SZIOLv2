package models;

/**
 * Created by Michał on 2015-05-10.
 */
public class CardModel {
    private Integer cardId;
    private String firstName;
    private String lastName;

    public CardModel()
    {

    }

    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
