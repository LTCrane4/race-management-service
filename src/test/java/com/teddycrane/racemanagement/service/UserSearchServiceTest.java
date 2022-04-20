package com.teddycrane.racemanagement.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.racemanagement.helper.TestResourceGenerator;
import com.teddycrane.racemanagement.model.user.User;
import com.teddycrane.racemanagement.model.user.request.SearchUserRequest;
import com.teddycrane.racemanagement.repositories.UserRepository;
import com.teddycrane.racemanagement.services.UserSearchService;
import com.teddycrane.racemanagement.services.UserSearchServiceImpl;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSearchServiceTest {
  @Mock private UserRepository userRepository;
  private UserSearchService userSearchService;

  private Collection<User> expected = TestResourceGenerator.generateUserList(5);

  @BeforeEach
  void setUp() {
    this.userSearchService = new UserSearchServiceImpl(this.userRepository);
  }

  @Test
  @DisplayName("User Search Service should return data")
  void searchUserShouldReturnData() {
    when(this.userRepository.searchUsers(any(), any(), any(), any(), any())).thenReturn(expected);
    var request = SearchUserRequest.builder().build();
    var result = this.userSearchService.searchUsers(request);

    assertAll(() -> assertNotNull(result, "The result should not be null"));
  }
}
