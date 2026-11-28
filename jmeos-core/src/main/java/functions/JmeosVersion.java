/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * The version of JMEOS, under its own name.
 *
 * Each binding answers its own version under its own name, and the version of
 * the MEOS library it links under MEOS's name: {@link GeneratedFunctions#meos_version}
 * and {@link GeneratedFunctions#meos_full_version}, generated from the catalog.
 * JMEOS has no other version routine; these two stand beside them, in the C
 * naming GeneratedFunctions uses.
 */
public final class JmeosVersion {

    /** The resource the build writes the project version into. */
    private static final String RESOURCE = "/jmeos/version.properties";

    private JmeosVersion() {
    }

    /**
     * Returns the version of JMEOS, its name first, as
     * {@link GeneratedFunctions#meos_version} answers MEOS's.
     *
     * @return "JMEOS" and the version of the project the jar is built from
     */
    public static String jmeos_version() {
        Properties properties = new Properties();
        try (InputStream in = JmeosVersion.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("JMEOS is built without " + RESOURCE);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return "JMEOS " + properties.getProperty("version");
    }

    /**
     * Returns the version of JMEOS followed by the string of
     * {@link GeneratedFunctions#meos_full_version}: the MEOS library it links
     * and MEOS's dependencies.
     *
     * @return the version of JMEOS and the versions of MEOS and its dependencies
     */
    public static String jmeos_full_version() {
        return jmeos_version() + ", " + GeneratedFunctions.meos_full_version();
    }
}
