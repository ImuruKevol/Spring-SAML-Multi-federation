package com.season.simpleweb.ctrl;

import java.util.Set;
import java.util.Timer;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.opensaml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.parse.ParserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/saml")
public class SSOController {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(SSOController.class);

    @Autowired
    private MetadataManager metadata;

    @Autowired
    ParserPool parserPool;

    private Set<String> parseProvider(MetadataProvider provider) throws MetadataProviderException {
        Set<String> result = new HashSet<String>();

        XMLObject object = provider.getMetadata();
        if (object instanceof EntityDescriptor) {
            addDescriptor(result, (EntityDescriptor) object);
        } else if (object instanceof EntitiesDescriptor) {
            addDescriptors(result, (EntitiesDescriptor) object);
        }
        return result;
    }

    private void addDescriptors(Set<String> result, EntitiesDescriptor descriptors) throws MetadataProviderException {
        if (descriptors.getEntitiesDescriptors() != null) {
            for (EntitiesDescriptor descriptor : descriptors.getEntitiesDescriptors()) {
                addDescriptors(result, descriptor);
            }
        }

        if (descriptors.getEntityDescriptors() != null) {
            for (EntityDescriptor descriptor : descriptors.getEntityDescriptors()) {
                addDescriptor(result, descriptor);
            }
        }
    }

    private void addDescriptor(Set<String> result, EntityDescriptor descriptor) throws MetadataProviderException {
        String entityID = descriptor.getEntityID();
        result.add(entityID);
    }

    @RequestMapping(value = "/discovery", method = RequestMethod.GET)
    public String idpSelection(HttpServletRequest request, Model model) throws MetadataProviderException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Timer timer = new Timer();
        MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
        HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager);
        String url = "<METADATA-URL>";

        HTTPMetadataProvider provider = new HTTPMetadataProvider(timer, httpClient, url);
        provider.setParserPool(parserPool);
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setIdpDiscoveryEnabled(true);
        extendedMetadata.setSigningAlgorithm("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
        extendedMetadata.setSignMetadata(true);
        extendedMetadata.setEcpEnabled(true);
        ExtendedMetadataDelegate metadataDelegate = new ExtendedMetadataDelegate(provider, extendedMetadata);
        metadataDelegate.initialize();
        Set<String> newEntityIds = parseProvider(metadataDelegate);
        Set<String> existingEntityIds = metadata.getIDPEntityNames();
        for (String id : existingEntityIds) {
            LOG.warn(id);
        }
        for (String id : newEntityIds) {
            LOG.warn(id);
        }
        if (!existingEntityIds.containsAll(newEntityIds)) {
            try {
                metadata.addMetadataProvider(metadataDelegate);
                metadata.refreshMetadata();
                metadata.addMetadataProvider(provider);
            }
            catch(Exception e) {}
        }
        timer.purge();
        multiThreadedHttpConnectionManager.shutdown();
        
        if (auth == null)
            LOG.debug("Current authentication instance from security context is null");
        else
            LOG.debug("Current authentication instance from security context: "
                    + this.getClass().getSimpleName());
        if (auth == null || (auth instanceof AnonymousAuthenticationToken)) {
            Set<String> idps = metadata.getIDPEntityNames();
            for (String idp : idps) {
                System.out.println("idp: " + idp);
                LOG.warn("Configured Identity Provider for SSO: " + idp);
            }
            model.addAttribute("idps", idps);
            return "pages/discovery";
        } else {
            LOG.warn("The current user is already logged.");
            return "redirect:/landing";
        }
    }

}
