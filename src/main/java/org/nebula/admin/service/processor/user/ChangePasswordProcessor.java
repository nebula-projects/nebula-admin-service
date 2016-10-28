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

import org.nebula.admin.client.request.admin.ChangePasswordRequest;
import org.nebula.admin.client.response.admin.ChangePasswordResponse;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordProcessor
    extends AbstractProcessor<ChangePasswordRequest, ChangePasswordResponse> {

  @Autowired
  private Validator<ChangePasswordRequest> changePasswordRequestValidator;

  @Autowired
  private UserManager userManager;

  protected ChangePasswordResponse processInternal(ChangePasswordRequest request) {

    if (userManager.authenticate(request.getUsername(), request.getOldPassword())) {
      throw new IllegalArgumentException(
          "The user " + request.getUsername() + " 's old password is not correct.");
    }

    int
        rows =
        userManager.changePassword(request.getUsername(), request.getOldPassword(),
                                   request.getNewPassword());

    ChangePasswordResponse response = new ChangePasswordResponse();
    response.setSuccess(rows > 0 ? true : false);

    return response;
  }

  protected Validator<ChangePasswordRequest> getValidator() {
    return changePasswordRequestValidator;
  }
}