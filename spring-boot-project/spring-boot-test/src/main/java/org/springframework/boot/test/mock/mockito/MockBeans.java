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

package org.springframework.boot.test.mock.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation that aggregates several {@link MockBean @MockBean} annotations.
 * <p>
 * Can be used natively, declaring several nested {@link MockBean @MockBean} annotations.
 * Can also be used in conjunction with Java 8's support for <em>repeatable
 * annotations</em>, where {@link MockBean @MockBean} can simply be declared several times
 * on the same {@linkplain ElementType#TYPE type}, implicitly generating this container
 * annotation.
 *
 * @author Phillip Webb
 * @since 1.4.0
 * @deprecated since 3.4.0 for removal in 4.0.0 in favor of
 * {@link org.springframework.test.context.bean.override.mockito.MockitoBean}
 */
@SuppressWarnings("removal")
@Deprecated(since = "3.4.0", forRemoval = true)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MockBeans {

	/**
	 * Return the contained {@link MockBean @MockBean} annotations.
	 * @return the mock beans
	 */
	MockBean[] value();

}
