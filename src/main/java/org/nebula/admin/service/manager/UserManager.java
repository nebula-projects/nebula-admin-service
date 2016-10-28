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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.service.dao.entity.User;
import org.nebula.admin.service.dao.mapper.UserMapper;
import org.nebula.admin.service.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManager {

  @Autowired
  private UserMapper userMapper;

  @Value("${password.salt}")
  private String passwordSalt;

  public User findByName(String username) {
    return userMapper.findByName(username);
  }

  public List<User> search(RowBounds rowBounds, String username) {
    return userMapper.search(rowBounds, username);
  }

  public Long insert(User user) {
    try {

      user.setAccessId(user.getUsername());
      user.setPassword(DigestUtils.md5Hex(user.getPassword() + passwordSalt));
      user.setSecretKey(UuidUtil.getUuid());

      return userMapper.insert(user);
    } catch (DuplicateKeyException e) {
      throw new IllegalArgumentException("The user already exists.");
    }
  }

  public int changePassword(String username, String oldPlainPassword, String newPlainPassword) {
    return userMapper.changePassword(username, DigestUtils.md5Hex(oldPlainPassword + passwordSalt), DigestUtils.md5Hex(newPlainPassword + passwordSalt));
  }

  public int changeSecretKey(String secretKey, String username, String plainPassword) {
    return userMapper.changeSecretKey(secretKey, username,
                                      DigestUtils.md5Hex(plainPassword + passwordSalt));
  }

  public void update(User user) {
    userMapper.update(user);
  }

  public int countFuzzyMatchUsers(String name) {
    return userMapper.countByUsername(name);
  }

  public int delete(String name) {
    return userMapper.delete(name);
  }

  public List<User> findByDomain(String domainName) {
    return userMapper.findByDomain(domainName);
  }

  public boolean authenticate(String username, String plainPassword) {
    User user = userMapper.findByUsernameAndPassword(username, DigestUtils
        .md5Hex(plainPassword + passwordSalt));
    return user!=null;
  }

  public String getSecretKeyByUsername(String username) {
    return userMapper.findSecretKeyByName(username);
  }
}
