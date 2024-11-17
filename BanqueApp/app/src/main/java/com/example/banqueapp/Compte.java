package com.example.banqueapp;

import com.google.gson.annotations.SerializedName;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "compte", strict = false)

public class Compte {
    @Element(name = "idLong", required = false)
    @SerializedName("idLong")
    private Long idLong;

    @Element(name = "solde", required = false)
    @SerializedName("solde")
    private double solde;

    @Element(name = "dateCreation", required = false)
    @SerializedName("dateCreation")
    private String dateCreation;

    @Element(name = "type", required = false)
    @SerializedName("type")
    private String type;


    // Default constructor
    public Compte() {
    }

    // Constructor with arguments
    public Compte(Long idLong, double solde, String dateCreation, String type) {
        this.idLong = idLong;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
    }

    // Getter and Setter for idLong
    public Long getIdLong() {
        return idLong;
    }

    public void setIdLong(Long idLong) {
        this.idLong = idLong;
    }

    // Getter and Setter for solde
    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    // Getter and Setter for dateCreation
    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
