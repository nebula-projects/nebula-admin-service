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

import org.nebula.admin.client.request.admin.DeleteNebulaServerRequest;
import org.nebula.admin.client.request.admin.GetNebulaServersRequest;
import org.nebula.admin.client.request.admin.NebulaServerHeartbeatRequest;
import org.nebula.admin.client.request.admin.SaveNebulaServerRequest;
import org.nebula.admin.client.response.admin.DeleteNebulaServerResponse;
import org.nebula.admin.client.response.admin.GetNebulaServersResponse;
import org.nebula.admin.client.response.admin.NebulaServerHeartbeatResponse;
import org.nebula.admin.client.response.admin.SaveNebulaServerResponse;
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
public class NebulaServerController {

  private final static Logger logger = LoggerFactory.getLogger(NebulaServerController.class);

  @Autowired
  private Processor<GetNebulaServersRequest, GetNebulaServersResponse> getNebulaServersProcessor;

  @Autowired
  private Processor<DeleteNebulaServerRequest, DeleteNebulaServerResponse>
      deleteNebulaServerProcessor;

  @Autowired
  private Processor<SaveNebulaServerRequest, SaveNebulaServerResponse> saveNebulaServerProcessor;

  @Autowired
  private Processor<NebulaServerHeartbeatRequest, NebulaServerHeartbeatResponse>
      nebulaServerHeartbeatProcessor;

  @RequestMapping(value = "/nebulaServers", method = RequestMethod.GET)
  @ResponseBody
  public GetNebulaServersResponse getNebulaServers(GetNebulaServersRequest request) {

    logger.info("GetNebulaServers Request {}", JsonUtils.toJson(request));

    GetNebulaServersResponse response = getNebulaServersProcessor.process(request);

    logger.info("GetNebulaServers Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/nebulaServer", method = RequestMethod.DELETE)
  @ResponseBody
  public DeleteNebulaServerResponse deleteNebulaServer(DeleteNebulaServerRequest request) {

    logger.info("DeleteNebulaServer Request {}", JsonUtils.toJson(request));

    DeleteNebulaServerResponse response = deleteNebulaServerProcessor.process(request);

    logger.info("DeleteNebulaServer Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/nebulaServer", method = RequestMethod.POST)
  @ResponseBody
  public SaveNebulaServerResponse saveNebulaServer(@RequestBody SaveNebulaServerRequest request) {

    logger.info("SaveNebulaServer Request {}", JsonUtils.toJson(request));

    SaveNebulaServerResponse response = saveNebulaServerProcessor.process(request);

    logger.info("SaveNebulaServer Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/nebulaServer/heartbeat", method = RequestMethod.POST)
  @ResponseBody
  public NebulaServerHeartbeatResponse heartbeatNebulaServer(
      @RequestBody NebulaServerHeartbeatRequest request) {

    logger.info("HeartbeatNebulaServer Request {}", JsonUtils.toJson(request));

    NebulaServerHeartbeatResponse response = nebulaServerHeartbeatProcessor.process(request);

    logger.info("HeartbeatNebulaServer Response {}", JsonUtils.toJson(response));

    return response;
  }
}
