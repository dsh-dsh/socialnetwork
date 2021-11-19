package com.skillbox.socialnet.service;


import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.LocationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;

@Service
public class PlatformService {


    public DefaultRS getLanguage(int offset, int itemPerPage, String language) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO languageDTO = new LocationDTO();
//        languageDTO.setTitle(language);
        languageDTO.setTitle("language");
        languageDTO.setId(0);
        defaultRS.setData(languageDTO);
        return defaultRS;
    }

    public DefaultRS getCity(int offset, int itemPerPage, String city) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        LocationDTO cityDTO = new LocationDTO();
//        cityDTO.setTitle(city);
        cityDTO.setTitle("city");
        cityDTO.setId(0);
        defaultRS.setData(cityDTO);
        return defaultRS;
    }

    public DefaultRS getCountry(int offset, int itemPerPage, String country) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        LocationDTO countryDTO = new LocationDTO();
//        countryDTO.setTitle(country);
        countryDTO.setTitle("country");
        countryDTO.setId(0);
        defaultRS.setData(countryDTO);
        return defaultRS;
    }

}
