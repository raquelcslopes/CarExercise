package exercise_vik_CarExercise.model;

import exercise_vik_CarExercise.entity.UserEntity;

public class UserConverter {

    public static UserDTO fromUserEntityToUserDto(UserEntity user) {
        return UserDTO.builder()
                .active(user.isActive())
                .firstName(user.getFirstName())
                .nif(user.getNif())
                .lastName(user.getLastName())
                .build();
    }

    public static UserEntity fromUserDtoToUserEntity(UserDTO dto) {
        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .nif(dto.getNif())
                .lastName(dto.getLastName())
                .isActive(dto.getActive())
                .build();

    }
}
