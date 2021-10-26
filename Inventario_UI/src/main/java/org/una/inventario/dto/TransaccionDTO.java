package org.una.inventario.dto;

import lombok.*;
import org.una.inventario.dto.UsuarioDTO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransaccionDTO {

    private Long id;
    private String objeto;
    private UsuarioDTO usuarioId;
    private String accion;
    private Date fechaRegistro;
    private Date fechaFinalizado;
}
