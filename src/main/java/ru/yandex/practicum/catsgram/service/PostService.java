package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private static Integer globalId = 0;
    private final UserService userService;
    private final List<Post> posts = new ArrayList<>();

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    private static Integer getNextId() {
        return globalId++;
    }

    public List<Post> findAll(int from, int size, String sort) {

        SortOrder order = SortOrder.from(sort);

        return posts.stream()
                .skip(from)
                .limit(size)
                .sorted(order.equals(SortOrder.ASCENDING) ?
                        Comparator.comparing(Post::getCreationDate) :
                        Comparator.comparing(Post::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    public Post create(Post post) {
        User postAuthor = userService.findUserByEmail(post.getAuthor());
        if (postAuthor == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    post.getAuthor()));
        }

        post.setId(getNextId());
        posts.add(post);
        return post;
    }

    public Post findPostById(Integer postId) {
        return posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(String.format("Пост № %d не найден", postId)));
    }
}

enum SortOrder {
    ASCENDING, DESCENDING;
    public static SortOrder from (String order) {
        return switch (order.toLowerCase()) {
            case "ascending", "asc" -> ASCENDING;
            case "descending", "desc" -> DESCENDING;
            default -> null;
        };
    }
}