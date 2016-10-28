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
import org.nebula.admin.client.request.admin.GetUserDomainsRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class GetUserDomainsRequestValidator implements Validator<GetUserDomainsRequest> {

  public void validate(GetUserDomainsRequest request) {

    Validate.notNull(request, "The GetUserDomainsRequest should not be null.");

    Validate.isTrue(request.getPageNo() >= 0, "The pageNo should be greater than or equal zero.");

    Validate.isTrue(request.getPageSize() <= 200, "The pageSize should be less than or equal 200");

  }
}
