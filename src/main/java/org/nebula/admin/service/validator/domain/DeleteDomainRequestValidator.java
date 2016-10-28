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

package org.nebula.admin.service.validator.domain;

import org.apache.commons.lang.Validate;
import org.nebula.admin.client.request.admin.DeleteDomainRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class DeleteDomainRequestValidator implements Validator<DeleteDomainRequest> {

  public void validate(DeleteDomainRequest request) {

    Validate.notNull(request, "The GetDomainsRequest should not be null.");

    Validate.notEmpty(request.getDomainName(), "The domainName should not be empty");

  }
}

