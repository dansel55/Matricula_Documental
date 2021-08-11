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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.*;
import lombok.Data;

/**
 *
 * @author bran-
 */
@Data
public class CarreraTS {

    public static CarreraRS carreraTransform(Carrera carrera) {
        CarreraRS carreraRS = CarreraRS.builder()
                .codigo(carrera.getCodigo())
                .nombre(carrera.getModalidad() + " - " + carrera.getNombre())
                .build();
        return carreraRS;
    }

}
