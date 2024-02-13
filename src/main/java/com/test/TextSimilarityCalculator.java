package com.test;

import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;

public class TextSimilarityCalculator {
    private static final String OPEN_AI_API_KEY = "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";
    private static final String OPEN_AI_MODEL = "text-embedding-3-small";
    private final OpenAiService openAiService = new OpenAiService(OPEN_AI_API_KEY);

    public double calculate(String first, String second) {
        return calculateCosineSimilarity(calculateEmbedding(first), calculateEmbedding(second));
    }

    private List<Double> calculateEmbedding(String word) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .input(List.of(word))
                .model(OPEN_AI_MODEL)
                .build();
        EmbeddingResult result = openAiService.createEmbeddings(request);
        return result.getData().get(0).getEmbedding();
    }

    private static double calculateCosineSimilarity(List<Double> embedding1, List<Double> embedding2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < embedding1.size(); i++) {
            dotProduct += embedding1.get(i) * embedding2.get(i);
            norm1 += Math.pow(embedding1.get(i), 2);
            norm2 += Math.pow(embedding2.get(i), 2);
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
