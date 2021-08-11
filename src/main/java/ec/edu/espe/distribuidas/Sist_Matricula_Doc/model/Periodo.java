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
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Usuario
 */
@Data
@Document(collection = "periodos")
public class Periodo {

    @Id
    private String id;
    @Indexed(name = "idx_periodo_nomper")
    private String nombre;
    private Date inicio;
    private Date fin;
    private String estado;
    //private List<Asignatura> asignaturas;

}
