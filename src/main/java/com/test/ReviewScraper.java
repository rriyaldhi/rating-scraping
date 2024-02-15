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
        headers.set("User-Agent", "PostmanRuntime/7.36.3");
        headers.set("Cookie", "ServerPool=A; TASession=V2ID.C6FD98E2720E4642B6D3E5FB4B077312*SQ.1*HS.recommended*ES.popularity*DS.5*SAS.popularity*FPS.oldFirst*FA.1*DF.0*TRA.true*LD.25360544; TATravelInfo=V2*A.2*MG.-1*HP.2*FL.3*RS.1; TAUD=RDD-1707991589489-2024_02_15; TAUnique=%1%enc%3AMtxYXGoLkee6Q7a2yO5vq%2BPbxOTgxHRX76Wxi86D6GK%2Fg9pFKXSrcG%2BISeEFa2PWNox8JbUSTxk%3D; _abck=AE9944991588CEEF10ED1603CA045014~-1~YAAQru8ZuGrMq2KNAQAA4WI7rAtgofbRZrus0n/ZTVKLYLDW3V95GuJbKi5zO87GzQ2ilja3buF3WxvQo0v3K8W7J8ywmsWokLzH8IUCsoLf0nTXi7vbmDd1bcu3GWXdUcbzWq+mGOgxHECkftx8FGPWSqLW4MWoJmypxjOcGnuxczhyMXF7HFOvd2XYBXbpwO46acvf3ND7BJYvYgQIbpY8DnJ0VTsXorWXJChiUbXjUPRLaJrb3nZRZNtACsx4+u0EEAREQFN2QyMa3S8WZBQnq6DOeNsc7thjuuU7uIjGTVa7bw0trsuyWSspQk7zR87oR8w3JU/uiwnF4genqe1Aw4LNiyFiKlonrDXj0IEJDu4cqJJNNIntkVweow==~-1~-1~-1; bm_sz=33BC8A38127E0C85EAF41D38C998EDF2~YAAQru8ZuGvMq2KNAQAA4WI7rBYMitrhBhElCczzwn7rxW4kjAW0ZYXQMBhBWVO1PFr9sLFzn8sBg2IhMMX99udlBOmqljizn7dLxBqKWsdi+b+yxyVz5TQbQQzyiWl8z0CnSFKu65DE6GlkY8TaItNVPn1RyH7czF2YVIRbnGBiTVup2RoH/SZjUSZDyCzl+StTZ7KnQcZ99hQQhX7wliWpqvoBBKKE7+xAKA7RiVAL9vwjbpUCGzOdgts2FH9mr64agVp4LDL4XG/FFRS80TLsiTEZDcHyUw26U0ntJCIh1Aus/+NWKlT/NivqC8VbFdB3P9uOpPV7zq4r4K/B42ot~3621168~4404804; PAC=AAl8nvIaoEP8vSS2kuQFOtr6sg-WD9oPLWbv9x0fnf4s6WwtQ-s5tvOnhG9IgGq1f3AA-OwPu8WCUuoiQMD-phkaxxJEvNLlCYP8lwjnq1cLTjWbN-Q418npjKj3Ew7DiX9vIccSr1NEESx9-LoEKBX9Tqq-nr81tJbzwtbRm7UkORq_sdYKAaLymSsh3tOB9VrQz5uCjJB8PpqKmG4a2A-WdMOqULKld875oeYWHJUdN32OLFrYpPfgVSLFRnFuFQ%3D%3D; PMC=V2*MS.15*MD.20240215*LD.20240215; SRT=TART_SYNC; TADCID=0pW6lEmqZ44X3XZWABQCmq6heh9ZSU2yA8SXn9Wv5HuXK3aP62BAs7vQHnTaPpICh2KZPCd8rhkvOql440yIX1SR-4ippeROgkc; TART=%1%enc%3AOtV8xx7pvloQVVImD3u5osLxRcEJegsmD734KBcyYg2DE6jjs4gjZjoUeZo5vdlR4ieoPdYMDDY%3D; TASID=C6FD98E2720E4642B6D3E5FB4B077312; TASSK=enc%3AAOpHTJ0bxD7kZpt5L4hCLw%2BeEnblReTdLvv2e01UQpl5z%2FFHlyZh809qSz1HJ8zAHWaA2vQk1zNQRB8MvINeMRpRB3AX8cUlosZh3c669pJxHpwC9rDGTkxPHoz8EoZzqQ%3D%3D; TASameSite=1");
        headers.set("Accept-Encoding", "gzip, deflate, br");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Resource> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Resource.class);
        try {
            InputStream inputStream = response.getBody().getInputStream();
            if (response.getHeaders().get("Content-Encoding").get(0).equals("br")) {
                BrotliInputStream brInputStream = new BrotliInputStream(inputStream);
                return Optional.of(new String(brInputStream.readAllBytes(), StandardCharsets.UTF_8));
            } else {
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8));
                return Optional.of(reader.lines().collect(Collectors.joining("\n")));
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
