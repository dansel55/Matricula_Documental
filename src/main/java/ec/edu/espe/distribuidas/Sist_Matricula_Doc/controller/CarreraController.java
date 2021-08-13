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

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.service.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.transform.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author bran-
 */
@RestController
@RequestMapping("/v1/carrera")
@CrossOrigin
public class CarreraController {

    public final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    @ApiOperation(value = "Busca las carreras disponibles",
            notes = "Devuelve todas las carreras disponibles para el estudiante")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Se encontraron los registros"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro una entidad"),
        @ApiResponse(code = 500, message = "Internal Server Error - Problemas al realizar la busqueda")})
    private ResponseEntity obtenerCarreras() {
        try {
            List<Carrera> carreras = this.carreraService.obtenerTodasCarreras();
            List<CarreraRS> carrerasRS = new ArrayList<>();
            for (Carrera carrera : carreras) {
                CarreraRS carreraRS = CarreraTS.carreraTransform(carrera);
                carrerasRS.add(carreraRS);
            }
            return ResponseEntity.ok(carrerasRS);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
