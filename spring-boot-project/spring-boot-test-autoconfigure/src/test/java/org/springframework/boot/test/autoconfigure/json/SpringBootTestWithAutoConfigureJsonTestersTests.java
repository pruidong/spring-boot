/*
 * Copyright 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.test.autoconfigure.json;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.app.ExampleBasicObject;
import org.springframework.boot.test.autoconfigure.json.app.ExampleJsonApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link SpringBootTest @SpringBootTest} with
 * {@link AutoConfigureJsonTesters @AutoConfigureJsonTesters}.
 *
 * @author Andy Wilkinson
 */
@SpringBootTest
@AutoConfigureJsonTesters
@ContextConfiguration(classes = ExampleJsonApplication.class)
class SpringBootTestWithAutoConfigureJsonTestersTests {

	@Autowired
	private BasicJsonTester basicJson;

	@Autowired
	private JacksonTester<ExampleBasicObject> jacksonTester;

	@Autowired
	private GsonTester<ExampleBasicObject> gsonTester;

	@Autowired
	private JsonbTester<ExampleBasicObject> jsonbTester;

	@Test
	void contextLoads() {
		assertThat(this.basicJson).isNotNull();
		assertThat(this.jacksonTester).isNotNull();
		assertThat(this.jsonbTester).isNotNull();
		assertThat(this.gsonTester).isNotNull();
	}

}
