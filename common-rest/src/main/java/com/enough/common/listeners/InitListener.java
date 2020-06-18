package com.enough.common.listeners;

import com.enough.common.rest.utils.WebTool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {

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
