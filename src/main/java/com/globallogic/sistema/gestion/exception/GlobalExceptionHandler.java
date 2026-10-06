package com.globallogic.sistema.gestion.exception;

import com.globallogic.sistema.gestion.controller.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(NotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unhandled exception: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        log.warn("Método HTTP no permitido: {}", ex.getMethod());
        String message = String.format("El método HTTP '%s' no está soportado para este endpoint.", ex.getMethod());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        log.warn("Recurso estático o endpoint no encontrado: /{}", ex.getResourcePath());
        String message = String.format("El recurso o endpoint '/%s' no existe.", ex.getResourcePath());
        return buildResponse(HttpStatus.NOT_FOUND, message);
    }


    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = ErrorResponse.builder()
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .message(message)
                        .build();
        return new ResponseEntity<>(error, status);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpHeaders headers) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
        return new ResponseEntity<>(error, headers, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        log.warn("Error de lectura en el JSON: {}", ex.getMessage());

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Error de tipo en parámetro: {}", ex.getName());
        String message = String.format("El valor '%s' no es válido para el parámetro '%s'.",
                ex.getValue(), ex.getName());

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(AlreadyHasRoleException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyHasRoleException(AlreadyHasRoleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(LockedAccountException.class)
    public ResponseEntity<ErrorResponse> handleLocked(LockedAccountException ex){
        long seconds = ex.getLockDuration().getSeconds();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.RETRY_AFTER, String.valueOf(seconds));
        return buildResponse(HttpStatus.LOCKED, ex.getMessage(), headers);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIlegalArgument(IllegalArgumentException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookie(MissingRequestCookieException ex){
        if (ex.getCookieName().equals(AuthController.REFRESH_COOKIE_NAME)){
            return buildResponse(HttpStatus.UNAUTHORIZED, "Refresh cookie not found");
        }

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(NotProfesorDelCursoException.class)
    public ResponseEntity<ErrorResponse> handleNotProfesorDelCurso(NotProfesorDelCursoException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(YaInscriptoException.class)
    public ResponseEntity<ErrorResponse> handleYaInscriptoException(YaInscriptoException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
}
