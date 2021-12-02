package com.skillbox.socialnet.service;


import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlatformService {


    public DefaultRS<?> getLanguage(String language, Pageable pageable) {
        LocationDTO languageDTO = new LocationDTO();
        languageDTO.setTitle("language");
        languageDTO.setId(1);
        return DefaultRSMapper.of(languageDTO, pageable);
    }

    public DefaultRS<?> getCity(String city, Pageable pageable) {
        LocationDTO cityDTO = new LocationDTO();
        cityDTO.setTitle("city");
        cityDTO.setId(1);
        return DefaultRSMapper.of(cityDTO, pageable);
    }

    public DefaultRS<?> getCountry(String country, Pageable pageable) {
        LocationDTO countryDTO = new LocationDTO();
//        countryDTO.setTitle(country);
        countryDTO.setTitle("country");
        countryDTO.setId(1);
        return DefaultRSMapper.of(countryDTO, pageable);
    }

}
