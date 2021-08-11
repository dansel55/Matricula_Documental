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
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Usuario
 */
public interface MatriculaRepository extends MongoRepository<Matricula, Integer> {


    Optional<Matricula> findByCorreoEstudianteAndPeriodo(String correo, String periodo);
    
    Optional<Matricula> findByCodigo(String codigo);
    
    List<Matricula> findByCorreoEstudiante(String correo);
    

}
