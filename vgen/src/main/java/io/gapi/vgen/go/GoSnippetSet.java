/* Copyright 2016 Google Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gapi.vgen.go;

import com.google.api.tools.framework.snippet.Doc;

/**
 * Entry points for a Go snippet set. Generation is partitioned into a first phase which generates
 * the content of the class without package and imports header, and a second phase which completes
 * the class based on the knowledge of which other classes have been imported.
 */
interface GoSnippetSet<Element> {

  /**
   * Generates the result filename for the generated document
   */
  Doc generateFilename(Element element);

  /**
   * Generates the body of the class for the service interface.
   */
  Doc generateBody(Element element);

  /**
   * Generates the result class, based on the result for the body, and a set of accumulated types to
   * be imported.
   */
  Doc generateClass(Element element, Doc body);
}
