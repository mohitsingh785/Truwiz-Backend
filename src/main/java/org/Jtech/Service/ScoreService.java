package org.Jtech.Service;


import org.Jtech.Constant.ProductContext;
import org.Jtech.DTO.*;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.scoring.ScoringEngine;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class ScoreService {

    private final UserService userService;
    private final InciService inciService;
    private final ScoringEngine scoringEngine;

    public ScoreService(UserService userService,InciService inciService,ScoringEngine scoringEngine){
        this.userService=userService;
        this.inciService=inciService;
        this.scoringEngine=scoringEngine;
    }

    public ScoreResult getProductScore(ProductDetailsDto product, Set<Long> inciIds, Long userId) {

        // Get the User profile Details using the userId Preload the UserConcern as well
        UserScoringProfile userScoringProfile= userService.loadUserScoreProfile(userId);

        // Get the INCI Details using the inciIds
        Map<Long, IngredientScoreData> ingredientScoreDataMap=inciService.findScoreDatabyIds(inciIds,userScoringProfile.concerns());

        ProductContext productContext = new ProductContext(product.area(), product.contactType());
        // Creating the Scoring Input to prepare the data for ScoringEngine.score, it will be containing List<ProductIngredientLineDto>, Map<Long, IngredientScoreData>,UserScoringProfile and the productContext(ex. Hair or Skin)
        ScoringInput scoringInput= new ScoringInput(product.ingredients(),ingredientScoreDataMap,userScoringProfile,productContext);
        // Calling the ScoreEngine and calculate the actual score using the provided details and save it to ScoreResult DTO
        // Return the scoreResult back to ScanService
        return scoringEngine.score(scoringInput);
    }
}
