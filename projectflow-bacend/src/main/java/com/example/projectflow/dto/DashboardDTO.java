package com.example.projectflow.dto;

public class DashboardDTO {

    private int projetsTermines;
    private int projetsEnCours;
    private int tachesEnRetard;
    private int messagesNonLus;
    private int notificationsNonLues;

    public DashboardDTO() {
    }

    public DashboardDTO(int projetsTermines, int projetsEnCours, int tachesEnRetard, int messagesNonLus, int notificationsNonLues) {
        this.projetsTermines = projetsTermines;
        this.projetsEnCours = projetsEnCours;
        this.tachesEnRetard = tachesEnRetard;
        this.messagesNonLus = messagesNonLus;
        this.notificationsNonLues = notificationsNonLues;
    }

    
    public int getProjetsTermines() { return projetsTermines; }
    public void setProjetsTermines(int projetsTermines) { this.projetsTermines = projetsTermines; }

    public int getProjetsEnCours() { return projetsEnCours; }
    public void setProjetsEnCours(int projetsEnCours) { this.projetsEnCours = projetsEnCours; }

    public int getTachesEnRetard() { return tachesEnRetard; }
    public void setTachesEnRetard(int tachesEnRetard) { this.tachesEnRetard = tachesEnRetard; }

    public int getMessagesNonLus() { return messagesNonLus; }
    public void setMessagesNonLus(int messagesNonLus) { this.messagesNonLus = messagesNonLus; }

    public int getNotificationsNonLues() { return notificationsNonLues; }
    public void setNotificationsNonLues(int notificationsNonLues) { this.notificationsNonLues = notificationsNonLues; }
}
