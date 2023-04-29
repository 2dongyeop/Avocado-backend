package io.wisoft.capstonedesign.domain.pick.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.pick.application.PickService;
import io.wisoft.capstonedesign.domain.pick.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "찜하기")
@RestController
@RequiredArgsConstructor
public class PickApiController {

    private final PickService pickService;

    @Operation(summary = "찜하기 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/picks/new")
    public CreatePickResponse createPick(@RequestBody @Valid final CreatePickRequest request) {
        return new CreatePickResponse(pickService.save(request));
    }


    @Operation(summary = "찜하기 취소")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/api/picks/{id}")
    public DeletePickResponse deletePick(@PathVariable("id") final Long id) {
        pickService.cancelPick(id);
        return new DeletePickResponse(id);
    }


    @Operation(summary = "찜하기 단건 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/picks/{id}/details")
    public Result pick(@PathVariable("id") final Long id) {
        return new Result(new PickDto(pickService.findDetailById(id)));
    }
}
