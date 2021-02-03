package spring.tutorial.repo;

import spring.tutorial.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
	
}
