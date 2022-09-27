/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.assertions;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.oro.text.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.jayway.jsonpath.JsonPath;

/**
 * This is main class for JSONPath Assertion which verifies assertion on
 * previous sample result using JSON path expression
 *
 * @since 4.0
 */
public class JSONPathAssertion extends AbstractTestElement implements Serializable, Assertion, ThreadListener {
    private static final Logger log = LoggerFactory.getLogger(JSONPathAssertion.class);
    private static final long serialVersionUID = 2L;
    public static final String JSONPATH = "JSON_PATH";
    public static final String EXPECTEDVALUE = "EXPECTED_VALUE";
    public static final String JSONVALIDATION = "JSONVALIDATION";
    public static final String EXPECT_NULL = "EXPECT_NULL";
    public static final String INVERT = "INVERT";
    public static final String ISREGEX = "ISREGEX";
    public static final String GREATERTHAN = "GREATERTHAN";
    public static final String GREATERTHANOREQUAL = "GREATERTHANOREQUAL";
    public static final String LESSTHAN = "LESSTHAN";
    public static final String LESSTHANOREQUAL = "LESSTHANOREQUAL";
    public static final String CONTAINS = "CONTAINS";

    private static final boolean USE_JAVA_REGEX = !JMeterUtils.getPropDefault("jmeter.regex.engine", "oro").equalsIgnoreCase("oro");

    private static ThreadLocal<DecimalFormat> decimalFormatter = ThreadLocal.withInitial(JSONPathAssertion::createDecimalFormat);

    private static DecimalFormat createDecimalFormat() {
        DecimalFormat decimalFormatter = new DecimalFormat("#.#");
        decimalFormatter.setMaximumFractionDigits(340); // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }

    public String getJsonPath() {
        return getPropertyAsString(JSONPATH);
    }

    public void setJsonPath(String jsonPath) {
        setProperty(JSONPATH, jsonPath);
    }

    public String getExpectedValue() {
        return getPropertyAsString(EXPECTEDVALUE);
    }

    public void setExpectedValue(String expectedValue) {
        setProperty(EXPECTEDVALUE, expectedValue);
    }

    public void setJsonValidationBool(boolean jsonValidation) {
        setProperty(JSONVALIDATION, jsonValidation);
    }

    public void setExpectNull(boolean val) {
        setProperty(EXPECT_NULL, val);
    }

    public boolean isExpectNull() {
        return getPropertyAsBoolean(EXPECT_NULL);
    }

    public boolean isJsonValidationBool() {
        return getPropertyAsBoolean(JSONVALIDATION);
    }

    public void setInvert(boolean invert) {
        setProperty(INVERT, invert);
    }

    public boolean isInvert() {
        return getPropertyAsBoolean(INVERT);
    }

    public void setGreaterThan(boolean greaterThan) {
        setProperty(GREATERTHAN, greaterThan);
    }

    public boolean isGreaterThan() {
        return getPropertyAsBoolean(GREATERTHAN);
    }

    public void setGreaterThanOrEqual(boolean greaterThanOrEqual) {
        setProperty(GREATERTHANOREQUAL, greaterThanOrEqual);
    }

    public boolean isGreaterThanOrEqual() {
        return getPropertyAsBoolean(GREATERTHANOREQUAL);
    }

    public void setLessThan(boolean lessThan) {
        setProperty(LESSTHAN, lessThan);
    }

    public boolean isLessThan() {
        return getPropertyAsBoolean(LESSTHAN);
    }

    public void setLessThanOrEqual(boolean lessThanOrEqual) {
        setProperty(LESSTHANOREQUAL, lessThanOrEqual);
    }

    public boolean isLessThanOrEqual() {
        return getPropertyAsBoolean(LESSTHANOREQUAL);
    }

    public void setContains(boolean contains) {
        setProperty(CONTAINS, contains);
    }

    public boolean isContains() {
        return getPropertyAsBoolean(CONTAINS);
    }

    public void setIsRegex(boolean flag) {
        setProperty(ISREGEX, flag);
    }

    public boolean isUseRegex() {
        return getPropertyAsBoolean(ISREGEX, true);
    }

    private void doAssert(String jsonString) {
        Object value = JsonPath.read(jsonString, getJsonPath());
        // 大于
        if (isGreaterThan()) {
            if (isGreaterThan(value)) {
                return;
            } else {
                String msg = "Value expected greaterThan to be '%s', but found '%s'";
                throw new IllegalStateException(String.format(msg, getExpectedValue(), objectToString(value)));
            }
        }

        // 大于等于
        if (isGreaterThanOrEqual()) {
            if (isGreaterThanOrEqual(value)) {
                return;
            } else {
                String msg = "Value expected greaterThanOrEqual to be '%s', but found '%s'";
                throw new IllegalStateException(String.format(msg, getExpectedValue(), objectToString(value)));
            }
        }

        // 小于
        if (isLessThan()) {
            if (isLessThan(value)) {
                return;
            } else {
                String msg = "Value expected lessThan to be '%s', but found '%s'";
                throw new IllegalStateException(String.format(msg, getExpectedValue(), objectToString(value)));
            }
        }

        // 小于等于
        if (isLessThanOrEqual()) {
            if (isLessThanOrEqual(value)) {
                return;
            } else {
                String msg = "Value expected lessThanOrEqual to be '%s', but found '%s'";
                throw new IllegalStateException(String.format(msg, getExpectedValue(), objectToString(value)));
            }
        }

        // 包含
        if (isContains()) {
            if (isContains(Objects.toString(value))) {
                return;
            } else {
                String msg = "Value expected contains to be '%s', but found '%s'";
                throw new IllegalStateException(String.format(msg, getExpectedValue(), objectToString(value)));
            }
        }

        if (!isJsonValidationBool()) {
            if (value instanceof JSONArray) {
                JSONArray arrayValue = (JSONArray) value;
                if (arrayValue.isEmpty() && !JsonPath.isPathDefinite(getJsonPath())) {
                    throw new IllegalStateException("JSONPath is indefinite and the extracted Value is an empty Array." + " Please use an assertion value, to be sure to get a correct result. " + getExpectedValue());
                }
            }
            return;
        }

        if (value instanceof JSONArray) {
            if (arrayMatched((JSONArray) value)) {
                return;
            }
        } else {
            if ((isExpectNull() && value == null) || isEquals(value)) {
                return;
            }
        }

        if (isExpectNull()) {
            throw new IllegalStateException(String.format("Value expected to be null, but found '%s'", value));
        } else {
            String msg;
            if (isUseRegex()) {
                msg = "Value expected to match regexp '%s', but it did not match: '%s'";
            } else {
                msg = "Value expected to be '%s', but found '%s'";
            }
            throw new IllegalStateException(String.format(msg, getExpectedValue(), objectToString(value)));
        }
    }

    private boolean arrayMatched(JSONArray value) {
        if (value.isEmpty() && "[]".equals(getExpectedValue())) {
            return true;
        }

        for (Object subj : value.toArray()) {
            if ((subj == null && isExpectNull()) || isEquals(subj)) {
                return true;
            }
        }

        return isEquals(value);
    }

    private boolean isEquals(Object subj) {
        if (isUseRegex()) {
            String str = objectToString(subj);
            if (USE_JAVA_REGEX) {
                return JMeterUtils.compilePattern(getExpectedValue()).matcher(str).matches();
            } else {
                Pattern pattern = JMeterUtils.getPatternCache().getPattern(getExpectedValue());
                return JMeterUtils.getMatcher().matches(str, pattern);
            }
        } else {
            Object expected = JSONValue.parse(getExpectedValue());
            return Objects.equals(expected, subj);
        }
    }

    /**
     * 大于
     * 支持整数，小数
     *
     * @param subj 实际结果
     * @return boolean
     */
    private boolean isGreaterThan(Object subj) {
        return getCompareResult(subj) > 0;
    }

    /**
     * 大于等于
     * 支持整数，小数
     *
     * @param subj 实际结果
     * @return boolean
     */
    private boolean isGreaterThanOrEqual(Object subj) {
        return getCompareResult(subj) >= 0;
    }

    /**
     * 小于
     * 支持整数，小数
     *
     * @param subj 实际结果
     * @return boolean
     */
    private boolean isLessThan(Object subj) {
        return getCompareResult(subj) < 0;
    }

    /**
     * 小于等于
     * 支持整数，小数
     *
     * @param subj 实际结果
     * @return boolean
     */
    private boolean isLessThanOrEqual(Object subj) {
        return getCompareResult(subj) <= 0;
    }

    /**
     * 包含
     * 支持字符串
     *
     * @param subj 实际结果
     * @return boolean
     */
    private boolean isContains(Object subj) {
        return StringUtils.contains(Objects.toString(subj), getExpectedValue());
    }

    /**
     * 获取两个数字比较的结果
     *
     * @param subj 实际结果
     * @return >0：大于；=0：等于；<0:小于
     */
    private int getCompareResult(Object subj) {
        String expectedValue = getExpectedValue();
        if (subj == null || StringUtils.isBlank(expectedValue)) {
            String msg = "expectedValue or actualValue is null, expectedValue='%s', actualValue='%s'";
            throw new IllegalStateException(String.format(msg, expectedValue, subj));
        }

        String actualValue = subj.toString();
        if (!validateNumber(actualValue) || !validateNumber(expectedValue)) {
            String msg = "expectedValue or actualValue is not number, expectedValue='%s', actualValue='%s'";
            throw new IllegalStateException(String.format(msg, expectedValue, actualValue));
        }

        return Double.valueOf(actualValue).compareTo(Double.valueOf(expectedValue));
    }

    /**
     * 判断是否是整数或者是小数
     *
     * @param str 目标字符串
     * @return boolean
     */
    private boolean validateNumber(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches("[+-]?[0-9]+(\\.[0-9]{1,10})?");
    }


    @Override
    public AssertionResult getResult(SampleResult samplerResult) {
        AssertionResult result = new AssertionResult(getName());
        String responseData = samplerResult.getResponseDataAsString();
        if (responseData.isEmpty()) {
            return result.setResultForNull();
        }

        result.setFailure(false);
        result.setFailureMessage("");

        if (!isInvert()) {
            try {
                doAssert(responseData);
            } catch (Exception e) {
                log.debug("Assertion failed", e);
                result.setFailure(true);
                result.setFailureMessage(e.getMessage());
            }
        } else {
            try {
                doAssert(responseData);
                result.setFailure(true);
                if (isJsonValidationBool()) {
                    if (isExpectNull()) {
                        result.setFailureMessage("Failed that JSONPath " + getJsonPath() + " not matches null");
                    } else {
                        result.setFailureMessage("Failed that JSONPath " + getJsonPath() + " not matches " + getExpectedValue());
                    }
                } else {
                    result.setFailureMessage("Failed that JSONPath not exists: " + getJsonPath());
                }
            } catch (Exception e) {
                log.debug("Assertion failed, as expected", e);
            }
        }
        return result;
    }

    public static String objectToString(Object subj) {
        String str;
        if (subj == null) {
            str = "null";
        } else if (subj instanceof Map) {
            //noinspection unchecked
            str = new JSONObject((Map<String, ?>) subj).toJSONString();
        } else if (subj instanceof Double || subj instanceof Float) {
            str = decimalFormatter.get().format(subj);
        } else {
            str = subj.toString();
        }
        return str;
    }

    @Override
    public void threadStarted() {
        // nothing to do on thread start
    }

    @Override
    public void threadFinished() {
        decimalFormatter.remove();
    }
}
