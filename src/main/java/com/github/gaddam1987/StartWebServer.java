package com.github.gaddam1987;

import com.github.gaddam1987.config.AppConfig;
import com.github.gaddam1987.config.WebMvcConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class StartWebServer {

    public static void main(String[] args) throws Exception {
        WebApplicationContext ctxt = createContext(WebMvcConfig.class, AppConfig.class);
        ContextLoaderListener springContextLoader = createApplicationContext(ctxt);

        Server server = new Server(9090);

        ServletContextHandler context = new ServletContextHandler();
        context.addEventListener(springContextLoader);
        context.addServlet(new ServletHolder(new DispatcherServlet(ctxt)), "/");

        server.setHandler(context);
//        context.addLifeCycleListener(new JettyStartingListener(context.getServletContext()));
        server.start();
        server.join();
    }

    private static ContextLoaderListener createApplicationContext(WebApplicationContext ctxt) {
        return new ContextLoaderListener(ctxt);
    }

    private static WebApplicationContext createContext(final Class... applicationContexts) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(applicationContexts);
        return context;
    }

    public static class JettyStartingListener extends AbstractLifeCycle.AbstractLifeCycleListener {
        private final ServletContext servletContext;

        public JettyStartingListener(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        public void lifeCycleStarting(LifeCycle event) {
            try {
                new ApplicationInitializer().onStartup(servletContext);
            } catch (ServletException e) {
                throw new RuntimeException("Unable to start application");
            }
        }
    }
}
