package us.galleryw.ufc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.log4j.lf5.LogLevel;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.backend.DatabaseUtil;


//@WebFilter(urlPatterns = { "/*" }, dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR })
@WebFilter(urlPatterns = { "/*" })
public class SessionInterceptor implements Filter {
    private Logger LOG = LoggerFactory.getLogger(getClass().getName());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

        final Session session = DatabaseUtil.getSessionFactory().getCurrentSession();

        if (session.getTransaction().isActive())
            session.getTransaction().commit();

        if (session.isOpen())
            session.close();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        final Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        try {
            if (!session.getTransaction().isActive())
                session.beginTransaction();

            chain.doFilter(request, response);

            if (session.getTransaction().isActive())
                session.getTransaction().commit();
        } catch (StaleObjectStateException e) {
            LOG.info(e.getMessage());

            if (session.getTransaction().isActive())
                session.getTransaction().rollback();

            throw e;
        } catch (Throwable e) {
            LOG.error(e.getMessage());

            if (session.getTransaction().isActive())
                session.getTransaction().rollback();

            throw new ServletException(e);
        }
    }
}