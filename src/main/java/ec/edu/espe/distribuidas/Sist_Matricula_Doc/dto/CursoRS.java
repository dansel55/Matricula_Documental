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

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author bran-
 */
@Data
@Builder
public class CursoRS {

    private String codigo;
    private Integer nrc;
    private Integer cupo;
    private String asignatura;
    private Integer disponible;
    private Integer creditos;
    private List<String> carreras;
    private List<HorarioRS> horarios;

}
