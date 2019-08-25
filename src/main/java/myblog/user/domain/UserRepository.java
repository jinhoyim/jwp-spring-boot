package myblog.user.domain;

import myblog.user.dto.SessionedUser;
import myblog.user.dto.UserCreatedDto;
import myblog.user.dto.UserUpdatedDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static final List<User> users = new ArrayList<>();

    public static User create(final UserCreatedDto createdDto) {
        final Optional<User> maybeUser = findByUserId(createdDto.getUserId());
        if (maybeUser.isPresent()) {
            return maybeUser.get();
        }

        final User user = new User(users.size() + 1,
                createdDto.getUserId(),
                createdDto.getEmail(),
                createdDto.getPassword());
        users.add(user);
        return user;
    }

    public static User findById(final int id) {
        return users.get(id - 1);
    }

    public static Optional<User> findByUserId(final String userId) {
        return users.stream()
                .filter(u -> u.matchUserId(userId))
                .findFirst();
    }

    public static User update(final SessionedUser loginUser, final int id, final UserUpdatedDto updatedDto) {
        final User user = findById(id);
        user.update(loginUser, updatedDto);
        return user;
    }
}