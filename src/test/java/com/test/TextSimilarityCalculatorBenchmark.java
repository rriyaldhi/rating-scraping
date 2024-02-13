package com.test;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
public class TextSimilarityCalculatorBenchmark {
    private final TextSimilarityCalculator textSimilarityCalculator = new TextSimilarityCalculator();

    @Benchmark
    public void testCalculate() {
        textSimilarityCalculator.calculate("Chichen Itza, a UNESCO World Heritage Site, is the most important Mayan site in Mexico. Explore its temples and pyramids with an archaeologist on this fascinating cultural tour that also includes a stop in Hispanic-era Valladolid and a swim in the Xcajum cenote. Since you'll be spending more than five hours on the bus, it's imperative that you be looked after, so in addition to food, your package includes a waiter on board and drinks on the go. Enjoy the insights of an archaeologist as you visit Chichen Itza Fuel up with drinks on the bus, including beers and soft drinks, plus lunch Visit the Church of San Servacio in Valladolid Round-trip transfers make things easy, and pickup is offered directly from most hotels\n" +
                "\n" +
                "Read more about Chichen Itza with Cenote and Valladolid - https://www.viator.com/tours/Cancun/Chichen-Itza-Cenote-Valladolid/d631-182716P1?mcid=56757", "Discover the wonders of Chichen Itza, a UNESCO World Heritage Site and the most significant Mayan location in Mexico, on a culturally enriching tour guided by an archaeologist. This journey includes a visit to historic Valladolid and a refreshing swim in the Xcajum cenote. With over five hours of travel time, comfort is ensured with on-board waiter service and a selection of drinks, including beer and soft drinks. Your experience at Chichen Itza is enhanced by the archaeologist's expertise. The package also covers a meal and a visit to the Church of San Servacio in Valladolid, with convenient round-trip transfers and hotel pickup for most locations.\n" +
                "\n" +
                "Learn more about the Chichen Itza, Cenote, and Valladolid tour here: Chichen Itza with Cenote and Valladolid - Viator");
    }
}
