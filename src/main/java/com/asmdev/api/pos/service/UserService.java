package com.asmdev.api.pos.service;

import com.asmdev.api.pos.dto.ApiResponseDto;
import com.asmdev.api.pos.dto.DisabledRegisterDto;
import com.asmdev.api.pos.dto.UserDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.persistence.entity.UserEntity;
import org.springframework.validation.BindingResult;

public interface UserService {

    ApiResponseDto executeCreateUser(UserDto userDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeUpdateUser(String userId, UserDto userDto, BindingResult bindingResult) throws BadRequestException, NotFoundException;
    ApiResponseDto executeGetListUsers(int page, int size, String userId, String status, String roleId,  String hireDate) throws NotFoundException;
    ApiResponseDto executeGetLisUsersBySelect() throws NotFoundException;
    ApiResponseDto executeGetUser(String userId) throws NotFoundException;
    ApiResponseDto executeDisabledUser(String userId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws NotFoundException, BadRequestException;
    UserEntity getUserById(String userId) throws NotFoundException;
    ApiResponseDto executeLogin();
    ApiResponseDto executeLogout();
    ApiResponseDto executeRefreshToken();
    ApiResponseDto executeViewProfile();
}
