package com.season.simpleweb.core;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);

        String userID = credential.getNameID().getValue();
        UserAttribute userinfo = new UserAttribute(userID, authorities);

        LOG.info("-------");
        LOG.info("In SAMLUserDetailServiceImpl");
        List<Attribute> attributes = credential.getAttributes();
        LOG.info("size: " + attributes.size());
        for (Attribute attr : attributes) {
            String n = attr.getName();
            if (n.equals("Role")) continue;
            String value = credential.getAttributeAsStringArray(n)[0];
            userinfo.setAttribute(n, value);
            LOG.info(n + " : " + value);
        }
        LOG.info("size:" + userinfo.getAttributes().size());
        LOG.info(userID + " is logged in");
        LOG.info("-------");

        return userinfo;
    }

}
