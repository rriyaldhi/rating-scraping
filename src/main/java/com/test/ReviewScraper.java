package com.test;

import java.time.Duration;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ReviewScraper {

    public String fetchDataFromURL(String inputURL) throws RestClientException {
        String rating = null;
        Optional<String> fetchedData = fetchPageFromURL(inputURL);
        if (fetchedData.isPresent()) {
            rating = extractRating(fetchedData.get());
        }
        return rating;
    }

    private Optional<String> fetchPageFromURL(String userInput) throws RestClientException {
        String finalUrl = buildURL(userInput);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(15))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/58.0.3029.110 Safari/537.36");
        headers.set("Referer", "http://www.google.com");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, String.class);
        return Optional.ofNullable(response.getBody());
    }

    private String buildURL(String userInput) {
        if (userInput.contains("viator") || userInput.contains("tiqets")) {
            return UriComponentsBuilder.fromHttpUrl("http://webcache.googleusercontent.com/search")
                    .queryParam("q", "cache:" + userInput)
                    .queryParam("strip", "1")
                    .queryParam("vwsrc", "0")
                    .toUriString();
        }
        return userInput;
    }

    private String extractRating(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.select(".averageRating__Sg6n").first();
        if (element == null) {
            return null;
        }
        return element.text();
    }
}
