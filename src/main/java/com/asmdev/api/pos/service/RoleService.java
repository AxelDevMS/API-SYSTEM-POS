package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.RoleDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import org.springframework.validation.BindingResult;

public interface RoleService {

    ApiResponseDto executeCreateRole(RoleDto roleDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeUpdateRole(String roleId, RoleDto roleDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    ApiResponseDto executeGetRole(String roleId) throws NotFoundException;
    ApiResponseDto executeGetListRoles(int page, int size, String roleId, String status) throws NotFoundException;
    ApiResponseDto executeGetListRolesBySelect() throws NotFoundException;
    ApiResponseDto executeDisabledRole(String roleId, DisabledRegisterDto disabledDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    RoleEntity getById(String roleId);

}
