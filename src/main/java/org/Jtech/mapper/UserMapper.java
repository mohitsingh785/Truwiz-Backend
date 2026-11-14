package org.Jtech.mapper;


import org.Jtech.Constant.Concern;
import org.Jtech.DTO.*;
import org.Jtech.Entity.User;
import org.Jtech.Entity.UserConcern;
import org.Jtech.Entity.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {


    // Save the UserDetailsDto into the UserDetails Entity
    public UserDetails createUserDetails(UserDetailsDTO userDetailsDTO) {

        // 1. Build the entity with the required fields (same as before)
        UserDetails userDetails = new UserDetails(
                userDetailsDTO.getSkinType(),
                userDetailsDTO.getScalpType(),
                userDetailsDTO.getAge(),
                userDetailsDTO.getGender(),
                userDetailsDTO.getSkinColour(),
                userDetailsDTO.getHeightCm(),
                userDetailsDTO.getWeightKg()
        );

        // 2. Add concerns, if the user chose any
        if (userDetailsDTO.getConcerns() != null) {
            for (Concern concern : userDetailsDTO.getConcerns()) {
                userDetails.addConcern(concern);
            }
        }

        // 3. Optional scoring inputs. null booleans stay null ("didn't answer").
        userDetails.setPregnant(userDetailsDTO.getPregnant());
        userDetails.setAcneProne(userDetailsDTO.getAcneProne());
        if (userDetailsDTO.getRegion() != null) {
            userDetails.setRegion(userDetailsDTO.getRegion());
        }   // else keep the entity default: Region.IN

        // 4. Now return it
        return userDetails;
    }

    // Entity → profile response (used by GET and PATCH /v1/users/me/profile)
    public UserProfileResponse toProfileResponse(UserDetails details) {

        List<UserProfileResponse.AllergyItem> allergies = details.getUserAllergies() == null
                ? List.of()
                : details.getUserAllergies().stream()
                  .map(ua -> new UserProfileResponse.AllergyItem(
                          ua.getAllergy().getAllergyId(),
                          ua.getAllergy().getAllergyName(),
                          ua.getSeverity()))
                  .sorted(Comparator.comparing(UserProfileResponse.AllergyItem::allergyId))
                  .toList();

        Set<Concern> concerns = details.getUserConcerns().stream()
                .map(UserConcern::getConcern)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Concern.class)));

        return new UserProfileResponse(
                details.getSkinType(),
                details.getScalpType(),
                details.getAge(),
                details.getGender(),
                details.getSkinColour(),
                details.getHeightCm(),
                details.getWeightKg(),
                details.getPregnant(),
                details.getAcneProne(),
                details.getRegion(),
                allergies,
                concerns
        );
    }


    // User + profile + token → login response
    public LoginResponse createLoginResponse(User user, UserProfileResponse profile, String token) {
        UserAccountInfo account = new UserAccountInfo(
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
        return new LoginResponse(true, user.getUserId(), token, account, profile);
    }
}
