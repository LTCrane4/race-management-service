package com.teddycrane.racemanagement.utils.mapper;

import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.dto.UserDTO;
import com.teddycrane.racemanagement.model.user.response.UserCollectionResponse;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

@UtilityClass
public class UserMapper {
  public static UserDTO convertEntityToDTO(@NonNull User user) {
    return UserDTO.builder()
        .id(user.getId())
        .createdTimestamp(user.getCreatedTimestamp())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .username(user.getUsername())
        .userType(user.getUserType())
        .userStatus(user.getStatus())
        .updatedTimestamp(user.getUpdatedTimestamp())
        .build();
  }

  public static UserCollectionResponse convertEntityListToDTO(@NonNull Collection<User> userList) {
    return new UserCollectionResponse(
        userList.stream().map(UserMapper::convertEntityToDTO).collect(Collectors.toList()));
  }
}
