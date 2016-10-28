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
import org.nebula.admin.service.dao.entity.Domain;
import org.nebula.admin.service.dao.entity.Nebula;
import org.nebula.admin.service.dao.mapper.DomainMapper;
import org.nebula.admin.service.dao.mapper.NebulaMapper;
import org.nebula.framework.utils.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomainManager {

  @Autowired
  PlatformTransactionManager transactionManager;

  @Autowired
  private DomainMapper domainMapper;

  @Autowired
  private NebulaMapper nebulaMapper;

  @Autowired
  private UserDomainManager userDomainManager;

  @Value("${admin.user.name}")
  private String admin;

  public Domain getDomainByName(String domainName) {
    Domain domain = domainMapper.findByName(domainName);

    if(domain==null) {
      domain = new Domain();
    }

    if (domain.exists()) {
      domain.setServers(findNebulaServers(domainName));
    }

    return domain;
  }

  public boolean domainExists(String domainName) {
    return getDomainByName(domainName).exists();
  }

  public List<String> findDomainNamesByUsernameWithFilter(RowBounds rowBounds, String username,
                                                          List<String> excludedDomains) {

    return domainMapper.findDomainNamesByUsernameWithFilter(rowBounds, username, excludedDomains);
  }

  public int countDomainNamesByUsernameWithFilter(String username,
                                                  List<String> excludedDomains) {

    return domainMapper.countDomainNamesByUsernameWithFilter(username, excludedDomains);
  }

  public List<String> searchWithFilter(RowBounds rowBounds, String domainName,
                                       List<String> excludedDomains) {
    return domainMapper.searchWithFilter(rowBounds, domainName, excludedDomains);
  }

  public int countWithFilter(String domainName, List<String> execludedDomains) {
    return domainMapper.countWithFilter(domainName, execludedDomains);
  }

  public List<Domain> search(RowBounds rowBounds, String domainName) {
    List<Domain> domains = domainMapper.search(rowBounds, domainName);

    for (Domain domain : domains) {
      domain.setServers(findNebulaServers(domain.getName()));
    }

    return domains;
  }

  public void insert(final Domain domain) {

    new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
      @Override
      public Object doInTransaction(TransactionStatus status) {

        try {
          domainMapper.insert(domain);
        } catch (DuplicateKeyException e) {
          throw new IllegalArgumentException("The domain name already exists.");
        }

        if (domain.getServers() != null) {
          for (String server : domain.getServers()) {
            int count = nebulaMapper.updateDomainName(server, domain.getName());
            Validate.isTrue(count == 1,
                            "The server " + server + " doesn't exist or has more than one.");
          }
        }

        addAdminToDomain(domain);

        return null;
      }
    });
  }

  private void addAdminToDomain(Domain domain){
    userDomainManager.addUserDomain(admin, domain.getName());
  }

  public void update(final Domain domain) {
    final String[] existNebulaServers = findNebulaServers(domain.getName());

    new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
      @Override
      public Object doInTransaction(TransactionStatus status) {

        try {
          domainMapper.update(domain);
        } catch (DuplicateKeyException e) {
          throw new IllegalArgumentException("The domain name already exists.");
        }

        for (String addedServer : minus(domain.getServers(), existNebulaServers)) {
          int count = nebulaMapper.updateDomainName(addedServer, domain.getName());
          Validate.isTrue(count == 1,
                          "The server " + addedServer + " doesn't exist or has more than one.");
        }

        for (String removedServer : minus(existNebulaServers, domain.getServers())) {
          nebulaMapper.updateDomainName(removedServer, null);
        }

        return null;
      }
    });
  }

  public int delete(final String domainName) {

    if (nebulaMapper.findByDomain(domainName).size() > 0) {
      throw new IllegalArgumentException("There is Nebula server in the domain " + domainName
                                         + ", Move it out of the domain first.");
    }

    if(userDomainManager.count(domainName, null) > 0) {
      throw new IllegalArgumentException("There is Nebula user in the domain " + domainName
                                         + ", Move it out of the domain first.");
    }

    return domainMapper.delete(domainName);
  }

  public int countFuzzyMatchDomains(String name) {
    return domainMapper.countByName(name);
  }

  private String[] minus(String[] firstArray, String[] secondArray) {

    List<String> result = new ArrayList<String>();

    if (firstArray != null) {
      for (String first : firstArray) {

        boolean found = false;

        if (secondArray != null) {
          for (String second : secondArray) {
            if (first.equals(second)) {
              found = true;
              break;
            }
          }
        }

        if (!found) {
          result.add(first);
        }
      }
    }

    return result.toArray(new String[]{});
  }

  private String[] findNebulaServers(String domainName) {
    List<Nebula> nebulas = nebulaMapper.findByDomain(domainName);

    List<String> nebulaServers = new ArrayList<String>();
    for (Nebula nebula : nebulas) {
      nebulaServers.add(nebula.getHost());
    }

    return nebulaServers.toArray(new String[]{});
  }
}
