#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# Simple Curl-based script for getting contextual related words suggestions for a single input sentence.
# Example:
#     $ bin/suggest.sh "what is the chance of rain tomorrow?" 5
#     [["rain","snow","rainfall","precipitation","rains","flooding","storms","raining","sunshine","showers"]]
#
# NOTE: You need to have REST server running (see 'start_server.sh' script in the same folder).
#

curl -d "{\"sentences\": [{\"text\": \"$1\", \"indexes\": [$2]}], \"simple\": true, \"limit\": 10}" -H 'Content-Type: application/json' http://localhost:5000/suggestions
