package io.verse.app

import io.verse.storage.core.DataObjectFileAccessor
import io.verse.storage.core.FilableDao

class AppBundleDao(
    name: String,
    path: String,
    accessor: DataObjectFileAccessor?
) : FilableDao<AppBundle>(name = name, path = path, accessor = accessor)