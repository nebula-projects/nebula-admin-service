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

import org.nebula.admin.client.request.admin.UserDomainRequest;
import org.nebula.admin.client.response.admin.UserDomainResponse;
import org.nebula.admin.service.manager.DomainManager;
import org.nebula.admin.service.manager.UserDomainManager;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddDomainForUserProcessor
    extends AbstractProcessor<UserDomainRequest, UserDomainResponse> {

  @Autowired
  private Validator<UserDomainRequest> userDomainRequestValidator;

  @Autowired
  private UserManager userManager;

  @Autowired
  private DomainManager domainManager;

  @Autowired
  private UserDomainManager userDomainManager;

  protected UserDomainResponse processInternal(UserDomainRequest request) {

    UserDomainResponse response = new UserDomainResponse();

    if (domainManager.domainExists(request.getDomainName()) && userExists(request.getUsername())) {

      userDomainManager.addUserDomain(request.getUsername(), request.getDomainName());
      response.setSuccess(true);

    } else {
      throw new IllegalArgumentException(
          "The domain " + request.getDomainName() + " doesn't exist.");
    }

    return response;
  }

  private boolean userExists(String user) {
    return userManager.findByName(user) != null;
  }

  protected Validator<UserDomainRequest> getValidator() {
    return userDomainRequestValidator;
  }
}