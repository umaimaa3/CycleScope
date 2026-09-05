package backend.prediction.controller;

import backend.prediction.dto.CyclePredictionDTO;
import org.springframework.web.bind.annotation.*;
import backend.prediction.service.PredictionService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController

// All prediction endpoints begin with /api/prediction
@RequestMapping("/api/prediction")
public class PredictionController {

    // Service responsible for orchestrating the prediction workflow
    private final PredictionService predictionService;

    // Spring injects PredictionService through the constructor
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    // GET /api/prediction
    // Generates or retrieves the appropriate prediction and returns it
    // to the frontend as a CyclePredictionDTO
    @GetMapping
    public CyclePredictionDTO getPrediction() {
        return predictionService.generateNextPrediction();
    }
}
