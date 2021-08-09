package ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstudianteEditarRS {

    private String nombre;
    private String apellido;
    private String correo;
    private String genero;
    private String telefono;
    private String direccion;
}
