/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ipo.molina.business;

/**
 *
 * @author José Molina Colmenero
 */
public class Producto implements Comparable<Producto> {

    private long codigo;
    private String nombre;
    private FormatoProducto formato;
    private float precio;
    private int stock;

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFormato(FormatoProducto formato) {
        this.formato = formato;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Producto(long codigo, String nombre, FormatoProducto formato, float precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.formato = formato;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public long getCodigo() {
        return codigo;
    }

    public FormatoProducto getFormato() {
        return formato;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            if (obj.getClass() == ProductoCantidad.class) {
                ProductoCantidad p = (ProductoCantidad) obj;
                if (codigo == p.getProducto().getCodigo()) {
                    return true;
                }
                return false;
            }
        }
        final Producto other = (Producto) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Producto o) {
        if (codigo == o.codigo) {
            return 0;
        }
        return codigo < o.codigo ? -1 : 1;
    }
}
