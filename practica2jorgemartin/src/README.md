## Ampliación realizada en esta práctica

En esta segunda parte de la práctica se ha extendido la funcionalidad inicial del carrito incorporando **persistencia**, **capa de servicios** y una nueva estructura de datos basada en **dos entidades relacionadas**: carrito y línea de carrito.

## Nuevas funcionalidades implementadas

### Persistencia en base de datos
Se ha sustituido el almacenamiento en memoria de la versión anterior por persistencia mediante **Spring Data JPA**, permitiendo que la información de los carritos y sus líneas quede almacenada en base de datos.

Para ello se han creado los siguientes repositorios:

- `CarritoRepository`
- `LineaCarritoRepository`

## Nueva capa de servicios
Se ha añadido una capa intermedia de lógica de negocio mediante la clase `ServicioCarrito`, con el objetivo de separar responsabilidades:

- el **controlador** gestiona las peticiones HTTP,
- el **servicio** implementa la lógica de negocio,
- el **repositorio** accede a la base de datos.

Esta capa se encarga de:

- crear carritos,
- consultar carritos,
- listar carritos,
- eliminar carritos,
- añadir líneas de carrito,
- borrar líneas de carrito,
- recalcular automáticamente el precio total del carrito.

## Nuevas entidades
Se han definido dos entidades persistentes:

### `CarritoEntity`
Representa el carrito de compra y contiene:

- `idCarrito`
- `idUsuario`
- `correoUsuario`
- `totalPrecio`

Además, mantiene una relación con sus líneas de carrito mediante una asociación `@OneToMany`.

### `LineaCarritoEntity`
Representa cada artículo incluido en el carrito y contiene:

- `idLinea`
- `idArticulo`
- `precioUnitario`
- `unidades`
- `costeLineaArticulo`

Cada línea pertenece a un único carrito mediante una relación `@ManyToOne`.

También se ha añadido una restricción de unicidad para evitar que un mismo artículo aparezca duplicado dentro del mismo carrito.

## Nuevos endpoints añadidos
Sobre la API anterior, se han incorporado dos nuevos endpoints para gestionar líneas de carrito:

### Añadir línea de carrito
- `POST /api/carrito/{idCarrito}/lineas`

Permite añadir un artículo a un carrito existente.  
Si el artículo ya estaba en el carrito, se actualiza la línea correspondiente acumulando unidades.

### Borrar línea de carrito
- `DELETE /api/carrito/{idCarrito}/lineas/{idArticulo}`

Permite eliminar del carrito la línea asociada a un artículo concreto.

## Nueva lógica de cálculo
Con la incorporación de líneas de carrito, se ha añadido lógica de negocio para calcular importes automáticamente:

- `costeLineaArticulo = precioUnitario × unidades`
- `totalPrecio = suma de todos los costes de línea del carrito`

Cada vez que se añade o elimina una línea, el total del carrito se recalcula automáticamente.

## Nuevos modelos de intercambio
Para adaptar la API a la nueva estructura se han añadido modelos/DTOs específicos:

- `ModeloCarrito`
- `ModeloLineaCarrito`
- `CrearCarritoRequest`
- `CrearLineaCarritoRequest`

Estos modelos permiten separar la representación externa de la API de las entidades persistentes de la base de datos.

## Resultado
La ampliación realizada en esta práctica permite pasar de una API REST básica a una versión más completa y estructurada, incorporando:

- persistencia real,
- separación en capas,
- soporte para múltiples líneas dentro de un carrito,
- y lógica de negocio para el cálculo automático de importes.