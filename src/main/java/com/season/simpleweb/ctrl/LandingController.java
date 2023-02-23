package com.season.simpleweb.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import com.season.simpleweb.core.UserAttribute;

@Controller
public class LandingController {
	
	// Logger
	private static final Logger LOG = LoggerFactory
			.getLogger(LandingController.class);

	@RequestMapping("/landing")
	public String landing(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			LOG.debug("Current authentication instance from security context is null");
		else {
			LOG.debug("Current authentication instance from security context: " + this.getClass().getSimpleName());
			LOG.info(auth.getDetails().toString());
		}
		
		LOG.info("-------");
		UserAttribute info = (UserAttribute) auth.getDetails();
		HashMap<String, String> attributes = info.getAttributes();
		for (String key: attributes.keySet()) {
			LOG.info(key + " : " + attributes.get(key));
			model.addAttribute(key, attributes.get(key));
		}
		LOG.info("-------");
		return "pages/landing";
	}

}
