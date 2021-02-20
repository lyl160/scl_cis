package cn.dofuntech.cis.bean;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class RestJaxRsApplication extends ResourceConfig {

    public RestJaxRsApplication() {
        packages("com.dunfeng.mfl.api.resource");

        // register filters
        register(RequestContextFilter.class);

        // register exception mappers
        register(ExceptionMapperSupport.class);

        // register features
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
    }

}
