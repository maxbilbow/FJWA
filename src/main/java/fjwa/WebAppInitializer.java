package fjwa;

import fjwa.config.SecurityConfig;
import fjwa.config.WebConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.nio.charset.StandardCharsets;

import static click.rmx.debug.Bugger.print;


public class WebAppInitializer  implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context)); //ContextLoadListener is here
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);

		addMappings(dispatcher);

		setUpFilterMapping(servletContext);

		//for webSockets
		dispatcher.setInitParameter("dispatchOptionsRequest", "true");
		dispatcher.setAsyncSupported(true);

		print("Started");
		//Start rabbitMQ Debug log receiver
//		try
//			WebBugger.getInstance().startDebugQueue();
//			//ReceiveLogsTopic.receive("#.log.#", "#.error.#");
//		} catch (Exception e) {
//			WebBugger.getInstance().addException(e, "Rabbit Topic server failed.");
//		}

	}

	private void addMappings(ServletRegistration.Dynamic dispatcher) {
		dispatcher.addMapping("*.html");
		dispatcher.addMapping("*.pdf");
		dispatcher.addMapping("*.json");
		dispatcher.addMapping("*.xml");
		dispatcher.addMapping("*.js");
		dispatcher.addMapping("*.css");
	}

	private void setUpFilterMapping(ServletContext servletContext) {
		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("SpringOpenEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
		encodingFilter.setInitParameter("SpringOpenEntityManagerInViewFilter", "/*");
		encodingFilter.addMappingForUrlPatterns(null,false,"/*");

		//For Security
		servletContext.addFilter("securityFilter",
				new DelegatingFilterProxy("springSecurityFilterChain"))
				.addMappingForUrlPatterns(null, false, "/*");

		//For Sockets?
		servletContext.addFilter("characterEncodingFilter", characterEncodingFilter());
	}

	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(WebConfig.class, SecurityConfig.class);
		return context;
	}



	//TODO
	private CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
		return  characterEncodingFilter;
	}


}
