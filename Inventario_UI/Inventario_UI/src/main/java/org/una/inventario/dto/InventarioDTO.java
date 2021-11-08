package org.una.inventario.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class InventarioDTO {

    private Long id;
    private Long responsable;
    private String descripcion;
    private boolean estado;
    private Date fechaCreacion;

}
