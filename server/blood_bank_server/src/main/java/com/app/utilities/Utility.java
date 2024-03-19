package com.app.utilities;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.app.dto.UserDTO;

@Component
public class Utility {
	
    public static  final ModelMapper modelMapper = new ModelMapper();
    
    private static UserDTO sessionUser;


    // Map one object to another
    public static <S, D> D mapObject(S source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
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
