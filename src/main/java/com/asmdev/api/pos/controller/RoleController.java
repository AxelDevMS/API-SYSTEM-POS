package com.asmdev.api.pos.controller;


import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.service.RoleService;
import jakarta.persistence.Access;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pos/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/get-alls")
    public ResponseEntity<ApiResponseDto> execuetGetListRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) String status
    ) throws NotFoundException {
        ApiResponseDto response = this.roleService.executeGetListRoles(page, size, roleId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list/select")
    public ResponseEntity<ApiResponseDto> executeGetListRolesBySelect() throws NotFoundException {
        ApiResponseDto response  = this.roleService.executeGetListRolesBySelect();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/{roleId}")
    public ResponseEntity<ApiResponseDto> executeGetRole(@PathVariable String roleId) throws NotFoundException {
        ApiResponseDto response = this.roleService.executeGetRole(roleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDto> executeCreateRole(@Valid @RequestBody RoleDto roleDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        ApiResponseDto response = this.roleService.executeCreateRole(roleDto, bindingResult);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping("/update/{roleId}")
    public ResponseEntity<ApiResponseDto> executeUpdateRole(
            @PathVariable String roleId,
            @Valid @RequestBody RoleDto roleDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.roleService.executeUpdateRole(roleId, roleDto, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/disabled/{roleId}")
    public ResponseEntity<ApiResponseDto> executeDisabledRole(
            @PathVariable String roleId,
            @Valid @RequestBody DisabledRegisterDto disabledRegisterDto,
            BindingResult bindingResult
    ) throws NotFoundException, BadRequestException {
        ApiResponseDto response = this.roleService.executeDisabledRole(roleId,disabledRegisterDto,bindingResult);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
