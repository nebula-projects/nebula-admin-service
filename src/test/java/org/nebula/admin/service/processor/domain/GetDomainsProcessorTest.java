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

package org.nebula.admin.service.processor.domain;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nebula.admin.client.request.admin.GetDomainsRequest;
import org.nebula.admin.client.response.admin.GetDomainsResponse;
import org.nebula.admin.service.dao.entity.Domain;
import org.nebula.admin.service.manager.DomainManager;
import org.nebula.admin.service.validator.Validator;

import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class GetDomainsProcessorTest {

  @Tested
  private GetDomainsProcessor getDomainsProcessor;

  @Injectable
  private DomainManager domainManager;

  @Injectable
  private Validator<GetDomainsRequest> validator;

  @Test
  public void testGetFuzzyDomains(@Mocked final Domain domain1, @Mocked final Domain domain2,
                                  @Mocked final GetDomainsRequest request) throws Exception {

    final List<Domain> domains = new ArrayList<Domain>();
    domains.add(domain1);
    domains.add(domain2);

    new Expectations() {
      {

        request.isExactMatch();
        result = false;
        request.getDomainName();
        result = "domain";

        domainManager.search((RowBounds) any, "domain");
        result = domains;
        domainManager.countFuzzyMatchDomains("domain");
        result = 2;
      }
    };

    final GetDomainsResponse response = getDomainsProcessor.process(request);

    new Verifications() {
      {
        assertEquals(2, response.getDomainSummaries().size());
      }
    };

  }

  @Test
  public void testGetAllDomains(@Mocked final Domain domain1, @Mocked final Domain domain2,
                                @Mocked final GetDomainsRequest request) throws Exception {

    final List<Domain> domains = new ArrayList<Domain>();
    domains.add(domain1);
    domains.add(domain2);

    new Expectations() {
      {

        request.isExactMatch();
        result = false;

        domainManager.search((RowBounds) any, null);
        result = domains;
        domainManager.countFuzzyMatchDomains(null);
        result = 2;
      }
    };

    final GetDomainsResponse response = getDomainsProcessor.process(request);

    new Verifications() {
      {
        assertEquals(2, response.getDomainSummaries().size());
      }
    };
  }


  @Test
  public void testExactDomains(@Mocked final Domain domain1,
                               @Mocked final GetDomainsRequest request) throws Exception {

    new Expectations() {
      {

        request.isExactMatch();
        result = true;
        request.getDomainName();
        result = "domain1";

        domainManager.getDomainByName("domain1");
        result = domain1;
      }
    };

    final GetDomainsResponse response = getDomainsProcessor.process(request);

    new Verifications() {
      {
        assertEquals(1, response.getDomainSummaries().size());
      }
    };
  }

  @Test
  public void testGetExactDomainsWithoutResult(@Mocked final GetDomainsRequest request)
      throws Exception {

    new Expectations() {
      {

        request.isExactMatch();
        result = true;
        request.getDomainName();
        result = "domain1";

        domainManager.getDomainByName("domain1");
        result = null;
      }
    };

    final GetDomainsResponse response = getDomainsProcessor.process(request);

    new Verifications() {
      {
        assertEquals(0, response.getDomainSummaries().size());
      }
    };
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetExactDomainsWithIllegalArgument(@Mocked final GetDomainsRequest request)
      throws Exception {

    new NonStrictExpectations() {
      {
        validator.validate(request);
        result = new IllegalArgumentException();
        request.isExactMatch();
        result = true;
      }
    };

    final GetDomainsResponse response = getDomainsProcessor.process(request);

  }

}
