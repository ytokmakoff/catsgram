package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.UserRepository;
import ru.yandex.practicum.catsgram.dto.UserDto;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.mapper.UserMapper;
import ru.yandex.practicum.catsgram.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(User request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Емейл должен быть указан");
        }

        Optional<User> alreadyExist = userRepository.findByEmail(request.getEmail());
        if (alreadyExist.isPresent()) {
            throw new DuplicatedDataException("Данный емейл уже используется");
        }

        userRepository.save(request);
        return UserMapper.mapToUserDto(request);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}