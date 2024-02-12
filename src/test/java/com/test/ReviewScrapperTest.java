package com.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ReviewScrapperTest {
    private ReviewScraper reviewScraper;

    @BeforeEach
    public void setup() {
        this.reviewScraper = new ReviewScraper();
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.viator.com/tours/Cancun/Chichen-Itza-Cenote-Valladolid/d631-182716P1, 4.0",
        "https://www.viator.com/tours/Playa-del-Carmen/5-hour-Cancun-Turtle-and-Cenote-Snorkeling-Adventure/d5501-12861P8, 5.0",
        "https://www.viator.com/tours/Washington-DC/Washington-DC-Hop-on-Hop-off-Trolley-Tour/d657-5046WAS_OTT, 4.0"
    })
    public void shouldReturnReviews(String url, String rating) {
        assertEquals(reviewScraper.fetchDataFromURL(url), rating);
    }
}
