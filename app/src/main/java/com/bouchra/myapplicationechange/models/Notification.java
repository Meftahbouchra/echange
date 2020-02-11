package com.bouchra.myapplicationechange.models;

import java.util.Date;

public class Notification {
    private String idNotification;
    private Date dateNotification;
    private int code;

    public Notification() {
    }

    public Notification(String idNotification, Date dateNotification, int code, String titreNotification, String contenuNotification) {
        this.idNotification = idNotification;
        this.dateNotification = dateNotification;
        this.code = code;
        this.titreNotification = titreNotification;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitreNotification() {
        return titreNotification;
    }

    public void setTitreNotification(String titreNotification) {
        this.titreNotification = titreNotification;
    }

    public String getContenuNotification() {
        return contenuNotification;
    }

    public void setContenuNotification(String contenuNotification) {
        this.contenuNotification = contenuNotification;
    }

    private String titreNotification;
    private String contenuNotification;


}
