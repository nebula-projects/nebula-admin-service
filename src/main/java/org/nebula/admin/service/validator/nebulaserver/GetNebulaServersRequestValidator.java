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

package org.nebula.admin.service.validator.nebulaserver;

import org.apache.commons.lang.Validate;
import org.nebula.admin.client.request.admin.GetNebulaServersRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class GetNebulaServersRequestValidator implements Validator<GetNebulaServersRequest> {

  public void validate(GetNebulaServersRequest request) {

    Validate.notNull(request, "The GetNebulaServersRequest should not be null.");

    Validate.isTrue(request.getPageNo() >= 0, "The pageNo should be greater than or equal zero.");

    Validate.isTrue(request.getPageSize() <= 200, "The pageSize should be less than or equal 200");

    Validate.isTrue(request.getSearchScope() == GetNebulaServersRequest.SEARCH_SCOPE_ALL ||
                    request.getSearchScope() == GetNebulaServersRequest.SEARCH_SCOPE_UNUSED,
                    "The searchScope should SEARCH_SCOPE_ALL or SEARCH_SCOPE_UNUSED");

  }
}

