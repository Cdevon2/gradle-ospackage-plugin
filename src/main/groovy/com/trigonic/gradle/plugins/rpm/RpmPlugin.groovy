/*
 * Copyright 2011 the original author or authors.
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

package com.trigonic.gradle.plugins.rpm

import com.trigonic.gradle.plugins.packaging.CommonPackagingPlugin
import org.freecompany.redline.Builder
import org.freecompany.redline.header.Architecture
import org.freecompany.redline.header.Flags
import org.freecompany.redline.header.Os
import org.freecompany.redline.header.RpmType
import org.freecompany.redline.payload.Directive
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.ConventionMapping
import org.gradle.api.internal.IConventionAware

class RpmPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.plugins.apply(CommonPackagingPlugin.class)

        project.ext.Rpm = Rpm.class

        Builder.metaClass.getDefaultSourcePackage() {
            format.getLead().getName() + "-src.rpm"
        }

        Directive.metaClass.or = { Directive other ->
            new Directive(delegate.flag | other.flag)
        }

        // Some defaults, if not set by the user
        project.tasks.withType(Rpm) { Rpm task ->
            // TODO Expose in parent extension, which might conflict with other formats
            task.aliasEnumValues(Architecture.values())
            task.aliasEnumValues(Os.values())
            task.aliasEnumValues(RpmType.values())
            task.aliasStaticInstances(Directive.class)
            task.aliasStaticInstances(Flags.class, int.class)

            task.applyConventions()
        }

    }
}