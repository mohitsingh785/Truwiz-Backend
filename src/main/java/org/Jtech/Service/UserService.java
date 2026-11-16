package org.Jtech.Service;

import org.Jtech.Constant.AllergySeverity;
import org.Jtech.Constant.Concern;
import org.Jtech.Constant.Gender;
import org.Jtech.DTO.*;
import org.Jtech.Entity.Allergies;
import org.Jtech.Entity.User;
import org.Jtech.Entity.UserAllergy;
import org.Jtech.Entity.UserConcern;
import org.Jtech.Exception.AllergyNotFoundException;
import org.Jtech.Exception.InvalidProfileException;
import org.Jtech.Exception.UserNotFoundException;
import org.Jtech.Repository.AllergiesRepository;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Repository.UserRepository;
import org.Jtech.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User Service
 *
 * Purpose:
 * Handles user profile and account-related business logic,
 * including retrieval of personal details and integration
 * with Spring Security for authentication context loading.
 *
 * Scope:
 * - Fetch user profile and personal details
 * - Aggregate core and extended user profile data
 * - Load user information for JWT-based authentication
 *
 * Metadata:
 * Created on : 2025-12-29
 * Author     : Mohit Singh
 *
 * Notes:
 * Authentication operations such as login and password
 * validation are handled outside this service to maintain
 * separation of concerns.
 */



@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository user;

    @Autowired
    private UserDetailsRepository userDetails;

    @Autowired
    private AllergiesRepository allergiesRepository;

    @Autowired
    private UserMapper userMapper;



    /**
     * Retrieve full user profile data using user ID.
     *
     * Used for:
     * - Aggregating core user data with extended profile details
     * - Internal testing and profile retrieval scenarios
     *
     * @param id unique identifier of the user
     * @return aggregated user profile data
     */
    /*
    public UserFullData printUserDetails(Long id){

        UserData userdata=user.alldata(id);
        UserDetailsDTO userDetailsDTO=userDetails.userDetailsData(id);

        // Construct and return UserFullData
        return new UserFullData(
                userdata.getUserName(),
                userdata.getEmail(),
                userdata.getPhoneNumber(),
                userDetailsDTO.getSkinType(),
                userDetailsDTO.getAge(),
                userDetailsDTO.getGender(),
                userDetailsDTO.getSkinColour(),
                userDetailsDTO.getAllergies(),
                userDetailsDTO.getHeightCm(),
                userDetailsDTO.getWeightKg()
        );

    }
/*

    /**
     * Retrieve full user profile data using user ID.
     *
     * Used for:
     * - Aggregating core user data with extended profile details
     * - Internal testing and profile retrieval scenarios
     *
     * @param userId unique identifier of the user
     * @return aggregated user profile data
     */
    /*
    public UserDetailsDTO getUserById(Long userId){
        // Fetch user details from the repository
        return  userDetails.userDetailsData(userId);
    }

    /*
    /**
     * Load user details by username (email).
     *
     * Used by:
     * - Spring Security during JWT authentication
     *
     * @param username user's email address
     * @return authenticated user details
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return  user.findByEmail(username).orElseThrow(()-> new RuntimeException("User Not Found"));
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // GET /v1/users/me/profile
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(Long userId) {
        org.Jtech.Entity.UserDetails details = loadProfile(userId);
        return userMapper.toProfileResponse(details);
    }


    @Transactional(readOnly = true)
    public UserScoringProfile loadUserScoreProfile(Long userId) {
        org.Jtech.Entity.UserDetails details = loadProfile(userId);
        return userMapper.toUserScoringProfileResponse(details);
    }

    // PATCH /v1/users/me/profile — null = unchanged; lists replace the whole set
    @Transactional
    public UserProfileResponse updateMyProfile(Long userId, String email, UserProfileUpdateRequest request) {
        logger.info("Profile update requested for userId={}", userId);
        org.Jtech.Entity.UserDetails details = loadProfile(userId);

        // 1. Validate everything first, so nothing is half-applied
        Gender effectiveGender = request.gender() != null ? request.gender() : details.getGender();
        Boolean effectivePregnant = request.pregnant() != null ? request.pregnant() : details.getPregnant();
        checkPregnancyMatchesGender(effectiveGender, effectivePregnant);

        Map<Long, AllergySeverity> wantedAllergies = null;
        Map<Long, Allergies> foundAllergies = null;
        if (request.allergies() != null) {
            wantedAllergies = toSeverityMap(request.allergies());
            foundAllergies = allergiesRepository.findAllById(wantedAllergies.keySet()).stream()
                    .collect(Collectors.toMap(Allergies::getAllergyId, a -> a));
            if (foundAllergies.size() != wantedAllergies.size()) {
                throw new AllergyNotFoundException("Invalid allergy IDs supplied.", email);
            }
        }

        // 2. Simple fields
        if (request.skinType() != null)   details.setSkinType(request.skinType());
        if (request.scalpType() != null)  details.setScalpType(request.scalpType());
        if (request.age() != null)        details.setAge(request.age());
        if (request.gender() != null)     details.setGender(request.gender());
        if (request.skinColour() != null) details.setSkinColour(request.skinColour());
        if (request.heightCm() != null)   details.setHeightCm(request.heightCm());
        if (request.weightKg() != null)   details.setWeightKg(request.weightKg());
        if (request.pregnant() != null)   details.setPregnant(request.pregnant());
        if (request.acneProne() != null)  details.setAcneProne(request.acneProne());
        if (request.region() != null)     details.setRegion(request.region());

        // 3. Allergies: merge, don't clear + re-add (Hibernate inserts before
        //    orphan deletes → would break uk_user_allergy)
        if (wantedAllergies != null) {
            mergeAllergies(details, wantedAllergies, foundAllergies);
        }

        // 4. Concerns: same merge idea (uk_user_concern)
        if (request.concerns() != null) {
            mergeConcerns(details, request.concerns());
        }

        logger.info("Profile updated for userId={}", userId);
        // Managed entity: changes flush on commit, no save() needed
        return userMapper.toProfileResponse(details);
    }

    // Shared rule, also called from signup (AuthService.registerUser)
    public void checkPregnancyMatchesGender(Gender gender, Boolean pregnant) {
        if (Boolean.TRUE.equals(pregnant) && gender == Gender.MALE) {
            throw new InvalidProfileException("pregnant can only be true when gender is female or other.");
        }
    }

    private org.Jtech.Entity.UserDetails loadProfile(Long userId) {
        return userDetails.findWithProfileByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found", userId));
    }

    // allergyId → severity; null severity kept as null (handled in merge). Duplicates: last one wins.
    private Map<Long, AllergySeverity> toSeverityMap(List<UserAllergyRequest> requests) {
        Map<Long, AllergySeverity> map = new LinkedHashMap<>();
        for (UserAllergyRequest req : requests) {
            map.put(req.getAllergyId(), req.getSeverity());
        }
        return map;
    }

    private void mergeAllergies(org.Jtech.Entity.UserDetails details,
                                Map<Long, AllergySeverity> wanted,
                                Map<Long, Allergies> found) {
        Set<UserAllergy> current = details.getUserAllergies();
        if (current == null) {
            current = new HashSet<>();
            details.setUserAllergies(current);
        }

        // Remove allergies the user no longer has
        current.removeIf(ua -> !wanted.containsKey(ua.getAllergy().getAllergyId()));

        Map<Long, UserAllergy> currentById = current.stream()
                .collect(Collectors.toMap(ua -> ua.getAllergy().getAllergyId(), ua -> ua));

        for (Map.Entry<Long, AllergySeverity> entry : wanted.entrySet()) {
            UserAllergy existing = currentById.get(entry.getKey());
            if (existing != null) {
                // Kept: update severity only if one was sent
                if (entry.getValue() != null) {
                    existing.setSeverity(entry.getValue());
                }
            } else {
                // New: default MODERATE
                AllergySeverity severity = entry.getValue() != null ? entry.getValue() : AllergySeverity.MODERATE;
                current.add(new UserAllergy(details, found.get(entry.getKey()), severity));
            }
        }
    }

    private void mergeConcerns(org.Jtech.Entity.UserDetails details, Set<Concern> requested) {
        Set<Concern> wanted = new HashSet<>(requested);
        wanted.remove(null);   // ignore "concerns": [null]

        details.getUserConcerns().removeIf(uc -> !wanted.contains(uc.getConcern()));

        Set<Concern> existing = details.getUserConcerns().stream()
                .map(UserConcern::getConcern)
                .collect(Collectors.toSet());

        wanted.stream()
                .filter(c -> !existing.contains(c))
                .forEach(details::addConcern);
    }

}
