package ec.edu.espe.distribuidas.Sist_Matricula_Doc.service;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.CarreraRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.EstudianteRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.EstudianteEditarRS;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.EstudianteRQ;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Carrera;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Estudiante;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;

    public EstudianteService(EstudianteRepository estudianteRepository, CarreraRepository carreraRepository) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
    }

    public void agregarEstudiante(EstudianteRQ estudianteRQ) {
        boolean ced = this.verificarCedula(estudianteRQ.getIdentificacion());

        if (!ced) {
            log.error("Cedula Invalida: {}", estudianteRQ.getIdentificacion());
            throw new EntityNotFoundException("Cedula Invalida");
        }

        List<Estudiante> estudiantes = this.estudianteRepository
                .findByCorreoOrIdentificacion(estudianteRQ.getCorreo() + "@espe.edu.ec",
                        estudianteRQ.getIdentificacion());
        if (!estudiantes.isEmpty()) {
            throw new EntityNotFoundException("Ya existe un usuario con esta cedula o nombre de usuario");
        }
        Optional<Carrera> carrera = this.carreraRepository.findByCodigo(estudianteRQ.getCarrera());
        if (carrera.isEmpty()) {
            throw new EntityNotFoundException("No existe la carrera con id: " + estudianteRQ.getCarrera());
        }
        Estudiante estudiante = new Estudiante();
        estudiante.setTipoIdentificacion(estudianteRQ.getTipo());
        estudiante.setIdentificacion(estudianteRQ.getIdentificacion());
        estudiante.setContrasena(estudianteRQ.getContrasena());
        estudiante.setCorreo(estudianteRQ.getCorreo() + "@espe.edu.ec");
        estudiante.setApellido(estudianteRQ.getApellido().toUpperCase());
        estudiante.setNombre(estudianteRQ.getNombre().toUpperCase());
        estudiante.setGenero(estudianteRQ.getGenero());
        estudiante.setTelefono(estudianteRQ.getTelefono());
        estudiante.setDireccion(estudianteRQ.getDireccion().toUpperCase());
        //estudiante.setCarrera(carrera.get());
        estudiante.setCarrera(carrera.get().getNombre());
        this.estudianteRepository.save(estudiante);
    }

    public Estudiante actualizarEstudiante(String usuario, EstudianteEditarRS estudianteRs) {
        Optional<Estudiante> estudianteOptional = this.estudianteRepository.findByCorreo(usuario);
        if (estudianteOptional.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el estudiante con el usuario: " + usuario);
        }
        Estudiante estudiante = estudianteOptional.get();
        estudiante.setApellido(estudianteRs.getApellido().toUpperCase());
        estudiante.setNombre(estudianteRs.getNombre().toUpperCase());
        estudiante.setGenero(estudianteRs.getGenero());
        estudiante.setTelefono(estudianteRs.getTelefono());
        estudiante.setDireccion(estudianteRs.getDireccion().toUpperCase());
        return this.estudianteRepository.save(estudiante);
    }

    public Estudiante obtenerEstudanterPorCorreo(String correo) {
        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(correo);
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el estudiante con el correo: " + correo);
        }
        log.info("Nombre: {}", estudianteOpt.get().getApellido());

        return estudianteOpt.get();
    }

    private boolean verificarCedula(String document) {
        byte sum = 0;
        try {
            if (document.trim().length() != 10) {
                return false;
            }
            String[] data = document.split("");
            byte verifier = Byte.parseByte(data[0] + data[1]);
            if (verifier < 1 || verifier > 24) {
                return false;
            }
            byte[] digits = new byte[data.length];
            for (byte i = 0; i < digits.length; i++) {
                digits[i] = Byte.parseByte(data[i]);
            }
            if (digits[2] > 6) {
                return false;
            }
            for (byte i = 0; i < digits.length - 1; i++) {
                if (i % 2 == 0) {
                    verifier = (byte) (digits[i] * 2);
                    if (verifier > 9) {
                        verifier = (byte) (verifier - 9);
                    }
                } else {
                    verifier = (byte) (digits[i] * 1);
                }
                sum = (byte) (sum + verifier);
            }
            if ((sum - (sum % 10) + 10 - sum) == digits[9]) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
