package spring.tutorial.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.ArrayList;

import spring.tutorial.models.Post;
import spring.tutorial.repo.PostRepository;

@Controller
public class MainController {

	@Autowired
	private PostRepository postRepository;

	@GetMapping("/home")
	public String home(Model model) {
		return "home";
	}

	@GetMapping("/blog")
	public String blog(Model model) {
		Iterable<Post> posts = postRepository.findAll();
		model.addAttribute("posts", posts);
		return "blog";
	}

	@GetMapping("/blog/add")
	public String blogAdd(Model model) {
		return "blogAdd";
	}

	@PostMapping("/blog/add")
	public String addArticle(
	@RequestParam(name="title", required=false) String title,
	@RequestParam(name="anons", required=false) String anons,
	@RequestParam(name="full_text", required=false) String full_text,
	Model model) {
		Post post = new Post(title, anons, full_text);
		postRepository.save(post);
		return "redirect:/blog";
	}

	@GetMapping("/blog/{id}")
	public String viewPost(
	@PathVariable(value = "id") long id,
	Model model) {
		if(!postRepository.existsById(id)) {
			return "redirect:/blog";
		}

		Post thisPost = postRepository.findById(id).orElseThrow();
		int views = thisPost.getViews();
		thisPost.setViews(++views);
		postRepository.save(thisPost);

		Optional<Post> post = postRepository.findById(id);
		ArrayList<Post> results = new ArrayList<>();
		post.ifPresent(results::add);
		model.addAttribute("post", results);
		return "viewPost";
	}

	@GetMapping("/blog/{id}/edit")
	public String editPost(
	@PathVariable(value = "id") long id,
	Model model) {
		if(!postRepository.existsById(id)) {
			return "redirect:/blog";
		}
		Optional<Post> post = postRepository.findById(id);
		ArrayList<Post> results = new ArrayList<>();
		post.ifPresent(results::add);
		model.addAttribute("post", results);
		return "editPost";
	}

	@PostMapping("/blog/{id}/edit")
	public String updateArticle(
	@PathVariable(value = "id") long id,
	@RequestParam(name="title", required=false) String title,
	@RequestParam(name="anons", required=false) String anons,
	@RequestParam(name="full_text", required=false) String full_text,
	Model model) {
		Post post = postRepository.findById(id).orElseThrow();
		post.setTitle(title);
		post.setAnons(anons);
		post.setFull_text(full_text);
		postRepository.save(post);
		return "redirect:/blog";
	}

	@PostMapping("/blog/{id}/remove")
	public String deleteArticle(
	@PathVariable(value = "id") long id,
	Model model) {
		Post post = postRepository.findById(id).orElseThrow();
		postRepository.delete(post);
		return "redirect:/blog";
	}

}
