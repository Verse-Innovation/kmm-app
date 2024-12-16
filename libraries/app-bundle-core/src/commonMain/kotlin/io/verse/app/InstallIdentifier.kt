package io.verse.app

import io.tagd.arch.datatype.DataObject

data class InstallIdentifier(
    var appGeneratedId: String? = null,
    var platformLevelId: String? = null,
    var googleAdvertisementId: String? = null,
    var resolvedId: String? = null,
) : DataObject()