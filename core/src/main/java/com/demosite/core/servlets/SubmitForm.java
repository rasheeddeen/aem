package com.demosite.core.servlets;


import com.demosite.core.FormData;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@Component(service = { Servlet.class })
@SlingServletPaths("/bin/demo/formsubmit")
public class SubmitForm extends SlingAllMethodsServlet {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private SlingSettingsService settings;

    @Reference
    FormData formData;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            boolean publish = settings.getRunModes().contains("publish");
            if(publish) {
                formData.addFormData(request.getParameterMap());
                response.getWriter().write("form data pushed to jcr");
            }else{
                response.getWriter().write("form data not pushed to jcr");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
