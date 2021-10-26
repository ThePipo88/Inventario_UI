package org.una.inventario.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContratoGarantiaDTO {

    private Long id;
    private ActivoDTO activo;
    private double montoAsegurado;
    private double costo;
    private boolean estado;
    private String fechaVencimiento;
    private Date fechaCreacion;
    private Date fechaModificacion;

}

