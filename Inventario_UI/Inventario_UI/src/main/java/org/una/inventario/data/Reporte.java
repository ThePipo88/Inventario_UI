package org.una.inventario.data;

import javafx.scene.control.TableColumn;
import lombok.*;

@Data
@AllArgsConstructor
@ToString
@Builder
public class Reporte implements Comparable<Reporte>{

    private final String id;

    private final String nombre;

    private final String fecha;

    public final String estado;

    private final String marca;

    @Override
    public int compareTo(Reporte o) {
        return this.getMarca().compareTo(o.getMarca());
    }
}
