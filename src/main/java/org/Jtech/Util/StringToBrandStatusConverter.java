package org.Jtech.Util;

import org.Jtech.Constant.BrandStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBrandStatusConverter implements Converter<String, BrandStatus> {

    @Override
    public BrandStatus convert(String source) {
        return BrandStatus.fromString(source);
    }
}
