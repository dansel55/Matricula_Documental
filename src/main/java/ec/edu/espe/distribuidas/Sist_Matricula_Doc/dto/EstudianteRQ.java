/*
 * Copyright (c) 2021 bran-.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bran- - initial API and implementation and/or initial documentation
 */
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.CarreraEstudiante;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author bran-
 */
@Data
@Builder
public class EstudianteRQ {

    private String tipo;  
    private String identificacion;
    private String nombre;
    private String apellido;  
    private String correo; 
    private String contrasena;  
    private String genero;
    private String telefono; 
    private String direccion;
    private String carrera;
    //private CarreraEstudiante carrera;
    
}
