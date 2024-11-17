package com.example.banqueapp;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "comptes")
public class CompteList {
    @ElementList(inline = true, required = false)
    private List<Compte> comptes;

    public List<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
    }
    public CompteList() {
        // Required empty constructor
    }

    public CompteList(List<Compte> comptes) {
        this.comptes = comptes;
    }

   /* public List<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
    }*/
}