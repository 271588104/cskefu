/* 
 * Copyright (C) 2023 Beijing Huaxia Chunsong Technology Co., Ltd. 
 * <https://www.chatopera.com>, Licensed under the Chunsong Public 
 * License, Version 1.0  (the "License"), https://docs.cskefu.com/licenses/v1.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Copyright (C) 2019-Jun. 2023 Chatopera Inc, <https://www.chatopera.com>, 
 * Licensed under the Apache License, Version 2.0, 
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.cskefu.cc.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemEnvHelper {
	private final static Logger logger = LoggerFactory.getLogger(SystemEnvHelper.class);
	private static Properties props;

	/**
	 * 根据类的全名查找是否存在
	 *
	 * @param classFullName
	 * @return
	 */
	public static boolean isClassExistByFullName(final String classFullName) {
		try {
//             Class<?> uriClass =
			Class.forName(classFullName);
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		}
	}

	/**
	 * 获得环境变量的值，如果不存在，返回默认值
	 *
	 * @param variable
	 * @param defaultvalue
	 * @return
	 */
	public static String getenv(final String variable, final String defaultvalue) {
		final String val = System.getenv(variable);

		if (StringUtils.isBlank(val)) {
			return defaultvalue;
		}
		return val;
	}

	/**
	 * 加载配置，先检查环境变量，再从application properties加载
	 *
	 * @param property
	 * @return
	 */
	public static String parseFromApplicationProps(final String property) {
		// 将 property 转化为环境变量
		String P = StringUtils.upperCase(property);

		P = StringUtils.replaceChars(P, "-", "_");
		P = StringUtils.replaceChars(P, ".", "_");
		String val = System.getenv(P);

		if (StringUtils.isBlank(val)) {
			try {
				val = getProps().getProperty(property);
			} catch (java.lang.NullPointerException ex) {
				return "NULL";
			}
		}
		return val;
	}

	/**
	 * Get properties filename
	 * 
	 * @return
	 */
	private static String getPropsFileName() {
		String profile = getenv("SPRING_PROFILES_ACTIVE", "");
		if (StringUtils.isNotBlank(profile)) {
			return "application-" + profile + ".properties";
		}
		return "application.properties";
	}

	/**
	 * 加载 application.properties
	 *
	 * @return
	 */
	private static Properties getProps() {
		if (props == null) {
			try (InputStream input = SystemEnvHelper.class.getClassLoader().getResourceAsStream(getPropsFileName())) {
				// load a properties file
				props = new Properties();
				props.load(input);
			} catch (IOException ex) {
				logger.error("[getProps] error", ex);
			}
		}
		return props;
	}

}
