package io.wisoft.capstonedesign.global.annotation.swagger;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Operation
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(implementation = Error.class)))
})
public @interface SwaggerApiFailWithoutAuth {
}
