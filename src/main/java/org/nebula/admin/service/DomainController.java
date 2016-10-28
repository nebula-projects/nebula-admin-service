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

import org.nebula.admin.client.request.admin.DeleteDomainRequest;
import org.nebula.admin.client.request.admin.GetDomainNamesRequest;
import org.nebula.admin.client.request.admin.GetDomainsRequest;
import org.nebula.admin.client.request.admin.SaveDomainRequest;
import org.nebula.admin.client.response.admin.DeleteDomainResponse;
import org.nebula.admin.client.response.admin.GetDomainNamesResponse;
import org.nebula.admin.client.response.admin.GetDomainsResponse;
import org.nebula.admin.client.response.admin.SaveDomainResponse;
import org.nebula.admin.service.manager.DomainManager;
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
public class DomainController {

  private final static Logger logger = LoggerFactory.getLogger(DomainController.class);

  @Autowired
  private DomainManager domainManager;

  @Autowired
  private Processor<GetDomainsRequest, GetDomainsResponse> getDomainsProcessor;

  @Autowired
  private Processor<GetDomainNamesRequest, GetDomainNamesResponse> getDomainNamesProcessor;

  @Autowired
  private Processor<DeleteDomainRequest, DeleteDomainResponse> deleteDomainProcessor;

  @Autowired
  private Processor<SaveDomainRequest, SaveDomainResponse> saveDomainProcessor;

  @RequestMapping(value = "/domains", method = RequestMethod.GET)
  @ResponseBody
  public GetDomainsResponse getDomains(GetDomainsRequest request) {

    logger.info("GetDomains Request {}", JsonUtils.toJson(request));

    GetDomainsResponse response = getDomainsProcessor.process(request);

    logger.info("GetDomains Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/domainNames", method = RequestMethod.GET)
  @ResponseBody
  public GetDomainNamesResponse getDomainNames(GetDomainNamesRequest request) {

    logger.info("GetDomainNames Request {}", JsonUtils.toJson(request));

    GetDomainNamesResponse response = getDomainNamesProcessor.process(request);

    logger.info("GetDomainNames Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/domain", method = RequestMethod.DELETE)
  @ResponseBody
  public DeleteDomainResponse deleteDomain(DeleteDomainRequest request) {

    logger.info("DeleteDomain Request {}", JsonUtils.toJson(request));

    DeleteDomainResponse response = deleteDomainProcessor.process(request);

    logger.info("DeleteDomain Response {}", JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/domain", method = RequestMethod.POST)
  @ResponseBody
  public SaveDomainResponse saveDomain(@RequestBody SaveDomainRequest request) {

    logger.info("SaveDomain Request {}", JsonUtils.toJson(request));

    SaveDomainResponse response = saveDomainProcessor.process(request);

    logger.info("SaveDomain Response {}", JsonUtils.toJson(response));

    return response;
  }

}
