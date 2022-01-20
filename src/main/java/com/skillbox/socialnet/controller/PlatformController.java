package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.LocationDTO;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
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
    public ResponseEntity<GeneralListResponse<LocationDTO>> getLanguages(
            @RequestParam(defaultValue = "") String language,
            ElementPageable pageable){
        return ResponseEntity.ok(platformService.getLanguage(language, pageable));
    }

    @MethodLog
    @GetMapping("/countries")
    public ResponseEntity<GeneralListResponse<LocationDTO>> getCountry(ElementPageable pageable){
        return ResponseEntity.ok(platformService.getCountry(pageable));
    }

    @MethodLog
    @PostMapping("/countries")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setCountry(@RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok(new GeneralResponse<>(platformService.addCountry(locationDTO)));
    }

    @MethodLog
    @GetMapping("/cities")
    public ResponseEntity<GeneralListResponse<LocationDTO>> getCity(ElementPageable pageable){
        return ResponseEntity.ok(platformService.getCity(pageable));
    }

    @MethodLog
    @PostMapping("/cities")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> setCity(@RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok(new GeneralResponse<>(platformService.addCity(locationDTO)));
    }

}
