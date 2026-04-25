const API_BASE = "http://localhost:8080/api/carrito";

const USUARIO_PRUEBA = {
  idUsuario: 1,
  correoUsuario: "jorge@prueba.com"
};

const PRODUCTOS = {
  1: { nombre: "Smartphone", precio: 499 },
  2: { nombre: "Auriculares Pro", precio: 129 },
  3: { nombre: "Reloj Inteligente", precio: 199 }
};

function obtenerIdCarrito() {
  return localStorage.getItem("idCarrito");
}

function guardarIdCarrito(idCarrito) {
  localStorage.setItem("idCarrito", String(idCarrito));
}

function borrarIdCarrito() {
  localStorage.removeItem("idCarrito");
}

function formatearPrecio(precio) {
  return `${precio} €`;
}

async function fetchJSON(url, options = {}) {
  const response = await fetch(url, options);

  if (response.status === 204) {
    return null;
  }

  if (!response.ok) {
    let mensaje = `Error ${response.status}`;

    try {
      const error = await response.json();
      if (error.message) {
        mensaje = error.message;
      }
    } catch (_) {}

    throw new Error(mensaje);
  }

  return response.json();
}

async function obtenerCarritoActual() {
  const idCarrito = obtenerIdCarrito();

  if (!idCarrito) {
    return null;
  }

  try {
    return await fetchJSON(`${API_BASE}/${idCarrito}`);
  } catch (error) {
    borrarIdCarrito();
    return null;
  }
}

async function crearCarrito() {
  const carrito = await fetchJSON(API_BASE, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(USUARIO_PRUEBA)
  });

  guardarIdCarrito(carrito.idCarrito);
  return carrito;
}

async function asegurarCarrito() {
  let carrito = await obtenerCarritoActual();

  if (carrito) {
    return carrito;
  }

  carrito = await crearCarrito();
  return carrito;
}

async function anadirProducto(idArticulo, unidades = 1) {
  const producto = PRODUCTOS[idArticulo];

  if (!producto) {
    throw new Error("Producto no encontrado");
  }

  const carrito = await asegurarCarrito();

  return await fetchJSON(`${API_BASE}/${carrito.idCarrito}/lineas`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      idArticulo: idArticulo,
      precioUnitario: producto.precio,
      unidades: unidades
    })
  });
}

async function eliminarLinea(idArticulo) {
  const idCarrito = obtenerIdCarrito();

  if (!idCarrito) {
    return null;
  }

  return await fetchJSON(`${API_BASE}/${idCarrito}/lineas/${idArticulo}`, {
    method: "DELETE"
  });
}

async function vaciarCarrito() {
  const idCarrito = obtenerIdCarrito();

  if (!idCarrito) {
    return;
  }

  const response = await fetch(`${API_BASE}/${idCarrito}`, {
    method: "DELETE"
  });

  if (!response.ok && response.status !== 204) {
    throw new Error("No se pudo vaciar el carrito");
  }

  borrarIdCarrito();
}