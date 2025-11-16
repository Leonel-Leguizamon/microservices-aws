package com.dlsoftware.msvc.products_msvc.configuration;

import com.dlsoftware.msvc.products_msvc.commons.enums.ErrorKeys;
import com.dlsoftware.msvc.products_msvc.commons.exceptions.BaseException;
import com.dlsoftware.msvc.products_msvc.commons.exceptions.ProductNotFoundException;
import com.dlsoftware.msvc.products_msvc.commons.model.GeneralErrorResponse;
import com.dlsoftware.msvc.products_msvc.commons.model.GeneralResponseWrapper;
import com.dlsoftware.msvc.products_msvc.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<GeneralResponseWrapper<String>> productNotFound(final ProductNotFoundException ex, final WebRequest request) {
        log.error("productNotFound: " + ex.getMessage(), ex);
        GeneralResponseWrapper<String> response = this.getGeneralResponse(ErrorKeys.PRODUCT_NOT_FOUND.toString(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<GeneralResponseWrapper<String>> generalError(final BaseException ex, final WebRequest request) {
        log.error("generalException: " + ex.getMessage(), ex);
        GeneralResponseWrapper<String> response = this.getGeneralResponse(ErrorKeys.GENERAL_EXCEPTION.toString(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
//
//    /**
//     * Funcion para catchear errores inesperados en la API. Siempre mantener a lo ultimo, puesto que spring lee
//     * estos handlers por matching, por lo cual este al ser mas generico debe permanecer ultimo, para que las demas
//     * excepiones caigan en la funcion correcta
//     * @param ex
//     * @param request
//     * @return
//     */
//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<GeneralResponseWrapper<String>> uncatchedExceptions(final Exception ex, final WebRequest request) {
//        log.error("uncatchedException: " + ex.getMessage(), ex);
//        GeneralErrorResponse error = GeneralErrorResponse.builder()
//                .errorKey(ErrorKeys.UNCATCHED_EXCEPTION.toString())
//                .errorCode("0")
//                .errorDescription(messageUtil.getMessage("exceptions.internal.general"))
//                .build();
//
//        GeneralResponseWrapper<String> response = GeneralResponseWrapper.<String>builder()
//                .content(null)
//                .error(error)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }

    private GeneralResponseWrapper<String> getGeneralResponse(String errorKey, BaseException ex){
        GeneralErrorResponse error = GeneralErrorResponse.builder()
                .errorKey(errorKey)
                .errorCode(ex.getCode())
                .errorDescription(ex.getMessage())
                .build();

        return GeneralResponseWrapper.<String>builder()
                .content(null)
                .error(error)
                .build();

    }

}