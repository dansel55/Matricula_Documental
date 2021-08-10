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
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.*;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author bran-
 */
@Service
@Slf4j
public class MatriculaService {
    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final PeriodoRepository periodoRepository;
    //private final DetalleMatriculaRepository detalleMatriculaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository,
            CursoRepository cursoRepository, PeriodoRepository periodoRepository/*,
            DetalleMatriculaRepository detalleMatriculaRepository*/) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.periodoRepository = periodoRepository;
        //this.detalleMatriculaRepository = detalleMatriculaRepository;
    }

    @Transactional
    public List<String> matricularse(MatriculaRQ matriculaRQ) {
        Matricula matricula;
        if (matriculaRQ.) {
            matricula = new Matricula();
        } else {
            Optional<Matricula> matriculaOptional = matriculaRepository.findById(matriculaRQ.getMatricula());
            if (matriculaOptional.isPresent()) {
                matricula = matriculaOptional.get();
            } else {
                matricula = new Matricula();
            }
        }

        List<DetalleMatricula> detalleMatriculas = new ArrayList<>();

        if (matricula.() != null) {
            detalleMatriculas = matricula.getDetalle();
        }

        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(matriculaRQ.getCorreo());
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el estudiante con el correo: " + matriculaRQ.getCorreo());
        }
        Estudiante estudiante = estudianteOpt.get();

        Optional<Periodo> periodo = this.periodoRepository.findById(matriculaRQ.getPeriodo());
        if (periodo.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el periodo con el ID" + matriculaRQ.getPeriodo());
        }
        Integer creditos = 0;
        if (matricula.getCodigo() != null) {
            creditos = matricula.getCreditosTotales().intValue();
        }

        List<String> errorCursos = new ArrayList<>();

        for (Integer crs : matriculaRQ.getCursos()) {
            Optional<Curso> cursoOpt = this.cursoRepository.findById(crs);

            if (cursoOpt.isEmpty()) {
                errorCursos.add("El curso con el ID: " + crs + " no existe");
                continue;
            }
            Curso curso = cursoOpt.get();

            if (matricula.getCodigo() != null) {
                List<DetalleMatricula> cursosRepetidos = matricula.getDetalle().stream()
                        .filter(d -> d.getCurso().getAsignatura().getNombre()
                        .equals(curso.getAsignatura().getNombre())).collect(Collectors.toList());
                if (!cursosRepetidos.isEmpty()) {
                    errorCursos.add("ya esta matriculado en una materia de " + curso.getAsignatura().getNombre());
                    continue;
                }
            }

            if (!curso.getPeriodo().equals(periodo.get())) {
                errorCursos.add("El curso con el NRC: " + curso.getNrc() + " no pertenece al mismo periodo");
                continue;
            }
            if (creditos + curso.getAsignatura().getCreditos() > 50) {
                errorCursos.add("No dispone de los creditos necesarios para matricularse en el curso:"
                        + curso.getNrc());
                log.error("No dispone de los creditos necesarios para matricularse en el curso:" + curso.getNrc());
                continue;
            }
            if (curso.getDisponible() == 0) {
                errorCursos.add("El curso:" + curso.getNrc() + " no cuenta con cupos disponibles");
                log.error("El curso:" + curso.getNrc() + " no cuenta con cupos disponibles");
                continue;
            }

            boolean carrera = false;
            boolean prerequisito = false;
            boolean materiaPasada = false;
            int numeroPre = 0;

            List<DetalleMatricula> materiasEstudiante
                    = this.detalleMatriculaRepository.findByEstudiante(estudiante.getCodigo());

            for (DetalleMatricula detMatricula : materiasEstudiante) {
                if (detMatricula.getCurso().getAsignatura().equals(curso.getAsignatura())) {
                    materiaPasada = true;
                    break;
                }
            }

            if (materiaPasada) {
                errorCursos.add("Materia ya pasada");
                continue;
            }

            for (Prerequisito pre : curso.getAsignatura().getPrerequisitos()) {
                for (DetalleMatricula detMatricula : materiasEstudiante) {
                    if (pre.getPrerequisito().equals(detMatricula.getCurso().getAsignatura())) {
                        numeroPre++;
                        break;
                    }
                }

            }
            if (curso.getAsignatura().getPrerequisitos().size() == numeroPre) {
                prerequisito = true;
            } else {
                log.error("No cumplio con los prerequisitos");
                errorCursos.add("No cumple con los prerequisitos para el curso:" + curso.getNrc());
            }

            for (CarreraCurso carreraCurso : curso.getCarreraCursos()) {
                if (carreraCurso.getCarrera().equals(estudiante.getCarrera())) {
                    carrera = true;
                    break;
                }
            }
            if (!carrera && !curso.getCarreraCursos().isEmpty()) {
                log.error("Carrera no disponible");
                errorCursos.add("Su carrera no esta contemplada en el curso:" + curso.getNrc());
            }

            boolean ver = false;
            if ((curso.getAsignatura().getPrerequisitos().isEmpty() || prerequisito)
                    && (curso.getCarreraCursos().isEmpty() || carrera) && !materiaPasada) {
                for (DetalleMatricula detalleMatricula : detalleMatriculas) {
                    for (Horario horarioActual : curso.getHorarios()) {
                        List<Horario> horariosChoque = detalleMatricula.getCurso().getHorarios()
                                .stream().filter(horario -> horario.getDia().equals(horarioActual.getDia())
                                && horario.getHoraInicio().before(horarioActual.getHoraFin())
                                && horario.getHoraFin().after(horarioActual.getHoraInicio()))
                                .collect(Collectors.toList());
                        if (!horariosChoque.isEmpty()) {
                            log.error("El curso:" + curso.getNrc() + " se choca con uno o mas cursos actuales");
                            errorCursos.add("El curso:" + curso.getNrc() + " se choca con uno o mas cursos actuales");
                            ver = true;
                            break;
                        }
                    }
                    if (ver) {
                        break;
                    }
                }
                if (!ver) {
                    DetalleMatricula detalleMatriculaNueva = new DetalleMatricula();
                    detalleMatriculaNueva.setMatricula(matricula);
                    detalleMatriculaNueva.setCurso(curso);
                    detalleMatriculaNueva.setEstado("CUR");
                    detalleMatriculaNueva.setFecha(new Date());
                    creditos += curso.getAsignatura().getCreditos();
                    curso.setDisponible((short) (curso.getDisponible() - 1));

                    detalleMatriculas.add(detalleMatriculaNueva);
                }

            }
        }

        matricula.setEstudiante(estudiante);

        matricula.setCreditosTotales(BigDecimal.valueOf(creditos));
        matricula.setDetalle(detalleMatriculas);
        matricula.setPeriodo(periodo.get());
        matricula.setFecha(new Date());
        if (detalleMatriculas.size() > 0) {
            this.matriculaRepository.save(matricula);
            return errorCursos;
        } else {
            errorCursos.add("No se logro matricular en ninguna materia");
            throw new MatriculaConflictException("No se logro matricular en ninguna materia", errorCursos);
        }

    }

    public Matricula buscarMatricula(String correo, Integer periodo) {

        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(correo);
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el estudiante con el correo" + correo);
        }
        Estudiante estudiante = estudianteOpt.get();

        Optional<Periodo> per = this.periodoRepository.findById(periodo);
        if (per.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el periodo con el ID" + periodo);
        }

        Optional<Matricula> matriculaOpt = this.matriculaRepository.findByEstudianteAndPeriodo(estudiante, per.get());
        if (matriculaOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro la una matricula en el periodo: " + per.get().getNombre()
                    + " del estudiante " + estudiante.getApellido() + " " + estudiante.getNombre());
        }

        return matriculaOpt.get();
    }

    public void borrarDetalleMatricula(Integer id) {
        Optional<DetalleMatricula> detalleMatriculaOpt = this.detalleMatriculaRepository.findById(id);
        if (detalleMatriculaOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el detalle");
        }
        detalleMatriculaOpt.get().getMatricula().setCreditosTotales(detalleMatriculaOpt.get().getMatricula()
                .getCreditosTotales().subtract(BigDecimal
                        .valueOf(detalleMatriculaOpt.get().getCurso().getAsignatura().getCreditos())));
        detalleMatriculaOpt.get().getCurso().setDisponible((short) (detalleMatriculaOpt.get().getCurso()
                .getDisponible() + 1));
        this.detalleMatriculaRepository.deleteById(id);
    }

    public List<Matricula> obtenerMatriculasPorEstudiante(String correo) {
        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(correo);
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el estudiante con el correo: " + correo);
        }
        Estudiante estudiante = estudianteOpt.get();

        return estudiante.getMatricula();
    }
    
}
