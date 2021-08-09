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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.DepartamentoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Departamento;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author bran-
 */
@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<Departamento> obtenerDepartamentos() {
        List<Departamento> departamentos = this.departamentoRepository.findAll();
        return departamentos;
    }

}
