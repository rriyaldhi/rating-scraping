package com.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TextSimilarityCalculatorTest {
    private TextSimilaryCalculator textSimilaryCalculator;

    @BeforeEach void setup() {
        textSimilaryCalculator = new TextSimilaryCalculator();
    }

    @Test
    public void shouldReturn1() {
        assertEquals(1.0, textSimilaryCalculator.calculate("Hello world!", "Hello world!"), 0.1);
    }

    @Test
    public void shouldReturn0() {
        assertEquals(0.0, textSimilaryCalculator.calculate("Mathematics is the study of numbers and shapes.", "Rainforests are vital for the health of the planet."), 0.1);
    }
}
