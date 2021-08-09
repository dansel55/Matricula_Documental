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

import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 *
 * @author Usuario
 */
@Data
public class Matricula {
    
    private Periodo periodo;
    private EstudianteMatricula estudiante;
    private Date fecha;
    private Integer creditosTotales;
    private List<DetalleMatricula> detalles;
}
