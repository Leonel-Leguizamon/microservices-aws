package com.dlsoftware.msvc.products_msvc.commons.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Wrapper utilizado para generalizar el formato de respuesta de esta API")
public class GeneralResponseWrapper<T> {
    @Schema(description = "Contenido de la respuesta cuando resulta exitoso")
    T content;
    @Schema(description = "Descripción de error en caso de que haya")
    GeneralErrorResponse error;
}

