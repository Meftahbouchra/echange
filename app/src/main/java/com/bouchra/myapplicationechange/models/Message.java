package com.bouchra.myapplicationechange.models;

import java.util.Date;

public class Message {
    private String idMessage;
    private String idsender;
    private String idreceiver;

    public Message(String idMessage, String idsender, String idreceiver, Date dateMessage, String textMessage) {
        this.idMessage = idMessage;
        this.idsender = idsender;
        this.idreceiver = idreceiver;
        this.dateMessage = dateMessage;
        this.textMessage = textMessage;
    }

    public String getIdsender() {
        return idsender;
    }

    public void setIdsender(String idsender) {
        this.idsender = idsender;
    }

    public String getIdreceiver() {
        return idreceiver;
    }

    public void setIdreceiver(String idreceiver) {
        this.idreceiver = idreceiver;
    }

    public Message() {
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
