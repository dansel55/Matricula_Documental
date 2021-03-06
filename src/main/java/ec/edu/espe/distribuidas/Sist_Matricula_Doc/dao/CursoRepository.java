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
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Curso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Usuario
 */
public interface CursoRepository extends MongoRepository<Curso, String> {
    
    Optional<Curso> findByNrc(Integer nrc);
    
    Optional<Curso> findByCodigo(String codigo);

    List<Curso> findByCupoGreaterThan(Integer cupo);

    List<Curso> findByAsignaturaAndPeriodoOrderByNrc(String asignatura, String periodo);

    //List<Horario> findByHorariosDiaAndHoraInicioAndHoraFinOrderByDia(String dia, Date horaInicio, Date horaFin);
}
