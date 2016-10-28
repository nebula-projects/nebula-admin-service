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

import org.nebula.admin.client.request.admin.ChangeSecretKeyRequest;
import org.nebula.admin.client.response.admin.ChangeSecretKeyResponse;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeSecretKeyProcessor extends
                                      AbstractProcessor<ChangeSecretKeyRequest, ChangeSecretKeyResponse> {

  @Autowired
  private Validator<ChangeSecretKeyRequest> changeSecretKeyRequestValidator;

  @Autowired
  private UserManager userManager;

  protected ChangeSecretKeyResponse processInternal(ChangeSecretKeyRequest request) {

    ChangeSecretKeyResponse response = new ChangeSecretKeyResponse();

    if(!userManager.authenticate(request.getUsername(),
                                request.getPassword())) {
      throw new IllegalArgumentException("The username/password is not correct");

    }
    int rows = userManager.changeSecretKey(request.getSecretKey(), request.getUsername(),
                                           request.getPassword());

    response.setSuccess(rows > 0 ? true : false);
    return response;
  }

  protected Validator<ChangeSecretKeyRequest> getValidator() {
    return changeSecretKeyRequestValidator;
  }
}