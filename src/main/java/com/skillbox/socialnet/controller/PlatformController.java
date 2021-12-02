package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.service.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/platform")
public class PlatformController {

    private final PlatformService platformService;


    @GetMapping("/languages")
    public ResponseEntity<?> getLanguages(
            @RequestParam(defaultValue = "") String language,
            Pageable pageable){
        return ResponseEntity.ok(platformService.getLanguage(language, pageable));
    }


    @GetMapping("/country")
    public ResponseEntity<?> getCountry(
            @RequestParam(defaultValue = "") String country,
            Pageable pageable){
        return ResponseEntity.ok(platformService.getCountry(country, pageable));
    }

    @GetMapping("/city")
    public ResponseEntity<?> getCity(
            @RequestParam(defaultValue = "") String city,
            Pageable pageable){
        return ResponseEntity.ok(platformService.getCity(city, pageable));
    }

}
