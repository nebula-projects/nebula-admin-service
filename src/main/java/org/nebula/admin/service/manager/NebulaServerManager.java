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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.service.dao.entity.Nebula;
import org.nebula.admin.service.dao.mapper.NebulaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NebulaServerManager {

  @Autowired
  private NebulaMapper nebulaMapper;

  public int delete(String host) {

    Nebula nebula = findByHost(host);

    if (nebula != null) {
      if (StringUtils.isNotBlank(nebula.getDomainName())) {
        throw new IllegalArgumentException(
            "The Nebula server " + host + " is in a domain " + nebula.getDomainName()
            + " Move it out of the domain first.");
      }
      return nebulaMapper.delete(host);
    }

    return 0;
  }

  public void insert(Nebula nebula) {
    try {
      nebulaMapper.insert(nebula);
    } catch (DuplicateKeyException e) {
      throw new IllegalArgumentException("The host already exists.");
    }
  }

  public void update(Nebula nebula) {
    try {
      nebulaMapper.update(nebula);
    } catch (DuplicateKeyException e) {
      throw new IllegalArgumentException("The host already exists.");
    }
  }

  public void heartbeat(String host) {

    int result = nebulaMapper.heartbeat(host);
    if (result == 0) {
      Nebula nebula = new Nebula();
      nebula.setHost(host);
      insert(nebula);
    }
  }

  public Nebula findByHost(String host) {
    return nebulaMapper.findByHost(host);
  }

  public List<Nebula> search(RowBounds rowBounds, String host) {
    return nebulaMapper.search(rowBounds, host);
  }

  public int countFuzzyMatchNebulas(String host) {
    return nebulaMapper.countFuzzyMatchNebulas(host);
  }

}
