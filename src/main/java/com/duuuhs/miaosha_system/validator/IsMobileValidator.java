package com.duuuhs.miaosha_system.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.duuuhs.miaosha_system.util.Assert.isMobile;
import static com.duuuhs.miaosha_system.util.Assert.isNull;

/**
 * @Author: DMY
 * @Date: 2019/4/16 0:32
 * @Description:
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required){//判断该值是否是必须具备的
            return isMobile(value);
        } else {
            //该值非必须的
            //判断是否为空,空代表这个参数不带,默认校验通过
            if (isNull(value)){
                return true;
            } else {
                return isMobile(value);
            }
        }
    }
}
