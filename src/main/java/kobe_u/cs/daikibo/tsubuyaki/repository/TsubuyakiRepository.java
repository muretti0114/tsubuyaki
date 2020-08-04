package kobe_u.cs.daikibo.tsubuyaki.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kobe_u.cs.daikibo.tsubuyaki.entity.Tsubuyaki;
@Repository
public interface TsubuyakiRepository extends CrudRepository<Tsubuyaki, Long>{
    public Iterable<Tsubuyaki> findByCommentLike(String keyword);
}