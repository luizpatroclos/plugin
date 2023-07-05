package eu.domibus.ep.edelivery.plugin.rest.configuration;

import eu.domibus.ep.edelivery.plugin.rest.properties.RSPluginPropertyManager;
import eu.domibus.logging.DomibusLogger;
import eu.domibus.logging.DomibusLoggerFactory;
import eu.domibus.plugin.environment.TomcatCondition;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import static org.springframework.util.unit.DataUnit.MEGABYTES;


@Conditional(TomcatCondition.class)
@Configuration
public class RSPluginTomcatConfiguration {

    @Autowired
    private RSPluginPropertyManager properties;

    private static final DomibusLogger LOG = DomibusLoggerFactory.getLogger(RSPluginTomcatConfiguration.class);

    @Bean("notifyBackendRestServiceQueue")
    public ActiveMQQueue notifyBackendFSQueue() {
        return new ActiveMQQueue("domibus.notification.restservice");
    }


    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartConfigResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        resolver.setMaxUploadSize(DataSize.of(Long.parseLong(properties.getKnownPropertyValue(RSPluginPropertyManager.FILES_MAXFILESIZE).trim()),MEGABYTES).toBytes()); // KB,MB
        resolver.setMaxUploadSizePerFile(DataSize.of(Long.parseLong(properties.getKnownPropertyValue(RSPluginPropertyManager.FILES_MAXREQUESTSIZE).trim()),MEGABYTES).toBytes()); // KB,MB

        return resolver;
    }


}
