/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nlpcraft.server.nlp.enrichers.ml

import io.opencensus.trace.Span
import org.apache.nlpcraft.common.NCService
import org.apache.nlpcraft.common.config.NCConfigurable
import org.apache.nlpcraft.common.nlp.{NCNlpSentence, NCNlpSentenceNote}
import org.apache.nlpcraft.server.nlp.enrichers.NCServerEnricher

object NCMlEnricher extends NCServerEnricher {
    @volatile private var url: String = _

    private object Config extends NCConfigurable {
        lazy val url: String = getStringOrElse("nlpcraft.server.ml.url", "http://localhost:5000")
    }

    override def start(parent: Span = null): NCService = startScopedSpan("start", parent) { span ⇒
        url = Config.url

        if (url.last == '/')
            url = url.dropRight(1)

        addTags(span, "mklUrl" → url)

        // Tries to access spaCy proxy server.
        // TODO: add health check.

        logger.info(s"ML server connected: $url")

        super.start()
    }

    override def stop(parent: Span): Unit = startScopedSpan("stop", parent) { _ ⇒
        super.stop()
    }

    override def enrich(ns: NCNlpSentence, parent: Span): Unit = {
        val elem = ns.mlData.mlElements.head._1
        val tok = ns(1)

        tok.add(NCNlpSentenceNote(Seq(tok.index), elem))
    }
}
