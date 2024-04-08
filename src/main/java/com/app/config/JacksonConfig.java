package com.app.config;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;

//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        JavaTimeModule module = new JavaTimeModule();
//        // Customize LocalDate serialization
//        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
//
//        return Jackson2ObjectMapperBuilder.json()
//                .modules(module)
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Important
//                .build();
//    }
//}

//import java.time.format.DateTimeFormatter;
//
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        JavaTimeModule module = new JavaTimeModule();
//        // Assuming you want to enforce a specific date format across all LocalDate/LocalDateTime fields
//        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
//        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
//
//        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
//                .modules(module)
//                .build();
//
//        return mapper;
//    }
//}