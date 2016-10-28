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
import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.admin.client.request.admin.GetDomainsRequest;
import org.nebula.admin.client.response.admin.GetDomainsResponse;
import org.nebula.admin.service.dao.entity.Domain;
import org.nebula.admin.service.manager.DomainManager;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDomainsProcessor extends AbstractProcessor<GetDomainsRequest, GetDomainsResponse> {

  private final static Logger logger = LoggerFactory.getLogger(GetDomainsProcessor.class);

  @Autowired
  private Validator<GetDomainsRequest> getDomainsRequestValidator;

  @Autowired
  private DomainManager domainManager;

  @Autowired
  private UserManager userManager;

  protected GetDomainsResponse processInternal(GetDomainsRequest request) {

    RowBounds rowBounds = new RowBounds(request.getPageNo(), request.getPageSize());

    List<Domain> domains = new ArrayList<Domain>();
    int total = 0;

    if (request.isExactMatch()) {
      Domain domain = domainManager.getDomainByName(request.getDomainName());
      if (domain.exists()) {
        domains.add(domain);
        total = 1;
      }
    } else {
      domains = domainManager.search(rowBounds, request.getDomainName());
      total = domainManager.countFuzzyMatchDomains(request.getDomainName());
    }

    return createGetDomainsResponse(request, total, domains);
  }


  private GetDomainsResponse createGetDomainsResponse(GetDomainsRequest request, int total,
                                                      List<Domain> domains) {

    GetDomainsResponse response = new GetDomainsResponse();
    response.setPageNo(request.getPageNo());
    response.setPageSize(request.getPageSize());
    response.setTotal(total);

    List domainSummaries = new ArrayList<DomainSummary>();
    for (Domain domain : domains) {
      domainSummaries.add(createDomainSummary(domain));
    }

    response.setDomainSummaries(domainSummaries);

    return response;
  }

  private DomainSummary createDomainSummary(Domain domain) {

    DomainSummary domainSummary = new DomainSummary();

    BeanUtils.copyProperties(domain, domainSummary);

    return domainSummary;
  }

  protected Validator<GetDomainsRequest> getValidator() {
    return getDomainsRequestValidator;
  }
}
