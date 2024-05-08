package tech.study.urlshortener.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.study.urlshortener.controller.dto.ShortenUrlRequest;
import tech.study.urlshortener.controller.dto.ShortenUrlResponse;
import tech.study.urlshortener.entities.UrlEntity;
import tech.study.urlshortener.repository.Urlrepository;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class URLController {

    private final Urlrepository urlrepository;

    public URLController(Urlrepository urlrepository) {
        this.urlrepository = urlrepository;
    }

    @PostMapping(value = "/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenerURL(@RequestBody ShortenUrlRequest request,
                                             HttpServletRequest servletRequest){
        String id;
        do{
            id = RandomStringUtils.randomAlphanumeric(5, 10);
        }while (urlrepository.existsById(id));

        urlrepository.save(new UrlEntity(id, request.url(), LocalDateTime.now().plusMinutes(1)));
        var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);
        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }
    @GetMapping("{id}")
    public  ResponseEntity<Void> redirect(@PathVariable("id")String id){
        var url = urlrepository.findById(id);
        if(url.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();

    }
}
