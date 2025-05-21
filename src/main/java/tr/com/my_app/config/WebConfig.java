package tr.com.my_app.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.WebUtils;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan("tr.com.my_app")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setCookieName("lang");
        clr.setDefaultLocale(new Locale("tr"));
        clr.setCookieMaxAge(7*24*60*60);
        clr.setCookiePath("/");
        return clr;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(
                    HttpServletRequest req,
                    HttpServletResponse res,
                    Object handler) throws Exception {
                if (WebUtils.getCookie(req, "lang") == null) {
                    Locale client = req.getLocale();
                    Cookie c = new Cookie("lang",client.getLanguage());
                    c.setPath("/");
                    c.setMaxAge(7*24*60*60);
                    res.addCookie(c);
                }
                return true;
            }
        });
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
