package model;

import java.util.Objects;

//Class definition
public class Event {

    //class attributes
    private int eventId;
    private String sender;
    private String opponent;
    private EventStatus status;
    private String turn;
    private int move;

    //default constructor
    public Event() {
        this.eventId = 0;
        this.sender = "";
        this.opponent = "";
        this.status = null;
        this.turn = "";
        this.move = 0;
    }

    //constructor that sets all attributes
    public Event(int eventId, String sender, String opponent, EventStatus status, String turn, int move) {
        this.eventId = eventId;
        this.sender = sender;
        this.opponent = opponent;
        this.status = status;
        this.turn = turn;
        this.move = move;
    }

    //getters and setters for all attributes
    public int getEventId() {
        return eventId;
    }

    public String getSender() {
        return sender;
    }

    public String getOpponent() {
        return opponent;
    }

    public EventStatus getStatus() {
        return status;
    }

    public String getTurn() {
        return turn;
    }

    public int getMove() {
        return move;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public void setMove(int move) {
        this.move = move;
    }

    //equals() method
    @Override
    public boolean equals(Object o) {
        //check for reference equality
        if (this == o) return true;
        //check for null and class type equality
        if (o == null || getClass() != o.getClass()) return false;
        //cast the object and compare the unique attribute
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId);

    }

    //Enumeration for EventStatus
    public enum EventStatus {
        PENDING,
        DECLINED,
        ACCEPTED,
        PLAYING,
        COMPLETED,
        ABORTED
    }

}
