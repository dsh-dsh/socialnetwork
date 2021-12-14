package com.skillbox.socialnet.service;


import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {


    public DefaultRS<?> getLanguage(String language, Pageable pageable) {

        List<LocationDTO> languages = List.of(new LocationDTO(1, "Русский"));

        return DefaultRSMapper.of(languages, pageable);
    }

    public DefaultRS<?> getCity(String city, Pageable pageable) {

        List<LocationDTO> cities = List.of(
                new LocationDTO(1, "Москва"),
                new LocationDTO(1, "Краснодар"),
                new LocationDTO(1, "Тель-Авив"),
                new LocationDTO(1, "Серов"));

        return DefaultRSMapper.of(cities, pageable);
    }

    public DefaultRS<?> getCountry(String country, Pageable pageable) {

        List<LocationDTO> countries = List.of(
                new LocationDTO(1, "Россия"),
                new LocationDTO(1, "Израиль"));

        return DefaultRSMapper.of(countries, pageable);
    }

}
