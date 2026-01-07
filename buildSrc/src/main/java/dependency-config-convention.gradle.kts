configurations.all {
    resolutionStrategy.eachDependency {
        // WS VULNERABILITIES
        ensureMinVersion("com.fasterxml.woodstox", "woodstox-core", "6.4.0")
        ensureMinVersion("com.fasterxml.jackson.core", "jackson-core", "2.15.0")
        ensureMinVersion("com.google.guava", "guava", "32.0.1-jre")
        ensureMinVersion("com.google.protobuf", "protobuf-java", "3.25.5")
        ensureMinVersion("commons-io", "commons-io", "2.14.0")
        ensureMinVersion("io.netty", "netty-codec-http", "4.1.108.Final")
        ensureMinVersion("io.netty", "netty-codec-http2", "4.1.100.Final")
        ensureMinVersion("io.netty", "netty-common", "4.1.118.Final")
        ensureMinVersion("io.netty", "netty-handler", "4.1.118.Final")
        ensureMinVersion("org.apache.commons", "commons-compress", "1.26.0")
        ensureMinVersion("org.json", "json", "20231013")
        ensureMinVersion("xerces", "xercesImpl", "2.12.2")
    }
}

/**
 * Ensures that a dependency has at least the specified minimum version.
 *
 * This function checks the version of a dependency specified by its group and name against a given minimum version.
 * If the requested version is less than the minimum version, it updates the dependency to the minimum version
 * or to a specified override coordinate.
 *
 * @param group The group of the dependency.
 * @param name The name of the dependency.
 * @param version The minimum version required for the dependency.
 * @param reason The reason for enforcing the minimum version (defaults to "<Outdated>").
 * @param coordinateOverride An optional override for the dependency coordinate. If provided, the dependency will be
 * updated to this coordinate instead of just the version. It should be formatted as `"group.name:artifact-id"` which will then be merge into `"group.name:artifact-id:$version"`
 */
private fun DependencyResolveDetails.ensureMinVersion(
    group: String,
    name: String,
    version: String,
    reason: String = "<Outdated>",
    coordinateOverride: String? = null,
) {
    if (requested.group == group && requested.name == name) {
        println("d: Checking dependency $group:$name:${requested.version} against $version")
        if (requested.version isLessThan version) {
            println(
                "i: Updating dependency $group:$name from ${requested.version} to ${coordinateOverride?.let { "$it:" }.orEmpty()}$version",
            )
            if (coordinateOverride != null) {
                useTarget("$coordinateOverride:$version")
            } else {
                useVersion(version)
            }
            because(reason)
        }
    }
}

/**
 * Determine if a version string is less than another version string.
 *
 * @param other The other version to compare this one to.
 * @return True if this version is less than or equal to other version passed as parameter, false otherwise. Null counts as the smallest version possible.
 */
infix fun String?.isLessThan(other: String?): Boolean =
    try {
        when {
            this == null -> true
            other == null -> false
            else -> VersionNumber.parse(this) < VersionNumber.parse(other)
        }
    } catch (t: Throwable) {
        // fail gracefully and log exception
        println("Exception when resolving dependency number $this against $other")
        t.printStackTrace()
        // worst case scenario report that this is still the most "up-to-date" version
        false
    }

// Gradle is making its VersionNumber class private with version 8.x.x (utils classes were never meant to be used publicly)
// This is slightly paraphrased, but more or less what the logic is currently in 7.x.x
data class VersionNumber(
    val parts: List<Int>,
    val qualifier: String,
) : Comparable<VersionNumber> {
    companion object {
        fun parse(version: String): VersionNumber {
            val parts = version.split('.')
            var lastPart = 0
            var qualifier = ""
            parts.lastOrNull()?.let {
                Regex("(\\d*)(.*)").find(it)?.let { result ->
                    val (digits, str) = result.destructured
                    lastPart = digits.toIntOrNull() ?: 0
                    qualifier = str
                }
            }

            val integerParts = parts.dropLast(1).mapNotNull { it.toIntOrNull() } + lastPart

            return VersionNumber(integerParts, qualifier)
        }
    }

    override fun compareTo(other: VersionNumber): Int {
        val smallerSize = kotlin.math.min(parts.size, other.parts.size)
        (0 until smallerSize).forEach { idx ->
            val thisPart = parts[idx]
            val otherPart = other.parts[idx]
            if (thisPart != otherPart) return thisPart - otherPart
        }
        return if (parts.size != other.parts.size) {
            parts.size - other.parts.size
        } else {
            qualifier.lowercase().compareTo(other.qualifier.lowercase())
        }
    }
}
