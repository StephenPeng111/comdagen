/*
 *  Copyright (c) 2018, salesforce.com, inc.
 *  All rights reserved.
 *  SPDX-License-Identifier: BSD-3-Clause
 *  For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.comdagen.config

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.salesforce.comdagen.RenderConfig


/**
 * Configuration file for a library
 */
@JsonRootName("libraryConfig")
@JsonIgnoreProperties(ignoreUnknown = true)
data class LibraryConfiguration(
    override val initialSeed: Long = 1234,
    val libraryId: String?,
    val contentAssetCount: Int = 3,
    val folders: List<FolderConfiguration> = listOf(),
    @JsonProperty("folderDefaults")
    val defaultFolderConfigs: FolderConfiguration,
    val folderCount: Int = 6,
    @JsonProperty("generateSummaryContentAsset")
    val renderComdagenSummaryContentAsset: Boolean = true,

    val libraries: List<LibraryConfiguration> = listOf(),
    @JsonProperty("contentAssetDefaults")
    val defaultContentAssetConfig: ContentConfiguration,

    @JsonProperty("libraryCount")
    override val elementCount: Int = 3,
    override val outputFilePattern: String = "",
    override val outputDir: String = "libraries",
    override val templateName: String = "library.ftlx"
) : RenderConfig {

    /**
     * Returns the number of content assets that will be generated by this library and its custom libraries.
     */
    fun totalContentAssetCount() =
            /* Number of default generated content assets */ contentAssetCount +
            /* Number of content assets generated by custom libraries */ libraries.sumBy { it.contentAssetCount }

    /**
     * Returns the number of folders that will be rendered in the template.
     */
    fun totalFolderCount(): Int {
        val x =        /* Number of default generated folders */ folderCount +
                /* Number of folders generated by custom libraries */ libraries.sumBy { it.folderCount }
        return if (renderComdagenSummaryContentAsset) x + 1 else x
    }

}