package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.NetsoundError;

public class WebApplicationExceptionMapper implements
		ExceptionMapper<WebApplicationException> {
	@Override
	public Response toResponse(WebApplicationException exception) {
		NetsoundError error = new NetsoundError(
				exception.getResponse().getStatus(), exception.getMessage());
		return Response.status(error.getStatus()).entity(error)
				.type(MediaType.NETSOUND_API_ERROR).build();
	}

}
