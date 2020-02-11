package com.bouchra.myapplicationechange.models;

import java.util.Date;

public class Message {
    private String idMessage;

    public Message() {
    }

    public Message(String idMessage, Date dateMessage, String textMessage) {
        this.idMessage = idMessage;
        this.dateMessage = dateMessage;
        this.textMessage = textMessage;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public Date getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(Date dateMessage) {
        this.dateMessage = dateMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    private Date dateMessage;
    private String textMessage;


}
