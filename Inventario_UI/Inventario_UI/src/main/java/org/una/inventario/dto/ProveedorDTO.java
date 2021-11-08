package org.una.inventario.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProveedorDTO {

    private Long id;
    private String nombre;
    private String notas;
    private String telefono;
    private String correo;
    private Boolean estado;
    private Date fechaCreacion;
    private Date fechaModificacion;

}
