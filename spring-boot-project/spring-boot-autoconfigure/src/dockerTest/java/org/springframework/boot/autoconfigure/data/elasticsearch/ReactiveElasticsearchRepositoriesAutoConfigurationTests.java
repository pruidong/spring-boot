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

package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.TestAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.data.alt.elasticsearch.CityReactiveElasticsearchDbRepository;
import org.springframework.boot.autoconfigure.data.elasticsearch.city.City;
import org.springframework.boot.autoconfigure.data.elasticsearch.city.ReactiveCityRepository;
import org.springframework.boot.autoconfigure.data.empty.EmptyDataPackage;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ReactiveElasticsearchClientAutoConfiguration;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.testsupport.container.TestImage;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReactiveElasticsearchRepositoriesAutoConfiguration}.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Brian Clozel
 * @author Scott Frederick
 */
@Testcontainers(disabledWithoutDocker = true)
class ReactiveElasticsearchRepositoriesAutoConfigurationTests {

	@Container
	static final ElasticsearchContainer elasticsearch = TestImage.container(ElasticsearchContainer.class);

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(ElasticsearchClientAutoConfiguration.class,
				ElasticsearchRestClientAutoConfiguration.class,
				ReactiveElasticsearchRepositoriesAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class,
				ReactiveElasticsearchClientAutoConfiguration.class))
		.withPropertyValues(
				"spring.elasticsearch.uris=" + elasticsearch.getHost() + ":" + elasticsearch.getFirstMappedPort(),
				"spring.elasticsearch.socket-timeout=30s");

	@Test
	void backsOffWithoutReactor() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
			.withClassLoader(new FilteredClassLoader(Mono.class))
			.run((context) -> assertThat(context)
				.doesNotHaveBean(ReactiveElasticsearchRepositoriesAutoConfiguration.class));
	}

	@Test
	void testDefaultRepositoryConfiguration() {
		this.contextRunner.withUserConfiguration(TestConfiguration.class)
			.run((context) -> assertThat(context).hasSingleBean(ReactiveCityRepository.class)
				.hasSingleBean(ReactiveElasticsearchTemplate.class));
	}

	@Test
	void testNoRepositoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
			.run((context) -> assertThat(context).hasSingleBean(ReactiveElasticsearchTemplate.class));
	}

	@Test
	void doesNotTriggerDefaultRepositoryDetectionIfCustomized() {
		this.contextRunner.withUserConfiguration(CustomizedConfiguration.class)
			.run((context) -> assertThat(context).hasSingleBean(CityReactiveElasticsearchDbRepository.class));
	}

	@Test
	void testAuditingConfiguration() {
		this.contextRunner.withUserConfiguration(AuditingConfiguration.class)
			.run((context) -> assertThat(context).hasSingleBean(ReactiveElasticsearchTemplate.class));
	}

	@Configuration(proxyBeanMethods = false)
	@TestAutoConfigurationPackage(City.class)
	static class TestConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@TestAutoConfigurationPackage(EmptyDataPackage.class)
	static class EmptyConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@TestAutoConfigurationPackage(ReactiveElasticsearchRepositoriesAutoConfigurationTests.class)
	@EnableReactiveElasticsearchRepositories(basePackageClasses = CityReactiveElasticsearchDbRepository.class)
	static class CustomizedConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@TestAutoConfigurationPackage(ElasticsearchRepositoriesAutoConfigurationTests.class)
	@EnableReactiveElasticsearchRepositories
	@EnableElasticsearchAuditing
	static class AuditingConfiguration {

	}

}
