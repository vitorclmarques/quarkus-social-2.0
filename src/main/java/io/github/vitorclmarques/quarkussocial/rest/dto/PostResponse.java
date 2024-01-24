package io.github.vitorclmarques.quarkussocial.rest.dto;

import java.time.LocalDateTime;

import io.github.vitorclmarques.quarkussocial.domain.model.Post;
import lombok.Data;
import lombok.var;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post){
        var response =  new PostResponse();
        response.setText(post.getText());
        response.setDateTime((post.getDateTime()));

        return response;
    }
}
