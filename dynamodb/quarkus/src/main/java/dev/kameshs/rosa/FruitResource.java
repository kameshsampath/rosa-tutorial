package dev.kameshs.rosa;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.sts.model.StsException;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FruitResource {

  private static final Logger LOGGER = Logger.getLogger(
    FruitResource.class.getName());

  @Inject
  FruitSyncService service;

  @GET
  @Path("/fruits")
  public Response getAll() {
    try {
      LOGGER.log(Level.INFO, "Getting all fruits");
      return Response.ok(service.findAll())
                     .build();
    } catch (StsException e) {
      return stsErrorResponse(e, "Error Getting Fruits");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Getting Fruits", e);
      return otherErrorResponse(e);
    }
  }

  @POST
  @Path("/fruit/{name}")
  public Response getSingle(@PathParam("name") String name) {
    try {
      LOGGER.log(Level.INFO, "Getting fruit by name {0}", name);
      return Response.ok(service.get(name))
                     .build();
    } catch (StsException e) {
      return stsErrorResponse(e, "Error Getting Fruit");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Getting Fruit", e);
      return otherErrorResponse(e);
    }

  }

  @POST
  @Path("/fruit")
  public Response addFruit(Fruit fruit) {
    try {
      LOGGER.log(Level.INFO, "Saving fruit {0}", fruit);
      service.add(fruit);
      return Response
        .created(URI.create("/api/fruit/" + fruit.name))
        .build();
    } catch (StsException e) {
      return stsErrorResponse(e, "Error Adding Fruit");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Adding Fruit", e);
      return otherErrorResponse(e);
    }
  }

  @DELETE
  @Path("/fruit/{name}")
  public Response delete(@PathParam("name") String fruitName) {
    try {
      LOGGER.log(Level.INFO, "Deleting fruit with name {0}", fruitName);
      service.delete(fruitName);
      return Response.noContent()
                     .build();
    } catch (StsException e) {
      return stsErrorResponse(e, "Error Adding Fruit");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Adding Fruit", e);
      return otherErrorResponse(e);
    }
  }

  private Response otherErrorResponse(Exception e) {
    Error err = Error.internalServerError(e);
    return Response
      .status(err.status, err.message)
      .entity(err)
      .build();
  }

  private Response stsErrorResponse(StsException e, String s) {
    AwsErrorDetails awsErrorDetails = e.awsErrorDetails();
    LOGGER.log(Level.SEVERE, awsErrorDetails.errorMessage(), e);
    Error err = Error.fromAwsErrorDetails(awsErrorDetails);
    return Response
      .status(err.status, err.message)
      .entity(err)
      .build();
  }
}
