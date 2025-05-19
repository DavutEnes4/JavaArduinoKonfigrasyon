package tr.com.my_app.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@ControllerAdvice
public class GlobalLangAdvice {

    @ModelAttribute("lang")
    public String getLang(HttpServletRequest request) {
        String lang = "tr"; // Varsayılan dil Türkçe
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("lang".equals(cookie.getName())) {
                    lang = cookie.getValue();
                    break;
                }
            }
        }
        return lang;
    }
}
