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

package org.nebula.admin.service.processor.nebulaserver;

import org.nebula.admin.client.model.admin.NebulaServerSummary;
import org.nebula.admin.client.request.admin.SaveNebulaServerRequest;
import org.nebula.admin.client.response.admin.SaveNebulaServerResponse;
import org.nebula.admin.service.dao.entity.Nebula;
import org.nebula.admin.service.manager.NebulaServerManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveNebulaServerProcessor
    extends AbstractProcessor<SaveNebulaServerRequest, SaveNebulaServerResponse> {

  @Autowired
  private Validator<SaveNebulaServerRequest> saveNebulaServerRequestValidator;

  @Autowired
  private NebulaServerManager nebulaServerManager;

  protected SaveNebulaServerResponse processInternal(SaveNebulaServerRequest request) {

    Nebula nebula = createNebula(request.getNebulaServerSummary());

    if (nebula.getId() == null) {
      nebulaServerManager.insert(nebula);
    } else {
      nebulaServerManager.update(nebula);
    }

    SaveNebulaServerResponse response = new SaveNebulaServerResponse();
    response.setNebulaServerId(nebula.getId());

    return response;
  }

  private Nebula createNebula(NebulaServerSummary nebulaServerSummary) {

    Nebula nebula = new Nebula();

    BeanUtils.copyProperties(nebulaServerSummary, nebula);

    return nebula;
  }

  protected Validator<SaveNebulaServerRequest> getValidator() {
    return saveNebulaServerRequestValidator;
  }
}
