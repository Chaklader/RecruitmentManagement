package com.recruitment.manager.exceptionhandling;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;


/**
 * Created by Chaklader on Feb, 2021
 */
public class LowerCaseClassNameResolver extends TypeIdResolverBase {


    @Override
    public String idFromValue(Object value) {

        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {

        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {

        return JsonTypeInfo.Id.CUSTOM;
    }
}
