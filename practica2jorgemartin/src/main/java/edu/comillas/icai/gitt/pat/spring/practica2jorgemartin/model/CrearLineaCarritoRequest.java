package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model;

public record CrearLineaCarritoRequest(
        long idArticulo,
        long precioUnitario,
        long unidades
) {
}