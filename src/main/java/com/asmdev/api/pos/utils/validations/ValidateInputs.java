package com.asmdev.api.pos.utils.validations;


import com.asmdev.api.pos.dto.ValidateInputDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidateInputs {

    public List<ValidateInputDto> validateInputs(BindingResult bindingResult){
        List<ValidateInputDto> inputs = new ArrayList<>();
        if (bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(inputError ->{
                ValidateInputDto input = new ValidateInputDto();
                input.setInput(inputError.getField());
                input.setInvalidMessage(inputError.getDefaultMessage());
                inputs.add(input);
            });
        }
        return inputs;
    }

}
