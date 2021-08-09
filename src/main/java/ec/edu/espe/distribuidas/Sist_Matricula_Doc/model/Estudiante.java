/*
 * Copyright (c) 2021 Usuario.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Usuario - initial API and implementation and/or initial documentation
 */
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Usuario
 */
@Data
@Document(collection="estudiantes")
@CompoundIndexes({
    @CompoundIndex(name = "idxu_compest_tiidid", def = "['tipoIdentificacion': 1, 'identificacion': 1]", unique = true),
    @CompoundIndex(name = "idxu_compest_apenom", def = "['apellido': 1, 'nombre': 1]")
})

public class Estudiante {
    
    @Id
    private String id;    
    private String tipoIdentificacion;    
    private String identificacion;
    private CarreraEstudiante carrera;
    private String nombre;
    @Indexed(name = "idx_estudiante_mail")
    private String correo;
    private String contrasena;
    @Indexed(name = "idx_estudiante_ape")
    private String apellido;
    private String genero;
    private String telefono;
    private String direccion;    
    
}
