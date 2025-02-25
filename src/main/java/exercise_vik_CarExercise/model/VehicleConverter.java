package exercise_vik_CarExercise.model;

import exercise_vik_CarExercise.entity.VehicleEntity;

public class VehicleConverter {
    public static VehicleDTO fromVehicleEntityToVehicleDto(VehicleEntity vehicle) {
        return VehicleDTO.builder()
                .plate(vehicle.getPlate())
                .active(vehicle.getUser().isActive())
                .build();
    }

    public static VehicleEntity fromVehicleDtoToVehicleEntity(VehicleDTO dto) {
        return VehicleEntity.builder()
                .plate(dto.getPlate())
                .isActive(dto.getActive())
                .build();

    }
}