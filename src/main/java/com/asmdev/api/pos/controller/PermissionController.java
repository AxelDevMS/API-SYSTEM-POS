package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.PermissionDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.security.preauthorize.PermissionPreAuthorize.*;
import com.asmdev.api.pos.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/pos/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;



    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreatePermission(@Valid @RequestBody PermissionDto permissionDto, BindingResult bindingResult) throws BadRequestException {
        ApiResponseDto response = this.permissionService.executeCreatePermission(permissionDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{permissionId}")
    public ResponseEntity<ApiResponseDto> executeUpdatePermission(
            @PathVariable String permissionId,
            @Valid @RequestBody PermissionDto permissionDto,
            BindingResult bindingResult) throws BadRequestException, NotFoundException {

        ApiResponseDto response = this.permissionService.executeUpdatePermisison(permissionId, permissionDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @CanGetPermission
    @GetMapping("/get/{permissionId}")
    public ResponseEntity<ApiResponseDto> executeGetPermission(@PathVariable String permissionId) throws NotFoundException {
        ApiResponseDto response = this.permissionService.executeGetPermission(permissionId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CanListPermisisons
    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> executeGetListPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String permissionId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String status
    ) throws NotFoundException {
        ApiResponseDto response = this.permissionService.executeGetListPermissions(page, size, permissionId, module, status);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("/disabled/{permissionId}")
    public ResponseEntity<ApiResponseDto> exeucteDisabledPermission(
            @PathVariable String permissionId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult) throws NotFoundException {

        ApiResponseDto response = this.permissionService.executeDisabledPermission(permissionId, disabledRegisterDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @CanListPermisisons
    @GetMapping("/list-select")
    public ResponseEntity<ApiResponseDto> executeGetListPermissionBySelect() throws NotFoundException {
        ApiResponseDto response = this.permissionService.executeGetListPermissionsBySelect();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("upload")
    public ResponseEntity<ApiResponseDto> executeUploadMassivePermisisons(@RequestParam("file")MultipartFile file) throws BadRequestException {
        ApiResponseDto response = this.permissionService.executeCreateMassivePermissions(file);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

}
