package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.skillbox.socialnet.util.Constants.NOT_VALID_LOCAL_MESSAGE;

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public List<LocationDTO> getLanguage() {
        List<Language> languages = languageRepository.findAll();
        return languages.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());
    }

    public List<LocationDTO> getCity() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .sorted(Comparator.comparing(City::getTitle))
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());
    }

    public List<LocationDTO> getCountry() {
        List<Country> cities = countryRepository.findAll();
        return cities.stream()
                .sorted(Comparator.comparing(Country::getTitle))
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());
    }

    public MessageOkDTO addCity(LocationDTO cityDTO) {
        cityRepository.findByTitle(cityDTO.getTitle())
                .orElseGet(() -> createNewCity(cityDTO));
        return new MessageOkDTO();
    }

    public MessageOkDTO addCountry(LocationDTO locationDTO) {
        countryRepository.findByTitle(locationDTO.getTitle())
                .orElseGet(() -> createNewCountry(locationDTO));
        return new MessageOkDTO();
    }

    private City createNewCity(LocationDTO locationDTO) {
        if (locationDTO.getTitle().trim().length() < 3 || locationDTO.getTitle() == null){
            throw new BadRequestException(NOT_VALID_LOCAL_MESSAGE);
        }
        City newCity = new City();
        newCity.setTitle(locationDTO.getTitle());
        return cityRepository.save(newCity);
    }

    private Country createNewCountry(LocationDTO locationDTO) {
        if (locationDTO.getTitle().trim().length() < 3 || locationDTO.getTitle() == null){
            throw new BadRequestException(NOT_VALID_LOCAL_MESSAGE);
        }
        Country newCountry = new Country();
        newCountry.setTitle(locationDTO.getTitle());
        return countryRepository.save(newCountry);
    }
}
