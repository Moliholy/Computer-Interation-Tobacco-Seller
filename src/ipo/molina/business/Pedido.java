/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ipo.molina.business;

import java.util.ArrayList;

/**
 *
 * @author José Molina Colmenero
 */
public class Pedido {
    
    private ArrayList<ProductoCantidad> productos;
    
    public Pedido() {
        productos = new ArrayList<>();
    }
    
    public void eliminar(int index) {
        if (index < productos.size()) {
            productos.remove(index);
        }
    }
    
    public void insertar(Producto p, int cantidad) {
        if (!productos.contains(p)) {
            ProductoCantidad producto = new ProductoCantidad(p, cantidad);
            productos.add(producto);
        } else {
            //y si ya está en el pedido, lo que vamos a hacer es incrementar su cantidad
            int index = productos.indexOf(p);
            ProductoCantidad pc = productos.get(index);
            pc.setCantidad(pc.getCantidad() + cantidad);
        }
    }
    
    public int numProductos() {
        return productos.size();
    }
    
    public ProductoCantidad getProductoPedido(String codigo) {
        Long cod;
        try {
            cod = Long.parseLong(codigo);
        } catch (NumberFormatException e) {
            return null;
        }
        for (int i = 0; i < productos.size(); i++) {
            ProductoCantidad pc = productos.get(i);
            if (pc.getProducto().getCodigo() == cod) {
                return pc;
            }
        }
        return null;
    }
    
    public ArrayList<ProductoCantidad> getProductos() {
        return productos;
    }
    
    public boolean setCantidad(Producto p, int cantidad) {
        int index = productos.indexOf(p);
        if (index != -1) {
            //entonces está en el vector, por lo que podemos modificar la cantidad
            productos.get(index).setCantidad(cantidad);
            return true;
        }
        return false;
    }
    
    public boolean eliminar(Producto p) {
        int index = productos.indexOf(p);
        if (index != -1) {
            productos.remove(index);
            return true;
        }
        return false;
    }
    
    public boolean pedidoVacio() {
        return productos.isEmpty();
    }
    
    public float calcularPrecio() {
        float precio = 0f;
        for (int i = 0; i < productos.size(); i++) {
            ProductoCantidad p = productos.get(i);
            precio += p.getCantidad() * p.getProducto().getPrecio();
        }
        return precio;
    }
}
