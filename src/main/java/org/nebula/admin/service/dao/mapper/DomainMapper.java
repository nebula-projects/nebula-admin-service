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

package org.nebula.admin.service.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.service.dao.entity.Domain;

import java.util.List;

public interface DomainMapper {

  Long insert(Domain domain);

  int update(Domain domain);

  Domain findById(Long id);

  Domain findByName(String name);

  List<Domain> search(RowBounds rowBounds, @Param("name") String name);

  int countByName(@Param("name") String name);

  int delete(String name);

  List<String> findDomainNamesByUsernameWithFilter(RowBounds rowBounds,
                                                   @Param("username") String username,
                                                   @Param("excludedDomains") List<String> excludedDomains);

  int countDomainNamesByUsernameWithFilter(@Param("username") String username,
                                           @Param("excludedDomains") List<String> excludedDomains);

  List<String> searchWithFilter(RowBounds rowBounds, @Param("name") String domainName,
                                @Param("excludedDomains") List<String> excludedDomains);

  int countWithFilter(@Param("name") String domainName,
                      @Param("excludedDomains") List<String> excludedDomains);

}
