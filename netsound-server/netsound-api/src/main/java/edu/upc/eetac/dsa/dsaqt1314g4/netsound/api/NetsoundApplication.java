package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class NetsoundApplication extends ResourceConfig {
	public NetsoundApplication() {
		super();
		register(DeclarativeLinkingFeature.class);   
	}
}
