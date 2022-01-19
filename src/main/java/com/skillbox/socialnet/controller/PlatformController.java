package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.entity.City;
import com.skillbox.socialnet.model.entity.Country;
import com.skillbox.socialnet.service.PlatformService;
import com.skillbox.socialnet.util.ElementPageable;
import com.skillbox.socialnet.util.anotation.MethodLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/platform")
public class PlatformController {

    private final PlatformService platformService;

    @MethodLog
    @GetMapping("/languages")
    public ResponseEntity<?> getLanguages(
            @RequestParam(defaultValue = "") String language,
            ElementPageable pageable){
        return ResponseEntity.ok(platformService.getLanguage(language, pageable));
    }

    @MethodLog
    @GetMapping("/countries")
    public ResponseEntity<?> getCountry(ElementPageable pageable){
        return ResponseEntity.ok(platformService.getCountry(pageable));
    }

    @MethodLog
    @PostMapping("/countries")
    public ResponseEntity<?> setCountry(@RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok(platformService.setCountry(locationDTO));
    }

    @MethodLog
    @GetMapping("/cities")
    public ResponseEntity<?> getCity(ElementPageable pageable){
        return ResponseEntity.ok(platformService.getCity(pageable));
    }

    @MethodLog
    @PostMapping("/cities")
    public ResponseEntity<?> setCity(@RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok(platformService.setCity(locationDTO));
    }

}
