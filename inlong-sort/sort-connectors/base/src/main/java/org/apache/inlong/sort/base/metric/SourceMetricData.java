/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.inlong.sort.base.metric;

import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Meter;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.metrics.SimpleCounter;
import org.apache.inlong.audit.AuditImp;
import org.apache.inlong.sort.base.Constants;
import org.apache.inlong.sort.base.metric.MetricOption.RegisteredMetric;

import javax.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.apache.inlong.sort.base.Constants.DELIMITER;
import static org.apache.inlong.sort.base.Constants.NUM_BYTES_IN;
import static org.apache.inlong.sort.base.Constants.NUM_BYTES_IN_FOR_METER;
import static org.apache.inlong.sort.base.Constants.NUM_BYTES_IN_PER_SECOND;
import static org.apache.inlong.sort.base.Constants.NUM_RECORDS_IN;
import static org.apache.inlong.sort.base.Constants.NUM_RECORDS_IN_FOR_METER;
import static org.apache.inlong.sort.base.Constants.NUM_RECORDS_IN_PER_SECOND;

/**
 * A collection class for handling metrics
 */
public class SourceMetricData implements MetricData {

    private MetricGroup metricGroup;
    private Map<String, String> labels;
    private Counter numRecordsIn;
    private Counter numBytesIn;
    private Counter numRecordsInForMeter;
    private Counter numBytesInForMeter;
    private Meter numRecordsInPerSecond;
    private Meter numBytesInPerSecond;
    private AuditImp auditImp;

    public SourceMetricData(MetricOption option, MetricGroup metricGroup) {
        this(option.getLabels(), option.getRegisteredMetric(), metricGroup, option.getIpPorts());
    }

    public SourceMetricData(
            Map<String, String> labels,
            @Nullable RegisteredMetric registeredMetric,
            MetricGroup metricGroup,
            @Nullable String auditHostAndPorts) {
        this.metricGroup = metricGroup;
        this.labels = labels;
        switch (registeredMetric) {
            default:
                registerMetricsForNumRecordsIn();
                registerMetricsForNumBytesIn();
                registerMetricsForNumBytesInPerSecond();
                registerMetricsForNumRecordsInPerSecond();
                break;

        }

        if (auditHostAndPorts != null) {
            AuditImp.getInstance().setAuditProxy(new HashSet<>(Arrays.asList(auditHostAndPorts.split(DELIMITER))));
            this.auditImp = AuditImp.getInstance();
        }
    }

    /**
     * Default counter is {@link SimpleCounter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumRecordsInForMeter() {
        registerMetricsForNumRecordsInForMeter(new SimpleCounter());
    }

    /**
     * User can use custom counter that extends from {@link Counter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumRecordsInForMeter(Counter counter) {
        numRecordsInForMeter = registerCounter(NUM_RECORDS_IN_FOR_METER, counter);
    }

    /**
     * Default counter is {@link SimpleCounter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumBytesInForMeter() {
        registerMetricsForNumBytesInForMeter(new SimpleCounter());
    }

    /**
     * User can use custom counter that extends from {@link Counter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumBytesInForMeter(Counter counter) {
        numBytesInForMeter = registerCounter(NUM_BYTES_IN_FOR_METER, counter);
    }

    /**
     * Default counter is {@link SimpleCounter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumRecordsIn() {
        registerMetricsForNumRecordsIn(new SimpleCounter());
    }

    /**
     * User can use custom counter that extends from {@link Counter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumRecordsIn(Counter counter) {
        numRecordsIn = registerCounter(NUM_RECORDS_IN, counter);
    }

    /**
     * Default counter is {@link SimpleCounter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumBytesIn() {
        registerMetricsForNumBytesIn(new SimpleCounter());
    }

    /**
     * User can use custom counter that extends from {@link Counter}
     * groupId and streamId and nodeId are label value, user can use it filter metric data when use metric reporter
     * prometheus
     */
    public void registerMetricsForNumBytesIn(Counter counter) {
        numBytesIn = registerCounter(NUM_BYTES_IN, counter);
    }

    public void registerMetricsForNumRecordsInPerSecond() {
        numRecordsInPerSecond = registerMeter(NUM_RECORDS_IN_PER_SECOND, this.numRecordsInForMeter);
    }

    public void registerMetricsForNumBytesInPerSecond() {
        numBytesInPerSecond = registerMeter(NUM_BYTES_IN_PER_SECOND, this.numBytesInForMeter);
    }

    public Counter getNumRecordsIn() {
        return numRecordsIn;
    }

    public Counter getNumBytesIn() {
        return numBytesIn;
    }

    public Meter getNumRecordsInPerSecond() {
        return numRecordsInPerSecond;
    }

    public Meter getNumBytesInPerSecond() {
        return numBytesInPerSecond;
    }

    public Counter getNumRecordsInForMeter() {
        return numRecordsInForMeter;
    }

    public Counter getNumBytesInForMeter() {
        return numBytesInForMeter;
    }

    @Override
    public MetricGroup getMetricGroup() {
        return metricGroup;
    }

    @Override
    public Map<String, String> getLabels() {
        return labels;
    }

    public void outputMetricsWithEstimate(Object o) {
        long size = o.toString().getBytes(StandardCharsets.UTF_8).length;
        outputMetrics(1, size);
    }

    public void outputMetrics(long rowCountSize, long rowDataSize) {
        if (numRecordsIn != null) {
            this.numRecordsIn.inc(rowCountSize);
        }

        if (numBytesIn != null) {
            this.numBytesIn.inc(rowDataSize);
        }

        if (numRecordsInForMeter != null) {
            this.numRecordsInForMeter.inc(rowCountSize);
        }

        if (numBytesInForMeter != null) {
            this.numBytesInForMeter.inc(rowDataSize);
        }

        if (auditImp != null) {
            auditImp.add(
                    Constants.AUDIT_SORT_INPUT,
                    getGroupId(),
                    getStreamId(),
                    System.currentTimeMillis(),
                    rowCountSize,
                    rowDataSize);
        }
    }
}
