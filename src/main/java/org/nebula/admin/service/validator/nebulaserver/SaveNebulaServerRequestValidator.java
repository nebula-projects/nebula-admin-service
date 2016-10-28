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
import org.nebula.admin.client.model.admin.NebulaServerSummary;
import org.nebula.admin.client.request.admin.SaveNebulaServerRequest;
import org.nebula.admin.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class SaveNebulaServerRequestValidator implements Validator<SaveNebulaServerRequest> {

  public void validate(SaveNebulaServerRequest request) {

    Validate.notNull(request, "The SaveNebulaServerRequest should not be null.");

    NebulaServerSummary nebulaServerSummary = request.getNebulaServerSummary();

    Validate.notNull(nebulaServerSummary, "The NebulaServerSummary should not be null.");

    Validate.notEmpty(nebulaServerSummary.getHost(), "The host must exist.");
    Validate.isTrue(nebulaServerSummary.getHost().length() <= 255,
                    "The length of domain name can't exceed 255.");

    if (nebulaServerSummary.getDescription() == null) {
      Validate.isTrue(nebulaServerSummary.getDescription().length() <= 255,
                      "The length of description can't exceed 255.");
    }
  }
}
