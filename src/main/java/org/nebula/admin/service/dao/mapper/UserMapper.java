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
import org.nebula.admin.service.dao.entity.User;

import java.util.List;

public interface UserMapper {

  User findByName(@Param("username") String username);

  Long insert(User user);

  int update(User user);

  int changePassword(@Param("username") String username, @Param("oldPassword") String oldPassword,
                     @Param("newPassword") String newPassword);

  int changeSecretKey(@Param("secretKey") String secretKey, @Param("username") String username, @Param("password") String password);

  List<User> search(RowBounds rowBounds, @Param("username") String username);

  int countByUsername(@Param("username") String username);

  int delete(@Param("username") String username);

  List<User> findByDomain(@Param("domainName") String domainName);

  String findSecretKeyByName(@Param("username") String username);

  User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
