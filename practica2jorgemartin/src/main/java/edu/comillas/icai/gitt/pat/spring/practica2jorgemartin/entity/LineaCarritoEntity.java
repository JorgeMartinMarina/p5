package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "lineas_carrito",
        uniqueConstraints = @UniqueConstraint(columnNames = {"carrito_id", "id_articulo"})
)
public class LineaCarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLinea;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private CarritoEntity carrito;

    @Column(name = "id_articulo", nullable = false)
    private Long idArticulo;

    @Column(nullable = false)
    private Long precioUnitario;

    @Column(nullable = false)
    private Long unidades;

    // precioUnitario * unidades
    @Column(nullable = false)
    private Long costeLineaArticulo;

    public Long getIdLinea() {
        return idLinea;
    }

    public CarritoEntity getCarrito() {
        return carrito;
    }

    public void setCarrito(CarritoEntity carrito) {
        this.carrito = carrito;
    }

    public Long getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Long idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Long getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Long precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Long getUnidades() {
        return unidades;
    }

    public void setUnidades(Long unidades) {
        this.unidades = unidades;
    }

    public Long getCosteLineaArticulo() {
        return costeLineaArticulo;
    }

    public void setCosteLineaArticulo(Long costeLineaArticulo) {
        this.costeLineaArticulo = costeLineaArticulo;
    }
}