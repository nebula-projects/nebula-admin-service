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
import org.nebula.admin.client.request.admin.SaveUserRequest;
import org.nebula.admin.client.response.admin.SaveUserResponse;
import org.nebula.admin.service.dao.entity.User;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveUserProcessor extends AbstractProcessor<SaveUserRequest, SaveUserResponse> {

  @Autowired
  private Validator<SaveUserRequest> saveUserRequestValidator;

  @Autowired
  private UserManager userManager;

  protected SaveUserResponse processInternal(SaveUserRequest request) {

    User user = createUser(request.getUserSummary());

    if (user.getId() == null) {
      userManager.insert(user);
    } else {
      //Use changePasswordProcessor for password change
      //Use changeSecretKeyProcessor for secretKey change
      userManager.update(user);
    }

    SaveUserResponse response = new SaveUserResponse();
    response.setUserId(user.getId());

    return response;
  }

  private User createUser(UserSummary userSummary) {

    User user = new User();

    BeanUtils.copyProperties(userSummary, user);

    return user;
  }

  protected Validator<SaveUserRequest> getValidator() {
    return saveUserRequestValidator;
  }
}