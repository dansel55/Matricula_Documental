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
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.CursoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.PeriodoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.CursoRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Asignatura;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Curso;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Periodo;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.transform.CursoTS;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author bran-
 */
@Service
@Slf4j
public class CursoService {

    private final CursoRepository cursoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final PeriodoRepository periodoRepository;

    public CursoService(CursoRepository cursoRepository, AsignaturaRepository asignaturaRepository,
            PeriodoRepository periodoRepository) {
        this.cursoRepository = cursoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.periodoRepository = periodoRepository;
    }

    public List<CursoRS> obtenerCursos(String codigoAsignatura, String codigoPeriodo) {
        Optional<Asignatura> asignatura = this.asignaturaRepository.findByCodigo(codigoAsignatura);

        if (asignatura.isEmpty()) {
            throw new EntityNotFoundException("No se encontro la asignatura");
        }
        Optional<Periodo> periodo = this.periodoRepository.findById(codigoPeriodo);
        if (periodo.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el periodo");
        }
        List<CursoRS> cursosRS = new ArrayList<>();
        List<Curso> curso = this.cursoRepository.findByAsignaturaAndPeriodoOrderByNrc(asignatura.get().getNombre(), periodo.get().getNombre());
        for (Curso c : curso) {
            cursosRS.add(CursoTS.cursoRS(c));
        }
        return cursosRS;
    }

    public CursoRS buscarPorNrc(Integer nrc) {
        Optional<Curso> curso = this.cursoRepository.findByNrc(nrc);
        if (curso.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el nrc: " + nrc);
        }
        CursoRS cursoRS = CursoTS.cursoRS(curso.get());
        return cursoRS;
    }

}
