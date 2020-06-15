package com.demosite.core.impl;

import com.demosite.core.FormData;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component(service = FormData.class,immediate = true)
public class FormDataImpl implements FormData {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ResourceResolverFactory resolverFactory;

    private Session session;

    @Override
    public void addFormData(Map requestparams) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "formdatapersist");
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);
            Node root = session.getRootNode();
            Node content = root.getNode("content/formdata");
            java.util.Random r = new java.util.Random();
            int low = 10;
            int high = 1000;
            int result = r.nextInt(high - low) + low;
            String numberValue = "form" + result;
            Node employeeRoot = content.addNode(numberValue, "nt:unstructured");
            Set s = requestparams.entrySet();
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
                String key = entry.getKey();
                String[] value = entry.getValue();
                employeeRoot.setProperty(key, value[0]);
            }
            session.save();
            session.logout();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }

        }
    }
}
