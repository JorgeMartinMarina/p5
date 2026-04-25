package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.service;

import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.entity.CarritoEntity;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.entity.LineaCarritoEntity;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.CrearCarritoRequest;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.CrearLineaCarritoRequest;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.ModeloCarrito;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.ModeloLineaCarrito;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.repository.CarritoRepository;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.repository.LineaCarritoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ServicioCarrito {

    private final CarritoRepository carritoRepository;
    private final LineaCarritoRepository lineaCarritoRepository;

    public ServicioCarrito(CarritoRepository carritoRepository,
                           LineaCarritoRepository lineaCarritoRepository) {
        this.carritoRepository = carritoRepository;
        this.lineaCarritoRepository = lineaCarritoRepository;
    }

    public ModeloCarrito crearCarrito(CrearCarritoRequest request) {
        CarritoEntity carrito = new CarritoEntity();
        carrito.setIdUsuario(request.idUsuario());
        carrito.setCorreoUsuario(request.correoUsuario());
        carrito.setTotalPrecio(0L);

        CarritoEntity guardado = carritoRepository.save(carrito);
        return toModel(guardado);
    }

    public ModeloCarrito buscarCarrito(long idCarrito) {
        CarritoEntity carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
        return toModel(carrito);
    }

    public List<ModeloCarrito> listarCarritos() {
        return StreamSupport.stream(carritoRepository.findAll().spliterator(), false)
                .map(this::toModel)
                .toList();
    }

    public void borrarCarrito(long idCarrito) {
        if (!carritoRepository.existsById(idCarrito)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        carritoRepository.deleteById(idCarrito);
    }

    public ModeloCarrito anadirLinea(long idCarrito, CrearLineaCarritoRequest request) {
        CarritoEntity carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        if (request.unidades() <= 0 || request.precioUnitario() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos de línea no válidos");
        }

        LineaCarritoEntity linea = lineaCarritoRepository
                .findByCarrito_IdCarritoAndIdArticulo(idCarrito, request.idArticulo())
                .orElse(null);

        if (linea == null) {
            linea = new LineaCarritoEntity();
            linea.setCarrito(carrito);
            linea.setIdArticulo(request.idArticulo());
            linea.setPrecioUnitario(request.precioUnitario());
            linea.setUnidades(request.unidades());
        } else {
            linea.setUnidades(linea.getUnidades() + request.unidades());
            linea.setPrecioUnitario(request.precioUnitario());
        }

        linea.setCosteLineaArticulo(linea.getPrecioUnitario() * linea.getUnidades());
        lineaCarritoRepository.save(linea);

        recalcularTotal(carrito);
        carritoRepository.save(carrito);

        return toModel(carritoRepository.findById(idCarrito).orElseThrow());
    }

    public ModeloCarrito borrarLinea(long idCarrito, long idArticulo) {
        CarritoEntity carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        LineaCarritoEntity linea = lineaCarritoRepository
                .findByCarrito_IdCarritoAndIdArticulo(idCarrito, idArticulo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea no encontrada"));

        lineaCarritoRepository.delete(linea);

        recalcularTotal(carrito);
        carritoRepository.save(carrito);

        return toModel(carritoRepository.findById(idCarrito).orElseThrow());
    }

    private void recalcularTotal(CarritoEntity carrito) {
        CarritoEntity carritoActualizado = carritoRepository.findById(carrito.getIdCarrito())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        long total = carritoActualizado.getLineas().stream()
                .mapToLong(LineaCarritoEntity::getCosteLineaArticulo)
                .sum();

        carritoActualizado.setTotalPrecio(total);
        carritoRepository.save(carritoActualizado);
    }

    private ModeloCarrito toModel(CarritoEntity entity) {
        List<ModeloLineaCarrito> lineas = entity.getLineas().stream()
                .map(linea -> new ModeloLineaCarrito(
                        linea.getIdArticulo(),
                        linea.getPrecioUnitario(),
                        linea.getUnidades(),
                        linea.getCosteLineaArticulo()
                ))
                .toList();

        return new ModeloCarrito(
                entity.getIdCarrito(),
                entity.getIdUsuario(),
                entity.getCorreoUsuario(),
                entity.getTotalPrecio(),
                lineas
        );
    }
}