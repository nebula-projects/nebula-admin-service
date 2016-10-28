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

import org.nebula.admin.client.request.admin.DeleteNebulaServerRequest;
import org.nebula.admin.client.response.admin.DeleteNebulaServerResponse;
import org.nebula.admin.service.manager.NebulaServerManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteNebulaServerProcessor
    extends AbstractProcessor<DeleteNebulaServerRequest, DeleteNebulaServerResponse> {

  @Autowired
  private Validator<DeleteNebulaServerRequest> deleteNebulaServerRequestValidator;

  @Autowired
  private NebulaServerManager nebulaServerManager;

  protected DeleteNebulaServerResponse processInternal(DeleteNebulaServerRequest request) {

    int rows = nebulaServerManager.delete(request.getHost());

    DeleteNebulaServerResponse response = new DeleteNebulaServerResponse();
    response.setSuccess(rows > 0 ? true : false);

    return response;
  }

  protected Validator<DeleteNebulaServerRequest> getValidator() {
    return deleteNebulaServerRequestValidator;
  }
}