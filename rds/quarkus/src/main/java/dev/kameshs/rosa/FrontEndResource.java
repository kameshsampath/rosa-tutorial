package dev.kameshs.rosa;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;

@Path("/")
public class FrontEndResource {

  private static final String FALLBACK_RESOURCE;
  private static final String STATIC_RESOURCE_BASE_DIR;

  static {
    STATIC_RESOURCE_BASE_DIR = "/frontend";
    FALLBACK_RESOURCE = STATIC_RESOURCE_BASE_DIR + "/index.html";
  }

  @GET
  @Path("/")
  public Response getFrontendRoot() throws IOException {
    return getFrontendStaticFile("index.html");
  }

  @GET
  @Path("/{fileName:.+}")
  public Response getFrontendStaticFile(@PathParam("fileName") String fileName) throws IOException {
    final InputStream requestedFileStream = FrontEndResource.class.getResourceAsStream(STATIC_RESOURCE_BASE_DIR+fileName);
    final InputStream inputStream = requestedFileStream != null ?
      requestedFileStream :
      FrontEndResource.class.getResourceAsStream(FALLBACK_RESOURCE);
    final StreamingOutput streamingOutput = outputStream -> IOUtils.copy(inputStream, outputStream);
    return Response
      .ok(streamingOutput)
      .cacheControl(CacheControl.valueOf("max-age=900"))
      .type(URLConnection.guessContentTypeFromStream(inputStream))
      .build();
  }
}
