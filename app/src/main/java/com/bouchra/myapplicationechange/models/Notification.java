package com.bouchra.myapplicationechange.models;

import java.util.Date;

public class Notification {
    private String idNotification;
    private Date dateNotification;
    private String idsender;
    private String idreceiver;


    public Notification() {
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

    public Notification(String idNotification, Date dateNotification, String idsender, String idreceiver, String contenuNotification) {
        this.idNotification = idNotification;
        this.dateNotification = dateNotification;
        this.idsender = idsender;
        this.idreceiver = idreceiver;
        this.contenuNotification = contenuNotification;
    }

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public Date getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(Date dateNotification) {
        this.dateNotification = dateNotification;
    }


    public String getContenuNotification() {
        return contenuNotification;
    }

    public void setContenuNotification(String contenuNotification) {
        this.contenuNotification = contenuNotification;
    }


    private String contenuNotification;


}
