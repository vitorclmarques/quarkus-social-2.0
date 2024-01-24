package io.github.vitorclmarques.quarkussocial.rest;





import java.util.stream.Collectors;

import io.github.vitorclmarques.quarkussocial.domain.model.Post;
import io.github.vitorclmarques.quarkussocial.domain.model.User;
import io.github.vitorclmarques.quarkussocial.domain.repository.PostRepository;
import io.github.vitorclmarques.quarkussocial.domain.repository.UserRepository;
import io.github.vitorclmarques.quarkussocial.rest.dto.CreatePostRequest;
import io.github.vitorclmarques.quarkussocial.rest.dto.PostResponse;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.var;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    
    private UserRepository userRepository;
    private PostRepository repository;

    @Inject
    public PostResource(UserRepository userRepository,
     PostRepository repository){
        this.userRepository = userRepository;
        this.repository = repository;
    }



    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request){
        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        Post post =  new Post();
        post.setText(request.getText());
        post.setUser(user);
        
        repository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }
    
    @GET
    public Response listPost(@PathParam("userId") Long userId){
        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var query = repository.find("user",Sort.by("dateTime", Sort.Direction.Descending), user);
        var list = query.list();

        var postResponseList = list.stream()
        // .map(post -> PostResponse.fromEntity(post))
        .map( PostResponse::fromEntity)
        .collect(Collectors.toList());


        return Response.ok(postResponseList).build();
        
        
        
    }
        
}
