package com.abdul;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

    @GET
    @Path("/{id}")
    public User get(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null)
            throw new WebApplicationException("User does not exist!", 404);
        return user;
    }

    @GET
    @Path("/all")
    public List<User> getAll() {
        return userRepository.listAll();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public User update(@PathParam("id") Long id, User userUpdated) {

        User user = userRepository.findById(id);
        if (user == null)
            throw new WebApplicationException("User does not exist!", 404);

        user.setUsername(userUpdated.getUsername());
        user.setPassword(userUpdated.getPassword());
        user.setEmail(userUpdated.getEmail());
        user.setAge(userUpdated.getAge());

        return user;
    }

    @Transactional
    @POST
    public Response create(User user) {
        if (user.getId() != null)
            throw new WebApplicationException("ID was invalidly set!", 422);
        userRepository.persist(user);

        return Response.ok(user).status(201).build();
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null)
            throw new WebApplicationException("User does not exist!", 404);
        userRepository.delete(user);
        return Response.status(204).build();
    }
    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}

