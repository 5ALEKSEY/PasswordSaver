package com.ak.core_repo_impl.data.model.db.migration

import androidx.room.migration.Migration

interface IDbMigrationHelper {
    fun getListOfDbMigrations(): List<Migration>
}