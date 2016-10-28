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

package org.nebula.admin.service.processor.domain;

import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.client.request.admin.GetDomainNamesRequest;
import org.nebula.admin.client.response.admin.GetDomainNamesResponse;
import org.nebula.admin.service.manager.DomainManager;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDomainNamesProcessor
    extends AbstractProcessor<GetDomainNamesRequest, GetDomainNamesResponse> {

  private final static Logger logger = LoggerFactory.getLogger(GetDomainNamesProcessor.class);

  @Autowired
  private Validator<GetDomainNamesRequest> getDomainNamesRequestValidator;

  @Autowired
  private DomainManager domainManager;

  @Autowired
  private UserManager userManager;

  protected GetDomainNamesResponse processInternal(GetDomainNamesRequest request) {

    RowBounds rowBounds = new RowBounds(request.getPageNo(), request.getPageSize());

    List<String> domainNames;
    int total = 0;

    if (request.getUsername() != null) {
      domainNames =
          domainManager.findDomainNamesByUsernameWithFilter(rowBounds, request.getUsername(),
                                                            request.getExcludedDomains());
      total =
          domainManager.countDomainNamesByUsernameWithFilter(request.getUsername(),
                                                             request.getExcludedDomains());
    } else {
      domainNames =
          domainManager
              .searchWithFilter(rowBounds, request.getDomainName(), request.getExcludedDomains());
      total = domainManager.countWithFilter(request.getDomainName(), request.getExcludedDomains());
    }

    return createGetDomainNamesResponse(request, total, domainNames);
  }


  private GetDomainNamesResponse createGetDomainNamesResponse(GetDomainNamesRequest request,
                                                              int total, List<String> domainNames) {

    GetDomainNamesResponse response = new GetDomainNamesResponse();
    response.setPageNo(request.getPageNo());
    response.setPageSize(request.getPageSize());
    response.setTotal(total);
    response.setNames(domainNames);

    return response;
  }

  protected Validator<GetDomainNamesRequest> getValidator() {
    return getDomainNamesRequestValidator;
  }
}

