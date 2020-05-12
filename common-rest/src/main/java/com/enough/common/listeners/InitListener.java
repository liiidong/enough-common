package com.enough.common.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.supermap.gaf.rest.utils.WebTool;

public class InitListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String webPath = sce.getServletContext().getRealPath("/");
        WebTool.setWebConfigPath(webPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //do nothings
    }

}
