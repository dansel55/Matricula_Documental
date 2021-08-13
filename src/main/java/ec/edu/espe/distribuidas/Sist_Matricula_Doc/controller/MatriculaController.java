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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.MatriculaRQ;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.MatriculaRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.MatriculaConflictException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Matricula;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.service.MatriculaService;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.transform.MatriculaTS;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author bran-
 */
@RestController
@RequestMapping("/v1/matricula/")
@CrossOrigin
@Slf4j
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @PostMapping
    @ApiOperation(value = "Matricular un estudiante en un periodo",
            notes = "Matricula a un estudiante en diferentes materias en un periodo")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Matricula exitosa"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro algun registro"),
        @ApiResponse(code = 409, message = "Conflic - No cumple con las reglas de negocio"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problemas al intentar matricular")})
    public ResponseEntity matricularse(@RequestBody MatriculaRQ matriculaRQ) {
        try {
            return ResponseEntity.ok(this.matriculaService.matricularse(matriculaRQ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MatriculaConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrores());
        } /*catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }*/
    }

    @ApiOperation(value = "Buscar una matricula",
            notes = "Buscar una matricula especifica por correo y periodo")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se encontro el registro"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro una entidad"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problema interno del servidor")})
    @GetMapping
    public ResponseEntity buscarMatricula(@RequestParam String correo, @RequestParam String periodo) {
        try {
            Matricula matricula = this.matriculaService.buscarMatricula(correo, periodo);
            MatriculaRS matriculaRS = MatriculaTS.matriculaRS(matricula);
            return ResponseEntity.ok(matriculaRS);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "{correo}")
    @ApiOperation(value = "Busca matriculas de un estudiante",
            notes = "Busca todas las matriculas que le pertenecen a un estudiante")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se encontraron los registros"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro una entidad"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problemas al intentar matricular")})
    public ResponseEntity buscarMatriculasPorEstudiante(@PathVariable String correo) {
        try {
            List<Matricula> matriculas = this.matriculaService.obtenerMatriculasPorEstudiante(correo);
            List<MatriculaRS> matriculasRS = new ArrayList<>();
            for (Matricula matricula : matriculas) {
                MatriculaRS matriculaRS = MatriculaTS.matriculaRS(matricula);
                matriculasRS.add(matriculaRS);
            }
            return ResponseEntity.ok(matriculasRS);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "{matricula}/{nrc}")
    @ApiOperation(value = "Elimina una materia de la matricula",
            notes = "Elimina una materia especifica de una matricula")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se elimino exitosamente"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro una entidad"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problemas al intentar matricular")})
    public ResponseEntity borarrMatriculaDetalle(@PathVariable String matricula, @PathVariable Integer nrc) {
        try {
            this.matriculaService.borrarDetalleMatricula(matricula, nrc);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
