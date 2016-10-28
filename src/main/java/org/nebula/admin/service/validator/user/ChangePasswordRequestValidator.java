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

package org.nebula.admin.service.validator.user;

import org.apache.commons.lang.Validate;
import org.nebula.admin.client.request.admin.ChangePasswordRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordRequestValidator implements Validator<ChangePasswordRequest> {

  public void validate(ChangePasswordRequest request) {

    Validate.notNull(request, "The ChangePasswordRequest should not be null.");

    Validate.notEmpty(request.getUsername(), "The username should not be empty");
    Validate
        .isTrue(request.getUsername().length() <= 10, "The length of the username can't exceed 10.");

    Validate.notEmpty(request.getOldPassword(), "The oldPassword should not be empty");
    Validate.isTrue(request.getOldPassword().length() <= 20,
                    "The length of oldPassword can't exceed 20.");

    Validate.notEmpty(request.getNewPassword(), "The newPassword should not be empty");
    Validate.isTrue(request.getNewPassword().length() <= 20,
                    "The length of newPassword can't exceed 20.");

  }
}
