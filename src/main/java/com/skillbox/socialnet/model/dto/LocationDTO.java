package com.skillbox.socialnet.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.socialnet.model.entity.City;
import com.skillbox.socialnet.model.entity.Country;
import com.skillbox.socialnet.model.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String title;

    public static LocationDTO getLocationDTO(Language language) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.id = language.getId();
        locationDTO.title = language.getTitle();
        return locationDTO;
    }

    public static LocationDTO getLocationDTO(City city) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.id = city.getId();
        locationDTO.title = city.getTitle();
        return locationDTO;
    }

    public static LocationDTO getLocationDTO(Country country) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.id = country.getId();
        locationDTO.title = country.getTitle();
        return locationDTO;
    }
}
