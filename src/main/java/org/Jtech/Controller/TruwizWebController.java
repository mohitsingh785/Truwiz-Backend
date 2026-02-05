package org.Jtech.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Truwiz Web Controller
 *
 * Purpose:
 * Serves static and informational web pages related to the Truwiz
 * platform, such as Terms and Conditions, Privacy Policy, and
 * other product-related information pages.
 *
 * Scope:
 * - Terms and Conditions page
 * - Informational pages about Truwiz
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This controller is responsible only for rendering web views
 * and does not expose REST APIs or handle business logic.
 */

@Controller
public class TruwizWebController {


    /**
     * Display the Terms and Conditions page.
     *
     * @return Thymeleaf view name for the Terms and Conditions page
     */
    @GetMapping("/terms")
    public String terms() {
        // Return the name of the Thymeleaf template (without the .html extension)
        return "terms-and-conditions";
    }
}
