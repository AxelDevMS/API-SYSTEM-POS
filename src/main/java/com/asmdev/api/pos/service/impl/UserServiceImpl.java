package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.UserMapper;
import com.asmdev.api.pos.persistence.entity.RoleEntity;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import com.asmdev.api.pos.persistence.repository.UserRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationUser;
import com.asmdev.api.pos.service.RoleService;
import com.asmdev.api.pos.service.UserService;
import com.asmdev.api.pos.utils.EmployeeCodeGenerator;
import com.asmdev.api.pos.utils.status.Status;
import com.asmdev.api.pos.utils.status.UserStatus;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ValidateInputs validateInputs;

    @Override
    public ApiResponseDto executeCreateUser(UserDto userDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputsValidate = this.validateInputs.validateInputs(bindingResult);
        if(!inputsValidate.isEmpty())
            throw new BadRequestException("Campos invalidos", inputsValidate);

        UserEntity userBD = this.userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (userBD != null)
            throw new BadRequestException("Ya existe un usuario con el correo "+userDto.getEmail()+" ingresar otro correo");

        RoleEntity role = this.roleService.getById(userDto.getRole().getId());
        if(role == null)
            throw new NotFoundException("El Rol que asignaste al usuario no existe en el sistema");

        if (!role.getStatus().equals(Status.ACTIVE))
            throw new BadRequestException("No puedes asignar este Rol al usuario ya que tiene status "+ role.getStatus());

        if (!userDto.getPassword().equals(userDto.getConfirmPassword()))
            throw new BadRequestException("La contraseña y su confirmación no coinciden.");

        UserEntity userSave = this.userMapper.convertToEntity(userDto);
        userSave.setRole(role);
        userSave.setEmployeeCode(EmployeeCodeGenerator.generateCodeEmployee());
        userSave = this.userRepository.save(userSave);

        return new ApiResponseDto(HttpStatus.CREATED.value(),"Se ha registrado el usuario exitosamente", this.userMapper.convertToDto(userSave));
    }

    @Override
    public ApiResponseDto executeUpdateUser(String userId, UserDto userDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputsValidate = this.validateInputs.validateInputs(bindingResult);
        if(!inputsValidate.isEmpty())
            throw new BadRequestException("Campos invalidos", inputsValidate);

        UserEntity userBD = this.getUserById(userId);

        RoleEntity role = this.roleService.getById(userDto.getRole().getId());
        if(role == null)
            throw new NotFoundException("El Rol que asignaste al usuario no existe en el sistema");

        if (!role.getStatus().equals(Status.ACTIVE))
            throw new BadRequestException("No puedes asignar este Rol al usuario ya que tiene status "+ role.getStatus());

        if (!userDto.getPassword().equals(userDto.getConfirmPassword()))
            throw new BadRequestException("La contraseña y su confirmación no coinciden.");


        userBD.setName(userDto.getName());
        userBD.setLastname(userDto.getLastname());
        userBD.setEmail(userDto.getEmail());
        userBD.setPassword(userDto.getPassword());
        userBD.setPhone(userDto.getPhone());
        userBD.setStreet(userDto.getStreet());
        userBD.setExteriorNumber(userBD.getExteriorNumber());
        userBD.setInteriorNumber(userDto.getInteriorNumber());
        userBD.setNeighborhood(userDto.getNeighborhood());
        userBD.setMunicipality(userDto.getMunicipality());
        userBD.setState(userDto.getState());
        userBD.setHireDate(userDto.getHireDate());
        userBD.setSalary(userDto.getSalary());
        userBD.setStatus(userDto.getStatus());
        userBD.setRole(role);

        userBD = this.userRepository.save(userBD);
        return new ApiResponseDto(HttpStatus.OK.value(),"El usuario se ha actualizado de forma exitosa", this.userMapper.convertToDto(userBD));
    }

    @Override
    public ApiResponseDto executeGetListUsers(int page, int size, String userId, String status, String roleId,String hireDate) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<UserEntity> filter = SpecificationUser.withFilter(userId, status, roleId, hireDate);
        Page<UserEntity> userListBD = this.userRepository.findAll(filter,pageable);

        if (userListBD.isEmpty())
            throw new NotFoundException("No tienes usuarios en el sistema");

        List<UserDto> userList = userListBD.stream().map(userMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(userList));
        pagination.setTotalElements((int) userListBD.getTotalElements());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de usuarios del sistema", pagination);
    }

    @Override
    public ApiResponseDto executeGetLisUsersBySelect() throws NotFoundException {
        List<UserEntity> userListBD = this.userRepository.findAll();
        if (userListBD.isEmpty())
            throw new NotFoundException("No tienes usuarios registrados en el sistema");

        List<UserDto> userList = userListBD.stream().map(userMapper::convertToDto).collect(Collectors.toList());
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de usuarios asociados al sistema", userList);
    }

    @Override
    public ApiResponseDto executeGetUser(String userId) throws NotFoundException {
        UserEntity userBD = this.getUserById(userId);
        return new ApiResponseDto(HttpStatus.OK.value(),"Información detallada del usuario", this.userMapper.convertToDto(userBD));
    }

    @Override
    public ApiResponseDto executeDisabledUser(String userId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException {
        UserEntity userBD = this.getUserById(userId);

        if (userBD.getStatus().equals(UserStatus.valueOf(disabledRegisterDto.getStatus())))
            throw new BadRequestException("El usuario ya tiene asignado el estado '" + disabledRegisterDto.getStatus() + "'");

        userBD.setStatus(UserStatus.valueOf(disabledRegisterDto.getStatus()));
        userBD = this.userRepository.save(userBD);

        return new ApiResponseDto(HttpStatus.OK.value(),"El usuario cambio de status exitosamente", this.userMapper.convertToDto(userBD));
    }

    @Override
    public UserEntity getUserById(String userId) throws NotFoundException {
        UserEntity userBD = this.userRepository.findById(userId).orElse(null);
        if (userBD == null)
            throw new NotFoundException("No existe este Empleado con ID " +userId + " en el sistema");

        return userBD;
    }

    @Override
    public ApiResponseDto executeLogin() {
        return null;
    }

    @Override
    public ApiResponseDto executeLogout() {
        return null;
    }

    @Override
    public ApiResponseDto executeRefreshToken() {
        return null;
    }

    @Override
    public ApiResponseDto executeViewProfile() {
        return null;
    }
}
