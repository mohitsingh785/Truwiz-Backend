package org.Jtech.DTO;

import org.Jtech.Constant.*;

public interface UserDetailsView {

    Long getDetailsId();
    SkinType getSkinType();
    ScalpType getScalpType();
    Integer getAge();
    Gender getGender();
    SkinColor getSkinColour();
    Float getHeightCm();
    Float getWeightKg();
}
