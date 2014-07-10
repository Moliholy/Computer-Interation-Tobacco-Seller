/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ipo.molina.business;

import java.util.Objects;

/**
 *
 * @author Molina
 */
public class ProductoCantidad {
    private Producto producto;
    private int cantidad;

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public ProductoCantidad(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            if (obj.getClass() == Producto.class) {
                Producto p=(Producto) obj;
                if(p.getCodigo()==producto.getCodigo()) {
                    return true;
                }
            } else {
                return false;
            }
        }
        final ProductoCantidad other = (ProductoCantidad) obj;
        if (!Objects.equals(this.producto, other.producto)) {
            return false;
        }
        return true;
    }
    

}
