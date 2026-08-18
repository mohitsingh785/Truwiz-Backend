package org.Jtech.DTO;

import org.Jtech.Constant.Gender;
import org.Jtech.Constant.HairType;
import org.Jtech.Constant.SkinColor;
import org.Jtech.Constant.SkinType;

public interface UserDetailsView {

    Long getDetailsId();
    SkinType getSkinType();
    HairType getHairType();
    Integer getAge();
    Gender getGender();
    SkinColor getSkinColour();
    Float getHeightCm();
    Float getWeightKg();
}
