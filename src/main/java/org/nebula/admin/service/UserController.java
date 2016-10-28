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

package org.nebula.admin.service;

import org.nebula.admin.client.request.admin.ChangePasswordRequest;
import org.nebula.admin.client.request.admin.ChangeSecretKeyRequest;
import org.nebula.admin.client.request.admin.DeleteUserRequest;
import org.nebula.admin.client.request.admin.GetSecretKeyRequest;
import org.nebula.admin.client.request.admin.GetUserDomainsRequest;
import org.nebula.admin.client.request.admin.GetUsersRequest;
import org.nebula.admin.client.request.admin.SaveUserRequest;
import org.nebula.admin.client.request.admin.UserDomainRequest;
import org.nebula.admin.client.request.admin.VerifyUserRequest;
import org.nebula.admin.client.response.admin.ChangePasswordResponse;
import org.nebula.admin.client.response.admin.ChangeSecretKeyResponse;
import org.nebula.admin.client.response.admin.DeleteUserResponse;
import org.nebula.admin.client.response.admin.GetSecretKeyResponse;
import org.nebula.admin.client.response.admin.GetUserDomainsResponse;
import org.nebula.admin.client.response.admin.GetUsersResponse;
import org.nebula.admin.client.response.admin.SaveUserResponse;
import org.nebula.admin.client.response.admin.UserDomainResponse;
import org.nebula.admin.client.response.admin.VerifyUserResponse;
import org.nebula.admin.service.processor.Processor;
import org.nebula.framework.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/nebula/admin")
public class UserController {

  private final static Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private Processor<GetUsersRequest, GetUsersResponse> getUsersProcessor;

  @Autowired
  private Processor<DeleteUserRequest, DeleteUserResponse> deleteUserProcessor;

  @Autowired
  private Processor<SaveUserRequest, SaveUserResponse> saveUserProcessor;

  @Autowired
  private Processor<VerifyUserRequest, VerifyUserResponse> verifyUserProcessor;

  @Autowired
  private Processor<ChangePasswordRequest, ChangePasswordResponse> changePasswordProcessor;

  @Autowired
  private Processor<UserDomainRequest, UserDomainResponse> addDomainForUserProcessor;

  @Autowired
  private Processor<UserDomainRequest, UserDomainResponse> removeDomainFromUserProcessor;

  @Autowired
  private Processor<GetUserDomainsRequest, GetUserDomainsResponse> getUserDomainsProcessor;

  @Autowired
  private Processor<ChangeSecretKeyRequest, ChangeSecretKeyResponse> changeSecretKeyProcessor;

  @Autowired
  private Processor<GetSecretKeyRequest, GetSecretKeyResponse> getSecretKeyProcessor;

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  @ResponseBody
  public GetUsersResponse getUsers(GetUsersRequest request) {

    logger.info("GetUsers Request {}", JsonUtils.toJson(request));

    GetUsersResponse response = getUsersProcessor.process(request);

    logger.info("GetUsers Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/user", method = RequestMethod.DELETE)
  @ResponseBody
  public DeleteUserResponse deleteUser(DeleteUserRequest request) {

    logger.info("DeleteUser Request {}", JsonUtils.toJson(request));

    DeleteUserResponse response = deleteUserProcessor.process(request);

    logger.info("DeleteUser Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseBody
  public SaveUserResponse saveUser(@RequestBody SaveUserRequest request) {

    logger.info("SaveUser Request {}", request);

    SaveUserResponse response = saveUserProcessor.process(request);

    logger.info("SaveUser Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/user/changepwd", method = RequestMethod.POST)
  @ResponseBody
  public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request) {

    logger.info("ChangePassword Request user {}", request.getUsername());

    ChangePasswordResponse response = changePasswordProcessor.process(request);

    logger.info("ChangePassword Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/user/verify", method = RequestMethod.GET)
  @ResponseBody
  public VerifyUserResponse verifyUser(VerifyUserRequest request) {

    logger.info("VerifyUser Request user {}", request.getUsername());

    VerifyUserResponse response = verifyUserProcessor.process(request);

    logger.info("VerifyUser Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/user/secretKey", method = RequestMethod.GET)
  @ResponseBody
  public GetSecretKeyResponse getSecretKey(GetSecretKeyRequest request) {

    logger.info("GetSecretKey Request {}", request);

    GetSecretKeyResponse response = getSecretKeyProcessor.process(request);

    logger.info("GetSecretKey Succeed.");

    return response;
  }

  @RequestMapping(value = "/user/secretKey", method = RequestMethod.POST)
  @ResponseBody
  public ChangeSecretKeyResponse changeSecretKey(@RequestBody ChangeSecretKeyRequest request) {

    logger.info("ChangeSecretKey Request user {}", request.getUsername());

    ChangeSecretKeyResponse response = changeSecretKeyProcessor.process(request);

    logger.info("ChangeSecretKey Response {}", JsonUtils.toJson(response));

    return response;
  }



  @RequestMapping(value = "/userdomain", method = RequestMethod.POST)
  @ResponseBody
  public UserDomainResponse addDomainForUser(@RequestBody UserDomainRequest request) {

    logger.info("AddDomainForUser Request {}", JsonUtils.toJson(request));

    UserDomainResponse response = addDomainForUserProcessor.process(request);

    logger.info("AddDomainForUser Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/userdomain", method = RequestMethod.DELETE)
  @ResponseBody
  public UserDomainResponse removeDomainFromUser(UserDomainRequest request) {

    logger.info("RemoveDomainFromUser Request {}", JsonUtils.toJson(request));

    UserDomainResponse response = removeDomainFromUserProcessor.process(request);

    logger.info("RemoveDomainFromUser Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/userdomain", method = RequestMethod.GET)
  @ResponseBody
  public GetUserDomainsResponse getUserDomains(GetUserDomainsRequest request) {

    logger.info("GetUserDomains Request {}", request.getUsername());

    GetUserDomainsResponse response = getUserDomainsProcessor.process(request);

    logger.info("GetUserDomains Response {}", JsonUtils.toJson(response));

    return response;
  }

}
