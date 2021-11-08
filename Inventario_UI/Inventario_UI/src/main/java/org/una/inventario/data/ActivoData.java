package org.una.inventario.data;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ActivoData {

    private String marca;

    private String proveedor;

    private String numero;

    private String nota;

    private String telefono;

    private String correo;

    private String proveedorFechaCreacion;

    private String continente;

    private String nombre;

    private String estado;

    private String fechaCreacion;

}
