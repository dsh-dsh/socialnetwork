package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.service.AuthService;
import com.skillbox.socialnet.service.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/platform")
public class PlatformController {

    private final PlatformService platformService;
    private final AuthService authService;

    @GetMapping("/languages")
    public ResponseEntity<GeneralListResponse<LocationDTO>> getLanguages(
            @RequestParam(defaultValue = "") String language){
        return ResponseEntity.ok(
                new GeneralListResponse<>(platformService.getLanguage()));
    }

    @GetMapping("/countries")
    public ResponseEntity<GeneralListResponse<LocationDTO>> getCountry(){
        return ResponseEntity.ok(
                new GeneralListResponse<>(platformService.getCountry()));
    }

    @PostMapping("/countries")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setCountry(
            @RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok(new GeneralResponse<>(platformService.addCountry(locationDTO)));
    }

    @GetMapping("/cities")
    public ResponseEntity<GeneralListResponse<LocationDTO>> getCity(){
        return ResponseEntity.ok(
                new GeneralListResponse<>(platformService.getCity()));
    }

    @PostMapping("/cities")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setCity(
            @RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok(new GeneralResponse<>(platformService.addCity(locationDTO)));
    }

}
