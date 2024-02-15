package com.test;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.BrotliInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ReviewScraper {

    public ReviewScraper() {
        Brotli4jLoader.ensureAvailability();
    }

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
        restTemplate.getMessageConverters().removeIf(m -> m instanceof StringHttpMessageConverter);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Cookie", "TAUnique=%1%enc%3AMtxYXGoLkee6Q7a2yO5vq%2BPbxOTgxHRX76Wxi86D6GK%2Fg9pFKXSrcG%2BISeEFa2PWNox8JbUSTxk%3D;" +
                "TASID=C6FD98E2720E4642B6D3E5FB4B077312;"
        );
        headers.set("Accept-Encoding", "gzip, br");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Resource> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Resource.class);
        try {
            InputStream inputStream = response.getBody().getInputStream();
            if (response.getHeaders().get("Content-Encoding").get(0).equals("br")) {
                BrotliInputStream brInputStream = new BrotliInputStream(inputStream);
                return Optional.of(new String(brInputStream.readAllBytes(), StandardCharsets.UTF_8));
            } else {
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                return Optional.of(new String(gzipInputStream.readAllBytes(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            return Optional.empty();
        }
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
        Element element = document.select("[data-automation='reviewBubbleScore']").first();
        if (element == null) {
            element = document.select(".averageRating__Sg6n").first();
            if (element == null) {
                return null;
            }
        }
        return element.text();
    }
}
