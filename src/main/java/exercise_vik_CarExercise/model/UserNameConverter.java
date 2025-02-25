package exercise_vik_CarExercise.model;

import exercise_vik_CarExercise.entity.UserEntity;

public class UserNameConverter {
    public static UserNameDTO fromUserEntityToUserNameDto(UserEntity user) {
        return UserNameDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public static UserEntity fromUserNameDtoToUserEntity(UserNameDTO dto) {
        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();

    }
}
