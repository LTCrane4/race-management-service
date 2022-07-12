package com.teddycrane.racemanagement.utils.mapper;

import com.teddycrane.racemanagement.model.racer.Racer;
import com.teddycrane.racemanagement.model.racer.RacerDTO;
import com.teddycrane.racemanagement.model.racer.response.RacerCollectionResponse;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

@UtilityClass
public class RacerMapper {

  public static Collection<RacerDTO> convertEntityListToDTO(@NonNull Collection<Racer> racerList) {
    return racerList.stream().map(RacerMapper::convertEntityToDTO).collect(Collectors.toList());
  }

  public static RacerCollectionResponse createRacerCollection(Collection<Racer> racers) {
    return new RacerCollectionResponse(RacerMapper.convertEntityListToDTO(racers));
  }

  public static RacerDTO convertEntityToDTO(@NonNull Racer racer) {
    return RacerDTO.builder()
        .id(racer.getId())
        .createdTimestamp(racer.getCreatedTimestamp())
        .updatedTimestamp(racer.getUpdatedTimestamp())
        .firstName(racer.getFirstName())
        .middleName(racer.getMiddleName())
        .lastName(racer.getLastName())
        .teamName(racer.getTeamName())
        .email(racer.getEmail())
        .bibNumber(String.format("%s", racer.getBibNumber()))
        .category(racer.getCategory())
        .isDeleted(racer.isDeleted())
        .phoneNumber(racer.getPhoneNumber())
        .build();
  }

  public static Racer convertDTOToEntity(@NonNull RacerDTO dto) {
    return Racer.builder()
        .id(dto.getId())
        .createdTimestamp(dto.getCreatedTimestamp())
        .updatedTimestamp(dto.getUpdatedTimestamp())
        .firstName(dto.getFirstName())
        .middleName(dto.getMiddleName())
        .lastName(dto.getLastName())
        .teamName(dto.getTeamName())
        .email(dto.getEmail())
        .bibNumber(Integer.parseInt(dto.getBibNumber()))
        .category(dto.getCategory())
        .isDeleted(dto.isDeleted())
        .phoneNumber(dto.getPhoneNumber())
        .build();
  }
}
