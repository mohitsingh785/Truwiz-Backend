package org.Jtech.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthCheckWebController {

    @GetMapping("/terms")
    public String terms() {
        // Return the name of the Thymeleaf template (without the .html extension)
        return "terms-and-conditions";
    }
}
