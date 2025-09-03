package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.RoleMapper;
import com.asmdev.api.pos.persistence.entity.PermissionEntity;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import com.asmdev.api.pos.persistence.repository.RoleRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationRole;
import com.asmdev.api.pos.service.PermissionService;
import com.asmdev.api.pos.service.RoleService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ValidateInputs validateInputs;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionService permissionService;

    @Override
    public ApiResponseDto executeCreateRole(RoleDto roleDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputsValidate = this.validateInputs.validateInputs(bindingResult);
        if (!inputsValidate.isEmpty())
            throw new BadRequestException("Campos invalidos", inputsValidate);

        List<PermissionEntity> permissionList = permissionService.validatePermissions(roleDto.getPermissions());
        RoleEntity roleSave = roleMapper.convertToEntity(roleDto);
        roleSave.setPermissions(permissionList);
        roleSave.setName(roleDto.getName().toUpperCase());
        roleSave = roleRepository.save(roleSave);
        return new ApiResponseDto(HttpStatus.CREATED.value(),"Se registro el Rol de forma existosa",roleMapper.convertToDto(roleSave));
    }

    @Override
    public ApiResponseDto executeUpdateRole(String roleId, RoleDto roleDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {

        RoleEntity roleBD = getById(roleId);
        if (roleBD==null)
            throw new NotFoundException("No existe este Rol con el ID "+roleId);

        List<ValidateInputDto> inputsValidate = this.validateInputs.validateInputs(bindingResult);
        if (!inputsValidate.isEmpty())
            throw new BadRequestException("Campos invalidos", inputsValidate);

        roleBD.setName(roleDto.getName().toUpperCase());
        roleBD.setDescription(roleDto.getDescription());
        roleBD.setStatus(roleDto.getStatus());
        roleBD.setPermissions(permissionService.validatePermissions(roleDto.getPermissions()));

        RoleEntity roleEdit = this.roleRepository.save(roleBD);
        return new ApiResponseDto(HttpStatus.OK.value(),"Se actualizo el Rol de forma exitosa", roleMapper.convertToDto(roleEdit));
    }

    @Override
    public ApiResponseDto executeGetRole(String roleId) throws NotFoundException {
        RoleEntity roleBD = getById(roleId);
        if (roleBD==null)
            throw new NotFoundException("No existe este Rol con el ID "+roleId);

        return new ApiResponseDto(HttpStatus.OK.value(),"Información del Rol",roleMapper.convertToDto(roleBD));
    }

    @Override
    public ApiResponseDto executeGetListRoles(int page, int size, String roleId, String status) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<RoleEntity> filter = SpecificationRole.withFilter(roleId, status);
        Page<RoleEntity> roleListBD = this.roleRepository.findAll(filter,pageable);

        if (roleListBD.isEmpty())
            throw new NotFoundException("No hay registros en la base de datos");

        List<RoleDto> roleList = roleListBD.getContent().stream().map(roleMapper::convertToDto).collect(Collectors.toList());
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(roleList));
        pagination.setTotalElements((int) roleListBD.getTotalElements());

        return new ApiResponseDto(HttpStatus.OK.value(),"Listado de Roles", pagination);
    }

    @Override
    public ApiResponseDto executeGetListRolesBySelect() throws NotFoundException {
        List<RoleEntity> roleListBD = this.roleRepository.findAll();
        if (roleListBD.isEmpty())
            throw new NotFoundException("No se encuentran registro de la base de datos");

        List<RoleDto> roleList = roleListBD.stream().map(roleMapper::convertToDto).collect(Collectors.toList());
        return new ApiResponseDto(HttpStatus.OK.value(),"Listado de Roles", roleList);
    }

    @Override
    public ApiResponseDto executeDisabledRole(String roleId, DisabledRegisterDto disabledDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {
        RoleEntity roleBD = this.getById(roleId);
        if (roleBD == null)
            throw new NotFoundException("No existe este Rol con el ID "+roleId);

        if (roleBD.getStatus().equals(Status.valueOf(disabledDto.getStatus())))
            throw new BadRequestException("El rol ya tiene asignado el estado '" + disabledDto.getStatus() + "'");

        roleBD.setStatus(Status.valueOf(disabledDto.getStatus()));
        RoleEntity roleDisabled = roleRepository.save(roleBD);
        return new ApiResponseDto(HttpStatus.OK.value(),"Se modifico el status del registro de forma exitosa", this.roleMapper.convertToDto(roleDisabled));
    }

    @Override
    public RoleEntity getById(String roleId) {
        return this.roleRepository.findById(roleId).orElse(null);
    }
}
