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

import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.admin.client.request.admin.SaveDomainRequest;
import org.nebula.admin.client.response.admin.SaveDomainResponse;
import org.nebula.admin.service.dao.entity.Domain;
import org.nebula.admin.service.manager.DomainManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveDomainProcessor extends AbstractProcessor<SaveDomainRequest, SaveDomainResponse> {

  @Autowired
  private Validator<SaveDomainRequest> saveDomainRequestValidator;

  @Autowired
  private DomainManager domainManager;

  protected SaveDomainResponse processInternal(SaveDomainRequest request) {

    Domain domain = createDomain(request.getDomainSummary());

    if (domain.getId() == null) {
      domainManager.insert(domain);
    } else {
      domainManager.update(domain);
    }

    SaveDomainResponse response = new SaveDomainResponse();
    response.setDomainId(domain.getId());

    return response;
  }

  private Domain createDomain(DomainSummary domainSummary) {

    Domain domain = new Domain();

    BeanUtils.copyProperties(domainSummary, domain);

    return domain;
  }

  protected Validator<SaveDomainRequest> getValidator() {
    return saveDomainRequestValidator;
  }
}