package eu.domibus.ep.edelivery.plugin.rest.properties;

import eu.domibus.ep.edelivery.plugin.rest.controller.RestBackendPlugin;
import eu.domibus.ep.edelivery.plugin.rest.controller.RestResponseEntityExceptionHandler;
import eu.domibus.ep.edelivery.plugin.rest.dao.MessageLogDao;
import eu.domibus.ep.edelivery.plugin.rest.service.JsonModelTransformer;
import eu.domibus.ep.edelivery.plugin.rest.service.PluginHandleMessageService;
import eu.domibus.ext.services.*;
import eu.domibus.plugin.handler.MessagePuller;
import eu.domibus.plugin.handler.MessageRetriever;
import eu.domibus.plugin.handler.MessageSubmitter;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestContextSetConfig {

        @Bean
        public RestBackendPlugin getRestBackendPlugin() {
            RestBackendPlugin restBackendPlugin = new RestBackendPlugin();
            return restBackendPlugin;
        }

        @Bean
        public MessageRetriever domibusMessageRetriver() {
            return Mockito.mock(MessageRetriever.class);
        }

        @Bean
        public MessageSubmitter domibusMessageSubmitter(){
            return Mockito.mock(MessageSubmitter.class);
        }

        @Bean
        public RestResponseEntityExceptionHandler domibusRestResponseEntityExceptionHandler(){ return Mockito.mock(RestResponseEntityExceptionHandler.class);}

        @Bean
        public MessagePuller domibusMessagePuller(){
            return Mockito.mock(MessagePuller.class);
        }

        @Bean
        public MessageExtService domibusMessageExtService(){
            return Mockito.mock(MessageExtService.class);
        }

        @Bean
        public JsonModelTransformer domibusJsonModelTransformer(){
            return Mockito.mock(JsonModelTransformer.class);
        }

        @Bean
        public FileUtilExtService domibusFileUtilExtService(){
            return Mockito.mock(FileUtilExtService.class);
        }

        @Bean
        public PluginHandleMessageService domibusPluginHandleMessageService(){
            return Mockito.mock(PluginHandleMessageService.class);
        }

        @Bean
        public MessageLogDao domibusMessageLogDao(){
            return Mockito.mock(MessageLogDao.class);
        }

        @Bean
        public MessageAcknowledgeExtService domibusMsgAcknowledgeExt(){ return Mockito.mock(MessageAcknowledgeExtService.class);}

         @Bean
         public RSPluginPropertyManager domibusPropertyManager(){ return Mockito.mock(RSPluginPropertyManager.class);}

         @Bean
         public DomibusPropertyExtService domibusPropertyExtService(){return  Mockito.mock(DomibusPropertyExtService.class);}

         @Bean
         public DomainExtService domibusDomainExtService(){return  Mockito.mock(DomainExtService.class);}
        @Bean
        public AuthenticationExtService domibusAuthentication(){ return Mockito.mock(AuthenticationExtService.class);}

       @Bean
       public DomainContextExtService domibusContextService(){ return Mockito.mock(DomainContextExtService.class);}


}
