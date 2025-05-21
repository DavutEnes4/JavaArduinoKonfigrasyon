package tr.com.my_app.config;

import jakarta.servlet.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import tr.com.my_app.advice.RequestResponseLoggingFilter;

import java.util.EnumSet;

public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        // Spring context
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebConfig.class);
        ctx.setServletContext(sc);

        // DispatcherServlet
        ServletRegistration.Dynamic servlet =
                sc.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        FilterRegistration.Dynamic reg =
                sc.addFilter("requestLogging", new RequestResponseLoggingFilter());
        reg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
        reg.setAsyncSupported(true);   // isteğe bağlı
    }
}
