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
import org.nebula.admin.client.request.admin.GetSecretKeyRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class GetSecretKeyRequestValidator implements Validator<GetSecretKeyRequest> {

  public void validate(GetSecretKeyRequest request) {

    Validate.notNull(request, "The ChangeSecretKeyRequest should not be null.");

    Validate.notEmpty(request.getUsername(), "The username should not be empty");
    Validate
        .isTrue(request.getUsername().length() <= 10, "The length of the username can't exceed 10.");
  }
}