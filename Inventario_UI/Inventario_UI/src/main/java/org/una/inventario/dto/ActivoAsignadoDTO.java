package org.una.inventario.dto;


import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ActivoAsignadoDTO {

    private Long id;
    private UsuarioDTO usuario;
    private ActivoDTO activo;
    private String justificacion;
    private boolean estado;
    private Date fechaCreacion;

}
