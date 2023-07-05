package eu.domibus.ep.edelivery.plugin.rest.security;

import eu.domibus.ext.services.AuthenticationExtService;
import eu.domibus.logging.DomibusLogger;
import eu.domibus.logging.DomibusLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;

/**
 * @author Luiz Albuquerque
 *
 */
@Component
public class PluginAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static final DomibusLogger LOG = DomibusLoggerFactory.getLogger(PluginAuthenticationInterceptor.class);

    @Autowired
    AuthenticationExtService authenticationExtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.debug("Intercepted request for {}", request.getRequestURI());

        try {

            authenticationExtService.authenticate(request);

            return true;
        } catch (Exception e) {
            response.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        request.logout();
    }

}