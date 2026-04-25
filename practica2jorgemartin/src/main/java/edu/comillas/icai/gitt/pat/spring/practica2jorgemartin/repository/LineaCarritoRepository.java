package edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.repository;

import edu.comillas.icai.gitt.pat.spring.practica2jorgemartin.entity.LineaCarritoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LineaCarritoRepository extends CrudRepository<LineaCarritoEntity, Long> {

    Optional<LineaCarritoEntity> findByCarrito_IdCarritoAndIdArticulo(Long idCarrito, Long idArticulo);

    boolean existsByCarrito_IdCarritoAndIdArticulo(Long idCarrito, Long idArticulo);
}