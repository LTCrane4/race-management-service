package com.teddycrane.racemanagement.utils.mapper;

import com.teddycrane.racemanagement.model.race.Race;
import com.teddycrane.racemanagement.model.race.RaceDto;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class RaceMapper extends Mapper<Race, RaceDto> {

  public RaceDto convertEntityToDTO(@NonNull Race race) {
    return RaceDto.builder()
        .id(race.getId())
        .name(race.getName())
        .createdTimestamp(race.getCreatedTimestamp())
        .updatedTimestamp(race.getUpdatedTimestamp())
        .eventDate(race.getEventDate())
        .startTime(race.getStartTime())
        .finishTime(race.getFinishTime())
        .category(race.getCategory())
        .racers(race.getRacers())
        .build();
  }

  public Race convertDTOToEntity(@NonNull RaceDto dto) {
    return Race.builder()
        .id(dto.getId())
        .name(dto.getName())
        .createdTimestamp(dto.getCreatedTimestamp())
        .updatedTimestamp(dto.getUpdatedTimestamp())
        .eventDate(dto.getEventDate())
        .startTime(dto.getStartTime())
        .finishTime(dto.getFinishTime())
        .category(dto.getCategory())
        .racers(dto.getRacers())
        .build();
  }

  public Collection<Race> convertDTOListToEntityList(@NonNull Collection<RaceDto> dtos) {
    return dtos.stream().map(this::convertDTOToEntity).collect(Collectors.toList());
  }

  public Collection<RaceDto> convertEntityListToDTOList(@NonNull Collection<Race> races) {
    return races.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
  }
}
