package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class netsoundApplication extends ResourceConfig {
	public netsoundApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
	}
}
