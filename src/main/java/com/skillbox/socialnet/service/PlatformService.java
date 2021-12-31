package com.skillbox.socialnet.service;


import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.LocationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {


    public GeneralListResponse<?> getLanguage(String language, Pageable pageable) {

        List<LocationDTO> languages = List.of(new LocationDTO(1, "Русский"));

        return new GeneralListResponse<>(languages, pageable);
    }

    public GeneralListResponse<?> getCity(String city, Pageable pageable) {

        List<LocationDTO> cities = List.of(
                new LocationDTO(1, "Москва"),
                new LocationDTO(1, "Краснодар"),
                new LocationDTO(1, "Тель-Авив"),
                new LocationDTO(1, "Серов"));

        return new GeneralListResponse<>(cities, pageable);
    }

    public GeneralListResponse<?> getCountry(String country, Pageable pageable) {

        List<LocationDTO> countries = List.of(
                new LocationDTO(1, "Россия"),
                new LocationDTO(1, "Израиль"));

        return new GeneralListResponse<>(countries, pageable);
    }

}
