/**
 * Copyright (C) 2015 DataTorrent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.benchmark;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import org.apache.hadoop.conf.Configuration;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.api.annotation.ApplicationAnnotation;

/**
 *
 * Application to benchmark the performance of couchbase input operator.
 * The number of tuples processed per second were around 9000.
 *
 * @since 2.0.0
 */
@ApplicationAnnotation(name = "CouchBaseAppInput")
public class CouchBaseAppInput implements StreamingApplication
{

  private final Locality locality = null;

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    CouchBaseInputOperator couchbaseInput = dag.addOperator("couchbaseInput", CouchBaseInputOperator.class);
    //couchbaseInput.getStore().setBucket("default");
    //couchbaseInput.getStore().setPassword("");
    WordCountOperator<String> counter = dag.addOperator("Counter", new WordCountOperator<String>());
    dag.addStream("Generator2Counter", couchbaseInput.outputPort, counter.input).setLocality(locality);
  }

}
