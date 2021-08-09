package ec.edu.espe.distribuidas.Sist_Matricula_Doc.service;

//import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dao.EstudianteRepository;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.LoginDto;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.EntityNotFoundException;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.model.Estudiante;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    /*private final EstudianteRepository estudianteRepository;

    public LoginService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public void authenticacion(LoginDto loginDto) throws Exception {
        Optional<Estudiante> estudianteOpt = this.estudianteRepository.findByCorreo(loginDto.getCorreo());
        if (estudianteOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        Estudiante estudiante = estudianteOpt.get();

        if (!estudiante.getContrasena().equals(loginDto.getPassword())) {
            throw new Exception("Contraseña Invalida");
        }
    }*/
}
