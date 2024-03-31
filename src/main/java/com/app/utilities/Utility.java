package com.app.utilities;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.app.dto.UserDTO;
import com.app.exception.ServerError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


@Component
public class Utility {
	
    public static  final ModelMapper modelMapper = new ModelMapper();
    public static  final ObjectMapper objectMapper = new ObjectMapper();
    
    private static UserDTO sessionUser;


    // Map one object to another
    public static <S, D> D mapObject(S source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }
    
    public static <T> String objectToString(T object) throws JsonProcessingException {
            return objectMapper.writeValueAsString(object);
      
    }
    
    public static <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationType) {
        return sourceList.stream()
                .map(source -> modelMapper.map(source, destinationType))
                .collect(Collectors.toList());
    }
    
    public static UserDTO getSessionUser() {
		return sessionUser;
	}
    
	public static void setSessionUser(UserDTO sessionUser) {
		
		Utility.sessionUser = sessionUser;
	}
	
	public static LocalDate parseDate(String dateString) {
		dateString = dateString.trim();
		System.out.println("dateString---"+dateString);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    try {
	        return LocalDate.parse(dateString, formatter);
	    } catch (DateTimeParseException e) {
	    	   e.printStackTrace();
	        throw new ServerError("Error in parsing String to LocalDateTime"+e);
	    }
	}

	 public static <T> T updateObjectWithNonNullFields(T target, T source) {
	        if (target == null || source == null) {
	            throw new IllegalArgumentException("Both target and source must not be null.");
	        }

	        if (!target.getClass().equals(source.getClass())) {
	            throw new IllegalArgumentException("Target and source must be of the same type.");
	        }

	        Field[] fields = source.getClass().getDeclaredFields();
	        for (Field sourceField : fields) {
	            int modifiers = sourceField.getModifiers();
	            // Skip static or final fields
	            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
	                continue;
	            }

	            try {
	                sourceField.setAccessible(true); // Access private fields
	                Object value = sourceField.get(source);

	                // Only update if the source field value is not null
	                if (value != null) {
	                    Field targetField = target.getClass().getDeclaredField(sourceField.getName());
	                    targetField.setAccessible(true); // Access private fields
	                    targetField.set(target, value);
	                }
	            } catch (IllegalAccessException | NoSuchFieldException e) {
	                // Handle exceptions (e.g., field does not exist on the target object)
	                e.printStackTrace();
	            }
	        }

	        return target; // Return the updated target object
	    }

}
