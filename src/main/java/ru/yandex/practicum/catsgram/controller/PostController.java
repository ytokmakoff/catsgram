package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.service.SortOrder;

import javax.swing.*;
import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> findAll(@RequestParam(defaultValue = "0") int from,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "desc") String sort) {
        SortOrder sortOrder = SortOrder.from(sort);
        if (sortOrder == null)
            throw new ParameterNotValidException("desc", "is not correct value, parameter must equals desc or asc");
        if (size <= 0) {
            throw new ParameterNotValidException("size", "size is must upper than zero");
        }
        if (from <= 0) {
            throw new ParameterNotValidException("from", "from is must upper than zero");
        }
        return postService.findAll(from, size, sort);
    }

    @PostMapping(value = "/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @GetMapping("/post/{postId}")
    public Post findPost(@PathVariable("postId") Integer postId){
        return postService.findPostById(postId);
    }
}