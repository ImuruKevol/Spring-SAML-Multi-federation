package com.season.simpleweb.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import java.util.HashMap;
import java.util.Collection;

public class UserAttribute extends User {
    private HashMap<String, String> attributes = null;

    public UserAttribute() {
        super("test", "unused", true, true, true, true, AuthorityUtils.createAuthorityList(new String[0]));
        attributes = new HashMap<String, String>();
    }

    public UserAttribute(String username, Collection<GrantedAuthority> authorities) {
        super(username, "unused", true, true, true, true, authorities);
        attributes = new HashMap<String, String>();
    }

    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }
}
