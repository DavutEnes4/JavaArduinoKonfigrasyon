package tr.com.my_app.advice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper reqWrapper  = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper resWrapper = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        filterChain.doFilter(reqWrapper, resWrapper);
        long duration = System.currentTimeMillis() - start;

        // Request parametreleri ve body
        String params = Collections.list(reqWrapper.getParameterNames())
                .stream()
                .collect(Collectors.toMap(p -> p, reqWrapper::getParameter))
                .toString();
        String reqBody = new String(reqWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Response body
        String resBody = new String(resWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        resWrapper.copyBodyToResponse();  // Body’yi response’a geri yaz (aksi halde boş döner)

        log.info("{} {} | {} ms | params={} | reqBody={} | status={} | resBody={}",
                request.getMethod(), request.getRequestURI(), duration,
                params, reqBody, response.getStatus(), resBody);
    }

    private String abbreviate(String str, int max) {
        return (str == null || str.length() <= max) ? str : str.substring(0, max) + "...";
    }
}
