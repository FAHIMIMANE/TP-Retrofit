package com.example.banqueapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface CompteApi {
    @GET("/banque/comptes")
    Call<CompteList> getAllComptesXml(@Header("Accept") String acceptHeader);

    @GET("/banque/comptes")
    Call<List<Compte>> getAllComptesJson(@Header("Accept") String acceptHeader);

    @POST("/banque/comptes")
    Call<Compte> createCompte(@Body Compte compte, @Header("Accept") String acceptHeader);

    @PUT("/banque/comptes/{id}")
    Call<Compte> updateCompte(@Path("id") Long id, @Body Compte compte, @Header("Accept") String acceptHeader);

    @DELETE("/banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id, @Header("Accept") String acceptHeader);
}