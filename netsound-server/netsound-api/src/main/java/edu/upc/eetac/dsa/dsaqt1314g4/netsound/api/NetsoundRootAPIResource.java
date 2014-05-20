package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

	import javax.ws.rs.GET;
	import javax.ws.rs.Path;
	import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.NetsoundRootApi;
	 
	@Path("/")
	public class NetsoundRootAPIResource {
		@GET
	public NetsoundRootApi getRootAPI() {
			NetsoundRootApi api = new NetsoundRootApi();
			return api;
		}
	}

