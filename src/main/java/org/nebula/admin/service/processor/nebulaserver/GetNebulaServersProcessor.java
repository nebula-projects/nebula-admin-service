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

import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.client.model.admin.NebulaServerSummary;
import org.nebula.admin.client.request.admin.GetNebulaServersRequest;
import org.nebula.admin.client.response.admin.GetNebulaServersResponse;
import org.nebula.admin.service.dao.entity.Nebula;
import org.nebula.admin.service.manager.NebulaServerManager;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetNebulaServersProcessor
    extends AbstractProcessor<GetNebulaServersRequest, GetNebulaServersResponse> {

  private final static Logger logger = LoggerFactory.getLogger(GetNebulaServersProcessor.class);

  @Autowired
  private Validator<GetNebulaServersRequest> getNebulaServersRequestValidator;

  @Autowired
  private NebulaServerManager nebulaServerManager;

  @Autowired
  private UserManager userManager;

  protected GetNebulaServersResponse processInternal(GetNebulaServersRequest request) {

    RowBounds rowBounds = new RowBounds(request.getPageNo(), request.getPageSize());

    List<Nebula> nebulas = new ArrayList<Nebula>();
    int total = 0;

    if (request.isExactMatch()) {
      Nebula nebula = nebulaServerManager.findByHost(request.getHost());
      if (nebula != null) {
        nebulas.add(nebula);
        total = 1;
      }
    } else {
      nebulas = nebulaServerManager.search(rowBounds, request.getHost());
      total = nebulaServerManager.countFuzzyMatchNebulas(request.getHost());
    }

    return createResponse(request, total, nebulas);
  }


  private GetNebulaServersResponse createResponse(GetNebulaServersRequest request, int total,
                                                  List<Nebula> nebulas) {

    GetNebulaServersResponse response = new GetNebulaServersResponse();
    response.setPageNo(request.getPageNo());
    response.setPageSize(request.getPageSize());
    response.setTotal(total);

    List nebulaServerSummaries = new ArrayList<NebulaServerSummary>();
    for (Nebula nebula : nebulas) {
      nebulaServerSummaries.add(createNebulaServerSummary(nebula));
    }

    response.setNebulaServerSummaries(nebulaServerSummaries);

    return response;
  }

  private NebulaServerSummary createNebulaServerSummary(Nebula nebula) {

    NebulaServerSummary nebulaServerSummary = new NebulaServerSummary();

    BeanUtils.copyProperties(nebula, nebulaServerSummary);

    return nebulaServerSummary;
  }

  protected Validator<GetNebulaServersRequest> getValidator() {
    return getNebulaServersRequestValidator;
  }
}
