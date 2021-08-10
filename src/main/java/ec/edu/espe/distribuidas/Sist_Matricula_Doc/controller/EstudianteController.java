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
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.controller;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.EstudianteEditarRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.EstudianteRQ;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.EstudianteRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Estudiante;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.service.EstudianteService;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.transform.EstudianteTS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author bran-
 */
@RestController
@RequestMapping("/v1/estudiante/")
@CrossOrigin
@Slf4j
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping(value = "{correo}")
    /*@ApiOperation(value = "Busca un estudiante por correo",
            notes = "Devuelve el correo registrado por el estudiante")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se encontraron los registros"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro una entidad")})*/
    public ResponseEntity obtenerDatosEstudiante(@PathVariable String correo) {
        try {
            Estudiante estudiante = this.estudianteService.obtenerEstudanterPorCorreo(correo);
            EstudianteRS estudianteRS = EstudianteTS.estudianteRS(estudiante);
            return ResponseEntity.ok(estudianteRS);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping
    /*@ApiOperation(value = "Realiza el registro de un nuevo estudiante",
            notes = "Realiza la creacion de un nuevo estudiante para una determinada carrera")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se ha creado correctamente"),
        @ApiResponse(code = 404, message = "Not Create - No se puede crear el registro"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problemas al realizar la operacion")})*/
    public ResponseEntity crearEstudiante(@RequestBody EstudianteRQ estudianteRQ) {
        try {
            this.estudianteService.agregarEstudiante(estudianteRQ);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{usuario}")
    /*@ApiOperation(value = "Modifica el registro de un estudiante",
            notes = "Realiza la modificacion de los registros de un estudiante")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se ha modificado correctamente"),
        @ApiResponse(code = 404, message = "Not Modify - No se puede modificar"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problemas al realizar la operacion")})*/
    public ResponseEntity<Estudiante> editarEstudiante(@PathVariable String usuario,
            @RequestBody EstudianteEditarRS estudianteEditarRs) {
        try {
            return ResponseEntity.ok(this.estudianteService.actualizarEstudiante(usuario, estudianteEditarRs));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
