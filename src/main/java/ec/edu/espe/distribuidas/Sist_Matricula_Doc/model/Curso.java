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

import java.util.List;
import lombok.Data;

/**
 *
 * @author Usuario
 */
@Data
public class Curso {
    
    private String asignatura;
    private String periodo;
    private Integer nrc;
    private Integer cupo;
    private Integer disponible;
    private List<Horario> horarios;
    private List<String> carreras;
}
