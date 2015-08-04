/*
 * Copyright (c) 2015 DataTorrent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.lib.appdata.schemas;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import com.datatorrent.lib.appdata.datastructs.DimensionalTable;

/**
 * This class is a schema registry which can hold multiple schemas. It is intended to be
 * used in operators that serve multiple schemas.
 * @since 3.0.0
 */
public class SchemaRegistryMultiple implements SchemaRegistry, Serializable
{
  private static final long serialVersionUID = 201505121108L;

  /**
   * The dimensional table which holds the mapping from schema keys to schemas.
   */
  private DimensionalTable<Schema> table;

  /**
   * Constructor for serialization.
   */
  private SchemaRegistryMultiple()
  {
    //for Kryo
  }

  /**
   * The names of all the schema keys for all schemas in this registry.
   * @param schemaKeys The names of all the schema keys for all schemas in this registry.
   */
  public SchemaRegistryMultiple(List<String> schemaKeys)
  {
    table = new DimensionalTable<Schema>(schemaKeys);
  }

  @Override
  public SchemaResult getSchemaResult(SchemaQuery schemaQuery)
  {
    Map<String, String> schemaKeys = schemaQuery.getSchemaKeys();
    List<Schema> data = null;

    if(schemaKeys == null) {
      data = table.getAllDataPoints();
    }
    else {
      data = table.getDataPoints(schemaKeys);
    }

    if(data.isEmpty()) {
      return null;
    }

    return new SchemaResult(schemaQuery, data);
  }

  @Override
  public void registerSchema(Schema schema)
  {
    Map<String, String> schemaKeys = schema.getSchemaKeys();
    table.appendRow(schema, schemaKeys);
  }

  @Override
  public Schema getSchema(Map<String, String> schemaKeys)
  {
    return table.getDataPoint(schemaKeys);
  }

  @Override
  public void registerSchema(Schema schema, Map<String, String> schemaKeys)
  {
    schema.setSchemaKeys(schemaKeys);
    table.appendRow(schema, schemaKeys);
  }

  @Override
  public int size()
  {
    return table.size();
  }
}
