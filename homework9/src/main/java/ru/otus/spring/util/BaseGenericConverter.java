package ru.otus.spring.util;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseGenericConverter<T> implements ConditionalGenericConverter {
    private final Class<T> typeClass;

    protected BaseGenericConverter(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return matchesStringToType(sourceType, targetType)
                || matchesTypeToString(sourceType, targetType)
                || matchesStringToTypeSet(sourceType, targetType)
                || matchesTypeSetToString(sourceType, targetType);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(
                new ConvertiblePair(String.class, typeClass),
                new ConvertiblePair(typeClass, String.class),
                new ConvertiblePair(String.class, Set.class),
                new ConvertiblePair(Set.class, String.class)
        );
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (matchesStringToType(sourceType, targetType)) {
            return convertStringToType((String) source);
        } else if (matchesTypeToString(sourceType, targetType)) {
            return convertTypeToString((T)source);
        } else if (matchesStringToTypeSet(sourceType, targetType)) {
            return convertStringToIdSet((String)source);
        } else if (matchesTypeSetToString(sourceType, targetType)) {
            return convertIdSetToString((Set<T>)source);
        }
        return null;
    }

    protected abstract T fromString(String source);

    protected abstract String toString(T source);

    private boolean matchesStringToType(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == String.class
                && targetType.getType() == typeClass;
    }

    private boolean matchesStringToTypeSet(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == String.class
                && targetType.getType() == Set.class
                && targetType.getElementTypeDescriptor() != null && targetType.getElementTypeDescriptor().getType() == typeClass;
    }

    private boolean matchesTypeToString(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == typeClass
                && targetType.getType() == String.class;
    }

    private boolean matchesTypeSetToString(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == Set.class
                && sourceType.getElementTypeDescriptor() != null && sourceType.getElementTypeDescriptor().getType() == typeClass
                && targetType.getType() == String.class;

    }

    private T convertStringToType(String source) {
        return fromString(source);
    }

    private String convertTypeToString(T source) {
        return toString(source);
    }

    private Set<T> convertStringToIdSet(String source) {
        Set<T> ids = new HashSet<>();
        for (String part : source.split(",")) {
            T id = fromString(part);
            if (id == null)
                return null;
            ids.add(id);
        }
        return ids;
    }

    private String convertIdSetToString(Set<T> source) {
        if (source == null)
            return null;
        return source.stream()
                .map(this::toString)
                .collect(Collectors.joining(","));
    }
}