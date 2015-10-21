package fjwa;

import click.rmx.debug.WebBugger;
import fjwa.config.SecurityConfig;
import fjwa.config.WebConfig;
import fjwa.config.WebSocketConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.nio.charset.StandardCharsets;


public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context)); //ContextLoadListener is here
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("*.html");
		dispatcher.addMapping("*.pdf");
		dispatcher.addMapping("*.json");
		dispatcher.addMapping("*.xml");
        dispatcher.addMapping("*.js");
		dispatcher.addMapping("*.css");

		setUpFilterMapping(servletContext);
		setUpSecurityMapping(servletContext);

		try {
			WebBugger.getInstance().startDebugQueue();
			//ReceiveLogsTopic.receive("#.log.#", "#.error.#");
		} catch (Exception e) {
			WebBugger.getInstance().addException(e, "Rabbit Topic server failed.");
		}

	}

	private void setUpFilterMapping(ServletContext servletContext) {
		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("SpringOpenEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
		encodingFilter.setInitParameter("SpringOpenEntityManagerInViewFilter", "/*");
		encodingFilter.addMappingForUrlPatterns(null,false,"/*");
		servletContext.addFilter("characterEncodingFilter", characterEncodingFilter());
	}


	private void setUpSecurityMapping(ServletContext servletContext) {

		servletContext.addFilter("securityFilter",
				new DelegatingFilterProxy("springSecurityFilterChain"))
				.addMappingForUrlPatterns(null, false, "/*");
	}

	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(WebConfig.class,SecurityConfig.class, WebSocketConfig.class);
		return context;
	}

	//TODO
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("dispatchOptionsRequest", "true");
		registration.setAsyncSupported(true);
	}


	//TODO
	private CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
		return  characterEncodingFilter;
	}


}
