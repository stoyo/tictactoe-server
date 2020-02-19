package com.three.d.tic.tac.toe.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.three.d.tic.tac.toe.dto.ErrorResponse;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NO_HANDLER_FOUND_MESSAGE = "%s %s is not implemented";
    private static final String METHOD_NOT_SUPPORTED_MESSAGE = "method not supported";
    private static final String METHOD_NOT_SUPPORTED_WITH_SUPPORTED_METHODS_MESSAGE =
            METHOD_NOT_SUPPORTED_MESSAGE + ", supported methods are: [%s]";
    private static final String MEDIA_TYPE_NOT_SUPPORTED_WITH_SUPPORTED_TYPES_MESSAGE =
            "media type not supported, supported media types are: [%s]";
    private static final String METHOD_ARGUMENT_TYPE_MISMATCH_MESSAGE =
            "'%s' is not a valid '%s'";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Stream.concat(
                ex.getBindingResult().getFieldErrors().stream(),
                ex.getBindingResult().getGlobalErrors().stream())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder().errors(errors).build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Stream.concat(
                ex.getBindingResult().getFieldErrors().stream(),
                ex.getBindingResult().getGlobalErrors().stream())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder().errors(errors).build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(ex.getMostSpecificCause().toString()))
                .build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = String.format(METHOD_ARGUMENT_TYPE_MISMATCH_MESSAGE, ex.getValue(),
                ex.getRequiredType().getSimpleName());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(error))
                .build();

        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(
                        String.format(NO_HANDLER_FOUND_MESSAGE, ex.getHttpMethod(), ex.getRequestURL())))
                .build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error;
        if (CollectionUtils.isEmpty(ex.getSupportedHttpMethods())) {
            error = METHOD_NOT_SUPPORTED_MESSAGE;
        } else {
            String supportedHttpMethods = ex.getSupportedHttpMethods().stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            error = String.format(METHOD_NOT_SUPPORTED_WITH_SUPPORTED_METHODS_MESSAGE, supportedHttpMethods);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(error))
                .build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String supportedMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(MimeType::toString)
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(
                        String.format(MEDIA_TYPE_NOT_SUPPORTED_WITH_SUPPORTED_TYPES_MESSAGE, supportedMediaTypes)))
                .build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    @ExceptionHandler({ ResponseStatusException.class })
    protected ResponseEntity<Object> handleResponseStatusExceptions(
            ResponseStatusException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(ex.getReason()))
                .build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
