/*
 * Copyright (C) 2022 Dremio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projectnessie.versioned.transfer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.immutables.value.Value;

/** Nessie importer using data from a ZIP file. */
@Value.Immutable
public abstract class ZipArchiveImporter extends AbstractNessieImporter {
  public static Builder builder() {
    return ImmutableZipArchiveImporter.builder();
  }

  public interface Builder extends AbstractNessieImporter.Builder<Builder, ZipArchiveImporter> {
    Builder sourceZipFile(Path sourceZipFile);
  }

  abstract Path sourceZipFile();

  @Value.Lazy
  ZipFile zipFile() throws IOException {
    return new ZipFile(sourceZipFile().toFile());
  }

  @Override
  protected InputStream newFileInput(String fileName) throws IOException {
    @SuppressWarnings("resource")
    ZipFile zip = zipFile();
    ZipEntry entry = zip.getEntry(fileName);
    return new BufferedInputStream(zip.getInputStream(entry));
  }
}
