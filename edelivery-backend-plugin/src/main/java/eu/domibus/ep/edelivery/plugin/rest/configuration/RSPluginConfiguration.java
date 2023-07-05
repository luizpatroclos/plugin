package eu.domibus.ep.edelivery.plugin.rest.configuration;

import eu.domibus.api.spring.DomibusWebContext;
import eu.domibus.common.NotificationType;
import eu.domibus.ep.edelivery.plugin.rest.controller.RestBackendPlugin;
import eu.domibus.ep.edelivery.plugin.rest.properties.RSPluginPropertyManager;
import eu.domibus.ep.edelivery.plugin.rest.security.PluginAuthenticationInterceptor;
import eu.domibus.ext.services.DomibusPropertyExtService;
import eu.domibus.logging.DomibusLogger;
import eu.domibus.logging.DomibusLoggerFactory;
import eu.domibus.plugin.environment.DomibusEnvironmentUtil;
import eu.domibus.plugin.notification.PluginAsyncNotificationConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.jms.Queue;
import java.util.List;


@EnableWebMvc
@Configuration("rspluginConfiguration")
@DomibusWebContext
public class RSPluginConfiguration implements WebMvcConfigurer {

    private static final DomibusLogger LOG = DomibusLoggerFactory.getLogger(RSPluginConfiguration.class);
    public static final String NOTIFY_BACKEND_QUEUE_JNDI = "jms/domibus.notification.restservice";


    @Bean("restBackendPlugin")
    public RestBackendPlugin createRestPlugin(DomibusPropertyExtService domibusPropertyExtService){

        List<NotificationType> messageNotifications = domibusPropertyExtService.getConfiguredNotifications(RSPluginPropertyManager.MESSAGE_NOTIFICATIONS);
        RestBackendPlugin pluginController = new RestBackendPlugin();
        pluginController.setRequiredNotifications(messageNotifications);
        return pluginController;
    }

    @Bean("restServiceAsyncPluginConfiguration")
    public PluginAsyncNotificationConfiguration pluginAsyncNotificationConfiguration(@Qualifier("notifyBackendRestServiceQueue") Queue notifyBackendRestServiceQueue,
                                                                                     RestBackendPlugin pluginController,
                                                                                     Environment environment) {
        PluginAsyncNotificationConfiguration pluginAsyncNotificationConfiguration = new PluginAsyncNotificationConfiguration(pluginController, notifyBackendRestServiceQueue);
        if (DomibusEnvironmentUtil.INSTANCE.isApplicationServer(environment)) {
            String queueNotificationJndi = NOTIFY_BACKEND_QUEUE_JNDI;
            LOG.debug("Domibus is running inside an application server. Setting the queue name to [{}]", queueNotificationJndi);
            pluginAsyncNotificationConfiguration.setQueueName(queueNotificationJndi);
        }
        return pluginAsyncNotificationConfiguration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(pluginAuthenticationInterceptor())
                .addPathPatterns("/ext/api/v1/ep/**")
                .order(0);
    }

    @Bean
    public PluginAuthenticationInterceptor pluginAuthenticationInterceptor(){
        return new PluginAuthenticationInterceptor();
    }

}