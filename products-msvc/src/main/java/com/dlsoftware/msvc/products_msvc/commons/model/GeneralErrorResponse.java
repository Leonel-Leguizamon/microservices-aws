package com.dlsoftware.msvc.products_msvc.commons.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Clase utilizada para generalizar la respuesta de errores")
public class GeneralErrorResponse {
    @Schema(description = "Clave para identificar el tipo de error")
    String errorKey;
    @Schema(description = "Codigo de error personalizado. El HTTP se expresa en el responsecode")
    String errorCode;
    @Schema(description = "Descripción de la causa del error")
    String errorDescription;
}

