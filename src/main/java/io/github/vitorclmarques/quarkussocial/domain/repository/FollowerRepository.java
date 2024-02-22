package io.github.vitorclmarques.quarkussocial.domain.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import io.github.vitorclmarques.quarkussocial.domain.model.Follower;
import io.github.vitorclmarques.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user) {

        // ** Utilizando metodo um sem o metodo do panache**
        // Map<String, Object> params = new HashMap<>();
        // params.put("follower", params);
        // params.put("user", params);

        var params = Parameters.with("follower", follower).and("user", user).map();

        PanacheQuery<Follower> query = find("follower =:follower and user =:user", params);
        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();
    }

    public List<Follower> findByUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();

    }

    public void deleByFollowerAndUser(Long followerId, Long userId) {
        var params = Parameters
                .with("userId", userId)
                .and("followerId", followerId)
                .map();
        delete("follower.id =:followerId and user.id =:userId", params);
    }
}
