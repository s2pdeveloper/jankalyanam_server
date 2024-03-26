//package com.app.convertor;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.util.stream.Stream;
//
//
//import com.app.constant.ServiceConstant.BloodGroup;
//
//@Converter(autoApply = true)
//public class BloodGroupConvertor implements AttributeConverter<BloodGroup, String> {
//
//	@Override
//	public String convertToDatabaseColumn(BloodGroup bloodGroup) {
//		if (bloodGroup == null) {
//            return null;
//        }
//        return bloodGroup.getValue();
//	}
//
//	@Override
//	public BloodGroup convertToEntityAttribute(String value) {
//		  if (value == null) {
//		        return null;
//		    }
//		
//		    return Stream.of(BloodGroup.values())
//		            .filter(c -> c.getValue().equals(value))
//		            .findFirst()
//		            .orElseThrow(IllegalArgumentException::new);
//
//	}
//}
