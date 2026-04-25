package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.controller;

import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.CrearCarritoRequest;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.CrearLineaCarritoRequest;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.model.ModeloCarrito;
import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.service.ServicioCarrito;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/api/carrito")
public class ControladorCarrito {

    private final ServicioCarrito servicioCarrito;

    public ControladorCarrito(ServicioCarrito servicioCarrito) {
        this.servicioCarrito = servicioCarrito;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModeloCarrito crear(@RequestBody CrearCarritoRequest request) {
        return servicioCarrito.crearCarrito(request);
    }

    @GetMapping("/{idCarrito}")
    public ModeloCarrito buscar(@PathVariable long idCarrito) {
        return servicioCarrito.buscarCarrito(idCarrito);
    }

    @GetMapping
    public List<ModeloCarrito> listar() {
        return servicioCarrito.listarCarritos();
    }

    @DeleteMapping("/{idCarrito}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrar(@PathVariable long idCarrito) {
        servicioCarrito.borrarCarrito(idCarrito);
    }

    @PostMapping("/{idCarrito}/lineas")
    @ResponseStatus(HttpStatus.CREATED)
    public ModeloCarrito anadirLinea(@PathVariable long idCarrito,
                                     @RequestBody CrearLineaCarritoRequest request) {
        return servicioCarrito.anadirLinea(idCarrito, request);
    }

    @DeleteMapping("/{idCarrito}/lineas/{idArticulo}")
    public ModeloCarrito borrarLinea(@PathVariable long idCarrito,
                                     @PathVariable long idArticulo) {
        return servicioCarrito.borrarLinea(idCarrito, idArticulo);
    }
}