package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.PermissionDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    ApiResponseDto executeCreatePermission(PermissionDto permissionDto, BindingResult bindingResult) throws BadRequestException;
    ApiResponseDto executeUpdatePermisison(String permissionId, PermissionDto permissionDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetListPermissions(int page, int size, String permissionId, String module, String status) throws NotFoundException;
    ApiResponseDto executeGetPermission(String permissionId) throws NotFoundException;
    ApiResponseDto executeDisabledPermission(String permissionId, DisabledRegisterDto disabledRegisterDto) throws NotFoundException;
    PermissionEntity getById(String permissionId);
    ApiResponseDto executeGetListPermissionsBySelect() throws NotFoundException;
    ApiResponseDto executeCreateMassivePermissions(MultipartFile file) throws BadRequestException;
    List<PermissionEntity> validatePermissions(List<PermissionDto> permissionsList) throws NotFoundException, BadRequestException;

}
