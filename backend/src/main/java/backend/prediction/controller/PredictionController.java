package backend.prediction.controller;

import backend.prediction.dto.CyclePredictionDTO;
import org.springframework.web.bind.annotation.*;
import backend.prediction.service.PredictionService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public CyclePredictionDTO getPrediction() {
        return predictionService.generateNextPrediction();
    }
}
