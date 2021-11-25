package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class PersonModelMapper {

    private final ModelMapper modelMapper;

    private final Converter<LocalDateTime, Long> timestampConverter =
            date -> (date.getSource().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());

    public PersonModelMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Person.class, UserDTO.class)
                .addMappings(m -> m.using(timestampConverter).map(Person::getRegData, UserDTO::setRegistrationDate))
                .addMappings(m -> m.using(timestampConverter).map(Person::getBirthDate, UserDTO::setBirthDate))
                .addMappings(m -> m.using(timestampConverter).map(Person::getLastOnlineTime, UserDTO::setLastOnlineTime));
    }
    
    public UserDTO mapToUserDTO(Person person) {
        return modelMapper.map(person, UserDTO.class);
    }
}
