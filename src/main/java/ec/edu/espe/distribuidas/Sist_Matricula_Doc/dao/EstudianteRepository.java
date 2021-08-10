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
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Estudiante;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author bran-
 */
public interface EstudianteRepository extends MongoRepository<Estudiante, String> {

    Optional<Estudiante> findByCorreo(String correo);

    List<Estudiante> findByCorreoOrIdentificacion(String correo, String identificacion);

}
