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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.AsignaturaRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.DepartamentoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.PeriodoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Asignatura;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Departamento;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Periodo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
@Slf4j
public class AsignaturaService {

    private final PeriodoRepository periodoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final AsignaturaRepository asignaturaRepository;

    public AsignaturaService(PeriodoRepository periodoRepository, DepartamentoRepository departamentoRepository,
            AsignaturaRepository asignaturaRepository) {
        this.periodoRepository = periodoRepository;
        this.departamentoRepository = departamentoRepository;
        this.asignaturaRepository = asignaturaRepository;
    }

    public List<Asignatura> obtenerAsignaturas(String codigoDepartamento, String codigoPeriodo) {
        Optional<Periodo> periodo
                = this.periodoRepository.findById(codigoPeriodo);
        if (periodo.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el asignaturas en este periodo");
        }
        Optional<Departamento> departamento = this.departamentoRepository.findById(codigoDepartamento);

        if (departamento.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el departamento");
        }
        log.info("departamento:{}", departamento.get().getNombre());
        List<Asignatura> asignaturas = new ArrayList<>();
        List<Asignatura> asignaturaPeriodo
                = this.asignaturaRepository.findByDepartamento(departamento.get().getNombre());
        log.info("paso:{}", asignaturaPeriodo.get(0).getNombre());
        for (Asignatura asig : asignaturaPeriodo) {
            log.info("paso 2");
            for (String per : asig.getPeriodos()) {
                log.info("Periodo asignatua:{}", per);
                if (per.equals(periodo.get().getNombre())) {
                    asignaturas.add(asig);
                }
            }
        }
        return asignaturas;
    }

}
