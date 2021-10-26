package org.una.inventario.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ValuacionDTO {
    private Long id;
    private ActivoDTO activo;
    private InventarioDTO inventario;
    private double precioValuacion;
    private Date fechaCreacion;

}
