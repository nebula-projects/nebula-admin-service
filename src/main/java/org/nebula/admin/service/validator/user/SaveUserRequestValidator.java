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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.nebula.admin.client.model.admin.UserSummary;
import org.nebula.admin.client.request.admin.SaveUserRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SaveUserRequestValidator implements Validator<SaveUserRequest> {

  public void validate(SaveUserRequest request) {

    Validate.notNull(request, "The SaveDomainRequest should not be null.");

    UserSummary userSummary = request.getUserSummary();

    Validate.notNull(userSummary, "The UserSummary should not be null.");

    Validate.notEmpty(userSummary.getUsername(), "The user name must exist.");
    Validate.isTrue(userSummary.getUsername().length() <= 10,
                    "The length of user name can't exceed 10.");
    Validate.isTrue(Pattern.matches("^[a-zA-Z0-9]*$", userSummary.getUsername()),
                                    "the user name must be numbers or letters.");

    Validate.notEmpty(userSummary.getNickname(), "The nick name must exist.");
    Validate.isTrue(userSummary.getNickname().length() <= 20,
                    "The length of nick name can't exceed 20.");

    //The password must exist for new user.
    if (userSummary.getId() == null) {
      Validate.notEmpty(userSummary.getPassword(), "The password must exist.");
      Validate.isTrue(userSummary.getPassword().length()>=6 && userSummary.getPassword().length() <= 20,
                      "The length of password must be between 6 and 20.");
    }

    Validate.notEmpty(userSummary.getEmail(), "The email must exist.");
    Validate
        .isTrue(userSummary.getEmail().length() <= 100, "The length of email can't exceed 100.");

    if (StringUtils.isNotBlank(userSummary.getMobile())) {
      Validate
          .isTrue(userSummary.getMobile().length() <= 50, "The length of mobile can't exceed 50.");
    }

    Validate.isTrue(userSummary.getStatus() == UserSummary.ENABLED ||
                    userSummary.getStatus() == UserSummary.DISABLED ||
                    userSummary.getStatus() == UserSummary.LOCKED,
                    "The status of domain must be 1 - enabled,  0 - disabled or -1 - locked.");

  }
}
