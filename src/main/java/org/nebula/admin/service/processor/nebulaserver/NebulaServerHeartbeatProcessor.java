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

import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.admin.client.model.admin.NebulaServerHeartbeatSummary;
import org.nebula.admin.client.model.admin.UserSummary;
import org.nebula.admin.client.request.admin.NebulaServerHeartbeatRequest;
import org.nebula.admin.client.response.admin.NebulaServerHeartbeatResponse;
import org.nebula.admin.service.dao.entity.Domain;
import org.nebula.admin.service.dao.entity.Nebula;
import org.nebula.admin.service.dao.entity.User;
import org.nebula.admin.service.manager.DomainManager;
import org.nebula.admin.service.manager.NebulaServerManager;
import org.nebula.admin.service.manager.UserManager;
import org.nebula.admin.service.processor.AbstractProcessor;
import org.nebula.admin.service.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class NebulaServerHeartbeatProcessor
    extends AbstractProcessor<NebulaServerHeartbeatRequest, NebulaServerHeartbeatResponse> {

  @Autowired
  private Validator<NebulaServerHeartbeatRequest> nebulaServerHeartbeatRequestValidator;

  @Autowired
  private NebulaServerManager nebulaServerManager;

  @Autowired
  private DomainManager domainManager;

  @Autowired
  private UserManager userManager;

  protected NebulaServerHeartbeatResponse processInternal(NebulaServerHeartbeatRequest request) {

    nebulaServerManager.heartbeat(request.getHost());

    NebulaServerHeartbeatResponse response = new NebulaServerHeartbeatResponse();
    NebulaServerHeartbeatSummary heartbeat = new NebulaServerHeartbeatSummary();
    response.setNebulaServerHeartbeatSummary(heartbeat);

    Nebula nebula = nebulaServerManager.findByHost(request.getHost());

    Domain domain = domainManager.getDomainByName(nebula.getDomainName());

    int latestDomainHashCode = domainHashCode(domain);

    if (needSyncDomain(latestDomainHashCode, request.getDomainHashCode())) {
      heartbeat.setDomainSummary(createDomainSummary(domain));
      heartbeat.setDomainHashCode(latestDomainHashCode);
    } else {
      heartbeat.setDomainHashCode(request.getDomainHashCode());
    }

    List<User> users = userManager.findByDomain(nebula.getDomainName());

    int latestUsersHashCode = usersHashCode(users);
    if (needSyncUsers(latestUsersHashCode, request.getUserCredentialsHashCode())) {
      heartbeat.setUserSummaries(createUserSummaries(users));
      heartbeat.setUserCredentialsHashCode(latestUsersHashCode);
    } else {
      heartbeat.setUserCredentialsHashCode(request.getUserCredentialsHashCode());
    }

    return response;
  }

  private static boolean needSyncDomain(int latestDomainHashCode, int requestDomainHashCode) {
    return latestDomainHashCode != requestDomainHashCode;
  }


  private static int domainHashCode(Domain domain) {
    return (domain.getName() + domain.getDbUrl() + domain.getRedisHost() + domain.getRedisPort()
            + domain.getStatus()).hashCode();
  }


  private static boolean needSyncUsers(int latestUsersHashCode, int requestUserHashCode) {
    return latestUsersHashCode != requestUserHashCode;
  }


  private static int usersHashCode(List<User> users) {
    Collections.sort(users, new Comparator<User>() {
      @Override
      public int compare(User lhs, User rhs) {
        return lhs.getAccessId().compareToIgnoreCase(rhs.getAccessId());
      }
    });

    StringBuilder sb = new StringBuilder();
    for (User user : users) {
      sb.append(user.getAccessId()).append(user.getSecretKey()).append(user.getStatus()).append("&");
    }

    return sb.toString().hashCode();
  }


  private static DomainSummary createDomainSummary(Domain domain) {

    DomainSummary domainSummary = new DomainSummary();

    BeanUtils.copyProperties(domain, domainSummary);

    return domainSummary;
  }


  private static List<UserSummary> createUserSummaries(List<User> users) {

    List<UserSummary> userSummaries = new ArrayList<UserSummary>();
    for (User user : users) {
      userSummaries.add(createUserSummary(user));
    }
    return userSummaries;
  }


  private static UserSummary createUserSummary(User user) {

    UserSummary userSummary = new UserSummary();

    BeanUtils.copyProperties(user, userSummary);

    return userSummary;
  }




  protected Validator<NebulaServerHeartbeatRequest> getValidator() {
    return nebulaServerHeartbeatRequestValidator;
  }
}
