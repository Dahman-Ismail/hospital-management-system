package com.example.demo.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    /**
     * View without attributes
     */
    protected ModelAndView view(String filePath) {
        return view(filePath, null);
    }

    /**
     * Main view method (renders layout.jsp)
     */
    protected ModelAndView view(String filePath, Map<String, Object> attributes) {
        ModelAndView mv = new ModelAndView("layout");

        // Auto add .jsp
        if (!filePath.endsWith(".jsp")) {
            filePath = filePath + ".jsp";
        }

        mv.addObject("contentPage", filePath);

        if (attributes != null) {
            attributes.forEach(mv::addObject);
        }

        return mv;
    }

    /**
     * Build attributes map:
     * attrs("key", value, "key2", value2)
     */
    protected Map<String, Object> attrs(Object... pairs) {
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("attrs() requires key/value pairs");
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i].toString(), pairs[i + 1]);
        }
        return map;
    }

    /**
     * Redirect without any extra params
     */
    protected ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }

    /**
     * Redirect with query params:
     * redirect("/patients", attrs("page", 1, "sort", "name"))
     */
    protected ModelAndView redirect(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return redirect(url);
        }

        StringBuilder sb = new StringBuilder(url);
        sb.append("?");

        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        sb.setLength(sb.length() - 1); // remove last "&"

        return new ModelAndView("redirect:" + sb.toString());
    }

    /**
     * Redirect with flash attributes
     * Example:
     * return redirectFlash("/patients", redirectAttributes, "success", "Patient saved");
     */
    protected ModelAndView redirectFlash(String url, RedirectAttributes ra, String key, Object value) {
        ra.addFlashAttribute(key, value);
        return new ModelAndView("redirect:" + url);
    }
}
