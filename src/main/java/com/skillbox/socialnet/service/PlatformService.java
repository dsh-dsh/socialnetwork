package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.entity.City;
import com.skillbox.socialnet.model.entity.Country;
import com.skillbox.socialnet.model.entity.Language;
import com.skillbox.socialnet.repository.CityRepository;
import com.skillbox.socialnet.repository.CountryRepository;
import com.skillbox.socialnet.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public List<LocationDTO> getLanguage(String language) {
        List<Language> languages = languageRepository.findAll();
        return languages.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());
    }

    public List<LocationDTO> getCity() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());
    }

    public List<LocationDTO> getCountry() {
        List<Country> cities = countryRepository.findAll();
        return cities.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());
    }

    public MessageOkDTO addCity(LocationDTO cityDTO) {
        City city = cityRepository.findByTitle(cityDTO.getTitle())
                .orElseGet(() -> createNewCity(cityDTO));

        return new MessageOkDTO();
    }

    public MessageOkDTO addCountry(LocationDTO locationDTO) {
        Country country = countryRepository.findByTitle(locationDTO.getTitle())
                .orElseGet(() -> createNewCountry(locationDTO));

        return new MessageOkDTO();
    }

    private City createNewCity(LocationDTO locationDTO) {
        City newCity = new City();
        newCity.setTitle(locationDTO.getTitle());
        return cityRepository.save(newCity);
    }

    private Country createNewCountry(LocationDTO locationDTO) {
        Country newCountry = new Country();
        newCountry.setTitle(locationDTO.getTitle());
        return countryRepository.save(newCountry);
    }
}
