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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.EstudianteMatricula;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Matricula;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Usuario
 */
public interface MatriculaRepository extends MongoRepository<Matricula, Integer> {

    //List<Matricula> findByFechaOrderByFechaDesc(Date fecha);
    //Optional<Matricula> findByEstudianteAndPeriodo(Estudiante estudiante, Periodo periodo);
    List<Matricula> findByEstudiante(EstudianteMatricula estudiante);

}
