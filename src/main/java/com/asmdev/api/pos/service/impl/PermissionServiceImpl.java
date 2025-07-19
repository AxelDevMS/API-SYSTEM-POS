package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.PermissionMapper;
import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import com.asmdev.api.pos.persistence.repository.PermissionRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationPermission;
import com.asmdev.api.pos.service.PermissionService;
import com.asmdev.api.pos.utils.status.Status;
import com.asmdev.api.pos.utils.validations.ValidateInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ValidateInputs validateInputs;

    @Override
    public ApiResponseDto executeCreatePermission(PermissionDto permissionDto, BindingResult bindingResult) throws BadRequestException {
        List<ValidateInputDto> inputsValidated = this.validateInputs.validateInputs(bindingResult);
        if (!inputsValidated.isEmpty())
            throw new BadRequestException("Campos invalidos", inputsValidated);

        PermissionEntity permissionBD = this.permissionRepository.findByName(permissionDto.getName()).orElse(null);
        if (permissionBD != null)
            throw new BadRequestException("Ya existe un permiso con el nombre "+permissionDto.getName());

        PermissionEntity permisionSave = this.permissionRepository.save(this.permissionMapper.convertToEntity(permissionDto));
        return new ApiResponseDto(HttpStatus.CREATED.value(),"El registro se inserto correctamente",this.permissionMapper.convertToDto(permisionSave));
    }

    @Override
    public ApiResponseDto executeUpdatePermisison(String permissionId, PermissionDto permissionDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputsValidated = this.validateInputs.validateInputs(bindingResult);
        if (!inputsValidated.isEmpty())
            throw new BadRequestException("Campos inavalidos", inputsValidated);

        PermissionEntity permissionBD = this.getById(permissionId);
        if (permissionBD == null)
            throw new NotFoundException("No existe este registro con el ID "+permissionId);

        permissionBD.setName(permissionDto.getName());
        permissionBD.setModule(permissionDto.getModule());
        permissionBD.setDescription(permissionDto.getDescription());
        permissionBD.setStatus(permissionDto.getStatus());
        PermissionEntity permissionEdit = this.permissionRepository.save(permissionBD);

        return new ApiResponseDto(HttpStatus.OK.value(),"El registro se actualizo exitosamente", this.permissionMapper.convertToDto(permissionEdit));
    }

    @Override
    public ApiResponseDto executeGetListPermissions(int page, int size, String permissionId, String module, String status) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<PermissionEntity> filter = SpecificationPermission.withFilter(permissionId,module,status);
        Page<PermissionEntity> permissionList = this.permissionRepository.findAll(filter,pageable);

        if (permissionList.isEmpty())
            throw new NotFoundException("No hay registros en la base de datos");

        List<PermissionDto> data = permissionList.getContent().stream().map(permissionMapper::convertToDto).collect(Collectors.toList());

        ListDataPaginationDto permissionsPagination = new ListDataPaginationDto();
        permissionsPagination.setData(Collections.singletonList(data));
        permissionsPagination.setTotalElements((int) permissionList.getTotalElements());

        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de permisos", permissionsPagination);
    }

    @Override
    public ApiResponseDto executeGetPermission(String permissionId) throws NotFoundException {
        PermissionEntity permissionBD = this.getById(permissionId);
        if (permissionBD == null)
            throw new NotFoundException("No existe este registro con ID "+ permissionId);

        return new ApiResponseDto(HttpStatus.OK.value(),"Información detallada del registro recuperado", this.permissionMapper.convertToDto(permissionBD));
    }

    @Override
    public ApiResponseDto executeDisabledPermission(String permissionId, DisabledRegisterDto disabledRegisterDto) throws NotFoundException {
        PermissionEntity permissionBD = this.getById(permissionId);
        if (permissionBD == null)
            throw new NotFoundException("No existe este registro con ID "+ permissionId);

        permissionBD.setStatus(Status.valueOf(disabledRegisterDto.getStatus()));
        PermissionEntity permissionDisabled = this.permissionRepository.save(permissionBD);

        return new ApiResponseDto(HttpStatus.OK.value(),"Se ha cambiado el status del registro",this.permissionMapper.convertToDto(permissionDisabled));
    }

    @Override
    public PermissionEntity getById(String permissionId) {
        return this.permissionRepository.findById(permissionId).orElse(null);
    }

    @Override
    public ApiResponseDto executeGetListPermissionsBySelect() throws NotFoundException {
        List<PermissionEntity> permissionListBD = this.permissionRepository.findAll();
        if (permissionListBD.isEmpty())
            throw new NotFoundException("No hay registros en la base de datos");

        List<PermissionDto> permissionsList = permissionListBD.stream().map(permissionMapper::convertToDto).collect(Collectors.toList());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de registros", permissionsList);
    }
}
