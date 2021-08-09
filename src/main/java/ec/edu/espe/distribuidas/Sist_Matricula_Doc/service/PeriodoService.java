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
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.service;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.PeriodoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Periodo;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author bran-
 */
@Service
public class PeriodoService {

    private final PeriodoRepository periodoRepository;

    public PeriodoService(PeriodoRepository periodoRepository) {
        this.periodoRepository = periodoRepository;
    }

    public List<Periodo> obtenerTodosPeriodos() {
        List<Periodo> periodos = this.periodoRepository.findAll();
        return periodos;
    }

}
