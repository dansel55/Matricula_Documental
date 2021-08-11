package ec.edu.espe.distribuidas.Sist_Matricula_Doc.controller;

import ec.edu.espe.distribuidas.Sist_Matricula_Doc.dto.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.exception.*;
import ec.edu.espe.distribuidas.Sist_Matricula_Doc.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/v1/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    @ApiOperation(value = "Realiza la autenticacion del estudiante",
            notes = "Permite autenticar las credenciales de un estudiante para el ingreso al sistema")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "ok - Logeo Exitoso"),
        @ApiResponse(code = 404, message = "Not Found - No se encontro una entidad"),
        @ApiResponse(code = 400, message = "Bad Request - Contrasena incorrecta ")})
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        try {
            this.loginService.authenticacion(loginDto);
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
