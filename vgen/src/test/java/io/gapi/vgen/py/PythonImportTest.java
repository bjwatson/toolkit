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
package io.gapi.vgen.py;

import com.google.common.truth.Truth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PythonImportHandler} and {@link PythonImport}
 */
@RunWith(JUnit4.class)
public class PythonImportTest {

  @Test
  public void testImport_simple() {
    PythonImport imp = PythonImport.create("foo", PythonImport.ImportType.STDLIB);
    Truth.assertThat(imp.importString()).isEqualTo("import foo");
    Truth.assertThat(imp.shortName()).isEqualTo("foo");
  }

  @Test
  public void testImport_moduleName() {
    PythonImport imp = PythonImport.create("foo", "bar", PythonImport.ImportType.STDLIB);
    Truth.assertThat(imp.importString()).isEqualTo("from foo import bar");
    Truth.assertThat(imp.shortName()).isEqualTo("bar");
  }

  @Test
  public void testImport_localName() {
    PythonImport imp = PythonImport.create("foo", "bar", "baz", PythonImport.ImportType.STDLIB);
    Truth.assertThat(imp.importString()).isEqualTo("from foo import bar as baz");
    Truth.assertThat(imp.shortName()).isEqualTo("baz");
  }

  @Test
  public void testImport_disambiguate_noLocalNameAndNoModuleName() {
    PythonImport imp = PythonImport.create("foo", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("import foo as foo_");
  }

  @Test
  public void testImport_disambiguate_noLocalName() {
    PythonImport imp =
        PythonImport.create("foo", "bar", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from foo import bar as foo_bar");
  }

  @Test
  public void testImport_disambiguate_movePackage() {
    PythonImport imp =
        PythonImport.create("foo", "bar", "baz", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from foo import bar as foo_baz");
  }

  @Test
  public void testImport_disambiguate_movePackage2() {
    PythonImport imp =
        PythonImport.create("a.b.c", "d", "e", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from a.b.c import d as c_e");
  }

  @Test
  public void testImport_disambiguate_moveAnotherPackage() {
    PythonImport imp =
        PythonImport.create("a.b.c", "d", "c_d", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from a.b.c import d as b_c_d");
  }

  @Test
  public void testImport_disambiguate_moveAnotherPackage2() {
    PythonImport imp =
        PythonImport.create("a.b.c", "d", "b_c_d", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from a.b.c import d as a_b_c_d");
  }

  @Test
  public void testImport_disambiguate_moveUnderscorePackage() {
    PythonImport imp =
        PythonImport.create("a.b_c.d", "e", "d_e", PythonImport.ImportType.STDLIB).disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from a.b_c.d import e as b_c_d_e");
  }

  @Test
  public void testImport_disambiguate_mangle() {
    PythonImport imp =
        PythonImport.create("a.b_c.d", "e", "a_b_c_d_e", PythonImport.ImportType.STDLIB)
            .disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from a.b_c.d import e as a_b_c_d_e_");
  }

  @Test
  public void testImport_disambiguate_doubleMangle() {
    PythonImport imp =
        PythonImport.create("a.b_c.d", "e", "a_b_c_d_e_", PythonImport.ImportType.STDLIB)
            .disambiguate();
    Truth.assertThat(imp.importString()).isEqualTo("from a.b_c.d import e as a_b_c_d_e__");
  }
}
