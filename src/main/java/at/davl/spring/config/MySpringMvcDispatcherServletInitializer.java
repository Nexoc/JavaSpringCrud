package at.davl.spring.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class}; // linked to SpringConfig
    }

    @Override
    protected String[] getServletMappings() {

        return new String[] {"/"}; // <url-pattern>/</url-pattern> -> to dispatcher
    }
}