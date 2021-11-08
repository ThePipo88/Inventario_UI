package org.una.inventario.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsuarioDTO {

    private Long id;
    private String nombreCompleto;
    private String cedula;
    private String passwordEncriptado;
    private boolean estado;
    private Date fechaRegistro;
    private Date fechaModificacion;
    private DepartamentoDTO departamento;
    private RolDTO rol;
    private boolean esJefe;

}
