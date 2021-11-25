package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.service.PlatformService;
import lombok.RequiredArgsConstructor;
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
            @RequestParam String language,
            @RequestParam (defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int itemPerPage
    ){
        return ResponseEntity.ok(platformService.getLanguage(offset, itemPerPage, language));
    }


    @GetMapping("/country")
    public ResponseEntity<?> getCountry(
            @RequestParam(defaultValue = "") String country,
            @RequestParam (defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int itemPerPage
    ){
        return ResponseEntity.ok(platformService.getCountry(offset, itemPerPage, country));
    }

    @GetMapping("/city")
    public ResponseEntity<?> getCity(
            @RequestParam(defaultValue = "") String city,
            @RequestParam (defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int itemPerPage
    ){
        return ResponseEntity.ok(platformService.getCity(offset, itemPerPage, city));
    }

}
