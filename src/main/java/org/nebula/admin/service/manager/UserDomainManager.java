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

package org.nebula.admin.service.manager;

import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.service.dao.entity.UserDomain;
import org.nebula.admin.service.dao.mapper.UserDomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDomainManager {

  @Autowired
  private UserDomainMapper userDomainMapper;

  public int count(String domainName, String username) {
    return userDomainMapper.count(domainName, username);
  }

  public List<UserDomain> getUserDomains(String domainName, String username, RowBounds rowBounds) {
    return userDomainMapper.findUserDomains(domainName, username, rowBounds);
  }

  public void addUserDomain(String username, String domainName) {
    try {
      userDomainMapper.addUserDomain(username, domainName);
    } catch (DuplicateKeyException e) {
      throw new IllegalArgumentException(
          "The domain " + domainName + " already added for user " + username);
    }
  }

  public int removeUserDomain(String username, String domainName) {
    return userDomainMapper.removeUserDomain(username, domainName);
  }

}