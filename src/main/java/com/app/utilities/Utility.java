package com.app.utilities;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.app.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Utility {
	
    public static  final ModelMapper modelMapper = new ModelMapper();
    public static  final ObjectMapper objectMapper = new ObjectMapper();
    
    private static UserDTO sessionUser;


    // Map one object to another
    public static <S, D> D mapObject(S source, Class<D> destinationType) {
    	System.out.println("v-------"+source.toString());
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
	


}
