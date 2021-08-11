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
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.transform;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.PeriodoRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Periodo;

/**
 *
 * @author bran-
 */
public class PeriodoTS {

    public static PeriodoRS periodoRS(Periodo periodo) {
        PeriodoRS rs = PeriodoRS.builder()
                .codigo(periodo.getId())
                .nombre(periodo.getNombre())
                //.estado(periodo.getEstado())
                .build();
        return rs;
    }

}
