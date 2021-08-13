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
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.EstudianteRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.MatriculaRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.PeriodoRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.MatriculaRQ;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.MatriculaConflictException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Asignatura;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Curso;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.DetalleMatricula;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Estudiante;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Horario;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Matricula;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Periodo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final AsignaturaRepository asignaturaRepository;
    //private final DetalleMatriculaRepository detalleMatriculaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository,
            CursoRepository cursoRepository, PeriodoRepository periodoRepository/*,
            DetalleMatriculaRepository detalleMatriculaRepository*/, AsignaturaRepository asignaturaRepository) {

        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.periodoRepository = periodoRepository;
        this.asignaturaRepository = asignaturaRepository;
        //this.detalleMatriculaRepository = detalleMatriculaRepository;
    }

    public List<String> matricularse(MatriculaRQ matriculaRQ) {
        Matricula matricula;
        if (matriculaRQ.getMatricula() == null) {
            matricula = new Matricula();
        } else {
            Optional<Matricula> matriculaOptional = matriculaRepository.findByCodigo(matriculaRQ.getMatricula());
            if (matriculaOptional.isPresent()) {
                matricula = matriculaOptional.get();
            } else {
                matricula = new Matricula();
            }
        }

        List<DetalleMatricula> detalleMatriculas = new ArrayList<>();

        if (matricula.getCodigo() != null) {
            detalleMatriculas = matricula.getDetalles();
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
        BigDecimal creditos = new BigDecimal(0);
        if (matricula.getCodigo() != null) {
            creditos = matricula.getCreditosTotales();
        }

        List<String> errorCursos = new ArrayList<>();

        for (String crs : matriculaRQ.getCursos()) {
            Optional<Curso> cursoOpt = this.cursoRepository.findByCodigo(crs);

            if (cursoOpt.isEmpty()) {
                errorCursos.add("El curso con el ID: " + crs + " no existe");
                continue;
            }
            Curso curso = cursoOpt.get();

            if (matricula.getCodigo() != null) {
                List<DetalleMatricula> cursosRepetidos = matricula.getDetalles().stream()
                        .filter(d -> d.getMateria()
                        .equals(curso.getAsignatura())).collect(Collectors.toList());
                if (!cursosRepetidos.isEmpty()) {
                    errorCursos.add("ya esta matriculado en una materia de " + curso.getAsignatura());
                    continue;
                }
            }

            if (!curso.getPeriodo().equals(periodo.get().getNombre())) {
                errorCursos.add("El curso con el NRC: " + curso.getNrc() + " no pertenece al mismo periodo");
                continue;
            }
            if (creditos.add(curso.getCreditos()).compareTo(new BigDecimal(50)) == 1) {
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

            List<Matricula> materiasEstudiante
                    = this.matriculaRepository.findByCorreoEstudiante(estudiante.getCorreo());

            for (Matricula matric : materiasEstudiante) {
                for (DetalleMatricula detMatricula : matric.getDetalles()) {
                    if (detMatricula.getNrc() == curso.getNrc()) {
                        materiaPasada = true;
                        break;
                    }
                }
            }

            if (materiaPasada) {
                errorCursos.add("Materia ya pasada");
                continue;
            }

            Optional<Asignatura> asignatura = this.asignaturaRepository.findByNombre(curso.getAsignatura());

            for (String pre : asignatura.get().getPrerequisitos()) {
                for (Matricula matric : materiasEstudiante) {
                    for (DetalleMatricula detMatricula : matric.getDetalles()) {
                        if (pre.equals(detMatricula.getMateria())) {
                            numeroPre++;
                            break;
                        }
                    }
                }

            }
            if (asignatura.get().getPrerequisitos().size() == numeroPre) {
                prerequisito = true;
            } else {
                log.error("No cumplio con los prerequisitos");
                errorCursos.add("No cumple con los prerequisitos para el curso:" + curso.getNrc());
            }

            for (String carreraCurso : curso.getCarreras()) {
                if (carreraCurso.equals(estudiante.getCarrera())) {
                    carrera = true;
                    break;
                }
            }
            if (!carrera && !curso.getCarreras().isEmpty()) {
                log.error("Carrera no disponible");
                errorCursos.add("Su carrera no esta contemplada en el curso:" + curso.getNrc());
            }

            boolean ver = false;
            if ((asignatura.get().getPrerequisitos().isEmpty() || prerequisito)
                    && (curso.getCarreras().isEmpty() || carrera) && !materiaPasada) {
                for (DetalleMatricula detalleMatricula : detalleMatriculas) {
                    Optional<Curso> cur = this.cursoRepository.findByNrc(detalleMatricula.getNrc());
                    for (Horario horarioActual : curso.getHorarios()) {
                        List<Horario> horariosChoque = cur.get().getHorarios()
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
                    detalleMatriculaNueva.setMateria(curso.getAsignatura());
                    detalleMatriculaNueva.setNrc(curso.getNrc());
                    detalleMatriculaNueva.setCreditos(curso.getCreditos());
                    detalleMatriculaNueva.setEstado("CUR");
                    detalleMatriculaNueva.setFecha(new Date());
                    creditos = creditos.add(curso.getCreditos());
                    curso.setDisponible((Integer) (curso.getDisponible() - 1));

                    detalleMatriculas.add(detalleMatriculaNueva);
                }

            }
        }

        matricula.setCorreoEstudiante(estudiante.getCorreo());

        matricula.setCreditosTotales(creditos);
        matricula.setDetalles(detalleMatriculas);
        matricula.setPeriodo(periodo.get().getNombre());
        matricula.setFecha(new Date());
        if (detalleMatriculas.size() > 0) {
            this.matriculaRepository.save(matricula);
            return errorCursos;
        } else {
            errorCursos.add("No se logro matricular en ninguna materia");
            throw new MatriculaConflictException("No se logro matricular en ninguna materia", errorCursos);
        }

    }

    public Matricula buscarMatricula(String correo, String periodo) {

        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(correo);
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el estudiante con el correo" + correo);
        }
        Estudiante estudiante = estudianteOpt.get();

        Optional<Periodo> per = this.periodoRepository.findById(periodo);
        if (per.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el periodo con el ID" + periodo);
        }

        Optional<Matricula> matriculaOpt = this.matriculaRepository
                .findByCorreoEstudianteAndPeriodo(estudiante.getCorreo(), per.get().getNombre());
        if (matriculaOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro la una matricula en el periodo: " + per.get().getNombre()
                    + " del estudiante " + estudiante.getApellido() + " " + estudiante.getNombre());
        }

        return matriculaOpt.get();
    }

    public void borrarDetalleMatricula(String matricula, Integer nrc) {
        Optional<Matricula> matriculaOpt = this.matriculaRepository.findByCodigo(matricula);
        if (matriculaOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro la matricula");
        }
        BigDecimal creditos;
        int index = 0;
        int i = 0;

        List<DetalleMatricula> detalleMatriculas = matriculaOpt.get().getDetalles();
        for (DetalleMatricula detalles : detalleMatriculas) {
            if (Objects.equals(detalles.getNrc(), nrc)) {
                i = index;
                matriculaOpt.get().setCreditosTotales(matriculaOpt.get()
                        .getCreditosTotales().subtract(detalles.getCreditos()));
            }
            index++;
        }
        detalleMatriculas.remove(i);
        matriculaOpt.get().setDetalles(detalleMatriculas);
        Optional<Curso> curso = this.cursoRepository.findByNrc(nrc);

        curso.get().setDisponible(curso.get().getDisponible() - 1);

        this.matriculaRepository.save(matriculaOpt.get());
    }

    public List<Matricula> obtenerMatriculasPorEstudiante(String correo) {
        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(correo);
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el estudiante con el correo: " + correo);
        }

        List<Matricula> matriculas = this.matriculaRepository.findByCorreoEstudiante(correo);
        if (matriculas.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron matriculas del  estudiante con el correo: " + correo);
        }

        Estudiante estudiante = estudianteOpt.get();

        return matriculas;
    }

}
