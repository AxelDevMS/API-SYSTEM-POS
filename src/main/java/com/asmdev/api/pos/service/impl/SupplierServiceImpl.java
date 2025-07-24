package com.asmdev.api.pos.service.impl;

import com.asmdev.api.pos.dto.*;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.exception.NotFoundException;
import com.asmdev.api.pos.mapper.SupplierMapper;
import com.asmdev.api.pos.persistence.entity.CustomerEntity;
import com.asmdev.api.pos.persistence.entity.ProductEntity;
import com.asmdev.api.pos.persistence.entity.SupplierEntity;
import com.asmdev.api.pos.persistence.repository.SupplierRepository;
import com.asmdev.api.pos.persistence.specification.SpecificationSupplier;
import com.asmdev.api.pos.service.SupplierService;
import com.asmdev.api.pos.utils.helper.ExcelHelper;
import com.asmdev.api.pos.utils.status.ProductStatus;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private ValidateInputs validateInputs;

    @Override
    public ApiResponseDto executeCreateSupplier(SupplierDto supplierDto, BindingResult bindingResult) throws BadRequestException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        SupplierEntity supplierBD = this.supplierRepository.findByEmail(supplierDto.getEmail()).orElse(null);
        if (supplierBD !=  null)
            throw new BadRequestException("Ya existe un proveedor con este mismo correo electronico: "+supplierDto.getEmail());

        supplierBD = this.supplierRepository.save(this.supplierMapper.convertToEntity(supplierDto));
        return new ApiResponseDto(HttpStatus.CREATED.value(),"Proveedor creado existosamente", this.supplierMapper.convertToDto(supplierBD));
    }

    @Override
    public ApiResponseDto executeUpdateSupplier(String supplierId, SupplierDto supplierDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        SupplierEntity supplier = this.getSupplierById(supplierId);
        supplier.setName(supplierDto.getName());
        supplier.setContactPerson(supplierDto.getContactPerson());
        supplier.setPhone(supplierDto.getPhone());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setStreet(supplierDto.getStreet());
        supplier.setExteriorNumber(supplierDto.getExteriorNumber());
        supplier.setNeighborhood(supplierDto.getNeighborhood());
        supplier.setMunicipality(supplierDto.getMunicipality());
        supplier.setState(supplierDto.getState());
        supplier.setStatus(supplierDto.getStatus());

        supplier = this.supplierRepository.save(supplier);
        return new ApiResponseDto(HttpStatus.OK.value(), "Provedor actualizado exitosamente", this.supplierMapper.convertToDto(supplier));
    }

    @Override
    public ApiResponseDto executeGetSupplierList(int page, int size, String supplierId, String status) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Specification<SupplierEntity> filter = SpecificationSupplier.withFilter(supplierId, status);
        Page<SupplierEntity> supplierListBD = this.supplierRepository.findAll(filter,pageable);

        if (supplierListBD.isEmpty())
            throw new NotFoundException("No hay proveedores en el sistema");

        List<SupplierDto> supplierList = supplierListBD.getContent().stream().map(supplierMapper::convertToDto).toList();
        ListDataPaginationDto pagination = new ListDataPaginationDto();
        pagination.setData(Collections.singletonList(supplierList));
        pagination.setTotalElements((int) supplierListBD.getTotalElements());

        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de proveedores", pagination);
    }

    @Override
    public ApiResponseDto executeGetSupplierListBySelect() throws NotFoundException {
        List<SupplierEntity> supplierListBD = this.supplierRepository.findAll();
        if (supplierListBD.isEmpty())
            throw new NotFoundException("No hay proveedores en el sistema");

        List<SupplierDto> supplierList = supplierListBD.stream().map(supplierMapper::convertToDto).toList();
        return new ApiResponseDto(HttpStatus.OK.value(), "Listado de proveedores", supplierList);
    }

    @Override
    public ApiResponseDto executeGetSupplier(String supplierId) throws NotFoundException {
        SupplierEntity supplier = this.getSupplierById(supplierId);
        return new ApiResponseDto(HttpStatus.OK.value(), "Información del provedor",this.supplierMapper.convertToDto(supplier));
    }

    @Override
    public ApiResponseDto executeDisabledSupplier(String supplierId, DisabledRegisterDto disabledRegisterDto, BindingResult bindingResult) throws BadRequestException, NotFoundException {
        List<ValidateInputDto> inputValidateList = this.validateInputs.validateInputs(bindingResult);
        if (!inputValidateList.isEmpty())
            throw new BadRequestException("Campos invalidos", inputValidateList);

        SupplierEntity supplier = this.getSupplierById(supplierId);
        if (supplier.getStatus().equals(Status.valueOf(disabledRegisterDto.getStatus())))
            throw new BadRequestException("El proveedor ya tiene este mismo estado "+disabledRegisterDto.getStatus());

        supplier.setStatus(Status.valueOf(disabledRegisterDto.getStatus()));
        supplier = this.supplierRepository.save(supplier);
        return new ApiResponseDto(HttpStatus.OK.value(),"Se actualizo el status del provedor exitosamente", this.supplierMapper.convertToDto(supplier));
    }

    @Override
    public ApiResponseDto executeImportSupplier(MultipartFile file) throws BadRequestException {
        try {
            List<SupplierDto> supplierListExcel = ExcelHelper.readDataSupplierExcel(file.getInputStream());

            List<SupplierEntity> supplierListSave = supplierListExcel.stream().map(supplierDto -> {

                SupplierEntity supplier = new SupplierEntity();
                supplier.setName(supplierDto.getName());
                supplier.setContactPerson(supplierDto.getContactPerson());
                supplier.setPhone(supplierDto.getPhone());
                supplier.setEmail(supplierDto.getEmail());
                supplier.setStreet(supplierDto.getStreet());
                supplier.setExteriorNumber(supplierDto.getExteriorNumber());
                supplier.setInteriorNumber(supplierDto.getInteriorNumber());
                supplier.setNeighborhood(supplierDto.getNeighborhood());
                supplier.setMunicipality(supplierDto.getMunicipality());
                supplier.setState(supplierDto.getState());
                supplier.setStatus(Status.ACTIVE);

                return supplier;
            }).toList();
            this.supplierRepository.saveAll(supplierListSave);
            return new ApiResponseDto(HttpStatus.CREATED.value(),"Se inserto todos los provedores de forma exitosa", "total de registros: "+supplierListSave.size());
        }catch (Exception e){
            throw new BadRequestException("Error al procesar el archivo excel "+ e.getMessage());
        }
    }

    @Override
    public SupplierEntity getSupplierById(String supplierId) throws NotFoundException {
        SupplierEntity supplier = this.supplierRepository.findById(supplierId).orElse(null);
        if (supplier == null)
            throw new NotFoundException("No existe este proveedor en el sistema");

        return supplier;
    }


}
