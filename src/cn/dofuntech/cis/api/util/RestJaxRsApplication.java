package cn.dofuntech.cis.api.util;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class RestJaxRsApplication extends ResourceConfig {
	public RestJaxRsApplication() {
		packages("cn.dofuntech.cis.api.resource");

		// register filters
		register(RequestContextFilter.class);

		// register exception mappers
		register(ExceptionMapperSupport.class);

		// register features
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
	}

}
