package io.github.vitorclmarques.quarkussocial.rest;


import java.util.Set;





// import org.hibernate.Incubating;
// import org.jboss.resteasy.reactive.common.jaxrs.ResponseImpl;

import io.github.vitorclmarques.quarkussocial.domain.model.User;
import io.github.vitorclmarques.quarkussocial.domain.repository.UserRepository;
import io.github.vitorclmarques.quarkussocial.rest.dto.CreateUserRequest;
import io.github.vitorclmarques.quarkussocial.rest.dto.ResponseError;
// import io.quarkus.hibernate.orm.panache.Panache;
// import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    
    
    private UserRepository repository;

    private Validator validator;
    @Inject
    public UserResource(UserRepository repository, Validator  validator){
        this.repository = repository;
        this.validator = validator;

    }


    @POST
    @Transactional
    public Response createUser(CreateUserRequest userResquest){

         Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userResquest);
         if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
         }   
    

        User user = new User();
        user.setAge(userResquest.getAge()); 
        user.setName(userResquest.getName());

        repository.persist(user);
        

        return Response.status(Response.Status.CREATED.getStatusCode())
        .entity(user)
        .build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }


    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id")Long id){
        User user = repository.findById(id);
        
        if (user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }    
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Put atualizar entidade 
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id")Long id, CreateUserRequest userData){
        User user = repository.findById(id);
        if(user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());

            return Response.noContent().build();
            }

            return Response.status(Response.Status.NOT_FOUND).build();

    }
}
