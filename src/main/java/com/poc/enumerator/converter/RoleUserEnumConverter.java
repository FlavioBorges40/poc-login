package com.poc.enumerator.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poc.enumerator.RoleUserEnum;

@Converter
public class RoleUserEnumConverter implements AttributeConverter<RoleUserEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RoleUserEnum roleUserEnum) {
        return Objects.isNull(roleUserEnum) ? null : roleUserEnum.getValor();
    }

    @Override
    public RoleUserEnum convertToEntityAttribute(Integer valor) {
        return Objects.isNull(valor) ? null : RoleUserEnum.getValor(valor);
    }
}
