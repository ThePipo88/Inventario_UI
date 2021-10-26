package org.una.inventario.data;

import javafx.scene.control.TableColumn;
import lombok.*;

@Data
@AllArgsConstructor
@ToString
@Builder
public class Reporte{

    private final String id;

    private final String nombre;

    private final String fecha;

    public final String estado;

    private final String marca;

}
