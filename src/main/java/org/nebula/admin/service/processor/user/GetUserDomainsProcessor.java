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

package org.nebula.admin.service.processor.user;

import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.client.model.admin.UserDomainSummary;
import org.nebula.admin.client.model.admin.UserSummary;
import org.nebula.admin.client.request.admin.GetUserDomainsRequest;
import org.nebula.admin.client.request.admin.GetUsersRequest;
import org.nebula.admin.client.response.admin.GetUserDomainsResponse;
import org.nebula.admin.client.response.admin.GetUsersResponse;
import org.nebula.admin.service.dao.entity.User;
import org.nebula.admin.service.dao.entity.UserDomain;
import org.nebula.admin.service.manager.UserDomainManager;
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
public class GetUserDomainsProcessor extends AbstractProcessor<GetUserDomainsRequest, GetUserDomainsResponse> {

  @Autowired
  private Validator<GetUserDomainsRequest> getUserDomainsRequestValidator;

  @Autowired
  private UserDomainManager userDomainManager;

  protected GetUserDomainsResponse processInternal(GetUserDomainsRequest request) {

    int total = userDomainManager.count(request.getDomainName(), request.getUsername());

    RowBounds rowBounds = new RowBounds(request.getPageNo(), request.getPageSize());
    List<UserDomain> userDomains = userDomainManager.getUserDomains(request.getDomainName(), request.getUsername(), rowBounds);

    return createGetUserDomainsResponse(request, total, userDomains);
  }

  private GetUserDomainsResponse createGetUserDomainsResponse(GetUserDomainsRequest request, int total,
                                                  List<UserDomain> userDomains) {

    GetUserDomainsResponse response = new GetUserDomainsResponse();
    response.setPageNo(request.getPageNo());
    response.setPageSize(request.getPageSize());
    response.setTotal(total);

    List userDomainSummaries = new ArrayList<UserDomainSummary>();
    for (UserDomain userDomain : userDomains) {
      userDomainSummaries.add(createUserDomainSummary(userDomain));
    }

    response.setUserDomainSummaries(userDomainSummaries);

    return response;
  }

  private UserDomainSummary createUserDomainSummary(UserDomain userDomain) {

    UserDomainSummary userDomainSummary = new UserDomainSummary();

    BeanUtils.copyProperties(userDomain, userDomainSummary);

    return userDomainSummary;
  }

  protected Validator<GetUserDomainsRequest> getValidator() {
    return getUserDomainsRequestValidator;
  }
}

