package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
public class CarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private String correoUsuario;

    // Total del carrito (suma de costes de líneas)
    @Column(nullable = false)
    private Long totalPrecio = 0L;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaCarritoEntity> lineas = new ArrayList<>();

    public Long getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Long idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public Long getTotalPrecio() {
        return totalPrecio;
    }

    public void setTotalPrecio(Long totalPrecio) {
        this.totalPrecio = totalPrecio;
    }

    public List<LineaCarritoEntity> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaCarritoEntity> lineas) {
        this.lineas = lineas;
    }
}