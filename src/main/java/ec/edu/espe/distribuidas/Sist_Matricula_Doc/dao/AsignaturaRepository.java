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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Asignatura;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Usuario
 */
public interface AsignaturaRepository extends MongoRepository<Asignatura, Integer> {

    /*List<Asignatura> findByNombreOrderByNombre(String nombre);

    List<Asignatura> findByCreditosGreaterThan(Integer creditos);
    
    List<Asignatura> findByEstadoOrderByNombre(String estado);
    
    List<Asignatura> findByPeriodoId(Integer codigoPeriodo);
    
    List<Prerequisitos> findByPrerequisitosEstado(String estado);*/

    
    Optional<Asignatura> findByNombre(String nombre);

    List<Asignatura> findByDepartamento(String departamento);
    
    Optional<Asignatura> findByCodigo(String codigo);

}
