/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nebula.admin.service.processor.user;

import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.client.model.admin.UserSummary;
import org.nebula.admin.client.request.admin.GetUsersRequest;
import org.nebula.admin.client.response.admin.GetUsersResponse;
import org.nebula.admin.service.dao.entity.User;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetUsersProcessor extends AbstractProcessor<GetUsersRequest, GetUsersResponse> {

  private final static Logger logger = LoggerFactory.getLogger(GetUsersProcessor.class);

  @Autowired
  private Validator<GetUsersRequest> getUsersRequestValidator;

  @Autowired
  private UserManager userManager;

  protected GetUsersResponse processInternal(GetUsersRequest request) {

    RowBounds rowBounds = new RowBounds(request.getPageNo(), request.getPageSize());

    List<User> users = new ArrayList<User>();
    int total = 0;
    if (request.isExactMatch()) {
      User user = userManager.findByName(request.getName());
      if (user != null) {
        users.add(user);
        total = 1;
      }
    } else {
      users = userManager.search(rowBounds, request.getName());
      total = userManager.countFuzzyMatchUsers(request.getName());
    }

    return createGetUsersResponse(request, total, users);
  }

  private GetUsersResponse createGetUsersResponse(GetUsersRequest request, int total,
                                                  List<User> users) {

    GetUsersResponse response = new GetUsersResponse();
    response.setPageNo(request.getPageNo());
    response.setPageSize(request.getPageSize());
    response.setTotal(total);

    List userSummaries = new ArrayList<UserSummary>();
    for (User user : users) {
      userSummaries.add(createUserSummary(user));
    }

    response.setUserSummaries(userSummaries);

    return response;
  }

  private UserSummary createUserSummary(User user) {

    UserSummary userSummary = new UserSummary();

    BeanUtils.copyProperties(user, userSummary);

    return userSummary;
  }

  protected Validator<GetUsersRequest> getValidator() {
    return getUsersRequestValidator;
  }
}

