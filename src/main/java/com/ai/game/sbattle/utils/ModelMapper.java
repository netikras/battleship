package com.ai.game.sbattle.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by netikras on 17.5.15.
 * <p>
 */
public class ModelMapper {


    private class TransformationMapping<Dto, Model> {

        private Dto dto;
        private Model model;


    }


    /**
     * Transforms Model to DTO object. Both parameters should be non-null.<br/>
     * Method reads all fields of Method object annotated with {@link ModelTransform}.<br/>
     * Mapping to DTO will be done according to rules predefined in the annotations<br/>
     * over Model's fields.
     *
     * @param model Model object the values shall be copied from
     * @param dto   DTO object values should be copied to
     * @return updated DTO object
     * @throws IllegalStateException - various possible reasons. Basically all of them mean that @ModelTransform <br/>
     *                               has some errors or its values do not reflect actual Model/DTO fields. <br/>
     *                               See exception message to figure out the cause for particular cases
     */
    public static <Dto, Model> Dto transform(Model model, Dto dto) {

        if (model == null) {
            return null;
        }

        if (dto == null) {
            throw new IllegalArgumentException("DTO value is null -- cannot project model onto null");
        }


        Field[] fields = model.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return dto;
        }

        for (Field field : fields) {
            try {
                ModelTransform transformAnnotation = field.getAnnotation(ModelTransform.class);
                if (transformAnnotation == null) continue;

                field.setAccessible(true);

                String dtoFieldName = transformAnnotation.dtoFieldName();
                boolean allowNull = transformAnnotation.dtoAllowNull();
                String modelDeeperField = transformAnnotation.dtoValueExtractField();


                Field dtoField = dto.getClass().getDeclaredField(dtoFieldName);
                dtoField.setAccessible(true);


                if (Collection.class.isAssignableFrom(field.getType())) {
                    try {
                        transformCollection(model, dto, field, dtoField, transformAnnotation);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    } catch (InstantiationException e) {
                        throw new IllegalStateException(e);
                    }
                    continue;
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    try {
                        transformMap(model, dto, field, dtoField, transformAnnotation);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else if (Enum.class.isAssignableFrom(field.getType())) {
                    try {
                        transformEnum(model, dto, field, dtoField, transformAnnotation);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                    continue;
                }


                Object value;
                Object deeperValue;

                if (!modelDeeperField.isEmpty()) {

                    String[] fieldAddress = modelDeeperField.split("\\.");

                    try {
                        deeperValue = extractDeeperValue(fieldAddress, field.get(model));
                        value = deeperValue;
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                } else {
                    value = field.get(model);
                }

                try {

                    if (value == null && !allowNull) {
                        throw new IllegalStateException("Cannot assign null to " + dtoField.getType() + " -> " + dtoFieldName);
                    }

                    if (dtoField.getType().isPrimitive() && field.getType().isPrimitive()) {
                        dtoField.set(dto, value);
                        continue;
                    }

                    if (value == null) {
                        dtoField.set(dto, null);
                        continue;
                    }

                    if (dtoField.getType().isAssignableFrom(value.getClass())) {
                        dtoField.set(dto, value);
                    } else {
//                            throw new IllegalStateException("Cannot assign model " + model.getClass().getSimpleName() + " field " + field.getName() + " type " + field.getType() + " to DTO type " + dtoField.getType());
                        try {
                            Object newDtoEntry = dtoField.getType().newInstance();

                            dtoField.set(dto, transform(value, newDtoEntry));

                        } catch (InstantiationException e) {
                            throw new IllegalStateException(e);
                        }

                    }


                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            } catch (NoSuchFieldException nsfe) {
                throw new IllegalStateException(nsfe);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

        }

        return dto;
    }


    public static <ModelT, DtoT> void transformEnum(ModelT model, DtoT dto, Field modelField, Field dtoField, ModelTransform annotation) throws IllegalAccessException {
        Object value;
        value = modelField.get(model);

        if (dtoField.getType().isAssignableFrom(modelField.getType())) { // the same type
            dtoField.set(dto, value);
        } else if (Number.class.isAssignableFrom(dtoField.getType())) { // expecting Ordinal
            dtoField.set(dto, ((Enum) value).ordinal());
        } else if (String.class.isAssignableFrom(dtoField.getType())) { // expecting String
            dtoField.set(dto, ((Enum) value).name());
        }
    }


    public static <ModelT, DtoT> void transformCollection(ModelT model, DtoT dto, Field modelField, Field dtoField, ModelTransform annotation) throws IllegalAccessException, InstantiationException, NoSuchFieldException {

        Object value = modelField.get(model);

        if (!Collection.class.isAssignableFrom(dtoField.getType())) {
            throw new IllegalStateException("Cannot assign Collection field to non-Collection");
        }

        Class<?> modelFieldTypeClass = (Class<?>) ((ParameterizedType) modelField.getGenericType()).getActualTypeArguments()[0];
        Class<?> dtoFieldTypeClass = (Class<?>) ((ParameterizedType) dtoField.getGenericType()).getActualTypeArguments()[0];

        if (dtoFieldTypeClass.isAssignableFrom(modelFieldTypeClass)) {
            dtoField.set(dto, value);
            return;
        }

        String deeperFieldName = annotation.dtoValueExtractField();
        String[] fieldAddress = null;

        if (!deeperFieldName.isEmpty()) {
            fieldAddress = deeperFieldName.split("\\.");
            if (fieldAddress.length == 0) {
                fieldAddress = null;
            }
        }


        Collection dtoCollection = null;

        if (!dtoField.getType().isInterface()) {
            dtoCollection = (Collection) dtoField.getType().newInstance();
        } else {
            if (List.class.isAssignableFrom(dtoField.getType())) {
                dtoCollection = new ArrayList();
            } else if (Set.class.isAssignableFrom(dtoField.getType())) {
                dtoCollection = new HashSet();
            }
        }


        Collection modelValues = (Collection) modelField.get(model);
        if (modelValues != null) {
            for (Iterator iterator = modelValues.iterator(); iterator.hasNext(); ) {
                Object modelCollectionValue = iterator.next();
                Object dtoCollectionValue = null;
//                dtoCollection.add(transform(collectionValue, dtoFieldTypeClass.newInstance()));

                if (fieldAddress != null) {
                    dtoCollectionValue = extractDeeperValue(fieldAddress, modelCollectionValue);
                } else {
                    dtoCollectionValue = transform(modelCollectionValue, dtoFieldTypeClass.newInstance());
                }

                dtoCollection.add(dtoCollectionValue);
            }
        }
        dtoField.set(dto, dtoCollection);

    }


    public static <ModelT, DtoT> void transformMap(ModelT model, DtoT dto, Field modelField, Field dtoField, ModelTransform annotation) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Map value = (Map) modelField.get(model);
        Map dtoValue = null;

        if (!Map.class.isAssignableFrom(dtoField.getType())) {
            throw new IllegalStateException("Cannot assign Map field to non-Map");
        }

        String deeperFieldName = annotation.dtoValueExtractField();
        String dtoKeyFieldName = null;
        String dtoValueFieldName = null;

        if (!deeperFieldName.isEmpty()) {
            String[] fieldNames = deeperFieldName.split(":"); // keyObject.id:valueObject.player.id
            if (fieldNames.length == 1) {
                dtoValueFieldName = fieldNames[0];
            } else if (fieldNames.length == 2) {
                dtoKeyFieldName = fieldNames[0];
                dtoValueFieldName = fieldNames[1];
            }

        }

        Class<?> modelKeyTypeClass = (Class<?>) ((ParameterizedType) modelField.getGenericType()).getActualTypeArguments()[0];
        Class<?> modelValueTypeClass = (Class<?>) ((ParameterizedType) modelField.getGenericType()).getActualTypeArguments()[1];

        Class<?> dtoKeyTypeClass = (Class<?>) ((ParameterizedType) dtoField.getGenericType()).getActualTypeArguments()[0];
        Class<?> dtoValueTypeClass = (Class<?>) ((ParameterizedType) dtoField.getGenericType()).getActualTypeArguments()[1];

        if (dtoKeyFieldName == null && dtoValueFieldName == null) { // Deeper field extraction is not required
            // assigning model's value as-is
            if (!dtoKeyTypeClass.isAssignableFrom(modelKeyTypeClass)) {
                throw new IllegalStateException("Cannot assign Map keys from Model's type " + modelKeyTypeClass + " to DTO's type " + dtoKeyTypeClass);
            }

            if (!dtoValueTypeClass.isAssignableFrom(modelValueTypeClass)) {
                throw new IllegalStateException("Cannot assign Map values from Model's type " + modelValueTypeClass + " to DTO's type " + dtoValueTypeClass);
            }
            dtoField.set(dto, value);
            return;
        }

        dtoValue = (Map) dtoField.getType().newInstance();
        dtoField.set(dto, dtoValue);

        String[] keyFieldAddress = null;
        String[] valueFieldAddress = null;
        if (dtoKeyFieldName != null) {
            keyFieldAddress = dtoKeyFieldName.split("\\.");
        }
        if (dtoValueFieldName != null) {
            valueFieldAddress = dtoValueFieldName.split("\\.");
        }

        for (Iterator entryIter = value.entrySet().iterator(); entryIter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) entryIter.next();

            Object modelKey = entry.getKey();
            Object modelValue = entry.getValue();

            Object dtoMapKey = null;
            Object dtoMapValue = null;

            if (keyFieldAddress != null) {
                dtoMapKey = extractDeeperValue(keyFieldAddress, modelKey);
            } else {
                dtoMapKey = modelKey;
            }

            if (valueFieldAddress != null) {
                dtoMapValue = extractDeeperValue(valueFieldAddress, modelValue);
            } else {
                dtoMapValue = modelValue;
            }

            if (!dtoKeyTypeClass.isAssignableFrom(dtoMapKey.getClass())) {
                dtoMapKey = transform(dtoMapKey, dtoKeyTypeClass.newInstance());
                if (!dtoKeyTypeClass.isAssignableFrom(dtoMapKey.getClass())) {
                    throw new IllegalStateException("Cannot assign Map keys: incompatible types " + dtoMapKey.getClass() + " and " + dtoKeyTypeClass);
                }
            }

            if (!dtoValueTypeClass.isAssignableFrom(dtoMapValue.getClass())) {
                dtoMapValue = transform(dtoMapValue, dtoValueTypeClass.newInstance());
                if (!dtoValueTypeClass.isAssignableFrom(dtoMapValue.getClass())) {
                    throw new IllegalStateException("Cannot assign Map values: incompatible types " + dtoMapValue.getClass() + " and " + dtoValueTypeClass);
                }
            }

            dtoValue.put(dtoMapKey, dtoMapValue);
        }

    }


    public static Object extractDeeperValue(String[] fieldAddress, Object object) throws NoSuchFieldException, IllegalAccessException {
        Field deeperField;
        Object deeperValue = null;

        if (object == null) {
            return null;
        }
        if (fieldAddress.length > 0) { // [player.]address.street.name
            deeperField = object.getClass().getDeclaredField(fieldAddress[0]); // Map<Player, Score>; Player.address.class
            deeperField.setAccessible(true);
            deeperValue = deeperField.get(object);// Player.address (value)

            for (int i = 1; i < fieldAddress.length; i++) { // e.g. "street.name"
                String fieldName = fieldAddress[i]; // "street"
                deeperField = deeperField.getType().getDeclaredField(fieldName); // Player.address.street.class
                deeperField.setAccessible(true);
                Object newValue = deeperField.get(deeperValue); // Player.address.street (value)
                deeperValue = newValue;

                if (deeperValue == null) {
                    break;
                }

            }
        }

        return deeperValue;
    }


    /**
     * Applies all the values back from DTO to Model ignoring what UPDATABLE value is set
     *
     * @param model Model object instructing the mapping. Values will be applied to it
     * @param dto   DTO object containing values that should be set to Model's fields
     * @return Model object with applied values from DTO
     * @throws IllegalStateException - various possible reasons. Basically all of them mean that @ModelTransform <br/>
     *                               has some errors or its values do not reflect actual Model/DTO fields. <br/>
     *                               See exception message to figure out the cause for particular cases
     */
    public static <Dto, Model> Model apply(Model model, Dto dto) {
        return null;
    }


    /**
     * Applies all the values back from DTO to Model respecting UPDATABLE value set
     *
     * @param model Model object instructing the mapping. Values will be applied to it
     * @param dto   DTO object containing values that should be set to Model's fields
     * @return Model object with applied values from DTO
     * @throws IllegalStateException - various possible reasons. Basically all of them mean that @ModelTransform <br/>
     *                               has some errors or its values do not reflect actual Model/DTO fields. <br/>
     *                               See exception message to figure out the cause for particular cases
     */
    public static <Dto, Model> Model applyUpdate(Model model, Dto dto) {
        return null;
    }


}
