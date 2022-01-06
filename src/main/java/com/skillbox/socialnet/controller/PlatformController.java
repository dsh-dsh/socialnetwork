package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.service.PlatformService;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/platform")
public class PlatformController {

    private final PlatformService platformService;


    @GetMapping("/languages")
    public ResponseEntity<?> getLanguages(
            @RequestParam(defaultValue = "") String language,
            ElementPageable pageable){
        return ResponseEntity.ok(platformService.getLanguage(language, pageable));
    }


    @GetMapping("/country")
    public ResponseEntity<?> getCountry(
            @RequestParam(defaultValue = "") String country,
            ElementPageable pageable){
        return ResponseEntity.ok(platformService.getCountry(country, pageable));
    }

    @GetMapping("/city")
    public ResponseEntity<?> getCity(
            @RequestParam(defaultValue = "") String city,
            ElementPageable pageable){
        return ResponseEntity.ok(platformService.getCity(city, pageable));
    }

}
