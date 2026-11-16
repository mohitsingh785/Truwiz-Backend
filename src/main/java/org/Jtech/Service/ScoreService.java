package org.Jtech.Service;


import org.Jtech.DTO.IngredientScoreData;
import org.Jtech.DTO.ProductDetailsDto;
import org.Jtech.DTO.ScoreResult;
import org.Jtech.DTO.UserScoringProfile;
import org.Jtech.Repository.UserDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class ScoreService {

    private final UserService userService;
    private final InciService inciService;

    public ScoreService(UserService userService,InciService inciService){
        this.userService=userService;
        this.inciService=inciService;
    }

    public ScoreResult getProductScore(ProductDetailsDto product, Set<Long> inciIds, Long userId) {

        // Get the User profile Details using the userId Preload the UserConcern as well
        UserScoringProfile userScoringProfile= userService.loadUserScoreProfile(userId);

        // Get the INCI Details using the inciIds
        Map<Long, IngredientScoreData> ingredientScoreDataMap=inciService.findScoreDatabyIds(inciIds,userScoringProfile.concerns());

        // Calling the ScoreEngine and calculate the actual score using the provided details and save it to ScoreResult DTO

//        ScoreResult scoreResult=
        return new ScoreResult();
    }
}
