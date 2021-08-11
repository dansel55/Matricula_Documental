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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Usuario
 */
@Data
@Document(collection = "matriculas")
public class Matricula {
    
    @Id
    private String codigo;
    private Periodo periodo;
    private String correoEstudiante;
    private Date fecha;
    private BigDecimal creditosTotales;
    private List<DetalleMatricula> detalles;
}
