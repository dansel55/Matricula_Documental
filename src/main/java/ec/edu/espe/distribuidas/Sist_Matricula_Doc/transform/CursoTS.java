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
package ec.edu.espe.distribuidas.Sist_Matricula_Doc.transform;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.CursoRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.HorarioRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Curso;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Horario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author bran-
 */
@Slf4j
public class CursoTS {

    public static CursoRS cursoRS(Curso curso) {
        return CursoRS.builder()
                .codigo(curso.getCodigo())
                .nrc(curso.getNrc())
                .cupo(curso.getCupo())
                .asignatura(curso.getAsignatura())
                .disponible(curso.getDisponible())
                .creditos(curso.getCreditos())
                .carreras(curso.getCarreras())
                .horarios(horarios(curso.getHorarios()))
                .build();
    }

    /*public static List<String> carrerasCusrso(List<CarreraCurso> carreraCurso) {
        List<String> carreras = new ArrayList<>();
        if (carreraCurso.isEmpty() || carreraCurso == null) {
            return null;
        }
        for (CarreraCurso c : carreraCurso) {
            carreras.add(c.getCarrera().getNombre());
        }
        return carreras;
    }*/
    public static List<HorarioRS> horarios(List<Horario> h) {
        List<HorarioRS> horarioRS = new ArrayList<>();
        if (h.isEmpty()) {
            return null;
        }
        for (Horario hor : h) {
            HorarioRS hrs = new HorarioRS();
            hrs.setDia(hor.getDia());
            log.info("HoraInicio:{}", hor.getHoraInicio().toString());
            String inicio[] = hor.getHoraInicio().toString().split(" ");
            hrs.setHoraInicio(inicio[3]);
            String fin[] = hor.getHoraFin().toString().split(" ");
            hrs.setHoraFin(fin[3]);
            horarioRS.add(hrs);
        }
        return horarioRS;
    }

}
