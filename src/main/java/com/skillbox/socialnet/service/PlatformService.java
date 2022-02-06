package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.ErrorResponse;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.entity.City;
import com.skillbox.socialnet.model.entity.Country;
import com.skillbox.socialnet.model.entity.Language;
import com.skillbox.socialnet.repository.CityRepository;
import com.skillbox.socialnet.repository.CountryRepository;
import com.skillbox.socialnet.repository.LanguageRepository;
import com.skillbox.socialnet.util.anotation.MethodLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.skillbox.socialnet.util.Constants.NOT_VALID_LOCAL_MESSAGE;

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @MethodLog
    public GeneralListResponse<LocationDTO> getLanguage(String language, Pageable pageable) {
        Page<Language> languagePage = languageRepository.findAll(pageable);
        List<LocationDTO> languages = languagePage.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());

        return new GeneralListResponse<>(languages, languagePage);
    }

    @MethodLog
    public GeneralListResponse<LocationDTO> getCity(Pageable pageable) {
        Page<City> cityPage = cityRepository.findAll(pageable);
        List<LocationDTO> cities = cityPage.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());

        return new GeneralListResponse<>(cities, cityPage);
    }

    @MethodLog
    public GeneralListResponse<LocationDTO> getCountry(Pageable pageable) {
        Page<Country> cityPage = countryRepository.findAll(pageable);
        List<LocationDTO> countries = cityPage.stream()
                .map(LocationDTO::getLocationDTO)
                .collect(Collectors.toList());

        return new GeneralListResponse<>(countries, pageable);
    }

    @MethodLog
    public MessageOkDTO addCity(LocationDTO cityDTO) {
        City city = cityRepository.findByTitle(cityDTO.getTitle())
                .orElseGet(() -> createNewCity(cityDTO));
        return new MessageOkDTO();
    }

    @MethodLog
    public MessageOkDTO addCountry(LocationDTO locationDTO) {
        Country country = countryRepository.findByTitle(locationDTO.getTitle())
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
