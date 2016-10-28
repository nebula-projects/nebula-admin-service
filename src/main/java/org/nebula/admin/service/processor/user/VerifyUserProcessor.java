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

import org.nebula.admin.client.model.admin.UserSummary;
import org.nebula.admin.client.request.admin.VerifyUserRequest;
import org.nebula.admin.client.response.admin.VerifyUserResponse;
import org.nebula.admin.service.dao.entity.User;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyUserProcessor extends AbstractProcessor<VerifyUserRequest, VerifyUserResponse> {

  @Autowired
  private Validator<VerifyUserRequest> verifyUserRequestValidator;

  @Autowired
  private UserManager userManager;

  protected VerifyUserResponse processInternal(VerifyUserRequest request) {

    VerifyUserResponse response = new VerifyUserResponse();

    User user = userManager.findByName(request.getUsername());
    if (user == null) {
      response.setResult(VerifyUserResponse.USER_NOT_EXIST);
      response.setDescription("User " + request.getUsername() + " doesn't exist.");
    } else if (user.getStatus() == UserSummary.LOCKED || user.getStatus() == UserSummary.DISABLED) {
      response.setResult(VerifyUserResponse.USER_LOCKED_DISABLED);
      response.setDescription("User " + request.getUsername() + " is locked or disabled.");
    } else if (userManager.authenticate(request.getUsername(), request.getPassword())) {
      response.setResult(VerifyUserResponse.SUCCESS);
    } else {
      response.setResult(VerifyUserResponse.USER_INCORRECT_PWD);
      response.setDescription("The user name or password is incorrect.");
    }

    return response;
  }


  protected Validator<VerifyUserRequest> getValidator() {
    return verifyUserRequestValidator;
  }
}
