/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ipo.molina.controller;

import ipo.molina.business.FormatoProducto;
import ipo.molina.business.Pedido;
import ipo.molina.business.Producto;
import ipo.molina.business.ProductoCantidad;
import ipo.molina.view.MainWindow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author José Molina Colmenero
 */
public class Controlador {

    private final String RUTA_CARGADO1 = "ipo/molina/controller/precios.csv";
    private final String RUTA_CARGADO2 = "src/ipo/molina/controller/precios.csv";
    private final String RUTA_CARGADO3 = "precios.csv";
    private MainWindow vista;
    private Pedido pedidoActual;
    private HashMap<Long, Producto> productos;
    private float pagado;
    private ArrayList<Pedido> ventas;

    public Controlador() {
        vista = new MainWindow(this);
        pedidoActual = null;
        productos = new HashMap<>();
        pagado = 0f;
        ventas = new ArrayList<>();
        cargaPorDefecto();
    }

    private void cargaPorDefecto() {
        File f1 = new File(RUTA_CARGADO1), f2 = new File(RUTA_CARGADO2), f3 = new File(RUTA_CARGADO3);
        try {
            if (f1.exists()) {
                cargarDesdeCSV(f1.getAbsolutePath());
            } else if (f2.exists()) {
                cargarDesdeCSV(f2.getAbsolutePath());
            } else if (f3.exists()) {
                // System.out.println("Cargando desde el fichero en la ruta "+f3.getAbsolutePath());
                cargarDesdeCSV(f3.getAbsolutePath());
            } else {
                //asumimos que cargamos desde un .jar e intentamos leer el fichero precios.csv de dentro de jar
                // System.out.println("Cargando desde el .jar");
                cargarDesdeJAR();
            }
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarDesdeJAR() {
        InputStream ie = getClass().getClassLoader().getResourceAsStream(RUTA_CARGADO1);
        InputStreamReader ier = new InputStreamReader(ie);
        try {
            BufferedReader br = new BufferedReader(ier);
            String cad = br.readLine();
            while (cad != null) {
                String[] datos = cad.split(";");
                introducirProducto(datos[0], datos[1], datos[2], datos[3], datos[4]);
                cad = br.readLine();
            }
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<Long, Producto> getProductos() {
        return productos;
    }

    public int getNumeroProductosEnPedido() {
        if (pedidoActual != null) {
            return pedidoActual.numProductos();
        } else {
            return 0;
        }
    }

    public void setPagado(float pagado) {
        this.pagado = pagado;
    }

    public void iniciarVista() {
        vista.setVisible(true);
    }

    public void introducirProducto(String _codigo, String _nombre, String _formato, String _precio, String _stock) throws Exception {
        Long codigo = Long.parseLong(_codigo);
        String nombre = _nombre;
        FormatoProducto formato = FormatoProducto.valueOf(_formato.toUpperCase());
        float precio = Float.parseFloat(_precio);
        int stock = Integer.parseInt(_stock);
        introducirProducto(codigo, nombre, formato, precio, stock);
    }

    private void introducirProducto(Long codigo, String nombre, FormatoProducto formato, float precio, int stock) {
        Producto p = new Producto(codigo, nombre, formato, precio, stock);
        productos.put(codigo, p);
    }

    public boolean cargarDesdeCSV(String path) throws IOException {
        File archivo = new File(path);
        if (archivo.exists() && !archivo.isDirectory() && path.endsWith(".csv")) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String cad = br.readLine();
                while (cad != null) {
                    String[] datos = cad.split(";");
                    introducirProducto(datos[0], datos[1], datos[2], datos[3], datos[4]);
                    cad = br.readLine();
                }
            } catch (Exception ex) {
                Logger.getLogger(Controlador.class
                        .getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean eliminarProducto(Producto p) {
        Producto eliminado = productos.remove(p.getCodigo());
        return eliminado != null ? true : false;
    }
    
    public boolean guardarInventarioProductos(){
        return guardarInventarioProductos(RUTA_CARGADO3);
    }

    public boolean guardarInventarioProductos(String path) {
        File file = new File(path);
        if (file.getAbsolutePath().endsWith(".csv")) {
            ArrayList<Producto> arrayProductos = new ArrayList<>();
            arrayProductos.addAll(productos.values());
            Collections.sort(arrayProductos);
            try (FileWriter fw = new FileWriter(file)) {
                Iterator<Producto> it = arrayProductos.iterator();
                while (it.hasNext()) {
                    Producto p = it.next();
                    //ya tenemos el producto, ahora toca parsear los datos y meterlos en el fichero
                    fw.write(p.getCodigo() + ";"
                            + p.getNombre() + ";"
                            + p.getFormato().toString() + ";"
                            + p.getPrecio() + ";"
                            + p.getStock() + "\n");


                }
            } catch (IOException ex) {
                Logger.getLogger(Controlador.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public ProductoCantidad buscarProductoEnPedido(String codigo) {
        return pedidoActual.getProductoPedido(codigo);
    }

    public Producto buscarProducto(String codigo) {
        Long cod;
        try {
            cod = Long.parseLong(codigo);
        } catch (NumberFormatException e) {
            return null;
        }
        return productos.get(cod);
    }

    public boolean agregarProductoAPedido(String codigo) {
        Producto prod = buscarProducto(codigo);
        if (prod != null) {
            if (pedidoActual == null) {
                pedidoActual = new Pedido();
            }
            pedidoActual.insertar(prod, 1);
            return true;
        }
        return false;
    }

    public float getCantidadDevolver() {
        if (pedidoActual == null) {
            return 0;
        }
        return pedidoActual.calcularPrecio() - pagado;
    }

    public float calcularTotalVentas() {
        float total = 0;
        for (int i = 0; i < ventas.size(); i++) {
            total += ventas.get(i).calcularPrecio();
        }
        return total;
    }

    public void reiniciar() {
        pagado = 0;
        pedidoActual = null;
        vista.reiniciar();
    }

    public boolean comprobarAgotamientoStock() {
        if (pedidoActual != null) {
            ProductoCantidad pc;
            for (int i = 0; i < pedidoActual.numProductos(); i++) {
                pc = pedidoActual.getProductos().get(i);
                Producto actual = pc.getProducto();
                if (actual.getStock() - pc.getCantidad() < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reducirStock() {
        if (pedidoActual != null) {
            ProductoCantidad pc;
            for (int i = 0; i < pedidoActual.numProductos(); i++) {
                pc = pedidoActual.getProductos().get(i);
                Producto actual = pc.getProducto();
                actual.setStock(actual.getStock() - pc.getCantidad());
            }
        }
    }

    public boolean confirmarVenta() {
        if (pedidoActual == null) {
            return false;
        }
        /*
         float costePedido = pedidoActual.calcularPrecio();
         if (pagado < costePedido) {
         return false;
         }
         */
        //como existe un pedido, y además está pagado, lo introducimos en las ventas
        if (comprobarAgotamientoStock()) {
            ventas.add(pedidoActual);
            reducirStock();
            //y ahora reiniciamos los atributos
            reiniciar();
            return true;
        }
        return false;
    }

    public boolean setStockProducto(Producto producto, int cantidad) {
        if (producto != null) {
            producto.setStock(cantidad);
            return true;
        }
        return false;
    }

    public boolean setCantidadProductoPedido(String codigo, int cantidad) {
        if (pedidoActual != null) {
            Producto prod = buscarProducto(codigo);
            return pedidoActual.setCantidad(prod, cantidad);
        }
        return false;
    }

    public static void main(String[] ags) {
        //primero establecemos el look and feel
        //tiene que ser antes de hacer cualquier otra cosa, sino no funciona
        String so = System.getProperty("os.name");
        if (so.contains("Windows")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            }
        }
        Controlador controlador = new Controlador();
        //TODO poner el LookAndFeel para tener apariencia windows
        controlador.iniciarVista();
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public float getPagado() {
        return pagado;
    }

    public void modificarPago(float cantidad) {
        pagado = Math.max(pagado + cantidad, 0f);
    }

    public boolean pagoRealizado() {
        float precioPedido = pedidoActual.calcularPrecio();
        return pagado - precioPedido >= 0;
    }

    public void setValoresProducto(Long codigo, String nombre, FormatoProducto formato, Float precio, Integer stock) {
        Producto p = buscarProducto(codigo.toString());
        p.setNombre(nombre);
        p.setFormato(formato);
        p.setPrecio(precio);
        p.setStock(stock);
    }
}
