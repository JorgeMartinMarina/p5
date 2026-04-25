package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model;

import java.util.List;

public record ModeloCarrito(
        long idCarrito,
        long idUsuario,
        String correoUsuario,
        long totalPrecio,
        List<ModeloLineaCarrito> lineas
) {
}